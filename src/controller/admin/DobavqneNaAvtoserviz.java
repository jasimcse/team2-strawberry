package controller.admin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import model.Autoservice;


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
	
	public void addAutoserviz()
	{
		serviz.writeToDB();
		serviz = new Autoservice();
		errorMessage = "Автосервизът беше добавен успешно!";
	}
	
}
