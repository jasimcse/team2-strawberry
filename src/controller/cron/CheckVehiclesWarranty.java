package controller.cron;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Vehicle;

import controller.serverCommunication.SOAPServer.Insurer;

/**
 * Servlet implementation class CheckVehiclesWarranty
 */
public class CheckVehiclesWarranty extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static final private Logger logger = Logger.getLogger(Insurer.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckVehiclesWarranty() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!isRealRequest(request)) {
			logger.info("Not Real Request");
			return;
		}
		
		logger.info("Real Request");
		checkThem();
		
	}
	
	private boolean isRealRequest(HttpServletRequest req) {
		return "true".equals(req.getHeader("X-AppEngine-Cron"));
	}
	
	
	/**
	 * check for vehicles whose warranty expires now
	 */
	private void checkThem() {
		List<Vehicle> vehicleList = Vehicle.queryGetWarrantyOK(0, 1000);
		Calendar now = new GregorianCalendar();
		
		for (Iterator<Vehicle> iterator = vehicleList.iterator(); iterator.hasNext(); ) {
			Vehicle vehicle = iterator.next();
			Calendar cal = new GregorianCalendar();
			cal.setTime(vehicle.getPurchaseDate());
			cal.add(Calendar.MONTH, Long.valueOf(vehicle.getWarrantyConditions().getMonths()).intValue());
			if (cal.before(now)) {
				vehicle.setWarrantyOK(Vehicle.WARRANTY_NO);
			} else {
				iterator.remove();
			}
		}
		
		if (!vehicleList.isEmpty()) {
			Vehicle.writeBatch(vehicleList);
		}
		
	}

}
