package service;

import java.util.List;
import models.Order;

public interface OrderService extends GenericService<Order> {
	List<Order> getAllOrdersByUserId(Long id, int offset, int noOfRecords);
	List<Order> getAll(int offset, int noOfRecords);
	 int getNoOfRecords() ;
}
