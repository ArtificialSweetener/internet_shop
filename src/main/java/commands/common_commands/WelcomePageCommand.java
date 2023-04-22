package commands.common_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;

/**
 * The WelcomePageCommand class implements the ICommand interface and represents
 * the command to show the welcome page of the application. This command is
 * responsible for rendering the index.jsp page.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class WelcomePageCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(WelcomePageCommand.class);

	public WelcomePageCommand() {

	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing WelcomePageCommand");
		return "/index.jsp";
	}

}
