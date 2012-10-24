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

import model.Autoservice;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaAvtoserviz")
@ViewScoped
public class AktualiziraneNaAvtoserviz implements Serializable {

	private Autoservice serviz = new Autoservice();
	
	private String errorMessage;
	
	private List<Autoservice> spisukAvtoservizi;

	private Stack<InterPageDataRequest> dataRequestStack;
	private int page = 0;
	private int pagesCount;
	
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaAvtoserviz() {
		
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
		return serviz.getName();
	}

	public void setName(String name) {
		serviz.setName(name);
	}

	public String getAddressCity() {
		return serviz.getAddressCity();
	}

	public void setAddressCity(String addressCity) {
		serviz.setAddressCity(addressCity);
	}

	public String getAddressLine() {
		return serviz.getAddressLine();
	}

	public void setAddressLine(String addressLine) {
		serviz.setAddressLine(addressLine);
	}

	public String getPhoneNumber() {
		return serviz.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		serviz.setPhoneNumber(phoneNumber);
	}

	public String getMail() {
		return serviz.getMail();
	}

	public void setMail(String mail) {
		serviz.setMail(mail);
	}

	public String getIBANNumber() {
		return serviz.getIBANNumber();
	}

	public void setIBANNumber(String IBANNumber) {
		serviz.setIBANNumber(IBANNumber);
	}

	public String getSWIFTCode() {
		return serviz.getSWIFTCode();
	}

	public void setSWIFTCode(String SWIFTCode) {
		serviz.setSWIFTCode(SWIFTCode);
	}

	public String getRegistrationNumber() {
		return serviz.getRegistrationNumber();
	}

	public void setRegistrationNumber(String registrationNumber) {
		serviz.setRegistrationNumber(registrationNumber);
	}

	public String getVATNumber() {
		return serviz.getVATNumber();
	}

	public void setVATNumber(String VATNumber) {
		if (!"".equals(VATNumber)) { 
		    serviz.setVATNumber(VATNumber);
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String saveAutoserviz()
	{
		serviz.writeToDB();
		
		readList();
		
		// set the message
		errorMessage = "Автосервизът беше актуализиран успешно!";
		
		return null;
	}
	
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}

	public List<Autoservice> getSpisukAvtoservizi() {
		return spisukAvtoservizi;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	
	private void readList() {
		spisukAvtoservizi = Autoservice.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		serviz = new Autoservice();
		rowsCount = Autoservice.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Autoservice autoservice : spisukAvtoservizi) {
			if (serviz == autoservice) {
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
	
	public void selectRow(Autoservice autoservice) {
		serviz = autoservice;
	}
	
	public void deselectRow() {
		serviz = new Autoservice();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukAvtoservizi.contains(serviz);
	}
	
	public String goToAdd() {
		return "DobavqneNaAvtoserviz.jsf?faces-redirect=true";
	}

	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseAutoserviz(Autoservice autoservice) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = autoservice;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
}
