package commands.common_commands;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.ItemDao;
import dao.ProductDao;
import dao.impl.ItemDaoImpl;
import dao.impl.ProductDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.DataProcessingException;
import models.Item;
import models.Product;
import service.ItemService;
import service.ProductService;
import service.impl.ItemServiceImpl;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;

/**
 * This class represents a command that retrieves all the items in a specific
 * order and displays them to the user with pagination. The command implements
 * the ICommand interface and requires the execute method to be implemented.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetAllOrderItemsCommand implements ICommand {
	private ProductService productService;
	private ItemService itemService;
	private static final Logger logger = LogManager.getLogger(GetAllOrderItemsCommand.class);

	/**
	 * Constructs a new GetAllOrderItemsCommand object and initializes the
	 * productService and itemService fields.
	 */
	public GetAllOrderItemsCommand() {
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		ItemDao itemdao = new ItemDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.itemService = new ItemServiceImpl(itemdao);
	}

	/**
	 * Retrieves all the items in a specific order and displays them to the user
	 * with pagination.
	 *
	 * @param req  The HttpServletRequest object that contains the request the
	 *             client has made of the servlet.
	 * @param resp The HttpServletResponse object that contains the response the
	 *             servlet sends to the client.
	 * @return A string that specifies the URL to which the user should be
	 *         redirected after the command has been executed.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetAllOrderItemsCommand");
		String targetUrl = "/common_pages/order_items.jsp";
		String referringPage = req.getHeader("referer");
		URI referringPageUri;
		String targetUrl_fail = "";
		try {
			referringPageUri = new URI(referringPage);
			String referringPageName = referringPageUri.getPath();
			String contextPath = req.getContextPath();
			targetUrl_fail = referringPageName.substring(referringPageName.indexOf(contextPath) + contextPath.length());

			Optional<String> currentOrderId = Optional.ofNullable(req.getParameter("orderId"));
			int page = 1;
			int recordsPerPage = 3;
			if (req.getParameter("page") != null) {
				page = Integer.parseInt(req.getParameter("page"));
			}
			Long id = Long.parseLong(currentOrderId.get());
			long totalOrderPrice = 0;

			List<Item> allItemsList = itemService.getAllByOrderId(id);
			if (!allItemsList.isEmpty()) {
				for (Item item : allItemsList) {
					totalOrderPrice += item.getItemPrice();
				}
				// pagination done
				List<Item> itemList = itemService.getAllByOrderId(id, (page - 1) * recordsPerPage, recordsPerPage);
				int noOfRecords = itemService.getNoOfRecords();
				int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
				List<Product> productList = new ArrayList<>();

				for (Item item : itemList) {
					Optional<Product> productOpt = productService.getNoDeleteCheck(item.getProductId());
					if (productOpt.isPresent()) {
						productList.add(productOpt.get());
					}
				}
				req.getSession().setAttribute("thisOrderId", id);
				req.getSession().setAttribute("itemList", itemList);
				req.getSession().setAttribute("productList", productList);
				req.getSession().setAttribute("noOfPagesAllItems", noOfPages);
				req.getSession().setAttribute("currentPageAllItems", page);
				req.getSession().setAttribute("totalOrderPrice", totalOrderPrice);
				// System.out.println("Number of pages is: " + noOfPages);
				// System.out.println("This page is:" + page);
				return targetUrl;
			}
		} catch (DataProcessingException e) {
			MessageAttributeUtil.setMessageAttribute(req, "message.order_items_error");
			return targetUrl_fail;

		} catch (URISyntaxException e) {
			MessageAttributeUtil.setMessageAttribute(req, "message.order_items_error");
			return "/error_page.jsp";
		}
		return targetUrl;
	}
}
