package model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

import model.util.EntityHelper;
import model.util.LimitedString;


public class Appointment {
	
	//TODO - makeEntity и подобните да се направят private ???
	
	private Entity thisEntity; // TODO - това да се замени само с ключ към Entity-то ??? 
	private Autoservice autoservice;
	
	private Key autoserviceID;
	private Long issuedCode; 
	private LimitedString clientName = new LimitedString(100); 
	private Date timestamp;
	private LimitedString phoneNumber = new LimitedString(15);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS", "thisEntity", "autoservice"})); // TODO - add PARENT_FIELD and NULLABLE_FIELDS
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] { "clientName", "phoneNumber" }));
	
	// TODO - add writeToDB!
	
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
	}
	
	public Autoservice getAutoservice() {
		if (autoservice == null) {
			autoservice = Autoservice.readEntity(this.autoserviceID);
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

	public long getIssuedCode() {
		return issuedCode.longValue();
	}

	public void setIssuedCode(long issuedCode) {
		this.issuedCode = new Long(issuedCode);
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
	
}

/*
CREATE TABLE Appointment ( 
	Appointment_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Autoservice_ID BIGINT NOT NULL,
	Issued_Code INTEGER NOT NULL,
	Client_Name VARCHAR(100) NOT NULL,
	Appointment_Timestamp TIMESTAMP NOT NULL,
	Phone_Number VARCHAR(15)
);

ALTER TABLE Appointment ADD CONSTRAINT PK_Appointment 
	PRIMARY KEY (Appointment_ID);


ALTER TABLE Appointment ADD CONSTRAINT FK_Appointment_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);
*/
