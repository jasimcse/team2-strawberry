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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class WarehouseOrder implements Serializable {
	
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
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "NEW", "COMPLETED",
					      "thisEntity", "autoservice", "supplier", "employee"}));
	
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
	
	private static List<WarehouseOrder> readList(List<Entity> listToRead) {
		List<WarehouseOrder> newList =  new ArrayList<WarehouseOrder>();
		
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

	public Key getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(Key supplierID) {
		this.supplierID = supplierID;
	}
	
	public Supplier getSupplier() {
		if (supplier == null) {
			if (this.supplierID != null) {
				supplier = Supplier.readEntity(this.supplierID);
			}
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
	
	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarehouseOrder.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByStatus(Key autoserviceID, String status) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarehouseOrder.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("status", FilterOperator.EQUAL, status)));
	}
	
	private static PreparedQuery getPreparedQueryBySupplierStatus(Key autoserviceID, Key supplierID, String status) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(WarehouseOrder.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__").
				       setFilter(CompositeFilterOperator.and(
				    		   new Query.FilterPredicate("supplierID", FilterOperator.EQUAL, supplierID),
				               new Query.FilterPredicate("status", FilterOperator.EQUAL, status))));
	}
	
	public static List<WarehouseOrder> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<WarehouseOrder> queryGetByStatus(String status, int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryByStatus(autoserviceID, status).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByStatus(Key autoserviceID, String status) {
		return getPreparedQueryByStatus(autoserviceID, status).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<WarehouseOrder> queryGetBySupplierStatus(Key supplierID, String status, int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryBySupplierStatus(autoserviceID, supplierID, status).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetBySupplierStatus(Key autoserviceID, Key supplierID, String status) {
		return getPreparedQueryBySupplierStatus(autoserviceID, supplierID, status).countEntities(FetchOptions.Builder.withLimit(10000));
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
