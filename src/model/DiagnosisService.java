package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;


public class DiagnosisService {
	
	private Key diagnosisID;
	private Key serviceID;
	
	private static final String PARENT_FIELD = "diagnosisID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static DiagnosisService readEntity(Entity entity) {
		DiagnosisService diagnosisService = new DiagnosisService();
		return EntityHelper.readIt(entity, diagnosisService, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getDiagnosisID() {
		return diagnosisID;
	}

	public void setDiagnosisID(Key diagnosisID) {
		this.diagnosisID = diagnosisID;
	}

	public Key getServiceID() {
		return serviceID;
	}

	public void setServiceID(Key serviceID) {
		this.serviceID = serviceID;
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
