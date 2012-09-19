package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;

public class SparePartGroup {
	
	private LimitedString description = new LimitedString(100);
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartGroup readEntity(Entity entity) {
		SparePartGroup sparePartGroup = new SparePartGroup();
		return EntityHelper.readIt(entity, sparePartGroup, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public String getDescription() {
		return description.getString();
	}

	public void setDescription(String description) {
		this.description.setString(description);
	}
	
}

/*
CREATE TABLE Spare_Part_Group ( 
	Spare_Part_Group_ID BIGINT NOT NULL,
	Description VARCHAR(100) NOT NULL
);

ALTER TABLE Spare_Part_Group ADD CONSTRAINT PK_Spare_Part_Group 
	PRIMARY KEY (Spare_Part_Group_ID);
*/

