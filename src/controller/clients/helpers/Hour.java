package controller.clients.helpers;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Hour implements Serializable {
	private String text;
	private Date time;
	private boolean disabled;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
