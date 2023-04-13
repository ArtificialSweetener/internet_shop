package service;

import java.util.List;
import java.util.Optional;
import models.Product;

public interface ProductService extends GenericService<Product> {
	double getMinPrice();

	double getMaxPrice();

	List<Product> getAll(int offset, int noOfRecords);

	int getNoOfRecords();

	List<Product> getAllBy(String categoryId, String colorId, String min, String max, String sort, int offset,
			int noOfRecords);

	Optional<Product> getNoDeleteCheck(Long id);
}
