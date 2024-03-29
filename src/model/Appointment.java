package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;

import model.util.EntityHelper;
import model.util.LimitedString;


@SuppressWarnings("serial")
public class Appointment implements Serializable {
	
	//TODO - makeEntity � ��������� �� �� �������� private ???
	
	/* TODO - ���� �� �� ������ ���� � ���� ��� Entity-�� ???
	 * ������ � ��� ���� �� �� ����� ������, �� �� �� ������� �������� �� Entity-�� ��� �����
	 */
	private Entity thisEntity;
	private Autoservice autoservice;
	
	private Key autoserviceID;
	private LimitedString issuedCode = new LimitedString(30); 
	private LimitedString clientName = new LimitedString(100); 
	private Date timestamp;
	private LimitedString phoneNumber = new LimitedString(15);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "autoservice"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] { "clientName", "phoneNumber" }));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public void writeToDB(boolean makeNew) {
		if (makeNew) {
			thisEntity = null;
		}
		writeToDB();
	}
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Appointment readEntity(Entity entity) {
		Appointment appointment = new Appointment();
		appointment.thisEntity = entity;
		return EntityHelper.readIt(entity, appointment, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Appointment readEntity(Key key) {
		Entity entity;
		if (key == null) {
			throw new NullPointerException("Argument \"key\" is null!");
		}
		
		try {
			entity = DatastoreServiceFactory.getDatastoreService().get(key);
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Entity with key " + key.toString() + " was not found!");
		}
		
		return readEntity(entity);
	}
	
	private static List<Appointment> readList(List<Entity> listToRead) {
		List<Appointment> newList =  new ArrayList<Appointment>();
		
		for (Entity entity : listToRead) {
			newList.add(readEntity(entity));
		}
		
		return newList;
	}
	
	public Key getID() {
		if (thisEntity == null) {
			throw new RuntimeException("There is no entity loaded! Maybe you should call makeEntity() first.");
		}
		return thisEntity.getKey();
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
		autoservice = null;
	}
	
	public Autoservice getAutoservice() {
		if (autoservice == null) {
			if (this.autoserviceID != null) {
				autoservice = Autoservice.readEntity(this.autoserviceID);
			}
		}
		
		return autoservice;
	}
	
	public void setAutoservice(Autoservice autoservice) {
		this.autoservice = autoservice;
		
		if (autoservice == null) {
			autoserviceID = null;
		} else {
			autoserviceID = autoservice.getID();
		}
	}

	public String getIssuedCode() {
		return issuedCode.getString();
	}

	public void setIssuedCode(String issuedCode) {
		this.issuedCode.setString(issuedCode);
	}

	public String getClientName() {
		return clientName.getString();
	}

	public void setClientName(String clientName) {
		this.clientName.setString(clientName);
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getPhoneNumber() {
		return phoneNumber.getString();
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.setString(phoneNumber);
	}
	
	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Appointment.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryForDay(Key autoserviceID, Date day) {
		Date dayStart = new Date(day.getTime());
		Date dayEnd = new Date(day.getTime() + 24*60*60*1000); // + 1 day
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Appointment.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("timestamp").
				       setFilter(CompositeFilterOperator.and(
				    		   new Query.FilterPredicate("timestamp", FilterOperator.GREATER_THAN_OR_EQUAL, dayStart),
				    		   new Query.FilterPredicate("timestamp", FilterOperator.LESS_THAN_OR_EQUAL, dayEnd))));
	}
	
	public static List<Appointment> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Appointment> queryGetForDay(int offset, int count, Key autoserviceID, Date day) {
		List<Entity> oldList = getPreparedQueryForDay(autoserviceID, day).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetForDay(Key autoserviceID, Date day) {
		return getPreparedQueryForDay(autoserviceID, day).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Appointment ( 
	Appointment_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Autoservice_ID BIGINT NOT NULL,
	Issued_Code VARCHAR(30) NOT NULL,
	Client_Name VARCHAR(100) NOT NULL,
	Appointment_Timestamp TIMESTAMP NOT NULL,
	Phone_Number VARCHAR(15) NOT NULL
);

ALTER TABLE Appointment ADD CONSTRAINT PK_Appointment 
	PRIMARY KEY (Appointment_ID);


ALTER TABLE Appointment ADD CONSTRAINT FK_Appointment_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);
*/
