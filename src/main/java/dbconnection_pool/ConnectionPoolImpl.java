package dbconnection_pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolImpl implements ConnectionPool {
	private final DataSource dataSource;
	private static final Logger logger = LogManager.getLogger(ConnectionPoolImpl.class);

	public ConnectionPoolImpl(String jndiName) {
		InitialContext context = null;
		try {
			context = new InitialContext();
			dataSource = (DataSource) context.lookup(jndiName);
			System.out.println("DataSource: " + dataSource);
		} catch (NamingException e) {
			throw new RuntimeException("Unable to get DataSource", e);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (NamingException e) {
					logger.error("???", e);// handle the exception
				}
			}
		}
	}

	public synchronized Connection getConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

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