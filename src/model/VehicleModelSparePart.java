package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class VehicleModelSparePart {
	
	private Key vehicleModelID;
	private Key sparePartID;
	
	
	private static final String PARENT_FIELD = "vehicleModelID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static VehicleModelSparePart readEntity(Entity entity) {
		VehicleModelSparePart vehicleModelSparePart = new VehicleModelSparePart();
		return EntityHelper.readIt(entity, vehicleModelSparePart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getVehicleModelID() {
		return vehicleModelID;
	}

	public void setVehicleModelID(Key vehicleModelID) {
		this.vehicleModelID = vehicleModelID;
	}

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
	}
	
}

/*
CREATE TABLE Vehicle_Model_Spare_Part ( 
	Spare_Part_ID BIGINT NOT NULL,
	Vehicle_Model_ID BIGINT NOT NULL
);

ALTER TABLE Vehicle_Model_Spare_Part ADD CONSTRAINT PK_Vehicle_Model_Spare_Part 
	PRIMARY KEY (Spare_Part_ID, Vehicle_Model_ID);


ALTER TABLE Vehicle_Model_Spare_Part ADD CONSTRAINT FK_Vehicle_Model_Spare_Part_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);

ALTER TABLE Vehicle_Model_Spare_Part ADD CONSTRAINT FK_Vehicle_Model_Spare_Part_Vehicle_Model 
	FOREIGN KEY (Vehicle_Model_ID) REFERENCES Vehicle_Model (Vehicle_Model_ID);
*/
