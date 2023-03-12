package service.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import dao.UserSaltDao;
import models.UserSalt;
import service.UserSaltService;

public class UserSaltServiceImpl implements UserSaltService {
	UserSaltDao userSaltDao;

	public UserSaltServiceImpl(UserSaltDao userSaltDao) {
		super();
		this.userSaltDao = userSaltDao;
	}

	public byte[] generateRandomSaltBytes() {
		// generate a random 16-character salt value
		byte[] salt = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}

	@Override
	public UserSalt create(UserSalt userSalt) {
		// TODO Auto-generated method stub
		return userSaltDao.create(userSalt);
	}

	@Override
	public Optional<UserSalt> get(Long id) {
		// TODO Auto-generated method stub
		return userSaltDao.get(id);
	}

	@Override
	public List<UserSalt> getAll() {
		// TODO Auto-generated method stub
		return userSaltDao.getAll();
	}

	@Override
	public UserSalt update(UserSalt userSalt) {
		// TODO Auto-generated method stub
		return userSaltDao.update(userSalt);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return userSaltDao.delete(id);
	}

	@Override
	public Optional<UserSalt> getSaltByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userSaltDao.getSaltByUserId(userId);
	}

}
