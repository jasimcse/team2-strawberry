package controller.serverCommunication.SOAPServer;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService
public interface InsurerInterface {
	
	@WebMethod
	public boolean createInsurerRequest(
			@WebParam(name="insurerID")
			String insurerID,
			@WebParam(name="VIN")
			String VIN);

}
