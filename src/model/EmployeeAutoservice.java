package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class EmployeeAutoservice implements Serializable {
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private Employee employee;
	
	private Key autoserviceID;
	private Key employeeID;
	private LimitedString username = new LimitedString(50);
	private LimitedString password = new LimitedString(50); // TODO - така ли да остава ?
	private LimitedString position = new LimitedString(30);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity", "autoservice", "employee"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		thisEntity = EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		return thisEntity;
	}
	
	public void writeToDB() {
		if (thisEntity == null) {
			DatastoreServiceFactory.getDatastoreService().put(makeEntity());
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
			DatastoreServiceFactory.getDatastoreService().put(thisEntity);
		}
	}
	
	public static EmployeeAutoservice readEntity(Entity entity) {
		EmployeeAutoservice employeeAutoservice = new EmployeeAutoservice();
		employeeAutoservice.thisEntity = entity;
		return EntityHelper.readIt(entity, employeeAutoservice, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static EmployeeAutoservice readEntity(Key key) {
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
	
	private static List<EmployeeAutoservice> readList(List<Entity> listToRead) {
		List<EmployeeAutoservice> newList =  new ArrayList<EmployeeAutoservice>();
		
		for (Entity entity : listToRead) {
			newList.add(readEntity(entity));
		}
		
		return newList;
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

	public String getUsername() {
		return username.getString();
	}

	public void setUsername(String username) {
		this.username.setString(username);
	}

	public String getPassword() {
		return password.getString();
	}

	public void setPassword(String password) {
		this.password.setString(scramblePassword(password));
	}

	public String getPosition() {
		return position.getString();
	}

	public void setPosition(String position) {
		this.position.setString(position);
	}
	
	private static String scramblePassword(String password) {
		//TODO - scramble it or hash it, maybe with salt and pepper :)
		return new String(password);
	}
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(EmployeeAutoservice.class.getSimpleName()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByUsername(String name) {
		return DatastoreServiceFactory.getDatastoreService().
				prepare(new Query(EmployeeAutoservice.class.getSimpleName()).
						addSort("__key__").
						setFilter(new Query.FilterPredicate("username", FilterOperator.EQUAL, name)));
	}
	private static PreparedQuery getPreparedQueryByUsernamePassword(String username, String password) {
		return DatastoreServiceFactory.getDatastoreService().
				prepare(new Query(EmployeeAutoservice.class.getSimpleName()).
						addSort("__key__").
						setFilter(CompositeFilterOperator.and(
								new FilterPredicate("username", FilterOperator.EQUAL, username),
								new FilterPredicate("password", FilterOperator.EQUAL, scramblePassword(password)))));
	}
	
	public static List<EmployeeAutoservice> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<EmployeeAutoservice> queryGetByUsername(String username, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByUsername(username).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByUsername(String username) {
		return getPreparedQueryByUsername(username).
				countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<EmployeeAutoservice> queryGetByUsernamePassword(String username, String password, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByUsernamePassword(username, password).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByUsernamePassword(String username, String password) {
		return getPreparedQueryByUsernamePassword(username, password).
				countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Employee_Autoservice ( 
	Employee_ID BIGINT NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Username VARCHAR(50) NOT NULL,
	Password VARCHAR(50) NOT NULL,
	Position VARCHAR(30) NOT NULL
);

ALTER TABLE Employee_Autoservice ADD CONSTRAINT PK_Employee_Autoservice 
	PRIMARY KEY (Employee_ID, Autoservice_ID);


ALTER TABLE Employee_Autoservice ADD CONSTRAINT FK_Employee_Autoservice_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Employee_Autoservice ADD CONSTRAINT FK_Employee_Autoservice_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);
*/
