package commands.logged_in_user_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;

public class LogoutCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing LogoutCommand");
		req.getSession().invalidate();
		String targetUrl = "/common_pages/login.jsp";
		return targetUrl;
	}
}
