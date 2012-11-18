package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;
import model.util.StringSearchAttribute;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class SparePartsDelivery implements Serializable {
	
	public static final String NOT_PAYED = "1";
	public static final String PAYED = "2";
	
	private Entity thisEntity;
	private Autoservice autoservice;
	
	private Key autoserviceID;
	private LimitedString documentNumber = new LimitedString(50);
	private Date documentDate;
	private Date deliveryDate;
	private LimitedString status = new LimitedString(1, true);
	private LimitedString paymentNumber = new LimitedString(30);
	private Double fullPrice; 
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "UNIQUE_FIELDS",
					      "NOT_PAYED", "PAYED",
					      "thisEntity", "autoservice"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"paymentNumber"}));
	
	private static final Set<String> UNIQUE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"documentNumber"}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		
		EntityHelper.checkUniqueFields(thisEntity, UNIQUE_FIELDS);
		
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
	
	public static SparePartsDelivery readEntity(Entity entity) {
		SparePartsDelivery sparePartsDelivery = new SparePartsDelivery();
		sparePartsDelivery.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartsDelivery, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartsDelivery readEntity(Key key) {
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
	
	private static List<SparePartsDelivery> readList(List<Entity> listToRead) {
		List<SparePartsDelivery> newList =  new ArrayList<SparePartsDelivery>();
		
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

	public String getDocumentNumber() {
		return documentNumber.getString();
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber.setString(documentNumber);
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		if (PAYED.equals(status) || NOT_PAYED.equals(status)) {
			this.status.setString(status);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}

	public String getPaymentNumber() {
		return paymentNumber.getString();
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber.setString(paymentNumber);
	}
	
	public double getFullPrice() {
		if (fullPrice == null) {
			return 0;
		}
		return fullPrice.doubleValue();
	}

	public void setFullPrice(double fullPrice) {
		this.fullPrice = Double.valueOf(fullPrice);
	}

	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartsDelivery.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}

	private static PreparedQuery getPreparedQueryByStatusDate(Key autoserviceID, String status, Date startDate, Date endDate) {
		Filter filter = null;
	
		if (status != null) {
			filter = new Query.FilterPredicate("status", FilterOperator.EQUAL, status);
		}
		
		if (startDate != null) {
			if (filter == null) {
				filter = new Query.FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, startDate);
			} else {
				filter = CompositeFilterOperator.and(
						filter,
						new Query.FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, startDate));
			}
		}
		
		if (endDate != null) {
			if (filter == null) {
				filter = new Query.FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, endDate);
			} else {
				filter = CompositeFilterOperator.and(
						filter,
						new Query.FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, endDate));
			}
		}
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartsDelivery.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("date").
				       setFilter(filter));
	}
	
	public static List<SparePartsDelivery> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}

	public static List<SparePartsDelivery> querySearch(
			Key autoserviceID,
			String supplierName,
			String status,
			Date startDate,
			Date endDate,
			int offset, int count) {
		
		List<StringSearchAttribute> searchStrings = new ArrayList<StringSearchAttribute>();
		List<Entity> oldList;
		if (supplierName != null) {
			List<String> parentList = new ArrayList<String>();
			parentList.add("supplierID");
			searchStrings.add(new StringSearchAttribute("name", supplierName, parentList));
		}
		
		if ((searchStrings.size() > 0) || (status != null) ||
			(startDate != null) || (endDate != null)) {
			oldList = EntityHelper.stringSearchFilter(
					getPreparedQueryByStatusDate(autoserviceID, status, startDate, endDate).asIterator(),
					searchStrings,
					offset, count);
		} else {
			oldList = new ArrayList<Entity>();
		}
			
		return readList(oldList);
	}
	
	public static int countSearch(
			Key autoserviceID,
			String supplierName,
			String status,
			Date startDate,
			Date endDate) {
		
		List<StringSearchAttribute> searchStrings = new ArrayList<StringSearchAttribute>();
		if (supplierName != null) {
			List<String> parentList = new ArrayList<String>();
			parentList.add("supplierID");
			searchStrings.add(new StringSearchAttribute("name", supplierName, parentList));
		}
		
		if ((searchStrings.size() > 0) || (status != null) ||
				(startDate != null) || (endDate != null)) {
			return EntityHelper.stringSearchCount(
					getPreparedQueryByStatusDate(autoserviceID, status, startDate, endDate).asIterator(),
					searchStrings);
		} else {
			return 0;
		}
	}
	
}

/*
CREATE TABLE Spare_Parts_Delivery ( 
	Spare_Parts_Delivery_ID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Document_Number VARCHAR(50) NOT NULL,
	Document_Date DATE NOT NULL,
	Delivery_Date DATE NOT NULL,
	Status SMALLINT NOT NULL,
	Payment_Number VARCHAR(30),
	Full_Price FLOAT NOT NULL
);

ALTER TABLE Spare_Parts_Delivery ADD CONSTRAINT PK_Spare_Parts_Delivery 
	PRIMARY KEY (Spare_Parts_Delivery_ID);
*/

