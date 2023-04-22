package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * The Order class represents an order made by a user. It contains information
 * such as the user id, order date and time, delivery address, phone number, and
 * order status. An order can contain multiple items, which are represented by
 * the Item class.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 * 
 */
public class Order implements Serializable {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Order order = (Order) obj;
        return Objects.equals(id, order.id) &&
               Objects.equals(userId, order.userId) &&
               Objects.equals(orderDate, order.orderDate) &&
               Objects.equals(orderTime, order.orderTime) &&
               Objects.equals(orderAddress, order.orderAddress) &&
               Objects.equals(orderPhone, order.orderPhone) &&
               Objects.equals(orderStatus, order.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, orderDate, orderTime, orderAddress, orderPhone, orderStatus);
    }
    
}
