package controller.debug;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import model.VehicleModel;

import com.google.appengine.api.utils.SystemProperty;

import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

@ManagedBean(name="dobavqneNaModelAvtomobil")
@RequestScoped
public class DobavqneNaModelAvtomobil {

	private VehicleModel modelAvtomobil = new VehicleModel();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//modelAvtomobil.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateVehicleModel(
					ConfigurationProperties.getElectronicShopID(),
					modelAvtomobil.getForeignID(),
					modelAvtomobil.getBrand(),
					modelAvtomobil.getModel(),
					modelAvtomobil.getCharacteristics());
			
			// set the message
			if (flag) {
				errorMessage = "Моделът автомобил беше добавен успешно!";
			} else {
				errorMessage = "Моделът автомобил НЕ беше добавен успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, моделът автомобил не можа да се добави успешно!";
		}
		
		// clean the data
		modelAvtomobil = new VehicleModel();
		
		return null;
	}

	public String getBrand() {
		return modelAvtomobil.getBrand();
	}

	public void setBrand(String brand) {
		modelAvtomobil.setBrand(brand);
	}

	public String getModel() {
		return modelAvtomobil.getModel();
	}

	public void setModel(String model) {
		modelAvtomobil.setModel(model);
	}

	public String getCharacteristics() {
		return modelAvtomobil.getCharacteristics();
	}

	public void setCharacteristics(String characteristics) {
		modelAvtomobil.setCharacteristics(characteristics);
	}

	public String getForeignID() {
		return modelAvtomobil.getForeignID();
	}

	public void setForeignID(String foreignID) {
		modelAvtomobil.setForeignID(foreignID);
	}
	
}
