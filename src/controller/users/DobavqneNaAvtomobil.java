package controller.users;

import java.io.Serializable;
import java.util.Stack;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;


import controller.common.InterPageDataRequest;

import model.Client;
import model.Vehicle;
import model.VehicleModel;


@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaAvtomobil")
@ViewScoped

public class DobavqneNaAvtomobil implements Serializable {

	private Vehicle avtomobil = new Vehicle();
	
	private transient UIComponent addButton;
	
	private String errorMessage;

	@SuppressWarnings("unchecked")
	public DobavqneNaAvtomobil() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
			{
				if(dataRequest.dataPage.equals("/users/PregledNaModelAvtomobil.jsf"))
				{
					this.avtomobil = ((DobavqneNaAvtomobil)dataRequest.requestObject).avtomobil;
					this.avtomobil.setVehicleModel((VehicleModel)dataRequest.requestedObject);
				}
				else
					if(dataRequest.dataPage.equals("/users/AktualiziraneNaKlient.jsf"))
					{
							this.avtomobil = ((DobavqneNaAvtomobil)dataRequest.requestObject).avtomobil;
							this.avtomobil.setClient((Client)dataRequest.requestedObject);
					}
			}
		}
	}
	
	public Client getClient() {
		return avtomobil.getClient();
	}

	public void setVehicleModelID(Key vehicleModelID) {
		avtomobil.setVehicleModelID(vehicleModelID);
	}

	public VehicleModel getVehicleModel() {
		return avtomobil.getVehicleModel();
	}

	public String getVIN() {
		return avtomobil.getVIN();
	}

	public void setVIN(String VIN) {
		try {
			avtomobil.setVIN(VIN);
		} catch (RuntimeException e) {
			// do nothing
		}
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

	public long getMileage() {
		return avtomobil.getMileage();
	}

	public void setMileage(long mileage) {
		avtomobil.setMileage(mileage);
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

	public String addAvtomobil()
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
		
		// set the message
		errorMessage = "Автомобилът беше добавен успешно!";
		
		return null;
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
