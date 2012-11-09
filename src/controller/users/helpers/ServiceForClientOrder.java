package controller.users.helpers;


import java.io.Serializable;

import model.ClientOrderService;

/**
 * 
 * използва се за таблицата с услуги които ще се включат към клиентака поръчка
 *
 */
@SuppressWarnings("serial")
public class ServiceForClientOrder   implements Serializable {
	
	private ClientOrderService clService;
	private double serviceDuration;
	private double fullPrice;
	
	private boolean editing;

	public ClientOrderService getClService() {
		return clService;
	}

	public void setClService(ClientOrderService clService) {
		this.clService = clService;
	}

	public double getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(double serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public String getEmployeeNames()
	{
		if(clService.getEmployee() == null )
			return "";
		
		return clService.getEmployee().getName() + " " + clService.getEmployee().getFamily();
	}
	
	public boolean isEditing() {
		return editing;
	}
	
	public boolean isDone() {
		if (clService.getEmployee() == null )
			return false;
		return true;
	}
	
	public void recalculateFullPrice() {
		fullPrice = this.serviceDuration * clService.getPriceHour();
	}
	
}


