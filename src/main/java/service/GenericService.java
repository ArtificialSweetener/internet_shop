package service;

import java.util.List;
import java.util.Optional;

/**
 * A generic service interface that defines common CRUD operations for any type
 * of object.
 * 
 * @param <T> the type of object this service works with
 */
public interface GenericService<T> {

	/**
	 * Creates a new object and persists it.
	 * 
	 * @param element the object to create
	 * @return the created object
	 */
	T create(T element);

	/**
	 * Retrieves an object by its ID.
	 * 
	 * @param id the ID of the object to retrieve
	 * @return an {@code Optional} containing the retrieved object, or empty if not
	 *         found
	 */
	Optional<T> get(Long id);

	/**
	 * Retrieves a list of all objects.
	 * 
	 * @return a list of all objects
	 */
	List<T> getAll();

	/**
	 * Updates an existing object.
	 * 
	 * @param element the object to update
	 * @return the updated object
	 */
	T update(T element);

	/**
	 * Deletes an object by its ID.
	 * 
	 * @param id the ID of the object to delete
	 * @return {@code true} if the object was deleted, {@code false} otherwise
	 */
	boolean delete(Long id);
}
