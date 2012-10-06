package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	private static List<DiagnosisService> readList(List<Entity> listToRead) {
		List<DiagnosisService> newList =  new ArrayList<DiagnosisService>();
		
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
	
	private static PreparedQuery getPreparedQueryAll(Key diagnosisID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(DiagnosisService.class.getSimpleName()).
					   setAncestor(diagnosisID).
				       addSort("__key__"));
	}
	
	public static List<DiagnosisService> queryGetAll(int offset, int count, Key diagnosisID) {
		List<Entity> oldList = getPreparedQueryAll(diagnosisID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key diagnosisID) {
		return getPreparedQueryAll(diagnosisID).countEntities(FetchOptions.Builder.withLimit(10000));
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
