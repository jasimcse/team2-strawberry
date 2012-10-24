package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.WarrantyConditions;
import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="pregledNaGarancionniUsloviq")
@ViewScoped
public class PregledNaGarancionniUsloviq implements Serializable {
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private WarrantyConditions garancionniUsloviq = new WarrantyConditions();
	private int page = 0;
	private int pagesCount;
	private List<WarrantyConditions> spisukGarancionniUsloviq;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public PregledNaGarancionniUsloviq() {
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		readList();
	}
	
	public long getMonths() {
		return garancionniUsloviq.getMonths();
	}

	public long getMileage() {
		return garancionniUsloviq.getMileage();
	}

	public String getOtherConditions() {
		return garancionniUsloviq.getOtherConditions();
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

	public List<WarrantyConditions> getSpisukGarancionniUsloviq() {
		return spisukGarancionniUsloviq;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukGarancionniUsloviq = WarrantyConditions.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		garancionniUsloviq = new WarrantyConditions();
		rowsCount = WarrantyConditions.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (WarrantyConditions wc : spisukGarancionniUsloviq) {
			if (garancionniUsloviq == wc) {
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
	
	public void selectRow(WarrantyConditions wc) {
		garancionniUsloviq = wc;
	}
	
	public void deselectRow() {
		garancionniUsloviq= new WarrantyConditions();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukGarancionniUsloviq.contains(garancionniUsloviq);
	}
	
	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseWarrantyConditions(WarrantyConditions warrantyConditions) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = warrantyConditions;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}

}
