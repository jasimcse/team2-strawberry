package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class WarehouseOrderPartDelivery {
	
	private Entity thisEntity;
	private WarehouseOrder warehouseOrder;
	private SparePart sparePart;
	
	private Key warehouseOrderID;
	private Key sparePartID;
	private Double orderedQuantity;
	private Double deliveredQuantity;
	
	private static final String PARENT_FIELD = "warehouseOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "warehouseOrder", "sparePart"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrderPartDelivery readEntity(Entity entity) {
		WarehouseOrderPartDelivery warehouseOrderPartDelivery = new WarehouseOrderPartDelivery();
		warehouseOrderPartDelivery.thisEntity = entity;
		return EntityHelper.readIt(entity, warehouseOrderPartDelivery, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrderPartDelivery readEntity(Key key) {
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

	public Key getWarehouseOrderID() {
		return warehouseOrderID;
	}

	public void setWarehouseOrderID(Key warehouseOrderID) {
		this.warehouseOrderID = warehouseOrderID;
	}
	
	public WarehouseOrder getWarehouseOrder() {
		if (warehouseOrder == null) {
			warehouseOrder = WarehouseOrder.readEntity(this.warehouseOrderID);
		}
		
		return warehouseOrder;
	}
	
	public void setWarehouseOrder(WarehouseOrder warehouseOrder) {
		this.warehouseOrder = warehouseOrder;
		
		if (warehouseOrder == null) {
			warehouseOrderID = null;
		} else {
			warehouseOrderID = warehouseOrder.getID();
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
CREATE TABLE Warehouse_Order_Part_Delivery ( 
	Spare_Parts_Delivery_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Warehouse_Order_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL,
	Price_Unit FLOAT NOT NULL
);

ALTER TABLE Warehouse_Order_Part_Delivery ADD CONSTRAINT PK_Warehouse_Order_Part_Delivery 
	PRIMARY KEY (Warehouse_Order_ID, Spare_Part_ID, Spare_Parts_Delivery_ID);


ALTER TABLE Warehouse_Order_Part_Delivery ADD CONSTRAINT FK_Warehouse_Order_Part_Delivery_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);

ALTER TABLE Warehouse_Order_Part_Delivery ADD CONSTRAINT FK_Warehouse_Order_Part_Delivery_Spare_Parts_Delivery 
	FOREIGN KEY (Spare_Parts_Delivery_ID) REFERENCES Spare_Parts_Delivery (Spare_Parts_Delivery_ID);

ALTER TABLE Warehouse_Order_Part_Delivery ADD CONSTRAINT FK_Warehouse_Order_Part_Delivery_Warehouse_Order 
	FOREIGN KEY (Warehouse_Order_ID) REFERENCES Warehouse_Order (Warehouse_Order_ID);
*/
