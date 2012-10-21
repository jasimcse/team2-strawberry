package controller.users.helpers;

import java.io.Serializable;

import model.WarehouseOrderPart;

/**
 * 
 * използва се за втората таблица на страницата за поръчка на резервни части
 *
 */
@SuppressWarnings("serial")
public class SparePartForOrder implements Serializable {
	
	private WarehouseOrderPart warehouseOrderPart;
	private double quantityAvailable;
	private double quantityNeeded;
	private double supplierPrice;
	private double fullPrice;
	
	private boolean editing;
	
	public double getQuantityAvailable() {
		return quantityAvailable;
	}
	
	public void setQuantityAvailable(double quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
	public double getQuantityNeeded() {
		return quantityNeeded;
	}
	
	public void setQuantityNeeded(double quantityNeeded) {
		this.quantityNeeded = quantityNeeded;
	}
	
	public double getSupplierPrice() {
		return supplierPrice;
	}
	
	public void setSupplierPrice(double supplierPrice) {
		this.supplierPrice = supplierPrice;
	}
	
	public WarehouseOrderPart getWarehouseOrderPart() {
		return warehouseOrderPart;
	}

	public void setWarehouseOrderPart(WarehouseOrderPart warehouseOrderPart) {
		this.warehouseOrderPart = warehouseOrderPart;
	}

	public double getFullPrice() {
		return fullPrice;
	}

	public boolean isEditing() {
		return editing;
	}
	
	public void recalculateFullPrice() {
		fullPrice = this.supplierPrice * warehouseOrderPart.getOrderedQuantity();
	}
	
	public void toggleEditing() {
		if (editing) {
			if (warehouseOrderPart.getOrderedQuantity() < quantityNeeded) {
				warehouseOrderPart.setOrderedQuantity(quantityNeeded);
			}
			
			recalculateFullPrice();
		}
		
		editing = !editing;
	}
	
}
