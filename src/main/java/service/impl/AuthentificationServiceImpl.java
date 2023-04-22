package service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exception.AuthentificationException;
import models.User;
import models.UserSalt;
import service.AuthentificationService;
import service.UserSaltService;
import service.UserService;

public class AuthentificationServiceImpl implements AuthentificationService {
	private UserService userService;
	private UserSaltService userSaltService;
	private static final Logger logger = LogManager.getLogger(AuthentificationServiceImpl.class);
	
	public AuthentificationServiceImpl(UserService userService, UserSaltService userSaltService) {
		super();
		this.userService = userService;
		this.userSaltService = userSaltService;
	}

	@Override
	public User login(String email, String password) throws AuthentificationException {
		
		Optional<User> optionalUser = userService.findByEmail(email);
		
		if (optionalUser.isEmpty()) {
			logger.error("AuthentificationException will be thrown while executing login() method of the AuthentificationServiceImpl class");
			throw new AuthentificationException("Invalid user (can´t find user  by user email).");
		} else {

			Optional<UserSalt> storedSaltStringOpt = userSaltService.getSaltByUserId(optionalUser.get().getUserId());

			if (storedSaltStringOpt.isEmpty()) {
				logger.error("AuthentificationException will be thrown while executing login() method of the AuthentificationServiceImpl class");
				throw new AuthentificationException("Invalid user (can´t find user salt by user id).");
			} else {

				String storedSaltString = storedSaltStringOpt.get().getSalt();// retrieve saltString from the database

				String storedHashedPasswordString = optionalUser.get().getUserPassword();// retrieve
																							// hashedPasswordString from
																							// // the database
				byte[] storedSalt = Base64.getDecoder().decode(storedSaltString);

				byte[] storedHashedPassword = Base64.getDecoder().decode(storedHashedPasswordString);

				MessageDigest loginMd;
				try {
					loginMd = MessageDigest.getInstance("SHA-256");
					loginMd.update(storedSalt);
					
					byte[] userHashedPassword = loginMd.digest(password.getBytes(StandardCharsets.UTF_8));
					
					if (Arrays.equals(userHashedPassword, storedHashedPassword)) {
						logger.info("Login successful");
						return optionalUser.get();
					} else {
						logger.info("Login failed");
						logger.error("AuthentificationException will be thrown while executing login() method of the AuthentificationServiceImpl class");
						throw new AuthentificationException("Wrong login or password");
					}
				} catch (NoSuchAlgorithmException e) {
					logger.error("NoSuchAlgorithmException was thrown while executing login() method of the AuthentificationServiceImpl class");
					logger.error("AuthentificationException will be thrown while executing login() method of the AuthentificationServiceImpl class");
					throw new AuthentificationException("Could not login due to NoSuchAlgorithmException" + e);
				}
			}
		}
	}
}
