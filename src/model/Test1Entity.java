package model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class Test1Entity {

	private int testInteger;
	private long testLong;
	private short testShort;
	private byte testByte;
	private float testFloat;
	private double testDouble;
	private String testStringLong;
	private String testStringShort;
	private boolean testBool;
	private Date testDate = new Date();
	
	private static final Set<String> IGNORED_FIELDS = new HashSet<String>(Arrays.asList(
		     new String[] {"serialVersionUID", "IGNORED_FIELDS"}
		));

	
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	

	
	public int getTestInteger() {
		return testInteger;
	}


	public void setTestInteger(int testInteger) {
		this.testInteger = testInteger;
	}


	public long getTestLong() {
		return testLong;
	}


	public void setTestLong(long testLong) {
		this.testLong = testLong;
	}


	public short getTestShort() {
		return testShort;
	}


	public void setTestShort(short testShort) {
		this.testShort = testShort;
	}


	public byte getTestByte() {
		return testByte;
	}


	public void setTestByte(byte testByte) {
		this.testByte = testByte;
	}


	public float getTestFloat() {
		return testFloat;
	}


	public void setTestFloat(float testFloat) {
		this.testFloat = testFloat;
	}


	public double getTestDouble() {
		return testDouble;
	}


	public void setTestDouble(double testDouble) {
		this.testDouble = testDouble;
	}


	public String getTestStringLong() {
		return testStringLong;
	}


	public void setTestStringLong(String testStringLong) {
		this.testStringLong = testStringLong;
	}


	public String getTestStringShort() {
		return testStringShort;
	}


	public void setTestStringShort(String testStringShort) {
		this.testStringShort = testStringShort;
	}


	public boolean isTestBool() {
		return testBool;
	}


	public void setTestBool(boolean testBool) {
		this.testBool = testBool;
	}


	public Date getTestDate() {
		return testDate;
	}


	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}


	public void getAll() {
		
	}
	
	
	public void save() {
		Class<? extends Test1Entity> c = this.getClass();
		Field fields[] = c.getDeclaredFields();
		Entity entity = new Entity("TestEntity");
		for (Field field : fields) {
			if (!IGNORED_FIELDS.contains(field.getName())) {
				field.setAccessible(true);
				try {
					entity.setProperty(field.getName(), field.get(this));
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					datastore.put(entity);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void read(Entity entity) {
		Class<? extends Test1Entity> c = this.getClass();
		Field fields[] = c.getDeclaredFields();
		for (Field field : fields) {
			System.out.print(field.getName());
			if (!IGNORED_FIELDS.contains(field.getName())) {
				field.setAccessible(true);
				System.out.print(" -> " + entity.getProperty(field.getName()));
				try {
					field.set(this, entity.getProperty(field.getName()));
					System.out.print(" ... OK");
				} catch (IllegalArgumentException e) {
					System.out.print(" ... IllegalArgumentException");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.out.print(" ... IllegalAccessException");
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}
	
	public void readFirst() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<Entity> le = datastore.prepare(
				new Query("TestEntity").
				addSort("__key__", SortDirection.ASCENDING)).
				asList(FetchOptions.Builder.withLimit(1));
		this.read(le.get(0));
	}
	
}
