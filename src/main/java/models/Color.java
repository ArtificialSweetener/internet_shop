package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Color class represents a color entity, which has a unique identifier and
 * a name. It implements the Serializable interface to enable serialization and
 * deserialization of the object.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class Color implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long colorId;
	private String colorName;

	public Color() {
	}

	public Color(String colorName) {
		this.colorName = colorName;
	}

	public Color(long colorId, String colorName) {
		super();
		this.colorId = colorId;
		this.colorName = colorName;
	}

	public long getColorId() {
		return colorId;
	}

	public void setColorId(long colorId) {
		this.colorId = colorId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	@Override
	public String toString() {
		return "Color [colorId=" + colorId + ", colorName=" + colorName + "]";
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == this) {
	        return true;
	    }
	    if (obj == null || this.getClass() != obj.getClass()) {
	        return false;
	    }
	    Color other = (Color) obj;
	    return Objects.equals(colorId, other.colorId) && Objects.equals(colorName, other.colorName);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(colorId, colorName);
	}
}
