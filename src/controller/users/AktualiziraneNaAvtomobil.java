package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

import model.Client;
import model.Vehicle;
import model.VehicleModel;
import model.WarrantyConditions;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaAvtomobil")
@ViewScoped

public class AktualiziraneNaAvtomobil implements Serializable {

	private Vehicle avtomobil = new Vehicle();
	
	private transient UIComponent changeButton;
	
	private String errorMessage;
	
	private List<Vehicle> spisukAvtomobili;

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
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

	
	public UIComponent getChangeButton() {
		return changeButton;
	}

	public void setChangeButton(UIComponent changeButton) {
		this.changeButton = changeButton;
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
		rowsCount = Vehicle.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize();
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
		if (!isChangingAllowed()) {
			throw new RuntimeException("Don't do that bastard!");
		}
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
	
	public boolean isChangingAllowed() {
		return true;
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

}

