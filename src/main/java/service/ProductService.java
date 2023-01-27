package service;

import java.util.List;
import java.util.Optional;
import models.Cart;
import models.Product;

public interface ProductService extends GenericService<Product> {
	double getMinPrice();

	double getMaxPrice();

	//List<Product> getAllByCategories(List<Product> productList, List<Long> categoryIdList);

	//List<Product> getAllByColors(List<Product> productList, List<Long> colorIdList);

	//List<Product> getAllByPriceRange(List<Product> productList, Integer min, Integer max);

	List<Cart> getCartProduct(List<Cart> cartList);

	List<Product> getAll(int offset, int noOfRecords);

	int getNoOfRecords();

	Optional<List<Product>> getAllBy(String categoryId, String colorId, String min, String max, String sort, int offset,
			int noOfRecords);
}
