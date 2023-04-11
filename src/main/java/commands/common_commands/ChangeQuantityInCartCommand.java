package commands.common_commands;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import models.Cart;
import models.Product;
import util.MessageAttributeUtil;
import util.validators.InputValidator;

public class ChangeQuantityInCartCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(ChangeQuantityInCartCommand.class);

	public ChangeQuantityInCartCommand() {
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ChangeQuantityInCartCommand");
		String chosenProductId = req.getParameter("chosenProductId");
		Optional<Object> cartOptional = Optional.ofNullable(req.getSession().getAttribute("cart"));
		String quantityString = req.getParameter("quantity").trim();
		String targetUrl = "/common_pages/cart.jsp";

		if (chosenProductId.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_cart_quantity_fail"); // done
			return targetUrl;
		} else if (cartOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.cart_empty"); // done
			return targetUrl;
		} else if (quantityString.isBlank()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.quantity_not_specified"); // done
			return targetUrl;
		} else if (Integer.parseInt(quantityString) == 0) {
			MessageAttributeUtil.setMessageAttribute(req, "message.quantity_not_zero"); // done
			return targetUrl;
		} else if (!InputValidator.getInstance().isNumberValid(quantityString)) {
			MessageAttributeUtil.setMessageAttribute(req, "message.quantity_not_valid"); // done
			return targetUrl;
		} else {
			long id = Long.parseLong(chosenProductId);
			Cart cart = (Cart) cartOptional.get();
			int quantity = Integer.parseInt(quantityString);
			Optional<Product> productOpt = cart.getProductById(id);

			if (productOpt.isPresent()) {
				Product product = productOpt.get();
				if (quantity > product.getProductQuantity()) {
					MessageAttributeUtil.setMessageAttribute(req, "message.quantity_bigger_fail");
				} else {
					cart.updateQuantity(product, quantity);
					MessageAttributeUtil.setMessageAttribute(req, "message.quantity_in_cart_changed");
				}
			}
			req.getSession().setAttribute("cart", cart);
			return targetUrl;
		}
	}
}
