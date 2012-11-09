package controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.KeyFactory;

import model.Insurer;
import model.util.UniqueAttributeException;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaZastrahovatel")
@ViewScoped
public class AktualiziraneNaZastrahovatel implements Serializable {
	
	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private Insurer zastrahovatel = new Insurer();
	private int page = 0;
	private int pagesCount;
	private List<Insurer> spisukZastrahovateli;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	private String searchName;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaZastrahovatel() {
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		readList();
	}
	
	public String getID() {
		if (isRowSelected()) {
			return KeyFactory.keyToString(zastrahovatel.getID());
		} else {
			return null;
			
		}
	}

	public String getName() {
		return zastrahovatel.getName();
	}

	public void setName(String name) {
		zastrahovatel.setName(name);
	}

	public String getAddressCity() {
		return zastrahovatel.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		zastrahovatel.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return zastrahovatel.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		zastrahovatel.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return zastrahovatel.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		zastrahovatel.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return zastrahovatel.getMail();
	}

	public void setMail(String mail) {
		zastrahovatel.setMail(mail);
	}

	public String getIBANNumber() {
		return zastrahovatel.getIBANNumber();
	}

	public void setIBANNumber(String IBANNumber) {
		zastrahovatel.setIBANNumber(IBANNumber);
	}

	public String getSWIFTCode() {
		return zastrahovatel.getSWIFTCode();
	}

	public void setSWIFTCode(String SWIFTCode) {
		zastrahovatel.setSWIFTCode(SWIFTCode);
	}

	public String getRegistrationNumber() {
		return zastrahovatel.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		zastrahovatel.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return zastrahovatel.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) { 
			zastrahovatel.setVATNumber(VATNumber);
		}
	}

	public String getContactPerson() {
		return zastrahovatel.getContactPerson();
	}

	public void setContactPerson(String contactPerson) {
		zastrahovatel.setContactPerson(contactPerson);
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

	public List<Insurer> getSpisukZastrahovateli() {
		return spisukZastrahovateli;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public String writeIt() {
		
		if (!isChangingAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return null;
		}
		
		try {
			zastrahovatel.writeToDB();
			errorMessage = "��������������� ���� ������������ �������!";
		} catch (UniqueAttributeException e) {
			errorMessage = "���������� ������!";
			return null;
		}
		
		readList();
		
		return null;
	}
	
	private void readList() {
		spisukZastrahovateli = Insurer.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		zastrahovatel = new Insurer();
		rowsCount = Insurer.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Insurer ins : spisukZastrahovateli) {
			if (zastrahovatel == ins) {
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
	
	public void selectRow(Insurer ins) {
		zastrahovatel = ins;
	}
	
	public void deselectRow() {
		zastrahovatel = new Insurer();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukZastrahovateli.contains(zastrahovatel);
	}
	
	public String goToAdd() {
		return "DobavqneNaZastrahovatel.jsf?faces-redirect=true";
	}
	
	public boolean isGoToAddAllowed() {
		return allPages.getReadRight(
					"/admin/DobavqneNaZastrahovatel.jsf",
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
	
	public String chooseInsurer(Insurer insurer) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = insurer;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}

	/**
	 * @return the searchName
	 */
	public String getSearchName() {
		return searchName;
	}

	/**
	 * @param searchName the searchName to set
	 */
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	public void searchIt() {
		if (searchName == null || searchName.isEmpty()) {
			readList();
			return;
		}
		
		spisukZastrahovateli = Insurer.querySearchByName(searchName, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		page = 0;
		rowsCount = Insurer.countSearchByName(searchName);
		zastrahovatel = new Insurer();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
		
	}
	
	public void resetSearch() {
		searchName = null;
		searchIt();
	}

}
