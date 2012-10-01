package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

import model.util.EntityHelper;
import model.util.LimitedString;


public class Autoservice {
	
	private Entity thisEntity;
	
	private LimitedString name = new LimitedString(100);
	private LimitedString addressCity = new LimitedString(30);
	private LimitedString addressLine = new LimitedString(100);
	private LimitedString phoneNumber = new LimitedString(15);
	private LimitedString mail = new LimitedString(50);
	private LimitedString IBANNumber = new LimitedString(30);
	private LimitedString SWIFTCode = new LimitedString(20);
	private LimitedString registrationNumber = new LimitedString(9, true);
	private LimitedString VATNumber = new LimitedString(11, true);
	
	private static final String PARENT_FIELD = null; // TODO - така ли да остава?
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "thisEntity"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"VATNumber"}));
	
	public void writeToDB() {
		if (thisEntity == null) {
			DatastoreServiceFactory.getDatastoreService().put(makeEntity());
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
			DatastoreServiceFactory.getDatastoreService().put(thisEntity);
		}
	}
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Autoservice readEntity(Entity entity) {
		Autoservice autoservice = new Autoservice();
		autoservice.thisEntity = entity;
		return EntityHelper.readIt(entity, autoservice, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Autoservice readEntity(Key key) {
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
	
	private static List<Autoservice> readList(List<Entity> listToRead) {
		List<Autoservice> newList =  new ArrayList<Autoservice>();
		
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
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Autoservice.class.getCanonicalName()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByName(String name) {
		return DatastoreServiceFactory.getDatastoreService().
				prepare(new Query(Autoservice.class.getCanonicalName()).
						addSort("__key__").
						setFilter(new Query.FilterPredicate("name", FilterOperator.EQUAL, name)));
	}
	
	public static List<Autoservice> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Autoservice> queryGetByName(String name, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByName(name).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByName(String name) {
		return getPreparedQueryByName(name).
				countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
}

/*
CREATE TABLE Autoservice ( 
	Autoservice_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	Name VARCHAR(100) NOT NULL,
	Address_City VARCHAR(30) NOT NULL,
	Address_Line VARCHAR(100) NOT NULL,
	Phone_Number VARCHAR(15) NOT NULL,
	E_Mail VARCHAR(50) NOT NULL,
	IBAN_Number VARCHAR(30) NOT NULL,
	SWIFT_Code VARCHAR(20) NOT NULL,
	Registration_Number CHAR(9) NOT NULL,
	VAT_Number CHAR(11)
);

ALTER TABLE Autoservice ADD CONSTRAINT PK_Autoservice 
	PRIMARY KEY (Autoservice_ID);
*/

