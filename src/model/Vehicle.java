package model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Vehicle {
	
	private Key clientID;
	private Key vehicleModelID;
	private Key warrantyConditionsID;
	private LimitedString VIN = new LimitedString(17, true);
	private LimitedString engineNumber = new LimitedString(50);
	private LimitedString plateNumber = new LimitedString(8);
	private LimitedString warrantyOK = new LimitedString(1, true);
	private Date purchaseDate;
	
	private static final String PARENT_FIELD = "clientID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"warrantyConditionsID", "warrantyOK", "purchaseDate"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Vehicle readEntity(Entity entity) {
		Vehicle vehicle = new Vehicle();
		return EntityHelper.readIt(entity, vehicle, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getClientID() {
		return clientID;
	}

	public void setClientID(Key clientID) {
		this.clientID = clientID;
	}

	public Key getVehicleModelID() {
		return vehicleModelID;
	}

	public void setVehicleModelID(Key vehicleModelID) {
		this.vehicleModelID = vehicleModelID;
	}

	public Key getWarrantyConditionsID() {
		return warrantyConditionsID;
	}

	public void setWarrantyConditionsID(Key warrantyConditionsID) {
		this.warrantyConditionsID = warrantyConditionsID;
	}

	public String getVIN() {
		return VIN.getString();
	}

	public void setVIN(String VIN) {
		this.VIN.setString(VIN);
	}

	public String getEngineNumber() {
		return engineNumber.getString();
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber.setString(engineNumber);
	}

	public String getPlateNumber() {
		return plateNumber.getString();
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber.setString(plateNumber);
	}

	public String getWarrantyOK() {
		return warrantyOK.getString();
	}

	public void setWarrantyOK(String warrantyOK) {
		this.warrantyOK.setString(warrantyOK);
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
}

/*
CREATE TABLE Vehicle ( 
	VIN CHAR(17) NOT NULL,
	Engine_Number VARCHAR(50) NOT NULL,
	Plate_Number VARCHAR(8) NOT NULL,
	Vehicle_Model_ID BIGINT NOT NULL,
	Warranty_ID BIGINT,
	Warranty_OK SMALLINT,
	Client_ID BIGINT NOT NULL,
	Purchase_Date DATE
);

ALTER TABLE Vehicle ADD CONSTRAINT PK_Vehicle 
	PRIMARY KEY (VIN);


ALTER TABLE Vehicle ADD CONSTRAINT FK_Vehicle_Client 
	FOREIGN KEY (Client_ID) REFERENCES Client (Client_ID);

ALTER TABLE Vehicle ADD CONSTRAINT FK_Vehicle_Vehicle_Model 
	FOREIGN KEY (Vehicle_Model_ID) REFERENCES Vehicle_Model (Vehicle_Model_ID);

ALTER TABLE Vehicle ADD CONSTRAINT FK_Vehicle_Warranty_Conditions 
	FOREIGN KEY (Warranty_ID) REFERENCES Warranty_Conditions (Warranty_ID);
*/
