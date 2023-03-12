package models;

import java.io.Serializable;

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

}
