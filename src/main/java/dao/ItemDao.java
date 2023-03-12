package dao;

import java.util.List;
import models.Item;

public interface ItemDao extends GenericDao<Item> {
	List<Item> getAllByOrderId(Long id);
	List<Item> getAllByOrderId(Long id, int offset, int noOfRecords);
	public int getNoOfRecords();
}
