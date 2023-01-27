package commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import exception.DataProcessingException;
import models.Product;

import service.ProductService;
import service.impl.ProductServiceImpl;
import util.GlobalStringsProvider;

@MultipartConfig(maxFileSize = 16177215)
public class AddProductCommand implements ICommand {
	private ProductService productService;

	public AddProductCommand() {
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String productName = req.getParameter("pName");
		String productDescription = req.getParameter("pDescription");
		long colorId = Long.parseLong(req.getParameter("color_id"));
		long catId = Long.parseLong(req.getParameter("catId"));
		double productPrice = Double.parseDouble(req.getParameter("pPrice"));
		int productQuantity = Integer.parseInt(req.getParameter("pQuantity"));
		LocalDate productDate = LocalDate.now();
		LocalTime productTime = LocalTime.now();
		try {
			Part filePart = req.getPart("pPhoto");
			Product product = new Product();
			product.setProductName(productName);
			product.setProductDescription(productDescription);
			product.setColorId(colorId);
			product.setCategoryId(catId);
			product.setProductPrice(productPrice);
			product.setProductQuantity(productQuantity);
			product.setProductDate(productDate);
			product.setProductTime(productTime);
			product.setProductPhoto(filePart.getInputStream().readAllBytes());
			product.setProductPhotoName(filePart.getSubmittedFileName());
			productService.create(product);
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("add_prod"));
		} catch (DataProcessingException | IOException | ServletException e) {
			req.setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("add_prod_fail"));
		}
		String admin = "admin.jsp";
		return admin;
	}

}
