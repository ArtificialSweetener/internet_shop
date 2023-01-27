package commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ColorDao;
import dao.impl.ColorDaoImpl;
import exception.DataProcessingException;
import models.Color;
import service.ColorService;
import service.impl.ColorServiceImpl;
import util.GlobalStringsProvider;

public class AddColorCommand implements ICommand{
	private ColorService colorService;
	
	public AddColorCommand() {
		ColorDao colorDao = new ColorDaoImpl();
		this.colorService = new ColorServiceImpl(colorDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String colorName = req.getParameter("color_name");
		try {
			Color color = new Color(colorName);
			colorService.create(color);
			req.getSession().setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("add_col"));
		} catch (DataProcessingException e) {
			req.setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("add_col_fail"));
		}
		String admin = "admin.jsp";
		return admin;
	}
}
