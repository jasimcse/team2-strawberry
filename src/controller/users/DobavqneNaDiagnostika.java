package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;

import controller.common.CurrentEmployee;
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

	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Diagnosis diagnostika = new Diagnosis();
	private String errorMessage;

	private List <DiagnosisService> spisukUslugi = new ArrayList<DiagnosisService>();
	//private Map <Integer, DiagnosisService> spisukUslugi2 = new HashMap<Integer, DiagnosisService>();
	private List <DiagnosisPart> spisukRezervni4asti = new ArrayList<DiagnosisPart>();
	
	@SuppressWarnings("unchecked")
	public DobavqneNaDiagnostika() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
	
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
			{
				this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
				this.spisukUslugi = ((DobavqneNaDiagnostika)dataRequest.requestObject).spisukUslugi;
				this.spisukRezervni4asti = ((DobavqneNaDiagnostika)dataRequest.requestObject).spisukRezervni4asti;
				
				if(dataRequest.dataPage.equals("/users/AktualiziraneNaAvtomobil.jsf"))
				{
					this.diagnostika.setVehicle((Vehicle)dataRequest.requestedObject);
				}
				else
					if(dataRequest.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
					{
						DiagnosisService diagService = new DiagnosisService();
						diagService.setService((Service)dataRequest.requestedObject);

						this.spisukUslugi.add( diagService);
					}
					else
						if(dataRequest.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf"))
						{
								DiagnosisPart diagPart = new DiagnosisPart();
								diagPart.setSparePart((SparePart)dataRequest.requestedObject);
								diagPart.setQuantity(1);
								this.spisukRezervni4asti.add(diagPart);
						}
			}
		}
	}
	
	
	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}


	public void setVehicle(Vehicle vehicle) {
		diagnostika.setVehicle(vehicle);
	}


	public Vehicle getVehicle() {
		return diagnostika.getVehicle();
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
		
		if( spisukUslugi.isEmpty() && spisukRezervni4asti.isEmpty() )
		{
			// set the message
			errorMessage = "Изберете услуги и/или резервни части нужни за извършване по автомобила!";
			return null;
		}

		diagnostika.setDate(new Date());
		diagnostika.setEmployeeID(currEmployee.getEmployeeID());
		diagnostika.setAutoserviceID(currEmployee.getAutoserviceID());
		diagnostika.writeToDB();
	 
		// TODO: транзакция
		for (DiagnosisService dSer : spisukUslugi) {
			dSer.setDiagnosisID(diagnostika.getID());
			dSer.writeToDB();
		}
		
		for (DiagnosisPart dSp : spisukRezervni4asti) {
			dSp.setDiagnosisID(diagnostika.getID());
			dSp.writeToDB();
		}
			
		// TODO: проверка дали затрахователя е искал диагностиката 
		// или автомобила е гаранционен
		
		// clean the data
		diagnostika = new Diagnosis();
		spisukRezervni4asti.clear();
		spisukUslugi.clear();
		
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
		errorMessage = "Trala - la";

		if(spisukUslugi.remove(diagService))
			errorMessage = "Изтрито е!";			

	}
	
	public String chooseSparePart()
	{	
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/PregledNaNali4niteRezervni4asti.jsf";
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
