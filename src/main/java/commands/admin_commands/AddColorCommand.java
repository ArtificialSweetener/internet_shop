package commands.admin_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.ColorDao;
import dao.impl.ColorDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.DataProcessingException;
import models.Color;
import service.ColorService;
import service.impl.ColorServiceImpl;
import util.MessageAttributeUtil;
import util.validators.InputValidator;

/**
 * The AddColorCommand class is a command object that implements the ICommand
 * interface and is used to handle requests to add a new color to the system. It
 * uses ColorService to create the color and validates the form data using
 * InputValidator. The class handles GET and POST requests, and redirects the
 * user to the appropriate view after processing the request.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class AddColorCommand implements ICommand {
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(AddColorCommand.class);

	/**
	 * Constructs an AddColorCommand object with a ColorDaoImpl instance and a
	 * ColorServiceImpl instance initialized with the ColorDaoImpl instance.
	 */
	public AddColorCommand() {
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

	/**
	 * Executes the AddColorCommand by processing the request and creating a new
	 * color.
	 * 
	 * @param req  the HttpServletRequest object containing the request information.
	 * @param resp the HttpServletResponse object used to send the response.
	 * @return a String representing the target URL of the view to redirect to after
	 *         processing the request.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddColorCommand");
		String colorName = req.getParameter("color_name").trim();
		if (!InputValidator.getInstance().isTitleValid(colorName)) {
			MessageAttributeUtil.setMessageAttribute(req, "message.col_name_not_valid"); // done
			return "/admin/add_color.jsp";
		}
		try {
			Color color = new Color(colorName);
			colorService.create(color);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_col"); // done
		} catch (DataProcessingException e) {
			logger.error("A DataProcessingException occurred while executing AddColorCommand", e);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_col_fail"); // done
		}
		String targetUrl = "/admin/admin.jsp";
		return targetUrl;
	}
}
