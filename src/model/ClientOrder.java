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

@SuppressWarnings("serial")
public class ClientOrder implements Serializable {
	
	public static final String HALTED = "1";
	public static final String PROCESSING = "2";
	public static final String FINISHED = "3";
	public static final String PAYED = "4";
	public static final String BLOCKED = "5";
	
	public static final String VEHICLE_PRESENTS = "1";
	public static final String VEHICLE_NOT_PRESENTS = "2";
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private Client client;
	private Vehicle vehicle;
	private Employee employee;
	
	private Key autoserviceID;
	private Key clientID;
	private Key vehicleID;
	private Key employeeID;
	private Date date;
	private LimitedString status = new LimitedString(1, true);
	private LimitedString paymentNumber = new LimitedString(30);
	private LimitedString vehiclePresent = new LimitedString(1, true);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "HALTED", "PROCESSING", "FINISHED", "PAYED", "BLOCKED",
					      "VEHICLE_PRESENTS", "VEHICLE_NOT_PRESENTS",
					      "thisEntity", "autoservice", "client", "vehicle", "employee"}));
	
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
	
	public void writeToDB(boolean makeNew) {
		if (makeNew) {
			thisEntity = null;
		}
		writeToDB();
	}
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrder readEntity(Entity entity) {
		ClientOrder clientOrder = new ClientOrder();
		clientOrder.thisEntity = entity;
		return EntityHelper.readIt(entity, clientOrder, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrder readEntity(Key key) {
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
	
	private static List<ClientOrder> readList(List<Entity> listToRead) {
		List<ClientOrder> newList =  new ArrayList<ClientOrder>();
		
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

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public Vehicle getVehicle() {
		if (vehicle == null) {
			if (this.vehicleID != null) {
				vehicle = Vehicle.readEntity(this.vehicleID);
			}
		}
		
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		
		if (vehicle == null) {
			vehicleID = null;
		} else {
			vehicleID = vehicle.getID();
		}
	}
	
	public Employee getEmployee() {
		if (employee == null) {
			if (this.employeeID != null) {
				employee = Employee.readEntity(this.employeeID);
			}
		}
		
		return employee;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
		
		if (employee == null) {
			employeeID = null;
		} else {
			employeeID = employee.getID();
		}
	}

	public Key getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Key employeeID) {
		this.employeeID = employeeID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		if (HALTED.equals(status) || PROCESSING.equals(status) || FINISHED.equals(status) ||
		    PAYED.equals(status) || BLOCKED.equals(status)) {
			
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

	public String getVehiclePresent() {
		return vehiclePresent.getString();
	}

	public void setVehiclePresent(String vehiclePresent) {
		if (VEHICLE_NOT_PRESENTS.equals(vehiclePresent) || VEHICLE_PRESENTS.equals(vehiclePresent)) {
			this.vehiclePresent.setString(vehiclePresent);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}
	
	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(ClientOrder.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}
	
	public static List<ClientOrder> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Client_Order ( 
	Client_Order_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	VIN CHAR(17) NOT NULL,
	ClientID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Employee_ID BIGINT NOT NULL,
	Date DATE NOT NULL,
	Status SMALLINT NOT NULL,
	Payment_Number VARCHAR(30),
	Vehicle_Present SMALLINT NOT NULL
);

ALTER TABLE Client_Order ADD CONSTRAINT PK_Client_Order 
	PRIMARY KEY (Client_Order_ID);


ALTER TABLE Client_Order ADD CONSTRAINT FK_Client_Order_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Client_Order ADD CONSTRAINT FK_Client_Order_Client 
	FOREIGN KEY (ClientID) REFERENCES Client (Client_ID);

ALTER TABLE Client_Order ADD CONSTRAINT FK_Client_Order_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);

ALTER TABLE Client_Order ADD CONSTRAINT FK_Client_Order_Vehicle 
	FOREIGN KEY (VIN) REFERENCES Vehicle (VIN);
*/
