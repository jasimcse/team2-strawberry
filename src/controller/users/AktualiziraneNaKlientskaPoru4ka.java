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
import model.EmployeeAutoservice;
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
	private boolean flagReadIt = true;
	
	private Stack<InterPageDataRequest> dataRequestStack;
	
	private int page = 0;
	private int pagesCount;
	private int rowsCount;
	
	private InterPageDataRequest dataRequest;
	
	@PostConstruct
	public void init() {
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
				if(poru4ka.getVehiclePresent().equals(ClientOrder.VEHICLE_PRESENTS))
				{
					setInAutoservice(true);
				}
				
				this.spisukPoru4ki = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).spisukPoru4ki;
				this.page = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).page;
				this.pagesCount = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).pagesCount;
				this.rowsCount = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).rowsCount;
				this.clientOrderPrice = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).clientOrderPrice;
				this.missingSpPart = ((AktualiziraneNaKlientskaPoru4ka)dataRequestNew.requestObject).missingSpPart;
				
				if(dataRequestNew.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
				{
					addSeviceForClientOrder((Service)dataRequestNew.requestedObject, ClientOrderService.CLIENT_PAYS);
					recalculateFullPrice();
					flagReadIt = false;
				}
				else
				{
					if(dataRequestNew.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf"))
					{
						addSparePartForClientOrder((SparePart)dataRequestNew.requestedObject, ClientOrderService.CLIENT_PAYS);
						recalculateFullPrice();
						flagReadIt = false;
					}
				}
			}
		}
		
		readList();
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
		
		checkMissingSpPart(partClientOrder);

		
		return true;
		
	}
	
	private void checkMissingSpPart(SparePartForClientOrder partClOrder)
	{
		// проверка дали разполагаме с необходимото количество от резервната част
		if ( partClOrder.getQuantityAvailable() < partClOrder.getClPart().getQuantity() )
			missingSpPart = true;
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

	public Date getDate() {
		return poru4ka.getDate();
	}

	public void setDate(Date date) {
		poru4ka.setDate(date);
	}

	public String getStatus() {
		if(poru4ka.getStatus() == null)
			return null;
		
		if(poru4ka.getStatus().equals(ClientOrder.NEW)  )
			return "нова";
		
		if(poru4ka.getStatus().equals(ClientOrder.HALTED) )
			return "задържана";
	
		if(poru4ka.getStatus().equals(ClientOrder.PROCESSING) )
			return "изпълнявана";

		if(poru4ka.getStatus().equals(ClientOrder.FINISHED) )
			return "завършена";
		
		if(poru4ka.getStatus().equals(ClientOrder.PAYED) )
			return "платена";
		
		if(poru4ka.getStatus().equals(ClientOrder.BLOCKED) )
			return "блокирана";
		
		return null;
	}

	public void setStatus(String status) {
		
		if(status.equals("нова") )
			poru4ka.setStatus(ClientOrder.NEW);
		else
			if(status.equals("задържана") )
				poru4ka.setStatus(ClientOrder.HALTED);
			else
				if(status.equals("изпълнявана") )
					poru4ka.setStatus(ClientOrder.PROCESSING);
				else
					if(status.equals("платена") )
						poru4ka.setStatus(ClientOrder.FINISHED);
					else
						if(status.equals("платена") )
							poru4ka.setStatus(ClientOrder.PAYED);
						else
							if(status.equals("блокирана") )
								poru4ka.setStatus(ClientOrder.BLOCKED);
	}
	public void finishedOrder( boolean bDone)
	{
		if( bDone )
			poru4ka.setStatus(ClientOrder.PAYED);
		else
			poru4ka.setStatus(ClientOrder.BLOCKED);
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
			return "застрахователя";
		else
			if(strWhoPay.equals(ClientOrderPart.PRODUCER_PAYS))
				return "производителя";
			else
				return "клиента";
	}
	
	public String getWhoDoService( Employee emp )
	{
		if(emp == null )
			return " ";
		
		return emp.getName() + " " + emp.getFamily();
	}
	
	public List<String> getAutoservices() {
		List<String> spisukAvtoservizi = new ArrayList<String>();
		List<Autoservice> spisukAuto = Autoservice.queryGetAll(0, 1000);
		for (Autoservice auto : spisukAuto) {
			spisukAvtoservizi.add(auto.getName());
		}
		return spisukAvtoservizi;
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
		if( currEmployee.getPosition().equals(EmployeeAutoservice.AUTO_MECHANIC) )
		{
			// добавя автомонтьор извършил услугата
			clService.getClService().setEmployeeID(currEmployee.getEmployeeID());
		}
	}
	
	public boolean isAvtomontior()
	{
		if( currEmployee.getPosition().equals(EmployeeAutoservice.AUTO_MECHANIC) )
			return true;
		
		 return false;
	}
	
	public boolean isSklad()
	{
		if( currEmployee.getPosition().equals(EmployeeAutoservice.WAREHOUSEMAN) )
			return true;
		
		 return false;
	}
	
	public boolean isPriemchik()
	{
		if( currEmployee.getPosition().equals(EmployeeAutoservice.CASHIER) )
			return true;
		
		 return false;
	}
	
 	public void deleteUsluga(ServiceForClientOrder clService)
	{
		// // TODO: триене от БД
 		// ако не е извършена!
		if(clService.getClService().getEmployee() != null)
			return;
		
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
		
		// TODO: Редактирай – колона с бутони редактирай която прави полето нужно количество възможно за редакция. 
		// Не може нужно количество да стане по малко от вложеното количество до момента.

		if(spForClO.getClPart().getQuantity() < spForClO.getQuantityUsed())
		{
			errorMessage = "Нужното количество от резервната чaст трябва да е по-голямо или равно на " + spForClO.getQuantityUsed();
			return;
		}
		
		if(spForClO.getQuantityUsed() > spForClO.getClPart().getQuantity())
		{
			errorMessage = "Нужното количество от резервната чст трябва да е по-голямо или равно на " + spForClO.getQuantityUsed();
			return;
		}
		
		spForClO.toggleEditing();
		checkMissingSpPart(spForClO);
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
		//ако количество от рез част е вложено тя не може да се изтрие!
		// TODO: триене от БД
		clientOrderPrice -= sPart.getFullPrice();
		spisukRezervni4asti.remove(sPart); 	
	}
	
	public boolean isPaied()
	{
		if(poru4ka.getStatus() == null)
			return true;
		
		if( poru4ka.getStatus().equals(ClientOrder.BLOCKED) || poru4ka.getStatus().equals(ClientOrder.PAYED))
			return true;
		
		return false;
	}
	
	public List< String > getOrderStatus() 
	{
		
		//1 = нова, 2 = задържана, 3 = изпълнявана, 4 = завършена, 5 = платена, 6 = блокирана.
		List<String> listStatus =  new ArrayList<String>();
		// при създаването си поръчката е нова
		listStatus.add("нова");
		// в статус задържана е ако се очаква пристигането на части 
		// за извършване на ремонта или автомобила не е приет
		listStatus.add("задържана");
		// в статус изпълнявана е когато е се извършва поне една услуга 
		// или е изписана поне една нужна за ремонта рез. част
		listStatus.add("изпълнявана");
		// завършена е когато са изпълнени всички услуги и са вложени всички части
		listStatus.add("завършена");
		// когато клиента заплати поръчката
		listStatus.add("платена");
		// когато клиента се откаже от поръчката, но по нея няма извършени услуги и вложени рез.части
		listStatus.add("блокирана");
		return listStatus;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		flagReadIt = true;
		readList();
	}

	public List<ClientOrder> getSpisukPoru4ki() {
		return spisukPoru4ki;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	private void readList() {
		if (flagReadIt) {
		//TODO: За автосервиза
		spisukPoru4ki = ClientOrder.queryGetAll(page * ConfigurationProperties.getPageSize(), 
				ConfigurationProperties.getPageSize(), currEmployee.getAutoserviceID());
		poru4ka = new ClientOrder();
		rowsCount = ClientOrder.countGetAll(currEmployee.getAutoserviceID());
		pagesCount = rowsCount / ConfigurationProperties.getPageSize() +
				(rowsCount % ConfigurationProperties.getPageSize() > 0 ? 1 : 0);
		}
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
		cleanData();
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
			
	
			List <SparePartReserved> listSpPartRes = SparePartReserved.queryGetBySparePart(clOrSer.getSparePartID(), 0, 1, poru4ka.getID());
		
			if(listSpPartRes.size() == 1)
			{
				//Резервирано количество за поръчката 
				partClientOrder.setQuantityReserved(listSpPartRes.get(0).getQuantity());
				//Вложено количество за поръчката
				partClientOrder.setQuantityUsed(listSpPartRes.get(0).getUsed());
			}
			
			spisukRezervni4asti.add(partClientOrder);
			
			// проверка дали разполагаме с необходимото количество от резервната част
			if ( partClientOrder.getQuantityAvailable() < partClientOrder.getClPart().getQuantity() )
				missingSpPart = true;
		}
		
		recalculateFullPrice();
		
	}
	
	public void deselectRow() {
		cleanData();
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
		//	Запази – актуализиране на запис в таблица Client_Order. 
		// Актуализиране на записи в таблиците Client_Order_Part и/или Client_Order_Service.
		// Актуализиране на записите в Spare_Part_Available (намали нужното количество от съответната част с 
		// количеството нужно за изпълнение на поръчката. Внимание: Не трябва да остава отрицателно количество от частта!) 
		// Spare_Part_Reserved и Spare_Part_Request (ако няма наличното количество).

		if (!isChangingAllowed()) {
			errorMessage = "Нямате права за актуализирането на данните!";
			return null;
		}

		if( spisukUslugi.isEmpty() && spisukRezervni4asti.isEmpty() )
		{
			// set the message
			errorMessage = "Изберете услуги и/или резервни части нужни за извършване по автомобила!";
			return null;
		}

		if( inAutoservice )
			poru4ka.setVehiclePresent(ClientOrder.VEHICLE_PRESENTS);
		else
			poru4ka.setVehiclePresent(ClientOrder.VEHICLE_NOT_PRESENTS);
			
		int doneService = isServiceDone();
		int usedPart = isUsedSparePart();
		
		if(poru4ka.getStatus() != ClientOrder.BLOCKED && poru4ka.getStatus() != ClientOrder.PAYED)
		{

			if( doneService == spisukUslugi.size() && usedPart == spisukRezervni4asti.size() )
				poru4ka.setStatus(ClientOrder.FINISHED);
			else
			{
				if( !missingSpPart && inAutoservice &&  doneService == 0 && usedPart == 0)
					poru4ka.setStatus(ClientOrder.NEW);
				else
				{
					if(!missingSpPart && inAutoservice &&  doneService > 0 || usedPart > 0)
						poru4ka.setStatus(ClientOrder.PROCESSING);
					else
						poru4ka.setStatus(ClientOrder.HALTED);
				
				}
			}
		}
			
		poru4ka.writeToDB();
			 
		// TODO: транзакция
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
				errorMessage = "Грешно зададена резервна част!";
				return null;
			}
					
//			if ( clSp.getQuantityAvailable() >= clSp.getClPart().getQuantity() )
//			{
//				listSpPartAuto.get(0).setQuantityReserved(listSpPartAuto.get(0).getQuantityReserved() + 
//						clSp.getClPart().getQuantity());
//				listSpPartAuto.get(0).setQuantityAvailable(listSpPartAuto.get(0).getQuantityAvailable() - 
//						clSp.getClPart().getQuantity());
//			}
//			else
//			{
//				listSpPartAuto.get(0).setQuantityRequested(listSpPartAuto.get(0).getQuantityRequested() + 
//						clSp.getClPart().getQuantity() - listSpPartAuto.get(0).getQuantityAvailable());
//						
//				listSpPartAuto.get(0).setQuantityReserved(listSpPartAuto.get(0).getQuantityReserved() + 
//						listSpPartAuto.get(0).getQuantityAvailable());
//						
//				listSpPartAuto.get(0).setQuantityAvailable(0);
//			}
//					
//			listSpPartAuto.get(0).writeToDB();
//					
//			SparePartReserved spPartReserved = new SparePartReserved();
//			spPartReserved.setClientOrderID(poru4ka.getID());
//			spPartReserved.setSparePartID(clSp.getClPart().getID());
//			spPartReserved.setQuantity(clSp.getClPart().getQuantity());
//			spPartReserved.setUsed(0);
//			spPartReserved.writeToDB();
				
		}
				
		// clean the data
		cleanData();
				
		// set the message
		errorMessage += "Поръчката беше актуализирана успешно!";
			
		flagReadIt = true;
		readList();

		return null;
	}
	
	private int isServiceDone()
	{
		// брой извършени услуги
		int iServiceDone = 0; 
		for (ServiceForClientOrder clSer : spisukUslugi) {
			if( clSer.isDone())
				iServiceDone++;
		}
		
		return iServiceDone;
	}
	
	private int isUsedSparePart()
	{
		// брой резервни части чието количество е напълно вложено в поръчката
		int iUsedPart = 0; 
		for (SparePartForClientOrder clSp : spisukRezervni4asti) {
			if( clSp.getClPart().getQuantity() == clSp.getQuantityUsed())
				iUsedPart++;
		}
		return iUsedPart;
		
	}
	
	private void cleanData(){
		
		poru4ka = new ClientOrder();
		spisukRezervni4asti.clear();
		spisukUslugi.clear();
		missingSpPart = false;
		clientOrderPrice = 0;
		setInAutoservice(false);
		flagReadIt = true;
	}
	
}

