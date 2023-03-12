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
import service.ProductService;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

public class DeleteProductCommand implements ICommand {
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(DeleteProductCommand.class);

	public DeleteProductCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
	}

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
			productService.delete(id);
			MessageAttributeUtil.setMessageAttribute(req, "message.product_deleted");
			return targetUrl;
		}
	}
}
