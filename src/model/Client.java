package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

import model.util.EntityHelper;
import model.util.LimitedString;

@SuppressWarnings("serial")
public class Client implements Serializable {
	
	public static final String PERSON = "P";
	public static final String COMPANY = "C";
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key clientParentID;
	private LimitedString personCompany = new LimitedString(1, true);
	private LimitedString addressCity = new LimitedString(30);
	private LimitedString addressLine = new LimitedString(100);
	private LimitedString phoneNumber = new LimitedString(15);
	private LimitedString mail = new LimitedString(50);
	private LimitedString IBANNumber = new LimitedString(30);
	private LimitedString SWIFTCode = new LimitedString(20);
	
	private static final String PARENT_FIELD = "clientParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS",
					      "PERSON", "COMPANY",
					      "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"phoneNumber", "mail", "IBANNumber", "SWIFTCode"}));
	
	public Entity makeEntity() {
		clientParentID = EntityHelper.getClientParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public static Client readEntity(Entity entity) {
		Client client = new Client();
		client.thisEntity = entity;
		return EntityHelper.readIt(entity, client, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Client readEntity(Key key) {
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

	public String getPersonCompany() {
		return personCompany.getString();
	}

	public void setPersonCompany(String personCompany) {
		if (PERSON.equals(personCompany) || COMPANY.equals(personCompany)) {
			this.personCompany.setString(personCompany);
		} else {
			throw new RuntimeException("The string doesn't match any of possible values");
		}
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

	public String getIBANNumber() {
		return IBANNumber.getString();
	}

	public void setIBANNumber(String iBANNumber) {
		this.IBANNumber.setString(iBANNumber);
	}

	public String getSWIFTCode() {
		return SWIFTCode.getString();
	}

	public void setSWIFTCode(String sWIFTCode) {
		this.SWIFTCode.setString(sWIFTCode);
	}
	
}

/*
CREATE TABLE Client ( 
	Client_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Address_City VARCHAR(30) NOT NULL,
	Address_Line VARCHAR(100) NOT NULL,
	Phone_Number VARCHAR(15),
	E_Mail VARCHAR(50),
	Person_Company CHAR(1) NOT NULL,
	IBAN_Number VARCHAR(30),
	SWIFT_Code VARCHAR(20)
);

ALTER TABLE Client ADD CONSTRAINT PK_Client 
	PRIMARY KEY (Client_ID);
*/

