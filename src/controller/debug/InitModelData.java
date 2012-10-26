package controller.debug;

import java.util.List;

import javax.servlet.http.HttpServlet;

import model.Autoservice;
import model.Employee;
import model.EmployeeAutoservice;
import model.Supplier;
import model.util.EntityHelper;


/**
 * 
 * Грижи се за установяване на базата данни в някакво начално състояние при стартиране на сървъра
 *
 */
public class InitModelData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public InitModelData() {
        super();
        
        // TODO - да се измести от тук
        // Инициализира базата данни
        EntityHelper.initializeDataStore();
        
        addAdministratorAccount();
        addDefaultSupplier();
    }
    
    
    private void addAdministratorAccount() {

    	// generate administrator employee
    	if (Employee.countGetByName("Administrator") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Administrator");
	    	emp.setFamily("Administratorski");
	    	emp.setAddressCity("Ruse");
	    	emp.setAddressLine("ul. Borisova, 120");
	    	emp.setMail("admin@admin.bg");
	    	emp.setPhoneNumber("+359881234567");
	    	
	    	emp.writeToDB();
    	}
    	
    	// generate administrator's autoservice; it's stupid I know :)
    	if (Autoservice.countGetByName("Administratorski serviz") < 1) {
	    	Autoservice autoservice = new Autoservice();
	    	autoservice.setName("Administratorski serviz");
	    	autoservice.setAddressCity("Ruse");
	    	autoservice.setAddressLine("nqma");
	    	autoservice.setMail("hop@trop.bg");
	    	autoservice.setPhoneNumber("1234567890");
	    	autoservice.setIBANNumber("00112233445566778899");
	    	autoservice.setSWIFTCode("MY_SWIFT_CODE");
	    	autoservice.setRegistrationNumber("BG1209876");
	    	
	    	autoservice.writeToDB();
    	}
    	
    	// finally generate administrator account
    	if (EmployeeAutoservice.countGetByUsername("admin") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Administratorski serviz", 0, 2);
    		List<Employee> listEmp = Employee.queryGetByName("Administrator", 0, 2);
    		if ((listAuto.size() != 1) || (listEmp.size() != 1)) {
    			throw new RuntimeException("Oooopsssie :)");
    		}
    		
    		empa.setAutoservice(listAuto.get(0));
    		empa.setEmployee(listEmp.get(0));
    		empa.setUsername("admin");
    		empa.setPassword("1234");
	    	empa.setPosition(EmployeeAutoservice.ADMINISTRATOR);
	    	
	    	empa.writeToDB();
    	}
    }
    
    private void addDefaultSupplier() {
    	
    	if (Supplier.countGetByName("Официален доставчик") < 1) {
	    	Supplier sup = new Supplier();
	    	sup.setName("Официален доставчик");
	    	sup.setAddressCity("-");
	    	sup.setAddressLine("-");
	    	sup.setContactPerson("-");
	    	sup.setMail("-");
	    	sup.setPhoneNumber("-");
	    	sup.setIBANNumber("-");
	    	sup.setSWIFTCode("-");
	    	sup.setRegistrationNumber("---------");
	    	
	    	sup.writeToDB();
    	}
    }

}
