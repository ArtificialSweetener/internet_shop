package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Item class represents an item in an order. It contains information about
 * the product, quantity, price, and the order it belongs to. This class
 * implements the Serializable interface to allow objects of this class to be
 * serialized.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private long productId;
	private long orderId;
	private int itemQuantity;
	private double itemPrice;

	public Item() {
	}

	public Item(long id, long productId, long orderId, int itemQuantity, double itemPrice) {
		super();
		this.id = id;
		this.productId = productId;
		this.orderId = orderId;
		this.itemQuantity = itemQuantity;
		this.itemPrice = itemPrice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double d) {
		this.itemPrice = d;
	}
	
	 @Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || this.getClass() != obj.getClass()) return false;
	        Item item = (Item) obj;
	        return Objects.equals(id, item.id) && Objects.equals(productId, item.productId) && Objects.equals(orderId, item.orderId) && Objects.equals(itemQuantity, item.itemQuantity) && Objects.equals(itemPrice, item.itemPrice);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(id, productId, orderId, itemQuantity, itemPrice);
	    }

}
