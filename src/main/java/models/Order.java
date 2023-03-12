package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long userId;
	private LocalDate orderDate;
	private LocalTime orderTime;
	private String orderAddress;
	private String orderPhone;
	private String orderStatus;

	public Order() {
	}

	public Order(long id, long userId, LocalDate orderDate, LocalTime orderTime, String orderAddress, String orderPhone,
			String orderStatus) {
		super();
		this.id = id;
		this.userId = userId;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.orderAddress = orderAddress;
		this.orderPhone = orderPhone;
		this.orderStatus = orderStatus;
	}

	public long getOrderId() {
		return id;
	}

	public void setOrderId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public LocalTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalTime orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
