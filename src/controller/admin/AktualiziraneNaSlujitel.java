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
	
	private String searchName;
	private String searchFamily;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaSlujitel() {
		
		// проверяваме дали има заявки записани във flash-а; записваме си стека със заявките, за да го възстановим след това
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			// има заявки във flash-а
			// проверяваме дали заявката е към текущата страница
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
			// ако (dataRequest != null) значи има направена заявка към текущата страница и заявката се пази в dataRequest
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
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
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
		readList();
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
		// проверка против грешно извикване
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		// слагаме исканите данни в заявката
		dataRequest.requestedObject = employee;
		// слагаме стека който сме прочели в конструктора пак във flash-а
		// забележка: данните за текущата заявка си стоят в стека; само сме добавили исканите данни
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// отиваме на страницата която е направила заявката
		return dataRequest.returnPage + "?faces-redirect=true";
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		if ("".equals(searchName)) {
			this.searchName = null;
			return;
		}
		this.searchName = searchName;
	}

	public String getSearchFamily() {
		return searchFamily;
	}

	public void setSearchFamily(String searchFamily) {
		if ("".equals(searchFamily)) {
			this.searchFamily = null;
			return;
		}
		this.searchFamily = searchFamily;
	}
	
	public void searchIt() {
		if ((searchName == null) && (searchFamily == null)) {
			readList();
			return;
		}
		
		spisukSlujiteli = Employee.querySearchByNameFamily(searchName, searchFamily, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		page = 0;
		rowsCount = Employee.countSearchByNameFamily(searchName, searchFamily);
		slujitel = new Employee();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public void resetSearch() {
		searchName = null;
		searchFamily = null;
		searchIt();
	}
}
