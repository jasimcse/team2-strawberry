package controller.admin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import model.Employee;


@ManagedBean(name="dobavqneNaSlujitel")
@RequestScoped
public class DobavqneNaSlujitel {
	
	private Employee slujitel = new Employee();
	
	private String errorMessage;

	public String getName() {
		return slujitel.getName();
	}

	public void setName(String name) {
		slujitel.setName(name);
	}

	public String getFamily() {
		return slujitel.getFamily();
	}

	public void setFamily(String family) {
		slujitel.setFamily(family);
	}

	public String getAddressCity() {
		return slujitel.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		slujitel.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return slujitel.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		slujitel.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return slujitel.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		slujitel.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return slujitel.getMail();
	}

	public void setMail(String mail) {
		slujitel.setMail(mail);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public String writeIt() {
		slujitel.writeToDB();
		
		// clean the data
		slujitel= new Employee();
		
		// set the message
		errorMessage = "Служителят беше добавен успешно!";
		
		return null;
	}
	
}
