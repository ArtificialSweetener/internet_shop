package dao;

import java.util.Optional;

import models.UserSalt;

public interface UserSaltDao extends GenericDao<UserSalt> {
	/**
	 * Retrieves the UserSalt with the given user_id from the database.
	 *
	 * @param id the user_id to search for
	 * @return an Optional containing the UserSalt if one was found, or an empty
	 *         Optional if not
	 */
	Optional<UserSalt> getSaltByUserId(Long id);
}
