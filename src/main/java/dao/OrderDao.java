package dao;

import java.util.List;
import models.Order;

public interface OrderDao extends GenericDao<Order> {
	List<Order> getAllOrdersByUserId(Long id, int offset, int noOfRecords);

	List<Order> getAll(int offset, int noOfRecords);

	public int getNoOfRecords();
}
