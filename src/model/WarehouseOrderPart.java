package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class WarehouseOrderPart implements Serializable {
	
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
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrderPart readEntity(Entity entity) {
		WarehouseOrderPart warehouseOrderPart = new WarehouseOrderPart();
		warehouseOrderPart.thisEntity = entity;
		return EntityHelper.readIt(entity, warehouseOrderPart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrderPart readEntity(Key key) {
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
	
	private static List<WarehouseOrderPart> readList(List<Entity> listToRead) {
		List<WarehouseOrderPart> newList =  new ArrayList<WarehouseOrderPart>();
		
		for (Entity entity : listToRead) {
			newList.add(readEntity(entity));
		}
		
		return newList;
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
			if (this.warehouseOrderID != null) {
				warehouseOrder = WarehouseOrder.readEntity(this.warehouseOrderID);
			}
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
			if (this.sparePartID != null) {
				sparePart = SparePart.readEntity(this.sparePartID);
			}
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
	
	private static PreparedQuery getPreparedQueryAll(Key warehouseOrderID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarehouseOrderPart.class.getSimpleName()).
					   setAncestor(warehouseOrderID).
				       addSort("__key__"));
	}
	
	public static List<WarehouseOrderPart> queryGetAll(int offset, int count, Key warehouseOrderID) {
		List<Entity> oldList = getPreparedQueryAll(warehouseOrderID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key warehouseOrderID) {
		return getPreparedQueryAll(warehouseOrderID).countEntities(FetchOptions.Builder.withLimit(10000));
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
