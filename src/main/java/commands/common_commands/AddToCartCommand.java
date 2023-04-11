package commands.common_commands;

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
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

public class AddToCartCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(AddToCartCommand.class);

	public AddToCartCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddToCartCommand");
		System.out.println("AddToCartCommand");
		Optional<String> chosenProductId = Optional.ofNullable(req.getParameter("productId"));
		String targetUrl = "/common_pages/products.jsp";
		if (chosenProductId.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_fail_cart");
			return targetUrl;
		} else {
			long productId = Long.parseLong(chosenProductId.get());
			Optional<Product> productOpt = productService.get(productId);

			if (productOpt.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_fail_cart_product_doesn´t_exist");
				return targetUrl;
			}
			Product product = productOpt.get();
			Optional<Object> cartOpt = Optional.ofNullable(req.getSession().getAttribute("cart"));
			Cart cart = null;

			if (cartOpt.isEmpty()) {
				cart = new Cart();
				cart.addProduct(product, 1);
				req.getSession().setAttribute("cart", cart);
				MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_cart");
				return targetUrl;
			} else {
				cart = (Cart) cartOpt.get();
				if (cart.containsProductById(productId)) {
					System.out.println("we have this product already");
					MessageAttributeUtil.setMessageAttribute(req, "message.prod_already_cart");
					return targetUrl;
				} else {
					cart.addProduct(product, 1);
					req.getSession().setAttribute("cart", cart);
					MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_cart");
					return targetUrl;
				}
			}
		}
	}
}
