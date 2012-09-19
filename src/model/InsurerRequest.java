package model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class InsurerRequest {
	
	private Key insurerID;
	private Key vehicleID;
	private Date date;
	private Key diagnosisID;
	
	private static final String PARENT_FIELD = "vehicleID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"diagnosisID"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static InsurerRequest readEntity(Entity entity) {
		InsurerRequest insurerRequest = new InsurerRequest();
		return EntityHelper.readIt(entity, insurerRequest, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getInsurerID() {
		return insurerID;
	}

	public void setInsurerID(Key insurerID) {
		this.insurerID = insurerID;
	}

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
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
