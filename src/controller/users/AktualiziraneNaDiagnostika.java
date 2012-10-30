package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;


import model.Autoservice;
import model.Diagnosis;
import model.DiagnosisPart;
import model.DiagnosisService;
import model.Employee;
import model.Vehicle;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaDiagnostika")
@ViewScoped

public class AktualiziraneNaDiagnostika  implements Serializable {


	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Diagnosis diagnostika = new Diagnosis();
	private String errorMessage;

	private List <Diagnosis> spisukDiagnostiki = new ArrayList<Diagnosis>();
	private List <DiagnosisService> spisukUslugi = new ArrayList<DiagnosisService>();
	private List <DiagnosisPart> spisukRezervni4asti = new ArrayList<DiagnosisPart>();
	
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
	}
	
	@PostConstruct
	public void init() {
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

	
	public Employee getEmployee() {
		return diagnostika.getEmployee();
	}

	public void setEmployee(Employee employee) {
		diagnostika.setEmployee(employee);
	}

	public Autoservice getAutoservice() {
		return diagnostika.getAutoservice();
	}

	public void setAutoservice(Autoservice autoservice) {
		diagnostika.setAutoservice(autoservice);
	}

	public double getPrice() {
		return diagnostika.getPrice();
	}
	
	public void setPrice(double price) {
		diagnostika.setPrice(price);
	}

	public String getStatus() {
		if(diagnostika.getStatus() == null)
			return null;
		
		if(diagnostika.getStatus().equals("1")  )
			return "неплатена";
		else
			if(diagnostika.getStatus().equals("2") )
				return "платена";
		
		return null;
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

	public Date getDate() {
		return diagnostika.getDate();
	}

	public void setDate(Date date) {
		diagnostika.setDate(date);
	}


	public String getErrorMessage() {
		return errorMessage;
	}
	
	public boolean isPaied()
	{
		if(diagnostika == null)
			return true;
		
		if( diagnostika.getStatus().equals("2") )
			return true;
		
		return false;
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
		//TODO - всички диагностики ??? за сега НЕ :)
		spisukDiagnostiki = Diagnosis.queryGetAll(page * ConfigurationProperties.getPageSize(), 
				ConfigurationProperties.getPageSize(), currEmployee.getAutoserviceID());
		diagnostika = new Diagnosis();
		rowsCount = Diagnosis.countGetAll(currEmployee.getAutoserviceID());
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
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
		
		spisukUslugi = DiagnosisService.queryGetAll(0, 
				DiagnosisService.countGetAll(diagnostika.getID()), diagnostika.getID());
		spisukRezervni4asti = DiagnosisPart.queryGetAll(0, 
				DiagnosisPart.countGetAll(diagnostika.getID()), diagnostika.getID());;
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
		errorMessage = "Докладът от извършената диагностика беше актуализиран успешно!";
		
		return null;
	}
	
}

