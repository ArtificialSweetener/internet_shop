package dbconnection_pool;
import java.sql.Connection;

public interface ConnectionPool extends AutoCloseable{
	public Connection getConnection() throws RuntimeException;
	//public boolean isValidConnection(Connection connection) ;
}
