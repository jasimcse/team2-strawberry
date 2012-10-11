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

import model.SparePart;
import model.SparePartGroup;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.utils.SystemProperty;

import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

//NOTE: не се добавя продажната цена на резервната част  понеже тя се определя от сервизите, т.е. нашата система

@ManagedBean(name="dobavqneNaRezervna4ast")
@RequestScoped
public class DobavqneNaRezervna4ast {

	private SparePart rezervna4ast = new SparePart();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//rezervna4ast.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateSparePart(
					ConfigurationProperties.getElectronicShopID(),
					rezervna4ast.getForeignID(),
					rezervna4ast.getSparePartGroup().getForeignID(),
					rezervna4ast.getDescription(),
					rezervna4ast.getDeliveryPrice(),
					rezervna4ast.getMeasuringUnit());
			
			// set the message
			if (flag) {
				errorMessage = "Резервната част беше добавена успешно!";
			} else {
				errorMessage = "Резервната част НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, резервната част не можа да се добави успешно!";
		}
		
		// clean the data
		rezervna4ast = new SparePart();
		
		return null;
	}

	public String getDescription() {
		return rezervna4ast.getDescription();
	}

	public void setDescription(String description) {
		rezervna4ast.setDescription(description);
	}

	public double getDeliveryPrice() {
		return rezervna4ast.getDeliveryPrice();
	}

	public void setDeliveryPrice(double deliveryPrice) {
		rezervna4ast.setDeliveryPrice(deliveryPrice);
	}

	public String getMeasuringUnit() {
		return rezervna4ast.getMeasuringUnit();
	}

	public void setMeasuringUnit(String measuringUnit) {
		rezervna4ast.setMeasuringUnit(measuringUnit);
	}

	public String getForeignID() {
		return rezervna4ast.getForeignID();
	}

	public void setForeignID(String foreignID) {
		rezervna4ast.setForeignID(foreignID);
	}
	
	public List<SparePartGroup> getSparePartGroups() {
		List<SparePartGroup> spg = SparePartGroup.queryGetAll(0, 1000);
		return spg;
	}
	
	public Key getSparePartGroupID() {
		return rezervna4ast.getSparePartGroupID();
	}

	public void setSparePartGroupID(Key sparePartGroupID) {
		rezervna4ast.setSparePartGroupID(sparePartGroupID);
	}
	
}
