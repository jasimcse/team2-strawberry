package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class VehicleModelService {
	
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
			vehicleModel = VehicleModel.readEntity(this.vehicleModelID);
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
			service = Service.readEntity(this.serviceID);
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
		return durationHour.doubleValue();
	}

	public void setDurationHour(double durationHour) {
		this.durationHour = Double.valueOf(durationHour);
	}

	public long getMonthsToNext() {
		return monthsToNext.longValue();
	}

	public void setMonthsToNext(long monthsToNext) {
		this.monthsToNext = Long.valueOf(monthsToNext);
	}

	public long getMilageToNext() {
		return milageToNext.longValue();
	}

	public void setMilageToNext(long milageToNext) {
		this.milageToNext = Long.valueOf(milageToNext);
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
