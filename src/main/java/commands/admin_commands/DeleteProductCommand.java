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
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

/**
 * The DeleteProductCommand class implements the ICommand interface and
 * represents a command that deletes a product from the database. It retrieves a
 * product ID from the request parameters, uses it to delete the product through
 * the ProductService object, and redirects to the product list page.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class DeleteProductCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(DeleteProductCommand.class);

	/**
	 * Constructs a new DeleteProductCommand object and initializes its
	 * productService field by creating a ProductDaoImpl object and passing it to a
	 * new ProductServiceImpl object.
	 */
	public DeleteProductCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	/**
	 * Executes this command by retrieving a product ID from the request parameters,
	 * using it to delete the product through the ProductService object, and
	 * redirecting to the product list page. If the product list is empty or a
	 * product ID is not specified, an appropriate error message is set in the
	 * request attributes and the user is redirected to the product list page.
	 * 
	 * @param req  the HttpServletRequest object containing the request parameters
	 * @param resp the HttpServletResponse object used to redirect the user
	 * @return a String representing the URL of the product list page
	 */

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing DeleteProductCommand");
		String targetUrl = "FrontController?command=GET_PRODUCTS_AND_PROPERTIES_LIST";
		Optional<Object> productsOptional = Optional.ofNullable(req.getSession().getAttribute("productList"));
		Optional<String> idOptional = Optional.ofNullable(req.getParameter("productId"));
		if (productsOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.delete_prod_fail_list_empty");
			return targetUrl;
		} else if (idOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_delete_fail");
			return targetUrl;
		} else {
			long id = Long.parseLong(idOptional.get());
			try {
				productService.delete(id);
				MessageAttributeUtil.setMessageAttribute(req, "message.product_deleted");
				return targetUrl;
			} catch (DataProcessingException e) {
				MessageAttributeUtil.setMessageAttribute(req, "message.prod_delete_fail_error");
				return targetUrl;
			}

		}
	}
}
