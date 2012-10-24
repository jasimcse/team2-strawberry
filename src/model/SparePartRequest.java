package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class SparePartRequest implements Serializable {
	
	public static final String NEW = "1";
	public static final String ORDERED = "2";
	public static final String COMPLETED = "3";
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private ClientOrder clientOrder;
	private SparePart sparePart;
	
	private Key autoserviceID;
	private Key clientOrderID;
	private Key sparePartID;
	private Double quantity;
	private Double quantityDelivered;
	private LimitedString status = new LimitedString(1, true);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "NEW", "COMPLETED",
					      "thisEntity", "clientOrder", "sparePart"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public void writeToDB() {
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
	
	public static SparePartRequest readEntity(Entity entity) {
		SparePartRequest sparePartRequest = new SparePartRequest();
		sparePartRequest.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartRequest, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartRequest readEntity(Key key) {
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
	
	private static List<SparePartRequest> readList(List<Entity> listToRead) {
		List<SparePartRequest> newList =  new ArrayList<SparePartRequest>();
		
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

	public Key getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(Key clientOrderID) {
		this.clientOrderID = clientOrderID;
	}
	
	public ClientOrder getClientOrder() {
		if (clientOrder == null) {
			if (this.clientOrderID != null) {
				clientOrder = ClientOrder.readEntity(this.clientOrderID);
			}
		}
		
		return clientOrder;
	}
	
	public void setClientOrder(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
		
		if (clientOrder == null) {
			clientOrderID = null;
		} else {
			clientOrderID = clientOrder.getID();
		}
	}

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
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

	public double getQuantity() {
		if (quantity == null) {
			return 0;
		}
		return quantity.doubleValue();
	}

	public void setQuantity(double quantity) {
		this.quantity = Double.valueOf(quantity);
	}
	
	public double getQuantityDelivered() {
		if (quantityDelivered == null) {
			return 0;
		}
		return quantityDelivered.doubleValue();
	}

	public void setQuantityDelivered(double quantityDelivered) {
		this.quantityDelivered = Double.valueOf(quantityDelivered);
	}
	
	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		if (NEW.equals(status) ||ORDERED.equals(status) || COMPLETED.equals(status)) {
			this.status.setString(status);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}
	
	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartRequest.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByStatus(Key autoserviceID, String status) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartRequest.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("status", FilterOperator.EQUAL, status)));
	}
	
	public static List<SparePartRequest> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePartRequest> queryGetByStatus(String status, int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryByStatus(autoserviceID, status).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByStatus(Key autoserviceID, String status) {
		return getPreparedQueryByStatus(autoserviceID, status).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Spare_Part_Request ( 
	Client_Order_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL,
	Quantity_Delivered FLOAT NOT NULL,
	Status SMALLINT NOT NULL
);

ALTER TABLE Spare_Part_Request ADD CONSTRAINT PK_Spare_Part_Request 
	PRIMARY KEY (Client_Order_ID, Spare_Part_ID);


ALTER TABLE Spare_Part_Request ADD CONSTRAINT FK_Spare_Part_Request_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Spare_Part_Request ADD CONSTRAINT FK_Spare_Part_Request_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
