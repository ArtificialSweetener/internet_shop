package dao;

import models.Category;

/**
 * The {@code CategoryDao} interface represents a Data Access Object for the
 * {@link models.Category} model. It extends the {@link dao.GenericDao}
 * interface, inheriting its CRUD operations for manipulating category records
 * in the database.
 * 
 * @see dao.GenericDao
 * @see models.Category
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public interface CategoryDao extends GenericDao<Category> {
}
