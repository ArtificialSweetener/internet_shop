package commands.common_commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import util.MessageAttributeUtil;

public class ChangeLanguageCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(ChangeLanguageCommand.class);

	public ChangeLanguageCommand() {
	}

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
