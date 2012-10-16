package controller.debug;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.utils.SystemProperty;


import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

import model.Client;
import model.Vehicle;
import model.VehicleModel;
import model.WarrantyConditions;


@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaAvtomobilDebug")
@ViewScoped

public class DobavqneNaAvtomobil implements Serializable {

	private Vehicle avtomobil = new Vehicle();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//avtomobil.writeToDB();
		
		URL url;
		QName qualifiedName;
		String host;
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
			host = "http://localhost:8888";
		} else {
			host = "http://uni-ruse-autoservice.appspot.com";
		}
		try {
			url = new URL(host + "/wsdl/ElectronicShopService.wsdl");
			qualifiedName = new QName("http://SOAPServer.serverCommunication.controller/", "ElectronicShopService");
			Service service = Service.create(url, qualifiedName);
			ElectronicShopInterface eshop = service.getPort(ElectronicShopInterface.class);
			boolean flag = eshop.createOrUpdateVehicle(
					ConfigurationProperties.getElectronicShopID(),
					avtomobil.getVIN(),
					avtomobil.getClient().getForeignID(),
					avtomobil.getVehicleModel().getForeignID(),
					avtomobil.getWarrantyConditions().getForeignID(),
					avtomobil.getEngineNumber(),
					avtomobil.getPlateNumber(),
					avtomobil.getPurchaseDate());
			
			// set the message
			if (flag) {
				errorMessage = "Автомобилът беше добавен успешно!";
			} else {
				errorMessage = "Автомобилът НЕ беше добавен успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, автомобилът не можа да се добави успешно!";
		}
		
		// clean the data
		avtomobil = new Vehicle();
		
		return null;
	}

	public Key getClientID() {
		return avtomobil.getClientID();
	}

	public void setClientID(Key clientID) {
		avtomobil.setClientID(clientID);
	}

	public Key getVehicleModelID() {
		return avtomobil.getVehicleModelID();
	}

	public void setVehicleModelID(Key vehicleModelID) {
		avtomobil.setVehicleModelID(vehicleModelID);
	}

	public Key getWarrantyConditionsID() {
		return avtomobil.getWarrantyConditionsID();
	}

	public void setWarrantyConditionsID(Key warrantyConditionsID) {
		avtomobil.setWarrantyConditionsID(warrantyConditionsID);
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

	public Date getPurchaseDate() {
		return avtomobil.getPurchaseDate();
	}

	public void setPurchaseDate(Date purchaseDate) {
		avtomobil.setPurchaseDate(purchaseDate);
	}
	
	public List<VehicleModel> getVehicleModels() {
		List<VehicleModel> vm = VehicleModel.queryGetAll(0, 1000);
		return vm;
	}
	
	public List<Client> getClients() {
		List<Client> clients = Client.queryGetHasForeignID(0, 1000);
		return clients;
	}
	
	public List<WarrantyConditions> getWarrantyConditions() {
		List<WarrantyConditions> wc = WarrantyConditions.queryGetAll(0, 1000);
		return wc;
	}
	
}