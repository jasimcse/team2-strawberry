package controller.common;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 * 
 * Контролер занимаващ се с логване и разлогване
 * 
 */
@ManagedBean(name="loginController")
@RequestScoped
public class LoginController {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currentEmployee;
	
	private String username;
	private String password;
	
	private String errorMessage;
	

	public void login() {
		if (currentEmployee.login(username, password) == -1) {
			errorMessage = "Грешка!";
		}
	}

	public String logout() {
		currentEmployee.logout();
		return FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath() + "?faces-redirect=true";
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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
	

	public boolean isLoggedIn() {
		return currentEmployee.isLoggedIn();
	}

	public void setCurrentEmployee(CurrentEmployee currentEmployee) {
		this.currentEmployee = currentEmployee;
	}
	

}
