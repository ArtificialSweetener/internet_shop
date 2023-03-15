package models;

import java.io.Serializable;

/**
 * The Cart class represents a shopping cart for a Product in a simple
 * e-commerce system.
 * 
 * It extends the Product class and implements the Serializable interface for
 * object serialization.
 * 
 * The Cart class has properties for the quantity of a product in the cart and
 * the total price of all products in the cart.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 * @see Product
 * @see Serializable
 * 
 */
public class Cart extends Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private int quantityInCart;
	private double totalPrice;

	public Cart() {
	}

	/**
	 * Returns the total price of all products in the cart.
	 * 
	 * @return The total price of all products in the cart.
	 */
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * Returns the quantity of a product in the cart.
	 * 
	 * @return The quantity of a product in the cart.
	 */
	public int getQuantityInCart() {
		return quantityInCart;
	}

	public void setQuantityInCart(int quantityInCart) {
		this.quantityInCart = quantityInCart;
	}

}
