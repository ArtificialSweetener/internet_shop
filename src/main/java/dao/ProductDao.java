package dao;

import java.util.List;
import java.util.Optional;
//import models.Cart;
import models.Product;

/**
 * 
 * The {@code ProductDao} interface represents a Data Access Object for the
 * {@link models.Product} model. It extends the {@link dao.GenericDao}
 * interface, inheriting its CRUD operations for manipulating product records in
 * the database.
 * 
 * @see dao.GenericDao
 * @see models.Product
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 *
 */
public interface ProductDao extends GenericDao<Product> {
	/**
	 * Retrieves a subset of all products from the database based on the given
	 * offset and number of records.
	 * 
	 * @param offset      the starting index of the subset
	 * @param noOfRecords the number of records to retrieve
	 * @return a list of products from the database
	 * 
	 */
	List<Product> getAll(int offset, int noOfRecords);

	/**
	 * Returns the total number of product records in the database.
	 * 
	 * @return the total number of product records in the database
	 */
	int getNoOfRecords();

	/**
	 * Returns the minimum price among all products in the database.
	 * 
	 * @return the minimum price among all products in the database
	 */
	double getMinPrice();

	/**
	 * Returns the maximum price among all products in the database.
	 * 
	 * @return the maximum price among all products in the database
	 */
	double getMaxPrice();

	/**
	 * Retrieves a subset of products from the database based on the given category,
	 * color, price range, sort criteria, offset, and number of records.
	 * 
	 * @param categoryId  the ID of the category to filter by, or null to not filter
	 *                    by category
	 * 
	 * @param colorId     the ID of the color to filter by, or null to not filter by
	 *                    color
	 * 
	 * @param min         the minimum price of products to retrieve, or null to not
	 *                    filter by minimum price
	 * 
	 * @param max         the maximum price of products to retrieve, or null to not
	 *                    filter by maximum price
	 * @param sort        the sort criteria to use, or null to not sort
	 * 
	 * @param offset      the starting index of the subset
	 * 
	 * @param noOfRecords the number of records to retrieve
	 * 
	 * @return a list of products from the database that match the given criteria
	 */
	List<Product> getAllBy(String categoryId, String colorId, String min, String max, String sort, int offset,
			int noOfRecords);

	/**
	 * Retrieves a product with the specified ID from the database, checking that it
	 * has not been soft deleted.
	 * 
	 * @param id the ID of the product to retrieve
	 * @return an optional containing the retrieved product, or empty if not found
	 *         or soft deleted
	 */
	Optional<Product> getNoDeleteCheck(Long id);
}
