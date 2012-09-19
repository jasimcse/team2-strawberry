package model.util;

//TODO - comments
public class LimitedString {
	
	private int limit=0;
	private boolean exact=false;
	private String str;
	
	public LimitedString(int limit) {
		this.limit = limit;
	}
	
	public LimitedString(int limit, boolean exact) {
		this(limit);
		this.exact = exact;
	}
	
	public LimitedString(int limit, String initial) {
		this(limit);
		setString(initial);
	}
	
	public LimitedString(int limit, boolean exact, String initial) {
		this(limit, exact);
		setString(initial);
	}
	
	public void setString(String str) {
		if (str == null) {
			//TODO - throw exception 
		}
		
		if ((exact) && (str.length() != limit)) {
			//TODO - throw exception
		}
		
		if (str.length() > limit) {
			//TODO - throw exception
		}
		
		this.str = str; 
	}
	
	public String getString() {
		return str;
	}

}
