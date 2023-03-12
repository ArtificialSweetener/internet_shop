package commands.admin_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import commands.icommand.ICommand;
import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.DataProcessingException;
import models.Category;
import service.CategoryService;
import service.impl.CategoryServiceImpl;
import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.AddCategoryFormValidator;

public class AddCategoryCommand implements ICommand {
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(AddCategoryCommand.class);
	private CategoryService categoryService;

	public AddCategoryCommand() {
		this.formValidator = new AddCategoryFormValidator(InputValidator.getInstance());
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		categoryService = new CategoryServiceImpl(categoryDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddCategoryCommand");
		String categoryTitle = req.getParameter("catTitle").trim();
		String categoryDescription = req.getParameter("catDescription").trim();
		if (formValidator.validate(req) == false) {
			return "/admin/add_category.jsp";
		}
		try {
			Category category = new Category(categoryTitle, categoryDescription);
			categoryService.create(category);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_cat");
		} catch (DataProcessingException e) {
			MessageAttributeUtil.setMessageAttribute(req, "message.add_cat_fail");
		}
		String targetUrl = "/admin/admin.jsp";
		return targetUrl;
	}
}
