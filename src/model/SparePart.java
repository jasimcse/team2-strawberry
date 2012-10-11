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
public class SparePart implements Serializable {
	
	private Entity thisEntity;
	private SparePartGroup sparePartGroup;
	
	@SuppressWarnings("unused")
	private Key sparePartParentID;
	private Key sparePartGroupID;
	private LimitedString description = new LimitedString(500);
	private Double deliveryPrice;
	private Double salePrice;
	private LimitedString measuringUnit = new LimitedString(100);
	private LimitedString foreignID = new LimitedString(50);
	
	private static final String PARENT_FIELD = "sparePartParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "sparePartGroup"}));
	
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
		sparePartParentID = EntityHelper.getSparePartParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePart readEntity(Entity entity) {
		SparePart sparePart = new SparePart();
		sparePart.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePart readEntity(Key key) {
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
	
	private static List<SparePart> readList(List<Entity> listToRead) {
		List<SparePart> newList =  new ArrayList<SparePart>();
		
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

	public Key getSparePartGroupID() {
		return sparePartGroupID;
	}

	public void setSparePartGroupID(Key sparePartGroupID) {
		this.sparePartGroupID = sparePartGroupID;
	}
	
	public SparePartGroup getSparePartGroup() {
		if (sparePartGroup == null) {
			if (this.sparePartGroupID != null) {
				sparePartGroup = SparePartGroup.readEntity(this.sparePartGroupID);
			}
		}
		
		return sparePartGroup;
	}
	
	public void setSparePartGroup(SparePartGroup sparePartGroup) {
		this.sparePartGroup = sparePartGroup;
		
		if (sparePartGroup == null) {
			sparePartGroupID = null;
		} else {
			sparePartGroupID = sparePartGroup.getID();
		}
	}

	public String getDescription() {
		return description.getString();
	}

	public void setDescription(String description) {
		this.description.setString(description);
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

	public double getSalePrice() {
		if (salePrice == null) {
			return 0;
		}
		return salePrice.doubleValue();
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = Double.valueOf(salePrice);
	}

	public String getMeasuringUnit() {
		return measuringUnit.getString();
	}

	public void setMeasuringUnit(String measuringUnit) {
		this.measuringUnit.setString(measuringUnit);
	}
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePart.class.getSimpleName()).
					   setAncestor(EntityHelper.getSparePartParent()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePart.class.getSimpleName()).
					   setAncestor(EntityHelper.getSparePartParent()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	public static List<SparePart> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePart> queryGetByForeignID(String foreignID, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByForeignID(foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID) {
		return getPreparedQueryByForeignID(foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Spare_Part ( 
	Spare_Part_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Spare_Part_Group_ID BIGINT NOT NULL,
	Description VARCHAR(500) NOT NULL,
	Delivery_Price FLOAT NOT NULL,
	Sale_Price FLOAT NOT NULL,
	Measuring_Unit VARCHAR(100) NOT NULL,
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Spare_Part ADD CONSTRAINT PK_Spare_Part 
	PRIMARY KEY (Spare_Part_ID);


ALTER TABLE Spare_Part ADD CONSTRAINT FK_Spare_Part_Spare_Part_Group 
	FOREIGN KEY (Spare_Part_Group_ID) REFERENCES Spare_Part_Group (Spare_Part_Group_ID);
*/
