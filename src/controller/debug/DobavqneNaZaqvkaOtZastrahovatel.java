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

import model.Insurer;
import model.InsurerRequest;
import model.Vehicle;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.utils.SystemProperty;

import controller.serverCommunication.SOAPServer.InsurerInterface;

@ManagedBean(name="dobavqneNaZaqvkaOtZastrahovatel")
@RequestScoped
public class DobavqneNaZaqvkaOtZastrahovatel {

	private InsurerRequest zaqvka = new InsurerRequest();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//zaqvka.writeToDB();
		
		URL url;
		QName qualifiedName;
		String host;
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
			host = "http://localhost:8888";
		} else {
			host = "http://uni-ruse-autoservice.appspot.com";
		}
		try {
			url = new URL(host + "/wsdl/InsurerService.wsdl");
			qualifiedName = new QName("http://SOAPServer.serverCommunication.controller/", "InsurerService");
			Service service = Service.create(url, qualifiedName);
			InsurerInterface insurerService = service.getPort(InsurerInterface.class);
			boolean flag = insurerService.createInsurerRequest(
					KeyFactory.keyToString(zaqvka.getInsurerID()),
					zaqvka.getVehicle().getVIN());
			
			// set the message
			if (flag) {
				errorMessage = "Заявката беше добавена успешно!";
			} else {
				errorMessage = "Заявката НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, заявката за модела автомобил не можа да се добави успешно!";
		}
		
		// clean the data
		zaqvka = new InsurerRequest();
		
		return null;
	}

	public Key getInsurerID() {
		return zaqvka.getInsurerID();
	}

	public void setInsurerID(Key insurerID) {
		zaqvka.setInsurerID(insurerID);
	}

	public Key getVehicleID() {
		return zaqvka.getVehicleID();
	}

	public void setVehicleID(Key vehicleID) {
		zaqvka.setVehicleID(vehicleID);
	}
	
	public List<Insurer> getInsurers() {
		List<Insurer> ins = Insurer.queryGetAll(0, 1000);
		return ins;
	}
	
	public List<Vehicle> getVehicles() {
		List<Vehicle> veh = Vehicle.queryGetAll(0, 1000);
		return veh;
	}
}
