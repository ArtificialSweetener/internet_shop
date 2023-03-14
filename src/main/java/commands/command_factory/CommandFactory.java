package commands.command_factory;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.command_enum.CommandEnum;
import commands.icommand.ICommand;

/**
 * The CommandFactory class is responsible for generating commands based on a
 * given HttpServletRequest. It uses the CommandEnum to get the corresponding
 * ICommand implementation based on the "command" parameter in the request.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public final class CommandFactory {
	private static final Logger logger = LogManager.getLogger(CommandFactory.class);

	/**
	 * Private constructor to prevent instantiation of the class.
	 */
	private CommandFactory() {
	}

	/**
	 * Returns an ICommand based on the "command" parameter in the given
	 * HttpServletRequest.
	 * 
	 * @param req the HttpServletRequest object containing the "command" parameter
	 * @return an ICommand object based on the "command" parameter in the request
	 */
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
