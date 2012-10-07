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

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class Vehicle implements Serializable {
	
	public static final String WARRANTY_YES = "1";
	public static final String WARRANTY_NO = "2";
	
	private Entity thisEntity;
	private Client client;
	private VehicleModel vehicleModel;
	private WarrantyConditions warrantyConditions;
	
	private Key clientID;
	private Key vehicleModelID;
	private Key warrantyConditionsID;
	private LimitedString VIN = new LimitedString(17, true);
	private LimitedString engineNumber = new LimitedString(50);
	private LimitedString plateNumber = new LimitedString(8);
	private LimitedString warrantyOK = new LimitedString(1, true);
	private Date purchaseDate;
	
	private static final String PARENT_FIELD = "clientID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "WARRANTY_YES", "WARRANTY_NO",
					      "thisEntity", "client", "vehicleModel", "warrantyConditions"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"warrantyConditionsID", "warrantyOK", "purchaseDate"}));
	
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
	
	public static Vehicle readEntity(Entity entity) {
		Vehicle vehicle = new Vehicle();
		vehicle.thisEntity = entity;
		return EntityHelper.readIt(entity, vehicle, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Vehicle readEntity(Key key) {
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
	
	private static List<Vehicle> readList(List<Entity> listToRead) {
		List<Vehicle> newList =  new ArrayList<Vehicle>();
		
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

	public Key getClientID() {
		return clientID;
	}

	public void setClientID(Key clientID) {
		this.clientID = clientID;
	}
	
	public Client getClient() {
		if (client == null) {
			if (this.clientID != null) {
				client = Client.readEntity(this.clientID);
			}
		}
		
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
		
		if (client == null) {
			clientID = null;
		} else {
			clientID = client.getID();
		}
	}

	public Key getVehicleModelID() {
		return vehicleModelID;
	}

	public void setVehicleModelID(Key vehicleModelID) {
		this.vehicleModelID = vehicleModelID;
	}
	
	public VehicleModel getVehicleModel() {
		if (vehicleModel == null) {
			if (this.vehicleModelID != null) {
				vehicleModel = VehicleModel.readEntity(this.vehicleModelID);
			}
		}
		
		return vehicleModel;
	}
	
	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
		
		if (vehicleModel == null) {
			vehicleModelID = null;
		} else {
			vehicleModelID = vehicleModel.getID();
		}
	}

	public Key getWarrantyConditionsID() {
		return warrantyConditionsID;
	}

	public void setWarrantyConditionsID(Key warrantyConditionsID) {
		this.warrantyConditionsID = warrantyConditionsID;
	}
	
	public WarrantyConditions getWarrantyConditions() {
		if (warrantyConditions == null) {
			if (this.warrantyConditionsID != null) {
				warrantyConditions = WarrantyConditions.readEntity(this.warrantyConditionsID);
			}
		}
		
		return warrantyConditions;
	}
	
	public void setWarrantyConditions(WarrantyConditions warrantyConditions) {
		this.warrantyConditions = warrantyConditions;
		
		if (warrantyConditions == null) {
			warrantyConditionsID = null;
		} else {
			warrantyConditionsID = warrantyConditions.getID();
		}
	}

	public String getVIN() {
		return VIN.getString();
	}

	public void setVIN(String VIN) {
		this.VIN.setString(VIN);
	}

	public String getEngineNumber() {
		return engineNumber.getString();
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber.setString(engineNumber);
	}

	public String getPlateNumber() {
		return plateNumber.getString();
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber.setString(plateNumber);
	}

	public String getWarrantyOK() {
		return warrantyOK.getString();
	}

	public void setWarrantyOK(String warrantyOK) {
		if (WARRANTY_YES.equals(warrantyOK) || WARRANTY_NO.equals(warrantyOK)) {
			this.warrantyOK.setString(warrantyOK);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	private static PreparedQuery getPreparedQueryAll(Key clientID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Vehicle.class.getSimpleName()).
					   setAncestor(clientID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByVIN(String VIN) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Vehicle.class.getSimpleName()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("VIN", FilterOperator.EQUAL, VIN)));
	}
	
	private static PreparedQuery getPreparedQueryByVIN(Key clientID, String VIN) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Vehicle.class.getSimpleName()).
					   setAncestor(clientID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("VIN", FilterOperator.EQUAL, VIN)));
	}
	
	//TODO - да се дава възможност да се разглеждата всички ?
	
	public static List<Vehicle> queryGetAll(int offset, int count, Key clientID) {
		List<Entity> oldList = getPreparedQueryAll(clientID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key clientID) {
		return getPreparedQueryAll(clientID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Vehicle> queryGetByVIN(String VIN, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByVIN(VIN).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByVIN(String VIN) {
		return getPreparedQueryByVIN(VIN).
				countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Vehicle> queryGetByVIN(String VIN, int offset, int count, Key clientID) {
		List<Entity> oldList = getPreparedQueryByVIN(clientID, VIN).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByVIN(String VIN, Key clientID) {
		return getPreparedQueryByVIN(clientID, VIN).
				countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Vehicle ( 
	VIN CHAR(17) NOT NULL,
	Engine_Number VARCHAR(50) NOT NULL,
	Plate_Number VARCHAR(8) NOT NULL,
	Vehicle_Model_ID BIGINT NOT NULL,
	Warranty_ID BIGINT,
	Warranty_OK SMALLINT,
	Client_ID BIGINT NOT NULL,
	Purchase_Date DATE
);

ALTER TABLE Vehicle ADD CONSTRAINT PK_Vehicle 
	PRIMARY KEY (VIN);


ALTER TABLE Vehicle ADD CONSTRAINT FK_Vehicle_Client 
	FOREIGN KEY (Client_ID) REFERENCES Client (Client_ID);

ALTER TABLE Vehicle ADD CONSTRAINT FK_Vehicle_Vehicle_Model 
	FOREIGN KEY (Vehicle_Model_ID) REFERENCES Vehicle_Model (Vehicle_Model_ID);

ALTER TABLE Vehicle ADD CONSTRAINT FK_Vehicle_Warranty_Conditions 
	FOREIGN KEY (Warranty_ID) REFERENCES Warranty_Conditions (Warranty_ID);
*/
