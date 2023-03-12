package models;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private long userId;
	private String userName;
	private String userSurname;
	private String userEmail;
	private String userPassword;
	private String userType;
	private boolean is_bloked;
	

	public User(long userId, String userName, String userSurname, String userEmail, String userPassword,
			String userType) {
		this.userId = userId;
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userType = userType;
	}

	public User(String userName, String userSurname, String userEmail, String userPassword, String userType) {
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userType = userType;
	}

	public User() {

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public boolean isIs_bloked() {
		return is_bloked;
	}

	public void setIs_bloked(boolean is_bloked) {
		this.is_bloked = is_bloked;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userSurname=" + userSurname + ", userEmail="
				+ userEmail + ", userPassword=" + userPassword + ", userType=" + userType + "]";
	}
}
