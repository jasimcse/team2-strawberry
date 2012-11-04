package controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;

import com.google.appengine.api.utils.SystemProperty;

import model.Autoservice;
import model.Employee;
import model.EmployeeAutoservice;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.Utils;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaPotrebitel")
@ViewScoped
public class AktualiziraneNaPotrebitel implements Serializable {

	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private EmployeeAutoservice potrebitel = new EmployeeAutoservice();
	private String newPassword;
	private String repeatNewPassword;
	private int page = 0;
	private int pagesCount;
	private List<EmployeeAutoservice> spisukPotrebiteli;
	private int rowsCount;
	
	private transient UIComponent changeButton;
	
	private String errorMessage;
	
	public AktualiziraneNaPotrebitel() {
		readList();
	}
	
	public void setAllPages(AllPages allPages) {
		this.allPages = allPages;
	}

	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}
	
	public List<EmployeeAutoservice> getSpisukPotrebiteli() {
		return spisukPotrebiteli;
	}
	
	public int getPagesCount() {
		return pagesCount;
	}
	
	public boolean isGenerateNewPasswordAllowed() {
		return currEmployee.getPosition().equals(EmployeeAutoservice.ADMINISTRATOR);
	}

	public String generateNewPassword() {
		
		if (!isGenerateNewPasswordAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return null;
		}
		
		// generate random password and set it
		String generatedPassword = Utils.generateRandomPassword(
						ConfigurationProperties.getAutoGeneratedPasswordLength());
		potrebitel.setPassword(generatedPassword);
		
		potrebitel.writeToDB();
		errorMessage = "������������ ���� ������������ �������!";
		
		// send mail with the generated password to the user
		Utils.sendPasswordToUser(potrebitel.getEmployee().getMail(), generatedPassword);
		
		// show the generated password if we are executing in the development server
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
			errorMessage += " ������������ ������ � \"" + generatedPassword + "\"";
		}
		
		readList();
		
		return null;
	}
	
	public boolean isChangePasswordAllowed() {
		return currEmployee.getEmployeeID().equals(potrebitel.getEmployeeID());
	}
	
	public String changePassword() {
		
		if (!isChangePasswordAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return null;
		}
		
		if ((newPassword != null) && !newPassword.equals("") && newPassword.equals(repeatNewPassword)) {
			potrebitel.setPassword(newPassword);
		}
		
		potrebitel.writeToDB();
		
		// send mail with the generated password to the user
		Utils.sendPasswordToUser(potrebitel.getEmployee().getMail(), newPassword);
		
		readList();
		
		// set the message
		errorMessage = "������������ ���� ������������ �������!";
		
		return null;
	}
	
	public void changePosition() {
		
		if (!isGenerateNewPasswordAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return;
		}
		
		potrebitel.writeToDB();
		
		readList();
		
		// set the message
		errorMessage = "������������ ���� ������������ �������!";
	}
	
	private void readList() {
		spisukPotrebiteli = EmployeeAutoservice.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		potrebitel = new EmployeeAutoservice();
		newPassword = null;
		repeatNewPassword = null;
		rowsCount = EmployeeAutoservice.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (EmployeeAutoservice empAuto : spisukPotrebiteli) {
			if (potrebitel == empAuto) {
				strbuff.append("selectedRow,");
			} else {
				strbuff.append("notSelectedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public List<Integer> getPagesList() {
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i=0; i < pagesCount; i++)
			list.add(Integer.valueOf(i+1));
		
		return list;
	}
	
	public void selectRow(EmployeeAutoservice empAuto) {
		potrebitel = empAuto;
	}
	
	public void deselectRow() {
		potrebitel = new EmployeeAutoservice();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukPotrebiteli.contains(potrebitel);
	}
	
	public UIComponent getChangeButton() {
		return changeButton;
	}

	public void setChangeButton(UIComponent changeButton) {
		this.changeButton = changeButton;
	}

	public String getEmployeeName() {
		Employee emp = potrebitel.getEmployee();
		if (emp == null) {
			return null;
		}
		return emp.getName();
	}
	
	public String getEmployeeFamily() {
		Employee emp = potrebitel.getEmployee();
		if (emp == null) {
			return null;
		}
		return emp.getFamily();
	}
	
	public String getAutoserviceName() {
		Autoservice auto = potrebitel.getAutoservice();
		if (auto == null) {
			return null;
		}
		return auto.getName();
	}

	public String getUsername() {
		return potrebitel.getUsername();
	}

	public String getPosition() {
		return potrebitel.getPosition();
	}

	public void setPosition(String position) {
		potrebitel.setPosition(position);
	}
	
	public Map<String, String> getPositions() {
		Map<String, String> pos = new TreeMap<String, String>();
		pos.put("�������������", EmployeeAutoservice.ADMINISTRATOR);
		pos.put("���������", EmployeeAutoservice.MANAGER);
		pos.put("��������/������", EmployeeAutoservice.CASHIER);
		pos.put("����������", EmployeeAutoservice.DIAGNOSTICIAN);
		pos.put("�������� �����", EmployeeAutoservice.WAREHOUSEMAN);
		pos.put("�����������", EmployeeAutoservice.AUTO_MECHANIC);
		pos.put("���������", EmployeeAutoservice.DELETED_USER);
		return pos;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getRepeatNewPassword() {
		return repeatNewPassword;
	}

	public void setRepeatNewPassword(String repeatNewPassword) {
		this.repeatNewPassword = repeatNewPassword;
	}
	
}
