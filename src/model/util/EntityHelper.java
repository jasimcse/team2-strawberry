package model.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * 
 * Помощни функции за работа с Google Datastore
 *
 */
public class EntityHelper {
	
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
			entity = new Entity(classLocal.getName());
		} else {
			try {
				Field field = classLocal.getDeclaredField(parent);
				field.setAccessible(true);
				entity = new Entity(classLocal.getName(), (Key)field.get(from));
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
				if ((valueObject == null) && (!nullables.contains(name))) {
					throw new RuntimeException("The field " + name + " is declared to be not null!");
				}
				
				if (valueObject instanceof LimitedString) {
					valueObject = ((LimitedString) valueObject).getString();
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
	
}
