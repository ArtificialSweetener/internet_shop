package models;

import java.io.Serializable;

public class Cart extends Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int quantityInCart;
	private double totalPrice;

	public Cart() {
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getQuantityInCart() {
		return quantityInCart;
	}

	public void setQuantityInCart(int quantityInCart) {
		this.quantityInCart = quantityInCart;
	}

}
