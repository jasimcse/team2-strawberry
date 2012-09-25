package controller.common;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.google.appengine.api.datastore.Key;


@ManagedBean(name="loginController")
@RequestScoped
public class loginController {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currentEmployee;
	
	private String username;
	private String password;
	
	private String errorMessage;
	

	public void login() {
		if (currentEmployee.login(username, password) == -1) {
			errorMessage = "Login failed!";
		}
	}

	public void logout() {
		currentEmployee.logout();
	}

	public boolean isLoggedIn() {
		return currentEmployee.isLoggedIn();
	}

	public Key getAutoserviceID() {
		return currentEmployee.getAutoserviceID();
	}

	public Key getEmployeeID() {
		return currentEmployee.getEmployeeID();
	}

	public String getUsername() {
		if (isLoggedIn()) {
			return currentEmployee.getUsername();
		} else {
			return null;
		}
	}

	public String getPosition() {
		return currentEmployee.getPosition();
	}

	public String getAutoserviceName() {
		return currentEmployee.getAutoserviceName();
	}
	
	public void setCurrentEmployee(CurrentEmployee currentEmployee) {
		this.currentEmployee = currentEmployee;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
