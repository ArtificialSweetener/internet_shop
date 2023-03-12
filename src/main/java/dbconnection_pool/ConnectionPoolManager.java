package dbconnection_pool;

public class ConnectionPoolManager {
private ConnectionPool connectionPool;

private static ConnectionPoolManager instance = null;

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
