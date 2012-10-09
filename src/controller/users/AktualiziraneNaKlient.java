package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;

import model.Client;
import model.Company;
import model.Person;
import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaKlient")
@ViewScoped
public class AktualiziraneNaKlient implements Serializable {

	private Client klient = new Client();
	private Person person = new Person();
	private Company company = new Company();
	
	private String errorMessage;
	
	//private List<Client> spisukKlienti;
	private List<Person> spisukPerson;
	private List<Company> spisukComapany;

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int pagePerson = 0;
	private int pagesCountPerson;
	private int rowsCountPerson;
	
	private int pageCompany = 0;
	private int pagesCountCompany;
	private int rowsCountCompany;
	
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
		
		readListPerson();
		readListCompany();
	}


	public Key getID() {
		return klient.getID();
	}


	public String getPersonCompany() {
		return klient.getPersonCompany();
	}


	public void setPersonCompany(String personCompany) {
		klient.setPersonCompany(personCompany);
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
		klient.setPhoneNumber(phoneNumber);
	}


	public String getMail() {
		return klient.getMail();
	}


	public void setMail(String mail) {
		klient.setMail(mail);
	}


	public String getIBANNumber() {
		return klient.getIBANNumber();
	}


	public void setIBANNumber(String iBANNumber) {
		klient.setIBANNumber(iBANNumber);
	}


	public String getSWIFTCode() {
		return klient.getSWIFTCode();
	}


	public void setSWIFTCode(String sWIFTCode) {
		klient.setSWIFTCode(sWIFTCode);
	}


	public String getPersonName() {
		return person.getName();
	}


	public void setPersonName(String name) {
		person.setName(name);
	}


	public String getFamily() {
		return person.getFamily();
	}


	public void setFamily(String family) {
		person.setFamily(family);
	}


	public String getCompanyName() {
		return company.getName();
	}

	public void setCompanyName(String name) {
		company.setName(name);
	}
	
	public String getRegistrationNumber() {
		return company.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		if (!"".equals(registrationNumber)) 
			company.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return company.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) 
		    company.setVATNumber(VATNumber);
	}

	public String getContactPerson() {
		return company.getContactPerson();
	}

	public void setContactPerson(String contactPerson) {
		company.setContactPerson(contactPerson);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	
	public String saveClient()
	{
		klient.writeToDB();
		if(klient.getPersonCompany().equals("P"))
		{
			person.writeToDB();
			deselectRowPerson();
		}
		else
		{
			company.writeToDB();
			deselectRowCompany();
		}
			
		readListPerson();
		readListCompany();
		
		// set the message
		errorMessage = "Клиентът беше актуализиран успешно!";
		
		return null;
	}
	
	
	public int getPagePerson() {
		return pagePerson;
	}

	public void setPagePerson(int page) {
		this.pagePerson = page;
		readListPerson();
	}


	public List<Person> getSpisukPerson() {
		return spisukPerson;
	}

	public int getPagesCountPerson() {
		return pagesCountPerson;
	}

	private void readListPerson() {
		spisukPerson = Person.queryGetAllM(pagePerson * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		person = new Person();
		rowsCountPerson = spisukPerson.size();
		
		pagesCountPerson = rowsCountPerson / ConfigurationProperties.getPageSize();
	}

	
	public String getRowStyleClassesPerson() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Person per : spisukPerson) {
			if (person == per) {
				strbuff.append("selectedRow,");
			} else {
				strbuff.append("notSelectedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public List<Integer> getPagesListPerson() {
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i=0; i < pagesCountPerson; i++)
			list.add(Integer.valueOf(i+1));
		
		return list;
	}
	
	public void selectRowPerson(Person per) {
		deselectRowCompany();
		person = per;
		klient = person.getClient();
	}
	
	public void deselectRowPerson() {
		klient = new Client();
		person = new Person();
	}
	

	public boolean isRowSelectedPerson() {
		return spisukPerson.contains(person);
	}
	
	public String goToAdd() {
		return "DobavqneNaKlient.jsf?faces-redirect=true";
	}

	public boolean isChoosingAlowedPerson() {
		return (dataRequest != null);
	}
	
	public String chooseKlient(Person per) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = per;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
	public int getPageCompany() {
		return pageCompany;
	}

	public void setPageCompany(int page) {
		this.pageCompany = page;
		readListCompany();
	}


	public List<Company> getSpisukCompany() {
		return spisukComapany;
	}

	public int getPagesCountCompany() {
		return pagesCountCompany;
	}

	private void readListCompany() {
		spisukComapany = Company.queryGetAllM(pageCompany * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		company = new Company();
		rowsCountCompany = spisukComapany.size();
		
		pagesCountCompany = rowsCountCompany / ConfigurationProperties.getPageSize();
	}
	
	public String getRowStyleClassesCompany() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Company comp : spisukComapany) {
			if (company == comp) {
				strbuff.append("selectedRow,");
			} else {
				strbuff.append("notSelectedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public List<Integer> getPagesListCompany() {
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i=0; i < pagesCountCompany; i++)
			list.add(Integer.valueOf(i+1));
		
		return list;
	}
	
	public void selectRowCompany(Company comp) {
		deselectRowPerson();
		company = comp;
		klient = company.getClient();
	}
	
	public void deselectRowCompany() {
		klient = new Client();
		company = new Company();
	}
	

	public void deselectRow() {
		deselectRowPerson();
		deselectRowCompany();
	}
	public boolean isRowSelectedCompany() {
		return spisukComapany.contains(company);
	}
	
	public boolean isChoosingAlowedCompany() {
		return (dataRequest != null);
	}
	
	public String chooseCompany(Company comp) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = comp;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
}
