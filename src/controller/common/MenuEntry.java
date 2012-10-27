package controller.common;

public class MenuEntry {
	
	private String value;
	private String path;
	private String title;
	
	@SuppressWarnings("unused")
	private MenuEntry() { }
	
	public MenuEntry(String value, String title, String path) {
		this.value = value;
		this.path = path;
	}
	
	public String getValue() {
		return value;
	}
	public String getPath() {
		return path;
	}
	public String getTitle() {
		return title;
	}
	
}
