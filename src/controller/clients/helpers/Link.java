package controller.clients.helpers;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Link implements Serializable {
	private String text;
	private boolean disabled;
	private int param;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public int getParam() {
		return param;
	}
	public void setParam(int param) {
		this.param = param;
	}
}
