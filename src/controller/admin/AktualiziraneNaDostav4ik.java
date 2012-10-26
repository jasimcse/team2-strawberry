package controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.KeyFactory;

import model.Supplier;
import model.util.UniqueAttributeException;
import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaDostav4ik")
@ViewScoped
public class AktualiziraneNaDostav4ik implements Serializable {

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private Supplier dostav4ik = new Supplier();
	private int page = 0;
	private int pagesCount;
	private List<Supplier> spisukDostav4ici;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaDostav4ik() {
		
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
			return KeyFactory.keyToString(dostav4ik.getID());
		} else {
			return null;
			
		}
	}

	public String getName() {
		return dostav4ik.getName();
	}

	public void setName(String name) {
		dostav4ik.setName(name);
	}

	public String getAddressCity() {
		return dostav4ik.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		dostav4ik.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return dostav4ik.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		dostav4ik.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return dostav4ik.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		dostav4ik.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return dostav4ik.getMail();
	}

	public void setMail(String mail) {
		dostav4ik.setMail(mail);
	}

	public String getIBANNumber() {
		return dostav4ik.getIBANNumber();
	}

	public void setIBANNumber(String IBANNumber) {
		dostav4ik.setIBANNumber(IBANNumber);
	}

	public String getSWIFTCode() {
		return dostav4ik.getSWIFTCode();
	}

	public void setSWIFTCode(String SWIFTCode) {
		dostav4ik.setSWIFTCode(SWIFTCode);
	}

	public String getRegistrationNumber() {
		return dostav4ik.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		dostav4ik.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return dostav4ik.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) { 
			dostav4ik.setVATNumber(VATNumber);
		}
	}

	public String getContactPerson() {
		return dostav4ik.getContactPerson();
	}

	public void setContactPerson(String contactPerson) {
		dostav4ik.setContactPerson(contactPerson);
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

	public List<Supplier> getSpisukDostav4ici() {
		return spisukDostav4ici;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public String writeIt() {
		
		try {
			dostav4ik.writeToDB();
			errorMessage = "Доставчикът беше актуализиран успешно!";
		} catch (UniqueAttributeException e) {
			errorMessage = "Неуникални полета!";
			return null;
		}
		
		readList();
		
		return null;
	}
	
	private void readList() {
		spisukDostav4ici = Supplier.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		dostav4ik = new Supplier();
		rowsCount = Supplier.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Supplier sup : spisukDostav4ici) {
			if (dostav4ik == sup) {
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
	
	public void selectRow(Supplier sup) {
		dostav4ik = sup;
	}
	
	public void deselectRow() {
		dostav4ik = new Supplier();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukDostav4ici.contains(dostav4ik);
	}
	
	public String goToAdd() {
		return "DobavqneNaDostav4ik.jsf?faces-redirect=true";
	}

	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseSupplier(Supplier supplier) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = supplier;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
}
