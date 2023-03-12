package commands.common_commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import models.Product;
import util.MessageAttributeUtil;

public class DeleteFromCartCommand implements ICommand {
	private static final Logger logger = LogManager.getLogger(DeleteFromCartCommand.class);

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing DeleteFromCartCommand");
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("chosenProductId"));
		String targetUrl = "/common_pages/cart.jsp";
		if (chosenProductId.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.delete_prod_not_chosen_cart_fail");
			return targetUrl;
		} else {
			Optional<Object> chosenProductListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
			if (chosenProductListOptional.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.delete_prod_fail_cart_empty");
				return targetUrl;
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
				MessageAttributeUtil.setMessageAttribute(req, "message.product_deleted_cart");
				return targetUrl;
			}
		}
	}

}
