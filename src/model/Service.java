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

public class Service {
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key serviceParentID;
	private LimitedString description = new LimitedString(100);
	private Double priceHour;
	
	private static final String PARENT_FIELD = "serviceParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
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
	
}

/*
CREATE TABLE Service ( 
	Service_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Description VARCHAR(100) NOT NULL,
	Price_Hour FLOAT NOT NULL
);

ALTER TABLE Service ADD CONSTRAINT PK_Service 
	PRIMARY KEY (Service_ID);
*/

