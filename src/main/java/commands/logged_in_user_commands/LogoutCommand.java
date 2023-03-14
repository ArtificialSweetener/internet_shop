package commands.logged_in_user_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;

/**
 * The LogoutCommand class implements the ICommand interface and represents the
 * command to log out the current user by invalidating their session.
 * 
 * This command is responsible for invalidating the user's session and
 * redirecting them to the login page.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class LogoutCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

	/**
	 * Executes the command to log out the current user by invalidating their
	 * session and redirecting them to the login page.
	 * 
	 * @param req  The HttpServletRequest object containing the request sent by the
	 *             client.
	 * @param resp The HttpServletResponse object containing the response to be sent
	 *             to the client.
	 * @return A String representing the URL to redirect the user to after logging
	 *         out.
	 */

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing LogoutCommand");
		req.getSession().invalidate();
		String targetUrl = "/common_pages/login.jsp";
		return targetUrl;
	}
}
