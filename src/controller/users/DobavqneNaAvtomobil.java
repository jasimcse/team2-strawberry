package controller.users;


import java.util.Date;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;

import controller.admin.DobavqneNaPotrebitel;
import controller.common.InterPageDataRequest;

import model.Client;
import model.Vehicle;
import model.VehicleModel;
import model.WarrantyConditions;



@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaAvtomobil")
@ViewScoped

public class DobavqneNaAvtomobil {

	private Vehicle avtomobil = new Vehicle();
	
	private transient UIComponent addButton;
	
	private String errorMessage;

	@SuppressWarnings("unchecked")
	public DobavqneNaAvtomobil() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) {
				this.avtomobil = ((DobavqneNaAvtomobil)dataRequest.requestObject).avtomobil;
				this.avtomobil.setClient((Client)dataRequest.requestedObject);
			}
		}
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

	
	public UIComponent getAddButton() {
		return addButton;
	}

	public void setAddButton(UIComponent addButton) {
		this.addButton = addButton;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void addAvtomobil()
	{
		avtomobil.writeToDB();
	
		avtomobil = new Vehicle();


		errorMessage = "Автомобилът беше добавен успешно!";
	}
	
	public String chooseModelAvtomobil()
	{
		
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
	
	public String chooseGarancionniUsloviq()
	{
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/PregledNaGarancionniUsloviq.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
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
