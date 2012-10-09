package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import model.VehicleModel;
import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="pregledNaModelAvtomobil")
@ViewScoped
public class PregledNaModelAvtomobil implements Serializable {

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private VehicleModel modelAvtomobil = new VehicleModel();
	private int page = 0;
	private int pagesCount;
	private List<VehicleModel> spisukModeliAvtomobili;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public PregledNaModelAvtomobil() {
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		readList();
	}
	
	public String getBrand() {
		return modelAvtomobil.getBrand();
	}

	public String getModel() {
		return modelAvtomobil.getModel();
	}

	public String getCharacteristics() {
		return modelAvtomobil.getCharacteristics();
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

	public List<VehicleModel> getSpisukModeliAvtomobili() {
		return spisukModeliAvtomobili;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukModeliAvtomobili = VehicleModel.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize());
		modelAvtomobil = new VehicleModel();
		rowsCount = VehicleModel.countGetAll();
		pagesCount = rowsCount / ConfigurationProperties.getPageSize();
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (VehicleModel vm : spisukModeliAvtomobili) {
			if (modelAvtomobil == vm) {
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
	
	public void selectRow(VehicleModel vm) {
		modelAvtomobil = vm;
	}
	
	public void deselectRow() {
		modelAvtomobil= new VehicleModel();
	}
	
	public boolean isRowSelected() {
		return spisukModeliAvtomobili.contains(modelAvtomobil);
	}
	
	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseVehicleModel(VehicleModel vehicleModel) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = vehicleModel;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
}
