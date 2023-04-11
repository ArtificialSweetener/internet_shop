package dao_test.impl_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ColorDao;
import dao.impl.ColorDaoImpl;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Color;

public class TestColorDaoImpl { // done, add more test cases
	@Mock
	private ConnectionPool connectionPool;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;

	private ColorDao colorDao;

	@Before
	public void setUp() throws Exception {
		connectionPool = mock(ConnectionPool.class);
		connection = mock(Connection.class);
		statement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		MockitoAnnotations.openMocks(this);
		colorDao = new ColorDaoImpl(connectionPool);

	}

	@Test
	public void testCreate() throws Exception {
		Color color = new Color(1L, "Brown");

		String query = "INSERT INTO colors (color_name) VALUES (?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getObject(1, Long.class)).thenReturn(1L);

		Color createdColor = colorDao.create(color);
		assertEquals(color.getColorId(), createdColor.getColorId());
		assertEquals(color.getColorName(), createdColor.getColorName());
	}

	@Test
	public void testCreateNullColor() throws Exception {
		String query = "INSERT INTO colors (color_name) VALUES (?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);

		assertThrows(DataProcessingException.class, () -> colorDao.create(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).setString(anyInt(), anyString());

		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testGetColor() throws SQLException {
		Color expectedColor = new Color(1L, "Brown");

		String query = "SELECT * FROM colors WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedColor.getColorId());

		when(resultSet.getString("color_name")).thenReturn(expectedColor.getColorName());

		Optional<Color> actualColorOpt = colorDao.get(1L);
		assertTrue(actualColorOpt.isPresent());
		Color actualColor = actualColorOpt.get();

		assertEquals(expectedColor.getColorId(), actualColor.getColorId());
		assertEquals(expectedColor.getColorName(), actualColor.getColorName());

	}

	@Test
	public void testGetOrderByNonExistentId() throws SQLException {
		Long nonExistentId = 999L;
		String query = "SELECT * FROM colors WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);
		Optional<Color> actualColorOpt = colorDao.get(nonExistentId);
		assertFalse(actualColorOpt.isPresent());
	}

	@Test(expected = DataProcessingException.class)
	public void testGetUserByIdThrowsException() throws SQLException {
		Long invalidId = -1L;
		String query = "SELECT * FROM colors WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).setLong(1, invalidId);

		colorDao.get(invalidId);
	}

	@Test
	public void testGetAll() throws SQLException {
		String query = "SELECT * FROM colors";

		List<Color> expectedColors = new ArrayList<>();

		Color expectedColor1 = new Color(1L, "Brown");
		Color expectedColor2 = new Color(1L, "Green");
		expectedColors.add(expectedColor1);
		expectedColors.add(expectedColor2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedColor1.getColorId(), expectedColor2.getColorId());
		when(resultSet.getString("color_name")).thenReturn(expectedColor1.getColorName(),
				expectedColor2.getColorName());

		List<Color> actualColors = colorDao.getAll();

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("color_name");

		assertEquals(expectedColors.size(), actualColors.size());

		for (int i = 0; i < expectedColors.size(); i++) {
			assertEquals(expectedColors.get(i).getColorId(), actualColors.get(i).getColorId());
			assertEquals(expectedColors.get(i).getColorName(), expectedColors.get(i).getColorName());
		}
	}

}