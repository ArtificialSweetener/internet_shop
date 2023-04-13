package commands.admin_commands;

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
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

/**
 * The GetProductToChangeCommand class implements the ICommand interface and
 * provides functionality to retrieve a product from the database by ID and set
 * it as a session attribute for use on the change product page in the admin
 * panel.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetProductToChangeCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(GetProductToChangeCommand.class);

	/**
	 * Constructs a new GetProductToChangeCommand object. Initializes a new
	 * ProductService instance with a new ProductDaoImpl instance, which is injected
	 * with a ConnectionPoolManager instance to obtain a connection pool for
	 * database access.
	 */
	public GetProductToChangeCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	/**
	 * Executes the GetProductToChangeCommand and retrieves the product with the
	 * given ID from the database. If the ID is invalid or not provided, an error
	 * message is set as a request attribute and the user is redirected to the
	 * change product fail page. If the product is retrieved successfully, it is set
	 * as a session attribute and the user is redirected to the change product page.
	 * 
	 * @param req  The HTTP servlet request object
	 * @param resp The HTTP servlet response object
	 * @return The target URL for the change product page or change product fail
	 *         page depending on the outcome of the command
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetProductToChangeCommand");
		String targetUrl = "/admin/change_product.jsp";
		String targetUrl_if_fail = "/admin/change_product_fail.jsp";
		String idString = req.getParameter("productId");
		if (!idString.isBlank()) {
			long id = Long.parseLong(idString);
			try {
				Optional<Product> productOpt = productService.get(id);
				if (productOpt.isPresent()) {
					req.getSession().setAttribute("product", productOpt.get());
					return targetUrl;
				} else {
					MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_info_fail"); // refactor
					return targetUrl_if_fail;
				}
			} catch (DataProcessingException e) {
				MessageAttributeUtil.setMessageAttribute(req, "message.prod_info_error"); // refactor
				return targetUrl_if_fail;
			}
		} else {
			MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_info_fail");
			return targetUrl_if_fail;
		}
	}
}
