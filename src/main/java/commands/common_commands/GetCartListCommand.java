package commands.common_commands;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import models.Cart;
import util.MessageAttributeUtil;

public class GetCartListCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(GetCartListCommand.class);

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
