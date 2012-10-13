package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;


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
	
	//private transient UIComponent addButton;
	
	private String errorMessage;
	
	private List<Vehicle> spisukAvtomobili;

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private InterPageDataRequest dataRequest;

	@SuppressWarnings("unchecked")
	public AktualiziraneNaAvtomobil() {
		Stack<InterPageDataRequest> dataRequestStackNew = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStackNew != null) {
			InterPageDataRequest dataRequestNew = dataRequestStackNew.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequestNew.returnPage)) 
			{
				if(dataRequestNew.dataPage.equals("/users/PregledNaModelAvtomobil.jsf"))
				{
					this.avtomobil = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).avtomobil;
					this.avtomobil.setVehicleModel((VehicleModel)dataRequestNew.requestedObject);
				}
				else
					if(dataRequestNew.dataPage.equals("/users/AktualiziraneNaKlient.jsf"))
					{
							this.avtomobil = ((AktualiziraneNaAvtomobil)dataRequestNew.requestObject).avtomobil;
							this.avtomobil.setClient((Client)dataRequestNew.requestedObject);
					}
			}
		}
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		readList();
	}
	
	public Key getID() {
		return avtomobil.getID();
	}

	public Client getClient() {
		return avtomobil.getClient();
	}

	public void setClient(Client client) {
		avtomobil.setClient(client);
	}

	public Key getVehicleModelID() {
		return avtomobil.getVehicleModelID();
	}

	public void setVehicleModelID(Key vehicleModelID) {
		avtomobil.setVehicleModelID(vehicleModelID);
	}

	public VehicleModel getVehicleModel() {
		return avtomobil.getVehicleModel();
	}

	public void setVehicleModel(VehicleModel vehicleModel) {
		avtomobil.setVehicleModel(vehicleModel);
	}

	public Key getWarrantyConditionsID() {
		return avtomobil.getWarrantyConditionsID();
	}

	public void setWarrantyConditionsID(Key warrantyConditionsID) {
		avtomobil.setWarrantyConditionsID(warrantyConditionsID);
	}

	public WarrantyConditions getWarrantyConditions() {
		return avtomobil.getWarrantyConditions();
	}

	public void setWarrantyConditions(WarrantyConditions warrantyConditions) {
		avtomobil.setWarrantyConditions(warrantyConditions);
	}

	public String getVIN() {
		return avtomobil.getVIN();
	}

	public void setVIN(String VIN) {
		avtomobil.setVIN(VIN);
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
		return avtomobil.getWarrantyOK();
	}

	public void setWarrantyOK(String warrantyOK) {
		avtomobil.setWarrantyOK(warrantyOK);
	}

	public Date getPurchaseDate() {
		return avtomobil.getPurchaseDate();
	}

	public void setPurchaseDate(Date purchaseDate) {
		avtomobil.setPurchaseDate(purchaseDate);
	}

	
	//public UIComponent getAddButton() {
	//	return addButton;
	//}

	//public void setAddButton(UIComponent addButton) {
	//	this.addButton = addButton;
	//}

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
		avtomobil = avto;
	}
	
	public void deselectRow() {
		avtomobil = new Vehicle();
	}

	public boolean isRowSelected() {
		return spisukAvtomobili.contains(avtomobil);
	}
	
	public String goToAdd() {
		return "DobavqneNaAvtomobil.jsf?faces-redirect=true";
	}

	public boolean isChoosingAlowed() {
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
		if (avtomobil.getVehicleModel() == null) {
			// set the message
			errorMessage = "Не е избран модел автомобил!";
			return null;
		}
		
		if (avtomobil.getClient() == null) {
			// set the message
			errorMessage = "Не е избран клиент!";
			return null;
		}
	
		avtomobil.writeToDB();
	
		// clean the data
		avtomobil = new Vehicle();
		
		
		readList();

		// set the message
		errorMessage = "Автомобилът беше актуализиран успешно!";
		
		return null;
	}
	
	public String chooseModelAvtomobil()
	{
		if (avtomobil.getVIN() == null) {
			// set the message
			errorMessage = "Не е попълнен номер на рама!";
			return null;
		}
		
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/PregledNaModelAvtomobil.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
	}

	
	public String chooseKlient()
	{
		if (avtomobil.getVIN() == null) {
			// set the message
			errorMessage = "Не е попълнен номер на рама!";
			return null;
		}
		
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

