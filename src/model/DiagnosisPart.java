package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;


public class DiagnosisPart {
	
	private Entity thisEntity;
	private Diagnosis diagnosis;
	private SparePart sparePart;
	
	private Key diagnosisID;
	private Key sparePartID;
	private Double quantity;
	
	private static final String PARENT_FIELD = "diagnosisID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "diagnosis", "sparePart"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static DiagnosisPart readEntity(Entity entity) {
		DiagnosisPart diagnosisPart = new DiagnosisPart();
		diagnosisPart.thisEntity = entity;
		return EntityHelper.readIt(entity, diagnosisPart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static DiagnosisPart readEntity(Key key) {
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
			diagnosis = Diagnosis.readEntity(this.diagnosisID);
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

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
	}
	
	public SparePart getSparePart() {
		if (sparePart == null) {
			sparePart = SparePart.readEntity(this.sparePartID);
		}
		
		return sparePart;
	}
	
	public void setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
		
		if (sparePart == null) {
			sparePartID = null;
		} else {
			sparePartID = sparePart.getID();
		}
	}

	public double getQuantity() {
		return quantity.doubleValue();
	}

	public void setQuantity(double quantity) {
		this.quantity = Double.valueOf(quantity);
	}
	
}

/*
CREATE TABLE Diagnosis_Part ( 
	Diagnosis_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL
);

ALTER TABLE Diagnosis_Part ADD CONSTRAINT PK_Diagnosis_Part 
	PRIMARY KEY (Diagnosis_ID, Spare_Part_ID);


ALTER TABLE Diagnosis_Part ADD CONSTRAINT FK_Diagnosis_Part_Diagnosis 
	FOREIGN KEY (Diagnosis_ID) REFERENCES Diagnosis (Diagnosis_ID);

ALTER TABLE Diagnosis_Part ADD CONSTRAINT FK_Diagnosis_Part_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
