package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

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
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "autoservice"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"paymentNumber"}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
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
	}
	
	public Autoservice getAutoservice() {
		if (autoservice == null) {
			autoservice = Autoservice.readEntity(this.autoserviceID);
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
	
}

/*
CREATE TABLE Spare_Parts_Delivery ( 
	Spare_Parts_Delivery_ID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Document_Number VARCHAR(50) NOT NULL,
	Document_Date DATE NOT NULL,
	Delivery_Date DATE NOT NULL,
	Status SMALLINT NOT NULL,
	Payment_Number VARCHAR(30)
);

ALTER TABLE Spare_Parts_Delivery ADD CONSTRAINT PK_Spare_Parts_Delivery 
	PRIMARY KEY (Spare_Parts_Delivery_ID);
*/

