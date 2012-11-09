package controller.users.helpers;



import java.io.Serializable;

import model.ClientOrderPart;

/**
 * 
 * използва се за таблиците за резервни части които ще се вложат в клиентска поръчка
 *
 */
@SuppressWarnings("serial")
public class SparePartForClientOrder  implements Serializable {
	
	private ClientOrderPart clPart;
	private double quantityAvailable;
	private double fullPrice;
	private double quantityReserved;
	private double quantityUsed = 0;
	private boolean editing;
	
	public double getQuantityAvailable() {
		return quantityAvailable;
	}
	
	public void setQuantityAvailable(double quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
	public ClientOrderPart getClPart() {
		return clPart;
	}

	public void setClPart(ClientOrderPart clPart) {
		this.clPart = clPart;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public double getQuantityReserved() {
		return quantityReserved;
	}

	public void setQuantityReserved(double quantityReserved) {
		this.quantityReserved = quantityReserved;
	}

	public double getQuantityUsed() {
		return quantityUsed;
	}

	public void setQuantityUsed(double quantityUsed) {
		this.quantityUsed = quantityUsed;
	}

	public boolean isEditing() {
		return editing;
	}
	
	public boolean isUsed() {
		if (quantityUsed == 0 )
			return false;
		return true;
	}
	
	public void recalculateFullPrice() {
		fullPrice = this.clPart.getPriceUnit() * this.clPart.getQuantity();
	}
	
	
	public void toggleEditing() {
		if (editing) {
			recalculateFullPrice();
		}
		
		editing = !editing;
	}
	
}



