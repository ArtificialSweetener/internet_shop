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

@MultipartConfig(maxFileSize = 16177215)
public class AddProductCommand implements ICommand {
	private ProductService productService;
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(AddProductCommand.class);

	public AddProductCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		this.formValidator = new AddProductFormValidator(InputValidator.getInstance());
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing AddProductCommand");
		String productName = req.getParameter("pName").trim();
		String productDescription = req.getParameter("pDescription").trim();
		long colorId = Long.parseLong(req.getParameter("color_id"));
		long catId = Long.parseLong(req.getParameter("catId"));
		String productPrice = req.getParameter("pPrice").trim();
		String productQuantity = req.getParameter("pQuantity").trim();
		LocalDate productDate = LocalDate.now();
		LocalTime productTime = LocalTime.now();

		if (formValidator.validate(req) == false) {
			return "/admin/add_product.jsp";
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
			MessageAttributeUtil.setMessageAttribute(req, "message.add_prod_fail");
		}
		String targetUrl = "/admin/admin.jsp";
		return targetUrl;
	}

}