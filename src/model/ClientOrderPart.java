package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class ClientOrderPart {
	
	private Key clientOrderID;
	private Key sparePartID;
	private Double quantity;
	private Double priceUnit;
	private LimitedString whoPays = new LimitedString(1, true); // TODO - define the values!!!
	
	private static final String PARENT_FIELD = "clientOrderID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static ClientOrderPart readEntity(Entity entity) {
		ClientOrderPart clientOrderPart = new ClientOrderPart();
		return EntityHelper.readIt(entity, clientOrderPart, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getClientOrderID() {
		return clientOrderID;
	}

	public void setClientOrderID(Key clientOrderID) {
		this.clientOrderID = clientOrderID;
	}

	public Key getSparePartID() {
		return sparePartID;
	}

	public void setSparePartID(Key sparePartID) {
		this.sparePartID = sparePartID;
	}

	public double getQuantity() {
		return quantity.doubleValue();
	}

	public void setQuantity(double quantity) {
		this.quantity = Double.valueOf(quantity);
	}

	public double getPriceUnit() {
		return priceUnit.doubleValue();
	}

	public void setPriceUnit(double priceUnit) {
		this.priceUnit = Double.valueOf(priceUnit);
	}

	public String getWhoPays() {
		return whoPays.getString();
	}

	public void setWhoPays(String whoPays) {
		this.whoPays.setString(whoPays);
	}
	
	
}

/*
CREATE TABLE Client_Order_Part ( 
	Client_Order_ID BIGINT NOT NULL,
	Spare_Part_ID BIGINT NOT NULL,
	Quantity FLOAT NOT NULL,
	Price_Unit FLOAT NOT NULL,
	Who_Pays SMALLINT NOT NULL
);

ALTER TABLE Client_Order_Part ADD CONSTRAINT PK_Client_Order_Part 
	PRIMARY KEY (Client_Order_ID, Spare_Part_ID);


ALTER TABLE Client_Order_Part ADD CONSTRAINT FK_Client_Order_Part_Client_Order 
	FOREIGN KEY (Client_Order_ID) REFERENCES Client_Order (Client_Order_ID);

ALTER TABLE Client_Order_Part ADD CONSTRAINT FK_Client_Order_Part_Spare_Part 
	FOREIGN KEY (Spare_Part_ID) REFERENCES Spare_Part (Spare_Part_ID);
*/
