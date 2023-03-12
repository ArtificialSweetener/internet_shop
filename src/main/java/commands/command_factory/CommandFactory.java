package commands.command_factory;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.command_enum.CommandEnum;
import commands.icommand.ICommand;

public final class CommandFactory {
	private static final Logger logger = LogManager.getLogger(CommandFactory.class);

	private CommandFactory() {
	}

	public static ICommand getCommand(HttpServletRequest req) {
		logger.info("Executing getCommand method of CommandFactory class");
		String command = req.getParameter("command");
		ICommand iCommand = null;
		if (command != null) {
			try {
				iCommand = CommandEnum.valueOf(command).getCommand();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				iCommand = CommandEnum.ERROR_PAGE.getCommand();
			}
		} else {
			iCommand = CommandEnum.ERROR_PAGE.getCommand();
		}
		return iCommand;
	}
}
