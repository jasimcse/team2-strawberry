package controller.common;

public class MenuEntry {
	
	private String value;
	private String path;
	
	@SuppressWarnings("unused")
	private MenuEntry() { }
	
	public MenuEntry(String value, String path) {
		this.value = value;
		this.path = path;
	}
	
	public String getValue() {
		return value;
	}
	public String getPath() {
		return path;
	}
	
}
