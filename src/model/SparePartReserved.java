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
public class SparePartReserved implements Serializable {
	
	private Entity thisEntity;
	private ClientOrder clientOrder;
	private SparePart sparePart;
	
	private Key clientOrderID;
	private Key sparePartID;
	private Double quantity;
	private Double used;
	
	private static final String PARENT_FIELD = "clientOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
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
	
	public static SparePartReserved readEntity(Entity entity) {
		SparePartReserved sparePartReserved = new SparePartReserved();
		sparePartReserved.thisEntity = entity;
		return EntityHelper.readIt(entity, sparePartReserved, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static SparePartReserved readEntity(Key key) {
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
	
	private static List<SparePartReserved> readList(List<Entity> listToRead) {
		List<SparePartReserved> newList =  new ArrayList<SparePartReserved>();
		
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
	
	public double getUsed() {
		return used.doubleValue();
	}

	public void setUsed(double used) {
		this.used = Double.valueOf(used);
	}

	private static PreparedQuery getPreparedQueryAll(Key clientOrderID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartReserved.class.getSimpleName()).
					   setAncestor(clientOrderID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryBySparePart(Key clientOrderID, Key sparePartID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(SparePartReserved.class.getSimpleName()).
					   setAncestor(clientOrderID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("sparePartID", FilterOperator.EQUAL, sparePartID)));
	}
	
	public static List<SparePartReserved> queryGetAll(int offset, int count, Key clientOrderID) {
		List<Entity> oldList = getPreparedQueryAll(clientOrderID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key clientOrderID) {
		return getPreparedQueryAll(clientOrderID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<SparePartReserved> queryGetBySparePart(Key sparePartID, int offset, int count, Key clientOrderID) {
		List<Entity> oldList = getPreparedQueryBySparePart(clientOrderID, sparePartID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetBySaprePart(Key clientOrderID, Key sparePartID) {
		return getPreparedQueryBySparePart(clientOrderID, sparePartID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Spare_Part_Reserved ( 
	Client_Order_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL,
	Used FLOAT NOT NULL
);

ALTER TABLE Spare_Part_Reserved ADD CONSTRAINT PK_Spare_Part_Reserved 
	PRIMARY KEY (Spare_Part_ID, Client_Order_ID);


ALTER TABLE Spare_Part_Reserved ADD CONSTRAINT FK_Spare_Part_Reserved_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Spare_Part_Reserved ADD CONSTRAINT FK_Spare_Part_Reserved_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
