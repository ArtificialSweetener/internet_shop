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

public class ResetFiltersCommand implements ICommand {
	private ProductService productService;
	private CategoryService categoryService;
	private ColorService colorService;

	public ResetFiltersCommand() {
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
		// TODO Auto-generated method stub
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
