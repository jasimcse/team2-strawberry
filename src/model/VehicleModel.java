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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class VehicleModel implements Serializable {
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key vehicleModelParentID;
	private LimitedString brand = new LimitedString(50);
	private LimitedString model = new LimitedString(50);
	private Text characteristics;
	private LimitedString foreignID = new LimitedString(50);
	
	private static final String PARENT_FIELD = "vehicleModelParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "UNIQUE_FIELDS",
					      "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"characteristics"}));
	
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
		vehicleModelParentID = EntityHelper.getVehicleModelParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static VehicleModel readEntity(Entity entity) {
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.thisEntity = entity;
		return EntityHelper.readIt(entity, vehicleModel, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static VehicleModel readEntity(Key key) {
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
	
	private static List<VehicleModel> readList(List<Entity> listToRead) {
		List<VehicleModel> newList =  new ArrayList<VehicleModel>();
		
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

	public String getBrand() {
		return brand.getString();
	}

	public void setBrand(String brand) {
		this.brand.setString(brand);
	}

	public String getModel() {
		return model.getString();
	}

	public void setModel(String model) {
		this.model.setString(model);
	}

	public String getCharacteristics() {
		if (characteristics == null) {
			return null;
		}
		return characteristics.getValue();
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = new Text(characteristics);
	}
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(VehicleModel.class.getSimpleName()).
					   setAncestor(EntityHelper.getVehicleModelParent()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(VehicleModel.class.getSimpleName()).
					   setAncestor(EntityHelper.getVehicleModelParent()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	public static List<VehicleModel> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<VehicleModel> queryGetByForeignID(String foreignID, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByForeignID(foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID) {
		return getPreparedQueryByForeignID(foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*	
CREATE TABLE Vehicle_Model ( 
	Vehicle_Model_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Brand VARCHAR(50) NOT NULL,
	Model VARCHAR(50) NOT NULL,
	Characteristics CLOB,
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Vehicle_Model ADD CONSTRAINT PK_Vehicle_Model 
	PRIMARY KEY (Vehicle_Model_ID);
*/

