package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class SparePartAvailable {
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private SparePart sparePart;
	
	private Key autoserviceID;
	private Key sparePartID;
	private Double quantity;
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "autoservice", "sparePart"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartAvailable readEntity(Entity entity) {
		SparePartAvailable sparePartAvailable = new SparePartAvailable();
		sparePartAvailable.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartAvailable, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartAvailable readEntity(Key key) {
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
CREATE TABLE Spare_Part_Available ( 
	Spare_Part_ID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL
);

ALTER TABLE Spare_Part_Available ADD CONSTRAINT PK_Spare_Part_Available 
	PRIMARY KEY (Spare_Part_ID);


ALTER TABLE Spare_Part_Available ADD CONSTRAINT FK_Spare_Part_Available_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Spare_Part_Available ADD CONSTRAINT FK_Spare_Part_Available_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
