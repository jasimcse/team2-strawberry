package controller.users;


import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.appengine.api.datastore.Key;

import model.Client;
import model.Company;
import model.Person;
import model.Vehicle;
import model.VehicleModel;
import model.WarrantyConditions;


@ManagedBean(name="dobavqneNaAvtomobil")
@RequestScoped

public class DobavqneNaAvtomobil {

	private Vehicle avtomobil = new Vehicle();
	
	private String errorMessage;

	public Key getID() {
		return avtomobil.getID();
	}

	public Client getClient() {
		return avtomobil.getClient();
	}

	public void setClient(Client client) {
		avtomobil.setClient(client);
	}

	public Key getVehicleModelID() {
		return avtomobil.getVehicleModelID();
	}

	public void setVehicleModelID(Key vehicleModelID) {
		avtomobil.setVehicleModelID(vehicleModelID);
	}

	public VehicleModel getVehicleModel() {
		return avtomobil.getVehicleModel();
	}

	public void setVehicleModel(VehicleModel vehicleModel) {
		avtomobil.setVehicleModel(vehicleModel);
	}

	public Key getWarrantyConditionsID() {
		return avtomobil.getWarrantyConditionsID();
	}

	public void setWarrantyConditionsID(Key warrantyConditionsID) {
		avtomobil.setWarrantyConditionsID(warrantyConditionsID);
	}

	public WarrantyConditions getWarrantyConditions() {
		return avtomobil.getWarrantyConditions();
	}

	public void setWarrantyConditions(WarrantyConditions warrantyConditions) {
		avtomobil.setWarrantyConditions(warrantyConditions);
	}

	public String getVIN() {
		return avtomobil.getVIN();
	}

	public void setVIN(String VIN) {
		avtomobil.setVIN(VIN);
	}

	public String getEngineNumber() {
		return avtomobil.getEngineNumber();
	}

	public void setEngineNumber(String engineNumber) {
		avtomobil.setEngineNumber(engineNumber);
	}

	public String getPlateNumber() {
		return avtomobil.getPlateNumber();
	}

	public void setPlateNumber(String plateNumber) {
		avtomobil.setPlateNumber(plateNumber);
	}

	public String getWarrantyOK() {
		return avtomobil.getWarrantyOK();
	}

	public void setWarrantyOK(String warrantyOK) {
		avtomobil.setWarrantyOK(warrantyOK);
	}

	public Date getPurchaseDate() {
		return avtomobil.getPurchaseDate();
	}

	public void setPurchaseDate(Date purchaseDate) {
		avtomobil.setPurchaseDate(purchaseDate);
	}
	
	

	public String getErrorMessage() {
		return errorMessage;
	}

	public void addAvtomobil()
	{
		avtomobil.writeToDB();
	
		avtomobil = new Vehicle();


		errorMessage = "Автомобилът беше добавен успешно!";
	}
	
	public void changeModel()
	{
		
	}

}
