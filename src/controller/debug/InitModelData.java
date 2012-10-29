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
        
        addDebugAccounts();
        addDefaultSupplier();
    }
    
    
    private void addDebugAccounts() {

    	// generate administrator employee
    	if (Employee.countGetByName("Administrator") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Administrator");
	    	emp.setFamily("n/a");
	    	emp.setAddressCity("n/a");
	    	emp.setAddressLine("n/a");
	    	emp.setMail("admin@default.bg");
	    	emp.setPhoneNumber("n/a");
	    	
	    	emp.writeToDB();
    	}
    	
    	// generate administrator's autoservice; it's stupid I know :)
    	if (Autoservice.countGetByName("Autoservice") < 1) {
	    	Autoservice autoservice = new Autoservice();
	    	autoservice.setName("Autoservice");
	    	autoservice.setAddressCity("n/a");
	    	autoservice.setAddressLine("n/a");
	    	autoservice.setMail("autoservice@default.bg");
	    	autoservice.setPhoneNumber("n/a");
	    	autoservice.setIBANNumber("n/a");
	    	autoservice.setSWIFTCode("n/a");
	    	autoservice.setRegistrationNumber("BG1209876");
	    	
	    	autoservice.writeToDB();
    	}
    	
    	// generate admin account
    	if (EmployeeAutoservice.countGetByUsername("admin") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Autoservice", 0, 2);
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
    	
    	// generate manager account
    	if (Employee.countGetByName("Manager") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Manager");
	    	emp.setFamily("n/a");
	    	emp.setAddressCity("n/a");
	    	emp.setAddressLine("n/a");
	    	emp.setMail("manager@default.bg");
	    	emp.setPhoneNumber("n/a");
	    	
	    	emp.writeToDB();
    	}
    	
    	if (EmployeeAutoservice.countGetByUsername("manager") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Autoservice", 0, 2);
    		List<Employee> listEmp = Employee.queryGetByName("Manager", 0, 2);
    		if ((listAuto.size() != 1) || (listEmp.size() != 1)) {
    			throw new RuntimeException("Oooopsssie :)");
    		}
    		
    		empa.setAutoservice(listAuto.get(0));
    		empa.setEmployee(listEmp.get(0));
    		empa.setUsername("manager");
    		empa.setPassword("1234");
	    	empa.setPosition(EmployeeAutoservice.MANAGER);
	    	
	    	empa.writeToDB();
    	}
    	
    	// generate cashier account
    	if (Employee.countGetByName("Cashier") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Cashier");
	    	emp.setFamily("n/a");
	    	emp.setAddressCity("n/a");
	    	emp.setAddressLine("n/a");
	    	emp.setMail("cashier@default.bg");
	    	emp.setPhoneNumber("n/a");
	    	
	    	emp.writeToDB();
    	}
    	
    	if (EmployeeAutoservice.countGetByUsername("cashier") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Autoservice", 0, 2);
    		List<Employee> listEmp = Employee.queryGetByName("Cashier", 0, 2);
    		if ((listAuto.size() != 1) || (listEmp.size() != 1)) {
    			throw new RuntimeException("Oooopsssie :)");
    		}
    		
    		empa.setAutoservice(listAuto.get(0));
    		empa.setEmployee(listEmp.get(0));
    		empa.setUsername("cashier");
    		empa.setPassword("1234");
	    	empa.setPosition(EmployeeAutoservice.CASHIER);
	    	
	    	empa.writeToDB();
    	}
    	
    	// generate warehouseman account
    	if (Employee.countGetByName("Warehouseman") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Warehouseman");
	    	emp.setFamily("n/a");
	    	emp.setAddressCity("n/a");
	    	emp.setAddressLine("n/a");
	    	emp.setMail("warehouseman@default.bg");
	    	emp.setPhoneNumber("n/a");
	    	
	    	emp.writeToDB();
    	}
    	
    	if (EmployeeAutoservice.countGetByUsername("warehouseman") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Autoservice", 0, 2);
    		List<Employee> listEmp = Employee.queryGetByName("Warehouseman", 0, 2);
    		if ((listAuto.size() != 1) || (listEmp.size() != 1)) {
    			throw new RuntimeException("Oooopsssie :)");
    		}
    		
    		empa.setAutoservice(listAuto.get(0));
    		empa.setEmployee(listEmp.get(0));
    		empa.setUsername("warehouseman");
    		empa.setPassword("1234");
	    	empa.setPosition(EmployeeAutoservice.WAREHOUSEMAN);
	    	
	    	empa.writeToDB();
    	}
    	
    	// generate diagnostician account
    	if (Employee.countGetByName("Diagnostician") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Diagnostician");
	    	emp.setFamily("n/a");
	    	emp.setAddressCity("n/a");
	    	emp.setAddressLine("n/a");
	    	emp.setMail("diagnostician@default.bg");
	    	emp.setPhoneNumber("n/a");
	    	
	    	emp.writeToDB();
    	}
    	
    	if (EmployeeAutoservice.countGetByUsername("diagnostician") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Autoservice", 0, 2);
    		List<Employee> listEmp = Employee.queryGetByName("Diagnostician", 0, 2);
    		if ((listAuto.size() != 1) || (listEmp.size() != 1)) {
    			throw new RuntimeException("Oooopsssie :)");
    		}
    		
    		empa.setAutoservice(listAuto.get(0));
    		empa.setEmployee(listEmp.get(0));
    		empa.setUsername("diagnostician");
    		empa.setPassword("1234");
	    	empa.setPosition(EmployeeAutoservice.DIAGNOSTICIAN);
	    	
	    	empa.writeToDB();
    	}
    	
    	// generate auto mechanic account
    	if (Employee.countGetByName("Auto_Mechanic") < 1) {
	    	Employee emp = new Employee();
	    	emp.setName("Auto_Mechanic");
	    	emp.setFamily("n/a");
	    	emp.setAddressCity("n/a");
	    	emp.setAddressLine("n/a");
	    	emp.setMail("auto_mechanic@default.bg");
	    	emp.setPhoneNumber("n/a");
	    	
	    	emp.writeToDB();
    	}
    	
    	if (EmployeeAutoservice.countGetByUsername("auto_mechanic") < 1) {
    		EmployeeAutoservice empa = new EmployeeAutoservice();
    		List<Autoservice> listAuto = Autoservice.queryGetByName("Autoservice", 0, 2);
    		List<Employee> listEmp = Employee.queryGetByName("Auto_Mechanic", 0, 2);
    		if ((listAuto.size() != 1) || (listEmp.size() != 1)) {
    			throw new RuntimeException("Oooopsssie :)");
    		}
    		
    		empa.setAutoservice(listAuto.get(0));
    		empa.setEmployee(listEmp.get(0));
    		empa.setUsername("auto_mechanic");
    		empa.setPassword("1234");
	    	empa.setPosition(EmployeeAutoservice.AUTO_MECHANIC);
	    	
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
