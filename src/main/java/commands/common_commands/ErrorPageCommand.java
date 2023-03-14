package commands.common_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;

/**
 * This class represents a command that handles errors and redirects the user to
 * the homepage. The command implements the ICommand interface and requires the
 * execute method to be implemented.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ErrorPageCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(ErrorPageCommand.class);

	/**
	 * Executes the ErrorPageCommand, redirecting the user to the homepage.
	 * 
	 * @param req  the HttpServletRequest object containing the user's request data
	 * @param resp the HttpServletResponse object used to send the response
	 * @return the target URL for the response
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ErrorPageCommand");
		String targetUrl = "/index.jsp";
		return targetUrl;
	}

}
