package commands.common_commands;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import models.Cart;
import util.MessageAttributeUtil;

public class DeleteFromCartCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(DeleteFromCartCommand.class);

	public DeleteFromCartCommand() {
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing DeleteFromCartCommand");
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("chosenProductId"));
		String targetUrl = "/common_pages/cart.jsp";

		if (chosenProductId.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.delete_prod_not_chosen_cart_fail");
			return targetUrl;
		} else {
			Optional<Object> cartOptional = Optional.ofNullable(req.getSession().getAttribute("cart"));
			if (cartOptional.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.delete_prod_fail_cart_empty");
				return targetUrl;
			} else {
				Cart cart = (Cart) cartOptional.get();
				long productId = Long.parseLong(chosenProductId.get());
				cart.removeProductById(productId);
				req.getSession().setAttribute("cart", cart);
				MessageAttributeUtil.setMessageAttribute(req, "message.product_deleted_cart");
				return targetUrl;
			}
		}
	}

}
