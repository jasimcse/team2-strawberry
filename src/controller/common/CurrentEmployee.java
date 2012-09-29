package controller.common;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.EmployeeAutoservice;

import com.google.appengine.api.datastore.Key;


/**
 * Съхранява данните на логнатия потребител
 *
 */
@ManagedBean(name="currentEmployee")
@SessionScoped
public class CurrentEmployee {
	
	private EmployeeAutoservice employeeAutoservice;
	
	/**
	 * checks for correct username and password;
	 *  - if correct the user information is stored in this object; the user is considered logged in from now on
	 *  - if not  -> throw RuntimeException
	 *   
	 * @return -1/0 = No such user / user has been logged in
	 */
	public int login(String username, String password) {
		
		// query to search the DB
		employeeAutoservice = EmployeeAutoservice.checkCredentials(username, password);
		
		if (employeeAutoservice == null) {
			// no such credentials
			return -1;
		}
		return 0;
	}
	
	/**
	 * logout the current user
	 * 
	 * @return
	 */
	public void logout() {
		employeeAutoservice = null;
	}
	
	
	public boolean isLoggedIn() {
		return !(employeeAutoservice == null);
	}

	public Key getAutoserviceID() {
		if (employeeAutoservice == null) {
			throw new RuntimeException("Current employee is not logged in!");
		} else {
			return employeeAutoservice.getAutoserviceID();
		}
	}

	public Key getEmployeeID() {
		if (employeeAutoservice == null) {
			throw new RuntimeException("Current employee is not logged in!");
		} else {
			return employeeAutoservice.getEmployeeID();
		}
	}

	public String getUsername() {
		if (employeeAutoservice == null) {
			throw new RuntimeException("Current employee is not logged in!");
		} else {
			return employeeAutoservice.getUsername();
		}
	}

	public String getPosition() {
		if (employeeAutoservice == null) {
			throw new RuntimeException("Current employee is not logged in!");
		} else {
			return employeeAutoservice.getPosition();
		}
	}
	
	public String getAutoserviceName() {
		if (employeeAutoservice == null) {
			throw new RuntimeException("Current employee is not logged in!");
		} else {
			return employeeAutoservice.getAutoservice().getName();
		}
	}
	
}
