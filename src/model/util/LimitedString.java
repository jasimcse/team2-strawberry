package model.util;

/**
 * 
 * Wrapper �� ������ String
 * ��������� �������� �� ��������� �� �������
 * �� ������������ �������� ������ � <b>null</b>
 *
 */
public class LimitedString {
	
	private int limit=0;
	private boolean exact=false;
	private String str;
	
	/**
	 * 
	 * @param limit - ����������� ������ �� �������� � ������-�
	 * 
	 */
	public LimitedString(int limit) {
		this.limit = limit;
	}
	
	/**
	 * 
	 * @param limit - ����������� ������ �� �������� � ������-�
	 * @param exact - ���� ������������� �� � � ������
	 */
	public LimitedString(int limit, boolean exact) {
		this(limit);
		this.exact = exact;
	}
	
	/**
	 * 
	 * @param limit - ����������� ������ �� �������� � ������-�
	 * @param initial - ������� ��������
	 */
	public LimitedString(int limit, String initial) {
		this(limit);
		setString(initial);
	}
	
	/**
	 * 
	 * @param limit - ����������� ������ �� �������� � ������-�
	 * @param exact - ���� ������������� �� � � ������
	 * @param initial - ������� ��������
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
