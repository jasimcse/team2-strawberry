package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
public class Insurer implements Serializable {
	
	private Entity thisEntity;
	
	@SuppressWarnings("unused")
	private Key insurerParentID;
	private LimitedString name = new LimitedString(100);
	private LimitedString addressCity = new LimitedString(30);
	private LimitedString addressLine = new LimitedString(100);
	private LimitedString phoneNumber = new LimitedString(15);
	private LimitedString mail = new LimitedString(50);
	private LimitedString IBANNumber = new LimitedString(30);
	private LimitedString SWIFTCode = new LimitedString(20);
	private LimitedString registrationNumber = new LimitedString(9, true);
	private LimitedString VATNumber = new LimitedString(11, true);
	private LimitedString contactPerson = new LimitedString(100);
	
	private static final String PARENT_FIELD = "insurerParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"VATNumber"}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		DatastoreServiceFactory.getDatastoreService().put(thisEntity);
	}
	
	public Entity makeEntity() {
		insurerParentID = EntityHelper.getInsurerParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Insurer readEntity(Entity entity) {
		Insurer insurer = new Insurer();
		insurer.thisEntity = entity;
		return EntityHelper.readIt(entity, insurer, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Insurer readEntity(Key key) {
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

	public String getName() {
		return name.getString();
	}

	public void setName(String name) {
		this.name.setString(name);
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

	public void setIBANNumber(String IBANNumber) {
		this.IBANNumber.setString(IBANNumber);
	}

	public String getSWIFTCode() {
		return SWIFTCode.getString();
	}

	public void setSWIFTCode(String SWIFTCode) {
		this.SWIFTCode.setString(SWIFTCode);
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
CREATE TABLE Insurer ( 
	Insurer_ID BIGINT NOT NULL,
	Name VARCHAR(100) NOT NULL,
	Address_City VARCHAR(30) NOT NULL,
	Address_Line VARCHAR(100) NOT NULL,
	Phone_Number VARCHAR(15) NOT NULL,
	E_Mail VARCHAR(50) NOT NULL,
	IBAN_Number VARCHAR(30) NOT NULL,
	SWIFT_Code VARCHAR(20) NOT NULL,
	Registration_Number CHAR(9) NOT NULL,
	VAT_Number CHAR(11),
	Contact_Person VARCHAR(100) NOT NULL
);

ALTER TABLE Insurer ADD CONSTRAINT PK_Insurer 
	PRIMARY KEY (Insurer_ID);
*/

