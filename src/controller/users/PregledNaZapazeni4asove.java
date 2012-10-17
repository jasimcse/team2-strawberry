package controller.users;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import model.Appointment;
import controller.common.ConfigurationProperties;
import controller.common.CurrentEmployee;


@SuppressWarnings("serial")
@ManagedBean(name="pregledNaZapazeni4asove")
@ViewScoped
public class PregledNaZapazeni4asove implements Serializable {

	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private List<Appointment> spisukZapazeni4asove;
	
	private Calendar cal;
	private Calendar now;
	
	public PregledNaZapazeni4asove() {
		now = new GregorianCalendar(TimeZone.getTimeZone("Europe/Sofia"));
		
		// проверка дали сме след работно време; в такъв случай отиваме на следващия ден
		if (now.get(Calendar.HOUR_OF_DAY) >= ConfigurationProperties.getWorkingTimeEndHour()) {
			
			now.add(Calendar.DAY_OF_MONTH, 1);
		}
				
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		cal = (Calendar)now.clone();
	}
	
	public List<Appointment> getSpisukZapazeni4asove() {
		return spisukZapazeni4asove;
	}

	@PostConstruct
	private void readList() {
		spisukZapazeni4asove = Appointment.queryGetForDay(0, 1000, currEmployee.getAutoserviceID(), cal.getTime());
	}

	public void setCurrEmployee(CurrentEmployee currEmployee) {
		this.currEmployee = currEmployee;
	}
	
	public void changeDay(int diff) {
		cal.add(Calendar.DAY_OF_MONTH, diff);
		readList();
	}
	
	public Date getDate() {
		return cal.getTime();
	}
	
	public boolean isPrevDayAllowed() {
		Calendar tempCal = (Calendar)cal.clone();
		tempCal.add(Calendar.DAY_OF_MONTH, -1);
		if (!now.after(tempCal)) {
			return true;
		} else {
			return false;
		}
	}
	
}
