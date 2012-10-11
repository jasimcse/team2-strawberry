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
import model.VehicleModel;
import model.VehicleModelSparePart;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.utils.SystemProperty;

import controller.common.ConfigurationProperties;
import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

@ManagedBean(name="dobavqneNaRezervna4astZaModelAvtomobil")
@RequestScoped
public class DobavqneNaRezervna4astZaModelAvtomobil {

	private VehicleModelSparePart rezervna4astZaModel = new VehicleModelSparePart();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String writeIt() {
		//rezervna4astZaModel.writeToDB();
		
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
			boolean flag = eshop.createOrUpdateVehicleModelPart(
					ConfigurationProperties.getElectronicShopID(),
					rezervna4astZaModel.getVehicleModel().getForeignID(),
					rezervna4astZaModel.getSparePart().getForeignID());
			
			// set the message
			if (flag) {
				errorMessage = "Резервната част за модела автомобил беше добавена успешно!";
			} else {
				errorMessage = "Резервната част за модела автомобил НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, резервната част за модела автомобил не можа да се добави успешно!";
		}
		
		// clean the data
		rezervna4astZaModel = new VehicleModelSparePart();
		
		return null;
	}

	public Key getVehicleModelID() {
		return rezervna4astZaModel.getVehicleModelID();
	}

	public void setVehicleModelID(Key vehicleModelID) {
		rezervna4astZaModel.setVehicleModelID(vehicleModelID);
	}

	public Key getSparePartID() {
		return rezervna4astZaModel.getSparePartID();
	}

	public void setSparePartID(Key sparePartID) {
		rezervna4astZaModel.setSparePartID(sparePartID);
	}
	
	public List<VehicleModel> getVehicleModels() {
		List<VehicleModel> vm = VehicleModel.queryGetAll(0, 1000);
		return vm;
	}
	
	public List<SparePart> getSpareParts() {
		List<SparePart> sp = SparePart.queryGetAll(0, 1000);
		return sp;
	}
	
}
