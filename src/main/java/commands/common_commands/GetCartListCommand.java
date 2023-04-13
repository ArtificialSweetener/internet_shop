package commands.common_commands;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import models.Cart;
import util.MessageAttributeUtil;

/**
 * A command that retrieves the cart list of the current user and sets it as a
 * session attribute. If the user is not logged in, it sets an error message and
 * redirects to the cart page.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetCartListCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(GetCartListCommand.class);

	/**
	 * Executes the command to retrieve the current user's cart and display it on
	 * the cart page. If the user is not logged in, the command will set an
	 * appropriate error message and return to the cart page. If the cart is empty,
	 * the command will set an appropriate message and return to the cart page.
	 * 
	 * @param req  the HttpServletRequest object containing the request parameters
	 *             and attributes
	 * @param resp the HttpServletResponse object for sending the response to the
	 *             client
	 * @return a String representing the target URL for the response
	 */

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetCartListCommand");
		String targetUrl = "/common_pages/cart.jsp";
		Optional<Object> cartOptional = Optional.ofNullable(req.getSession().getAttribute("cart"));

		if (cartOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.cart_empty");
			return targetUrl;
		} else {
			Cart cart = (Cart) cartOptional.get();
			req.getSession().setAttribute("cart", cart);
			Optional<Object> currentUserOptional = Optional.ofNullable(req.getSession().getAttribute("current_user"));
			if (currentUserOptional.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.login_to_order");
				return targetUrl;
			}
			return targetUrl;
		}
	}
}
