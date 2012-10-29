package controller.common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MenuEntry {
	
	private String value;
	private String path;
	private String type;
	private List<MenuEntry> swallowedEntries;
	
	@SuppressWarnings("unused")
	private MenuEntry() { }
	
	public MenuEntry(String value) {
		this.value = value;
		type = "simpleGroup";
	}
	
	public MenuEntry(String value, String path) {
		this.value = value;
		this.path = path;
		type = "simpleEntry";
	}
	
	public String getValue() {
		return value;
	}
	public String getPath() {
		return path;
	}
	public String getType() {
		return type;
	}
	
	public MenuEntry swallow(MenuEntry ... entries) {
		MenuEntry newEntry = new MenuEntry(this.value);
		newEntry.swallowedEntries = new LinkedList<MenuEntry>(Arrays.asList(entries));
		
		return newEntry;
	}
	
	public List<MenuEntry> getSwallowedEntries() {
		return swallowedEntries;
	}
	
}
