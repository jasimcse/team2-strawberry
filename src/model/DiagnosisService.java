package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
public class DiagnosisService implements Serializable {
	
	private Entity thisEntity;
	private Diagnosis diagnosis;
	private Service service;
	
	private Key diagnosisID;
	private Key serviceID;
	
	private static final String PARENT_FIELD = "diagnosisID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static DiagnosisService readEntity(Entity entity) {
		DiagnosisService diagnosisService = new DiagnosisService();
		diagnosisService.thisEntity = entity;
		return EntityHelper.readIt(entity, diagnosisService, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static DiagnosisService readEntity(Key key) {
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

	public Key getServiceID() {
		return serviceID;
	}

	public void setServiceID(Key serviceID) {
		this.serviceID = serviceID;
	}
	
	public Service getService() {
		if (service == null) {
			if (this.serviceID != null) {
				service = Service.readEntity(this.serviceID);
			}
		}
		
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
		
		if (service == null) {
			serviceID = null;
		} else {
			serviceID = service.getID();
		}
	}
	
}

/*
CREATE TABLE Diagnosis_Service ( 
	Diagnosis_ID BIGINT NOT NULL,
	Service_ID BIGINT NOT NULL
);

ALTER TABLE Diagnosis_Service ADD CONSTRAINT PK_Diagnosis_Service 
	PRIMARY KEY (Diagnosis_ID, Service_ID);


ALTER TABLE Diagnosis_Service ADD CONSTRAINT FK_Diagnosis_Service_Diagnosis 
	FOREIGN KEY (Diagnosis_ID) REFERENCES Diagnosis (Diagnosis_ID);

ALTER TABLE Diagnosis_Service ADD CONSTRAINT FK_Diagnosis_Service_Service 
	FOREIGN KEY (Service_ID) REFERENCES Service (Service_ID);
*/
