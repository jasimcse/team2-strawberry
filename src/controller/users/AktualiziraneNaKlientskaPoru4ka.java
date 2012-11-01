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

import model.Autoservice;
import model.Client;
import model.ClientOrder;
import model.ClientOrderPart;
import model.ClientOrderService;
import model.Employee;
import model.Service;
import model.SparePart;
import model.SparePartAutoservice;
import model.SparePartReserved;
import model.Vehicle;
import model.VehicleModelService;
import controller.common.AllPages;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;
import controller.users.helpers.ServiceForClientOrder;
import controller.users.helpers.SparePartForClientOrder;


@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaKlientskaPoru4ka")
@ViewScoped
public class AktualiziraneNaKlientskaPoru4ka implements Serializable {
	
	@ManagedProperty(value="#{allPages}")
	private AllPages allPages;
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private ClientOrder poru4ka = new ClientOrder();
	private String errorMessage;

	private List <ClientOrder> spisukPoru4ki = new ArrayList<ClientOrder>();
	private List <ServiceForClientOrder> spisukUslugi = new ArrayList<ServiceForClientOrder>();
	private List <SparePartForClientOrder> spisukRezervni4asti = new ArrayList<SparePartForClientOrder>();
	
	private double clientOrderPrice = 0;
	private boolean inAutoservice = false;
	private boolean missingSpPart = false;
	private String searchStatus;
	private String searchAutoservice;

	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private InterPageDataRequest dataRequest;
	
	@PostConstruct
	public void init() {
		readList();
	}
	
	@SuppressWarnings("unchecked")
	//@PostConstruct
	public AktualiziraneNaKlientskaPoru4ka() {
		
		
		
		dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
		
		if (dataRequestStack != null) {
			dataRequest = dataRequestStack.peek();
			if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.dataPage)) {
				dataRequest = null;
			}
		}
		
		
		Stack<InterPageDataRequest> dataRequestStackNew = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
	
		if (dataRequestStackNew != null) 
		{
			InterPageDataRequest dataRequestNew = dataRequestStackNew.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequestNew.returnPage)) 
			{
				this.poru4ka = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).poru4ka;
				this.spisukUslugi = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).spisukUslugi;
				this.spisukRezervni4asti = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).spisukRezervni4asti;
				
				if(dataRequestNew.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
				{
					addSeviceForClientOrder((Service)dataRequestNew.requestedObject, ClientOrderService.CLIENT_PAYS);
					recalculateFullPrice();
				}
				else
				{
					if(dataRequestNew.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf"))
					{
						addSparePartForClientOrder((SparePart)dataRequestNew.requestedObject, ClientOrderService.CLIENT_PAYS);
						recalculateFullPrice();
					}
				}
			}
		}
	}

	public boolean addSeviceForClientOrder(Service service, String whoPay)
	{
		ClientOrderService clService = new ClientOrderService();
		clService.setService(service);
		clService.setPriceHour(service.getPriceHour());
		clService.setWhoPays(whoPay);
	
		ServiceForClientOrder serClientOrder = new ServiceForClientOrder();
		serClientOrder.setClService(clService);
		List<VehicleModelService> listModelService =  VehicleModelService.queryGetByService(service.getID(), 0, 1, poru4ka.getVehicle().getVehicleModelID());
		if(listModelService.size() != 1)
		{
			errorMessage = "������ �������� ����� ��������� � ������!";
			return false;
		}
		
		serClientOrder.setServiceDuration(listModelService.get(0).getDurationHour());
		serClientOrder.recalculateFullPrice();
		this.spisukUslugi.add(serClientOrder);
		
		return true;
		
	}
	
	public boolean addSparePartForClientOrder(SparePart spPart, String whoPay)
	{
		ClientOrderPart  clPart = new ClientOrderPart();
		clPart.setSparePart(spPart);								
		clPart.setQuantity(1);
		clPart.setPriceUnit(spPart.getSalePrice());
		clPart.setWhoPays(whoPay);
		
		SparePartForClientOrder partClientOrder = new SparePartForClientOrder();
		partClientOrder.setClPart(clPart);
		List <SparePartAutoservice> listAutosevicePart = SparePartAutoservice.queryGetBySparePartID(spPart.getID(), 0, 1, currEmployee.getAutoserviceID());
		if(listAutosevicePart.size() != 1)
		{
			errorMessage = "������ �������� �������� ����!";
			return false;
		}
		
		partClientOrder.setQuantityAvailable(listAutosevicePart.get(0).getQuantityAvailable());
		partClientOrder.recalculateFullPrice();
		this.spisukRezervni4asti.add(partClientOrder);
		
		// �������� ���� ����������� � ������������ ���������� �� ���������� ����
		if ( partClientOrder.getQuantityAvailable() < partClientOrder.getClPart().getQuantity() )
			missingSpPart = true;
		
		return true;
		
	}
	
	public Autoservice getAutoservice() {
		return poru4ka.getAutoservice();
	}

	public void setAutoservice(Autoservice autoservice) {
		poru4ka.setAutoservice(autoservice);
	}

	public Client getClient() {
		return poru4ka.getClient();
	}

	public void setClient(Client client) {
		poru4ka.setClient(client);
	}

	public Vehicle getVehicle() {
		return poru4ka.getVehicle();
	}

	public void setVehicle(Vehicle vehicle) {
		poru4ka.setVehicle(vehicle);
	}

	public Employee getEmployee() {
		return poru4ka.getEmployee();
	}

	public void setEmployee(Employee employee) {
		poru4ka.setEmployee(employee);
	}

	public String getVehiclePresent() {
		return poru4ka.getVehiclePresent();
	}

	public void setVehiclePresent(String vehiclePresent) {
		poru4ka.setVehiclePresent(vehiclePresent);
	}

	
	public Date getDate() {
		return poru4ka.getDate();
	}

	public void setDate(Date date) {
		poru4ka.setDate(date);
	}

	public String getStatus() {
		return poru4ka.getStatus();
	}

	public void setStatus(String status) {
		poru4ka.setStatus(status);
	}

	public String getPaymentNumber() {
		return poru4ka.getPaymentNumber();
	}

	public void setPaymentNumber(String paymentNumber) {
		poru4ka.setPaymentNumber(paymentNumber);
	}

	public boolean isInAutoservice() {
		return inAutoservice;
	}

	public void setInAutoservice(boolean inAutoservice) {
		this.inAutoservice = inAutoservice;
	}

	
	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	
	public String getSearchAutoservice() {
		return searchAutoservice;
	}

	public void setSearchAutoservice(String searchAutoservice) {
		this.searchAutoservice = searchAutoservice;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setAllPages(AllPages allPages) {
		this.allPages = allPages;
	}

	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}

	public double getClientOrderPrice() {
		return clientOrderPrice;
	}

	public void setClientOrderPrice(double clientOrderPrice) {
		this.clientOrderPrice = clientOrderPrice;
	}

	public String getWhoWillPay(String strWhoPay) 
	{
		if(strWhoPay.equals(ClientOrderPart.INSURER_PAYS))
			return "��������������";
		else
			if(strWhoPay.equals(ClientOrderPart.PRODUCER_PAYS))
				return "�������������";
			else
				return "�������";
	}
	
	public String getWhoDoService( Employee emp )
	{
		if(emp == null )
			return " ";
		
		return emp.getName() + " " + emp.getFamily();
	}
	
	public List<ServiceForClientOrder> getSpisukUslugi() {
		return spisukUslugi;
	}
	
	public List<SparePartForClientOrder> getSpisukRezervni4asti() {
		return spisukRezervni4asti;
	}

	public String chooseUsluga()
	{	
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/AktualiziraneNaUsluga.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
		
	}

	public void setEmployeeToService(ServiceForClientOrder clService)
	{
		// ������ ����������� �������� ��������
		clService.getClService().setEmployeeID(currEmployee.getEmployeeID());
		readList();
	}
	
	public void deleteUsluga(ServiceForClientOrder clService)
	{
		// TODO ��� �� � ���������!
		clientOrderPrice -= clService.getFullPrice();
		spisukUslugi.remove(clService);
	}
	
	public String chooseSparePart()
	{	
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
	
	public void toggleEditSparePartForClientOrder(SparePartForClientOrder spForClO) {
		
		// TODO: ���������� � ������ � ������ ���������� ����� ����� ������ ����� ���������� �������� �� ��������. 
		// �� ���� ����� ���������� �� ����� �� ����� �� ��������� ���������� �� �������.

		spForClO.toggleEditing();
		recalculateFullPrice();
	}

	private void recalculateFullPrice() {
		clientOrderPrice = 0;
		for (ServiceForClientOrder serForClO : spisukUslugi) {
			clientOrderPrice += serForClO.getFullPrice();
		}
		
		missingSpPart = false;
		for (SparePartForClientOrder spForClO : spisukRezervni4asti) {
			clientOrderPrice += spForClO.getFullPrice();
			// �������� ���� ����������� � ������������ ���������� �� ���������� ����
			if ( spForClO.getQuantityAvailable() < spForClO.getClPart().getQuantity() )
					missingSpPart = true;
			
		}
	}

	public void deleteSparePart(SparePartForClientOrder sPart)
	{
		// TODO: ��� ���������� �� ��� ���� � ������� �� �� ���� �� �� ������!
		
		clientOrderPrice -= sPart.getFullPrice();
		spisukRezervni4asti.remove(sPart); 	
	}
	
	public boolean isPaied()
	{
		if(poru4ka == null)
			return true;
		
		if( poru4ka.getStatus().equals(poru4ka.BLOCKED) || poru4ka.getStatus().equals(poru4ka.PAYED))
			return true;
		
		return false;
	}
	
	public List< String > getOrderStatus() 
	{
		
		// 1 = ���������, 2 = �����������, 3 = ���������, 4 = �������, 5 = ���������.
		List<String> listStatus =  new ArrayList<String>();
		// ��� ����������� �� ��������� � ����
		listStatus.add("����");
		// � ������ ��������� � ��� �� ������ ������������ �� ����� 
		// �� ���������� �� ������� ��� ���������� �� � �����
		listStatus.add("���������");
		// � ������ ����������� � ������ � �� �������� ���� ���� ������ 
		// ��� � �������� ���� ���� ����� �� ������� ���. ����
		listStatus.add("�����������");
		// ��������� � ������ �� ��������� ������ ������ � �� ������� ������ �����
		listStatus.add("���������");
		// ������ ������� ������� ���������
		listStatus.add("�������");
		// ������ ������� �� ������ �� ���������, �� �� ��� ���� ��������� ������ � ������� ���.�����
		listStatus.add("���������");
		return listStatus;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		readList();
	}

	public List<ClientOrder> getSpisukPoru4ki() {
		return spisukPoru4ki;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		//TODO: �� �����������
		spisukPoru4ki = ClientOrder.queryGetAll(page * ConfigurationProperties.getPageSize(), 
				ConfigurationProperties.getPageSize(), currEmployee.getAutoserviceID());
		poru4ka = new ClientOrder();
		rowsCount = ClientOrder.countGetAll(currEmployee.getAutoserviceID());
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
	}

	public String getRowStyleClasses() {
		StringBuilder strbuff = new StringBuilder();
		
		for (ClientOrder order : spisukPoru4ki) {
			if (poru4ka == order) {
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
	
	public void selectRow(ClientOrder order) {
		poru4ka = order;
		
		List <ClientOrderService> spisukClOrderService = ClientOrderService.queryGetAll(0, 
			ClientOrderService.countGetAll(poru4ka.getID()), poru4ka.getID());

		for (ClientOrderService clOrSer : spisukClOrderService) 
		{
			ServiceForClientOrder serClientOrder = new ServiceForClientOrder();
			serClientOrder.setClService(clOrSer);
			List<VehicleModelService> listModelService =  VehicleModelService.queryGetByService(clOrSer.getServiceID(), 
					0, 1, poru4ka.getVehicle().getVehicleModelID());
		
			serClientOrder.setServiceDuration(listModelService.get(0).getDurationHour());
			serClientOrder.recalculateFullPrice();
			spisukUslugi.add(serClientOrder);
		}
		
		
		List <ClientOrderPart> spisukClOrderPart = ClientOrderPart.queryGetAll(0, 
			ClientOrderPart.countGetAll(poru4ka.getID()), poru4ka.getID());

		for (ClientOrderPart clOrSer : spisukClOrderPart) 
		{
			SparePartForClientOrder partClientOrder = new SparePartForClientOrder();
			partClientOrder.setClPart(clOrSer);
			List <SparePartAutoservice> listAutosevicePart = SparePartAutoservice.queryGetBySparePartID(clOrSer.getSparePartID(), 0, 1, currEmployee.getAutoserviceID());
			
			partClientOrder.setQuantityAvailable(listAutosevicePart.get(0).getQuantityAvailable());
			partClientOrder.recalculateFullPrice();
			
			//TODO:
			//����������� ���������� �� ��������� Read only - Spare_Part_Reserved.Quantity
			//������� ���������� �� ��������� Read only - Spare_Part_Reserved.Used
			
			spisukRezervni4asti.add(partClientOrder);
			
			// �������� ���� ����������� � ������������ ���������� �� ���������� ����
			if ( partClientOrder.getQuantityAvailable() < partClientOrder.getClPart().getQuantity() )
				missingSpPart = true;
		}
		
		
	}
	
	public void deselectRow() {
		poru4ka = new ClientOrder();
		readList();
	}

	public boolean isRowSelected() {
		return spisukPoru4ki.contains(poru4ka);
	}
	
	public String goToAdd() {
		return "DobavqneNaKlientskaPoru4ka.jsf?faces-redirect=true";
	}
	
	public boolean isGoToAddAllowed() {
		return allPages.getReadRight(
					"/users/DobavqneNaKlientskaPoru4ka.jsf",
					currEmployee.getPosition());
	}
	
	public boolean isChangingAllowed() {
		return allPages.getWriteRight(
				FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath(),
				currEmployee.getPosition());
	}
	
	public boolean isChoosingAllowed() {
		return (dataRequest != null);
	}
	
	public String choosePoru4ka(ClientOrder order) {
		if (dataRequest == null) {
			throw new RuntimeException("Don't do that bastard!");
		}
		
		dataRequest.requestedObject = order;
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
		
		return dataRequest.returnPage + "?faces-redirect=true";
	}
	
	public String saveOrder()
	{	
		//	������ � ������������� �� ����� � ������� Client_Order. 
		// ������������� �� ������ � ��������� Client_Order_Part �/��� Client_Order_Service.
		// ������������� �� �������� � Spare_Part_Available (������ ������� ���������� �� ����������� ���� � 
		// ������������ ����� �� ���������� �� ���������. ��������: �� ������ �� ������ ����������� ���������� �� ������!) 
		// Spare_Part_Reserved � Spare_Part_Request (��� ���� ��������� ����������).

		if (!isChangingAllowed()) {
			errorMessage = "������ ����� �� ��������������� �� �������!";
			return null;
		}

		if( spisukUslugi.isEmpty() && spisukRezervni4asti.isEmpty() )
		{
			// set the message
			errorMessage = "�������� ������ �/��� �������� ����� ����� �� ���������� �� ����������!";
			return null;
		}

		if( inAutoservice )
			poru4ka.setVehiclePresent(ClientOrder.VEHICLE_PRESENTS);
		else
			poru4ka.setVehiclePresent(ClientOrder.VEHICLE_NOT_PRESENTS);
			
		// TODO �������?					
//		if( !missingSpPart && inAutoservice )
//					poru4ka.setStatus(ClientOrder.NEW);
//				else
//					poru4ka.setStatus(ClientOrder.HALTED);
				
		poru4ka.writeToDB();
			 
		// TODO: ����������
		for (ServiceForClientOrder clSer : spisukUslugi) {
			clSer.getClService().writeToDB();
		}
		
		//TODO:
		for (SparePartForClientOrder clSp : spisukRezervni4asti) {
			clSp.getClPart().writeToDB();
					
			List<SparePartAutoservice> listSpPartAuto = SparePartAutoservice.queryGetBySparePartID(
					clSp.getClPart().getSparePartID(), 0, 1, currEmployee.getAutoserviceID());
					
			if(listSpPartAuto.size() != 1)
			{
				errorMessage = "������ �������� �������� ����!";
				return null;
			}
					
			if ( clSp.getQuantityAvailable() >= clSp.getClPart().getQuantity() )
			{
				listSpPartAuto.get(0).setQuantityReserved(listSpPartAuto.get(0).getQuantityReserved() + 
						clSp.getClPart().getQuantity());
				listSpPartAuto.get(0).setQuantityAvailable(listSpPartAuto.get(0).getQuantityAvailable() - 
						clSp.getClPart().getQuantity());
			}
			else
			{
				listSpPartAuto.get(0).setQuantityRequested(listSpPartAuto.get(0).getQuantityRequested() + 
						clSp.getClPart().getQuantity() - listSpPartAuto.get(0).getQuantityAvailable());
						
				listSpPartAuto.get(0).setQuantityReserved(listSpPartAuto.get(0).getQuantityReserved() + 
						listSpPartAuto.get(0).getQuantityAvailable());
						
				listSpPartAuto.get(0).setQuantityAvailable(0);
			}
					
			listSpPartAuto.get(0).writeToDB();
					
			SparePartReserved spPartReserved = new SparePartReserved();
			spPartReserved.setClientOrderID(poru4ka.getID());
			spPartReserved.setSparePartID(clSp.getClPart().getID());
			spPartReserved.setQuantity(clSp.getClPart().getQuantity());
			spPartReserved.setUsed(0);
			spPartReserved.writeToDB();
				
		}
				
		// clean the data
		poru4ka = new ClientOrder();
		spisukRezervni4asti.clear();
		spisukUslugi.clear();
		missingSpPart = false;
		clientOrderPrice = 0;
		setInAutoservice(false);
				
		// set the message
		errorMessage = "��������� ���� ������������� �������!";
					
		readList();

		return null;
	}
	
}

