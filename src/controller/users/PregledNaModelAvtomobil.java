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
		modelAvtomobil = new VehicleModel();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukModeliAvtomobili.contains(modelAvtomobil);
	}
	
	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseVehicleModel(VehicleModel vehicleModel) {
		// проверка против грешно извикване
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		// слагаме исканите данни в заявката
		dataRequest.requestedObject = vehicleModel;
		// слагаме стека който сме прочели в конструктора пак във flash-а
		// забележка: данните за текущата заявка си стоят в стека; само сме добавили исканите данни
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// отиваме на страницата която е направила заявката
		return dataRequest.returnPage + "?faces-redirect=true";
	}
}
