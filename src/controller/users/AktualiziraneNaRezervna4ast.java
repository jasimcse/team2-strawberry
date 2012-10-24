package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.SparePart;
import model.SparePartGroup;
import model.VehicleModel;
import controller.common.ConfigurationProperties;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaRezervna4ast")
@ViewScoped
public class AktualiziraneNaRezervna4ast implements Serializable {

	private Stack<InterPageDataRequest> dataRequestStack;
	
	private SparePart rezervna4ast = new SparePart();
	private int page = 0;
	private int pagesCount;
	private List<SparePart> spisukRezervni4asti;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public AktualiziraneNaRezervna4ast() {
		
		// ����������� ���� ��� ������ �������� ��� flash-�; ��������� �� ����� ��� ��������, �� �� �� ����������� ���� ����
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			// ��� ������ ��� flash-�
			// ����������� ���� �������� � ��� �������� ��������
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
			// ��� (dataRequest != null) ����� ��� ��������� ������ ��� �������� �������� � �������� �� ���� � dataRequest
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
		rezervna4ast.writeToDB();
		
		readList();
		
		// set the message
		errorMessage = "���������� ���� ���� ������������� �������!";
		
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
	}
	
	public void deselectRow() {
		rezervna4ast = new SparePart();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukRezervni4asti.contains(rezervna4ast);
	}
	
	public boolean isChoosingAlowed() {
		return (dataRequest != null);
	}
	
	public String chooseService(SparePart sparePart) {
		// �������� ������ ������ ���������
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		// ������� �������� ����� � ��������
		dataRequest.requestedObject = sparePart;
		// ������� ����� ����� ��� ������� � ������������ ��� ��� flash-�
		// ���������: ������� �� �������� ������ �� ����� � �����; ���� ��� �������� �������� �����
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// ������� �� ���������� ����� � ��������� ��������
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
	
}
