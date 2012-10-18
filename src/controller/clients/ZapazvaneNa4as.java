package controller.clients;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;

import com.google.appengine.api.datastore.Key;

import controller.clients.helpers.Hour;
import controller.clients.helpers.Link;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;
import controller.common.Utils;

import model.Appointment;
import model.Autoservice;


@SuppressWarnings("serial")
@ManagedBean(name="zapazvaneNa4as")
@ViewScoped
public class ZapazvaneNa4as implements Serializable {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currentEmployee;
	
	private transient UIComponent reserveButton;
	
	private Appointment zapazen4as = new Appointment();
	
	private String errorMessage;
	
	private List<Hour> spisukZapazeni4asove;
	private List<Link> linkListPrev;
	private List<Link> linkListNext;

	private Calendar cal;
	private Calendar now;
	
	public ZapazvaneNa4as() {
		now = new GregorianCalendar(TimeZone.getTimeZone("Europe/Sofia"));
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		int min = now.get(Calendar.MINUTE);
		now.add(Calendar.MINUTE, ConfigurationProperties.getAppointmentLenghtMinutes() -
				min % ConfigurationProperties.getAppointmentLenghtMinutes());
		
		// �������� ���� ��� ���� ������� �����; � ����� ������ ������� �� ��������� ���
		if (now.get(Calendar.HOUR_OF_DAY) >= ConfigurationProperties.getWorkingTimeEndHour()) {
			
			now.set(Calendar.HOUR_OF_DAY, ConfigurationProperties.getWorkingTimeStartHour());
			now.set(Calendar.MINUTE, 0);
			now.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		// NOTE: ��������, �� ������� ������ ����� ��� �� ��������� :), �� ����� �� ������ �� �� �������� ��� 
		
		cal = (Calendar)now.clone();
		
		List<Autoservice> al = Autoservice.queryGetAll(0, 1);
		if (al.size() > 0) {
			zapazen4as.setAutoserviceID(al.get(0).getID());
		}
		
		readList();
	}
	
	public String writeIt() {	
		
		String generatedCode = Utils.generateRandomCode(ConfigurationProperties.getAutoGeneratedCodeLength());
		zapazen4as.setIssuedCode(generatedCode);
		zapazen4as.writeToDB();
	
		readList();

		// set the message
		errorMessage = "����� �� ���� ������� �������! ���������� �� ��� �� ������������� � \"" +
		               generatedCode + "\"";
		
		return null;
	}
	
	public void chooseAutoservice() {
		// �� ������ ���� :)
		// ������ ������ AJAX � ���������� ���� �� �� �� ��������� �������
	}
	
	private void readList() {
		DateFormat df = new SimpleDateFormat("HH:mm");
		spisukZapazeni4asove = new ArrayList<Hour>();
		List<Appointment> appointmentList;
		Calendar curr = (Calendar)cal.clone();
		Calendar end = (Calendar)cal.clone();
		
		df.setTimeZone(now.getTimeZone());
		
		curr.set(Calendar.HOUR_OF_DAY, 0);
		if (zapazen4as.getAutoserviceID() == null) {
			appointmentList = new ArrayList<Appointment>();
		} else {
		    appointmentList = Appointment.queryGetForDay(0, 1000, zapazen4as.getAutoserviceID(), curr.getTime());
		}
		
		curr.set(Calendar.HOUR_OF_DAY, ConfigurationProperties.getWorkingTimeStartHour());
		end.set(Calendar.HOUR_OF_DAY, ConfigurationProperties.getWorkingTimeEndHour());

		while (curr.before(end)) {
			Hour h = new Hour();
			h.setText(df.format(curr.getTime()));
			h.setTime(curr.getTime());
			
			if (now.after(curr)) {
				// ����������� ������ ������ ����� �� ����� �������� �����
				h.setDisabled(true);
			} else {
				// ����������� ���� � ����������� ���� ���� �� � �������, ��� � ������� �� �����������
				h.setDisabled(false);
				for (Appointment appointment : appointmentList) {
					if (appointment.getTimestamp().compareTo(curr.getTime()) == 0) {
						h.setDisabled(true);
						break;
					}
				}
			}
			spisukZapazeni4asove.add(h);
			
			curr.add(Calendar.MINUTE, ConfigurationProperties.getAppointmentLenghtMinutes());
		}
		
		generateLinkLists();
	}
	
	public void generateLinkLists() {
		int i;
		Calendar tempCal = (Calendar)cal.clone();
		linkListPrev = new ArrayList<Link>();
		linkListNext = new ArrayList<Link>();
		
		for (i=0; i<5; i++) {
			Link link = new Link();
			link.setParam(-i-1);
			link.setText(String.valueOf(-i-1));
			
			tempCal = (Calendar)cal.clone();
			tempCal.add(Calendar.DAY_OF_MONTH, -i-1);
			if (!now.after(tempCal)) {
				link.setDisabled(false);
			} else {
				link.setDisabled(true);
			}
			linkListPrev.add(0, link);
		}
		
		for (i=0; i<5; i++) {
			Link link = new Link();
			link.setParam(i+1);
			link.setText(String.format("%+d",i+1));
			link.setDisabled(false);
			
			linkListNext.add(link);
		}
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public UIComponent getReserveButton() {
		return reserveButton;
	}

	public void setReserveButton(UIComponent reserveButton) {
		this.reserveButton = reserveButton;
	}

	public void setCurrentEmployee(CurrentEmployee currentEmployee) {
		this.currentEmployee = currentEmployee;
	}

	public List<Hour> getSpisukZapazeni4asove() {
		return spisukZapazeni4asove;
	}

	public void changeDay(int diff) {
		cal.add(Calendar.DAY_OF_MONTH, diff);
		readList();
	}
	
	public void changeMonth(int diff) {
		cal.add(Calendar.MONTH, diff);
		readList();
	}
	
	
	
	public Key getAutoserviceID() {
		return zapazen4as.getAutoserviceID();
	}

	public void setAutoserviceID(Key autoserviceID) {
		zapazen4as.setAutoserviceID(autoserviceID);
	}

	public String getClientName() {
		return zapazen4as.getClientName();
	}

	public void setClientName(String clientName) {
		zapazen4as.setClientName(clientName);
	}

	public String getPhoneNumber() {
		return zapazen4as.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		zapazen4as.setPhoneNumber(phoneNumber);
	}
	
	public Date getDate() {
		return cal.getTime();
	}
	
	public void setDate(Date date) {
		zapazen4as.setTimestamp(date);
	}
	
	public List<Autoservice> getAutoservices() {
		List<Autoservice> auto = Autoservice.queryGetAll(0, 1000);
		return auto;
	}
	
	public List<Link> getLinkListPrev() {
		return linkListPrev;
	}
	
	public List<Link> getLinkListNext() {
		return linkListNext;
	}
	
	public boolean isPrevMonthAllowed() {
		Calendar tempCal = (Calendar)cal.clone();
		tempCal.add(Calendar.MONTH, -1);
		if (!now.after(tempCal)) {
			return true;
		} else {
			return false;
		}
	}
	
}

