package controller.users.helpers;

import java.io.Serializable;

import model.SparePart;

/**
 * 
 * използва се за първата таблица на страницата за поръчка на резервни части
 *
 */
@SuppressWarnings("serial")
public class SparePartNeeded implements Serializable {

	private SparePart sparePart;
	private double quantityNeeded;
	private boolean availableAtCurrentSupplier;
	
	public SparePart getSparePart() {
		return sparePart;
	}
	
	public void setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}
	
	public double getQuantityNeeded() {
		return quantityNeeded;
	}
	
	public void setQuantityNeeded(double quantityNeeded) {
		this.quantityNeeded = quantityNeeded;
	}

	public boolean isAvailableAtCurrentSupplier() {
		return availableAtCurrentSupplier;
	}

	public void setAvailableAtCurrentSupplier(boolean availableAtCurrentSupplier) {
		this.availableAtCurrentSupplier = availableAtCurrentSupplier;
	}
	
}
