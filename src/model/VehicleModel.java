package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public class VehicleModel {
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key vehicleModelParentID;
	private LimitedString brand = new LimitedString(50);
	private LimitedString model = new LimitedString(50);
	private Text characteristics;
	
	private static final String PARENT_FIELD = "vehicleParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"characteristics"}));
	
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
		return characteristics.getValue();
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = new Text(characteristics);
	}
	
}

/*
CREATE TABLE Vehicle_Model ( 
	Vehicle_Model_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Brand VARCHAR(50) NOT NULL,
	Model VARCHAR(50) NOT NULL,
	Characteristics CLOB
);

ALTER TABLE Vehicle_Model ADD CONSTRAINT PK_Vehicle_Model 
	PRIMARY KEY (Vehicle_Model_ID);
*/

