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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class Diagnosis implements Serializable {
	
	public static final String NOT_PAYED = "1";
	public static final String PAYED = "2";
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private Vehicle vehicle;
	private Employee employee;
	
	private Key autoserviceID;
	private Key vehicleID;
	private Key employeeID;
	private Date date;
	private Double price;
	private LimitedString status = new LimitedString(1, true);
	private LimitedString paymentNumber = new LimitedString(30);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "NOT_PAYED", "PAYED",
					      "thisEntity", "autoservice", "vehicle", "employee"}));
	
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
	
	public static Diagnosis readEntity(Entity entity) {
		Diagnosis diagnosis = new Diagnosis();
		diagnosis.thisEntity = entity;
		return EntityHelper.readIt(entity, diagnosis, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Diagnosis readEntity(Key key) {
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
	
	private static List<Diagnosis> readList(List<Entity> listToRead) {
		List<Diagnosis> newList =  new ArrayList<Diagnosis>();
		
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
		autoservice = null;
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

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
		vehicle = null;
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

	public Key getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Key employeeID) {
		this.employeeID = employeeID;
		employee = null;
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

	public double getPrice() {
		if (price == null) {
			return 0;
		}
		return price.doubleValue();
	}

	public void setPrice(double price) {
		this.price = Double.valueOf(price);
	}

	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		if (PAYED.equals(status) || NOT_PAYED.equals(status)) {
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
	
	private static PreparedQuery getPreparedQueryAll(Key autoserviceID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Diagnosis.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByDates(Key autoserviceID, Date startDate, Date endDate) {
		Filter filter = null;

		if (startDate != null) {
			filter = new Query.FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, startDate);
		}
		
		if (endDate != null) {
			if (filter == null) {
				filter = new Query.FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, endDate);
			} else {
				filter = CompositeFilterOperator.and(
						filter,
						new Query.FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, endDate));
			}
		}
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Diagnosis.class.getSimpleName()).
					   setAncestor(autoserviceID).
				       addSort("date").
				       setFilter(filter));
	}
	
	public static List<Diagnosis> queryGetAll(int offset, int count, Key autoserviceID) {
		List<Entity> oldList = getPreparedQueryAll(autoserviceID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll(Key autoserviceID) {
		return getPreparedQueryAll(autoserviceID).countEntities(FetchOptions.Builder.withLimit(10000));
	}
	

	public static List<Diagnosis> querySearchByDates(Key autoserviceID, Date dayStart, Date dayEnd, int offset, int count) {
		//List<StringSearchAttribute> searchStrings = new ArrayList<StringSearchAttribute>();
		List<Entity> oldList;
		
		if ((dayStart != null) || (dayEnd != null)) {
			oldList = getPreparedQueryByDates(autoserviceID, dayStart, dayEnd).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		} else {
			oldList = new ArrayList<Entity>();
		}

		return readList(oldList);
	}
	
	public static int countSearchByDates(Key autoserviceID, Date dayStart, Date dayEnd) {
		
		List<Entity> oldList;
		
		if ((dayStart != null) || (dayEnd != null)) {
			oldList = getPreparedQueryByDates(autoserviceID, dayStart, dayEnd).
				asList(FetchOptions.Builder.withOffset(0));
		} else {
			oldList = new ArrayList<Entity>();
		}
		
		return oldList.size();
	}
	
}

/*
CREATE TABLE Diagnosis ( 
	Diagnosis_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	VIN CHAR(17) NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Employee_ID BIGINT NOT NULL,
	Date DATE NOT NULL,
	Price FLOAT NOT NULL,
	Status SMALLINT NOT NULL,
	Payment_Number VARCHAR(30)
);

ALTER TABLE Diagnosis ADD CONSTRAINT PK_Diagnosis 
	PRIMARY KEY (Diagnosis_ID);


ALTER TABLE Diagnosis ADD CONSTRAINT FK_Diagnosis_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Diagnosis ADD CONSTRAINT FK_Diagnosis_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);

ALTER TABLE Diagnosis ADD CONSTRAINT FK_Diagnosis_Vehicle 
	FOREIGN KEY (VIN) REFERENCES Vehicle (VIN);
*/
