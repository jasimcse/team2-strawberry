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

public class SparePart {
	
	private Entity thisEntity;
	private SparePartGroup sparePartGroup;
	
	private Key sparePartGroupID;
	private LimitedString description = new LimitedString(500);
	private Double deliveryPrice;
	private Double salePrice;
	private LimitedString measuringUnit = new LimitedString(100);
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS", "thisEntity", "sparePartGroup"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePart readEntity(Entity entity) {
		SparePart sparePart = new SparePart();
		sparePart.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePart readEntity(Key key) {
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

	public Key getSparePartGroupID() {
		return sparePartGroupID;
	}

	public void setSparePartGroupID(Key sparePartGroupID) {
		this.sparePartGroupID = sparePartGroupID;
	}
	
	public SparePartGroup getSparePartGroup() {
		if (sparePartGroup == null) {
			sparePartGroup = SparePartGroup.readEntity(this.sparePartGroupID);
		}
		
		return sparePartGroup;
	}
	
	public void setSparePartGroup(SparePartGroup sparePartGroup) {
		this.sparePartGroup = sparePartGroup;
		
		if (sparePartGroup == null) {
			sparePartGroupID = null;
		} else {
			sparePartGroupID = sparePartGroup.getID();
		}
	}

	public String getDescription() {
		return description.getString();
	}

	public void setDescription(String description) {
		this.description.setString(description);
	}

	public double getDeliveryPrice() {
		return deliveryPrice.doubleValue();
	}

	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = Double.valueOf(deliveryPrice);
	}

	public double getSalePrice() {
		return salePrice.doubleValue();
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = Double.valueOf(salePrice);
	}

	public String getMeasuringUnit() {
		return measuringUnit.getString();
	}

	public void setMeasuringUnit(String measuringUnit) {
		this.measuringUnit.setString(measuringUnit);
	}
	
}

/*
CREATE TABLE Spare_Part ( 
	Spare_Part_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Spare_Part_Group_ID BIGINT NOT NULL,
	Description VARCHAR(500) NOT NULL,
	Delivery_Price FLOAT NOT NULL,
	Sale_Price FLOAT NOT NULL,
	Measuring_Unit VARCHAR(100) NOT NULL
);

ALTER TABLE Spare_Part ADD CONSTRAINT PK_Spare_Part 
	PRIMARY KEY (Spare_Part_ID);


ALTER TABLE Spare_Part ADD CONSTRAINT FK_Spare_Part_Spare_Part_Group 
	FOREIGN KEY (Spare_Part_Group_ID) REFERENCES Spare_Part_Group (Spare_Part_Group_ID);
*/
