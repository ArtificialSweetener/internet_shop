package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Product;
import util.GlobalStringsProvider;

public class DeleteFromCartCommand implements ICommand {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("chosenProductId"));
		String forward = "cart.jsp";
		if (chosenProductId.isEmpty()) {
			req.setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("delete_prod_not_chosen_cart_fail"));
			return forward;
		} else {
			Optional<Object> chosenProductListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
			if (chosenProductListOptional.isEmpty()) {
				req.setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("delete_prod_fail_cart_empty"));
				return forward;
			} else {
				@SuppressWarnings("unchecked")
				List<Product> chosenProductList = (List<Product>) chosenProductListOptional.get();
				List<Product> newChosenProductsList = new ArrayList<>();
				for (Product product : chosenProductList) {
					if (product.getProductId() != Long.parseLong(chosenProductId.get())) {
						newChosenProductsList.add(product);
					}
				}
				int totalPrice = 0;
				for (Product product : newChosenProductsList) {
					totalPrice += product.getProductPrice();
				}
				req.getSession().setAttribute("totalPrice", totalPrice);
				req.getSession().setAttribute("cartList", newChosenProductsList);
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("product_deleted_cart"));
				return forward;
			}
		}
	}

}
