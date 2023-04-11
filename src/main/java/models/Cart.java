package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Cart implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Map<Product, Integer> products = new HashMap<>();

	public Cart() {
	}

	public void addProduct(Product product, int quantity) {
		products.put(product, quantity);
	}

	public void removeProduct(Product product) {
		products.remove(product);
	}

	public void removeProductById(long productId) {
		for (Map.Entry<Product, Integer> entry : products.entrySet()) {
			Product product = entry.getKey();
			if (product.getProductId() == productId) {
				products.remove(product);
				return;
			}
		}
	}

	public void updateQuantity(Product product, int quantity) {
		products.put(product, quantity);
	}

	public int getProductQuantity(Product product) {
		Integer quantity = products.get(product);
		return quantity != null ? quantity : 0;
	}

	public boolean containsProductById(long productId) {
		for (Product product : products.keySet()) {
			if (product.getProductId() == productId) {
				return true;
			}
		}
		return false;
	}

	public double getProductTotalPrice(Product product) {
		int quantity = getProductQuantity(product);
		return product.getProductPrice() * quantity;
	}

	public double getTotalPrice() {
		double totalPrice = 0.0;
		for (Map.Entry<Product, Integer> entry : products.entrySet()) {
			totalPrice += getProductTotalPrice(entry.getKey());
		}
		return totalPrice;
	}

	public Map<Product, Integer> getProducts() {
		return products;
	}

	public Optional<Product> getProductById(long productId) {
		for (Product product : products.keySet()) {
			if (product.getProductId() == productId) {
				return Optional.of(product);
			}
		}
		return Optional.empty();
	}

	public void setProducts(Map<Product, Integer> products) {
		this.products = products;
	}
}
