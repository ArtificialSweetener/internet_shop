package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Category class represents a category of products in a simple e-commerce
 * system. It implements the Serializable interface for object serialization.
 * The Category class has properties for the category ID, title, description,
 * and a list of products in the category.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 * @see Serializable
 * @see Product
 */
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long categoryId;
	private String categoryTitle;
	private String categoryDescription;
	private List<Product> products = new ArrayList<>();

	public Category() {
	}

	public Category(long categoryId, String categoryTitle, String categoryDescription) {
		this.categoryId = categoryId;
		this.categoryTitle = categoryTitle;
		this.categoryDescription = categoryDescription;
	}

	public Category(String categoryTitle, String categoryDescription) {
		this.categoryTitle = categoryTitle;
		this.categoryDescription = categoryDescription;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryTitle=" + categoryTitle + ", categoryDescription="
				+ categoryDescription + "]";
	}

}
