package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class Person {
	
	private Entity thisEntity;
	private Client client;
	
	private Key clientID;
	private LimitedString name = new LimitedString(50);
	private LimitedString family = new LimitedString(50);
	
	private static final String PARENT_FIELD = "clientID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS", "thisEntity", "client"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Person readEntity(Entity entity) {
		Person person = new Person();
		person.thisEntity = entity;
		return EntityHelper.readIt(entity, person, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Person readEntity(Key key) {
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

	public Key getClientID() {
		return clientID;
	}

	public void setClientID(Key clientID) {
		this.clientID = clientID;
	}
	
	public Client getClient() {
		if (client == null) {
			client = Client.readEntity(this.clientID);
		}
		
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
		
		if (client == null) {
			clientID = null;
		} else {
			clientID = client.getID();
		}
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
	
}

/*
CREATE TABLE Person ( 
	Client_ID BIGINT NOT NULL,
	Name VARCHAR(50) NOT NULL,
	Family VARCHAR(50) NOT NULL
);

ALTER TABLE Person ADD CONSTRAINT PK_Person 
	PRIMARY KEY (Client_ID);


ALTER TABLE Person ADD CONSTRAINT FK_Person_Client 
	FOREIGN KEY (Client_ID) REFERENCES Client (Client_ID);
*/