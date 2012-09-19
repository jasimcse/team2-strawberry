package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;

public class WarrantyConditions {
	
	private Long months;
	private Long mileage;
	private LimitedString otherConditions = new LimitedString(500);
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"otherConditions"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarrantyConditions readEntity(Entity entity) {
		WarrantyConditions warrantyConditions = new WarrantyConditions();
		return EntityHelper.readIt(entity, warrantyConditions, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public long getMonths() {
		return months.longValue();
	}

	public void setMonths(long months) {
		this.months = Long.valueOf(months);
	}

	public long getMileage() {
		return mileage.longValue();
	}

	public void setMileage(long mileage) {
		this.mileage = Long.valueOf(mileage);
	}

	public String getOtherConditions() {
		return otherConditions.getString();
	}

	public void setOtherConditions(String otherConditions) {
		this.otherConditions.setString(otherConditions);
	}
	
}

/*
CREATE TABLE Warranty_Conditions ( 
	Warranty_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Months INTEGER NOT NULL,
	Mileage INTEGER NOT NULL,
	Other_Conditions VARCHAR(500)
);

ALTER TABLE Warranty_Conditions ADD CONSTRAINT PK_Warranty_Conditions 
	PRIMARY KEY (Warranty_ID);
*/

