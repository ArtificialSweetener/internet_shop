package dao;

import java.util.List;
import java.util.Optional;
import models.Cart;
import models.Product;

public interface ProductDao extends GenericDao<Product> {

	List<Cart> getCartProduct(List<Cart> cartList);

	List<Product> getAll(int offset, int noOfRecords);

	int getNoOfRecords();

	double getMinPrice();

	double getMaxPrice();

	List<Product> getAllBy(String categoryId, String colorId, String min, String max, String sort, int offset,
			int noOfRecords);

	Optional<Product> getNoDeleteCheck(Long id);
}
