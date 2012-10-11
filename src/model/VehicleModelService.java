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
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class VehicleModelService implements Serializable {
	
	private Entity thisEntity;
	private VehicleModel vehicleModel;
	private Service service;
	
	private Key vehicleModelID;
	private Key serviceID;
	private Double durationHour;
	private Long monthsToNext;
	private Long milageToNext;
	
	private static final String PARENT_FIELD = "vehicleModelID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "vehicleModel", "service"}));
	
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
	
	public static VehicleModelService readEntity(Entity entity) {
		VehicleModelService vehicleModelService = new VehicleModelService();
		vehicleModelService.thisEntity = entity;
		return EntityHelper.readIt(entity, vehicleModelService, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static VehicleModelService readEntity(Key key) {
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
	
	private static List<VehicleModelService> readList(List<Entity> listToRead) {
		List<VehicleModelService> newList =  new ArrayList<VehicleModelService>();
		
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

	public Key getServiceID() {
		return serviceID;
	}

	public void setServiceID(Key serviceID) {
		this.serviceID = serviceID;
	}
	
	public Service getService() {
		if (service == null) {
			if (this.serviceID != null) {
				service = Service.readEntity(this.serviceID);
			}
		}
		
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
		
		if (service == null) {
			serviceID = null;
		} else {
			serviceID = service.getID();
		}
	}

	public double getDurationHour() {
		if (durationHour == null) {
			return 0;
		}
		return durationHour.doubleValue();
	}

	public void setDurationHour(double durationHour) {
		this.durationHour = Double.valueOf(durationHour);
	}

	public long getMonthsToNext() {
		if (monthsToNext == null) {
			return 0;
		}
		return monthsToNext.longValue();
	}

	public void setMonthsToNext(long monthsToNext) {
		this.monthsToNext = Long.valueOf(monthsToNext);
	}

	public long getMilageToNext() {
		if (milageToNext == null) {
			return 0;
		}
		return milageToNext.longValue();
	}

	public void setMilageToNext(long milageToNext) {
		this.milageToNext = Long.valueOf(milageToNext);
	}
	
	private static PreparedQuery getPreparedQueryAll(Key vehicleModelID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(VehicleModelService.class.getSimpleName()).
					   setAncestor(vehicleModelID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByService(Key vehicleModelID, Key serviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(VehicleModelService.class.getSimpleName()).
					   setAncestor(vehicleModelID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("serviceID", FilterOperator.EQUAL, serviceID)));
	}
	
	public static List<VehicleModelService> queryGetAll(int offset, int count, Key vehicleModelID) {
		List<Entity> oldList = getPreparedQueryAll(vehicleModelID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key vehicleModelID) {
		return getPreparedQueryAll(vehicleModelID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<VehicleModelService> queryGetByService(Key ServiceID, int offset, int count, Key vehicleModelID) {
		List<Entity> oldList = getPreparedQueryByService(vehicleModelID, ServiceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByService(Key serviceID, Key vehicleModelID) {
		return getPreparedQueryByService(vehicleModelID, serviceID).
				countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Vehicle_Model_Service ( 
	Vehicle_Model_ID BIGINT NOT NULL,
	Service_ID BIGINT NOT NULL,
	Duration_Hour FLOAT NOT NULL,
	Months_To_Next INTEGER NOT NULL,
	Milage_To_Next INTEGER NOT NULL
);

ALTER TABLE Vehicle_Model_Service ADD CONSTRAINT PK_Vehicle_Model_Service 
	PRIMARY KEY (Vehicle_Model_ID, Service_ID);


ALTER TABLE Vehicle_Model_Service ADD CONSTRAINT FK_Vehicle_Model_Service_Service 
	FOREIGN KEY (Service_ID) REFERENCES Service (Service_ID);

ALTER TABLE Vehicle_Model_Service ADD CONSTRAINT FK_Vehicle_Model_Service_Vehicle_Model 
	FOREIGN KEY (Vehicle_Model_ID) REFERENCES Vehicle_Model (Vehicle_Model_ID);
*/
