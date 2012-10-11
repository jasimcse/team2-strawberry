package controller.debug;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import model.VehicleModel;
import model.VehicleModelService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.utils.SystemProperty;

import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

@ManagedBean(name="dobavqneNaUslugaZaModelAvtomobil")
@RequestScoped
public class DobavqneNaUslugaZaModelAvtomobil {

	private VehicleModelService uslugaZaModel = new VehicleModelService();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//uslugaZaModel.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateVehicleModelService(
					ConfigurationProperties.getElectronicShopID(),
					uslugaZaModel.getVehicleModel().getForeignID(),
					uslugaZaModel.getService().getForeignID(),
					uslugaZaModel.getDurationHour(),
					uslugaZaModel.getMonthsToNext(),
					uslugaZaModel.getMilageToNext());
			
			// set the message
			if (flag) {
				errorMessage = "Услугата за модела автомобил беше добавена успешно!";
			} else {
				errorMessage = "Услугата за модела автомобил НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, услугата за модела автомобил не можа да се добави успешно!";
		}
		
		// clean the data
		uslugaZaModel = new VehicleModelService();
		
		return null;
	}

	public Key getVehicleModelID() {
		return uslugaZaModel.getVehicleModelID();
	}

	public void setVehicleModelID(Key vehicleModelID) {
		uslugaZaModel.setVehicleModelID(vehicleModelID);
	}

	public Key getServiceID() {
		return uslugaZaModel.getServiceID();
	}

	public void setServiceID(Key serviceID) {
		uslugaZaModel.setServiceID(serviceID);
	}

	public double getDurationHour() {
		return uslugaZaModel.getDurationHour();
	}

	public void setDurationHour(double durationHour) {
		uslugaZaModel.setDurationHour(durationHour);
	}

	public long getMonthsToNext() {
		return uslugaZaModel.getMonthsToNext();
	}

	public void setMonthsToNext(long monthsToNext) {
		uslugaZaModel.setMonthsToNext(monthsToNext);
	}

	public long getMilageToNext() {
		return uslugaZaModel.getMilageToNext();
	}

	public void setMilageToNext(long milageToNext) {
		uslugaZaModel.setMilageToNext(milageToNext);
	}
	
	public List<VehicleModel> getVehicleModels() {
		List<VehicleModel> vm = VehicleModel.queryGetAll(0, 1000);
		return vm;
	}
	
	public List<model.Service> getServices() {
		List<model.Service> services = model.Service.queryGetAll(0, 1000);
		return services;
	}
	
}
