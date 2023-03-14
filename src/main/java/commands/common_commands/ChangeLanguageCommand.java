package commands.common_commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import util.MessageAttributeUtil;

/**
 * The ChangeLanguageCommand class implements the ICommand interface and is
 * responsible for changing the language of the current session based on the
 * value of the "language" parameter passed in the HttpServletRequest object. It
 * also sets an optional message in the request session and returns the target
 * URL to redirect the user.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ChangeLanguageCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(ChangeLanguageCommand.class);

	public ChangeLanguageCommand() {
	}

	/**
	 * Changes the language of the current session based on the value of the
	 * "language" parameter passed in the HttpServletRequest object. Sets an
	 * optional message in the request session and returns the target URL to
	 * redirect the user.
	 *
	 * @param req  The HttpServletRequest object containing the request data.
	 * @param resp The HttpServletResponse object used to send the response.
	 * @return The target URL to redirect the user.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ChangeLanguageCommand");

		System.out.println("ChangeLanguageCommand executed");
		String changeToLanguage = req.getParameter("language");
		Optional<Object> messageOpt = Optional.ofNullable(req.getSession().getAttribute("message"));

		// String forward = req.getParameter("pageName");
		String targetUrl = req.getParameter("pageName");
		System.out.println(targetUrl);
		if (changeToLanguage.equals("en")) {
			req.getSession().setAttribute("language", "en");
		} else if (changeToLanguage.equals("ua")) {
			req.getSession().setAttribute("language", "ua");
		}
		if (messageOpt.isPresent()) {
			String message = (String) messageOpt.get();
			MessageAttributeUtil.setMessageAttribute(req, message);
		}
		return targetUrl;
	}
}
