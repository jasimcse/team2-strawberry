package controller.common;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.EmployeeAutoservice;

import com.google.appengine.api.datastore.Key;


/**
 * Съхранява данните на логнатия потребител
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name="currentEmployee")
@SessionScoped
public class CurrentEmployee implements Serializable {
	
	private EmployeeAutoservice employeeAutoservice;
	
	/**
	 * checks for correct username and password;
	 * if correct, the user information is stored in this object; the user is considered logged in from now on
	 *   
	 * @return -1/0 = No such user / user has been logged in
	 */
	public int login(String username, String password) {
		
		// query to search the DB
		if (EmployeeAutoservice.countGetByUsernamePassword(username, password) == 1) {
			employeeAutoservice = EmployeeAutoservice.queryGetByUsernamePassword(username, password, 0, 1).get(0);
			if (employeeAutoservice.getPosition().equals(EmployeeAutoservice.DELETED_USER)) {
				employeeAutoservice = null;
			}
		} else {
			employeeAutoservice = null;
		}
		
		if (employeeAutoservice == null) {
			// no such credentials
			return -1;
		}
		return 0;
	}
	
	/**
	 * logout the current user
	 * 
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
			return null;
		} else {
			return employeeAutoservice.getPosition();
		}
	}
	
	public String getPositionString() {
		if (employeeAutoservice == null) {
			throw new RuntimeException("Current employee is not logged in!");
		} else {
			String pos = employeeAutoservice.getPosition();
			if (EmployeeAutoservice.ADMINISTRATOR.equals(pos)) {
				return "Администратор";
			}
			if (EmployeeAutoservice.MANAGER.equals(pos)) {
				return "Управител";
			}
			if (EmployeeAutoservice.CASHIER.equals(pos)) {
				return "Приемчик/Касиер";
			}
			if (EmployeeAutoservice.DIAGNOSTICIAN.equals(pos)) {
				return "Диагностик";
			}
			if (EmployeeAutoservice.WAREHOUSEMAN.equals(pos)) {
				return "Началник склад";
			}
			if (EmployeeAutoservice.AUTO_MECHANIC.equals(pos)) {
				return "Автомонтьор";
			}
			return null;
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
