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

public class AddColorCommand implements ICommand {
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(AddColorCommand.class);

	public AddColorCommand() {
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

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
			MessageAttributeUtil.setMessageAttribute(req, "message.add_col_fail"); // done
		}
		String targetUrl = "/admin/admin.jsp";
		return targetUrl;
	}
}
