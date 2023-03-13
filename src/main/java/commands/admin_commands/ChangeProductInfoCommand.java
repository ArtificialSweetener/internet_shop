package commands.admin_commands;

import java.io.IOException;
import java.util.Optional;
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
import util.validators.InputValidator;

/**
 * This command class handles changing the information of a product in the
 * system. It implements the ICommand interface and utilizes the ProductService
 * to manage products. The class also utilizes the MessageAttributeUtil to set
 * messages in the HttpServletRequest object.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
@MultipartConfig(maxFileSize = 16177215)
public class ChangeProductInfoCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(ChangeProductInfoCommand.class);

	/**
	 * Constructs a new instance of the ChangeProductInfoCommand class. It
	 * initializes a ProductDaoImpl object and uses it to create a new
	 * ProductService object.
	 */
	public ChangeProductInfoCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	/**
	 * Updates the information of a product in the system.
	 * 
	 * @param req  the HttpServletRequest object containing information about the
	 *             request
	 * @param resp the HttpServletResponse object used to send the response
	 * @return a String representing the target URL to redirect to after execution
	 * 
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ChangeProductInfoCommand");
		String targetUrl = "/admin/change_product.jsp";
		String targetUrl_if_fail = "/admin/change_product_fail.jsp";

		String productName = req.getParameter("pName").trim();
		String productDescription = req.getParameter("pDescription").trim();
		String colorId = req.getParameter("color_id");
		String catId = req.getParameter("catId");
		String productPrice = req.getParameter("pPrice").trim();
		String productQuantity = req.getParameter("pQuantity").trim();

		Optional<Object> oldProductOptional = Optional.ofNullable(req.getSession().getAttribute("product"));
		if (oldProductOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_info_update_fail");
			return targetUrl_if_fail;
		} else {
			try {
				Product oldProduct = (Product) req.getSession().getAttribute("product");
				Part filePart = req.getPart("pPhoto");
				Product product = new Product();
				if (productName.isBlank()) {
					product.setProductName(oldProduct.getProductName());
				} else {
					if (InputValidator.getInstance().isTitleValid(productName)) {
						product.setProductName(productName);
					} else {
						MessageAttributeUtil.setMessageAttribute(req, "message.product_name_not_valid"); // done
					}
				}
				if (productDescription.isBlank()) {
					product.setProductDescription(oldProduct.getProductDescription());
				} else {
					if (InputValidator.getInstance().isDescriptionValid(productDescription)) {
						product.setProductDescription(productDescription);
					} else {
						MessageAttributeUtil.setMessageAttribute(req, "message.product_desc_not_valid"); // done
					}
				}

				if (colorId.isBlank()) {
					product.setColorId(oldProduct.getColorId());
				} else {
					product.setColorId(Integer.parseInt(colorId));
				}
				if (catId.isBlank()) {
					product.setCategoryId(oldProduct.getCategoryId());
				} else {
					product.setCategoryId(Integer.parseInt(catId));
				}
				if (productPrice.isBlank()) {
					product.setProductPrice(oldProduct.getProductPrice());
				} else {
					if (InputValidator.getInstance().isPriceValid(productPrice)) {
						product.setProductPrice(Integer.parseInt(productPrice));
					} else {
						MessageAttributeUtil.setMessageAttribute(req, "message.product_price_not_valid"); // done
						return targetUrl_if_fail;
					}
				}
				if (productQuantity.isBlank()) {
					product.setProductQuantity(oldProduct.getProductQuantity());
				} else {
					if (InputValidator.getInstance().isNumberValid(productQuantity)) {
						product.setProductQuantity(Integer.parseInt(productQuantity));
					} else {
						MessageAttributeUtil.setMessageAttribute(req, "message.product_quantity_not_valid"); // done
						return targetUrl_if_fail;
					}
				}
				if (filePart.equals(null) || filePart.getSize() == 0) {
					product.setProductPhoto(oldProduct.getProductPhoto());
					product.setProductPhotoName(oldProduct.getProductPhotoName());
				} else {
					product.setProductPhoto(filePart.getInputStream().readAllBytes());
					product.setProductPhotoName(filePart.getSubmittedFileName());
				}
				product.setProductId(oldProduct.getProductId());
				productService.update(product);
				MessageAttributeUtil.setMessageAttribute(req, "message.product_updated");
			} catch (DataProcessingException | IOException | ServletException e) {
				MessageAttributeUtil.setMessageAttribute(req, "message.product_update_fail");
				return targetUrl;
			}
			return targetUrl;
		}
	}

}
