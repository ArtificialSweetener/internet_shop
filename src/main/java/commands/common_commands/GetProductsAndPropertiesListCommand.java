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

public class GetProductsAndPropertiesListCommand implements ICommand {
	private ProductService productService;
	private CategoryService categoryService;
	private ColorService colorService;
	private static final Logger logger = LogManager.getLogger(GetProductsAndPropertiesListCommand.class);

	public GetProductsAndPropertiesListCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		ColorDao colorDao = new ColorDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.colorService = new ColorServiceImpl(colorDao);
	}

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
