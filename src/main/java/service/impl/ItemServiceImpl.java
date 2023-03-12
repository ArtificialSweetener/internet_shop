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
		// TODO Auto-generated method stub
		return itemDao.create(item);
	}

	@Override
	public Optional<Item> get(Long id) {
		// TODO Auto-generated method stub
		return itemDao.get(id);
	}

	@Override
	public List<Item> getAll() {
		// TODO Auto-generated method stub
		return itemDao.getAll();
	}

	@Override
	public Item update(Item item) {
		// TODO Auto-generated method stub
		return itemDao.update(item);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return itemDao.delete(id);
	}

	@Override
	public List<Item> getAllByOrderId(Long orderId) {
		return itemDao.getAllByOrderId(orderId);
	}

	@Override
	public List<Item> getAllByOrderId(Long id, int offset, int noOfRecords) {
		// TODO Auto-generated method stub
		return itemDao.getAllByOrderId(id, offset, noOfRecords);
	}

	@Override
	public int getNoOfRecords() {
		// TODO Auto-generated method stub
		return itemDao.getNoOfRecords();
	}

}
