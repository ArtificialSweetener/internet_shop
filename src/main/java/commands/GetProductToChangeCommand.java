package commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import models.Product;

import service.ProductService;
import service.impl.ProductServiceImpl;
import util.GlobalStringsProvider;

public class GetProductToChangeCommand implements ICommand {
	private ProductService productService;

	public GetProductToChangeCommand() {
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String forward = "change_product.jsp";
		String forward_if_fail = "change_product_fail.jsp";
		Optional<String> idOptional = Optional.ofNullable(req.getParameter("productId"));
		if (idOptional.isPresent()) {
			long id = Long.parseLong(idOptional.get());
			Product product = productService.get(id);
			req.getSession().setAttribute("product", product);
			return forward;
		} else {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("prod_not_chosen_info_fail"));
			return forward_if_fail;
		}
	}
}
