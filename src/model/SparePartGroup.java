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

public class SparePartGroup {
	
	private Entity thisEntity;
	
	private LimitedString description = new LimitedString(100);
	
	@SuppressWarnings("unused")
	private Key sparePartGroupParentID;
	private static final String PARENT_FIELD = "sparePartGroupParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		sparePartGroupParentID = EntityHelper.getSparePartGroupParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartGroup readEntity(Entity entity) {
		SparePartGroup sparePartGroup = new SparePartGroup();
		sparePartGroup.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartGroup, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartGroup readEntity(Key key) {
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
	
}

/*
CREATE TABLE Spare_Part_Group ( 
	Spare_Part_Group_ID BIGINT NOT NULL,
	Description VARCHAR(100) NOT NULL
);

ALTER TABLE Spare_Part_Group ADD CONSTRAINT PK_Spare_Part_Group 
	PRIMARY KEY (Spare_Part_Group_ID);
*/

