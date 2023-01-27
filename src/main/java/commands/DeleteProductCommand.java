package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import models.Product;

import service.ProductService;
import service.impl.ProductServiceImpl;
import util.GlobalStringsProvider;

public class DeleteProductCommand implements ICommand {
	private ProductService productService;

	public DeleteProductCommand() {
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String redirect = "products.jsp";
		Optional<Object> productsOptional = Optional.ofNullable(req.getSession().getAttribute("productList"));
		Optional<String> idOptional = Optional.ofNullable(req.getParameter("productId"));
		if (productsOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("delete_prod_fail_list_empty"));
			return redirect;
		} else if (idOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("prod_not_chosen_delete_fail"));
			return redirect;
		} else {
			long id = Long.parseLong(idOptional.get());
			productService.delete(id);
			@SuppressWarnings("unchecked")
			List<Product> products = (List<Product>) productsOptional.get();
			List<Product> newProductsList = new ArrayList<>();
			for (Product product : products) {
				if (product.getProductId() != id) {
					newProductsList.add(product);
				}
			}
			req.getSession().setAttribute("productList", newProductsList);
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("product_deleted"));
			return redirect;
		}
	}
}
