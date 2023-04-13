
package dao;

import java.util.List;
import java.util.Optional;

/**
 * The {@code GenericDao} interface defines the basic CRUD operations for data
 * access objects. It provides methods for creating, retrieving, updating, and
 * deleting records of a specific type.
 * 
 * @param <T> the type of the records to be manipulated
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 * 
 */
public interface GenericDao<T> {
	T create(T element);

	Optional<T> get(Long id);

	List<T> getAll();

	T update(T element);

	boolean delete(Long id);
}
