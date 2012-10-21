package controller.admin;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import model.Autoservice;
import model.SparePart;
import model.SparePartAutoservice;


@ManagedBean(name="dobavqneNaAvtoserviz")
@RequestScoped
public class DobavqneNaAvtoserviz {
	
	private Autoservice serviz = new Autoservice();
	
	private String errorMessage;

	public String getName() {
		return serviz.getName();
	}

	public void setName(String name) {
		serviz.setName(name);
	}

	public String getAddressCity() {
		return serviz.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		serviz.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return serviz.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		serviz.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return serviz.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		serviz.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return serviz.getMail();
	}

	public void setMail(String mail) {
		serviz.setMail(mail);
	}

	public String getIBANNumber() {
		return serviz.getIBANNumber();
	}

	public void setIBANNumber(String IBANNumber) {
		serviz.setIBANNumber(IBANNumber);
	}

	public String getSWIFTCode() {
		return serviz.getSWIFTCode();
	}

	public void setSWIFTCode(String SWIFTCode) {
		serviz.setSWIFTCode(SWIFTCode);
	}

	public String getRegistrationNumber() {
		return serviz.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		serviz.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return serviz.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) { 
		    serviz.setVATNumber(VATNumber);
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void addAutoservice()
	{
		// добавяме нов сервиз -> инициализираме наличните резервни части в новия сервиз
		Transaction tr = DatastoreServiceFactory.getDatastoreService().beginTransaction(TransactionOptions.Builder.withXG(true));
		serviz.writeToDB();
		
		SparePartAutoservice spa = new SparePartAutoservice();
		spa.setQuantityAvailable(0);
		spa.setQuantityBad(0);
		spa.setQuantityMinimum(0);
		spa.setQuantityOrdered(0);
		spa.setQuantityReserved(0);
		spa.setQuantityRequested(0);
		spa.setAutoserviceID(serviz.getID());
		
		List<SparePart> spl = SparePart.queryGetAll(0, 1000);
		for (SparePart sparePart : spl) {
			spa.setSparePartID(sparePart.getID());
			spa.writeToDB(true);
		}
		
		tr.commit();
		
		
		serviz = new Autoservice();
		errorMessage = "Автосервизът беше добавен успешно!";
	}
	
}
