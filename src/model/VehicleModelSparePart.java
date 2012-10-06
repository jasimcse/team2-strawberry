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
public class VehicleModelSparePart implements Serializable {
	
	private Entity thisEntity;
	private VehicleModel vehicleModel;
	private SparePart sparePart;
	
	private Key vehicleModelID;
	private Key sparePartID;
	
	
	private static final String PARENT_FIELD = "vehicleModelID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "vehicleModel", "sparePart"}));
	
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
	
	public static VehicleModelSparePart readEntity(Entity entity) {
		VehicleModelSparePart vehicleModelSparePart = new VehicleModelSparePart();
		vehicleModelSparePart.thisEntity = entity;
		return EntityHelper.readIt(entity, vehicleModelSparePart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static VehicleModelSparePart readEntity(Key key) {
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
	
	private static List<VehicleModelSparePart> readList(List<Entity> listToRead) {
		List<VehicleModelSparePart> newList =  new ArrayList<VehicleModelSparePart>();
		
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

	public Key getVehicleModelID() {
		return vehicleModelID;
	}

	public void setVehicleModelID(Key vehicleModelID) {
		this.vehicleModelID = vehicleModelID;
	}
	
	public VehicleModel getVehicleModel() {
		if (vehicleModel == null) {
			if (this.vehicleModelID != null) {
				vehicleModel = VehicleModel.readEntity(this.vehicleModelID);
			}
		}
		
		return vehicleModel;
	}
	
	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
		
		if (vehicleModel == null) {
			vehicleModelID = null;
		} else {
			vehicleModelID = vehicleModel.getID();
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
	
	private static PreparedQuery getPreparedQueryAll(Key vehicleModelID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(VehicleModelSparePart.class.getSimpleName()).
					   setAncestor(vehicleModelID).
				       addSort("__key__"));
	}
	
	public static List<VehicleModelSparePart> queryGetAll(int offset, int count, Key vehicleModelID) {
		List<Entity> oldList = getPreparedQueryAll(vehicleModelID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key vehicleModelID) {
		return getPreparedQueryAll(vehicleModelID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Vehicle_Model_Spare_Part ( 
	Spare_Part_ID BIGINT NOT NULL,
	Vehicle_Model_ID BIGINT NOT NULL
);

ALTER TABLE Vehicle_Model_Spare_Part ADD CONSTRAINT PK_Vehicle_Model_Spare_Part 
	PRIMARY KEY (Spare_Part_ID, Vehicle_Model_ID);


ALTER TABLE Vehicle_Model_Spare_Part ADD CONSTRAINT FK_Vehicle_Model_Spare_Part_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);

ALTER TABLE Vehicle_Model_Spare_Part ADD CONSTRAINT FK_Vehicle_Model_Spare_Part_Vehicle_Model 
	FOREIGN KEY (Vehicle_Model_ID) REFERENCES Vehicle_Model (Vehicle_Model_ID);
*/
