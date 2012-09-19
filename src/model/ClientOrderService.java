package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class ClientOrderService {
	
	private Key clientOrderID;
	private Key serviceID;
	private Key employeeID;
	private Double priceHour;
	private LimitedString whoPays = new LimitedString(1, true); // TODO - define the values!!!
	
	private static final String PARENT_FIELD = "clientOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrderService readEntity(Entity entity) {
		ClientOrderService clientOrderService = new ClientOrderService();
		return EntityHelper.readIt(entity, clientOrderService, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(Key clientOrderID) {
		this.clientOrderID = clientOrderID;
	}

	public Key getServiceID() {
		return serviceID;
	}

	public void setServiceID(Key serviceID) {
		this.serviceID = serviceID;
	}

	public Key getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Key employeeID) {
		this.employeeID = employeeID;
	}

	public double getPriceHour() {
		return priceHour.doubleValue();
	}

	public void setPriceHour(double priceHour) {
		this.priceHour = Double.valueOf(priceHour);
	}

	public String getWhoPays() {
		return whoPays.getString();
	}

	public void setWhoPays(String whoPays) {
		this.whoPays.setString(whoPays);
	}
	
}

/*
CREATE TABLE Client_Order_Service ( 
	Service_ID BIGINT NOT NULL,
	Client_Order_ID BIGINT NOT NULL,
	Employee_ID BIGINT NOT NULL,
	Price_Hour FLOAT NOT NULL,
	Who_Pays SMALLINT NOT NULL
);

ALTER TABLE Client_Order_Service ADD CONSTRAINT PK_Client_Order_Service 
	PRIMARY KEY (Client_Order_ID, Service_ID);


ALTER TABLE Client_Order_Service ADD CONSTRAINT FK_Client_Order_Service_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Client_Order_Service ADD CONSTRAINT FK_Client_Order_Service_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);

ALTER TABLE Client_Order_Service ADD CONSTRAINT FK_Client_Order_Service_Service 
	FOREIGN KEY (Service_ID) REFERENCES Service (Service_ID);
*/
