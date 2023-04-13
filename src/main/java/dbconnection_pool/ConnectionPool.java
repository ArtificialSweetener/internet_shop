package dbconnection_pool;

import java.sql.Connection;

/**
 * The ConnectionPool interface provides methods to manage a pool of database
 * connections.
 * 
 * The getConnection() method is used to obtain a connection from the pool.
 * 
 * The isValidConnection() method could be used to check if a connection is
 * still valid before it is returned to the pool.
 * 
 * The interface extends the AutoCloseable interface, which allows the
 * ConnectionPool object to be used in a try-with-resources statement.
 * 
 * Implementing classes should provide a way to create and initialize the pool,
 * and to destroy it when no longer needed.
 * 
 * @see java.sql.Connection
 * @see java.lang.AutoCloseable
 * @see dbconnection_pool.ConnectionPoolImpl
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 *
 */
public interface ConnectionPool extends AutoCloseable {
	/**
	 * Returns a connection from the pool.
	 * 
	 * @return a connection from the pool
	 * @throws RuntimeException if a connection cannot be obtained
	 */
	public Connection getConnection() throws RuntimeException;
}
