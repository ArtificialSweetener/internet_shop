package service;

import java.util.List;
import java.util.Optional;

import models.User;

public interface UserService extends GenericService<User>{
	    Optional<User> findByEmail(String email);
	    List<User> getAll(int offset, int noOfRecords) ;
	    int getNoOfRecords() ;
}
