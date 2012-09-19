package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class VehicleModel {
	
	private LimitedString brand = new LimitedString(50);
	private LimitedString model = new LimitedString(50);
	private Text characteristics;
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"characteristics"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static VehicleModel readEntity(Entity entity) {
		VehicleModel vehicleModel = new VehicleModel();
		return EntityHelper.readIt(entity, vehicleModel, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
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

