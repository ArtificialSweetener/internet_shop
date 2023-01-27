package commands;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CategoryDao;
import dao.ColorDao;
import dao.impl.CategoryDaoImpl;
import dao.impl.ColorDaoImpl;
import models.Category;
import models.Color;

import service.CategoryService;
import service.ColorService;
import service.impl.CategoryServiceImpl;
import service.impl.ColorServiceImpl;

public class GetCategoriesAndColorsListCommand implements ICommand {
	private CategoryService categoryService;
	private ColorService colorService;
	
	public GetCategoriesAndColorsListCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl();
		this.categoryService = new CategoryServiceImpl(categoryDao);
		ColorDao colorDao = new ColorDaoImpl();
		this.colorService = new ColorServiceImpl(colorDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		List<Category> categoryList = categoryService.getAll();
		List<Color> colorList = colorService.getAll();
		req.getSession().setAttribute("categoryList", categoryList);
		req.getSession().setAttribute("colorList", colorList);
		String forward = "add_product.jsp";
		return forward;
	}

}
