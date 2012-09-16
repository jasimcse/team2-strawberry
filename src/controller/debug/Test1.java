package controller.debug;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import model.Test1Entity;


@ManagedBean (name = "Test1")
@RequestScoped
public class Test1 {
	
	private Test1Entity local = new Test1Entity();
	private boolean saveOK = false;

	public int getTestInteger() {
		return local.getTestInteger();
	}

	public void setTestInteger(int testInteger) {
		local.setTestInteger(testInteger);
	}

	public long getTestLong() {
		return local.getTestLong();
	}

	public void setTestLong(long testLong) {
		local.setTestLong(testLong);
	}

	public short getTestShort() {
		return local.getTestShort();
	}

	public void setTestShort(short testShort) {
		local.setTestShort(testShort);
	}

	public byte getTestByte() {
		return local.getTestByte();
	}

	public void setTestByte(byte testByte) {
		local.setTestByte(testByte);
	}

	public float getTestFloat() {
		return local.getTestFloat();
	}

	public void setTestFloat(float testFloat) {
		local.setTestFloat(testFloat);
	}

	public double getTestDouble() {
		return local.getTestDouble();
	}

	public void setTestDouble(double testDouble) {
		local.setTestDouble(testDouble);
	}

	public String getTestStringLong() {
		return local.getTestStringLong();
	}

	public void setTestStringLong(String testStringLong) {
		local.setTestStringLong(testStringLong);
	}

	public String getTestStringShort() {
		return local.getTestStringShort();
	}

	public void setTestStringShort(String testStringShort) {
		local.setTestStringShort(testStringShort);
	}

	public boolean isTestBool() {
		return local.isTestBool();
	}

	public void setTestBool(boolean testBool) {
		local.setTestBool(testBool);
	}

	public Date getTestDate() {
		return local.getTestDate();
	}

	public void setTestDate(Date testDate) {
		local.setTestDate(testDate);
	}
	

	public boolean isSaveOK() {
		return saveOK;
	}

	public String saveIt() {
		local.save();
		saveOK = true;
		return null;
	}
	
	public String readFirst() {
		local.readFirst();
		return null;
	}
	
	public String doNothing(String str) {
		local.setTestInteger(18);
		return null;
		//return "../index.jsf?faces-redirect=true";
	}
	
	

}
