package controller.serverCommunication.SOAPServer;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;

import controller.common.ConfigurationProperties;

import model.Client;
import model.Company;
import model.Person;
import model.Service;
import model.SparePart;
import model.SparePartGroup;
import model.Vehicle;
import model.VehicleModel;
import model.VehicleModelService;
import model.VehicleModelSparePart;
import model.WarrantyConditions;

/**
 * 
 * дефинира заявките които могат да се викат от електронния магазин
 *
 */
@WebService(endpointInterface="controller.serverCommunication.SOAPServer.ElectronicShopInterface")
public class ElectronicShop implements ElectronicShopInterface {
	
	static final private Logger logger = Logger.getLogger(ElectronicShop.class.getName());

	@Override
	public boolean createOrUpdateWarrantyConditions(
			String electronicShopID,
			String warrantyConditionsID,
			long months,
			long milage,
			String otherConditions) {
		
		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}
		
		List<WarrantyConditions> wcl = WarrantyConditions.queryGetByForeignID(warrantyConditionsID, 0, 1);
		WarrantyConditions wc;
		if (wcl.size() == 0) {
			wc = new WarrantyConditions();
		} else {
			wc = wcl.get(0);
		}
		try {
			wc.setForeignID(warrantyConditionsID);
			wc.setMonths(months);
			wc.setMileage(milage);
			wc.setOtherConditions(otherConditions);
			wc.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean createOrUpdateVehicleModel(
			String electronicShopID,
			String vehicleModelID,
			String brand,
			String model,
			String characteristics) {
		
		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}
		
		List<VehicleModel> vml = VehicleModel.queryGetByForeignID(vehicleModelID, 0, 1);
		VehicleModel vm;
		if (vml.size() == 0) {
			vm = new VehicleModel();
		} else {
			vm = vml.get(0);
		}
		try {
			vm.setForeignID(vehicleModelID);
			vm.setBrand(brand);
			vm.setModel(model);
			vm.setCharacteristics(characteristics);
			vm.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean createOrUpdateVehicleModelPart(
			String electronicShopID,
			String vehicleModelID,
			String sparePartID) {
		
		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}
		
		// NOTE: напрактика само може да се добавя
		VehicleModelSparePart vmsp = new VehicleModelSparePart();
		try {
			List<VehicleModel> vehicleModel = VehicleModel.queryGetByForeignID(vehicleModelID, 0, 1);
			if (vehicleModel.size() == 0) {
				logger.warning("vehicleModelID \"" + vehicleModelID + "\" not known!");
				return false;
			}
			
			List<SparePart> sparePart = SparePart.queryGetByForeignID(sparePartID, 0, 1);
			if (sparePart.size() == 0) {
				logger.warning("sparePartID \"" + sparePartID + "\" not known!");
				return false;
			}
			
			vmsp.setVehicleModelID(vehicleModel.get(0).getID());
			vmsp.setSparePartID(sparePart.get(0).getID());
			vmsp.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean createOrUpdateVehicleModelService(
			String electronicShopID,
			String vehicleModelID,
			String serviceID,
			double durationHour,
			long monthsToNext,
			long milageToNext) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}

		VehicleModelService vms = new VehicleModelService();
		try {
			List<VehicleModel> vehicleModel = VehicleModel.queryGetByForeignID(vehicleModelID, 0, 1);
			if (vehicleModel.size() == 0) {
				logger.warning("vehicleModelID \"" + vehicleModelID + "\" not known!");
				return false;
			}
			
			List<Service> service = Service.queryGetByForeignID(serviceID, 0, 1);
			if (service.size() == 0) {
				logger.warning("serviceID \"" + serviceID + "\" not known!");
				return false;
			}

			List<VehicleModelService> vmsl = VehicleModelService.queryGetByService(service.get(0).getID(), 0, 1, vehicleModel.get(0).getID());
			if (vmsl.size() == 1) {
				vms = vmsl.get(0);
			}
			
			vms.setVehicleModelID(vehicleModel.get(0).getID());
			vms.setServiceID(service.get(0).getID());
			vms.setDurationHour(durationHour);
			vms.setMonthsToNext(monthsToNext);
			vms.setMilageToNext(milageToNext);
			vms.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

	@Override
	public boolean createOrUpdateSparePart(
			String electronicShopID,
			String sparePartID,
			String name,
			String sparePartGroupID,
			String description,
			double deliveryPrice,
			String measuringUnit) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}

		List<SparePart> spl = SparePart.queryGetByForeignID(sparePartID, 0, 1);
		SparePart sp;
		if (spl.size() == 0) {
			sp = new SparePart();
		} else {
			sp = spl.get(0);
		}
		try {
			List<SparePartGroup> sparePartGroup = SparePartGroup.queryGetByForeignID(sparePartGroupID, 0, 1);
			if (sparePartGroup.size() == 0) {
				logger.warning("sparePartGroupID \"" + sparePartGroupID + "\" not known!");
				return false;
			}
			
			sp.setForeignID(sparePartID);
			sp.setSparePartGroupID(sparePartGroup.get(0).getID());
			sp.setName(name);
			sp.setDescription(description);
			sp.setDeliveryPrice(deliveryPrice);
			sp.setSalePrice(deliveryPrice);    // <--- отначало продажната цена е същата като доставната !!!
			sp.setMeasuringUnit(measuringUnit);
			sp.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

	@Override
	public boolean createOrUpdateService(
			String electronicShopID,
			String serviceID,
			String description) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}

		List<Service> sl = Service.queryGetByForeignID(serviceID, 0, 1);
		Service s;
		if (sl.size() == 0){
			s = new Service();
		} else {
			s = sl.get(0);
		}
		try {
			s.setForeignID(serviceID);
			s.setDescription(description);
			s.setPriceHour(0);             // <--- отначало цената е 0 което означава, че не се извършва засега !!!
			s.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

	@Override
	public boolean createOrUpdateSparePartGroup(
			String electronicShopID,
			String sparePartGroupID,
			String description) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}

		List<SparePartGroup> spgl = SparePartGroup.queryGetByForeignID(sparePartGroupID, 0, 1);
		SparePartGroup spg;
		if (spgl.size() == 0) {
			spg = new SparePartGroup();
		} else {
			spg = spgl.get(0);
		}
		try {
			spg.setForeignID(sparePartGroupID);
			spg.setDescription(description);
			spg.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

	@Override
	public boolean createOrUpdateVehicle(
			String electronicShopID,
			String VIN,
			String clientID,
			String vehicleModelID,
			String warrantyConditionsID,
			String engineNumber,
			String plateNumber,
			Date purchaseDate) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}

		Vehicle v = new Vehicle();
		try {
			List<Client> client = Client.queryGetByForeignID(clientID, 0, 1);
			if (client.size() == 0) {
				logger.warning("clientID \"" + clientID + "\" not known!");
				return false;
			}
			
			List<Vehicle> vl = Vehicle.queryGetByVIN(VIN, 0, 1, client.get(0).getID());
			if (vl.size() == 1) {
				v = vl.get(0);
			}
			
			List<VehicleModel> vehicleModel = VehicleModel.queryGetByForeignID(vehicleModelID, 0, 1);
			if (vehicleModel.size() == 0) {
				logger.warning("vehicleModelID \"" + vehicleModelID + "\" not known!");
				return false;
			}
			
			List<WarrantyConditions> warrantyConditions = WarrantyConditions.queryGetByForeignID(warrantyConditionsID, 0, 1);
			if (warrantyConditions.size() == 0) {
				logger.warning("warrantyConditionsID \"" + warrantyConditionsID + "\" not known!");
				return false;
			}
			
			v.setVIN(VIN);
			v.setClientID(client.get(0).getID());
			v.setVehicleModelID(vehicleModel.get(0).getID());
			v.setWarrantyConditionsID(warrantyConditions.get(0).getID());
			v.setEngineNumber(engineNumber);
			v.setPlateNumber(plateNumber);
			v.setPurchaseDate(purchaseDate);
			v.setWarrantyOK(Vehicle.WARRANTY_YES);
			v.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

	@Override
	public boolean createOrUpdatePerson(
			String electronicShopID,
			String clientID,
			String name,
			String family,
			String addressCity,
			String addressLine,
			String phoneNumber,
			String mail,
			String IBANNumber,
			String SWIFTCode) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}
		
		List<Client> cl = Client.queryGetByForeignID(clientID, 0, 1);
		List<Person> pl = null;
		Client c;
		if (cl.size() == 0) {
			c = new Client();
		} else {
			c = cl.get(0);
			pl = Person.queryGetAll(0, 1, c.getID());
		}
		
		Person p;
		if ((pl == null) || (pl.size() == 0)) {
			p = new Person();
		} else {
			p = pl.get(0);
		}
		try {
			c.setForeignID(clientID);
			c.setAddressCity(addressCity);
			c.setAddressLine(addressLine);
			c.setMail(mail);
			c.setPhoneNumber(phoneNumber);
			c.setIBANNumber(IBANNumber);
			c.setSWIFTCode(SWIFTCode);
			
			p.setName(name);
			p.setFamily(family);
			c.setPerson(p);
			c.writeToDB();
			
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

	@Override
	public boolean createOrUpdateCompany(
			String electronicShopID,
			String clientID,
			String name,
			String contactPerson,
			String addressCity,
			String addressLine,
			String phoneNumber,
			String mail,
			String IBANNumber,
			String SWIFTCode,
			String registrationNumber,
			String VATNumber) {

		// check if we know that shop
		if (!ConfigurationProperties.getElectronicShopID().equals(electronicShopID)) {
			logger.warning("electronicShopID \"" + electronicShopID + "\" not known!");
			return false;
		}

		List<Client> cl = Client.queryGetByForeignID(clientID, 0, 1);
		List<Company> cmpnl = null;
		Client c;
		if (cl.size() == 0) {
			c = new Client();
		} else {
			c = cl.get(0);
			cmpnl = Company.queryGetAll(0, 1, c.getID());
		}
		
		
		Company cmpn;
		if ((cmpnl == null) || (cmpnl.size() == 0)) {
			cmpn = new Company();
		} else {
			cmpn = cmpnl.get(0);
		}
		try {
			c.setForeignID(clientID);
			c.setAddressCity(addressCity);
			c.setAddressLine(addressLine);
			c.setMail(mail);
			c.setPhoneNumber(phoneNumber);
			c.setIBANNumber(IBANNumber);
			c.setSWIFTCode(SWIFTCode);

			cmpn.setName(name);
			cmpn.setContactPerson(contactPerson);
			cmpn.setRegistrationNumber(registrationNumber);
			if (!"".equals(VATNumber)) {
				cmpn.setVATNumber(VATNumber);
			}
			c.setCompany(cmpn);
			c.writeToDB();
			
			
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}
}
