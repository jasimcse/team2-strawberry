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
import model.Diagnosis;
import model.DiagnosisPart;
import model.DiagnosisService;
import model.Employee;
import model.InsurerRequest;
import model.Service;
import model.SparePart;
import model.SparePartAutoservice;
import model.Vehicle;
import model.VehicleModel;
import model.VehicleModelService;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;
import controller.users.helpers.ServiceForClientOrder;
import controller.users.helpers.SparePartForClientOrder;
import controller.users.helpers.SparePartForOrder;

@SuppressWarnings("serial")
@ManagedBean(name="dobavqneNaKlientskaPoru4ka")
@ViewScoped
public class DobavqneNaKlientskaPoru4ka implements Serializable {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private ClientOrder poru4ka = new ClientOrder();
	private String errorMessage;

	private List <ServiceForClientOrder> spisukUslugi = new ArrayList<ServiceForClientOrder>();
	private List <SparePartForClientOrder> spisukRezervni4asti = new ArrayList<SparePartForClientOrder>();
	private double clientOrderPrice = 0;
	

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
	
		if (dataRequestStack != null) 
		{
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
			{
				this.poru4ka = ((DobavqneNaKlientskaPoru4ka)dataRequest.requestObject).poru4ka;
				this.spisukUslugi = ((DobavqneNaKlientskaPoru4ka)dataRequest.requestObject).spisukUslugi;
				this.spisukRezervni4asti = ((DobavqneNaKlientskaPoru4ka)dataRequest.requestObject).spisukRezervni4asti;
				
				if(dataRequest.dataPage.equals("/users/AktualiziraneNaAvtomobil.jsf"))
				{
					this.poru4ka.setVehicle((Vehicle)dataRequest.requestedObject);
					this.poru4ka.setClient(((Vehicle)dataRequest.requestedObject).getClient());
				}
				else
				{
					if(dataRequest.dataPage.equals("/users/AktualiziraneNaDiagnostika.jsf"))
					{
						this.poru4ka.setVehicle(((Diagnosis)dataRequest.requestedObject).getVehicle());
						this.poru4ka.setClient(((Diagnosis)dataRequest.requestedObject).getVehicle().getClient());
						this.spisukUslugi.clear();
						this.spisukRezervni4asti.clear();
						
						List< DiagnosisService > spisukDiagService = new ArrayList<DiagnosisService>();
						spisukDiagService = DiagnosisService.queryGetAll(0, DiagnosisService.countGetAll(((Diagnosis)dataRequest.requestedObject).getID()), 
								((Diagnosis)dataRequest.requestedObject).getID());
						
						// ��������� �� ������� � ������ �� ��������� �����������
						for (DiagnosisService dSer : spisukDiagService) {
							List<InsurerRequest> listInsureReq = InsurerRequest.queryGetAll(0, 
									InsurerRequest.countGetAll(poru4ka.getVehicleID()), poru4ka.getVehicleID());
							if(listInsureReq.size() != 0)
							{
								for(InsurerRequest inReq: listInsureReq)
								{
									if(inReq.getDiagnosisID() == ((Diagnosis)dataRequest.requestedObject).getID())
									{
										addSeviceForClientOrder(dSer.getService(), ClientOrderService.INSURER_PAYS );
										break;
									}
								}
							}
							else
								addSeviceForClientOrder(dSer.getService(), ClientOrderService.CLIENT_PAYS );
							
						}
						
						// ��������� �� ������� � �������� ����� �� ��������� �����������
						List< DiagnosisPart > spisukDiagPart = new ArrayList<DiagnosisPart>();
						spisukDiagPart = DiagnosisPart.queryGetAll(0, DiagnosisPart.countGetAll(((Diagnosis)dataRequest.requestedObject).getID()), 
								((Diagnosis)dataRequest.requestedObject).getID());
						for (DiagnosisPart dSp : spisukDiagPart) {
							List<InsurerRequest> listInsureReq = InsurerRequest.queryGetAll(0, 
									InsurerRequest.countGetAll(poru4ka.getVehicleID()), poru4ka.getVehicleID());
							if(listInsureReq.size() != 0)
							{
								for(InsurerRequest inReq: listInsureReq)
								{
									if(inReq.getDiagnosisID() == ((Diagnosis)dataRequest.requestedObject).getID())
									{
										addSparePartForClientOrder(dSp.getSparePart(), ClientOrderService.INSURER_PAYS );
										break;
									}
								}
							}
							else
								addSparePartForClientOrder(dSp.getSparePart(), ClientOrderService.CLIENT_PAYS);
						}
					}
					else
					{
						if(dataRequest.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
						{
							addSeviceForClientOrder((Service)dataRequest.requestedObject, ClientOrderService.CLIENT_PAYS);
						}
						else
						{
							if(dataRequest.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf"))
							{
								addSparePartForClientOrder((SparePart)dataRequest.requestedObject, ClientOrderService.CLIENT_PAYS);
							}
						}
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
		this.spisukRezervni4asti.add(partClientOrder);
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

	public String getErrorMessage() {
		return errorMessage;
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

	public List< String > getStatusPoru4ka() 
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
		//listStatus.add("�����������");
		// ��������� � ������ �� ��������� ������ ������ � �� ������� ������ �����
		//listStatus.add("���������");
		// ������ ������� ������� ���������
		//listStatus.add("�������");
		// ������ ������� �� ������ �� ���������, �� �� ��� ���� ��������� ������ � ������� ���.�����
		//listStatus.add("���������");
		return listStatus;
	}

	public List< String > getWhoWillPay() 
	{
		List<String> listStatus =  new ArrayList<String>();
		listStatus.add("�������");
		listStatus.add("��������������");
		listStatus.add("�������������");
		return listStatus;
	}
	
	public List<ServiceForClientOrder> getSpisukUslugi() {
		return spisukUslugi;
	}
	
	public List<SparePartForClientOrder> getSpisukRezervni4asti() {
		return spisukRezervni4asti;
	}

	public String addClientOrder()
	{	
		//TODO:
		if (poru4ka.getVehicleID() == null) {
			// set the message
			errorMessage = "�� � ������ ���������!";
			return null;
		}
		
		if( spisukUslugi.isEmpty() && spisukRezervni4asti.isEmpty() )
		{
			// set the message
			errorMessage = "�������� ������ �/��� �������� ����� ����� �� ���������� �� ����������!";
			return null;
		}
		
//		5.	������ - ����������� �� ����� � ������� Client_Order. ���������� �� ������ 
//		� ��������� Client_Order_Part �/��� Client_Order_Service. ������������� �� �������� 
//		� Spare_Part_Available (������ ������� ���������� �� ����������� ���� � ������������ 
//		����� �� ���������� �� ���������. ��������: �� ������ �� ������ ����������� ���������� �� ������!) 
//		Spare_Part_Reserved � Spare_Part_Request (��� ���� ��������� ����������).

		poru4ka.setDate(new Date());
		poru4ka.setEmployeeID(currEmployee.getEmployeeID());
		poru4ka.setAutoserviceID(currEmployee.getAutoserviceID());
		// TODO: �������� ���� ������ ����� �� �������
		if(true)
			poru4ka.setStatus(ClientOrder.NEW);
		else
			poru4ka.setStatus(ClientOrder.HALTED);
		poru4ka.writeToDB();
	 
		// TODO: ����������
		for (ServiceForClientOrder clSer : spisukUslugi) {
			clSer.getClService().setClientOrderID(poru4ka.getID());
			clSer.getClService().writeToDB();
		}
		
		for (SparePartForClientOrder clSp : spisukRezervni4asti) {
			clSp.getClPart().setClientOrderID(poru4ka.getID());
			clSp.getClPart().writeToDB();
		}

		
		// TODO: �������� ���� ������������� � ����� ������������� 
		// ��� ���������� � �����������
		
		// clean the data
		poru4ka = new ClientOrder();
		spisukRezervni4asti.clear();
		spisukUslugi.clear();
		
		// set the message
		errorMessage = "��������� ���� ������� �������!";
		
		return null;
	}
	
	public String chooseAvtomobil()
	{
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/AktualiziraneNaAvtomobil.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
	}

	public String chooseDiagnostika()
	{
		Stack<InterPageDataRequest> dataRequestStack = new Stack<InterPageDataRequest>();
		InterPageDataRequest dataRequest = new InterPageDataRequest();
			
		dataRequest.requestObject = this;
		dataRequest.returnPage = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		dataRequest.dataPage = "/users/AktualiziraneNaDiagnostika.jsf";
		dataRequest.requestedObject = null;
			
		dataRequestStack.push(dataRequest);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dataRequestStack", dataRequestStack);
			
		return dataRequest.dataPage + "?faces-redirect=true";
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

	
//	public String whoWillPays(ClientOrderService clService) {
//		if(clService.getWhoPays().equals("��������������"))
//			return ClientOrderPart.INSURER_PAYS;
//		else
//			if(clService.getWhoPays().equals("�������������"))
//				return ClientOrderPart.PRODUCER_PAYS;
//			else
//				return ClientOrderPart.CLIENT_PAYS;
//	}
	public void toggleEditServiceForClientOrder(ServiceForClientOrder serForClO) {
		serForClO.toggleEditing();
		recalculateFullPrice();
	}
	
	public void deleteUsluga(ClientOrderService clService)
	{
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
	
//	
//	public String whoWillPays(ClientOrderPart sPart) {
//		if(sPart.getWhoPays().equals("��������������"))
//			return ClientOrderPart.INSURER_PAYS;
//		else
//			if(sPart.getWhoPays().equals("�������������"))
//				return ClientOrderPart.PRODUCER_PAYS;
//			else
//				return ClientOrderPart.CLIENT_PAYS;
//	}
	
	public void toggleEditSparePartForClientOrder(SparePartForClientOrder spForClO) {
		spForClO.toggleEditing();
		recalculateFullPrice();
	}

	private void recalculateFullPrice() {
		clientOrderPrice = 0;
		for (SparePartForClientOrder spForClO : spisukRezervni4asti) {
			clientOrderPrice += spForClO.getFullPrice();
		}
	}
	public void deleteSparePart(ClientOrderPart sPart)
	{
		spisukRezervni4asti.remove(sPart); 
	}
	
	
}
