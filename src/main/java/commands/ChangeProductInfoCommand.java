package commands;

import java.io.IOException;
import java.util.Optional;

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
public class ChangeProductInfoCommand implements ICommand {
	private ProductService productService;

	public ChangeProductInfoCommand() {
		ProductDao productDao = new ProductDaoImpl();
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String redirect = "change_product.jsp";
		String redirect_if_fail = "change_product_fail.jsp";
		String productName = req.getParameter("pName");
		String productDescription = req.getParameter("pDescription");
		String colorId = req.getParameter("color_id");
		String catId = req.getParameter("catId");
		String productPrice = req.getParameter("pPrice");
		String productQuantity = req.getParameter("pQuantity");
		Optional<Object> oldProductOptional = Optional.ofNullable(req.getSession().getAttribute("product"));
		if (oldProductOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("prod_not_chosen_info_update_fail"));
			return redirect_if_fail;
		} else {
			try {
				Product oldProduct = (Product) req.getSession().getAttribute("product");
				Part filePart = req.getPart("pPhoto");
				Product product = new Product();
				if (productName.equals("")) {
					product.setProductName(oldProduct.getProductName());
				} else {
					product.setProductName(productName);
				}
				if (productDescription.equals("")) {
					product.setProductDescription(oldProduct.getProductDescription());
				} else {
					product.setProductDescription(productDescription);
				}

				if (colorId.equals("")) {
					product.setColorId(oldProduct.getColorId());
				} else {
					product.setColorId(Integer.parseInt(colorId));
				}
				if (catId.equals("")) {
					product.setCategoryId(oldProduct.getCategoryId());
				} else {
					product.setCategoryId(Integer.parseInt(catId));
				}
				if (productPrice.equals("")) {
					product.setProductPrice(oldProduct.getProductPrice());
				} else {
					product.setProductPrice(Integer.parseInt(productPrice));
				}
				if (productQuantity.equals("")) {
					product.setProductQuantity(oldProduct.getProductQuantity());
				} else {
					product.setProductQuantity(Integer.parseInt(productQuantity));
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
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("product_updated"));
			} catch (DataProcessingException | IOException | ServletException e) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("product_update_fail"));
				return redirect;
			}
			return redirect;
		}
	}

}
