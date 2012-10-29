package controller.common;

import java.util.BitSet;

public class PageAttributes {
	
	private String url;
	private String title;
	private boolean loggedUsersOnly;
	
	private Rights readRights;
	private Rights writeRights;
	
	public static class Rights {
		private final int POSITIONS_COUNT = 6;
		
		private BitSet rights = new BitSet(POSITIONS_COUNT);
		
		private int parsePosition(String position) {
			int pos = Integer.parseInt(position); 
			if ((pos > POSITIONS_COUNT) || (pos <= 0)) {
				throw new RuntimeException("Position string is not a number between 1 and " + POSITIONS_COUNT + " inculding!");
			}
			return pos;
		}
		
		public boolean getRight(String position) {
			return rights.get(parsePosition(position));
		}
		
		public void setRight(String position, boolean right) {
			rights.set(parsePosition(position), right);
		}
	}
	
	@SuppressWarnings("unused")
	private PageAttributes() { }
	
	public PageAttributes(String url, String title) {
		this.url = url;
		this.title = title;
		loggedUsersOnly = false;
		readRights = new Rights();
		writeRights = new Rights();
	}
	
	public PageAttributes(String url, String title, boolean loggedUsersOnly) {
		this.url = url;
		this.title = title;
		this.loggedUsersOnly = loggedUsersOnly;
		readRights = new Rights();
		writeRights = new Rights();
	}
	
	public PageAttributes(String url, String title, boolean loggedUsersOnly, Rights readRights, Rights writeRights) {
		this.url = url;
		this.title = title;
		this.loggedUsersOnly = loggedUsersOnly;
		this.readRights = readRights;
		this.writeRights = writeRights;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public boolean isLoggedUsersOnly() {
		return loggedUsersOnly;
	}
	
	public boolean getReadRight(String position) {
		return readRights.getRight(position);
	}
	
	public boolean getWriteRight(String position) {
		return writeRights.getRight(position);
	}
	
}
