package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class ClientOrderService {
	
	public static final String CLIENT_PAYS = "1";
	public static final String INSURER_PAYS = "2";
	public static final String PRODUCER_PAYS = "3";
	
	private Entity thisEntity;
	private ClientOrder clientOrder;
	private Service service;
	private Employee employee;
	
	private Key clientOrderID;
	private Key serviceID;
	private Key employeeID;
	private Double priceHour;
	private LimitedString whoPays = new LimitedString(1, true);
	
	private static final String PARENT_FIELD = "clientOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS", "thisEntity", "clientOrder", "service", "employee"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrderService readEntity(Entity entity) {
		ClientOrderService clientOrderService = new ClientOrderService();
		clientOrderService.thisEntity = entity;
		return EntityHelper.readIt(entity, clientOrderService, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrderService readEntity(Key key) {
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
	
	public void setClientOrder(ClientOrder clientOrder) {
		this.clientOrder = clientOrder;
		
		if (clientOrder == null) {
			clientOrderID = null;
		} else {
			clientOrderID = clientOrder.getID();
		}
	}

	public Key getServiceID() {
		return serviceID;
	}

	public void setServiceID(Key serviceID) {
		this.serviceID = serviceID;
	}
	
	public Service getService() {
		if (service == null) {
			service = Service.readEntity(this.serviceID);
		}
		
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
		
		if (service == null) {
			serviceID = null;
		} else {
			serviceID = service.getID();
		}
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
		if (CLIENT_PAYS.equals(whoPays) || INSURER_PAYS.equals(whoPays) || PRODUCER_PAYS.equals(whoPays)) {
			this.whoPays.setString(whoPays);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
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
