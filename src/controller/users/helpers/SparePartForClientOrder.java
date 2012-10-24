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

	public boolean isEditing() {
		return editing;
	}
	
	public void recalculateFullPrice() {
		fullPrice = clPart.getPriceUnit() * clPart.getQuantity();
	}
	
	//TODO:
	public void toggleEditing() {
		if (editing) {
			recalculateFullPrice();
		}
		
		editing = !editing;
	}
	
}



