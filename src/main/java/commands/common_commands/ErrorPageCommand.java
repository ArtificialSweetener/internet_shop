package commands.common_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;

public class ErrorPageCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(ErrorPageCommand.class);

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ErrorPageCommand");
		String targetUrl = "/index.jsp";
		return targetUrl;
	}

}
