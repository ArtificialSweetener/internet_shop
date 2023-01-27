package dao;

import java.util.List;
import java.util.Optional;

import models.Item;

public interface ItemDao extends GenericDao<Item> {
	Optional<List<Item>> getAllByOrderId(Long id);
	List<Item> getAllByOrderId(Long id, int offset, int noOfRecords);
	public int getNoOfRecords();
}
