package models;

import java.io.Serializable;

/**
 * UserSalt represents a user's salt, which is used in password hashing to add
 * an extra layer of security. This class is Serializable, meaning that it can
 * be saved and restored across different Java Virtual Machines.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
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
