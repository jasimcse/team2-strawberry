package controller.users;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;


import controller.common.InterPageDataRequest;
import model.Diagnosis;
import model.SparePart;
import model.Service;


@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaDiagnostika")
@ViewScoped

public class DobavqneNaDiagnostika  implements Serializable {

	private Diagnosis diagnostika = new Diagnosis();

	private String errorMessage;

	List<Service> spisukUslugi;
	List<SparePart> spisukRezervni4asti;
	
	@SuppressWarnings("unchecked")
	public DobavqneNaDiagnostika() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
//		if (dataRequestStack != null) {
//			InterPageDataRequest dataRequest = dataRequestStack.peek();
//			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
//			{
//				if(dataRequest.dataPage.equals("/users/AktualiziraneNaAvtomobil.jsf"))
//				{
//					this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
//					this.diagnostika.setVehicleID((Vehicle)dataRequest.requestedObject);
//				}
//				else
//					if(dataRequest.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
//					{
//							this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
//							this.diagnostika.set((Client)dataRequest.requestedObject);
//					}
//					else
//						// TODO: 4akame Venci!
//						if(dataRequest.dataPage.equals("/users/AktualiziraneNa.jsf"))
//						{
//								this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
//								this.diagnostika.setClient((Client)dataRequest.requestedObject);
//						}
//			}
//		}
	}
	
	
	public Key getVehicleID() {
		return diagnostika.getVehicleID();
	}


	public double getPrice() {
		return diagnostika.getPrice();
	}


	public String getStatus() {
		return diagnostika.getStatus();
	}


	public String getPaymentNumber() {
		return diagnostika.getPaymentNumber();
	}


	public void setAutoserviceID(Key autoserviceID) {
		diagnostika.setAutoserviceID(autoserviceID);
	}


	public void setVehicleID(Key vehicleID) {
		diagnostika.setVehicleID(vehicleID);
	}


	public void setEmployeeID(Key employeeID) {
		diagnostika.setEmployeeID(employeeID);
	}


	public void setPrice(double price) {
		diagnostika.setPrice(price);
	}


	public void setStatus(String status) {
		diagnostika.setStatus(status);
	}


	public void setPaymentNumber(String paymentNumber) {
		diagnostika.setPaymentNumber(paymentNumber);
	}


	public String getErrorMessage() {
		return errorMessage;
	}

	public String addDiagnostika()
	{	
//		if (avtomobil.getVehicleModel() == null) {
//			// set the message
//			errorMessage = "Не е избран модел автомобил!";
//			return null;
//		}
//		
//		if (avtomobil.getClient() == null) {
//			// set the message
//			errorMessage = "Не е избран клиент!";
//			return null;
//		}
	
		diagnostika.writeToDB();
	
		// clean the data
		diagnostika = new Diagnosis();
		
		// set the message
		errorMessage = "Докладът за диагностика беше добавен успешно!";
		
		return null;
	}
	
	public String chooseAvtomobil()
	{
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/AktualiziraneNaAvtomobil.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
	}

	
	public String chooseUsluga()
	{	
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/AktualiziraneNaUsluga.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
		
	}
	
	public void deleteUsluga(Service ser)
	{
		// TODO:
	}
	
	public String chooseSparePart()
	{	
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		// TODO: Venci 4akame!
		//dataRequest.dataPage = "/users/AktualiziraneNa.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
		
	}
	
	public void deleteSparePart(SparePart sPart)
	{
		// TODO:
		
	}

	
}
