package commands.admin_commands;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.CategoryDao;
import dao.ColorDao;
import dao.impl.CategoryDaoImpl;
import dao.impl.ColorDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import models.Category;
import models.Color;

import service.CategoryService;
import service.ColorService;
import service.impl.CategoryServiceImpl;
import service.impl.ColorServiceImpl;

public class GetCategoriesAndColorsListCommand implements ICommand {
	private CategoryService categoryService;
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(GetCategoriesAndColorsListCommand.class);

	public GetCategoriesAndColorsListCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetCategoriesAndColorsListCommand");
		List<Category> categoryList = categoryService.getAll();
		List<Color> colorList = colorService.getAll();
		req.getSession().setAttribute("categoryList", categoryList);
		req.getSession().setAttribute("colorList", colorList);
		String targetUrl = "/admin/add_product.jsp";
		return targetUrl;
	}

}
