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
import model.SparePartReserved;
import model.Vehicle;
import model.VehicleModelService;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;
import controller.users.helpers.ServiceForClientOrder;
import controller.users.helpers.SparePartForClientOrder;

@SuppressWarnings("serial")
@ManagedBean(name="aktualiziraneNaKlientskaPoru4ka")
@ViewScoped
public class AktualiziraneNaKlientskaPoru4ka implements Serializable {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private ClientOrder poru4ka = new ClientOrder();
	private String errorMessage;

	private List <ServiceForClientOrder> spisukUslugi = new ArrayList<ServiceForClientOrder>();
	private List <SparePartForClientOrder> spisukRezervni4asti = new ArrayList<SparePartForClientOrder>();
	private double clientOrderPrice = 0;
	private boolean inAutoservice = false;
	private boolean missingSpPart = false;
	

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
	
		if (dataRequestStack != null) 
		{
			InterPageDataRequest dataRequest = dataRequestStack.peek();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
			{
				this.poru4ka = ((AktualiziraneNaKlientskaPoru4ka)dataRequest.requestObject).poru4ka;
				this.spisukUslugi = ((AktualiziraneNaKlientskaPoru4ka)dataRequest.requestObject).spisukUslugi;
				this.spisukRezervni4asti = ((AktualiziraneNaKlientskaPoru4ka)dataRequest.requestObject).spisukRezervni4asti;
				
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
						
						// попълване на списъка с услуги от избраната диагностика
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
						
						// попълване на списъка с резервни части от избраната диагностика
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
						
						recalculateFullPrice();
					}
					else
					{
						if(dataRequest.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
						{
							addSeviceForClientOrder((Service)dataRequest.requestedObject, ClientOrderService.CLIENT_PAYS);
							recalculateFullPrice();
						}
						else
						{
							if(dataRequest.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf"))
							{
								addSparePartForClientOrder((SparePart)dataRequest.requestedObject, ClientOrderService.CLIENT_PAYS);
								recalculateFullPrice();
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
			errorMessage = "Грешно зададени модел автомобил и услуга!";
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
			errorMessage = "Грешно зададена резервна част!";
			return false;
		}
		
		partClientOrder.setQuantityAvailable(listAutosevicePart.get(0).getQuantityAvailable());
		partClientOrder.recalculateFullPrice();
		this.spisukRezervni4asti.add(partClientOrder);
		
		// проверка дали разполагаме с необходимото количество от резервната част
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

//	public List< String > getStatusPoru4ka() 
//	{
//		// 1 = задържана, 2 = изпълнявана, 3 = завършена, 4 = платена, 5 = блокирана.
//		List<String> listStatus =  new ArrayList<String>();
//		// при създаването си поръчката е нова
//		listStatus.add("нова");
//		// в статус задържана е ако се очаква пристигането на части 
//		// за извършване на ремонта или автомобила не е приет
//		listStatus.add("задържана");
//		// в статус изпълнявана е когато е се извършва поне една услуга 
//		// или е изписана поне една нужна за ремонта рез. част
//		//listStatus.add("изпълнявана");
//		// завършена е когато са изпълнени всички услуги и са вложени всички части
//		//listStatus.add("завършена");
//		// когато клиента заплати поръчката
//		//listStatus.add("платена");
//		// когато клиента се откаже от поръчката, но по нея няма извършени услуги и вложени рез.части
//		//listStatus.add("блокирана");
//		return listStatus;
//	}

	public String getWhoWillPay(String strWhoPay) 
	{
		if(strWhoPay.equals(ClientOrderPart.INSURER_PAYS))
			return "застрахователя";
		else
			if(strWhoPay.equals(ClientOrderPart.PRODUCER_PAYS))
				return "производителя";
			else
				return "клиента";
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
			errorMessage = "Не е избран автомобил!";
			return null;
		}
		
		if( spisukUslugi.isEmpty() && spisukRezervni4asti.isEmpty() )
		{
			// set the message
			errorMessage = "Изберете услуги и/или резервни части нужни за извършване по автомобила!";
			return null;
		}

		poru4ka.setDate(new Date());
		poru4ka.setEmployeeID(currEmployee.getEmployeeID());
		poru4ka.setAutoserviceID(currEmployee.getAutoserviceID());
		if( inAutoservice )
			poru4ka.setVehiclePresent(ClientOrder.VEHICLE_PRESENTS);
		else
			poru4ka.setVehiclePresent(ClientOrder.VEHICLE_NOT_PRESENTS);
	
						
		// TODO: проверка дали всички части са налични
		if( !missingSpPart && inAutoservice )
			poru4ka.setStatus(ClientOrder.NEW);
		else
			poru4ka.setStatus(ClientOrder.HALTED);
		
		poru4ka.writeToDB();
	 
		// TODO: транзакция
		for (ServiceForClientOrder clSer : spisukUslugi) {
			clSer.getClService().setClientOrderID(poru4ka.getID());
			//TODO: това трябва да е ID-то на автомонтжора извършил услугата
			clSer.getClService().setEmployeeID(currEmployee.getEmployeeID());
			clSer.getClService().writeToDB();
		}
		
		for (SparePartForClientOrder clSp : spisukRezervni4asti) {
			clSp.getClPart().setClientOrderID(poru4ka.getID());
			clSp.getClPart().writeToDB();
			
			List<SparePartAutoservice> listSpPartAuto = SparePartAutoservice.queryGetBySparePartID(
					clSp.getClPart().getSparePartID(), 0, 1, currEmployee.getAutoserviceID());
			
			if(listSpPartAuto.size() != 1)
			{
				errorMessage = "Грешно зададена резервна част!";
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
		errorMessage = "Поръчката беше добавен успешно!";
		
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

	
	public void deleteUsluga(ServiceForClientOrder clService)
	{
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
			// проверка дали разполагаме с необходимото количество от резервната част
			if ( spForClO.getQuantityAvailable() < spForClO.getClPart().getQuantity() )
					missingSpPart = true;
			
		}
	}
	
	
	public void deleteSparePart(SparePartForClientOrder sPart)
	{
		clientOrderPrice -= sPart.getFullPrice();
		spisukRezervni4asti.remove(sPart); 
		
	}
	
	
}

