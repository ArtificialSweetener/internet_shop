package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dbconnection_pool.ConnectionPool;
import dbconnection_pool.ConnectionPoolImpl;
import dbconnection_pool.ConnectionPoolManager;

@WebListener
public class ConnectionPoolContextListener implements ServletContextListener {
	private static final Logger logger = LogManager.getLogger(ConnectionPoolContextListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		//ConnectionPool connectionPool = new ConnectionPoolImpl("java:comp/env/jdbc/testpool", 100000, true);
		ConnectionPool connectionPool = new ConnectionPoolImpl("java:comp/env/jdbc/testpool");
		ConnectionPoolManager.getInstance().setConnectionPool(connectionPool);
		System.out.println("Checking if connection pool gets initialized: " + ConnectionPoolManager.getInstance().getConnectionPool());
	}

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
