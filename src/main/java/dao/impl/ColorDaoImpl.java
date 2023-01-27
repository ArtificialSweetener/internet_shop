package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.ColorDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Color;


public class ColorDaoImpl implements ColorDao{

	@Override
	public Color create(Color color) {
		String insertQuery = "INSERT INTO colors (color_name) VALUES (?)";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement createColorStatement =
                        connection.prepareStatement(
                             insertQuery, Statement.RETURN_GENERATED_KEYS)) {
        	createColorStatement.setString(1, color.getColorName());
        	createColorStatement.executeUpdate();
            ResultSet resultSet = createColorStatement.getGeneratedKeys();
            if (resultSet.next()) {
            	color.setColorId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create color " + color, e);
        }
        System.out.println(color);
        return color;
	}

	@Override
	public Optional<Color> get(Long id) {
		String query = "SELECT * FROM colors WHERE id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getColorStatement = connection.prepareStatement(query)) {
			getColorStatement.setLong(1, id);
			ResultSet resultSet = getColorStatement.executeQuery();
			Color color = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					color = parseColorFromResultSet(resultSet);
					System.out.println(color);
				} while (resultSet.next());
			}
			return Optional.ofNullable(color);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get color by id " + id, e);
		}
	}

	@Override
	public List<Color> getAll() {
		String query = "SELECT * FROM colors";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getAllColorsStatement = connection.prepareStatement(query)) {
			List<Color> listColors = new ArrayList<>();
			ResultSet resultSet = getAllColorsStatement.executeQuery();
			while (resultSet.next()) {
				listColors.add(parseColorFromResultSet(resultSet));
			}
			return listColors;
		} catch (SQLException e) {
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
		System.out.println("Method parseColorFromResultSet invoked");
		long colorId = resultSet.getLong("id");
		String colorName = resultSet.getString("color_name");
		Color color = new Color();
		color.setColorId(colorId);
		color.setColorName(colorName);
		return color;
	}
	
}
