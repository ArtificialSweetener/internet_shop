package commands.logged_in_user_commands;

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
import models.Order;
import models.User;
import service.OrderService;
import service.impl.OrderServiceImpl;
import util.MessageAttributeUtil;

public class GetUserOrdersListCommand implements ICommand {

	private OrderService orderService;
	private static final Logger logger = LogManager.getLogger(GetUserOrdersListCommand.class);

	public GetUserOrdersListCommand() {
		OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.orderService = new OrderServiceImpl(orderDao);
	}

//do pagination here
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetUserOrdersListCommand");
		Optional<Object> currentUserOptional = Optional.ofNullable(req.getSession().getAttribute("current_user"));
		int page = 1;
		int recordsPerPage = 3;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}

		String targetUrl = "/normal/orders_user.jsp";
		String targetUrl_if_fail = "/normal/login.jsp";
		User user = null;
		if (currentUserOptional.isPresent()) {
			user = (User) currentUserOptional.get();
			List<Order> orderList = orderService.getAllOrdersByUserId(user.getUserId(),
					(page - 1) * recordsPerPage, recordsPerPage);
			if (orderList.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.order_list_empty");
				return targetUrl;
			} else {
				int noOfRecords = orderService.getNoOfRecords();
				int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
				System.out.println(orderList);
				req.getSession().setAttribute("allOrdersList", orderList);
				req.getSession().setAttribute("noOfPagesAllUserOrders", noOfPages);
				req.getSession().setAttribute("currentPageAllUserOrders", page);
				return targetUrl;
			}
		} else {
			MessageAttributeUtil.setMessageAttribute(req, "message.login_to_view");
			return targetUrl_if_fail;
		}
	}

}
