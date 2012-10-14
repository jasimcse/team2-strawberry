package controller.serverCommunication.SOAPServer;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService
public interface ElectronicShopInterface {
	
	@WebMethod
	public boolean createOrUpdateWarrantyConditions(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="warrantyConditionsID")
			String warrantyConditionsID,
			@WebParam(name="months")
			long months,
			@WebParam(name="milage")
			long milage,
			@WebParam(name="otherConditions")
			String otherConditions);
	
	@WebMethod
	public boolean createOrUpdateVehicleModel(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="vehicleModelID")
			String vehicleModelID,
			@WebParam(name="brand")
			String brand,
			@WebParam(name="model")
			String model,
			@WebParam(name="characteristics")
			String characteristics);
	
	@WebMethod
	public boolean createOrUpdateVehicleModelPart(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="vehicleModelID")
			String vehicleModelID,
			@WebParam(name="sparePartID")
			String sparePartID);
	
	@WebMethod
	public boolean createOrUpdateVehicleModelService(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="vehicleModelID")
			String vehicleModelID,
			@WebParam(name="serviceID")
			String serviceID,
			@WebParam(name="durationHour")
			double durationHour,
			@WebParam(name="monthsToNext")
			long monthsToNext,
			@WebParam(name="milageToNext")
			long milageToNext);
	
	@WebMethod
	public boolean createOrUpdateSparePart(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="sparePartID")
			String sparePartID,
			@WebParam(name="name")
			String name,
			@WebParam(name="sparePartGroupID")
			String sparePartGroupID,
			@WebParam(name="description")
			String description,
			@WebParam(name="deliveryPrice")
			double deliveryPrice,
			@WebParam(name="measuringUnit")
			String measuringUnit);
	
	@WebMethod
	public boolean createOrUpdateService(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="serviceID")
			String serviceID,
			@WebParam(name="description")
			String description);
	
	@WebMethod
	public boolean createOrUpdateSparePartGroup(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="sparePartGroupID")
			String sparePartGroupID,
			@WebParam(name="description")
			String description);
	
	@WebMethod
	public boolean createOrUpdateVehicle(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="VIN")
			String VIN,
			@WebParam(name="clientID")
			String clientID,
			@WebParam(name="vehicleModelID")
			String vehicleModelID,
			@WebParam(name="warrantyConditionsID")
			String warrantyConditionsID,
			@WebParam(name="engineNumber")
			String engineNumber,
			@WebParam(name="plateNumber")
			String plateNumber,
			@WebParam(name="purchaseDate")
			Date purchaseDate);
	
	@WebMethod
	public boolean createOrUpdatePerson(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="clientID")
			String clientID,
			@WebParam(name="name")
			String name,
			@WebParam(name="family")
			String family,
			@WebParam(name="addressCity")
			String addressCity,
			@WebParam(name="addressLine")
			String addressLine,
			@WebParam(name="phoneNumber")
			String phoneNumber,
			@WebParam(name="mail")
			String mail,
			@WebParam(name="IBANNumber")
			String IBANNumber,
			@WebParam(name="SWIFTCode")
			String SWIFTCode);
	
	@WebMethod
	public boolean createOrUpdateCompany(
			@WebParam(name="electronicShopID")
			String electronicShopID,
			@WebParam(name="clientID")
			String clientID,
			@WebParam(name="name")
			String name,
			@WebParam(name="contactPerson")
			String contactPerson,
			@WebParam(name="addressCity")
			String addressCity,
			@WebParam(name="addressLine")
			String addressLine,
			@WebParam(name="phoneNumber")
			String phoneNumber,
			@WebParam(name="mail")
			String mail,
			@WebParam(name="IBANNumber")
			String IBANNumber,
			@WebParam(name="SWIFTCode")
			String SWIFTCode,
			@WebParam(name="registrationNumber")
			String registrationNumber,
			@WebParam(name="VATNumber")
			String VATNumber);
	

}
