package dao;

import java.util.List;
import models.Order;

/**
 * The OrderDao interface provides methods to perform CRUD operations on the
 * Order entity and retrieve all orders, orders by user ID, and the number of
 * records in the orders table. import models.Order;
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 *
 */
public interface OrderDao extends GenericDao<Order> {
	/**
	 * Returns a list of orders belonging to the user with the specified ID,
	 * starting from the specified offset and returning noOfRecords records.
	 * 
	 * @param id          the ID of the user to get orders for
	 * @param offset      the starting offset of the records to return
	 * @param noOfRecords the number of records to return
	 * @return a list of orders belonging to the user with the specified ID
	 */
	List<Order> getAllOrdersByUserId(Long id, int offset, int noOfRecords);

	/**
	 * Returns a list of all orders, starting from the specified offset and
	 * returning noOfRecords records.
	 * 
	 * @param offset      the starting offset of the records to return
	 * @param noOfRecords the number of records to return
	 * @return a list of all orders
	 */
	List<Order> getAll(int offset, int noOfRecords);

	/**
	 * Returns the number of records in the orders table.
	 * 
	 * @return the number of records in the orders table
	 */
	public int getNoOfRecords();
}
