package controller.users;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;


import model.Client;
import model.Company;
import model.Person;
import model.util.UniqueAttributeException;

@ManagedBean(name="dobavqneNaKlient")
@RequestScoped
public class DobavqneNaKlient {

	private Client klientPerson;
	private Client klientCompany;
	
	private transient UIComponent addPerson;
	private transient UIComponent addCompany;
	
	private String errorMessage;
	
	public DobavqneNaKlient() {
		init();
	}
	
	private void init() {
		klientPerson = new Client();
		klientCompany = new Client();
		klientPerson.setPerson(new Person());
		klientCompany.setCompany(new Company());
	}
	
	public String getAddressCityPerson() {
		return klientPerson.getAddressCity();
	}

	public void setAddressCityPerson(String addressCity) {
		klientPerson.setAddressCity(addressCity);
	}

	public String getAddressLinePerson() {
		return klientPerson.getAddressLine();
	}

	public void setAddressLinePerson(String addressLine) {
		klientPerson.setAddressLine(addressLine);
	}

	public String getPhoneNumberPerson() {
		return klientPerson.getPhoneNumber();
	}

	public void setPhoneNumberPerson(String phoneNumber) {
		klientPerson.setPhoneNumber(phoneNumber);
	}

	public String getMailPerson() {
		return klientPerson.getMail();
	}

	public void setMailPerson(String mail) {
		klientPerson.setMail(mail);
	}

	public String getIBANNumberPerson() {
		return klientPerson.getIBANNumber();
	}

	public void setIBANNumberPerson(String iBANNumber) {
		klientPerson.setIBANNumber(iBANNumber);
	}

	public String getSWIFTCodePerson() {
		return klientPerson.getSWIFTCode();
	}

	public void setSWIFTCodePerson(String sWIFTCode) {
		klientPerson.setSWIFTCode(sWIFTCode);
	}
	
	public String getNamePerson() {
		return klientPerson.getPerson().getName();
	}

	public void setNamePerson(String name) {
		klientPerson.getPerson().setName(name);
	}

	public String getFamilyPerson() {
		return klientPerson.getPerson().getFamily();
	}

	public void setFamilyPerson(String family) {
		klientPerson.getPerson().setFamily(family);
	}
	
	
	
	
	public String getAddressCityCompany() {
		return klientCompany.getAddressCity();
	}

	public void setAddressCityCompany(String addressCity) {
		klientCompany.setAddressCity(addressCity);
	}

	public String getAddressLineCompany() {
		return klientCompany.getAddressLine();
	}

	public void setAddressLineCompany(String addressLine) {
		klientCompany.setAddressLine(addressLine);
	}

	public String getPhoneNumberCompany() {
		return klientCompany.getPhoneNumber();
	}

	public void setPhoneNumberCompany(String phoneNumber) {
		klientCompany.setPhoneNumber(phoneNumber);
	}

	public String getMailCompany() {
		return klientCompany.getMail();
	}

	public void setMailCompany(String mail) {
		klientCompany.setMail(mail);
	}

	public String getIBANNumberCompany() {
		return klientCompany.getIBANNumber();
	}

	public void setIBANNumberCompany(String iBANNumber) {
		klientCompany.setIBANNumber(iBANNumber);
	}

	public String getSWIFTCodeCompany() {
		return klientCompany.getSWIFTCode();
	}

	public void setSWIFTCodeCompany(String sWIFTCode) {
		klientCompany.setSWIFTCode(sWIFTCode);
	}

	public String getNameCompany() {
		return klientCompany.getCompany().getName();
	}

	public void setNameCompany(String name) {
		klientCompany.getCompany().setName(name);
	}

	public String getRegistrationNumberCompany() {
		return klientCompany.getCompany().getRegistrationNumber();
	}

	public void setRegistrationNumberCompany(String registrationNumber) {
		
		if (!"".equals(registrationNumber)) 
			klientCompany.getCompany().setRegistrationNumber(registrationNumber);
	}

	public String getVATNumberCompany() {
		return klientCompany.getCompany().getVATNumber();
	}

	public void setVATNumberCompany(String VATNumber) {
		
		if (!"".equals(VATNumber)) 
			klientCompany.getCompany().setVATNumber(VATNumber);
	}

	public String getContactPersonCompany() {
		return klientCompany.getCompany().getContactPerson();
	}

	public void setContactPersonCompany(String contactPerson) {
		klientCompany.getCompany().setContactPerson(contactPerson);
	}

	public UIComponent getAddPerson() {
		return addPerson;
	}

	public void setAddPerson(UIComponent addPerson) {
		this.addPerson = addPerson;
	}

	public UIComponent getAddCompany() {
		return addCompany;
	}

	public void setAddCompany(UIComponent addCompany) {
		this.addCompany = addCompany;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void addPerson()
	{
		try {
			klientPerson.writeToDB();
			errorMessage = "Клиентът беше добавен успешно!";
		} catch (UniqueAttributeException e) {
			errorMessage = "Неуникални полета!";
			return;
		}

		init();
	}
	
	public void addCompany() {
		try {
			klientCompany.writeToDB();
			errorMessage = "Клиентът беше добавен успешно!";
		} catch (UniqueAttributeException e) {
			errorMessage = "Неуникални полета!";
			return;
		}
		
		init();
	}
	
}
