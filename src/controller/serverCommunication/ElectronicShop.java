package controller.serverCommunication;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import model.WarrantyConditions;

/**
 * 
 * дефинира заявките които могат да се викат от електронния магазин
 *
 */
@WebService
public class ElectronicShop {

	@WebMethod
	public boolean createOrUpdateWarrantyConditions(
			@WebParam(name="ID")
			String ID,
			@WebParam(name="months")
			long months,
			@WebParam(name="milage")
			long milage,
			@WebParam(name="otherConditions")
			String otherConditions) {
		
		boolean ret = true;
		
		WarrantyConditions wc = new WarrantyConditions();
		try {
			wc.setForeignID(ID);
			wc.setMonths(months);
			wc.setMileage(milage);
			wc.setOtherConditions(otherConditions);
			wc.writeToDB();
		} catch (RuntimeException e) {
			//TODO - log it
			ret = false;
		}
		
		return ret;
	}
}
