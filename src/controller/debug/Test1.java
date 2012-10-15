package controller.debug;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.Date;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import controller.serverCommunication.SOAPServer.ElectronicShopInterface;

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
	
	public String doNothing() {
		URL url;
		QName qualifiedName;
		try {
			url = new URL("http://localhost:8888/wsdl/ElectronicShopService.wsdl");
			qualifiedName = new QName("http://serverCommunication.controller/", "ElectronicShopService");
			Service service = Service.create(url, qualifiedName);
			ElectronicShopInterface eshop = service.getPort(ElectronicShopInterface.class);
			local.setTestBool(eshop.createOrUpdateWarrantyConditions("electronicShop1", "testID", 11, 9999, "eha eha"));
			local.setTestStringShort(eshop.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		return null;
		//return "../index.jsf?faces-redirect=true";
	}
	
	public String getSecurityProviders() {
		StringBuilder sb = new StringBuilder();
		Provider []p = Security.getProviders();

		for (Provider provider : p) {
			sb.append(provider.getName()).
			append("   ").
			append(provider.getVersion()).
			append("   ").
			append(provider.getInfo()).
			append("<br>");
		}
		
		Set<String> s = Security.getAlgorithms("MessageDigest");
		for (String string : s) {
			sb.append(string).
			append("   ");
		}
		
		sb.append(Integer.toBinaryString(7)).
		append("   ").
		append(Integer.toOctalString(15)).
		append("   ").
		append(Integer.toHexString(17));
		
		return sb.toString();
	}

}
