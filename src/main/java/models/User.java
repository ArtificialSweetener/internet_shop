package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The User class represents a user object that contains information about a
 * user such as their ID, name, surname, email, password, user type and whether
 * or not the user is blocked. This class provides getters and setters for all
 * attributes, as well as constructors to initialize an object of this class.
 * The class also overrides the toString method to provide a string
 * representation of the object.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 * 
 */
public class User implements Serializable {

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
	

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return Objects.equals(userId, user.userId)
                && Objects.equals(userName, user.userName)
                && Objects.equals(userSurname, user.userSurname)
                && Objects.equals(userEmail, user.userEmail)
                && Objects.equals(userPassword, user.userPassword)
                && Objects.equals(userType, user.userType)
                && is_bloked == user.is_bloked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userSurname, userEmail, userPassword, userType, is_bloked);
    }
}
