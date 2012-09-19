package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;

public class Employee {
	
	private LimitedString name = new LimitedString(50);
	private LimitedString family = new LimitedString(50);
	private LimitedString addressCity = new LimitedString(30);
	private LimitedString addressLine = new LimitedString(100);
	private LimitedString phoneNumber = new LimitedString(15);
	private LimitedString mail = new LimitedString(50);
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Employee readEntity(Entity entity) {
		Employee employee = new Employee();
		return EntityHelper.readIt(entity, employee, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public String getName() {
		return name.getString();
	}

	public void setName(String name) {
		this.name.setString(name);
	}

	public String getFamily() {
		return family.getString();
	}

	public void setFamily(String family) {
		this.family.setString(family);
	}

	public String getAddressCity() {
		return addressCity.getString();
	}

	public void setAddressCity(String addressCity) {
		this.addressCity.setString(addressCity);
	}

	public String getAddressLine() {
		return addressLine.getString();
	}

	public void setAddressLine(String addressLine) {
		this.addressLine.setString(addressLine);
	}

	public String getPhoneNumber() {
		return phoneNumber.getString();
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.setString(phoneNumber);
	}

	public String getMail() {
		return mail.getString();
	}

	public void setMail(String mail) {
		this.mail.setString(mail);
	}
	
}

/*
CREATE TABLE Employee ( 
	Employee_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Name VARCHAR(50) NOT NULL,
	Family VARCHAR(50) NOT NULL,
	Address_City VARCHAR(30) NOT NULL,
	Address_Line VARCHAR(100) NOT NULL,
	Phone_Number VARCHAR(15) NOT NULL,
	E_Mail VARCHAR(50) NOT NULL
);

ALTER TABLE Employee ADD CONSTRAINT PK_Employee 
	PRIMARY KEY (Employee_ID);
*/

