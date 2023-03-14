package commands.common_commands;

import java.util.List;
import java.util.Optional;
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
import util.validators.InputValidator;
/**
 * This class represents a command for retrieving a list of products based on
 * specified filtering and sorting criteria. It also retrieves all available
 * product categories, colors, and the minimum and maximum prices of products.
 * The command implements the ICommand interface and is used by the
 * FrontController servlet for dispatching requests to the corresponding
 * commands.
 * 
 * The GetProductsAndPropertiesListCommand class has three Service dependencies:
 * ProductService - for retrieving the products list
 * CategoryService - for retrieving all available product categories
 * ColorService - for retrieving all available product colors
 * 
 * The command extracts filtering and sorting criteria from the HTTP request
 * and the HTTP session. It retrieves a list of products from the ProductService
 * based on the extracted criteria and stores it in the HTTP session. It also
 * retrieves all available categories, colors, and the minimum and maximum prices
 * of products and stores them in the HTTP session for further use in filtering
 * and sorting criteria. The command returns the URL of the JSP page that displays
 * the list of products.
 * 
 * The class has the following properties:
 * productService - an instance of the ProductService interface for retrieving the products list
 * categoryService - an instance of the CategoryService interface for retrieving all available product categories
 * colorService - an instance of the ColorService interface for retrieving all available product colors
 * logger - an instance of the Logger interface from the log4j framework for logging the execution of the command
 * 
 * The class has one constructor that initializes the above services using their corresponding DAO implementations.
 * 
 * The class implements the execute() method of the ICommand interface, which takes HttpServletRequest and HttpServletResponse
 * objects as arguments, retrieves filtering and sorting criteria from the request and session objects, retrieves a list of
 * products based on the extracted criteria using the ProductService, retrieves all available categories and colors using
 * the CategoryService and ColorService respectively, calculates the total number of pages of the products list and stores
 * all the retrieved data in the session object. Finally, it returns the URL of the JSP page that displays the list of products.
 * 
 * The class also uses the following utility classes:
 * InputValidator - for validating input parameters for filtering criteria
 * MessageAttributeUtil - for setting a message attribute in the session object in case of invalid input for filtering criteria
 * ConnectionPoolManager - for obtaining a connection to the database through a connection pool
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetProductsAndPropertiesListCommand implements ICommand {
	private ProductService productService;
	private CategoryService categoryService;
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(GetProductsAndPropertiesListCommand.class);

	 /**
     * Initializes the ProductService, CategoryService, and ColorService using their corresponding DAO implementations.
     * 
     * @param productService the ProductService instance for retrieving the products list
     * @param categoryService the CategoryService instance for retrieving all available product categories
     * @param colorService the ColorService instance for retrieving all available product colors
     */
	public GetProductsAndPropertiesListCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

	/**
	 * Retrieves a list of products based on filtering and sorting criteria from the
	 * request and session objects. Stores the retrieved products, available categories
	 * and colors, minimum and maximum prices of products, and the total number of pages
	 * of the products list in the session object. Returns the URL of the JSP page that
	 * displays the list of products.
	 *
	 * @param request  the HTTP request object
	 * @param response the HTTP response object
	 * @throws ServletException if an error occurs while executing the command
	 * @throws IOException      if an error occurs while executing the command
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetProductsAndPropertiesListCommand");
		String targetUrl = "/common_pages/products.jsp";

		int page = 1;
		int recordsPerPage = 3;
		if (req.getParameter("page") != null)
			page = Integer.parseInt(req.getParameter("page"));

		Optional<Object> sortOldOpt = Optional.ofNullable(req.getSession().getAttribute("sortOld"));
		Optional<String> sortNewOpt = Optional.ofNullable(req.getParameter("sort"));

		// old
		Optional<Object> categoryIdOldOptional = Optional.ofNullable(req.getSession().getAttribute("categoryOld"));

		Optional<Object> colorIdOldOptional = Optional.ofNullable(req.getSession().getAttribute("colorOld"));

		Optional<String> minOldOptional = Optional.ofNullable((String) req.getSession().getAttribute("minOld"));
		Optional<String> maxOldOptional = Optional.ofNullable((String) req.getSession().getAttribute("maxOld"));

		// new
		Optional<String> categoryIdNewOptional = Optional.ofNullable(req.getParameter("category"));
		Optional<String> colorIdNewOptional = Optional.ofNullable(req.getParameter("color"));

		Optional<String> minNewOpt = Optional.ofNullable(req.getParameter("min"));
		Optional<String> maxNewOpt = Optional.ofNullable(req.getParameter("max"));

		String categoryId = "";
		String colorId = "";
		String min = "";
		String max = "";
		String sort = "";

		if (categoryIdNewOptional.isPresent()) {
			categoryId = categoryIdNewOptional.get();
		} else if (categoryIdOldOptional.isPresent()) {
			categoryId = (String) categoryIdOldOptional.get();
		}

		if (colorIdNewOptional.isPresent()) {
			colorId = colorIdNewOptional.get();
		} else if (colorIdOldOptional.isPresent()) {
			colorId = (String) colorIdOldOptional.get();
		}

		if (minNewOpt.isPresent() && !minNewOpt.get().isEmpty() && maxNewOpt.isPresent()
				&& !maxNewOpt.get().isEmpty()) {
			boolean validMin = InputValidator.getInstance().isNumberValid(minNewOpt.get());
			boolean validMax = InputValidator.getInstance().isNumberValid(maxNewOpt.get());

			if (validMin && validMax) {
				min = minNewOpt.get();
				max = maxNewOpt.get();
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.number_not_valid");
				return targetUrl;
			}
		} else {
			min = minNewOpt.orElse(minOldOptional.orElse(""));
			max = maxNewOpt.orElse(maxOldOptional.orElse(""));
		}

		if (sortNewOpt.isPresent()) {
			sort = sortNewOpt.get();
		} else if (sortOldOpt.isPresent()) {
			sort = (String) sortOldOpt.get();
		}

		System.out.println("CategoryId is:" + categoryId);
		System.out.println("ColorId is:" + colorId);
		System.out.println("Min proce is:" + min);
		System.out.println("Max proce is:" + max);
		System.out.println("Sort criteria is:" + sort);

		try {
			List<Product> productList = productService.getAllBy(categoryId, colorId, min, max, sort,
					(page - 1) * recordsPerPage, recordsPerPage);

			int noOfRecords = productService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
			List<Category> categoryList = categoryService.getAll();
			List<Color> colorList = colorService.getAll();
			double productMinPrice = productService.getMinPrice();
			double productMaxPrice = productService.getMaxPrice();

			System.out.println(productList);

			if (!productList.isEmpty()) {
				req.getSession().setAttribute("productList", productList);

				req.getSession().setAttribute("noOfPagesProducts", noOfPages);
				req.getSession().setAttribute("currentPageProducts", page);

				req.getSession().setAttribute("categoryOld", categoryId);
				req.getSession().setAttribute("colorOld", colorId);
				req.getSession().setAttribute("minOld", min);
				req.getSession().setAttribute("maxOld", max);
				req.getSession().setAttribute("sortOld", sort);

				req.getSession().setAttribute("categoryList", categoryList);
				req.getSession().setAttribute("colorList", colorList);

				req.getSession().setAttribute("productMinPrice", productMinPrice);
				req.getSession().setAttribute("productMaxPrice", productMaxPrice);
				return targetUrl;
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.no_products_availaible");
				return targetUrl;
			}
		} catch (ClassCastException e) {
			throw new DataProcessingException("Could not get productList as an attribute", e);
		}
	}
}
