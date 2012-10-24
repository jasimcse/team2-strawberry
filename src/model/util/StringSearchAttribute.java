package model.util;

/**
 * 
 * помощен клас за филтриране на записи от базата данни с атрибути от тип символен низ
 *
 */
public class StringSearchAttribute {
	private String attributeName;
	private String searchString;
	
	public StringSearchAttribute(String attributeName, String searchString) {
		this.attributeName = attributeName;
		this.searchString = searchString;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getSearchString() {
		return searchString;
	}
	
}
