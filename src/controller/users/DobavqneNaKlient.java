package controller.users;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


import model.Client;
import model.Company;
import model.Person;

@ManagedBean(name="dobavqneNaKlient")
@RequestScoped

public class DobavqneNaKlient {

	private Client klient = new Client();
	private Person person = new Person();
	private Company company = new Company();
	
	private String errorMessage;

	public DobavqneNaKlient() {
		
		klient.setPersonCompany("P");
	}

	public String getPersonCompany() {
		return klient.getPersonCompany();
	}

	public void setPersonCompany(String personCompany) {
		
		klient.setPersonCompany(personCompany);
	}

	public boolean isPersonCompany()
	{
		return getPersonCompany().equals("P");
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

	public void setClient(Client client) {
		company.setClient(client);
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void addClient()
	{
		klient.writeToDB();
		
		if(klient.getPersonCompany().equals("P"))
			person.writeToDB();
		else
			company.writeToDB();
			
		
		person = new Person();
		company = new Company();
		klient = new Client();
		klient.setPersonCompany("P");
		
		errorMessage = "Клиентът беше добавен успешно!";
	}
	
}
