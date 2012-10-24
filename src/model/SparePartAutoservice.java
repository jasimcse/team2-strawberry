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
public class SparePartAutoservice implements Serializable {
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private SparePart sparePart;
	
	private Key autoserviceID;
	private Key sparePartID;
	private Double quantityAvailable;
	private Double quantityMinimum;
	private Double quantityReserved;
	private Double quantityOrdered;
	private Double quantityBad;
	private Double quantityRequested;
	private Double belowMinimum; // помощно поле = minimum - availabe + requested - ordered
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "autoservice", "sparePart"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public void writeToDB() {
		
		belowMinimum = quantityMinimum - quantityAvailable + quantityRequested - quantityOrdered;
		
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public void writeToDB(boolean makeNew) {
		if (makeNew) {
			thisEntity = null;
		}
		writeToDB();
	}
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartAutoservice readEntity(Entity entity) {
		SparePartAutoservice sparePartAvailable = new SparePartAutoservice();
		sparePartAvailable.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartAvailable, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartAutoservice readEntity(Key key) {
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
	
	private static List<SparePartAutoservice> readList(List<Entity> listToRead) {
		List<SparePartAutoservice> newList =  new ArrayList<SparePartAutoservice>();
		
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

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
		autoservice = null;
	}
	
	public Autoservice getAutoservice() {
		if (autoservice == null) {
			if (this.autoserviceID != null) {
				autoservice = Autoservice.readEntity(this.autoserviceID);
			}
		}
		
		return autoservice;
	}
	
	public void setAutoservice(Autoservice autoservice) {
		this.autoservice = autoservice;
		
		if (autoservice == null) {
			autoserviceID = null;
		} else {
			autoserviceID = autoservice.getID();
		}
	}

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
		sparePart = null;
	}
	
	public SparePart getSparePart() {
		if (sparePart == null) {
			if (this.sparePartID != null) {
				sparePart = SparePart.readEntity(this.sparePartID);
			}
		}
		
		return sparePart;
	}
	
	public void setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
		
		if (sparePart == null) {
			sparePartID = null;
		} else {
			sparePartID = sparePart.getID();
		}
	}

	public double getQuantityAvailable() {
		if (quantityAvailable == null) {
			return 0;
		}
		return quantityAvailable.doubleValue();
	}

	public void setQuantityAvailable(double quantityAvailable) {
		this.quantityAvailable = Double.valueOf(quantityAvailable);
	}
	
	public double getQuantityMinimum() {
		if (quantityMinimum == null) {
			return 0;
		}
		return quantityMinimum.doubleValue();
	}

	public void setQuantityMinimum(double quantityMinimum) {
		this.quantityMinimum = Double.valueOf(quantityMinimum);
	}
	
	public double getQuantityBad() {
		if (quantityBad == null) {
			return 0;
		}
		return quantityBad.doubleValue();
	}

	public void setQuantityBad(double quantityBad) {
		this.quantityBad = Double.valueOf(quantityBad);
	}
	
	public double getQuantityReserved() {
		if (quantityReserved == null) {
			return 0;
		}
		return quantityReserved.doubleValue();
	}

	public void setQuantityReserved(double quantityReserved) {
		this.quantityReserved = Double.valueOf(quantityReserved);
	}
	
	public double getQuantityOrdered() {
		if (quantityOrdered == null) {
			return 0;
		}
		return quantityOrdered.doubleValue();
	}

	public void setQuantityOrdered(double quantityOrdered) {
		this.quantityOrdered = Double.valueOf(quantityOrdered);
	}
	
	public double getQuantityRequested() {
		if (quantityRequested == null) {
			return 0;
		}
		return quantityRequested.doubleValue();
	}

	public void setQuantityRequested(double quantityRequested) {
		this.quantityRequested = Double.valueOf(quantityRequested);
	}
	
	public double getBelowMinimum() {
		if (belowMinimum == null) {
			return 0;
		}
		return belowMinimum;
	}

	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartAutoservice.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryBelowMinimum(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartAutoservice.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("belowMinimum").
				       setFilter(new Query.FilterPredicate("belowMinimum", FilterOperator.GREATER_THAN, Double.valueOf(0))));
	}
	
	private static PreparedQuery getPreparedQueryBySparePartID(Key sparePartID, Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartAutoservice.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("sparePartID", FilterOperator.EQUAL, sparePartID)));
	}
	
	public static List<SparePartAutoservice> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}

	public static List<SparePartAutoservice> queryGetBelowMinimum(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryBelowMinimum(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetBelowMinimum(Key autoserviceID) {
		return getPreparedQueryBelowMinimum(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePartAutoservice> queryGetBySparePartID(Key sparePartID, int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryBySparePartID(sparePartID, autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetBySparePartID(Key sparePartID, Key autoserviceID) {
		return getPreparedQueryBySparePartID(sparePartID, autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Spare_Part_Autoservice ( 
	Spare_Part_ID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	QuantityAvailable FLOAT NOT NULL,
	QuantityMinimum FLOAT NOT NULL,
	QuantityReserved FLOAT NOT NULL,
	QuantityOrdered FLOAT NOT NULL,
	QuantityBad FLOAT NOT NULL,
	QuantityRequested FLOAT NOT NULL
);

ALTER TABLE Spare_Part_Autoservice ADD CONSTRAINT PK_Spare_Part_Available 
	PRIMARY KEY (Spare_Part_ID);


ALTER TABLE Spare_Part_Autoservice ADD CONSTRAINT FK_Spare_Part_Available_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Spare_Part_Autoservice ADD CONSTRAINT FK_Spare_Part_Available_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
