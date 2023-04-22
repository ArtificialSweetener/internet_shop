package commands.admin_commands;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.OrderDao;
import dao.impl.OrderDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.DataProcessingException;
import models.Order;
import service.OrderService;
import service.impl.OrderServiceImpl;
import util.MessageAttributeUtil;

/**
 * The GetAllOrdersListCommand class implements ICommand interface and provides
 * functionality to get all orders and display them on the admin orders page.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetAllOrdersListCommand implements ICommand {
	private OrderService orderService;
	private static final Logger logger = LogManager.getLogger(GetAllOrdersListCommand.class);

	/**
	 * Creates a new instance of the GetAllOrdersListCommand class with the default
	 * OrderService.
	 */
	public GetAllOrdersListCommand() {
		OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.orderService = new OrderServiceImpl(orderDao);
	}

	/**
	 * Executes the command to get all orders and display them on the admin orders
	 * page.
	 * 
	 * @param req  the HttpServletRequest object containing the request the client
	 *             has made of the servlet
	 * @param resp the HttpServletResponse object containing the response the
	 *             servlet sends to the client
	 * 
	 * @return the URL of the admin orders page
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetAllOrdersListCommand");
		String targetUrl = "/admin/orders.jsp";
		int page = 1;
		int recordsPerPage = 4;
		System.out.println("Page attribute is " + req.getParameter("page"));
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			System.out.println("Changed page attribute to " + page);
		}

		try {
			Optional<Object> orderListOptional = Optional
					.ofNullable(orderService.getAll((page - 1) * recordsPerPage, recordsPerPage));
			int noOfRecords = orderService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
			if (orderListOptional.isPresent()) {
				@SuppressWarnings("unchecked")
				List<Order> orderList = (List<Order>) orderListOptional.get();
				req.getSession().setAttribute("allOrdersList", orderList);
				req.getSession().setAttribute("noOfPagesAllOrders", noOfPages);
				req.getSession().setAttribute("currentPageAllOrders", page);
				return targetUrl;
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.order_list_empty");
				return targetUrl;
			}
		} catch (DataProcessingException e) {
			logger.error("A DataProcessingException occurred while executing GetAllOrdersListCommand", e);
			MessageAttributeUtil.setMessageAttribute(req, "message.order_list_error");
			return "/admin/admin.jsp";
		}
	}
}
