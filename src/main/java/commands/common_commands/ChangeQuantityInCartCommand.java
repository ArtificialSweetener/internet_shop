package commands.common_commands;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import models.Cart;
import util.MessageAttributeUtil;
import util.validators.InputValidator;

/**
 * The ChangeQuantityInCartCommand class implements the ICommand interface and
 * is responsible for changing the quantity of a product in the shopping cart of
 * the current session based on the values of the "chosenProductId" and
 * "buyAmount" parameters passed in the HttpServletRequest object. It also
 * performs validation on the input values and sets an appropriate message in
 * the request session before returning the target URL to redirect the user.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ChangeQuantityInCartCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(ChangeQuantityInCartCommand.class);

	public ChangeQuantityInCartCommand() {
	}

	/**
	 * Executes the ChangeQuantityInCartCommand by changing the quantity of a
	 * product in the shopping cart and redirecting the user to the cart page.
	 * 
	 * @param req  The HttpServletRequest object.
	 * @param resp The HttpServletResponse object.
	 * @return A string representing the URL of the cart page.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ChangeQuantityInCartCommand");
		String chosenProductId = req.getParameter("chosenProductId");
		Optional<Object> chosenProductListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
		String buyAmount = req.getParameter("buyAmount").trim();
		String targetUrl = "/common_pages/cart.jsp";

		if (chosenProductId.isBlank()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_cart_quantity_fail"); // done
			return targetUrl;
		} else if (chosenProductListOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.cart_empty"); // done
			return targetUrl;
		} else if (buyAmount.isBlank()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.quantity_not_specified"); // done
			return targetUrl;
		} else if (Integer.parseInt(buyAmount) == 0) {
			MessageAttributeUtil.setMessageAttribute(req, "message.quantity_not_zero"); // done
			return targetUrl;
		} else if (!InputValidator.getInstance().isNumberValid(buyAmount)) {
			MessageAttributeUtil.setMessageAttribute(req, "message.quantity_not_valid"); // done
			return targetUrl;
		} else {
			long id = Long.parseLong(chosenProductId);
			@SuppressWarnings("unchecked")
			List<Cart> listChange = (List<Cart>) chosenProductListOptional.get();
			int amountInCart = Integer.parseInt(buyAmount);
			int totalPrice = 0;
			for (Cart cart : listChange) {
				if (cart.getProductId() == id) {
					if (amountInCart > cart.getProductQuantity()) {
						MessageAttributeUtil.setMessageAttribute(req, "message.quantity_bigger_fail");
					} else {
						cart.setTotalPrice((cart.getTotalPrice() / cart.getQuantityInCart()) * amountInCart);
						cart.setQuantityInCart(amountInCart);
						MessageAttributeUtil.setMessageAttribute(req, "message.quantity_in_cart_changed");
					}
				}
				totalPrice += cart.getTotalPrice();
			}
			req.getSession().setAttribute("cartList", listChange);
			req.getSession().setAttribute("totalPrice", totalPrice);
			return targetUrl;
		}
	}

}
