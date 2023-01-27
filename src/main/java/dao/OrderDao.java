package dao;

import java.util.List;
import java.util.Optional;

import models.Order;

public interface OrderDao extends GenericDao<Order>{
	Optional<List<Order>> getAllOrdersByUserId(Long id, int offset, int noOfRecords);
	List<Order> getAll(int offset, int noOfRecords);
	public int getNoOfRecords();
}
