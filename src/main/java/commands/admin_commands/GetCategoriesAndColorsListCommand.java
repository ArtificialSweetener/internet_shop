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

/**
 * The GetCategoriesAndColorsListCommand class implements ICommand interface and
 * provides functionality to get all categories and colors and display them on
 * the add product page in the admin panel.
 * 
 * This class retrieves the list of all categories and colors from the database
 * and sets them as attributes in the HTTP session. These attributes are later
 * used to populate the drop-down menus on the add product page in the admin
 * panel.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetCategoriesAndColorsListCommand implements ICommand {
	private CategoryService categoryService;
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(GetCategoriesAndColorsListCommand.class);

	/**
	 * Constructs a new GetCategoriesAndColorsListCommand object. Initializes the
	 * CategoryService and ColorService objects by creating corresponding DAO and
	 * passing it to their implementation classes.
	 */
	public GetCategoriesAndColorsListCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

	/**
	 * Executes the GetCategoriesAndColorsListCommand. Retrieves all categories and
	 * colors from the database using CategoryService and ColorService objects and
	 * stores them in the session attributes. Returns the target URL for the add
	 * product page in the admin panel.
	 *
	 * @param req  The HttpServletRequest object.
	 * @param resp The HttpServletResponse object.
	 * @return The target URL for the add product page in the admin panel.
	 */
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
