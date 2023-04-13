package dao;

import java.util.List;
import models.Item;

/**
 * This interface represents a data access object for the {@link models.Item}
 * model. It extends the {@link dao.GenericDao} interface to inherit generic
 * CRUD methods.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public interface ItemDao extends GenericDao<Item> {
	/**
	 * Retrieves all items associated with a given order ID.
	 * 
	 * @param id the ID of the order to retrieve items for
	 * @return a list of all items associated with the given order ID
	 */
	List<Item> getAllByOrderId(Long id);

	/**
	 * Retrieves a subset of items associated with a given order ID, based on the
	 * specified offset and limit.
	 * 
	 * @param id          the ID of the order to retrieve items for
	 * @param offset      the offset from which to start retrieving items
	 * @param noOfRecords the maximum number of records to retrieve
	 * @return a list of items associated with the given order ID, based on the
	 *         specified offset and limit
	 */
	List<Item> getAllByOrderId(Long id, int offset, int noOfRecords);

	/**
	 * Returns the total number of records for items associated with a given order
	 * ID.
	 * 
	 * @return the total number of records for items associated with a given order
	 *         ID
	 */
	public int getNoOfRecords();
}
