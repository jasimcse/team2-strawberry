package controller.debug;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import model.SparePartGroup;

import com.google.appengine.api.utils.SystemProperty;

import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

@ManagedBean(name="dobavqneNaGrupaRezervni4asti")
@RequestScoped
public class DobavqneNaGrupaRezervni4asti {

	private SparePartGroup grupaRezervni4ast = new SparePartGroup();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//grupaRezervni4ast.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateSparePartGroup(
					ConfigurationProperties.getElectronicShopID(),
					grupaRezervni4ast.getForeignID(),
					grupaRezervni4ast.getDescription());
			
			// set the message
			if (flag) {
				errorMessage = "Групата резервни части беше добавена успешно!";
			} else {
				errorMessage = "Групата резервни части НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, групата резервни части не можа да се добави успешно!";
		}
		
		// clean the data
		grupaRezervni4ast = new SparePartGroup();
		
		return null;
	}

	public String getDescription() {
		return grupaRezervni4ast.getDescription();
	}

	public void setDescription(String description) {
		grupaRezervni4ast.setDescription(description);
	}

	public String getForeignID() {
		return grupaRezervni4ast.getForeignID();
	}

	public void setForeignID(String foreignID) {
		grupaRezervni4ast.setForeignID(foreignID);
	}
	
}
