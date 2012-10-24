package model.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

/**
 * 
 * Помощни функции за работа с Google Datastore
 *
 */
public class EntityHelper {
	
	private static Key autoserviceParentKey;
	private static Key employeeParentKey;
	private static Key clientParentKey;
	private static Key insurerParentKey;
	private static Key serviceParentKey;
	private static Key sparePartParentKey;
	private static Key sparePartGroupParentKey;
	private static Key supplierParentKey;
	private static Key vehicleModelParentKey;
	private static Key warrantyConditionsParentKey;
	
	
	/**
	 * инициализира базата данни;
	 * създава Entity групи, за да може повечето заявки да са "Strong consistent", а не "Eventual consistent"
	 * т.е. след като се направи запис в базата, веднага след това да може да се види какво е записано, а не след няколко секунди 
	 * 
	 */
	public static void initializeDataStore() {
		Entity ent;
		DatastoreService store = DatastoreServiceFactory.getDatastoreService();
		
		ent = store.prepare(new Query("AutoserviceParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("AutoserviceParent");
			store.put(ent);
		}
		autoserviceParentKey = ent.getKey();
		
		ent = store.prepare(new Query("EmployeeParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("EmployeeParent");
			store.put(ent);
		}
		employeeParentKey = ent.getKey();
		
		ent = store.prepare(new Query("ClientParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("ClientParent");
			store.put(ent);
		}
		clientParentKey = ent.getKey();
		
		ent = store.prepare(new Query("InsurerParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("InsurerParent");
			store.put(ent);
		}
		insurerParentKey = ent.getKey();
		
		ent = store.prepare(new Query("ServiceParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("ServiceParent");
			store.put(ent);
		}
		serviceParentKey = ent.getKey();
		
		ent = store.prepare(new Query("SparePartParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("SparePartParent");
			store.put(ent);
		}
		sparePartParentKey = ent.getKey();
		
		ent = store.prepare(new Query("SparePartGroupParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("SparePartGroupParent");
			store.put(ent);
		}
		sparePartGroupParentKey = ent.getKey();
		
		ent = store.prepare(new Query("SupplierParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("SupplierParent");
			store.put(ent);
		}
		supplierParentKey = ent.getKey();
		
		ent = store.prepare(new Query("VehicleModelParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("VehicleModelParent");
			store.put(ent);
		}
		vehicleModelParentKey = ent.getKey();
		
		ent = store.prepare(new Query("WarrantyConditionsParent").setKeysOnly()).asSingleEntity();
		if (ent == null) {
			ent = new Entity("WarrantyConditionsParent");
			store.put(ent);
		}
		warrantyConditionsParentKey = ent.getKey();
		
	}
	
	
	public static Key getAutoserviceParent() {
		if (autoserviceParentKey == null) {
			throw new RuntimeException("Autoservice Parent Key is NULL!");
		}
		return autoserviceParentKey;
	}
	
	public static Key getEmployeeParent() {
		if (employeeParentKey == null) {
			throw new RuntimeException("Employee Parent Key is NULL!");
		}
		return employeeParentKey;
	}
	
	public static Key getClientParent() {
		if (clientParentKey == null) {
			throw new RuntimeException("Client Parent Key is NULL!");
		}
		return clientParentKey;
	}
	
	public static Key getInsurerParent() {
		if (insurerParentKey == null) {
			throw new RuntimeException("Insurer Parent Key is NULL!");
		}
		return insurerParentKey;
	}
	
	public static Key getServiceParent() {
		if (serviceParentKey == null) {
			throw new RuntimeException("Service Parent Key is NULL!");
		}
		return serviceParentKey;
	}
	
	public static Key getSparePartParent() {
		if (sparePartParentKey == null) {
			throw new RuntimeException("SparePart Parent Key is NULL!");
		}
		return sparePartParentKey;
	}
	
	public static Key getSparePartGroupParent() {
		if (sparePartGroupParentKey == null) {
			throw new RuntimeException("SparePart Parent Key is NULL!");
		}
		return sparePartGroupParentKey;
	}
	
	public static Key getSupplierParent() {
		if (supplierParentKey == null) {
			throw new RuntimeException("Supplier Parent Key is NULL!");
		}
		return supplierParentKey;
	}
	
	public static Key getVehicleModelParent() {
		if (vehicleModelParentKey == null) {
			throw new RuntimeException("Vehicle Model Parent Key is NULL!");
		}
		return vehicleModelParentKey;
	}
	
	public static Key getWarrantyConditionsParent() {
		if (warrantyConditionsParentKey == null) {
			throw new RuntimeException("Warranty Conditions Parent Key is NULL!");
		}
		return warrantyConditionsParentKey;
	}
	
	
	/**
	 * Построява Entity oбект от данните на обект от Data Model-а
	 * 
	 * @param from - от кой обект се вземат данните
	 * @param parent - как се казва полето в което е записан ключа на родителя
	 * @param ignored - имена на полетата които трябва да се игнорират и да не се записват <b>(transient)</b>
	 * @param nullables - имена на полетата които могат да имат стойности null
	 * 
	 * @return построения Entity обект
	 * 
	 */
	public static Entity buildIt(Object from, String parent, Set<String> ignored, Set<String> nullables) {
		Object valueObject = null;
		
		// parameters check
		if (from == null) {
			throw new NullPointerException("Argument \"from\" is null!");
		}
		if (ignored == null) {
			ignored = new HashSet<String>();
		}
		if (nullables == null) {
			nullables = new HashSet<String>();
		}
		
		Class<? extends Object> classLocal = from.getClass();
		Entity entity = null;
		
		
		// Entity creation
		if (parent == null) {
			entity = new Entity(classLocal.getSimpleName());
		} else {
			try {
				Field field = classLocal.getDeclaredField(parent);
				field.setAccessible(true);
				valueObject = field.get(from); 
				if ((valueObject == null) && (!nullables.contains(parent))) {
					throw new RuntimeException("The parent field " + parent + " is declared to be not null!");
				}
				entity = new Entity(classLocal.getSimpleName(), (Key)valueObject);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Parent field not found", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Parent field not found", e);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException("Parent field not found", e);
			}
		}
		
		populateIt(entity, from, parent, ignored, nullables);
		
		return entity;
	}
	
	
	/**
	 * Попълва стойностите на Entity oбект от данните на обект от Data Model-а
	 * 
	 * @param entity - обектът който ще се попълва
	 * @param from - от кой обект се вземат данните
	 * @param parent - как се казва полето в което е записан ключа на родителя
	 * @param ignored - имена на полетата които трябва да се игнорират и да не се записват <b>(transient)</b>
	 * @param nullables - имена на полетата които могат да имат стойности null
	 * 
	 */
	public static void populateIt(Entity entity, Object from, String parent, Set<String> ignored, Set<String> nullables) {
		
		// parameters check
		if (entity == null) {
			throw new NullPointerException("Argument \"entity\" is null!");
		}
		if (from == null) {
			throw new NullPointerException("Argument \"from\" is null!");
		}
		if (ignored == null) {
			ignored = new HashSet<String>();
		}
		if (nullables == null) {
			nullables = new HashSet<String>();
		}
		
		Class<? extends Object> classLocal = from.getClass();
		Field allFields[];
		Object valueObject = null;
		String name;
		
		allFields = classLocal.getDeclaredFields();
		
		for (Field field : allFields) {
			name = field.getName();
			if (!ignored.contains(name) && !name.equals(parent)) {
				field.setAccessible(true);
				try {
					valueObject = field.get(from); 
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Can't populate the field " + name + "!", e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Can't populate the field " + name + "!", e);
				}
				
				if (valueObject instanceof LimitedString) {
					valueObject = ((LimitedString) valueObject).getString();
				}
				if ((valueObject == null) && (!nullables.contains(name))) {
					throw new RuntimeException("The field " + name + " is declared to be not null!");
				}
				
				entity.setProperty(name, valueObject);
			}
		}
		
	}
	
	
	/**
	 * Прочита атрибутите на Entity oбект и ги попълва в обект от Data Model-а
	 * 
	 * @param entity - обектът който ще се чете
	 * @param from - в кой обект да се запишат данните
	 * @param parent - как се казва полето в което ще записан ключа на родителя
	 * @param ignored - имена на полетата които трябва да се игнорират и да не се записват <b>(transient)</b>
	 * @param nullables - имена на полетата които могат да имат стойности null
	 * 
	 */
	public static <EntityClass> EntityClass readIt(Entity entity, EntityClass to, String parent, Set<String> ignored, Set<String> nullables) {
		
		// parameters check
		if (entity == null) {
			throw new NullPointerException("Argument \"entity\" is null!");
		}
		if (to == null) {
			throw new NullPointerException("Argument \"to\" is null!");
		}
		if (ignored == null) {
			ignored = new HashSet<String>();
		}
		if (nullables == null) {
			nullables = new HashSet<String>();
		}
				
		Class<? extends Object> classLocal = to.getClass();
		Field allFields[];
		Object valueObject = null;
		String name;
		
		allFields = classLocal.getDeclaredFields();
		
		for (Field field : allFields) {
			name = field.getName();
			if (!ignored.contains(name)) {
				field.setAccessible(true);
				if (name.equals(parent)) {
					valueObject = entity.getParent();
				} else {
					valueObject = entity.getProperty(name);
				}
				
				if ((valueObject == null) && (!nullables.contains(name))) {
					throw new RuntimeException("The field " + name + " is declared to be not null!");
				}
				
				try {
					if (field.get(to) instanceof LimitedString) {
						if (valueObject == null) {
							((LimitedString)field.get(to)).clearString();
						} else {
							((LimitedString)field.get(to)).setString((String)valueObject);
						}
					} else {
						field.set(to, valueObject);
					}
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Can't set the field " + name + "!", e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Can't set the field " + name + "!", e);
				}
			}
		}
		
		return to;
	}
	
	public static List<Entity> stringSearchFilter(
			Iterator<Entity> iterator,
			List<StringSearchAttribute> stringSearchAttributes,
			int offset,
			int count) {
		
		for (StringSearchAttribute ssa : stringSearchAttributes) {
			if (ssa.getAttributeName() == null) {
				throw new RuntimeException("A searching field attribute name is null!");
			}
			if ("".equals(ssa.getAttributeName())) {
				throw new RuntimeException("The searching field attribute name is empty!");
			}
			if (ssa.getSearchString() == null) {
				throw new RuntimeException("A searching field search string is null!");
			}
			if ("".equals(ssa.getSearchString())) {
				throw new RuntimeException("The searching field search string is empty!");
			}
		}
		
		int passed = 0;
		List<Entity> list = new ArrayList<Entity>();
		
		filter:
		while (iterator.hasNext()) {
			Entity curr = iterator.next();
			
			for (StringSearchAttribute ssa : stringSearchAttributes) {
				Object temp = curr.getProperty(ssa.getAttributeName());
				if (temp instanceof String) {
					if (((String)temp).toLowerCase().indexOf(ssa.getSearchString().toLowerCase()) == -1) {
						// не съвпада
						continue filter; 
					}
				}
			}
			
			passed++;
			if (passed < offset) {
				continue;
			}
			
			list.add(curr);
			
			if (passed - offset >= count) {
				break;
			}
		}
		
		return list;
	}
	
	public static int stringSearchCount(
			Iterator<Entity> iterator,
			List<StringSearchAttribute> stringSearchAttributes) {
		
		for (StringSearchAttribute ssa : stringSearchAttributes) {
			if (ssa.getAttributeName() == null) {
				throw new RuntimeException("A searching field attribute name is null!");
			}
			if ("".equals(ssa.getAttributeName())) {
				throw new RuntimeException("The searching field attribute name is empty!");
			}
			if (ssa.getSearchString() == null) {
				throw new RuntimeException("A searching field search string is null!");
			}
			if ("".equals(ssa.getSearchString())) {
				throw new RuntimeException("The searching field search string is empty!");
			}
		}
		
		int passed = 0;
		int count = 10000;
		
		filter:
		while (iterator.hasNext()) {
			Entity curr = iterator.next();
			
			for (StringSearchAttribute ssa : stringSearchAttributes) {
				Object temp = curr.getProperty(ssa.getAttributeName());
				if (temp instanceof LimitedString) {
					if (((String)temp).toLowerCase().indexOf(ssa.getSearchString().toLowerCase()) == -1) {
						// не съвпада
						continue filter; 
					}
				}
			}
			
			passed++;
			
			if (passed >= count) {
				break;
			}
		}
		
		return passed;
	}
	
}
