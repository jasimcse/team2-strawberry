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


import model.WarrantyConditions;

@ManagedBean(name="dobavqneNaGarancionniUsloviq")
@RequestScoped
public class DobavqneNaGarancionniUsloviq {

	private WarrantyConditions garancionniUsloviq = new WarrantyConditions();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//garancionniUsloviq.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateWarrantyConditions(
					ConfigurationProperties.getElectronicShopID(),
					garancionniUsloviq.getForeignID(),
					garancionniUsloviq.getMonths(),
					garancionniUsloviq.getMileage(),
					garancionniUsloviq.getOtherConditions());
			
			// set the message
			if (flag) {
				errorMessage = "Гаранционните условия бяха добавени успешно!";
			} else {
				errorMessage = "Гаранционните условия НЕ бяха добавени успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, гаранционните условия не можаха да се добавят успешно!";
		}
		
		// clean the data
		garancionniUsloviq = new WarrantyConditions();
		
		return null;
	}

	public long getMonths() {
		return garancionniUsloviq.getMonths();
	}

	public void setMonths(long months) {
		garancionniUsloviq.setMonths(months);
	}

	public long getMileage() {
		return garancionniUsloviq.getMileage();
	}

	public void setMileage(long mileage) {
		garancionniUsloviq.setMileage(mileage);
	}

	public String getOtherConditions() {
		return garancionniUsloviq.getOtherConditions();
	}

	public void setOtherConditions(String otherConditions) {
		garancionniUsloviq.setOtherConditions(otherConditions);
	}

	public String getForeignID() {
		return garancionniUsloviq.getForeignID();
	}

	public void setForeignID(String foreignID) {
		garancionniUsloviq.setForeignID(foreignID);
	}
	
	
}
