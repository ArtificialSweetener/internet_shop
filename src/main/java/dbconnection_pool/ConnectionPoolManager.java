package dbconnection_pool;

/**
 * The ConnectionPoolManager class is responsible for managing the
 * ConnectionPool object and providing access to it. It is implemented as a
 * singleton, ensuring that there is only one instance of the class throughout
 * the application.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ConnectionPoolManager {
	private ConnectionPool connectionPool;

	private static ConnectionPoolManager instance = null;

	/**
	 * Returns the singleton instance of the ConnectionPoolManager class. If the
	 * instance does not exist yet, it will be created.
	 * 
	 * @return the singleton instance of the ConnectionPoolManager class
	 */
	public static ConnectionPoolManager getInstance() {
		if (instance == null) {
			instance = new ConnectionPoolManager();
		}
		return instance;
	}

	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	public void setConnectionPool(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

}
