package models;

import java.io.Serializable;

public class UserSalt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userSaltId;
	private Long userId;
	private String salt;

	public UserSalt(Long userSaltId, Long userId, String salt) {
		this.userSaltId = userSaltId;
		this.userId = userId;
		this.salt = salt;
	}

	public UserSalt(Long userId, String salt) {
		this.userId = userId;
		this.salt = salt;
	}

	public UserSalt() {
	}

	public Long getUserSaltId() {
		return userSaltId;
	}

	public void setUserSaltId(Long userSaltId) {
		this.userSaltId = userSaltId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
