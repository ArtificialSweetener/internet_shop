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
 * This command class handles changing the status of an order in the system. It
 * implements the ICommand interface. The class utilizes the OrderService to
 * get, update and manage orders in the system. The class also utilizes the
 * MessageAttributeUtil to set messages in the HttpServletRequest object.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class ChangeOrderStatusCommand implements ICommand {
	private OrderService orderService;

	private static final Logger logger = LogManager.getLogger(ChangeOrderStatusCommand.class);

	/**
	 * Constructs a new ChangeOrderStatusCommand instance and initializes its
	 * dependencies.
	 */
	public ChangeOrderStatusCommand() {
		OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.orderService = new OrderServiceImpl(orderDao);
	}

	/**
	 * Executes this command and changes the status of a given order.
	 * 
	 * @param req  the HttpServletRequest object.
	 * @param resp the HttpServletResponse object.
	 * 
	 * @return a String representing the target URL.
	 */

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing ChangeOrderStatusCommand");
		Optional<String> currentStatusOptional = Optional.ofNullable(req.getParameter("status"));
		Optional<String> orderIdOptional = Optional.ofNullable(req.getParameter("orderId"));
		Optional<Object> allOrdersListOptional = Optional.ofNullable(req.getSession().getAttribute("allOrdersList"));
		int page = (Integer) req.getSession().getAttribute("currentPageAllOrders");
		String targetUrl = "/admin/orders.jsp";
		int recordsPerPage = 4;

		if (currentStatusOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.please_select_order_stat");
			return targetUrl;
		} else if (orderIdOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.order_not_chosen_status_fail");
			return targetUrl;
		} else if (allOrdersListOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.no_orders_status_fail");
			return targetUrl;
		} else {
			try {
				Optional<Order> orderOpt = orderService.get(Long.parseLong(orderIdOptional.get()));
				if (orderOpt.isPresent()) {
					Order order = orderOpt.get();
					if (order.getOrderStatus().equalsIgnoreCase(currentStatusOptional.get())) {
						MessageAttributeUtil.setMessageAttribute(req, "message.order_status_already_fail");
						return targetUrl;
					} else {
						order.setOrderStatus(currentStatusOptional.get());

						orderService.update(order);
						List<Order> allOrdersList = orderService.getAll((page - 1) * recordsPerPage, recordsPerPage);
						int noOfRecords = orderService.getNoOfRecords();

						int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

						req.getSession().setAttribute("allOrdersList", allOrdersList);
						req.getSession().setAttribute("noOfPagesAllOrders", noOfPages);
						req.getSession().setAttribute("currentPageAllOrders", page);
						MessageAttributeUtil.setMessageAttribute(req, "message.order_status_updated");
						return targetUrl;
					}
				} else {
					MessageAttributeUtil.setMessageAttribute(req, "message.order_not_chosen_status_fail");
					return targetUrl;
				}
			} catch (DataProcessingException e) {
				MessageAttributeUtil.setMessageAttribute(req, "message.change_status_fail_error");
				return targetUrl;
			}
		}
	}
}
