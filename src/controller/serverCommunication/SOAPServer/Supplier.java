package controller.serverCommunication.SOAPServer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import model.SparePart;
import model.SparePartSupplier;


/**
 * 
 * дефинира заявките които могат да се викат от доставяиците на резервни части и консумативи
 *
 */
@WebService(endpointInterface="controller.serverCommunication.SOAPServer.SupplierInterface")
public class Supplier implements SupplierInterface {

	static final private Logger logger = Logger.getLogger(Insurer.class.getName());
	
	@Override
	public boolean createOrUpdateSparePartSupplier(
			String supplierID,
			String originalSparePartID,
			String supplierSparePartID,
			double deliveryPrice) {
		
		// check if we know that supplier
		model.Supplier supplier;
		try {
			Key supplierKey = KeyFactory.stringToKey(supplierID);
			supplier = model.Supplier.readEntity(supplierKey);
		} catch(Exception e) {
			logger.log(Level.WARNING, "supplierID \"" + supplierID + "\" not known!", e);
			return false;
		}
		
		List<SparePartSupplier> srsl = SparePartSupplier.queryGetByForeignID(supplierSparePartID, 0, 1, supplier.getID());
		SparePartSupplier srs;
		if (srsl.size() == 0) {
			srs = new SparePartSupplier();
		} else {
			srs = srsl.get(0);
		}
		try {
			List<SparePart> sparePart = SparePart.queryGetByForeignID(originalSparePartID, 0, 1);
			if (sparePart.size() == 0) {
				logger.warning("originalSparePartID \"" + originalSparePartID + "\" not known!");
				return false;
			}

			srs.setSupplierID(supplier.getID());
			srs.setSparePartID(sparePart.get(0).getID());
			srs.setForeignID(supplierSparePartID);
			srs.setDeliveryPrice(deliveryPrice);
			srs.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

}
