package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * The Product class represents a product in the inventory of a store. It
 * contains information such as the product's ID, name, description, color,
 * category, price, quantity, date, time, photo, photo name, and a flag
 * indicating whether the product has been marked as deleted. It also includes
 * methods for accessing and modifying these attributes, as well as a method for
 * retrieving the product's date and time as a LocalDateTime object.
 * Additionally, it implements the Serializable interface, allowing instances of
 * this class to be easily serialized and deserialized.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private long productId;
	private String productName;
	private String productDescription;
	private long colorId;
	private long categoryId;
	private double productPrice;
	private int productQuantity;
	private LocalDate productDate;
	private LocalTime productTime;

	private byte[] productPhoto;
	private String productPhotoName;
	private int isdeleted;
	private String base64Image;

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public Product() {
	}

	public Product(String productName, String productDescription, long colorId, long categoryId, double productPrice,
			int productQuantity, LocalDate productDate, LocalTime productTime, byte[] productPhoto,
			String productPhotoName, int isdeleted) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.colorId = colorId;
		this.categoryId = categoryId;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.productDate = productDate;
		this.productTime = productTime;
		this.productPhoto = productPhoto;
		this.productPhotoName = productPhotoName;
	}

	public Product(Long id, String productName, String productDescription, long colorId, long categoryId,
			double productPrice, int productQuantity, LocalDate productDate, LocalTime productTime, byte[] productPhoto,
			String productPhotoName, int isdeleted) {
		this.productId = id;
		this.productName = productName;
		this.productDescription = productDescription;
		this.colorId = colorId;
		this.categoryId = categoryId;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.productDate = productDate;
		this.productTime = productTime;
		this.productPhoto = productPhoto;
		this.productPhotoName = productPhotoName;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public long getColorId() {
		return colorId;
	}

	public void setColorId(long colorId) {
		this.colorId = colorId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public LocalDate getProductDate() {
		return productDate;
	}

	public void setProductDate(LocalDate productDate) {
		this.productDate = productDate;
	}

	public LocalTime getProductTime() {
		return productTime;
	}

	public void setProductTime(LocalTime productTime) {
		this.productTime = productTime;
	}

	public byte[] getProductPhoto() {
		return productPhoto;
	}

	public void setProductPhoto(byte[] productPhoto) {
		this.productPhoto = productPhoto;
	}

	public String getProductPhotoName() {
		return productPhotoName;
	}

	public void setProductPhotoName(String productPhotoName) {
		this.productPhotoName = productPhotoName;
	}

	public int getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(int isdeleted) {
		this.isdeleted = isdeleted;
	}

	public LocalDateTime getDateTime() {
		LocalDateTime fromDateAndTime = LocalDateTime.of(this.productDate, this.productTime);
		return fromDateAndTime;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productDescription="
				+ productDescription + ", colorId=" + colorId + ", categoryId=" + categoryId + ", productPrice="
				+ productPrice + ", productQuantity=" + productQuantity + ", productDate=" + productDate
				+ ", productTime=" + productTime + ", productPhoto=" + Arrays.toString(productPhoto)
				+ ", productPhotoName=" + productPhotoName + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Product)) {
			return false;
		}
		Product product = (Product) obj;
		return Objects.equals(productId, product.productId) && Objects.equals(productName, product.productName)
				&& Objects.equals(productDescription, product.productDescription)
				&& Objects.equals(colorId, product.colorId) && Objects.equals(categoryId, product.categoryId)
				&& Objects.equals(productPrice, product.productPrice)
				&& Objects.equals(productQuantity, product.productQuantity)
				&& Objects.equals(productDate, product.productDate) && Objects.equals(productTime, product.productTime)
				&& Arrays.equals(productPhoto, product.productPhoto)
				&& Objects.equals(productPhotoName, product.productPhotoName)
				&& Objects.equals(isdeleted, product.isdeleted);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, productName, productDescription, colorId, categoryId, productPrice,
				productQuantity, productDate, productTime, productPhoto, productPhotoName, isdeleted);
	}

}
