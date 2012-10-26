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
public class SparePartGroup implements Serializable {
	
	private Entity thisEntity;
	
	private LimitedString description = new LimitedString(100);
	private LimitedString foreignID = new LimitedString(50);
	
	@SuppressWarnings("unused")
	private Key sparePartGroupParentID;
	private static final String PARENT_FIELD = "sparePartGroupParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "UNIQUE_FIELDS",
					      "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	private static final Set<String> UNIQUE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"foreignID"}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		
		EntityHelper.checkUniqueFields(thisEntity, UNIQUE_FIELDS);
		
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public void writeToDB(boolean makeNew) {
		if (makeNew) {
			thisEntity = null;
		}
		writeToDB();
	}
	
	public Entity makeEntity() {
		sparePartGroupParentID = EntityHelper.getSparePartGroupParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartGroup readEntity(Entity entity) {
		SparePartGroup sparePartGroup = new SparePartGroup();
		sparePartGroup.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartGroup, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartGroup readEntity(Key key) {
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
	
	private static List<SparePartGroup> readList(List<Entity> listToRead) {
		List<SparePartGroup> newList =  new ArrayList<SparePartGroup>();
		
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

	public String getDescription() {
		return description.getString();
	}

	public void setDescription(String description) {
		this.description.setString(description);
	}
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartGroup.class.getSimpleName()).
					   setAncestor(EntityHelper.getSparePartGroupParent()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartGroup.class.getSimpleName()).
					   setAncestor(EntityHelper.getSparePartGroupParent()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	public static List<SparePartGroup> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePartGroup> queryGetByForeignID(String foreignID, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByForeignID(foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID) {
		return getPreparedQueryByForeignID(foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Spare_Part_Group ( 
	Spare_Part_Group_ID BIGINT NOT NULL,
	Description VARCHAR(100) NOT NULL,
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Spare_Part_Group ADD CONSTRAINT PK_Spare_Part_Group 
	PRIMARY KEY (Spare_Part_Group_ID);
*/

