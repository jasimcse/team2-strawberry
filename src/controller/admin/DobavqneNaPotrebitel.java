package controller.admin;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;

import controller.common.InterPageDataRequest;

import model.Autoservice;
import model.Employee;
import model.EmployeeAutoservice;


@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaPotrebitel")
@ViewScoped
public class DobavqneNaPotrebitel implements Serializable {
	
	private EmployeeAutoservice potrebitel = new EmployeeAutoservice();
	
	private transient UIComponent addButton;
	
	private String errorMessage;

	@SuppressWarnings("unchecked")
	public DobavqneNaPotrebitel() {
		// проверяваме дали се връщат данни от страница към която сме направили заявка
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			// има заявки; проверяваме дали са от текущата страница
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) {
				// от текущата страница са; възстановяваме състоянието на страницата такова каквото е било при направата на заявката 
				this.potrebitel = ((DobavqneNaPotrebitel)dataRequest.requestObject).potrebitel;
				// добавяме си данните от заявката
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
		return potrebitel.getEmployee().getFamily();
	}
	
	public List<Autoservice> getAutoservices() {
		List<Autoservice> auto = Autoservice.queryGetAll(0, 1000);
		return auto;
	}
	
	public Key getAutoserviceID() {
		return potrebitel.getAutoserviceID();
	}

	public void setAutoserviceID(Key autoserviceID) {
		potrebitel.setAutoserviceID(autoserviceID);
	}

	public String getUsername() {
		return potrebitel.getUsername();
	}
	
	public void setUsername(String username) {
		this.potrebitel.setUsername(username);
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

	public UIComponent getAddButton() {
		return addButton;
	}

	public void setAddButton(UIComponent addButton) {
		this.addButton = addButton;
	}

	public String writeIt() {
		
		if (potrebitel.getEmployee() == null) {
			// set the message
			errorMessage = "Не е избран служител!";
			return null;
		}
		
		//TODO - generate random password, maybe using java.util.UUID
		String generatedPassword = "1234";
		potrebitel.setPassword(generatedPassword);
		
		potrebitel.writeToDB();
		
		//TODO - send mail
		
		// clean the data
		potrebitel = new EmployeeAutoservice();
		
		// set the message
		errorMessage = "Потребителят беше добавен успешно!";
		
		return null;
	}
	
	public String chooseEmployee() {
		// инициализираме нов стек за заявки за данни към други страници
	    Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
	    
	    // инициализираме си нова заявка за данни към други страници
		InterPageDataRequest dataRequest = new InterPageDataRequest();
		
		// попълваме заявката
		// записваме състоянието на текущия обекта, за да го възстановим след изпълнението на заявката
		// т.е. ако потребителя е попълнил нещо в полетата, след като се върнем полетата ще са му попълнени пак със същите данни
		dataRequest.requestObject = this;
		// записваме текущата страница като страницата към която трябва да се върнат резултатите 
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		// записваме страницата от която искаме данни
		dataRequest.dataPage = "/admin/AktualiziraneNaSlujitel.jsf";
		// зануляваме полето където ще бъдат попълнени данните
		dataRequest.requestedObject = null;
		
		// слагаме заявката в стека
		dataRequestStack.push(dataRequest);
		// слагаме стека във flash-а; flash-а запазва данните записани в него до отварянето на друго view (друга страница)
		// flash-а изтрива автоматично данните съхранявани в него след отварянето на следващото view
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// отиваме на страницата от която искаме данни
		return dataRequest.dataPage + "?faces-redirect=true";
	}
	
}