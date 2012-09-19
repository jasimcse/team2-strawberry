package model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class WarehouseOrder {
	
	private Key autoserviceID;
	private Key supplierID;
	private Key employeeID;
	private Date date;
	private LimitedString status = new LimitedString(1, true); // TODO - define the values!!!
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static WarehouseOrder readEntity(Entity entity) {
		WarehouseOrder warehouseOrder = new WarehouseOrder();
		return EntityHelper.readIt(entity, warehouseOrder, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public Key getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(Key supplierID) {
		this.supplierID = supplierID;
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
