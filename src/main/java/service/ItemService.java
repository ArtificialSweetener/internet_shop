package service;

import java.util.List;
import java.util.Optional;

import models.Item;

public interface ItemService extends GenericService<Item> {
	Optional<List<Item>> getAllByOrderId(Long id);
	List<Item> getAllByOrderId(Long id, int offset, int noOfRecords);
	public int getNoOfRecords();
}
