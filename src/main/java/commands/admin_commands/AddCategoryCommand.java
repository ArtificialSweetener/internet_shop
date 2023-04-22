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

/**
 * The AddCategoryCommand class is a command object that implements the ICommand
 * interface and is used to handle requests to add a new category to the system.
 * It uses an AddCategoryFormValidator to validate the form data and
 * CategoryService to create the category. The class handles GET and POST
 * requests, and redirects the user to the appropriate view after processing the
 * request.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class AddCategoryCommand implements ICommand {
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(AddCategoryCommand.class);
	private CategoryService categoryService;

	/**
	 * Constructs an AddCategoryCommand object. Initializes the categoryService
	 * using a CategoryDaoImpl instance. Initializes the formValidator using an
	 * AddCategoryFormValidator instance.
	 */
	public AddCategoryCommand() {
		this.formValidator = new AddCategoryFormValidator(InputValidator.getInstance());
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		categoryService = new CategoryServiceImpl(categoryDao);
	}

	/**
	 * Handles the request to add a new category to the system.
	 * 
	 * @param req  the HttpServletRequest object containing the request parameters
	 *             and attributes
	 * @param resp the HttpServletResponse object used to send the response to the
	 *             client
	 * @return the target URL of the view to be displayed after processing the
	 *         request
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddCategoryCommand");
		String categoryTitle = req.getParameter("catTitle").trim();
		String categoryDescription = req.getParameter("catDescription").trim();
		String targetUrl = "/admin/admin.jsp";
		String targetUrl_if_fail = "/admin/add_category.jsp";
		if (formValidator.validate(req) == false) {
			return targetUrl_if_fail;
		}
		try {
			Category category = new Category(categoryTitle, categoryDescription);
			categoryService.create(category);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_cat");
		} catch (DataProcessingException e) {
			logger.error("A DataProcessingException occurred while executing AddCategoryCommand", e);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_cat_fail");
			return targetUrl_if_fail;
		}
		return targetUrl;
	}
}
