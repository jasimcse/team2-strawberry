package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

// TODO - да се преименува на Works, например?
public class EmployeeAutoservice {
	
	private Entity thisEntity;
	private Autoservice autoservice;
	private Employee employee;
	
	private Key autoserviceID;
	private Key employeeID;
	private LimitedString username = new LimitedString(50);
	private LimitedString password = new LimitedString(50); // TODO - така ли да остава ?
	private LimitedString position = new LimitedString(30);
	
	private static final String PARENT_FIELD = "autoserviceID"; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS", "thisEntity", "autoservice", "employee"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		thisEntity = EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		return thisEntity;
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
	
	public static EmployeeAutoservice checkCredentials(String username, String password) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(EmployeeAutoservice.class.getName());
		FilterPredicate filterUsername = new FilterPredicate("username", FilterOperator.EQUAL, username);
		FilterPredicate filterPassword = new FilterPredicate("password", FilterOperator.EQUAL, scramblePassword(password));
		query.setFilter(CompositeFilterOperator.and(filterUsername, filterPassword));
		
		Entity entity = datastore.prepare(query).asSingleEntity();
		if (entity == null) {
			return null;
		}
		return EmployeeAutoservice.readEntity(entity);
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
