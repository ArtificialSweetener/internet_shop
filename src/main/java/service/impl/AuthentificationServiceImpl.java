package service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import exception.AuthentificationException;
import models.User;
import models.UserSalt;
import service.AuthentificationService;
import service.UserSaltService;
import service.UserService;

public class AuthentificationServiceImpl implements AuthentificationService {
	private UserService userService;
	private UserSaltService userSaltService;

	public AuthentificationServiceImpl(UserService userService, UserSaltService userSaltService) {
		super();
		this.userService = userService;
		this.userSaltService = userSaltService;
	}

	@Override
	public User login(String email, String password) throws AuthentificationException {
		System.out.println(email);
		System.out.println(password);

		Optional<User> optionalUser = userService.findByEmail(email);

		if (optionalUser.isEmpty()) {
			throw new AuthentificationException("Invalid user (can´t find user  by user email).");
		} else {

			Optional<UserSalt> storedSaltStringOpt = userSaltService.getSaltByUserId(optionalUser.get().getUserId());

			if (storedSaltStringOpt.isEmpty()) {
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
					System.out.println(userHashedPassword);
					System.out.println(storedHashedPassword);
					if (Arrays.equals(userHashedPassword, storedHashedPassword)) {
						// login successful
						return optionalUser.get();
					} else {
						throw new AuthentificationException("Wrong login or password");
						// login failed
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					throw new AuthentificationException("Could not login due to NoSuchAlgorithmException" + e);
				}
			}
		}
	}
}
