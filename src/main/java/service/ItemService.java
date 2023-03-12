package service;

import java.util.List;
import models.Item;

public interface ItemService extends GenericService<Item> {
	List<Item> getAllByOrderId(Long id);
	List<Item> getAllByOrderId(Long id, int offset, int noOfRecords);
	public int getNoOfRecords();
}
