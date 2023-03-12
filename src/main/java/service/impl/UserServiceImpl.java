package service.impl;

import java.util.List;
import java.util.Optional;

import dao.UserDao;
import models.User;

import service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		super();
		this.userDao = userDao;
	}

	public User create(User user) {
		return userDao.create(user);
	}

	@Override
	public Optional<User> get(Long id) {
		return userDao.get(id);
	}

	@Override
	public List<User> getAll() {
		return userDao.getAll();
	}

	@Override
	public User update(User user) {
		return userDao.update(user);
	}

	@Override
	public boolean delete(Long id) {
		return userDao.delete(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public List<User> getAll(int offset, int noOfRecords) {
		return userDao.getAll(offset, noOfRecords);
	}

	public int getNoOfRecords() {
		return userDao.getNoOfRecords();
	}
}
