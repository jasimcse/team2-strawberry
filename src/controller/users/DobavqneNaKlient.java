package controller.users;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;

import com.google.appengine.api.datastore.Key;


import model.Client;
import model.Company;
import model.Person;

@ManagedBean(name="dobavqneNaKlient")
@RequestScoped

public class DobavqneNaKlient {

	private Client klient = new Client();
	private Person person = new Person();
	private Company company = new Company();
	
	private transient UIComponent addPerson;
	private transient UIComponent addCompany;
	
	private String errorMessage;
	
	public Key getID() {
		return klient.getID();
	}

	public String getAddressCity() {
		return klient.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		klient.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return klient.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		klient.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return klient.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		klient.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return klient.getMail();
	}

	public void setMail(String mail) {
		klient.setMail(mail);
	}

	public String getIBANNumber() {
		return klient.getIBANNumber();
	}

	public void setIBANNumber(String iBANNumber) {
		klient.setIBANNumber(iBANNumber);
	}

	public String getSWIFTCode() {
		return klient.getSWIFTCode();
	}

	public void setSWIFTCode(String sWIFTCode) {
		klient.setSWIFTCode(sWIFTCode);
	}
	
	public String getPersonName() {
		return person.getName();
	}

	public void setPersonName(String name) {
		person.setName(name);
		
	}

	public String getFamily() {
		return person.getFamily();
	}

	public void setFamily(String family) {
		person.setFamily(family);
	}

	public String getCompanyName() {
		return company.getName();
	}

	public void setCompanyName(String name) {
		company.setName(name);
	}

	public String getRegistrationNumber() {
		return company.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		
		if (!"".equals(registrationNumber)) 
			company.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return company.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		
		if (!"".equals(VATNumber)) 
		    company.setVATNumber(VATNumber);
	}

	public String getContactPerson() {
		return company.getContactPerson();
	}

	public void setContactPerson(String contactPerson) {
		company.setContactPerson(contactPerson);
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
		klient.setPersonCompany("P");
		klient.writeToDB();
		

		person.setClient(klient);
		person.writeToDB();
		
		person = new Person();
		company = new Company();
		klient = new Client();

		
		errorMessage = "Клиентът беше добавен успешно!";
	}
	
	public void addCompany()
	{
		klient.setPersonCompany("C");
		klient.writeToDB();
		
		company.setClient(klient);
		company.writeToDB();
	
		person = new Person();
		company = new Company();
		klient = new Client();

		errorMessage = "Клиентът беше добавен успешно!";
	}
	
}
