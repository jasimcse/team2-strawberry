package controller.admin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.appengine.api.datastore.KeyFactory;

import model.Supplier;


@ManagedBean(name="dobavqneNaDostav4ik")
@RequestScoped
public class DobavqneNaDostav4ik {
	
	private Supplier dostav4ik = new Supplier();
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	

	public String getName() {
		return dostav4ik.getName();
	}


	public void setName(String name) {
		dostav4ik.setName(name);
	}


	public String getAddressCity() {
		return dostav4ik.getAddressCity();
	}


	public void setAddressCity(String addressCity) {
		dostav4ik.setAddressCity(addressCity);
	}


	public String getAddressLine() {
		return dostav4ik.getAddressLine();
	}


	public void setAddressLine(String addressLine) {
		dostav4ik.setAddressLine(addressLine);
	}


	public String getPhoneNumber() {
		return dostav4ik.getPhoneNumber();
	}


	public void setPhoneNumber(String phoneNumber) {
		dostav4ik.setPhoneNumber(phoneNumber);
	}


	public String getMail() {
		return dostav4ik.getMail();
	}


	public void setMail(String mail) {
		dostav4ik.setMail(mail);
	}


	public String getIBANNumber() {
		return dostav4ik.getIBANNumber();
	}


	public void setIBANNumber(String IBANNumber) {
		dostav4ik.setIBANNumber(IBANNumber);
	}


	public String getSWIFTCode() {
		return dostav4ik.getSWIFTCode();
	}


	public void setSWIFTCode(String SWIFTCode) {
		dostav4ik.setSWIFTCode(SWIFTCode);
	}


	public String getRegistrationNumber() {
		return dostav4ik.getRegistrationNumber();
	}


	public void setRegistrationNumber(String registrationNumber) {
		dostav4ik.setRegistrationNumber(registrationNumber);
	}


	public String getVATNumber() {
		return dostav4ik.getVATNumber();
	}


	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) { 
			dostav4ik.setVATNumber(VATNumber);
		}
	}


	public String getContactPerson() {
		return dostav4ik.getContactPerson();
	}


	public void setContactPerson(String contactPerson) {
		dostav4ik.setContactPerson(contactPerson);
	}

	public String writeIt() {
		dostav4ik.writeToDB();
		
		// set the message
		errorMessage = "Доставчикът беше добавен успешно! Код за достъп " + KeyFactory.keyToString(dostav4ik.getID());
		
		// clean the data
		dostav4ik = new Supplier();
		
		return null;
	}
}
