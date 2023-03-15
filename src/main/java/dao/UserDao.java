package dao;

import java.util.List;
import java.util.Optional;

import models.User;

public interface UserDao extends GenericDao<User> {
	/**
	 * Finds the User object with the specified email address in the database.
	 *
	 * @param email the email address of the User object to be found
	 * @return an Optional containing the User object if found, or an empty Optional
	 *         otherwise
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Gets a list of User objects from the database, starting from the given offset
	 * and returning up to noOfRecords records.
	 *
	 * @param offset      the starting index of the records to return
	 * @param noOfRecords the maximum number of records to return
	 * @return a List of User objects
	 */

	List<User> getAll(int offset, int noOfRecords);

	/**
	 * Gets the number of records.
	 *
	 * @return the number of records
	 */
	int getNoOfRecords();
}
