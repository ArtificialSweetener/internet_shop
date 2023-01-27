package commands;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Cart;
import util.GlobalStringsProvider;

public class ChangeQuantityInCartCommand implements ICommand {

	public ChangeQuantityInCartCommand() {
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("chosenProductId"));
		Optional<Object> chosenProductListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
		Optional<String> amount = Optional.ofNullable(req.getParameter("buyAmount"));
		String redirect = "cart.jsp";
		if (chosenProductId.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("prod_not_chosen_cart_quantity_fail"));
			return redirect;
		} else if (chosenProductListOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("cart_empty"));
			return redirect;
		} else if (amount.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("quantity_not_specified"));
			return redirect;
		} else if (Integer.parseInt(amount.get()) == 0) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("quantity_not_zero"));
			return redirect;
		} else {
			long id = Long.parseLong(chosenProductId.get());
			@SuppressWarnings("unchecked")
			List<Cart> listChange = (List<Cart>) chosenProductListOptional.get();
			int amountInCart = Integer.parseInt(amount.get());
			int totalPrice = 0;
			for (Cart cart : listChange) {
				if (cart.getProductId() == id) {
					if (amountInCart > cart.getProductQuantity()) {
						req.getSession().setAttribute("message",
								GlobalStringsProvider.getInstance().getLocalizationMap().get("quantity_bigger_fail"));
					} else {
						cart.setTotalPrice((cart.getTotalPrice() / cart.getQuantityInCart()) * amountInCart);
						cart.setQuantityInCart(amountInCart);
						req.getSession().setAttribute("message", GlobalStringsProvider.getInstance()
								.getLocalizationMap().get("quantity_in_cart_changed"));
					}
				}
				totalPrice += cart.getTotalPrice();
			}
			req.getSession().setAttribute("cartList", listChange);
			req.getSession().setAttribute("totalPrice", totalPrice);
			return redirect;
		}
	}

}
