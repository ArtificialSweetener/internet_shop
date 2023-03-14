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

/**
 * A command class that retrieves the cart list from the session and calculates
 * the total price of the items in the cart. It sets the cart list and total
 * price as attributes in the request session, and returns the URL of the cart
 * page.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetCartListCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(GetCartListCommand.class);

	/**
	 * Executes the command by retrieving the cart list from the session and
	 * calculating the total price of the items in the cart. It sets the cart list
	 * and total price as attributes in the request session, and returns the URL of
	 * the cart page.
	 * 
	 * @param req  the HttpServletRequest object
	 * @param resp the HttpServletResponse object
	 * @return the URL of the cart page
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetCartListCommand");
		String targetUrl = "/common_pages/cart.jsp";
		Optional<Object> cartListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
		double totalPrice = 0;

		if (cartListOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.cart_empty");
			req.getSession().setAttribute("totalPrice", totalPrice);
			return targetUrl;
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
				MessageAttributeUtil.setMessageAttribute(req, "message.login_to_order");
				return targetUrl;
			} else {
				return targetUrl;
			}
		}
	}

}
