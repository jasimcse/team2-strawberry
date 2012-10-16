package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class InsurerRequest implements Serializable {
	
	private Entity thisEntity;
	private Insurer insurer;
	private Vehicle vehicle;
	private Diagnosis diagnosis;
	
	private Key insurerID;
	private Key vehicleID;
	private Date date;
	private Key diagnosisID;
	
	private static final String PARENT_FIELD = "vehicleID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "insurer", "vehicle", "diagnosis"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"diagnosisID"}));
	
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
	
	public static InsurerRequest readEntity(Entity entity) {
		InsurerRequest insurerRequest = new InsurerRequest();
		insurerRequest.thisEntity = entity;
		return EntityHelper.readIt(entity, insurerRequest, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static InsurerRequest readEntity(Key key) {
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
	
	private static List<InsurerRequest> readList(List<Entity> listToRead) {
		List<InsurerRequest> newList =  new ArrayList<InsurerRequest>();
		
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

	public Key getInsurerID() {
		return insurerID;
	}

	public void setInsurerID(Key insurerID) {
		this.insurerID = insurerID;
	}
	
	public Insurer getInsurer() {
		if (insurer == null) {
			if (this.insurerID != null) {
				insurer = Insurer.readEntity(this.insurerID);
			}
		}
		
		return insurer;
	}
	
	public void setInsurer(Insurer insurer) {
		this.insurer = insurer;
		
		if (insurer == null) {
			insurerID = null;
		} else {
			insurerID = insurer.getID();
		}
	}

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public Vehicle getVehicle() {
		if (vehicle == null) {
			if (this.vehicleID != null) {
				vehicle = Vehicle.readEntity(this.vehicleID);
			}
		}
		
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		
		if (vehicle == null) {
			vehicleID = null;
		} else {
			vehicleID = vehicle.getID();
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Key getDiagnosisID() {
		return diagnosisID;
	}

	public void setDiagnosisID(Key diagnosisID) {
		this.diagnosisID = diagnosisID;
	}
	
	public Diagnosis getDiagnosis() {
		if (diagnosis == null) {
			if (this.diagnosisID != null) {
				diagnosis = Diagnosis.readEntity(this.diagnosisID);
			}
		}
		
		return diagnosis;
	}
	
	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
		
		if (diagnosis == null) {
			diagnosisID = null;
		} else {
			diagnosisID = diagnosis.getID();
		}
	}
	
	private static PreparedQuery getPreparedQueryAll(Key vehicleID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(InsurerRequest.class.getSimpleName()).
					   setAncestor(vehicleID).
				       addSort("__key__"));
	}
	
	public static List<InsurerRequest> queryGetAll(int offset, int count, Key vehicleID) {
		List<Entity> oldList = getPreparedQueryAll(vehicleID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key vehicleID) {
		return getPreparedQueryAll(vehicleID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Insurer_Request ( 
	Insurer_ID BIGINT NOT NULL,
	VIN CHAR(17) NOT NULL,
	Date DATE NOT NULL,
	Diagnosis_ID BIGINT
);

ALTER TABLE Insurer_Request ADD CONSTRAINT PK_Insurer_Request 
	PRIMARY KEY (Insurer_ID, VIN, Date);


ALTER TABLE Insurer_Request ADD CONSTRAINT FK_Insurer_Request_Diagnosis 
	FOREIGN KEY (Diagnosis_ID) REFERENCES Diagnosis (Diagnosis_ID);

ALTER TABLE Insurer_Request ADD CONSTRAINT FK_Insurer_Request_Insurer 
	FOREIGN KEY (Insurer_ID) REFERENCES Insurer (Insurer_ID);

ALTER TABLE Insurer_Request ADD CONSTRAINT FK_Insurer_Request_Vehicle 
	FOREIGN KEY (VIN) REFERENCES Vehicle (VIN);
*/
