package controller.admin;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.google.appengine.api.datastore.Key;

import model.Autoservice;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaAvtoserviz")
@ViewScoped
public class AktualiziraneNaAvtoserviz implements Serializable {

	private Autoservice serviz = new Autoservice();
	
	private String errorMessage;
	
	private List<Autoservice> spisukAvtoservizi;

	public Key getID() {
		return serviz.getID();
	}

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
		serviz.setVATNumber(VATNumber);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void saveAutoserviz()
	{
		
		
	}
	
}
