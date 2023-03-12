package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ColorDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Color;

public class ColorDaoImpl implements ColorDao {
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(ColorDaoImpl.class);

	public ColorDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	@Override
	public Color create(Color color) {
		logger.info("method create(Color color) of ColorDaoImpl class is invoked");
		String insertQuery = "INSERT INTO colors (color_name) VALUES (?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createColorStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createColorStatement.setString(1, color.getColorName());
			createColorStatement.executeUpdate();
			ResultSet resultSet = createColorStatement.getGeneratedKeys();
			if (resultSet.next()) {
				color.setColorId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException | NullPointerException e) {
			logger.error("Error while creating color:{}", color, e);
			throw new DataProcessingException("Can't create color " + color, e);
		}
		logger.debug("Successfully created color: {}", color);
		return color;
	}

	@Override
	public Optional<Color> get(Long id) {
		logger.info("method get(Long id) of ColorDaoImpl class is invoked");
		String query = "SELECT * FROM colors WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getColorStatement = connection.prepareStatement(query)) {
			getColorStatement.setLong(1, id);
			ResultSet resultSet = getColorStatement.executeQuery();
			Color color = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					color = parseColorFromResultSet(resultSet);
					logger.debug("Successfully got color: {}", color);
				} while (resultSet.next());
			}
			return Optional.ofNullable(color);
		} catch (SQLException | NullPointerException e) {
			logger.error("Error while getting color by id:{}", id, e);
			throw new DataProcessingException("Couldn't get color by id " + id, e);
		}
	}

	@Override
	public List<Color> getAll() {
		logger.info("method getAll() of ColorDaoImpl class is invoked");
		String query = "SELECT * FROM colors";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllColorsStatement = connection.prepareStatement(query)) {
			List<Color> listColors = new ArrayList<>();
			ResultSet resultSet = getAllColorsStatement.executeQuery();
			while (resultSet.next()) {
				listColors.add(parseColorFromResultSet(resultSet));
			}
			logger.debug("Successfully got color list: {}", listColors);
			return listColors;
		} catch (SQLException e) {
			logger.error("Error while getting color list:{}", e);
			throw new DataProcessingException("Couldn't get a list of color" + "from colors table. ", e);
		}
	}

	@Override
	public Color update(Color element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	private Color parseColorFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseColorFromResultSet(ResultSet resultSet) of ColorDaoImpl class is invoked");
		long colorId = resultSet.getLong("id");
		String colorName = resultSet.getString("color_name");
		Color color = new Color();
		color.setColorId(colorId);
		color.setColorName(colorName);
		logger.debug("Successfully parsed color: {}", color);
		return color;
	}

}
