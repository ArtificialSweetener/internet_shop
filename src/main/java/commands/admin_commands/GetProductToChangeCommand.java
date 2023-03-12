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
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

public class GetProductToChangeCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(GetProductToChangeCommand.class);

	public GetProductToChangeCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetProductToChangeCommand");
		String targetUrl = "/admin/change_product.jsp";
		String targetUrl_if_fail = "/admin/change_product_fail.jsp";
		String idString = req.getParameter("productId");
		if (!idString.isBlank()) {
			long id = Long.parseLong(idString);
			Optional<Product> productOpt = productService.get(id);
			if (productOpt.isPresent()) {
				req.getSession().setAttribute("product", productOpt.get());
				return targetUrl;
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_info_fail"); // refactor
				return targetUrl_if_fail;
			}
		} else {
			MessageAttributeUtil.setMessageAttribute(req, "message.prod_not_chosen_info_fail");
			return targetUrl_if_fail;
		}
	}
}
