package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import models.Cart;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.GlobalStringsProvider;

public class AddToCartCommand implements ICommand {
	private ProductService productService;

	public AddToCartCommand() {
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("productId"));
		String forward = "products.jsp";
		if (chosenProductId.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("add_prod_fail_cart"));
			return forward;
		} else {
			long productId = Long.parseLong(chosenProductId.get());
			List<Cart> newCartList = new ArrayList<>();
			Cart cm = new Cart();
			cm.setProductId(productId);
			cm.setQuantityInCart(1);
			@SuppressWarnings("unchecked")
			List<Cart> oldCartList = (List<Cart>) req.getSession().getAttribute("cartList");
			if (oldCartList == null) {
				newCartList.add(cm);
				newCartList = productService.getCartProduct(newCartList);
				req.getSession().setAttribute("cartList", newCartList);
				req.getSession().setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("add_prod_cart"));
				return forward;
			} else {
				newCartList = oldCartList;
				boolean exist = false;
				for (Cart cart : oldCartList) {
					if (cart.getProductId() == productId) {
						exist = true;
						req.getSession().setAttribute("message",
								GlobalStringsProvider.getInstance().getLocalizationMap().get("prod_already_cart"));
						return forward;
					}
				}
				if (!exist) {
					newCartList.add(cm);
					newCartList = productService.getCartProduct(newCartList);
					req.getSession().setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("add_prod_cart"));
					req.getSession().setAttribute("cartList", newCartList);
					return forward;
				}
			}
			return forward;
		}
	}
}
