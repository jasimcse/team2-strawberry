package model.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * помощен клас за филтриране на записи от базата данни с атрибути от тип символен низ
 *
 */
public class StringSearchAttribute {
	private String attributeName;
	private String searchString;
	private List<String> parentIDList;
	
	public StringSearchAttribute(String attributeName, String searchString) {
		this(attributeName, searchString, null);
	}
	
	public StringSearchAttribute(String attributeName, String searchString, List<String> parentIDList) {
		this.attributeName = attributeName;
		this.searchString = searchString;
		if (parentIDList != null) {
			this.parentIDList = parentIDList;
		} else {
			this.parentIDList = new ArrayList<String>();
		}
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getSearchString() {
		return searchString;
	}
	
	public List<String> getParentIDList() {
		return parentIDList;
	}
	
}
