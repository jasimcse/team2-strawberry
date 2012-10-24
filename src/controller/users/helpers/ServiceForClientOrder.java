package controller.users.helpers;


import java.io.Serializable;

import model.ClientOrderService;

/**
 * 
 * �������� �� �� ��������� � ������ ����� �� �� ������� ��� ��������� �������
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

	public boolean isEditing() {
		return editing;
	}
	
	public void recalculateFullPrice() {
		fullPrice = this.serviceDuration * clService.getPriceHour();
	}
	

	public void toggleEditing() {
		if (editing) {
			recalculateFullPrice();
		}
		
		editing = !editing;
	}
	
}


