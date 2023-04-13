package dao;

import models.Color;

/**
 * The {@code ColorDao} interface represents a Data Access Object for the
 * {@link models.Color} model. It extends the {@link dao.GenericDao} interface,
 * inheriting its CRUD operations for manipulating color records in the
 * database.
 * 
 * @see dao.GenericDao
 * @see models.Color
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public interface ColorDao extends GenericDao<Color> {
}
