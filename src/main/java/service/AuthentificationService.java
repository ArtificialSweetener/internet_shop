package service;

import exception.AuthentificationException;
import models.User;

public interface AuthentificationService {
	User login(String email, String password) throws AuthentificationException;
}

