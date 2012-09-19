package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class SparePartAvailable {
	
	private Key autoserviceID;
	private Key sparePartID;
	private Double quantity;
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartAvailable readEntity(Entity entity) {
		SparePartAvailable sparePartAvailable = new SparePartAvailable();
		return EntityHelper.readIt(entity, sparePartAvailable, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
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
