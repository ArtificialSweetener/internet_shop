package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dbconnection_pool.ConnectionPool;
import dbconnection_pool.ConnectionPoolImpl;
import dbconnection_pool.ConnectionPoolManager;

/**
 * This class implements the ServletContextListener interface to initialize and
 * destroy a connection pool for a web application. It uses the
 * ConnectionPoolManager class to manage the connection pool, and the
 * ConnectionPoolImpl class to create and configure the connection pool.
 * 
 * This class is annotated with the WebListener annotation to indicate that it
 * is a listener for web application events.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
@WebListener
public class ConnectionPoolContextListener implements ServletContextListener {
	private static final Logger logger = LogManager.getLogger(ConnectionPoolContextListener.class);

	/**
	 * Initializes the connection pool by creating a new instance of
	 * ConnectionPoolImpl and passing the JNDI name of the data source. The
	 * connection pool is then set using the ConnectionPoolManager singleton
	 * instance.
	 * 
	 * @param sce the ServletContextEvent object containing information about the
	 *            servlet context
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ConnectionPool connectionPool = new ConnectionPoolImpl("java:comp/env/jdbc/testpool");
		ConnectionPoolManager.getInstance().setConnectionPool(connectionPool);
		logger.info("Connection pool gets initialized: "
				+ ConnectionPoolManager.getInstance().getConnectionPool());
	}

	/**
	 * Destroys the connection pool by getting the connection pool from the
	 * ConnectionPoolManager and calling the close method to release any connections
	 * and clean up resources.
	 * 
	 * @param sce the ServletContextEvent object containing information about the
	 *            servlet context
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ConnectionPool connectionPool = ConnectionPoolManager.getInstance().getConnectionPool();
		if (connectionPool != null) {
			try {
				connectionPool.close();
			} catch (Exception e) {
				logger.error("Exception was thrown while trying to close ConnectionPool");
				e.printStackTrace();
			}
		}
	}
}
