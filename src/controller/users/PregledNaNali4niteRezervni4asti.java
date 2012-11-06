package controller.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.SparePart;
import model.SparePartAutoservice;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@SuppressWarnings("serial")
@ManagedBean(name="pregledNaNali4niteRezervni4asti")
@ViewScoped
public class PregledNaNali4niteRezervni4asti implements Serializable {
	
	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private SparePartAutoservice nali4nost = new SparePartAutoservice();
	private float quantityBadRequest;
	private int page = 0;
	private int pagesCount;
	private List<SparePartAutoservice> spisukNali4nosti;
	private int rowsCount;
	private InterPageDataRequest dataRequest;
	
	private String errorMessage;
	
	@SuppressWarnings("unchecked")
	public PregledNaNali4niteRezervni4asti() {
		
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
		
	}
	
	@PostConstruct
	public void init() {
		readList();
	}
	
	public float getQuantityBadRequest() {
		return quantityBadRequest;
	}

	public void setQuantityBadRequest(float quantityBadRequest) {
		this.quantityBadRequest = quantityBadRequest;
	}
	
	public void setAllPages(AllPages allPages) {
		this.allPages = allPages;
	}

	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}

	public SparePart getSparePart() {
		return nali4nost.getSparePart();
	}

	public double getQuantityAvailable() {
		return nali4nost.getQuantityAvailable();
	}

	public double getQuantityMinimum() {
		return nali4nost.getQuantityMinimum();
	}

	public void setQuantityMinimum(double quantityMinimum) {
		nali4nost.setQuantityMinimum(quantityMinimum);
	}

	public double getQuantityBad() {
		return nali4nost.getQuantityBad();
	}

	public double getQuantityReserved() {
		return nali4nost.getQuantityReserved();
	}

	public double getQuantityOrdered() {
		return nali4nost.getQuantityOrdered();
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

	public List<SparePartAutoservice> getSpisukNali4nosti() {
		return spisukNali4nosti;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		spisukNali4nosti = SparePartAutoservice.queryGetAll(page * ConfigurationProperties.getPageSize(), ConfigurationProperties.getPageSize(), currEmployee.getAutoserviceID());
		nali4nost = new SparePartAutoservice();
		quantityBadRequest = 0;
		rowsCount = SparePartAutoservice.countGetAll(currEmployee.getAutoserviceID());
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}
	
	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (SparePartAutoservice spa : spisukNali4nosti) {
			if (nali4nost == spa) {
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
	
	public void selectRow(SparePartAutoservice spa) {
		nali4nost = spa;
	}
	
	public void deselectRow() {
		nali4nost = new SparePartAutoservice();
		readList();
	}
	
	public boolean isRowSelected() {
		return spisukNali4nosti.contains(nali4nost);
	}
	
	public boolean isChangingAllowed() {
		return allPages.getWriteRight(
				FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath(),
				currEmployee.getPosition());
	}
	
	public boolean isChoosingAllowed() {
		return (dataRequest != null);
	}
	
	public String chooseSparePart(SparePartAutoservice spa) {
		// �������� ������ ������ ���������
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		// ������� �������� ����� � ��������
		dataRequest.requestedObject = spa.getSparePart();
		// ������� ����� ����� ��� ������� � ������������ ��� ��� flash-�
		// ���������: ������� �� �������� ������ �� ����� � �����; ���� ��� �������� �������� �����
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		// ������� �� ���������� ����� � ��������� ��������
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
	public String writeIt()
	{	
		if (!isChangingAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return null;
		}

		nali4nost.writeToDB();
	
		readList();

		// set the message
		errorMessage = "����������� ���������� ���� ������������� �������!";
		
		return null;
	}
	
	public void makeItBad() {
		
		if (!isChangingAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return;
		}
		
		if (quantityBadRequest > nali4nost.getQuantityAvailable()) {
			errorMessage = "���� ��� �� �� ������� ������ ���������� �� ��������� � �������!";
			return;
		}
		
		nali4nost.setQuantityAvailable(nali4nost.getQuantityAvailable() - quantityBadRequest);
		nali4nost.setQuantityBad(nali4nost.getQuantityBad() + quantityBadRequest);
		
		nali4nost.writeToDB();
		
		quantityBadRequest = 0;
		//readList();

		// set the message
		errorMessage = "����������� ���� ��������� �������!";
	}
}
