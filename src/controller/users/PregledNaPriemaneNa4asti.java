package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.SparePartsDelivery;
import model.Supplier;
import model.WarehouseOrderPartDelivery;

import com.google.appengine.api.datastore.Key;

import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="pregledNaPriemaneNa4asti")
@ViewScoped
public class PregledNaPriemaneNa4asti implements Serializable {
	
	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private SparePartsDelivery dostavka = new SparePartsDelivery();
	private List<SparePartsDelivery> spisukDostavki;
	private Key autoserviceID;
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private String errorMessage;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	private InterPageDataRequest dataRequest;
	
	// атрибути за търсене
	private String searchSupplier;
	private String searchStatus;
	private Date searchDateFrom;
	private Date searchDateTo;
	
	@SuppressWarnings("unchecked")
	public void PregledNaGarancionniUsloviq() {
		
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
		
		autoserviceID = currEmployee.getAutoserviceID();
		
		readList();
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}

	public List<SparePartsDelivery> getSpisukDostavki() {
		return spisukDostavki;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukDostavki = SparePartsDelivery.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize(), autoserviceID);
		dostavka = new SparePartsDelivery();
		rowsCount = SparePartsDelivery.countGetAll(autoserviceID);
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (SparePartsDelivery spd : spisukDostavki) {
			if (dostavka == spd) {
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
	
	public void selectRow(SparePartsDelivery spd) {
		dostavka = spd;
	}
	
	public boolean isRowSelected() {
		return spisukDostavki.contains(dostavka);
	}
	
	public boolean isChangingAllowed() {
		return allPages.getWriteRight(
				FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath(),
				currEmployee.getPosition());
	}
	
	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public void setAllPages(AllPages allPages) {
		this.allPages = allPages;
	}
	
	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}
	
	public void chooseDelivery(SparePartsDelivery spd) {
		dostavka = spd;
	}
	
	public List<WarehouseOrderPartDelivery> getSpisuk4asti() {
		if (isRowSelected()) {
			return WarehouseOrderPartDelivery.queryGetAll(0, 1000, dostavka.getID());
		} else {
			return null;
		}
	}
	
	public List<Supplier> getSuppliers() {
		List<Supplier> sup = Supplier.queryGetAll(0, 1000);
		return sup;
	}
	
	public Map<String, String> getStatuses() {
		Map<String, String> sta = new TreeMap<String, String>();
		sta.put("Неплатена", SparePartsDelivery.NOT_PAYED);
		sta.put("Платена", SparePartsDelivery.PAYED);
		return sta;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getDocumentNumber() {
		return dostavka.getDocumentNumber();
	}

	public Date getDocumentDate() {
		return dostavka.getDocumentDate();
	}

	public Date getDeliveryDate() {
		return dostavka.getDeliveryDate();
	}

	public String getStatus() {
		if ("1".equals(dostavka.getStatus())) {
			return "Неплатена";
		} else if ("2".equals(dostavka.getStatus())) {
			return "Платена";
		} else {
			return null;
		}
	}

	public void setStatus(String status) {
		dostavka.setStatus(status);
	}

	public String getPaymentNumber() {
		return dostavka.getPaymentNumber();
	}

	public void setPaymentNumber(String paymentNumber) {
		dostavka.setPaymentNumber(paymentNumber);
	}

	public double getFullPrice() {
		return dostavka.getFullPrice();
	}
	
	public String writeIt() {
		
		if (!isChangingAllowed()) {
			errorMessage = "Нямате права за актуализирането на данните!";
			return null;
		}
		
		if (dostavka.getStatus().equals("1")) {
			// set the message
			errorMessage = "Няма промени по статуса на доставката!";
			readList();
			return null;
		}
		
		dostavka.writeToDB();
	
		readList();

		// set the message
		errorMessage = "Доставката беше актуализирана успешно!";
		
		return null;
	}
	

	public String getSearchSupplier() {
		return searchSupplier;
	}

	public void setSearchSupplier(String searchSupplier) {
		this.searchSupplier = searchSupplier;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public Date getSearchDateFrom() {
		return searchDateFrom;
	}

	public void setSearchDateFrom(Date searchDateFrom) {
		this.searchDateFrom = searchDateFrom;
	}

	public Date getSearchDateTo() {
		return searchDateTo;
	}

	public void setSearchDateTo(Date searchDateTo) {
		this.searchDateTo = searchDateTo;
	}
	

	public void searchIt() {

		if ((searchDateTo == null) && (searchDateFrom == null)) {
			readList();
			return;
		}
		
		page = 0;

		spisukDostavki = SparePartsDelivery.querySearch(
				currEmployee.getAutoserviceID(),
				searchSupplier,
				searchStatus,
				searchDateFrom,
				searchDateTo,
				page * ConfigurationProperties.getPageSize(),
				ConfigurationProperties.getPageSize());

		rowsCount = SparePartsDelivery.countSearch(
				currEmployee.getAutoserviceID(),
				searchSupplier,
				searchStatus,
				searchDateFrom,
				searchDateTo);
		dostavka = new SparePartsDelivery();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public void resetSearch() {
		
		searchDateFrom = null;
		searchDateTo = null;
		searchIt();
	}

	
}
