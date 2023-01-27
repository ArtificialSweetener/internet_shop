package commands;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CategoryDao;
import dao.ColorDao;
import dao.ProductDao;
import dao.impl.CategoryDaoImpl;
import dao.impl.ColorDaoImpl;
import dao.impl.ProductDaoImpl;
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
import util.GlobalStringsProvider;

public class GetProductsAndPropertiesListCommand implements ICommand {
	private ProductService productService;
	private CategoryService categoryService;
	private ColorService colorService;

	public GetProductsAndPropertiesListCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl();
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
		ColorDao colorDao = new ColorDaoImpl();
		this.colorService = new ColorServiceImpl(colorDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {

		String forward = "products.jsp";

		int page = 1;
		int recordsPerPage = 3;
		if (req.getParameter("page") != null)
			page = Integer.parseInt(req.getParameter("page"));

		Optional<Object> sortOldOpt = Optional.ofNullable(req.getSession().getAttribute("sortOld"));
		Optional<String> sortNewOpt = Optional.ofNullable(req.getParameter("sort"));

		// old
		Optional<Object> categoryIdOldOptional = Optional.ofNullable(req.getSession().getAttribute("categoryOld"));

		Optional<Object> colorIdOldOptional = Optional.ofNullable(req.getSession().getAttribute("colorOld"));

		Optional<Object> minOldOptional = Optional.ofNullable(req.getSession().getAttribute("minOld"));
		Optional<Object> maxOldOptional = Optional.ofNullable(req.getSession().getAttribute("maxOld"));

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

		if ((minNewOpt.isPresent() && minNewOpt.get() != "") || (maxNewOpt.isPresent() && maxNewOpt.get() != "")) {
			if (minNewOpt.isPresent()) {
				min = minNewOpt.get();
			}
			if (maxNewOpt.isPresent()) {
				max = maxNewOpt.get();
			}
		} else {
			if (minOldOptional.isPresent()) {
				min = (String) minOldOptional.get();
			}
			if (maxOldOptional.isPresent()) {
				max = (String) maxOldOptional.get();
			}
		}

		if (sortNewOpt.isPresent()) {
			sort = sortNewOpt.get();
		} else if (sortOldOpt.isPresent()) {
			sort = (String) sortOldOpt.get();
		}

		System.out.println(categoryId);
		System.out.println(colorId);
		System.out.println(min);
		System.out.println(max);
		System.out.println(sort);

		try {
			Optional<List<Product>> productList = productService.getAllBy(categoryId, colorId, min, max, sort,
					(page - 1) * recordsPerPage, recordsPerPage);

			int noOfRecords = productService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			List<Category> categoryList = categoryService.getAll();
			List<Color> colorList = colorService.getAll();

			double productMinPrice = productService.getMinPrice();
			double productMaxPrice = productService.getMaxPrice();

			if (productList.isPresent()) {
				req.getSession().setAttribute("productList", productList.get());

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
				return forward;
			} else {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("no_products_availaible"));
				return forward;
			}
		} catch (ClassCastException e) {
			throw new DataProcessingException("Could not get productList as an attribute", e);
		}
	}
}
