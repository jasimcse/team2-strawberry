package controller.users.helpers;

import java.io.Serializable;

import model.WarehouseOrderPartDelivery;


/**
 * 
 * използва се за третата таблица на страницата за приемане на резервни части
 *
 */
@SuppressWarnings("serial")
public class SparePartDelivered implements Serializable {

	private WarehouseOrderPartDelivery warehouseOrderPartDelivery;
	private double quantityNotDelivered;
	private double fullPrice;
	
	private boolean editing;
	
	public double getQuantityNotDelivered() {
		return quantityNotDelivered;
	}
	
	public void setQuantityNotDelivered(double quantityNotDelivered) {
		this.quantityNotDelivered = quantityNotDelivered;
	}
	
	public WarehouseOrderPartDelivery getWarehouseOrderPartDelivery() {
		return warehouseOrderPartDelivery;
	}

	public void setWarehouseOrderPartDelivery(WarehouseOrderPartDelivery warehouseOrderPartDelivery) {
		this.warehouseOrderPartDelivery = warehouseOrderPartDelivery;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public boolean isEditing() {
		return editing;
	}
	
	public void recalculateFullPrice() {
		fullPrice = warehouseOrderPartDelivery.getPrice() * warehouseOrderPartDelivery.getQuantity();
	}
	
	public void toggleEditing() {
		if (editing) {
			if ((warehouseOrderPartDelivery.getQuantity() > quantityNotDelivered) ||
				(warehouseOrderPartDelivery.getQuantity() <= 0)) {
				warehouseOrderPartDelivery.setQuantity(quantityNotDelivered);
			}
			
			recalculateFullPrice();
		}
		
		editing = !editing;
	}
}
