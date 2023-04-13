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
import exception.DataProcessingException;
import models.Cart;
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

/**
 * The AddToCartCommand class implements ICommand and is responsible for adding
 * products to the shopping cart.
 * 
 * It retrieves the chosen product's ID from the request parameter and validates
 * it. If the product exists, it is added to the cart.
 * 
 * If the cart does not exist, a new one is created and the product is added to
 * it. If the product is already in the cart, an appropriate message is
 * displayed to the user. The results of this command are then forwarded to the
 * "/common_pages/products.jsp" page.
 * 
 * This class uses a ProductService to get the Product by ID and a Cart object
 * to store the added products. The ConnectionPoolManager provides the
 * ConnectionPool instance, which is used to instantiate the ProductDao and
 * ProductDao is used to instantiate ProductService.
 * 
 * The logger is used to log information when executing the command. The
 * MessageAttributeUtil is used to set the appropriate message attributes
 * depending on the result of the command.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 *
 */
public class AddToCartCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(AddToCartCommand.class);

	/**
	 * Constructs a new AddToCartCommand object and initializes it with a ProductDao
	 * instance and a ProductService instance that uses the ProductDao object.
	 */
	public AddToCartCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	/**
	 * Executes the AddToCartCommand logic. Retrieves the product id from the
	 * request parameters, retrieves the product from the database using
	 * ProductService and adds it to the user's cart. If the user does not have a
	 * cart in the session, a new Cart object is created and stored in the session.
	 * If the user already has the product in the cart, the user gets a message. If
	 * an error occurs, an appropriate error message is set as a request attribute.
	 * 
	 * @param req  the HttpServletRequest object containing the request parameters
	 * @param resp the HttpServletResponse object containing the response parameters
	 * @return the URL of the page to which the request should be forwarded
	 */

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
			try {
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
			} catch (DataProcessingException e) {
				MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_fail_cart_error");
				return targetUrl;
			}
		}
	}
}
