package controller.debug;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.google.appengine.api.utils.SystemProperty;

import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;


//NOTE: не се добавя цената на услугата понеже тя се определя от сервизите, т.е. нашата система

@ManagedBean(name="dobavqneNaUsluga")
@RequestScoped
public class DobavqneNaUsluga {

	private model.Service usluga = new model.Service();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//usluga.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateService(
					ConfigurationProperties.getElectronicShopID(),
					usluga.getForeignID(),
					usluga.getDescription());
			
			// set the message
			if (flag) {
				errorMessage = "Услугата беше добавена успешно!";
			} else {
				errorMessage = "Услугата НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, услугата не можа да се добави успешно!";
		}
		
		// clean the data
		usluga = new model.Service();
		
		return null;
	}

	public String getDescription() {
		return usluga.getDescription();
	}

	public void setDescription(String description) {
		usluga.setDescription(description);
	}

	public String getForeignID() {
		return usluga.getForeignID();
	}

	public void setForeignID(String foreignID) {
		usluga.setForeignID(foreignID);
	}
	
}
