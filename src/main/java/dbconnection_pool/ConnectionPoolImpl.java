package dbconnection_pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class implements the ConnectionPool interface to provide a connection
 * pool for accessing a database through a DataSource. It utilizes the
 * javax.naming package to lookup the DataSource object by a given JNDI name.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ConnectionPoolImpl implements ConnectionPool {
	private final DataSource dataSource;
	private static final Logger logger = LogManager.getLogger(ConnectionPoolImpl.class);

	/**
	 * Constructs a new ConnectionPoolImpl object with a given JNDI name.
	 *
	 * @param jndiName the JNDI name of the DataSource object to use.
	 * @throws RuntimeException if there is an error getting the DataSource object
	 *                          from the context.
	 */
	public ConnectionPoolImpl(String jndiName) {
		InitialContext context = null;
		try {
			context = new InitialContext();
			dataSource = (DataSource) context.lookup(jndiName);
			logger.info("DataSource: " + dataSource);
		} catch (NamingException e) {
			throw new RuntimeException("Unable to get DataSource", e);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (NamingException e) {
					logger.error("NamingException occured while trying to close context in ConnectionPoolImpl constructor", e);// handle the exception
				}
			}
		}
	}

	/**
	 * Retrieves a connection from the connection pool.
	 *
	 * @return a Connection object.
	 * @throws SQLException if there is an error getting a connection from the
	 *                      DataSource object.
	 */
	public synchronized Connection getConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error("SQLException occured while trying to get connection", e);
		}
		return connection;
	}

	/**
	 * Closes the connection pool and the DataSource object.
	 *
	 * @throws Exception if there is an error closing the DataSource object.
	 */
	@Override
	public void close() {
		if (dataSource != null && dataSource instanceof AutoCloseable) {
			try {
				((AutoCloseable) dataSource).close();
			} catch (Exception e) {
				logger.error("Error while shutting down connection pool", e);
			}
		}
	}

}