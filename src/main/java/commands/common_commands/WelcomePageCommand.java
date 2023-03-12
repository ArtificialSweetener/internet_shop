package commands.common_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;

public class WelcomePageCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(WelcomePageCommand.class);

	public WelcomePageCommand() {

	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		logger.info("Executing WelcomePageCommand");
		return "/index.jsp";
	}

}
