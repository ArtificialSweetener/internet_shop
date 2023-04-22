package commands.common_commands;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.CategoryDao;
import dao.ColorDao;
import dao.ProductDao;
import dao.impl.CategoryDaoImpl;
import dao.impl.ColorDaoImpl;
import dao.impl.ProductDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.DataProcessingException;
import models.Category;
import models.Color;
import models.Product;
import service.CategoryService;
import service.ColorService;
import service.ProductService;
import service.impl.CategoryServiceImpl;
import service.impl.ColorServiceImpl;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

/**
 * ResetFiltersCommand class implements the ICommand interface and represents
 * the command to reset the filters applied on the products page. This command
 * removes the filter criteria that were applied on the products page, retrieves
 * the list of products based on the default filter criteria, and sets the
 * necessary attributes on the request object for rendering the page.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ResetFiltersCommand implements ICommand {
	private ProductService productService;
	private CategoryService categoryService;
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(ResetFiltersCommand.class);

	/**
	 * Constructs a new ResetFiltersCommand object by creating instances of
	 * CategoryService, ProductService, and ColorService using their respective DAO
	 * implementations.
	 */
	public ResetFiltersCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

	/**
	 * Executes the command to reset the filters applied on the products page.
	 * 
	 * @param req  The HttpServletRequest object containing the request parameters
	 *             and attributes.
	 * @param resp The HttpServletResponse object for sending the response.
	 * @return The target URL to forward to after executing the command.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ResetFiltersCommand");
		String targetUrl = "/common_pages/products.jsp";
		
		req.getSession().removeAttribute("sortOld");
		req.getSession().removeAttribute("categoryOld");
		req.getSession().removeAttribute("colorOld");
		req.getSession().removeAttribute("minOld");
		req.getSession().removeAttribute("maxOld");

		int page = 1;
		int recordsPerPage = 3;
		if (req.getParameter("page") != null)
			page = Integer.parseInt(req.getParameter("page"));

		String categoryId = "";
		String colorId = "";
		String min = "";
		String max = "";
		String sort = "";

		try {
			List<Product> productList = productService.getAllBy(categoryId, colorId, min, max, sort,
					(page - 1) * recordsPerPage, recordsPerPage);

			int noOfRecords = productService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			List<Category> categoryList = categoryService.getAll();
			List<Color> colorList = colorService.getAll();

			double productMinPrice = productService.getMinPrice();
			double productMaxPrice = productService.getMaxPrice();

			if (!productList.isEmpty()) {
				req.getSession().setAttribute("productList", productList);

				req.getSession().setAttribute("noOfPagesProducts", noOfPages);
				req.getSession().setAttribute("currentPageProducts", page);

				req.getSession().setAttribute("categoryList", categoryList);
				req.getSession().setAttribute("colorList", colorList);

				req.getSession().setAttribute("productMinPrice", productMinPrice);
				req.getSession().setAttribute("productMaxPrice", productMaxPrice);
				return targetUrl;
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.no_products_availaible");
				return targetUrl;
			}
		} catch (DataProcessingException | ClassCastException e) {
			logger.error("An exception occurred while executing RegisterCommand", e);
			MessageAttributeUtil.setMessageAttribute(req, "message.product_list_error");
			return targetUrl;
		}
	}
}
