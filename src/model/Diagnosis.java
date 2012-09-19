package model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.util.EntityHelper;
import model.util.LimitedString;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;


public class Diagnosis {
	
	private Key autoserviceID;
	private Key vehicleID;
	private Key employee_ID;
	private Date date;
	private Double price;
	private LimitedString status = new LimitedString(1, true); // TODO - define the values!!!
	private LimitedString paymentNumber = new LimitedString(30);
	
	private static final String PARENT_FIELD = "autoserviceID";
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"IGNORED_FIELDS"}));
	
	private static final Set<String> NULLABLE_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] { "paymentNumber"}));
	
	public Entity makeEntity() {
		return EntityHelper.buildIt(this, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}
	
	public static Diagnosis readEntity(Entity entity) {
		Diagnosis diagnosis = new Diagnosis();
		return EntityHelper.readIt(entity, diagnosis, PARENT_FIELD, IGNORED_FIELDS, NULLABLE_FIELDS);
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public Key getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Key vehicleID) {
		this.vehicleID = vehicleID;
	}

	public Key getEmployee_ID() {
		return employee_ID;
	}

	public void setEmployee_ID(Key employee_ID) {
		this.employee_ID = employee_ID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPrice() {
		return price.doubleValue();
	}

	public void setPrice(double price) {
		this.price = Double.valueOf(price);
	}

	public String getStatus() {
		return status.getString();
	}

	public void setStatus(String status) {
		this.status.setString(status);
	}

	public String getPaymentNumber() {
		return paymentNumber.getString();
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber.setString(paymentNumber);
	}
	
}

/*
CREATE TABLE Diagnosis ( 
	Diagnosis_ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	VIN CHAR(17) NOT NULL,
	Autoservice_ID BIGINT NOT NULL,
	Employee_ID BIGINT NOT NULL,
	Date DATE NOT NULL,
	Price FLOAT NOT NULL,
	Status SMALLINT NOT NULL,
	Payment_Number VARCHAR(30)
);

ALTER TABLE Diagnosis ADD CONSTRAINT PK_Diagnosis 
	PRIMARY KEY (Diagnosis_ID);


ALTER TABLE Diagnosis ADD CONSTRAINT FK_Diagnosis_Autoservice 
	FOREIGN KEY (Autoservice_ID) REFERENCES Autoservice (Autoservice_ID);

ALTER TABLE Diagnosis ADD CONSTRAINT FK_Diagnosis_Employee 
	FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID);

ALTER TABLE Diagnosis ADD CONSTRAINT FK_Diagnosis_Vehicle 
	FOREIGN KEY (VIN) REFERENCES Vehicle (VIN);
*/
