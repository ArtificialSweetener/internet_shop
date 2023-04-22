package service.impl;

import java.util.List;
import java.util.Optional;

import dao.ItemDao;
import models.Item;

import service.ItemService;

public class ItemServiceImpl implements ItemService {
	private ItemDao itemDao;

	public ItemServiceImpl(ItemDao itemDao) {
		super();
		this.itemDao = itemDao;
	}

	@Override
	public Item create(Item item) {
		return itemDao.create(item);
	}

	@Override
	public Optional<Item> get(Long id) {
		return itemDao.get(id);
	}

	@Override
	public List<Item> getAll() {
		return itemDao.getAll();
	}

	@Override
	public Item update(Item item) {
		return itemDao.update(item);
	}

	@Override
	public boolean delete(Long id) {
		return itemDao.delete(id);
	}

	@Override
	public List<Item> getAllByOrderId(Long orderId) {
		return itemDao.getAllByOrderId(orderId);
	}

	@Override
	public List<Item> getAllByOrderId(Long id, int offset, int noOfRecords) {
		return itemDao.getAllByOrderId(id, offset, noOfRecords);
	}

	@Override
	public int getNoOfRecords() {
		return itemDao.getNoOfRecords();
	}

}
