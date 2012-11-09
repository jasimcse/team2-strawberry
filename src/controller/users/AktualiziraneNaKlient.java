package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import model.Client;
import model.Company;
import model.Person;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaKlient")
@ViewScoped
public class AktualiziraneNaKlient implements Serializable {

	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Client klient = new Client();
	
	private String errorMessage;
	
	private List<Client> spisukKlienti;

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private String searchNamePerson;
	private String searchFamilyPerson;
	private String searchNameCompany;
	
	private InterPageDataRequest dataRequest;

	@SuppressWarnings("unchecked")
	public AktualiziraneNaKlient() {
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		readList();
	}
	
	public String getAddressCity() {
		return klient.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		klient.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return klient.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		klient.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return klient.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		if (!"".equals(phoneNumber)) {
			klient.setPhoneNumber(phoneNumber);
		}
	}

	public String getMail() {
		return klient.getMail();
	}

	public void setMail(String mail) {
		if (!"".equals(mail)) {
			klient.setMail(mail);
		}
	}

	public String getIBANNumber() {
		return klient.getIBANNumber();
	}

	public void setIBANNumber(String iBANNumber) {
		if (!"".equals(iBANNumber)) {
			klient.setIBANNumber(iBANNumber);
		}
	}

	public String getSWIFTCode() {
		return klient.getSWIFTCode();
	}

	public void setSWIFTCode(String sWIFTCode) {
		if (!"".equals(sWIFTCode)) {
			klient.setSWIFTCode(sWIFTCode);
		}
	}
	
	public boolean isPerson() {
		return (klient.getPerson() != null);
	}
	
	public boolean isCompany() {
		return (klient.getCompany() != null);
	}
	
	public String getNamePerson() {
		if (klient.getPerson() == null) {
			return null;
		}
		return klient.getPerson().getName();
	}

	public void setNamePerson(String name) {
		klient.getPerson().setName(name);
	}
	
	public String getFamilyPerson() {
		if (klient.getPerson() == null) {
			return null;
		}
		return klient.getPerson().getFamily();
	}

	public void setFamilyPerson(String family) {
		klient.getPerson().setFamily(family);
	}
	
	public String getNameCompany() {
		if (klient.getCompany() == null) {
			return null;
		}
		return klient.getCompany().getName();
	}

	public void setNameCompany(String name) {
		klient.getCompany().setName(name);
	}
	
	public String getRegistrationNumberCompany() {
		if (klient.getCompany() == null) {
			return null;
		}
		return klient.getCompany().getRegistrationNumber();
	}

	public void setRegistrationNumberCompany(String registrationNumber) {
		if (!"".equals(registrationNumber)) {
			klient.getCompany().setRegistrationNumber(registrationNumber);
		}
	}

	public String getVATNumberCompany() {
		if (klient.getCompany() == null) {
			return null;
		}
		return klient.getCompany().getVATNumber();
	}

	public void setVATNumberCompany(String VATNumber) {
		if (!"".equals(VATNumber)) {
			klient.getCompany().setVATNumber(VATNumber);
		}
	}
	
	public String getContactPersonCompany() {
		if (klient.getCompany() == null) {
			return null;
		}
		return klient.getCompany().getContactPerson();
	}

	public void setContactPersonCompany(String contactPerson) {
		if (!"".equals(contactPerson)) {
			klient.getCompany().setContactPerson(contactPerson);
		}
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
	
	public String saveClient() {
		
		if (!isChangingAllowed()) {
			errorMessage = "Нямате права за актуализирането на данните!";
			return null;
		}
		
		klient.writeToDB();
			
		readList();
		
		// set the message
		errorMessage = "Клиентът беше актуализиран успешно!";
		
		return null;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}


	public List<Client> getSpisukKlienti() {
		return spisukKlienti;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukKlienti = Client.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		klient = new Client();
		rowsCount = Client.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}

	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Client cli : spisukKlienti) {
			if (klient == cli) {
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
	
	public void selectRow(Client client) {
		klient = client;
	}
	
	public void deselectRow() {
		klient = new Client();
		readList();
	}

	public boolean isRowSelected() {
		return spisukKlienti.contains(klient);
	}
	
	public String goToAdd() {
		return "DobavqneNaKlient.jsf?faces-redirect=true";
	}
	
	public boolean isGoToAddAllowed() {
		return allPages.getReadRight(
					"/users/DobavqneNaKlient.jsf",
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
	
	public String chooseClient(Client client) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = client;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
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
		if (((searchNamePerson == null) && (searchFamilyPerson == null) && (searchNameCompany == null)) ||
			(((searchNamePerson != null) || (searchFamilyPerson != null)) && (searchNameCompany != null))) {
			readList();
			return;
		}
		
		spisukKlienti.clear();
		rowsCount = 0;
		
		if ((searchNamePerson != null) || (searchFamilyPerson != null)) {
			List<Person> personList = Person.querySearchByNameFamily(searchNamePerson, searchFamilyPerson, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
			rowsCount = Person.countSearchByNameFamily(searchNamePerson, searchFamilyPerson);
			for (Person person : personList) {
				spisukKlienti.add(person.getClient());
			}
		}
		
		if (searchNameCompany != null) {
			List<Company> companyList = Company.querySearchByName(searchNameCompany, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
			rowsCount = Company.countSearchByName(searchNameCompany);
			for (Company company : companyList) {
				spisukKlienti.add(company.getClient());
			}
		}
		
		page = 0;
		klient = new Client();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public void resetSearch() {
		searchNamePerson = null;
		searchFamilyPerson = null;
		searchNameCompany = null;
		searchIt();
	}
	
}
