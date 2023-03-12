package models;

import java.io.Serializable;

public class Item implements Serializable{
	/**
	 * 
	 */
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

}
