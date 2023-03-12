package dao;

import java.util.List;
import java.util.Optional;

import models.User;


public interface UserDao extends GenericDao<User> {
	Optional<User> findByEmail(String email);
	List<User> getAll(int offset, int noOfRecords);
	int getNoOfRecords() ;
}
