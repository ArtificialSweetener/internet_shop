package service.impl;

import java.util.Optional;

import exception.AuthentificationException;
import models.User;
import service.AuthentificationService;
import service.UserService;

public class AuthentificationServiceImpl implements AuthentificationService {
	private UserService userService; 
	
	public AuthentificationServiceImpl(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@Override
	public User login(String email, String password) throws AuthentificationException {
		// TODO Auto-generated method stub
		//logger info
		Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty() || !(optionalUser.get().getUserPassword().equals(password))) {
            throw new AuthentificationException("Wrong login or password");
        }
        return optionalUser.get();
	}
	

}
