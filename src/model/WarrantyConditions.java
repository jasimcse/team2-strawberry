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
public class WarrantyConditions implements Serializable {
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key warrantyConditionsParentID;
	private Long months;
	private Long mileage;
	private LimitedString otherConditions = new LimitedString(500);
	private LimitedString foreignID = new LimitedString(50);
	
	private static final String PARENT_FIELD = "warrantyConditionsParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"otherConditions"}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public Entity makeEntity() {
		warrantyConditionsParentID = EntityHelper.getWarrantyConditionsParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarrantyConditions readEntity(Entity entity) {
		WarrantyConditions warrantyConditions = new WarrantyConditions();
		warrantyConditions.thisEntity = entity;
		return EntityHelper.readIt(entity, warrantyConditions, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarrantyConditions readEntity(Key key) {
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
	
	private static List<WarrantyConditions> readList(List<Entity> listToRead) {
		List<WarrantyConditions> newList =  new ArrayList<WarrantyConditions>();
		
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

	public long getMonths() {
		return months.longValue();
	}

	public void setMonths(long months) {
		this.months = Long.valueOf(months);
	}

	public long getMileage() {
		return mileage.longValue();
	}

	public void setMileage(long mileage) {
		this.mileage = Long.valueOf(mileage);
	}

	public String getOtherConditions() {
		return otherConditions.getString();
	}

	public void setOtherConditions(String otherConditions) {
		this.otherConditions.setString(otherConditions);
	}
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}

	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarrantyConditions.class.getSimpleName()).
					   setAncestor(EntityHelper.getWarrantyConditionsParent()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarrantyConditions.class.getSimpleName()).
					   setAncestor(EntityHelper.getWarrantyConditionsParent()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	public static List<WarrantyConditions> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<WarrantyConditions> queryGetByForeignID(String foreignID, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByForeignID(foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID) {
		return getPreparedQueryByForeignID(foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Warranty_Conditions ( 
	Warranty_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Months INTEGER NOT NULL,
	Mileage INTEGER NOT NULL,
	Other_Conditions VARCHAR(500),
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Warranty_Conditions ADD CONSTRAINT PK_Warranty_Conditions 
	PRIMARY KEY (Warranty_ID);
*/

