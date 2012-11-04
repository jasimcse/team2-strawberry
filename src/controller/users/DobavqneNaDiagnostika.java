package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

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
	private long mileage;
	private String errorMessage;

	private List <DiagnosisService> spisukUslugi = new ArrayList<DiagnosisService>();
	private List <DiagnosisPart> spisukRezervni4asti = new ArrayList<DiagnosisPart>();
	
	@SuppressWarnings("unchecked")
	public DobavqneNaDiagnostika() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
	
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
			{
				this.mileage = ((DobavqneNaDiagnostika)dataRequest.requestObject).mileage;
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
		diagnostika.setStatus(status);
	}

	public String getPaymentNumber() {
		return diagnostika.getPaymentNumber();
	}

	public void setPaymentNumber(String paymentNumber) {
		diagnostika.setPaymentNumber(paymentNumber);
	}

	public long getMileage() {
		return mileage;
	}


	public void setMileage(long mileage) {
		this.mileage = mileage;
	}


	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Map<String, String> getDiagStatus() {
		Map<String, String> listStatus =  new TreeMap<String, String>();
		listStatus.put("неплатена", Diagnosis.NOT_PAYED);
		listStatus.put("платена", Diagnosis.PAYED);
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
		
		if (mileage < diagnostika.getVehicle().getMileage()) {
			// set the message
			errorMessage = "Пробегът на автомобила е по-малък от предния записан!";
			return null;
		}
		
		if( spisukUslugi.isEmpty() && spisukRezervni4asti.isEmpty() )
		{
			// set the message
			errorMessage = "Изберете услуги и/или резервни части нужни за извършване по автомобила!";
			return null;
		}

		diagnostika.getVehicle().setMileage(mileage);
		diagnostika.setDate(new Date());
		diagnostika.setEmployeeID(currEmployee.getEmployeeID());
		diagnostika.setAutoserviceID(currEmployee.getAutoserviceID());
	 
		Transaction tr = DatastoreServiceFactory.getDatastoreService().beginTransaction(TransactionOptions.Builder.withXG(true));
		
		diagnostika.getVehicle().writeToDB();
		diagnostika.writeToDB();
		
		for (DiagnosisService dSer : spisukUslugi) {
			dSer.setDiagnosisID(diagnostika.getID());
			dSer.writeToDB();
		}
		
		for (DiagnosisPart dSp : spisukRezervni4asti) {
			dSp.setDiagnosisID(diagnostika.getID());
			dSp.writeToDB();
		}
		
		// TODO: проверка дали застраховател не е заявил диагностиката и изпращане на доклад до застрахователя
		// TODO: проверка дали автомобила е гаранционен и изпращане на доклад до електронния магазин
		
		tr.commit();
		
		// clean the data
		diagnostika = new Diagnosis();
		mileage = 0;
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
		spisukUslugi.remove(diagService);
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
