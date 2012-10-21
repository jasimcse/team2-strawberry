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

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

import model.SparePart;
import model.SparePartAutoservice;
import model.SparePartRequest;
import model.SparePartSupplier;
import model.Supplier;
import model.WarehouseOrder;
import model.WarehouseOrderPart;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;
import controller.users.helpers.SparePartForOrder;
import controller.users.helpers.SparePartNeeded;

@SuppressWarnings("serial")
@ManagedBean(name="poru4kaNa4asti")
@ViewScoped
public class Poru4kaNa4asti implements Serializable {

	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private WarehouseOrder poru4kaNa4asti = new WarehouseOrder();
	private double fullPrice;
	private boolean flagReadIt = true;
	private Key officialSupplierID;
	private String errorMessage;

	private List<SparePartNeeded> spisuk4astiNujni = new ArrayList<SparePartNeeded>();
	private List<SparePartForOrder> spisuk4astiZaPoru4ka = new ArrayList<SparePartForOrder>();
	private List<SparePartRequest> spisukZaqveni4asti;
	
	
	public Poru4kaNa4asti() {
		
		// слагаме доставчика по подразбиране
		List<Supplier> suppliers = Supplier.queryGetAll(0, 1); // би трябвало първи доставчик да е официалния !!!
		if (suppliers.size() > 0) {
			poru4kaNa4asti.setSupplier(suppliers.get(0));
			officialSupplierID = suppliers.get(0).getID();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void manageFlashData() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) {
				// връщаме от запитване на друга страница
				this.poru4kaNa4asti = ((Poru4kaNa4asti)dataRequest.requestObject).poru4kaNa4asti;
				this.spisuk4astiNujni = ((Poru4kaNa4asti)dataRequest.requestObject).spisuk4astiNujni;
				this.spisuk4astiZaPoru4ka = ((Poru4kaNa4asti)dataRequest.requestObject).spisuk4astiZaPoru4ka;
				this.fullPrice = ((Poru4kaNa4asti)dataRequest.requestObject).fullPrice;

				flagReadIt = false;
				
				if(dataRequest.dataPage.equals("/admin/AktualiziraneNaDostav4ik.jsf")) {
					
					this.poru4kaNa4asti.setSupplier((Supplier)dataRequest.requestedObject);
					// NOTE: инициализираме списъците, понеже може да е избран друг доставчик,
					// който може и да не доставя част от частите които вече са избрани
					spisuk4astiNujni.clear();
					spisuk4astiZaPoru4ka.clear();
					fullPrice = 0;
					flagReadIt = true;
					
				} else if(dataRequest.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf")) {
					
					SparePart sp = (SparePart)dataRequest.requestedObject;
					
					WarehouseOrderPart wop = new WarehouseOrderPart();
					SparePartForOrder spfo = new SparePartForOrder();
					
					wop.setSparePart(sp);
					wop.setOrderedQuantity(0);
					
					spfo.setWarehouseOrderPart(wop);
					spfo.setQuantityNeeded(0);
					List<SparePartAutoservice> spal = SparePartAutoservice.queryGetBySparePartID(sp.getID(), 0, 1, currEmployee.getAutoserviceID());
					spfo.setQuantityAvailable(spal.get(0).getQuantityAvailable());
					
					if (poru4kaNa4asti.getSupplierID().equals(officialSupplierID)) {
						// официалния доставчик
						spfo.setSupplierPrice(sp.getDeliveryPrice());
					} else {
						// неофициален доставчик
						List<SparePartSupplier> spsl = SparePartSupplier.queryGetBySparePartID(sp.getID(), 0, 1, poru4kaNa4asti.getSupplierID());
						if (spsl.size() == 0) {
							// доставчика който е избран не доставя тази част
							errorMessage = "Доставчика, който сте избрали не доставя избраната част!";
						} else {
							spfo.setSupplierPrice(spsl.get(0).getDeliveryPrice());
						}
					}
					spfo.recalculateFullPrice();
					
					if (errorMessage == null) {
						// ако всичко е ОК, добавяме частта в списъка
						spisuk4astiZaPoru4ka.add(0, spfo);
					}
					recalculateFullPrice();
					
				}
			}
		}
	}
	
	@PostConstruct
	public void myInit() {
		manageFlashData();
		readList();
	}
	
	public void readList() {
		
		if (flagReadIt) {
			spisuk4astiNujni.clear();
			spisuk4astiZaPoru4ka.clear();
			
			// Вземаме списъка с разервните части които имат количество под минимума си
			List<SparePartAutoservice> spal = SparePartAutoservice.queryGetBelowMinimum(0, 1000, currEmployee.getAutoserviceID());
			
			for (SparePartAutoservice spa : spal) {
				SparePartNeeded spn = new SparePartNeeded();
				spn.setSparePart(spa.getSparePart());
				spn.setQuantityNeeded(spa.getBelowMinimum());
				spn.setAvailableAtCurrentSupplier(true);
				if (!poru4kaNa4asti.getSupplierID().equals(officialSupplierID)) {
					// доставчика, който е избран не е официалния -> забраняваме частите които не се доставят от този доставчик
					if (SparePartSupplier.countGetBySparePartID(spa.getSparePartID(), poru4kaNa4asti.getSupplierID()) == 0) {
						spn.setAvailableAtCurrentSupplier(false);
					}
				}
				
				spisuk4astiNujni.add(spn);
			}
			
			// Вземаме списъка със заявените резервни части за клиентски поръчки, които още не са поръчани
			// запазваме си списъка, за да отразим кои са поръчани
			spisukZaqveni4asti = SparePartRequest.queryGetNew(0, 1000, currEmployee.getAutoserviceID());
			
			recalculateFullPrice();
			flagReadIt = false;
		}
	}
	
	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public List<SparePartNeeded> getSpisuk4astiNujni() {
		return spisuk4astiNujni;
	}
	
	public List<SparePartForOrder> getSpisuk4astiZaPoru4ka() {
		return spisuk4astiZaPoru4ka;
	}
	
	public String getSpisuk4astiNujniRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (SparePartNeeded spn : spisuk4astiNujni) {
			if (spn.isAvailableAtCurrentSupplier()) {
				strbuff.append("allowedRow,");
			} else {
				strbuff.append("disallowedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public String getSpisukZaPoru4kaRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (SparePartForOrder spfo : spisuk4astiZaPoru4ka) {
			if (spfo.isEditing()) {
				strbuff.append("selectedRow,");
			} else {
				strbuff.append("notSelectedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public String writeIt() {
		
		if(spisuk4astiZaPoru4ka.isEmpty()) {
			// set the message
			errorMessage = "Изберете резервни части!";
			return null;
		}

		poru4kaNa4asti.setDate(new Date());
		poru4kaNa4asti.setAutoserviceID(currEmployee.getAutoserviceID());
		poru4kaNa4asti.setEmployeeID(currEmployee.getEmployeeID());
		poru4kaNa4asti.setStatus(WarehouseOrder.NEW);
	 
		Transaction tr = DatastoreServiceFactory.getDatastoreService().beginTransaction();
		
		poru4kaNa4asti.writeToDB();
		
		for (SparePartForOrder spfo : spisuk4astiZaPoru4ka) {
			WarehouseOrderPart wop = spfo.getWarehouseOrderPart();
			wop.setDeliveredQuantity(0);
			wop.setWarehouseOrderID(poru4kaNa4asti.getID());
			
			wop.writeToDB();
			
			// актуализираме заявките които са изпълнени
			for (SparePartRequest spr : spisukZaqveni4asti) {
				if (spr.getSparePartID().equals(wop.getSparePartID())) {
					spr.setStatus(SparePartRequest.COMPLETED);
				}
				spr.writeToDB();
			}
			
			// отбелязваме, че сме поръчали частите
			SparePartAutoservice spa = SparePartAutoservice.queryGetBySparePartID(wop.getSparePartID(), 0, 1, currEmployee.getAutoserviceID()).get(0);
			spa.setQuantityOrdered(spa.getQuantityOrdered() + wop.getOrderedQuantity());
			spa.writeToDB();
		}
			
		// TODO - да се изпрати заявката по WSDL
		
		tr.commit();
		
		// clean the data
		poru4kaNa4asti = new WarehouseOrder();
		poru4kaNa4asti.setSupplierID(officialSupplierID);
		flagReadIt = true;
		readList();
		
		// set the message
		errorMessage = "Поръчката на части беше направена успешно!";
		
		return null;
	}
	
	public String chooseSupplier()	{
		
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
		
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/admin/AktualiziraneNaDostav4ik.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
	}
	
	public String chooseSparePart()	{
		
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
	
	public double getFullPrice() {
		return fullPrice;
	}
	
	public void addSparePartForOrder(SparePartNeeded spn) {
		
		WarehouseOrderPart wop = new WarehouseOrderPart();
		SparePartForOrder spfo = new SparePartForOrder();
		
		wop.setSparePart(spn.getSparePart());
		wop.setOrderedQuantity(spn.getQuantityNeeded());
		
		spfo.setWarehouseOrderPart(wop);
		spfo.setQuantityNeeded(spn.getQuantityNeeded());
		
		List<SparePartAutoservice> spal = SparePartAutoservice.queryGetBySparePartID(spn.getSparePart().getID(), 0, 1, currEmployee.getAutoserviceID());
		spfo.setQuantityAvailable(spal.get(0).getQuantityAvailable());
		
		if (poru4kaNa4asti.getSupplierID().equals(officialSupplierID)) {
			// официалния доставчик
			spfo.setSupplierPrice(spn.getSparePart().getDeliveryPrice());
		} else {
			// неофициален доставчик
			List<SparePartSupplier> spsl = SparePartSupplier.queryGetBySparePartID(spn.getSparePart().getID(), 0, 1, poru4kaNa4asti.getSupplierID());
			spfo.setSupplierPrice(spsl.get(0).getDeliveryPrice());
		}
		spfo.recalculateFullPrice();
		
		spisuk4astiZaPoru4ka.add(0, spfo);
		spisuk4astiNujni.remove(spn);
		
		recalculateFullPrice();
	}
	
	public void removeSparePartForOrder(SparePartForOrder spfo) {
		SparePartNeeded spn = new SparePartNeeded();
		
		spn.setSparePart(spfo.getWarehouseOrderPart().getSparePart());
		spn.setQuantityNeeded(spfo.getQuantityNeeded());
		spn.setAvailableAtCurrentSupplier(true);
		
		if (spfo.getQuantityNeeded() > 0) {
			// частта е дошла от предната таблица, връщаме я пак там
			spisuk4astiNujni.add(0, spn);
		}
		spisuk4astiZaPoru4ka.remove(spfo);
		
		recalculateFullPrice();
	}
	
	public void toggleEditSparePartForOrder(SparePartForOrder spfo) {
		spfo.toggleEditing();
		recalculateFullPrice();
	}

	private void recalculateFullPrice() {
		fullPrice = 0;
		for (SparePartForOrder spfo : spisuk4astiZaPoru4ka) {
			fullPrice += spfo.getFullPrice();
		}
	}
	
	public Supplier getSupplier() {
		return poru4kaNa4asti.getSupplier();
	}

}
