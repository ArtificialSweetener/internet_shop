package commands;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Cart;
import util.GlobalStringsProvider;

public class GetCartListCommand implements ICommand {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String redirect = "cart.jsp";
		Optional<Object> cartListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
		double totalPrice = 0;

		if (cartListOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("cart_empty"));
			req.getSession().setAttribute("totalPrice", totalPrice);
			return redirect;
		} else {
			Optional<Object> currentUserOptional = Optional.ofNullable(req.getSession().getAttribute("current_user"));
			@SuppressWarnings("unchecked")
			List<Cart> chosenProductList = (List<Cart>) cartListOptional.get();
			for (Cart cart : chosenProductList) {
				totalPrice += cart.getTotalPrice();
			}
			req.getSession().setAttribute("cartList", chosenProductList);
			req.getSession().setAttribute("totalPrice", totalPrice);
			if (currentUserOptional.isEmpty()) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("login_to_order"));
				return redirect;
			} else {
				return redirect;
			}
		}
	}

}
