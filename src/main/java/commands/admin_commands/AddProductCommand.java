package commands.admin_commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.AddProductFormValidator;

/**
 * This class represents the command for adding a new product to the database.
 * It implements the {@link ICommand} interface and overrides its
 * {@link #execute(HttpServletRequest, HttpServletResponse)} method. Upon
 * execution, the command reads the necessary parameters from the request,
 * validates the form data using a {@link FormValidator}, creates a new
 * {@link Product} object and adds it to the database using a
 * {@link ProductService}.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

@MultipartConfig(maxFileSize = 16177215)
public class AddProductCommand implements ICommand {
	private ProductService productService;
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(AddProductCommand.class);

	/**
	 * Constructs a new AddProductCommand object and initializes its ProductService
	 * and FormValidator fields. The ProductService is created using a
	 * ProductDaoImpl object which is created with a ConnectionPool. The
	 * FormValidator is created using an AddProductFormValidator object which in
	 * turn uses an InputValidator.
	 */
	public AddProductCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		this.formValidator = new AddProductFormValidator(InputValidator.getInstance());
	}

	/**
	 * Executes the AddProductCommand. Reads the product data from the request,
	 * validates the form data, creates a new Product object and adds it to the
	 * database using the ProductService. If the form data is invalid or an error
	 * occurs while adding the product to the database, an appropriate message is
	 * set in the request attribute. Returns the target URL of the redirect after
	 * the command is executed.
	 *
	 * @param req  the HttpServletRequest object
	 * @param resp the HttpServletResponse object
	 * @return the target URL of the redirect after the command is executed
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddProductCommand");
		String targetUrl = "/admin/admin.jsp";
		String targetUrl_if_fail = "/admin/add_product.jsp";

		String productName = req.getParameter("pName").trim();
		String productDescription = req.getParameter("pDescription").trim();
		long colorId = Long.parseLong(req.getParameter("color_id"));
		long catId = Long.parseLong(req.getParameter("catId"));
		String productPrice = req.getParameter("pPrice").trim();
		String productQuantity = req.getParameter("pQuantity").trim();
		LocalDate productDate = LocalDate.now();
		LocalTime productTime = LocalTime.now();

		if (formValidator.validate(req) == false) {
			return targetUrl_if_fail;
		}
		try {
			Part filePart = req.getPart("pPhoto");
			Product product = new Product();
			product.setProductName(productName);
			product.setProductDescription(productDescription);
			product.setColorId(colorId);
			product.setCategoryId(catId);
			product.setProductPrice(Double.parseDouble(productPrice));
			product.setProductQuantity(Integer.parseInt(productQuantity));
			product.setProductDate(productDate);
			product.setProductTime(productTime);
			product.setProductPhoto(filePart.getInputStream().readAllBytes());
			product.setProductPhotoName(filePart.getSubmittedFileName());
			productService.create(product);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_prod");
		} catch (DataProcessingException | IOException | ServletException e) {
			logger.error("A DataProcessingException occurred while executing AddProductCommand", e);
			MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_fail");
			return targetUrl_if_fail;
		}
		return targetUrl;
	}

}
