package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import model.SparePartAutoservice;
import model.SparePartRequest;
import model.SparePartReserved;
import model.SparePartSupplier;
import model.SparePartsDelivery;
import model.Supplier;
import model.WarehouseOrder;
import model.WarehouseOrderPart;
import model.WarehouseOrderPartDelivery;
import model.util.UniqueAttributeException;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;
import controller.users.helpers.SparePartDelivered;
import controller.users.helpers.WarehouseOrderComparator;

@SuppressWarnings("serial")
@ManagedBean(name="priemaneNa4asti")
@ViewScoped
public class PriemaneNa4asti implements Serializable {

	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private SparePartsDelivery priemaneNa4asti = new SparePartsDelivery();
	private WarehouseOrder poru4ka;
	private double fullPrice;
	private Key officialSupplierID;
	private Supplier supplier;
	private String errorMessage;
	
	private transient UIComponent writeItButton;

	private List<WarehouseOrder> spisukPoru4ki = new ArrayList<WarehouseOrder>();
	private List<WarehouseOrderPart> spisuk4astiPoru4ani = new ArrayList<WarehouseOrderPart>();
	private List<SparePartDelivered> spisuk4astiDostaveni = new ArrayList<SparePartDelivered>();
	
	
	public PriemaneNa4asti() {
		
		List<Supplier> suppliers = Supplier.queryGetAll(0, 1); // �� �������� ����� ��������� �� � ���������� !!!
		if (suppliers.size() > 0) {
			supplier = suppliers.get(0);
			officialSupplierID = supplier.getID();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void manageFlashData() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) {
				// ������� �� ��������� �� ����� ��������
				this.priemaneNa4asti = ((PriemaneNa4asti)dataRequest.requestObject).priemaneNa4asti;

				if(dataRequest.dataPage.equals("/admin/AktualiziraneNaDostav4ik.jsf")) {
					
					supplier = (Supplier)dataRequest.requestedObject;
					
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
		
		spisukPoru4ki.clear();
		spisuk4astiPoru4ani.clear();
		spisuk4astiDostaveni.clear();
		
		// ������� ������� � ��������� �� �������� ����� ����� ��� ��� �� �� ���������
		spisukPoru4ki = WarehouseOrder.queryGetBySupplierStatus(supplier.getID(), WarehouseOrder.NEW, 0, 1000, currEmployee.getAutoserviceID());
		
	}
	
	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public List<WarehouseOrder> getSpisukPoru4ki() {
		return spisukPoru4ki;
	}
	
	public List<WarehouseOrderPart> getSpisuk4astiPoru4ani() {
		return spisuk4astiPoru4ani;
	}
	
	public List<SparePartDelivered> getSpisuk4astiDostaveni() {
		return spisuk4astiDostaveni;
	}
	
	public String getSpisukPoru4kiRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (WarehouseOrder wo : spisukPoru4ki) {
			if (wo.equals(poru4ka)) {
				strbuff.append("selectedRow,");
			} else {
				strbuff.append("notSelectedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public String getspisuk4astiDostaveniRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (SparePartDelivered spd : spisuk4astiDostaveni) {
			if (spd.isEditing()) {
				strbuff.append("selectedRow,");
			} else {
				strbuff.append("notSelectedRow,");
			}
		}
		
		return strbuff.toString();
	}
	
	public String writeIt() {
		
		if(spisuk4astiDostaveni.isEmpty()) {
			// set the message
			errorMessage = "�������� ��������� �������� �����!";
			return null;
		}
		
		boolean flagNothingToWrite = true;
		for (SparePartDelivered spd : spisuk4astiDostaveni) {
			if (spd.getWarehouseOrderPartDelivery().getQuantity() > 0) {
				flagNothingToWrite = false;
				break;
			}
		}
		
		if (flagNothingToWrite) {
			// set the message
			errorMessage = "��������� ���������� �������� �� 0!";
			return null;
		}

		// �� �� ��������� ��������� �� ����� ����� �� �������� ������ ���� �� ���������
		Map<WarehouseOrder, Integer> spisukPoru4kiZaProverka = new TreeMap<WarehouseOrder, Integer>(new WarehouseOrderComparator());
		
		priemaneNa4asti.setAutoserviceID(currEmployee.getAutoserviceID());
		priemaneNa4asti.setDeliveryDate(new Date());
		priemaneNa4asti.setFullPrice(fullPrice);
		priemaneNa4asti.setStatus(SparePartsDelivery.NOT_PAYED);
	 
		Transaction tr = DatastoreServiceFactory.getDatastoreService().beginTransaction();
		
		try {
			priemaneNa4asti.writeToDB();
			errorMessage = "�������� �� ��������� ����� ���� �������� �������!";
		
			// ������� ������ � ������ ������ �� �����, �� �� ����� �� �� �������������
			List<SparePartRequest> spisukZaqveni4asti = SparePartRequest.queryGetByStatus(SparePartRequest.ORDERED, 0, 1000, currEmployee.getAutoserviceID());
			
			for (SparePartDelivered spd : spisuk4astiDostaveni) {
	
				spd.getWarehouseOrderPartDelivery().setSparePartsDeliveryID(priemaneNa4asti.getID());
				spd.getWarehouseOrderPartDelivery().writeToDB();
				
				double quantity = spd.getWarehouseOrderPartDelivery().getQuantity();
				double quantityToReserveTotal = 0;
				Key sparePartID = spd.getWarehouseOrderPartDelivery().getSparePartID();
				
				// ������������� ������� �� ���������� �����
				WarehouseOrderPart wop = WarehouseOrderPart.queryGetBySparePartID(sparePartID, 0, 1, spd.getWarehouseOrderPartDelivery().getWarehouseOrderID()).get(0);
				wop.setDeliveredQuantity(wop.getDeliveredQuantity() + quantity);
				wop.writeToDB();
				if (wop.getNeededQuantity() == 0) {
					// ���� ����� �� ��������� ������
					Integer count = spisukPoru4kiZaProverka.get(wop);
					if (count == null) {
						// ���� ��� ������ ������� � �������
						count = 0;
					}
					count++;
					spisukPoru4kiZaProverka.put(spd.getWarehouseOrderPartDelivery().getWarehouseOrder(), count);
				}
				
				for (SparePartRequest spr : spisukZaqveni4asti) {
					if (spr.getSparePartID().equals(sparePartID)) {
						double quantityToReserve;
						
						// ������������� �������� �� ����� �� ��������� �������
						if (spr.getQuantity() - spr.getQuantityDelivered() > quantity) {
							// ������������ ����� � ��������� ���� �� � ������ ������� ����������
							spr.setQuantityDelivered(spr.getQuantityDelivered() + quantity);
							quantityToReserve = quantity;
							quantity = 0;
						} else {
							// ������������ ����� � ������� � ���������
							quantity -= spr.getQuantity() - spr.getQuantityDelivered();
							quantityToReserve = spr.getQuantity() - spr.getQuantityDelivered();
							spr.setQuantityDelivered(spr.getQuantity());
							spr.setStatus(SparePartRequest.COMPLETED);
						}
						spr.writeToDB();
						quantityToReserveTotal += quantityToReserve;
						
						// ����������� �� ����� �� ��������� �������
						List<SparePartReserved> spReservedList = SparePartReserved.queryGetBySparePart(sparePartID, 0, 1, spr.getClientOrderID());
						if (spReservedList.size() > 0) {
							// ��� ���� ����������� �� ���� ����� �� ���� ��������� �������
							SparePartReserved spReserved = spReservedList.get(0);
							spReserved.setQuantity(spReserved.getQuantity() + quantityToReserve);
							spReserved.writeToDB();
						} else {
							// ��� ��� ���� ����������� ����� �� ���� ��� �� ���� ��������� �������
							SparePartReserved spReserved = new SparePartReserved();
							spReserved.setClientOrderID(spr.getClientOrderID());
							spReserved.setSparePartID(spr.getSparePartID());
							spReserved.setUsed(0);
							spReserved.setQuantity(quantityToReserve);
							spReserved.writeToDB();
						}
					}
				}
				
				// �����������, �� ������� �� ��������� � ���� �� ��� �� ����������� (����������)
				SparePartAutoservice spa = SparePartAutoservice.queryGetBySparePartID(sparePartID, 0, 1, currEmployee.getAutoserviceID()).get(0);
				spa.setQuantityAvailable(spa.getQuantityAvailable() + quantity);
				spa.setQuantityReserved(spa.getQuantityReserved() + quantityToReserveTotal);
				spa.setQuantityRequested(spa.getQuantityRequested() - quantityToReserveTotal);
				spa.setQuantityOrdered(spa.getQuantityOrdered() - spd.getWarehouseOrderPartDelivery().getQuantity());
				spa.writeToDB();
			}
			
			for (Entry<WarehouseOrder, Integer> wo : spisukPoru4kiZaProverka.entrySet()) {
				if (WarehouseOrderPart.countGetActive(wo.getKey().getID()) == wo.getValue()) {
					wo.getKey().setStatus(WarehouseOrder.COMPLETED);
					wo.getKey().writeToDB();
				}
			}
			
			//TODO - ��������� �� ��������� �� �������, ��� � ����� �����
		
			tr.commit();
			
		} catch (UniqueAttributeException e) {
			tr.rollback();
			errorMessage = "���������� ������!";
			return null;
		}
		
		// clean the data
		priemaneNa4asti = new SparePartsDelivery();
		poru4ka = null;
		fullPrice = 0;
		readList();
		
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
	
	public double getFullPrice() {
		return fullPrice;
	}
	
	public void chooseOrder(WarehouseOrder warehouseOrder) {
		poru4ka = warehouseOrder;
		spisuk4astiPoru4ani = WarehouseOrderPart.queryGetActive(0, 1000, poru4ka.getID());
		
		// ������ ������� ����� ���� �� � ������ ������
		for (Iterator<WarehouseOrderPart> iterator = spisuk4astiPoru4ani.iterator(); iterator.hasNext();) {
			WarehouseOrderPart wop = iterator.next();
			for (SparePartDelivered spd : spisuk4astiDostaveni) {
				if (spd.getWarehouseOrderPartDelivery().getSparePartID().equals(wop.getSparePartID()) &&
					spd.getWarehouseOrderPartDelivery().getWarehouseOrderID().equals(wop.getWarehouseOrderID())) {
					iterator.remove();
					break;
				}
			}
		}
	}
	
	public void addSparePartDelivered(WarehouseOrderPart wop) {
		
		WarehouseOrderPartDelivery wopd = new WarehouseOrderPartDelivery();
		SparePartDelivered spd = new SparePartDelivered();
		
		wopd.setSparePart(wop.getSparePart());
		wopd.setWarehouseOrder(wop.getWarehouseOrder());
		wopd.setQuantity(wop.getNeededQuantity());
		if (wop.getWarehouseOrder().getSupplierID().equals(officialSupplierID)) {
			// ���������� ���������
			wopd.setPrice(wop.getSparePart().getDeliveryPrice());
		} else {
			// ����������� ���������
			List<SparePartSupplier> spsl = SparePartSupplier.queryGetBySparePartID(wop.getSparePartID(), 0, 1, wop.getWarehouseOrder().getSupplierID());
			wopd.setPrice(spsl.get(0).getDeliveryPrice());
		}
		
		spd.setWarehouseOrderPartDelivery(wopd);
		spd.setQuantityNotDelivered(wop.getNeededQuantity());
		spd.recalculateFullPrice();
		
		spisuk4astiDostaveni.add(0, spd);
		spisuk4astiPoru4ani.remove(wop);
		
		recalculateFullPrice();
	}
	
	public void removeSparePartDelivered(SparePartDelivered spd) {
		List<WarehouseOrderPart> wopl = WarehouseOrderPart.queryGetBySparePartID(spd.getWarehouseOrderPartDelivery().getSparePartID(), 0, 1, poru4ka.getID());
		
		if (wopl.size() > 0) {
			WarehouseOrderPart wop = wopl.get(0);
			if (poru4ka.getID().equals(wop.getWarehouseOrderID())) {
				spisuk4astiPoru4ani.add(0, wop);
			}
		}
		spisuk4astiDostaveni.remove(spd);
		
		recalculateFullPrice();
	}
	
	public void toggleEditSparePartDelivered(SparePartDelivered spd) {
		spd.toggleEditing();
		recalculateFullPrice();
	}

	private void recalculateFullPrice() {
		fullPrice = 0;
		for (SparePartDelivered spd : spisuk4astiDostaveni) {
			fullPrice += spd.getFullPrice();
		}
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public String getDocumentNumber() {
		return priemaneNa4asti.getDocumentNumber();
	}

	public void setDocumentNumber(String documentNumber) {
		priemaneNa4asti.setDocumentNumber(documentNumber);
	}

	public Date getDocumentDate() {
		return priemaneNa4asti.getDocumentDate();
	}

	public void setDocumentDate(Date documentDate) {
		priemaneNa4asti.setDocumentDate(documentDate);
	}

	public UIComponent getWriteItButton() {
		return writeItButton;
	}

	public void setWriteItButton(UIComponent writeItButton) {
		this.writeItButton = writeItButton;
	}
	
}
