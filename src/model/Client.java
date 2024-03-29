package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;

import model.util.EntityHelper;
import model.util.LimitedString;

@SuppressWarnings("serial")
public class Client implements Serializable {
	
	public static final String PERSON = "P";
	public static final String COMPANY = "C";
	
	private Entity thisEntity;
	private Object personOrCompany;
	
	@SuppressWarnings("unused")
	private Key clientParentID;
	private LimitedString personCompany = new LimitedString(1, true);
	private LimitedString addressCity = new LimitedString(30);
	private LimitedString addressLine = new LimitedString(100);
	private LimitedString phoneNumber = new LimitedString(15);
	private LimitedString mail = new LimitedString(50);
	private LimitedString IBANNumber = new LimitedString(30);
	private LimitedString SWIFTCode = new LimitedString(20);
	private LimitedString foreignID = new LimitedString(50);
	
	private static final String PARENT_FIELD = "clientParentID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"PARENT_FIELD", "IGNORED_FIELDS", "NULLABLE_FIELDS", "UNIQUE_FIELDS",
					      "PERSON", "COMPANY",
					      "thisEntity", "personOrCompany"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"phoneNumber", "mail", "IBANNumber", "SWIFTCode", "foreignID"}));
	
	private static final Set<String> UNIQUE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IBANNumber", "foreignID"}));
	
	public Entity makeEntity() {
		clientParentID = EntityHelper.getClientParent();
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public void writeToDB() {
		
		if (personOrCompany instanceof Company) {
			personCompany.setString(COMPANY);
		} else if (personOrCompany instanceof Person) {
			personCompany.setString(PERSON);
		}
		
		if (thisEntity == null) {
			thisEntity = makeEntity();
		} else {
			EntityHelper.populateIt(thisEntity, this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
		}
		
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		
		EntityHelper.checkUniqueFields(thisEntity, UNIQUE_FIELDS);
		
		Transaction transaction = dataStore.beginTransaction();
		
		try {
			dataStore.put(thisEntity);
			
			if (personOrCompany instanceof Company) {
				((Company)personOrCompany).setClientID(getID());
				((Company)personOrCompany).writeToDB();
			} else if (personOrCompany instanceof Person) {
				((Person)personOrCompany).setClientID(getID());
				((Person)personOrCompany).writeToDB();
			}
			transaction.commit();
			
		} catch (RuntimeException e) {
			transaction.rollback();
			throw e;
		}
	}
	
	public void writeToDB(boolean makeNew) {
		if (makeNew) {
			thisEntity = null;
		}
		writeToDB();
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
	
	private static List<Client> readList(List<Entity> listToRead) {
		List<Client> newList =  new ArrayList<Client>();
		
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

	public String getPersonCompany() {
		return personCompany.getString();
	}

	public Person getPerson() {
		if ((personOrCompany ==  null) &&
			(thisEntity != null) &&
			(PERSON.equals(personCompany.getString()))) {
			if (Person.countGetAll(thisEntity.getKey()) != 1) {
				throw new RuntimeException("More than one person linked to this client!");
			}
			List<Person> person = Person.queryGetAll(0, 1, thisEntity.getKey());
			personOrCompany = person.get(0);
		}
		
		if (personOrCompany instanceof Person) {
			return (Person)personOrCompany;
		}
		return null;
	}
	
	public void setPerson(Person person) {
		if (personOrCompany != null) {
			throw new RuntimeException("Person or Company has been already set. You can't reset it!");
		}
		personOrCompany = person;
	}
	
	public Company getCompany() {
		if ((personOrCompany ==  null) &&
				(thisEntity != null) &&
				(COMPANY.equals(personCompany.getString()))) {
				if (Company.countGetAll(thisEntity.getKey()) != 1) {
					throw new RuntimeException("More than one company linked to this client!");
				}
				List<Company> company = Company.queryGetAll(0, 1, thisEntity.getKey());
				personOrCompany = company.get(0);
			}
		
		if (personOrCompany instanceof Company) {
			return (Company)personOrCompany;
		}
		return null;
	}
	
	public void setCompany(Company company) {
		if (personOrCompany != null) {
			throw new RuntimeException("Person or Company has been already set. You can't reset it!");
		}
		personOrCompany = company;
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
	
	public String getForeignID() {
		return foreignID.getString();
	}

	public void setForeignID(String foreignID) {
		this.foreignID.setString(foreignID);
	}
	
	private static PreparedQuery getPreparedQueryAll() { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Client.class.getSimpleName()).
					   setAncestor(EntityHelper.getClientParent()).
				       addSort("__key__"));
	}
	
	private static PreparedQuery getPreparedQueryByForeignID(String foreignID) { 
		return DatastoreServiceFactory.getDatastoreService().
			   prepare(new Query(Client.class.getSimpleName()).
					   setAncestor(EntityHelper.getClientParent()).
				       addSort("__key__").
				       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.EQUAL, foreignID)));
	}
	
	private static PreparedQuery getPreparedQueryHasForeignID() { 
		return DatastoreServiceFactory.getDatastoreService().
				   prepare(new Query(Client.class.getSimpleName()).
						   setAncestor(EntityHelper.getClientParent()).
					       addSort("foreignID").
					       setFilter(new Query.FilterPredicate("foreignID", FilterOperator.NOT_EQUAL, null)));
	}
	
	public static List<Client> queryGetAll(int offset, int count) {
		List<Entity> oldList = getPreparedQueryAll().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetAll() {
		return getPreparedQueryAll().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Client> queryGetHasForeignID(int offset, int count) {
		List<Entity> oldList = getPreparedQueryHasForeignID().
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetHasForeignID() {
		return getPreparedQueryHasForeignID().countEntities(FetchOptions.Builder.withLimit(10000));
	}
	
	public static List<Client> queryGetByForeignID(String foreignID, int offset, int count) {
		List<Entity> oldList = getPreparedQueryByForeignID(foreignID).
				asList(FetchOptions.Builder.withOffset(offset).limit(count));
		
		return readList(oldList);
	}
	
	public static int countGetByForeignID(String foreignID) {
		return getPreparedQueryByForeignID(foreignID).countEntities(FetchOptions.Builder.withLimit(10000));
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
	SWIFT_Code VARCHAR(20),
	Foreign_ID VARCHAR(50) NOT NULL
);

ALTER TABLE Client ADD CONSTRAINT PK_Client 
	PRIMARY KEY (Client_ID);
*/

