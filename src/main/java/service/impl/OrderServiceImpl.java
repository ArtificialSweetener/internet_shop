package service.impl;

import java.util.List;
import java.util.Optional;

import dao.OrderDao;
import models.Order;

import service.OrderService;

public class OrderServiceImpl implements OrderService {
	private OrderDao orderDao;

	public OrderServiceImpl(OrderDao orderDao) {
		super();
		this.orderDao = orderDao;
	}

	@Override
	public Order create(Order order) {
		return orderDao.create(order);
	}

	@Override
	public Optional<Order> get(Long id) {
		return orderDao.get(id);
	}

	@Override
	public List<Order> getAll() {
		return orderDao.getAll();
	}

	@Override
	public Order update(Order order) {
		return orderDao.update(order);
	}

	@Override
	public boolean delete(Long id) {
		return orderDao.delete(id);
	}

	@Override
	public List<Order> getAllOrdersByUserId(Long id, int offset, int noOfRecords) {
		return orderDao.getAllOrdersByUserId(id, offset, noOfRecords);
	}

	@Override
	public List<Order> getAll(int offset, int noOfRecords) {
		return orderDao.getAll(offset, noOfRecords);
	}

	@Override
	public int getNoOfRecords() {
		return orderDao.getNoOfRecords();
	}

}
