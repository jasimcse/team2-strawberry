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
public class WarehouseOrderPartDelivery implements Serializable {
	
	private Entity thisEntity;
	private SparePartsDelivery sparePartsDelivery;
	private WarehouseOrder warehouseOrder;
	private SparePart sparePart;
	
	private Key sparePartsDeliveryID;
	private Key warehouseOrderID;
	private Key sparePartID;
	private Double quantity;
	private Double price;
	
	private static final String PARENT_FIELD = "sparePartsDeliveryID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "thisEntity", "sparePartsDelivery", "warehouseOrder", "sparePart"}));
	
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
	
	public void writeToDB(boolean makeNew) {
		if (makeNew) {
			thisEntity = null;
		}
		writeToDB();
	}
	
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
	
	private static List<WarehouseOrderPartDelivery> readList(List<Entity> listToRead) {
		List<WarehouseOrderPartDelivery> newList =  new ArrayList<WarehouseOrderPartDelivery>();
		
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
	
	public Key getSparePartsDeliveryID() {
		return sparePartsDeliveryID;
	}

	public void setSparePartsDeliveryID(Key sparePartsDeliveryID) {
		this.sparePartsDeliveryID = sparePartsDeliveryID;
		sparePartsDelivery = null;
	}
	
	public SparePartsDelivery getSparePartsDelivery() {
		if (sparePartsDelivery == null) {
			if (this.sparePartsDeliveryID != null) {
				sparePartsDelivery = SparePartsDelivery.readEntity(this.sparePartsDeliveryID);
			}
		}
		
		return sparePartsDelivery;
	}
	
	public void setSparePartsDelivery(SparePartsDelivery sparePartsDelivery) {
		this.sparePartsDelivery = sparePartsDelivery;
		
		if (sparePartsDelivery == null) {
			sparePartsDeliveryID = null;
		} else {
			sparePartsDeliveryID = sparePartsDelivery.getID();
		}
	}

	public Key getWarehouseOrderID() {
		return warehouseOrderID;
	}

	public void setWarehouseOrderID(Key warehouseOrderID) {
		this.warehouseOrderID = warehouseOrderID;
		warehouseOrder = null;
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
		sparePart = null;
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

	public double getQuantity() {
		return quantity.doubleValue();
	}

	public void setQuantity(double quantity) {
		this.quantity = Double.valueOf(quantity);
	}
	
	public double getPrice() {
		return price.doubleValue();
	}

	public void setPrice(double price) {
		this.price = Double.valueOf(price);
	}

	private static PreparedQuery getPreparedQueryAll(Key sparePartDeliveryID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarehouseOrderPartDelivery.class.getSimpleName()).
					   setAncestor(sparePartDeliveryID).
				       addSort("__key__"));
	}
	
	public static List<WarehouseOrderPartDelivery> queryGetAll(int offset, int count, Key sparePartDeliveryID) {
		List<Entity> oldList = getPreparedQueryAll(sparePartDeliveryID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key sparePartDeliveryID) {
		return getPreparedQueryAll(sparePartDeliveryID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*	
CREATE TABLE Warehouse_Order_Part_Delivery ( 
	Spare_Parts_Delivery_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Warehouse_Order_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL,
	Price FLOAT NOT NULL
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
