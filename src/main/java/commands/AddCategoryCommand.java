package commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import exception.DataProcessingException;
import models.Category;

import service.CategoryService;
import service.impl.CategoryServiceImpl;
import util.GlobalStringsProvider;

public class AddCategoryCommand implements ICommand{
	private CategoryService categoryService;
	
	public AddCategoryCommand() {
		CategoryDao categoryDao = new CategoryDaoImpl();
		this.categoryService = new CategoryServiceImpl(categoryDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String categoryTitle = req.getParameter("catTitle");
		String categoryDescription = req.getParameter("catDescription");
		try {
			Category category = new Category(categoryTitle, categoryDescription);
			categoryService.create(category);
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("add_cat"));
		} catch (DataProcessingException e) {
				req.setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("add_cat_fail"));
			} 
		String admin = "admin.jsp";
		return admin;
	}

}
