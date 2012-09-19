package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class SparePartReserved {
	
	private Key clientOrderID;
	private Key sparePartID;
	private Double quantity;
	
	private static final String PARENT_FIELD = "clientOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartReserved readEntity(Entity entity) {
		SparePartReserved sparePartReserved = new SparePartReserved();
		return EntityHelper.readIt(entity, sparePartReserved, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(Key clientOrderID) {
		this.clientOrderID = clientOrderID;
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
CREATE TABLE Spare_Part_Reserved ( 
	Client_Order_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL
);

ALTER TABLE Spare_Part_Reserved ADD CONSTRAINT PK_Spare_Part_Reserved 
	PRIMARY KEY (Spare_Part_ID, Client_Order_ID);


ALTER TABLE Spare_Part_Reserved ADD CONSTRAINT FK_Spare_Part_Reserved_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Spare_Part_Reserved ADD CONSTRAINT FK_Spare_Part_Reserved_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
