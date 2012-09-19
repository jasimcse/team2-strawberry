package model.util;

import java.lang.reflect.Field;
import java.util.Set;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

// TODO - comments !!! :)
public class EntityHelper {
	
	public static Entity buildIt(Object from, String parent, Set<String> ignored, Set<String> nullables) {
		Class<? extends Object> classLocal = from.getClass();
		Field allFields[];
		Object valueObject = null;
		String name;
		Entity entity = null;
		
		allFields = classLocal.getDeclaredFields();
		
		// Entity creation
		if (parent == null) {
			entity = new Entity(classLocal.getName());
		} else {
			try {
				entity = new Entity(classLocal.getName(), (Key)classLocal.getDeclaredField(parent).get(from));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				// parent not found
				//TODO - throw exception
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				// parent not found
				//TODO - throw exception
				return null;
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				// parent not found
				//TODO - throw exception
				return null;
			}
		}
		
		
		// Fields filling
		for (Field field : allFields) {
			name = field.getName();
			if (!ignored.contains(name)) {
				field.setAccessible(true);
				try {
					valueObject = field.get(from); 
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					//TODO - throw exception
					return null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					//TODO - throw exception
					return null;
				}
				if ((valueObject == null) && (!nullables.contains(name))) {
					// field declared not null is null
					//TODO - throw exception
					return null;
				}
				entity.setProperty(name, valueObject);
			}
		}
		
		return entity;
	}
	
	
	public static <EntityClass> EntityClass readIt(Entity entity, EntityClass to, String parent, Set<String> ignored, Set<String> nullables) {
		Class<? extends Object> classLocal = to.getClass();
		Field allFields[];
		Object valueObject = null;
		String name;
		
		allFields = classLocal.getDeclaredFields();
		
		// Fields filling
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
					// field declared not null is null
					//TODO - throw exception
					return to;
				}
				
				try {
					field.set(to, valueObject);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					//TODO - throw exception
					return to;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					//TODO - throw exception
					return to;
				}
			}
		}
		
		return to;
	}

}
