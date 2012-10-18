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
import model.SparePartSupplier;
import model.Supplier;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.utils.SystemProperty;

import controller.serverCommunication.SOAPServer.SupplierInterface;

@ManagedBean(name="dobavqneNaRezervna4astOtDrugDostav4ik")
@RequestScoped
public class DobavqneNaRezervna4astOtDrugDostav4ik {
	
	private SparePartSupplier rezervna4ast = new SparePartSupplier();
	
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
			url = new URL(host + "/wsdl/SupplierService.wsdl");
			qualifiedName = new QName("http://SOAPServer.serverCommunication.controller/", "SupplierService");
			Service service = Service.create(url, qualifiedName);
			SupplierInterface supplierService = service.getPort(SupplierInterface.class);
			boolean flag = supplierService.createOrUpdateSparePartSupplier(
					KeyFactory.keyToString(rezervna4ast.getSupplierID()),
					rezervna4ast.getSparePart().getForeignID(),
					rezervna4ast.getForeignID(),
					rezervna4ast.getDeliveryPrice());
			
			// set the message
			if (flag) {
				errorMessage = "Резервната част от друг доставчик беше добавена успешно!";
			} else {
				errorMessage = "Резервната част от друг доставчик НЕ беше добавена успешно! За повече информация виж лога на сървъра или конзолата му.";
			}
		} catch (MalformedURLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ooopsie", e);
			// set the message
			errorMessage = "Грешка, резервната част от друг доставчик не можа да се добави успешно!";
		}
		
		// clean the data
		rezervna4ast = new SparePartSupplier();
		
		return null;
	}

	public Key getSupplierID() {
		return rezervna4ast.getSupplierID();
	}

	public void setSupplierID(Key supplierID) {
		rezervna4ast.setSupplierID(supplierID);
	}

	public Key getSparePartID() {
		return rezervna4ast.getSparePartID();
	}

	public void setSparePartID(Key sparePartID) {
		rezervna4ast.setSparePartID(sparePartID);
	}

	public double getDeliveryPrice() {
		return rezervna4ast.getDeliveryPrice();
	}

	public void setDeliveryPrice(double deliveryPrice) {
		rezervna4ast.setDeliveryPrice(deliveryPrice);
	}

	public String getForeignID() {
		return rezervna4ast.getForeignID();
	}

	public void setForeignID(String foreignID) {
		rezervna4ast.setForeignID(foreignID);
	}
	
	public List<Supplier> getSuppliers() {
		List<Supplier> sup = Supplier.queryGetAll(0, 1000);
		return sup;
	}
	
	public List<SparePart> getSpareParts() {
		List<SparePart> sp = SparePart.queryGetAll(0, 1000);
		return sp;
	}

}
