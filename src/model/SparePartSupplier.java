package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;


@SuppressWarnings("serial")
public class SparePartSupplier implements Serializable {

	private Entity thisEntity;
	
	private Supplier supplier;
	private SparePart sparePart;
	
	private Key supplierID;
	private Key sparePartID;
	private Double deliveryPrice;
	private LimitedString foreignID = new LimitedString(50);
	
	private static final String PARENT_FIELD = "supplierID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "thisEntity", "supplier", "sparePart"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
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
	
	public static SparePartSupplier readEntity(Entity entity) {
		SparePartSupplier sparePartSupplier = new SparePartSupplier();
		sparePartSupplier.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartSupplier, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartSupplier readEntity(Key key) {
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
	
	private static List<SparePartSupplier> readList(List<Entity> listToRead) {
		List<SparePartSupplier> newList =  new ArrayList<SparePartSupplier>();
		
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
	
	public Key getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(Key supplierID) {
		this.supplierID = supplierID;
		supplier = null;
	}
	
	public Supplier getSupplier() {
		if (supplier == null) {
			if (this.supplierID != null) {
				supplier = Supplier.readEntity(this.supplierID);
			}
		}
		
		return supplier;
	}
	
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
		
		if (supplier == null) {
			supplierID = null;
		} else {
			supplierID = supplier.getID();
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
	
	public double getDeliveryPrice() {
		if (deliveryPrice == null) {
			return 0;
		}
		return deliveryPrice.doubleValue();
	}

	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = Double.valueOf(deliveryPrice);
	}
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}
	
	private static PreparedQuery getPreparedQueryAll(Key supplierID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartSupplier.class.getSimpleName()).
					   setAncestor(supplierID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(Key supplierID, String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartSupplier.class.getSimpleName()).
					   setAncestor(supplierID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	private static PreparedQuery getPreparedQueryBySparePartID(Key supplierID, Key sparePartID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartSupplier.class.getSimpleName()).
					   setAncestor(supplierID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("sparePartID", FilterOperator.EQUAL, sparePartID)));
	}
	
	public static List<SparePartSupplier> queryGetAll(int offset, int count, Key supplierID) {
		List<Entity> oldList = getPreparedQueryAll(supplierID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key supplierID) {
		return getPreparedQueryAll(supplierID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePartSupplier> queryGetByForeignID(String foreignID, int offset, int count, Key supplierID) {
		List<Entity> oldList = getPreparedQueryByForeignID(supplierID, foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID, Key supplierID) {
		return getPreparedQueryByForeignID(supplierID, foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePartSupplier> queryGetBySparePartID(Key sparePartID, int offset, int count, Key supplierID) {
		List<Entity> oldList = getPreparedQueryBySparePartID(supplierID, sparePartID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetBySparePartID(Key sparePartID, Key supplierID) {
		return getPreparedQueryBySparePartID(supplierID, sparePartID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Spare_Part_Supplier ( 
	Supplier_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Delivery_Price FLOAT NOT NULL,
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Spare_Part_Supplier ADD CONSTRAINT PK_Spare_Part_Supplier 
	PRIMARY KEY (Spare_Part_ID, Supplier_ID);


ALTER TABLE Spare_Part_Supplier ADD CONSTRAINT FK_Spare_Part_Supplier_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);

ALTER TABLE Spare_Part_Supplier ADD CONSTRAINT FK_Spare_Part_Supplier_Supplier 
	FOREIGN KEY (Supplier_ID) REFERENCES Supplier (Supplier_ID)*/
