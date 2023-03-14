package commands.common_commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import models.Cart;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

/**
 * The AddToCartCommand class is responsible for handling requests to add a
 * product to the shopping cart. It implements the ICommand interface and
 * contains an execute method that retrieves the product ID from the request
 * parameter and creates a Cart object to add to the cart. It then updates the
 * cart in the session and returns the target URL to display the updated cart.
 * If the product ID is not present or is already in the cart, an error message
 * is displayed instead.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class AddToCartCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(AddToCartCommand.class);

	/**
	 * Constructs a new AddToCartCommand object with a new ProductServiceImpl
	 * object.
	 */
	public AddToCartCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	/**
	 * Executes the AddToCartCommand by retrieving the product ID from the request
	 * parameter, creating a Cart object, and adding it to the cart in the session.
	 * It then returns the target URL to display the updated cart or an error
	 * message if the product ID is not present or is already in the cart.
	 * 
	 * @param req  the HttpServletRequest object containing the request parameters
	 * @param resp the HttpServletResponse object for the response
	 * @return the target URL to display the updated cart or an error message
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddToCartCommand");
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("productId"));
		String targetUrl = "/common_pages/products.jsp";
		if (chosenProductId.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_fail_cart");
			return targetUrl;
		} else {
			long productId = Long.parseLong(chosenProductId.get());
			List<Cart> newCartList = new ArrayList<>();
			Cart cm = new Cart();
			cm.setProductId(productId);
			cm.setQuantityInCart(1);
			@SuppressWarnings("unchecked")
			List<Cart> oldCartList = (List<Cart>) req.getSession().getAttribute("cartList");
			if (oldCartList == null) {
				newCartList.add(cm);
				newCartList = productService.getCartProduct(newCartList);
				req.getSession().setAttribute("cartList", newCartList);
				MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_cart");
				return targetUrl;
			} else {
				newCartList = oldCartList;
				boolean exist = false;
				for (Cart cart : oldCartList) {
					if (cart.getProductId() == productId) {
						exist = true;
						MessageAttributeUtil.setMessageAttribute(req, "message.prod_already_cart");
						return targetUrl;
					}
				}
				if (!exist) {
					newCartList.add(cm);
					newCartList = productService.getCartProduct(newCartList);
					MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_cart");
					req.getSession().setAttribute("cartList", newCartList);
					return targetUrl;
				}
			}
			return targetUrl;
		}
	}
}
