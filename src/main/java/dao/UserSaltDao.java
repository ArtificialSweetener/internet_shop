package dao;

import java.util.Optional;

import models.UserSalt;

public interface UserSaltDao extends GenericDao<UserSalt>{
	Optional<UserSalt> getSaltByUserId(Long id) ;
}
