package model.util;

/**
 * 
 * Wrapper на класът String
 * Позволява проверка на дължината на стринга
 * По подразбиране началния стринг е <b>null</b>
 *
 */
public class LimitedString {
	
	private int limit=0;
	private boolean exact=false;
	private String str;
	
	/**
	 * 
	 * @param limit - ограничение отгоре на символие в стринг-а
	 * 
	 */
	public LimitedString(int limit) {
		this.limit = limit;
	}
	
	/**
	 * 
	 * @param limit - ограничение отгоре на символие в стринг-а
	 * @param exact - дали ограничението да е и отдолу
	 */
	public LimitedString(int limit, boolean exact) {
		this(limit);
		this.exact = exact;
	}
	
	/**
	 * 
	 * @param limit - ограничение отгоре на символие в стринг-а
	 * @param initial - начална стойност
	 */
	public LimitedString(int limit, String initial) {
		this(limit);
		setString(initial);
	}
	
	/**
	 * 
	 * @param limit - ограничение отгоре на символие в стринг-а
	 * @param exact - дали ограничението да е и отдолу
	 * @param initial - начална стойност
	 */
	public LimitedString(int limit, boolean exact, String initial) {
		this(limit, exact);
		setString(initial);
	}
	
	public void setString(String str) {
		if (str == null) {
			throw new IllegalArgumentException("Null object!");
		}
		
		if ((exact) && (str.length() != limit)) {
			throw new IllegalArgumentException("String length is not exactly " + limit + "!");
		}
		
		if (str.length() > limit) {
			throw new IllegalArgumentException("String length is greater than " + limit + "!");
		}
		
		this.str = str; 
	}
	
	public String getString() {
		return str;
	}

}
