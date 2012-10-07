package controller.serverCommunication.SOAPServer;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface SupplierInterface {

	@WebMethod
	public boolean createOrUpdateSparePartSupplier(
			@WebParam(name="supplierID")
			String supplierID,
			@WebParam(name="originalSparePartID")
			String originalSparePartID,
			@WebParam(name="supplierSparePartID")
			String supplierSparePartID,
			@WebParam(name="deliveryPrice")
			double deliveryPrice);
}
