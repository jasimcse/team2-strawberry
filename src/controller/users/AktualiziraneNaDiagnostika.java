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

import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;
import model.Autoservice;
import model.Client;
import model.Diagnosis;
import model.DiagnosisPart;
import model.DiagnosisService;
import model.Employee;
import model.SparePart;
import model.Service;
import model.Vehicle;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaDiagnostika")
@ViewScoped

public class AktualiziraneNaDiagnostika  implements Serializable {

	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Diagnosis diagnostika = new Diagnosis();
	private String errorMessage;

	private List <Diagnosis> spisukDiagnostiki;
	private List <DiagnosisService> spisukUslugi;
	private List <DiagnosisPart> spisukRezervni4asti;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private InterPageDataRequest dataRequest;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaDiagnostika() {
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		readList();
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
		if(status.equals("���������") )
			diagnostika.setStatus("1");
		else
			if(status.equals("�������") )
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
	
	

	public Autoservice getAutoservice() {
		return diagnostika.getAutoservice();
	}


	public void setAutoservice(Autoservice autoservice) {
		diagnostika.setAutoservice(autoservice);
	}


	public Employee getEmployee() {
		return diagnostika.getEmployee();
	}


	public void setEmployee(Employee employee) {
		diagnostika.setEmployee(employee);
	}


	public Date getDate() {
		return diagnostika.getDate();
	}


	public void setDate(Date date) {
		diagnostika.setDate(date);
	}


	public String getErrorMessage() {
		return errorMessage;
	}
	
	public List< String > getDiagStatus() 
	{
		List<String> listStatus =  new ArrayList<String>();
		listStatus.add("���������");
		listStatus.add("�������");
		return listStatus;
	}

	public List<DiagnosisService> getSpisukUslugi() {
		return spisukUslugi;
	}
	
	public List<DiagnosisPart> getSpisukRezervni4asti() {
		return spisukRezervni4asti;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}


	public List<Diagnosis> getSpisukDiagnostiki() {
		return spisukDiagnostiki;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukDiagnostiki = Diagnosis.queryGetAll(page * ConfigurationProperties.getPageSize(), 
				ConfigurationProperties.getPageSize(), currEmployee.getAutoserviceID());
		diagnostika = new Diagnosis();
		rowsCount = Vehicle.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize();
	}

	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Diagnosis diag : spisukDiagnostiki) {
			if (diagnostika == diag) {
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
	
	public void selectRow(Diagnosis diag) {
		diagnostika = diag;
	}
	
	public void deselectRow() {
		diagnostika = new Diagnosis();
		readList();
		
	}

	public boolean isRowSelected() {
		return spisukDiagnostiki.contains(diagnostika);
	}
	
	public String goToAdd() {
		return "DobavqneNaDiagnostika.jsf?faces-redirect=true";
	}
	
	

	public boolean isChoosingAllowed() {
		return (dataRequest != null);
	}
	
	public String chooseDiagnostika(Diagnosis diag) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = diag;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
	public String saveDiagnostika()
	{	

		diagnostika.writeToDB();
	
		readList();

		// set the message
		errorMessage = "�������� �� ����������� ����������� ���� ������������ �������!";
		
		return null;
	}
	
}

