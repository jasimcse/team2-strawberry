package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class Person {
	
	private Key clientID;
	private LimitedString name = new LimitedString(50);
	private LimitedString family = new LimitedString(50);
	
	private static final String PARENT_FIELD = "clientID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Person readEntity(Entity entity) {
		Person person = new Person();
		return EntityHelper.readIt(entity, person, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
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
