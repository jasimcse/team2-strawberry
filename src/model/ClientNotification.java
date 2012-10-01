package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

import model.util.EntityHelper;
import model.util.LimitedString;

@SuppressWarnings("serial")
public class ClientNotification implements Serializable {
	
	public static final String NEED_TO_CALL = "1";
	public static final String SUCCESS = "2";
	public static final String UNSUCCESS = "3";
	
	private Entity thisEntity;
	private Client client;
	private Employee employee;
	private Vehicle vehicle;
	private ClientOrder clientOrder;
	
	private Key clientID;
	private Date timestamp;
	private Key employeeID;
	private Key vehicleID; // може да е null, ако ще се пращат промоционални съобщения например
	private Key clientOrderID;
	private LimitedString status = new LimitedString(1, true);
	private LimitedString notes = new LimitedString(500);
	private LimitedString mail = new LimitedString(50);
	private LimitedString phoneNumber = new LimitedString(15);
	
	private static final String PARENT_FIELD = "clientID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "client", "employee", "vehicle", "clientOrder"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"employeeID", "vehicleID", "clientOrderID", "notes", "mail", "phoneNumber"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientNotification readEntity(Entity entity) {
		ClientNotification clientNotification = new ClientNotification();
		clientNotification.thisEntity = entity;
		return EntityHelper.readIt(entity, clientNotification, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientNotification readEntity(Key key) {
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

	public Key getClientID() {
		return clientID;
	}

	public void setClientID(Key clientID) {
		this.clientID = clientID;
	}
	
	public Client getClient() {
		if (client == null) {
			client = Client.readEntity(this.clientID);
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Key getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Key employeeID) {
		this.employeeID = employeeID;
	}
	
	public Employee getEmployee() {
		if (employee == null) {
			employee = Employee.readEntity(this.employeeID);
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

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public Vehicle getVehicle() {
		if (vehicle == null) {
			vehicle = Vehicle.readEntity(this.vehicleID);
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

	public Key getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(Key clientOrderID) {
		this.clientOrderID = clientOrderID;
	}
	
	public ClientOrder getClientOrder() {
		if (clientOrder == null) {
			clientOrder = ClientOrder.readEntity(this.clientOrderID);
		}
		
		return clientOrder;
	}
	
	public void setVehicle(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
		
		if (clientOrder == null) {
			clientOrderID = null;
		} else {
			clientOrderID = clientOrder.getID();
		}
	}

	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		if (NEED_TO_CALL.equals(status) || SUCCESS.equals(status) || UNSUCCESS.equals(status)) {
			this.status.setString(status);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}

	public String getNotes() {
		return notes.getString();
	}

	public void setNotes(String notes) {
		this.notes.setString(notes);
	}

	public String getMail() {
		return mail.getString();
	}

	public void setMail(String mail) {
		this.mail.setString(mail);
	}

	public String getPhoneNumber() {
		return phoneNumber.getString();
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.setString(phoneNumber);
	}
	
	
	
}

/*
CREATE TABLE Client_Notification ( 
	Client_Notification_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Client_ID BIGINT NOT NULL,
	Timestamp TIMESTAMP NOT NULL,
	Employee_ID BIGINT,
	VIN CHAR(17),
	Client_Order_ID BIGINT,
	Status SMALLINT NOT NULL,
	Notes VARCHAR(500),
	E_Mail VARCHAR(50),
	Phone_Number VARCHAR(15)
);

ALTER TABLE Client_Notification ADD CONSTRAINT PK_Client_Notification 
	PRIMARY KEY (Client_Notification_ID);


ALTER TABLE Client_Notification ADD CONSTRAINT FK_Client_Notification_Client 
	FOREIGN KEY (Client_ID) REFERENCES Client (Client_ID);

ALTER TABLE Client_Notification ADD CONSTRAINT FK_Client_Notification_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Client_Notification ADD CONSTRAINT FK_Client_Notification_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);

ALTER TABLE Client_Notification ADD CONSTRAINT FK_Client_Notification_Vehicle 
	FOREIGN KEY (VIN) REFERENCES Vehicle (VIN);
*/
