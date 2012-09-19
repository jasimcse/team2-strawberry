package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Company {
	
	private Key clientID;
	private LimitedString name = new LimitedString(100);
	private LimitedString registrationNumber = new LimitedString(9, true);
	private LimitedString VATNumber = new LimitedString(11, true);
	private LimitedString contactPerson = new LimitedString(100);
	
	private static final String PARENT_FIELD = "clientID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] { "VATNumber", "contactPerson"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Company readEntity(Entity entity) {
		Company company = new Company();
		return EntityHelper.readIt(entity, company, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getClientID() {
		return clientID;
	}

	public void setClientID(Key clientID) {
		this.clientID = clientID;
	}

	public String getName() {
		return name.getString();
	}

	public void setName(String name) {
		this.name.setString(name);
	}

	public String getRegistrationNumber() {
		return registrationNumber.getString();
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber.setString(registrationNumber);
	}

	public String getVATNumber() {
		return VATNumber.getString();
	}

	public void setVATNumber(String VATNumber) {
		this.VATNumber.setString(VATNumber);
	}

	public String getContactPerson() {
		return contactPerson.getString();
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson.setString(contactPerson);
	}
	
	
}

/*
CREATE TABLE Company ( 
	Client_ID BIGINT NOT NULL,
	Name VARCHAR(100) NOT NULL,
	Registration_Number CHAR(9) NOT NULL,
	VAT_Number CHAR(11),
	Contact_Person VARCHAR(100)
);

ALTER TABLE Company ADD CONSTRAINT PK_Company 
	PRIMARY KEY (Client_ID);


ALTER TABLE Company ADD CONSTRAINT FK_Company_Client 
	FOREIGN KEY (Client_ID) REFERENCES Client (Client_ID);
*/
