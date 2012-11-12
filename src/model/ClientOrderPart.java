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
public class ClientOrderPart implements Serializable {
	
	public static final String CLIENT_PAYS = "1";
	public static final String INSURER_PAYS = "2";
	public static final String PRODUCER_PAYS = "3";
	
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_REMOVED = "2";
	
	private Entity thisEntity;
	private ClientOrder clientOrder;
	private SparePart sparePart;
	
	private Key clientOrderID;
	private Key sparePartID;
	private Double quantity;
	private Double priceUnit;
	private LimitedString whoPays = new LimitedString(1, true);
	private LimitedString status = new LimitedString(1, true);
	
	private static final String PARENT_FIELD = "clientOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "CLIENT_PAYS", "INSURER_PAYS", "PRODUCER_PAYS",
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
	
	public static ClientOrderPart readEntity(Entity entity) {
		ClientOrderPart clientOrderPart = new ClientOrderPart();
		clientOrderPart.thisEntity = entity;
		return EntityHelper.readIt(entity, clientOrderPart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrderPart readEntity(Key key) {
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
	
	private static List<ClientOrderPart> readList(List<Entity> listToRead) {
		List<ClientOrderPart> newList =  new ArrayList<ClientOrderPart>();
		
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

	public Key getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(Key clientOrderID) {
		this.clientOrderID = clientOrderID;
		clientOrder = null;
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

	public double getQuantity() {
		return quantity.doubleValue();
	}

	public void setQuantity(double quantity) {
		this.quantity = Double.valueOf(quantity);
	}

	public double getPriceUnit() {
		return priceUnit.doubleValue();
	}

	public void setPriceUnit(double priceUnit) {
		this.priceUnit = Double.valueOf(priceUnit);
	}

	public String getWhoPays() {
		return whoPays.getString();
	}

	public void setWhoPays(String whoPays) {
		if (CLIENT_PAYS.equals(whoPays) || INSURER_PAYS.equals(whoPays) || PRODUCER_PAYS.equals(whoPays)) {
			this.whoPays.setString(whoPays);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}
	
	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		if (STATUS_NORMAL.equals(status) || STATUS_REMOVED.equals(status)) {
			this.status.setString(status);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}
	
	private static PreparedQuery getPreparedQueryAll(Key clientOrderID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(ClientOrderPart.class.getSimpleName()).
					   setAncestor(clientOrderID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByStatus(Key clientOrderID, String status) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(ClientOrderPart.class.getSimpleName()).
					   setAncestor(clientOrderID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("status", FilterOperator.EQUAL, status)));
	}
	
	public static List<ClientOrderPart> queryGetAll(int offset, int count, Key clientOrderID) {
		List<Entity> oldList = getPreparedQueryAll(clientOrderID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key clientOrderID) {
		return getPreparedQueryAll(clientOrderID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<ClientOrderPart> queryGetByStatus(String status, int offset, int count, Key clientOrderID) {
		List<Entity> oldList = getPreparedQueryByStatus(clientOrderID, status).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByStatus(Key clientOrderID, String status) {
		return getPreparedQueryByStatus(clientOrderID, status).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Client_Order_Part ( 
	Client_Order_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL,
	Price_Unit FLOAT NOT NULL,
	Who_Pays SMALLINT NOT NULL,
	Status CHAR(1) NOT NULL
);

ALTER TABLE Client_Order_Part ADD CONSTRAINT PK_Client_Order_Part 
	PRIMARY KEY (Client_Order_ID, Spare_Part_ID);


ALTER TABLE Client_Order_Part ADD CONSTRAINT FK_Client_Order_Part_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Client_Order_Part ADD CONSTRAINT FK_Client_Order_Part_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
