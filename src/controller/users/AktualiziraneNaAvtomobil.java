package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

import model.Client;
import model.Company;
import model.Person;
import model.Vehicle;
import model.VehicleModel;
import model.WarrantyConditions;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaAvtomobil")
@ViewScoped
public class AktualiziraneNaAvtomobil implements Serializable {

	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Vehicle avtomobil = new Vehicle();
	private long mileage;
	
	private transient UIComponent changeButton;
	
	private String errorMessage;
	
	private List<Vehicle> spisukAvtomobili;

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private String searchPlateNumber;
	private String searchNamePerson;
	private String searchFamilyPerson;
	private String searchNameCompany;
	
	private InterPageDataRequest dataRequest;

	@SuppressWarnings("unchecked")
	public AktualiziraneNaAvtomobil() {
		readList();
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequestNew = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequestNew.returnPage)) { 
				avtomobil = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).avtomobil;
				spisukAvtomobili = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).spisukAvtomobili;
				page = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).page;
				pagesCount = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).pagesCount;
				rowsCount = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).rowsCount;
						
				if (dataRequestNew.dataPage.equals("/users/AktualiziraneNaKlient.jsf")) {
					this.avtomobil.setClient((Client)dataRequestNew.requestedObject);
				}
			}
		}
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
	}
	
	public Client getClient() {
		return avtomobil.getClient();
	}

	public VehicleModel getVehicleModel() {
		return avtomobil.getVehicleModel();
	}


	public WarrantyConditions getWarrantyConditions() {
		return avtomobil.getWarrantyConditions();
	}

	public String getVIN() {
		return avtomobil.getVIN();
	}

	public String getEngineNumber() {
		return avtomobil.getEngineNumber();
	}

	public void setEngineNumber(String engineNumber) {
		avtomobil.setEngineNumber(engineNumber);
	}

	public String getPlateNumber() {
		return avtomobil.getPlateNumber();
	}

	public void setPlateNumber(String plateNumber) {
		avtomobil.setPlateNumber(plateNumber);
	}

	public String getWarrantyOK() {
		if (avtomobil.getWarrantyOK() == Vehicle.WARRANTY_YES) {
			return "ДА";
		} else if (avtomobil.getWarrantyOK() == Vehicle.WARRANTY_NO) {
			return "НЕ";
		}
		return "НЕ";
	}

	public Date getPurchaseDate() {
		return avtomobil.getPurchaseDate();
	}

	
	public long getMileage() {
		return mileage;
	}

	public void setMileage(long mileage) {
		this.mileage = mileage;
	}

	public UIComponent getChangeButton() {
		return changeButton;
	}

	public void setChangeButton(UIComponent changeButton) {
		this.changeButton = changeButton;
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


	public List<Vehicle> getSpisukAvtomobili() {
		return spisukAvtomobili;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukAvtomobili = Vehicle.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		avtomobil = new Vehicle();
		mileage = 0;
		rowsCount = Vehicle.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}

	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Vehicle avto : spisukAvtomobili) {
			if (avtomobil == avto) {
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
	
	public void selectRow(Vehicle avto) {
		avtomobil = avto;
	}
	
	public void deselectRow() {
		avtomobil = new Vehicle();
		readList();
		
	}

	public boolean isRowSelected() {
		return spisukAvtomobili.contains(avtomobil);
	}
	
	public String goToAdd() {
		return "DobavqneNaAvtomobil.jsf?faces-redirect=true";
	}
	
	public boolean isGoToAddAllowed() {
		return allPages.getReadRight(
					"/users/DobavqneNaAvtomobil.jsf",
					currEmployee.getPosition());
	}
	
	public boolean isChangingAllowed() {
		return allPages.getWriteRight(
				FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath(),
				currEmployee.getPosition());
	}

	public boolean isChoosingAllowed() {
		return (dataRequest != null);
	}
	
	public String chooseAvtomobil(Vehicle avto) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = avto;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
	public String saveAvtomobil()
	{	
		if (!isChangingAllowed()) {
			errorMessage = "Нямате права за актуализирането на данните!";
			return null;
		}
		
		if (mileage < avtomobil.getMileage()) {
			// set the message
			errorMessage = "Пробегът на автомобила е по-малък от предния записан!";
			readList();
			return null;
		}
		
		avtomobil.setMileage(mileage);
		avtomobil.writeToDB();
	
		readList();

		// set the message
		errorMessage = "Автомобилът беше актуализиран успешно!";
		
		return null;
	}
	
	public String chooseKlient()
	{

		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/AktualiziraneNaKlient.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
		
	}
	
	public String getSearchPlateNumber() {
		return searchPlateNumber;
	}

	public void setSearchPlateNumber(String searchPlateNumber) {
		if ("".equals(searchPlateNumber)) {
			this.searchPlateNumber = null;
			return;
		}
		this.searchPlateNumber = searchPlateNumber;
	}
	
	public String getSearchNamePerson() {
		return searchNamePerson;
	}

	public void setSearchNamePerson(String searchNamePerson) {
		if ("".equals(searchNamePerson)) {
			this.searchNamePerson = null;
			return;
		}
		this.searchNamePerson = searchNamePerson;
	}

	public String getSearchFamilyPerson() {
		return searchFamilyPerson;
	}

	public void setSearchFamilyPerson(String searchFamilyPerson) {
		if ("".equals(searchFamilyPerson)) {
			this.searchFamilyPerson = null;
			return;
		}
		this.searchFamilyPerson = searchFamilyPerson;
	}
	
	public String getSearchNameCompany() {
		return searchNameCompany;
	}

	public void setSearchNameCompany(String searchNameCompany) {
		if ("".equals(searchNameCompany)) {
			this.searchNameCompany = null;
			return;
		}
		this.searchNameCompany = searchNameCompany;
	}
	
	public void searchIt() {
		if (((searchPlateNumber == null) && (searchNamePerson == null) &&
			 (searchFamilyPerson == null) && (searchNameCompany == null)) ||
			(((searchNamePerson != null) || (searchFamilyPerson != null)) && (searchNameCompany != null))) {
			readList();
			return;
		}
		
		spisukAvtomobili.clear();
		rowsCount = 0;
		
		if ((searchNamePerson != null) || (searchFamilyPerson != null)) {
			List<Person> personList = Person.querySearchByNameFamily(searchNamePerson, searchFamilyPerson, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
			for (Person person : personList) {
				List<Vehicle> vehicleList = Vehicle.queryGetAll(0, 1000, person.getClientID());
				if (searchPlateNumber != null) {
					for (Vehicle vehicle : vehicleList) {
						if (vehicle.getPlateNumber().toLowerCase().indexOf(searchPlateNumber.toLowerCase()) != -1) {
							spisukAvtomobili.add(vehicle);
							rowsCount++;
						}
					}
				} else {
					spisukAvtomobili.addAll(vehicleList);
				}
			}
		} else if (searchNameCompany != null) {
			List<Company> companyList = Company.querySearchByName(searchNameCompany, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
			for (Company company : companyList) {
				List<Vehicle> vehicleList = Vehicle.queryGetAll(0, 1000, company.getClientID());
				if (searchPlateNumber != null) {
					for (Vehicle vehicle : vehicleList) {
						if (vehicle.getPlateNumber().toLowerCase().indexOf(searchPlateNumber.toLowerCase()) != -1) {
							spisukAvtomobili.add(vehicle);
							rowsCount++;
						}
					}
				} else {
					spisukAvtomobili.addAll(vehicleList);
				}
			}
		} else {
			spisukAvtomobili = Vehicle.querySearchByPlateNumber(searchPlateNumber, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
			rowsCount = Vehicle.countSearchByNameFamily(searchPlateNumber);
		}
		
		page = 0;
		avtomobil = new Vehicle();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public void resetSearch() {
		searchPlateNumber = null;
		searchNamePerson = null;
		searchFamilyPerson = null;
		searchNameCompany = null;
		searchIt();
	}

}

