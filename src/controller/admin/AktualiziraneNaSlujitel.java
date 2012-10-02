package controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

import model.Employee;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaSlujitel")
@ViewScoped
public class AktualiziraneNaSlujitel implements Serializable {

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private Employee slujitel = new Employee();
	private int page = 0;
	private int pagesCount;
	private List<Employee> spisukSlujiteli;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaSlujitel() {
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		readList();
	}

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
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}

	public List<Employee> getSpisukSlujiteli() {
		return spisukSlujiteli;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public String writeIt() {
		slujitel.writeToDB();
		
		readList();
		
		// set the message
		errorMessage = "Служителят беше актуализиран успешно!";
		
		return null;
	}
	
	private void readList() {
		spisukSlujiteli = Employee.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		slujitel = new Employee();
		rowsCount = Employee.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize();
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Employee emp : spisukSlujiteli) {
			if (slujitel == emp) {
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
	
	public void selectRow(Employee emp) {
		slujitel = emp;
	}
	
	public void deselectRow() {
		slujitel = new Employee();
	}
	
	public boolean isRowSelected() {
		return spisukSlujiteli.contains(slujitel);
	}
	
	public String goToAdd() {
		return "DobavqneNaSlujitel.jsf?faces-redirect=true";
	}

	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseEmployee(Employee employee) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = employee;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
}
