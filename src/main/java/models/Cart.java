package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a shopping cart that stores a map of products and their
 * corresponding quantities. Provides methods to add, remove, and update
 * products in the cart, as well as retrieve various information about the
 * contents of the cart.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 *
 */
public class Cart implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Map<Product, Integer> products = new HashMap<>();

	/**
	 * Creates a new empty Cart instance.
	 */
	public Cart() {
	}

	/**
	 * Adds a product to the cart with the specified quantity.
	 * 
	 * @param product  the Product instance to add to the cart
	 * @param quantity the quantity of the product to add
	 */
	public void addProduct(Product product, int quantity) {
		products.put(product, quantity);
	}

	/**
	 * Removes a product from the cart.
	 * 
	 * @param product the Product instance to remove from the cart
	 */
	public void removeProduct(Product product) {
		products.remove(product);
	}

	/**
	 * Removes a product from the cart by its ID.
	 * 
	 * @param productId the ID of the product to remove from the cart
	 */
	public void removeProductById(long productId) {
		for (Map.Entry<Product, Integer> entry : products.entrySet()) {
			Product product = entry.getKey();
			if (product.getProductId() == productId) {
				products.remove(product);
				return;
			}
		}
	}

	/**
	 * Updates the quantity of a product in the cart.
	 * 
	 * @param product  the Product instance to update
	 * @param quantity the new quantity of the product
	 */
	public void updateQuantity(Product product, int quantity) {
		products.put(product, quantity);
	}

	/**
	 * Retrieves the quantity of a product in the cart.
	 * 
	 * @param product the Product instance to retrieve the quantity of
	 * @return the quantity of the product in the cart, or 0 if it is not present
	 */
	public int getProductQuantity(Product product) {
		Integer quantity = products.get(product);
		return quantity != null ? quantity : 0;
	}

	/**
	 * Checks if a product with the specified ID is present in the cart.
	 * 
	 * @param productId the ID of the product to check for
	 * @return true if a product with the specified ID is present in the cart, false
	 *         otherwise
	 */
	public boolean containsProductById(long productId) {
		for (Product product : products.keySet()) {
			if (product.getProductId() == productId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculates the total price of a product in the cart.
	 * 
	 * @param product the Product instance to calculate the total price of
	 * @return the total price of the product in the cart
	 */
	public double getProductTotalPrice(Product product) {
		int quantity = getProductQuantity(product);
		return product.getProductPrice() * quantity;
	}

	/**
	 * Calculates the total price of all products in the cart.
	 * 
	 * @return the total price of all products in the cart
	 */
	public double getTotalPrice() {
		double totalPrice = 0.0;
		for (Map.Entry<Product, Integer> entry : products.entrySet()) {
			totalPrice += getProductTotalPrice(entry.getKey());
		}
		return totalPrice;
	}

	/**
	 * Returns the map of products in this cart along with their corresponding
	 * quantities.
	 *
	 * @return the map of products and their quantities in this cart
	 */
	public Map<Product, Integer> getProducts() {
		return products;
	}

	/**
	 * Returns an optional Product object with the specified ID from this cart, if
	 * it exists.
	 *
	 * @param productId the ID of the product to search for in this cart
	 * @return an optional Product object with the specified ID, or an empty
	 *         optional if it does not exist in this cart
	 */
	public Optional<Product> getProductById(long productId) {
		for (Product product : products.keySet()) {
			if (product.getProductId() == productId) {
				return Optional.of(product);
			}
		}
		return Optional.empty();
	}

	/**
	 * Sets the map of products in this cart to the specified map.
	 *
	 * @param products the map of products and their quantities to set for this cart
	 */
	public void setProducts(Map<Product, Integer> products) {
		this.products = products;
	}
}
