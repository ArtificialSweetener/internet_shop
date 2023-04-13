package commands.icommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * The {@code ICommand} interface represents a command that can be executed in
 * response to a user request. It provides a method for executing the command
 * and returning a result.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public interface ICommand {
	/**
	 * Executes the command and returns a result as a string.
	 * 
	 * @param req  the HTTP servlet request object containing the request parameters
	 * @param resp the HTTP servlet response object to be populated with the
	 *             command's result
	 * @return a string representing the result of the command
	 */
	String execute(HttpServletRequest req, HttpServletResponse resp);
}
