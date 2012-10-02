package controller.admin;

import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import controller.common.InterPageDataRequest;

import model.Autoservice;
import model.Employee;
import model.EmployeeAutoservice;


@ManagedBean(name="dobavqneNaPotrebitel")
@RequestScoped
public class DobavqneNaPotrebitel {
	
	private EmployeeAutoservice potrebitel = new EmployeeAutoservice();
	
	private String errorMessage;

	@SuppressWarnings("unchecked")
	public DobavqneNaPotrebitel() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) {
				this.potrebitel = ((DobavqneNaPotrebitel)dataRequest.requestObject).potrebitel;
				this.potrebitel.setEmployee((Employee)dataRequest.requestedObject);
			}
		}
	}
	
	public String getEmployeeName() {
		if (potrebitel.getEmployee() == null) {
			return null;
		}
		return potrebitel.getEmployee().getName();
	}
	
	public String getEmployeeFamily() {
		if (potrebitel.getEmployee() == null) {
			return null;
		}
		return potrebitel.getEmployee().getName();
	}
	
	public List<Autoservice> getAutoservices() {
		return Autoservice.queryGetAll(0, 1000);
	}

	public void setAutoservice(Autoservice autoservice) {
		potrebitel.setAutoservice(autoservice);
	}

	public String getUsername() {
		return potrebitel.getUsername();
	}

	public String getPosition() {
		return potrebitel.getPosition();
	}
	
	public void setPosition(String position) {
		this.potrebitel.setPosition(position);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String writeIt() {
		potrebitel.writeToDB();
		
		// clean the data
		potrebitel = new EmployeeAutoservice();
		
		// set the message
		errorMessage = "Потребителят беше добавен успешно!";
		
		return null;
	}
	
	public String chooseEmployee() {
	    Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
		
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/admin/AktualiziraneNaSlujitel.jsf";
		dataRequest.requestedObject = null;
		
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.dataPage + "?faces-redirect=true";
	}
	
}