package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Entity;

import model.util.EntityHelper;
import model.util.LimitedString;


public class Client {
	
	private LimitedString personCompany = new LimitedString(1, true); // TODO - define the values!!!
	private LimitedString addressCity = new LimitedString(30);
	private LimitedString addressLine = new LimitedString(100);
	private LimitedString phoneNumber = new LimitedString(15);
	private LimitedString mail = new LimitedString(50);
	private LimitedString IBANNumber = new LimitedString(30);
	private LimitedString SWIFTCode = new LimitedString(20);
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"phoneNumber", "mail", "IBANNumber", "SWIFTCode"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Client readEntity(Entity entity) {
		Client client = new Client();
		return EntityHelper.readIt(entity, client, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public String getPersonCompany() {
		return personCompany.getString();
	}

	public void setPersonCompany(String personCompany) {
		this.personCompany.setString(personCompany);
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

