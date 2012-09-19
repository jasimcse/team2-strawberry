package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;

public class Service {
	
	private LimitedString description = new LimitedString(100);
	private Double priceHour;
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Service readEntity(Entity entity) {
		Service service = new Service();
		return EntityHelper.readIt(entity, service, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
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

