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
public class Service implements Serializable {
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key serviceParentID;
	private LimitedString description = new LimitedString(100);
	private Double priceHour;
	private LimitedString foreignID = new LimitedString(50);
	
	private static final String PARENT_FIELD = "serviceParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
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
		serviceParentID = EntityHelper.getServiceParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Service readEntity(Entity entity) {
		Service service = new Service();
		service.thisEntity = entity;
		return EntityHelper.readIt(entity, service, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Service readEntity(Key key) {
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
	
	private static List<Service> readList(List<Entity> listToRead) {
		List<Service> newList =  new ArrayList<Service>();
		
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

	public double getPriceHour() {
		return priceHour.doubleValue();
	}

	public void setPriceHour(double priceHour) {
		this.priceHour = Double.valueOf(priceHour);
	}
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Service.class.getSimpleName()).
					   setAncestor(EntityHelper.getServiceParent()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Service.class.getSimpleName()).
					   setAncestor(EntityHelper.getServiceParent()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	public static List<Service> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Service> queryGetByForeignID(String foreignID, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByForeignID(foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID) {
		return getPreparedQueryByForeignID(foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Service ( 
	Service_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Description VARCHAR(100) NOT NULL,
	Price_Hour FLOAT NOT NULL,
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Service ADD CONSTRAINT PK_Service 
	PRIMARY KEY (Service_ID);
*/

