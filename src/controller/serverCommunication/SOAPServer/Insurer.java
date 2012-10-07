package controller.serverCommunication.SOAPServer;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import model.InsurerRequest;
import model.Vehicle;

/**
 * 
 * дефинира заявките които могат да се викат от застрахователите
 *
 */
@WebService(endpointInterface="controller.serverCommunication.SOAPServer.InsurerInterface")
public class Insurer implements InsurerInterface {
	
	static final private Logger logger = Logger.getLogger(Insurer.class.getName());

	@Override
	public boolean createInsurerRequest(
			String insurerID,
			String VIN) {
		
		// check if we know that insurer
		model.Insurer insurer;
		try {
			Key insurerKey = KeyFactory.stringToKey(insurerID);
			if (!insurerKey.getKind().equals(model.Insurer.class.getSimpleName())) {
				throw new Exception();
			}
			insurer = model.Insurer.readEntity(insurerKey);
		} catch(Exception e) {
			logger.log(Level.WARNING, "insurerID \"" + insurerID + "\" not known!", e);
			return false;
		}

		InsurerRequest ir = new InsurerRequest();
		try {
			List<Vehicle> vehicle = Vehicle.queryGetByVIN(VIN, 0, 1);
			if (vehicle.size() == 0) {
				logger.warning("VIN \"" + VIN + "\" not known!");
				return false;
			}
			
			ir.setInsurerID(insurer.getID());
			ir.setVehicleID(vehicle.get(0).getID());
			ir.setDate(new Date());
			ir.writeToDB();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING, "Can't write to DB!", e);
			return false;
		}

		return true;
	}

}
