package model;

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

public class WarehouseOrder {
	
	public static final String NEW = "1";
	public static final String COMPLETED = "2";
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private Supplier supplier;
	private Employee employee;
	
	private Key autoserviceID;
	private Key supplierID;
	private Key employeeID;
	private Date date;
	private LimitedString status = new LimitedString(1, true);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS", "thisEntity", "autoservice", "supplier", "employee"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrder readEntity(Entity entity) {
		WarehouseOrder warehouseOrder = new WarehouseOrder();
		warehouseOrder.thisEntity = entity;
		return EntityHelper.readIt(entity, warehouseOrder, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrder readEntity(Key key) {
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

	public Key getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(Key supplierID) {
		this.supplierID = supplierID;
	}
	
	public Supplier getSupplier() {
		if (supplier == null) {
			supplier = Supplier.readEntity(this.supplierID);
		}
		
		return supplier;
	}
	
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
		
		if (supplier == null) {
			supplierID = null;
		} else {
			supplierID = supplier.getID();
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
		if (NEW.equals(status) || COMPLETED.equals(status)) {
			this.status.setString(status);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
	}
	
}

/*
CREATE TABLE Warehouse_Order ( 
	Warehouse_Order_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Supplier_ID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Employee_ID BIGINT NOT NULL,
	Date DATE NOT NULL,
	Status SMALLINT NOT NULL
);

ALTER TABLE Warehouse_Order ADD CONSTRAINT PK_Warehouse_Order 
	PRIMARY KEY (Warehouse_Order_ID);


ALTER TABLE Warehouse_Order ADD CONSTRAINT FK_Warehouse_Order_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Warehouse_Order ADD CONSTRAINT FK_Warehouse_Order_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);

ALTER TABLE Warehouse_Order ADD CONSTRAINT FK_Warehouse_Order_Supplier 
	FOREIGN KEY (Supplier_ID) REFERENCES Supplier (Supplier_ID);
*/