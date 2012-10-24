package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.Service;
import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaUsluga")
@ViewScoped
public class AktualiziraneNaUsluga implements Serializable {
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private Service usluga = new Service();
	private int page = 0;
	private int pagesCount;
	private List<Service> spisukUslugi;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaUsluga() {
		
		// проверяваме дали има заявки записани във flash-а; записваме си стека със заявките, за да го възстановим след това
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			// има заявки във flash-а
			// проверяваме дали заявката е към текущата страница
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
			// ако (dataRequest != null) значи има направена заявка към текущата страница и заявката се пази в dataRequest
		}
		
		readList();
	}
	
	public String getDescription() {
		return usluga.getDescription();
	}

	public void setDescription(String description) {
		usluga.setDescription(description);
	}

	public double getPriceHour() {
		return usluga.getPriceHour();
	}

	public void setPriceHour(double priceHour) {
		usluga.setPriceHour(priceHour);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}

	public List<Service> getSpisukUslugi() {
		return spisukUslugi;
	}

	public int getPagesCount() {
		return pagesCount;
	}
	
	public String writeIt() {
		usluga.writeToDB();
		
		readList();
		
		// set the message
		errorMessage = "Услугата беше актуализирана успешно!";
		
		return null;
	}

	private void readList() {
		spisukUslugi = Service.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		usluga = new Service();
		rowsCount = Service.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (Service service : spisukUslugi) {
			if (usluga == service) {
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
	
	public void selectRow(Service service) {
		usluga = service;
	}
	
	public void deselectRow() {
		usluga = new Service();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukUslugi.contains(usluga);
	}
	
	public boolean isChangingAllowed() {
		// TODO - като се сложи security да се оправят правата на потребителите, за сега всеки може да променя
		return true;
	}
	
	public boolean isChoosingAllowed() {
		return (dataRequest != null);
	}
	
	public String chooseService(Service service) {
		// проверка против грешно извикване
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		// слагаме исканите данни в заявката
		dataRequest.requestedObject = service;
		// слагаме стека който сме прочели в конструктора пак във flash-а
		// забележка: данните за текущата заявка си стоят в стека; само сме добавили исканите данни
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// отиваме на страницата която е направила заявката
		return dataRequest.returnPage + "?faces-redirect=true";
	}

}
