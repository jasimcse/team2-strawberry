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

import com.google.appengine.api.datastore.Key;

import model.Autoservice;
import model.Employee;
import model.Supplier;
import model.WarehouseOrder;
import model.WarehouseOrderPart;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="pregledNaPoru4kaNa4asti")
@ViewScoped
public class PregledNaPoru4kaNa4asti implements Serializable {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private WarehouseOrder poru4ka = new WarehouseOrder();
	private List<WarehouseOrder> spisukPoru4ki;
	private Key autoserviceID;
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	private InterPageDataRequest dataRequest;
	
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

	public List<WarehouseOrder> getSpisukPoru4ki() {
		return spisukPoru4ki;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukPoru4ki = WarehouseOrder.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize(), autoserviceID);
		poru4ka = new WarehouseOrder();
		rowsCount = WarehouseOrder.countGetAll(autoserviceID);
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (WarehouseOrder wo : spisukPoru4ki) {
			if (poru4ka == wo) {
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
	
	public void selectRow(WarehouseOrder wo) {
		poru4ka = wo;
	}
	
	public boolean isRowSelected() {
		return spisukPoru4ki.contains(poru4ka);
	}

	public Key getAutoserviceID() {
		return autoserviceID;
	}

	public void setAutoserviceID(Key autoserviceID) {
		this.autoserviceID = autoserviceID;
	}

	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}

	public Autoservice getAutoservice() {
		return poru4ka.getAutoservice();
	}

	public Supplier getSupplier() {
		return poru4ka.getSupplier();
	}

	public Employee getEmployee() {
		return poru4ka.getEmployee();
	}

	public Date getDate() {
		return poru4ka.getDate();
	}

	public String getStatus() {
		if ("1".equals(poru4ka.getStatus())) {
			return "Нова";
		} else if ("2".equals(poru4ka.getStatus())) {
			return "Изпълнена";
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
		sta.put("Нова", WarehouseOrder.NEW);
		sta.put("Изпълнена", WarehouseOrder.COMPLETED);
		return sta;
	}
	
	public List<WarehouseOrderPart> getSpisuk4asti() {
		if (isRowSelected()) {
			return WarehouseOrderPart.queryGetAll(0, 1000, poru4ka.getID());
		} else {
			return null;
		}
	}
	
	public boolean isChoosingAllowed() {
		return (dataRequest != null);
	}
	
	public String chooseWarehouseOrder(WarehouseOrder wo) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = wo;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}

}
