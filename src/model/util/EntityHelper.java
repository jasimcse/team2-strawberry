package model.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * 
 * ������� ������� �� ������ � Google Datastore
 *
 */
public class EntityHelper {
	
	/**
	 * ��������� Entity o���� �� ������� �� ����� �� Data Model-�
	 * 
	 * @param from - �� ��� ����� �� ������ �������
	 * @param parent - ��� �� ����� ������ � ����� � ������� ����� �� ��������
	 * @param ignored - ����� �� �������� ����� ������ �� �� ��������� � �� �� �� �������� <b>(transient)</b>
	 * @param nullables - ����� �� �������� ����� ����� �� ���� ��������� null
	 * 
	 * @return ���������� Entity �����
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
	 * ������� ����������� �� Entity o���� �� ������� �� ����� �� Data Model-�
	 * 
	 * @param entity - ������� ����� �� �� �������
	 * @param from - �� ��� ����� �� ������ �������
	 * @param parent - ��� �� ����� ������ � ����� � ������� ����� �� ��������
	 * @param ignored - ����� �� �������� ����� ������ �� �� ��������� � �� �� �� �������� <b>(transient)</b>
	 * @param nullables - ����� �� �������� ����� ����� �� ���� ��������� null
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
	 * ������� ���������� �� Entity o���� � �� ������� � ����� �� Data Model-�
	 * 
	 * @param entity - ������� ����� �� �� ����
	 * @param from - � ��� ����� �� �� ������� �������
	 * @param parent - ��� �� ����� ������ � ����� �� ������� ����� �� ��������
	 * @param ignored - ����� �� �������� ����� ������ �� �� ��������� � �� �� �� �������� <b>(transient)</b>
	 * @param nullables - ����� �� �������� ����� ����� �� ���� ��������� null
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
