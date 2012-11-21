package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.google.appengine.api.datastore.Key;

import model.SparePart;
import model.SparePartGroup;
import model.VehicleModel;
import model.VehicleModelSparePart;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaRezervna4ast")
@ViewScoped
public class AktualiziraneNaRezervna4ast implements Serializable {

	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private SparePart rezervna4ast = new SparePart();
	private int page = 0;
	private int pagesCount;
	private List<SparePart> spisukRezervni4asti;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private Key searchSparePartGroupID;
	private boolean searchBySparePartGroup;
	private Key searchSparePartModelID;
	private boolean searchBySparePartModel;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaRezervna4ast() {
		
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
	
	public SparePartGroup getSparePartGroup() {
		return rezervna4ast.getSparePartGroup();
	}

	public String getName() {
		return rezervna4ast.getName();
	}

	public void setName(String name) {
		rezervna4ast.setName(name);
	}

	public String getDescription() {
		return rezervna4ast.getDescription();
	}

	public double getDeliveryPrice() {
		return rezervna4ast.getDeliveryPrice();
	}

	public double getSalePrice() {
		return rezervna4ast.getSalePrice();
	}

	public void setSalePrice(double salePrice) {
		rezervna4ast.setSalePrice(salePrice);
	}
	
	public String getMeasuringUnit() {
		return rezervna4ast.getMeasuringUnit();
	}
	
	public void setAllPages(AllPages allPages) {
		this.allPages = allPages;
	}

	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
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

	public List<SparePart> getSpisukRezervni4asti() {
		return spisukRezervni4asti;
	}

	public int getPagesCount() {
		return pagesCount;
	}
	
	public String writeIt() {
		
		if (!isChangingAllowed()) {
			errorMessage = "Нямате права за актуализирането на данните!";
			return null;
		}
		
		rezervna4ast.writeToDB();
		
		readList();
		
		// set the message
		errorMessage = "Резервната част беше актуализирана успешно!";
		
		return null;
	}

	private void readList() {
		spisukRezervni4asti = SparePart.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		rezervna4ast = new SparePart();
		rowsCount = SparePart.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (SparePart sp : spisukRezervni4asti) {
			if (rezervna4ast == sp) {
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
	
	public void selectRow(SparePart sparePart) {
		rezervna4ast = sparePart;
		errorMessage = null;
	}
	
	public void deselectRow() {
		rezervna4ast = new SparePart();
		errorMessage = null;
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukRezervni4asti.contains(rezervna4ast);
	}
	
	public boolean isChangingAllowed() {
		return allPages.getWriteRight(
				FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath(),
				currEmployee.getPosition());
	}
	
	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseService(SparePart sparePart) {
		// проверка против грешно извикване
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		// слагаме исканите данни в заявката
		dataRequest.requestedObject = sparePart;
		// слагаме стека който сме прочели в конструктора пак във flash-а
		// забележка: данните за текущата заявка си стоят в стека; само сме добавили исканите данни
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// отиваме на страницата която е направила заявката
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
	public List<SparePartGroup> getSparePartGroups() {
		List<SparePartGroup> spg = SparePartGroup.queryGetAll(0, 1000);
		return spg;
	}
	
	public List<VehicleModel> getVehicleModels() {
		List<VehicleModel> vm = VehicleModel.queryGetAll(0, 1000);
		return vm;
	}
	
	public Key getSearchSparePartGroupID() {
		return searchSparePartGroupID;
	}

	public void setSearchSparePartGroupID(Key searchSparePartGroupID) {
		this.searchSparePartGroupID = searchSparePartGroupID;
	}
	
	public Key getSearchSparePartModelID() {
		return searchSparePartModelID;
	}

	public void setSearchSparePartModelID(Key searchSparePartModelID) {
		this.searchSparePartModelID = searchSparePartModelID;
	}
	
	public boolean isSearchBySparePartGroup() {
		return searchBySparePartGroup;
	}

	public void setSearchBySparePartGroup(boolean searchBySparePartGroup) {
		this.searchBySparePartGroup = searchBySparePartGroup;
	}

	public boolean isSearchBySparePartModel() {
		return searchBySparePartModel;
	}

	public void setSearchBySparePartModel(boolean searchBySparePartModel) {
		this.searchBySparePartModel = searchBySparePartModel;
	}

	public void searchIt() {

		if (!searchBySparePartGroup) {
			searchSparePartGroupID = null;
		}
		
		if (!searchBySparePartModel) {
			searchSparePartModelID = null;
		}
		
		if ((searchSparePartGroupID == null) && (searchSparePartModelID == null)) {
			readList();
			return;
		}
		
		spisukRezervni4asti.clear();
		rowsCount = 0;
		
		if (searchSparePartModelID != null) {
			List<VehicleModelSparePart> vehicleModelSparePartList = VehicleModelSparePart.queryGetAll(0, 1000, searchSparePartModelID);
			for (VehicleModelSparePart vehicleModelSparePart : vehicleModelSparePartList) {
				if ((searchSparePartGroupID == null) ||
					vehicleModelSparePart.getSparePart().getSparePartGroupID().equals(searchSparePartGroupID)) {
					
					spisukRezervni4asti.add(vehicleModelSparePart.getSparePart());
					rowsCount++;
				}
			}
		} else {
			spisukRezervni4asti = SparePart.queryGetBySparePartGroupID(searchSparePartGroupID, page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
			rowsCount = SparePart.countGetBySparePartGroupID(searchSparePartGroupID);
		}
		
		page = 0;
		rezervna4ast = new SparePart();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public void resetSearch() {
		searchSparePartGroupID = null;
		searchSparePartModelID = null;
		searchBySparePartGroup = false;
		searchBySparePartModel = false;
		searchIt();
	}
	
}
