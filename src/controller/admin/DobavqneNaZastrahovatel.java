package controller.admin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.appengine.api.datastore.KeyFactory;

import model.Insurer;

@ManagedBean(name="dobavqneNaZastrahovatel")
@RequestScoped
public class DobavqneNaZastrahovatel {
	
	private Insurer zastrahovatel = new Insurer();
	
	private String errorMessage;

	public String getName() {
		return zastrahovatel.getName();
	}

	public void setName(String name) {
		zastrahovatel.setName(name);
	}

	public String getAddressCity() {
		return zastrahovatel.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		zastrahovatel.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return zastrahovatel.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		zastrahovatel.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return zastrahovatel.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		zastrahovatel.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return zastrahovatel.getMail();
	}

	public void setMail(String mail) {
		zastrahovatel.setMail(mail);
	}

	public String getIBANNumber() {
		return zastrahovatel.getIBANNumber();
	}

	public void setIBANNumber(String IBANNumber) {
		zastrahovatel.setIBANNumber(IBANNumber);
	}

	public String getSWIFTCode() {
		return zastrahovatel.getSWIFTCode();
	}

	public void setSWIFTCode(String SWIFTCode) {
		zastrahovatel.setSWIFTCode(SWIFTCode);
	}

	public String getRegistrationNumber() {
		return zastrahovatel.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		zastrahovatel.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return zastrahovatel.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) { 
			zastrahovatel.setVATNumber(VATNumber);
		}
	}

	public String getContactPerson() {
		return zastrahovatel.getContactPerson();
	}

	public void setContactPerson(String contactPerson) {
		zastrahovatel.setContactPerson(contactPerson);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public String writeIt() {
		zastrahovatel.writeToDB();
		
		// set the message
		errorMessage = "Застрахователят беше добавен успешно! Код за достъп " + KeyFactory.keyToString(zastrahovatel.getID());
		
		// clean the data
		zastrahovatel = new Insurer();
		
		return null;
	}

}
