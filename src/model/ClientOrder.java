package model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;


public class ClientOrder {
	
	private Key autoserviceID;
	private Key clientID;
	private Key vehicleID;
	private Key employeeID;
	private Date date;
	private LimitedString status = new LimitedString(1, true); // TODO - define the values!!!
	private LimitedString paymentNumber = new LimitedString(30);
	private LimitedString vehiclePresent = new LimitedString(1, true); // TODO - define the values!!!
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"paymentNumber"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrder readEntity(Entity entity) {
		ClientOrder clientOrder = new ClientOrder();
		return EntityHelper.readIt(entity, clientOrder, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public Key getClientID() {
		return clientID;
	}

	public void setClientID(Key clientID) {
		this.clientID = clientID;
	}

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
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
		this.status.setString(status);
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
		this.vehiclePresent.setString(vehiclePresent);
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
