package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class WarehouseOrderPart {
	
	private Key warehouseOrderID;
	private Key sparePartID;
	private Double orderedQuantity;
	private Double deliveredQuantity;
	
	
	private static final String PARENT_FIELD = "warehouseOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrderPart readEntity(Entity entity) {
		WarehouseOrderPart warehouseOrderPart = new WarehouseOrderPart();
		return EntityHelper.readIt(entity, warehouseOrderPart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getWarehouseOrderID() {
		return warehouseOrderID;
	}

	public void setWarehouseOrderID(Key warehouseOrderID) {
		this.warehouseOrderID = warehouseOrderID;
	}

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
	}

	public double getOrderedQuantity() {
		return orderedQuantity.doubleValue();
	}

	public void setOrderedQuantity(double orderedQuantity) {
		this.orderedQuantity = Double.valueOf(orderedQuantity);
	}

	public double getDeliveredQuantity() {
		return deliveredQuantity.doubleValue();
	}

	public void setDeliveredQuantity(double deliveredQuantity) {
		this.deliveredQuantity = Double.valueOf(deliveredQuantity);
	}
	
}

/*
CREATE TABLE Warehouse_Order_Part ( 
	Warehouse_Order_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Ordered_Quantity FLOAT NOT NULL,
	Delivered_Quantity FLOAT NOT NULL
);

ALTER TABLE Warehouse_Order_Part ADD CONSTRAINT PK_Warehouse_Order_Part 
	PRIMARY KEY (Warehouse_Order_ID, Spare_Part_ID);


ALTER TABLE Warehouse_Order_Part ADD CONSTRAINT FK_Warehouse_Order_Part_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);

ALTER TABLE Warehouse_Order_Part ADD CONSTRAINT FK_Warehouse_Order_Part_Warehouse_Order 
	FOREIGN KEY (Warehouse_Order_ID) REFERENCES Warehouse_Order (Warehouse_Order_ID);
*/
