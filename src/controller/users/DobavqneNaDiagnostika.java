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

import controller.common.InterPageDataRequest;
import model.Diagnosis;
import model.DiagnosisPart;
import model.DiagnosisService;
import model.SparePart;
import model.Service;
import model.Vehicle;


@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaDiagnostika")
@ViewScoped

public class DobavqneNaDiagnostika  implements Serializable {

	private Diagnosis diagnostika = new Diagnosis();
	private String errorMessage;

	private List <DiagnosisService> spisukUslugi;
	private List <DiagnosisPart> spisukRezervni4asti;
	
	@SuppressWarnings("unchecked")
	public DobavqneNaDiagnostika() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
			{
				if(dataRequest.dataPage.equals("/users/AktualiziraneNaAvtomobil.jsf"))
				{
					this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
					this.diagnostika.setVehicle((Vehicle)dataRequest.requestedObject);
				}
				else
					if(dataRequest.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
					{
						this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
						DiagnosisService diagService = new DiagnosisService();
						diagService.setDiagnosis(this.diagnostika);
						diagService.setService((Service)dataRequest.requestedObject);
						this.spisukUslugi.add(diagService);
						
					}
					else
						// TODO: 4akame Venci!
						if(dataRequest.dataPage.equals("/users/AktualiziraneNa.jsf"))
						{
								this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
								DiagnosisPart diagPart = new DiagnosisPart();
								diagPart.setDiagnosis(diagnostika);
								diagPart.setSparePart((SparePart)dataRequest.requestedObject);
								diagPart.setQuantity(1);
								this.spisukRezervni4asti.add(diagPart);
						}
			}
		}
	}
	
	
	public void setVehicle(Vehicle vehicle) {
		diagnostika.setVehicle(vehicle);
	}


	public Key getVehicleID() {
		if (diagnostika.getVehicleID() == null) {
			return null;
		}
		return diagnostika.getVehicleID();
	}


	public void setVehicleID(Key vehicleID) {
		diagnostika.setVehicleID(vehicleID);
	}
	
	public double getPrice() {
		return diagnostika.getPrice();
	}
	
	public void setPrice(double price) {
		diagnostika.setPrice(price);
	}

	public String getStatus() {
		return diagnostika.getStatus();
	}

	public void setStatus(String status) {
		if(status.equals("неплатена") )
			diagnostika.setStatus("1");
		else
			if(status.equals("платена") )
				diagnostika.setStatus("2");
	}

	public String getPaymentNumber() {
		return diagnostika.getPaymentNumber();
	}

	public void setPaymentNumber(String paymentNumber) {
		diagnostika.setPaymentNumber(paymentNumber);
	}

	public void setAutoserviceID(Key autoserviceID) {
		diagnostika.setAutoserviceID(autoserviceID);
	}

	public void setEmployeeID(Key employeeID) {
		diagnostika.setEmployeeID(employeeID);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public List< String > getDiagStatus() 
	{
		List<String> listStatus =  new ArrayList<String>();
		listStatus.add("неплатена");
		listStatus.add("платена");
		return listStatus;
	}

	public List<DiagnosisService> getSpisukUslugi() {
		return spisukUslugi;
	}
	
	public List<DiagnosisPart> getSpisukRezervni4asti() {
		return spisukRezervni4asti;
	}

	public String addDiagnostika()
	{	
		if (diagnostika.getVehicleID() == null) {
			// set the message
			errorMessage = "Не е избран автомобил!";
			return null;
		}

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
	
	public void deleteUsluga(DiagnosisService diagService)
	{
		spisukUslugi.remove(diagService);
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
	
	public void deleteSparePart(DiagnosisPart sPart)
	{
		spisukRezervni4asti.remove(sPart); 
	}
	
}
