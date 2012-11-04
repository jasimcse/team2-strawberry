package controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;

import model.Employee;
import model.EmployeeAutoservice;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaPotrebitel")
@ViewScoped
public class AktualiziraneNaPotrebitel implements Serializable {

	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private EmployeeAutoservice potrebitel = new EmployeeAutoservice();
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

	public String generateNewPassword() {
		
//		if (!isChangingAllowed()) {
//			errorMessage = "Нямате права за актуализирането на данните!";
//			return null;
//		}
//		
//		slujitel.writeToDB();
//		
//		readList();
//		
//		// set the message
//		errorMessage = "Служителят беше актуализиран успешно!";
		
		return null;
	}
	
	public String changePassword() {
		
//		if (!isChangingAllowed()) {
//			errorMessage = "Нямате права за актуализирането на данните!";
//			return null;
//		}
//		
//		slujitel.writeToDB();
//		
//		readList();
//		
//		// set the message
//		errorMessage = "Служителят беше актуализиран успешно!";
		
		return null;
	}
	
	private void readList() {
		spisukPotrebiteli = EmployeeAutoservice.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		potrebitel = new EmployeeAutoservice();
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
	
	public boolean generateNewPasswordAllowed() {
		return currEmployee.getPosition().equals(EmployeeAutoservice.ADMINISTRATOR);
	}
	
	public UIComponent getChangeButton() {
		return changeButton;
	}

	public void setChangeButton(UIComponent changeButton) {
		this.changeButton = changeButton;
	}

	public String getName() {
		Employee emp = potrebitel.getEmployee();
		if (emp == null) {
			return null;
		}
		return emp.getName();
	}
	
	public String getFamily() {
		Employee emp = potrebitel.getEmployee();
		if (emp == null) {
			return null;
		}
		return emp.getFamily();
	}
	
}
