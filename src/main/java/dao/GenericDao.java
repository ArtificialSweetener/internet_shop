/**
 * 
 */
package dao;

import java.util.List;
import java.util.Optional;

/**
 * @author annak
 *
 */
public interface GenericDao<T> {
	T create(T element);

	Optional<T> get(Long id);

	List<T> getAll();

	T update(T element);

	boolean delete(Long id);
}
