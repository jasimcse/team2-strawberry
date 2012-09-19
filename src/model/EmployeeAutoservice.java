package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

// TODO - да се преименува на Works, например?
public class EmployeeAutoservice {
	
	private Key autoserviceID;
	private Key employeeID;
	private LimitedString username = new LimitedString(50);
	private LimitedString password = new LimitedString(50); // TODO - така ли да остава ?
	private LimitedString position = new LimitedString(30);
	
	private static final String PARENT_FIELD = "autoserviceID"; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static EmployeeAutoservice readEntity(Entity entity) {
		EmployeeAutoservice employeeAutoservice = new EmployeeAutoservice();
		return EntityHelper.readIt(entity, employeeAutoservice, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public Key getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Key employeeID) {
		this.employeeID = employeeID;
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
		this.password.setString(password);
	}

	public String getPosition() {
		return position.getString();
	}

	public void setPosition(String position) {
		this.position.setString(position);
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
