package commands;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrderDao;
import dao.impl.OrderDaoImpl;
import models.Order;
import models.User;
import service.OrderService;
import service.impl.OrderServiceImpl;
import util.GlobalStringsProvider;

public class GetUserOrdersListCommand implements ICommand {

	private OrderService orderService;

	public GetUserOrdersListCommand() {
		OrderDao orderDao = new OrderDaoImpl();
		this.orderService = new OrderServiceImpl(orderDao);
	}
	
//do pagination here
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		Optional<Object> currentUserOptional = Optional.ofNullable(req.getSession().getAttribute("current_user"));
		int page = 1;
		int recordsPerPage = 3;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));}
		
		String forward_if_success = "orders_user.jsp";
		String forward_if_fail = "login.jsp";
		User user = null;
		if (currentUserOptional.isPresent()) {
			user = (User) currentUserOptional.get();
			Optional <List<Order>> orderListOptional = orderService.getAllOrdersByUserId(user.getUserId(), (page - 1) * recordsPerPage, recordsPerPage);
			if (orderListOptional.isEmpty() || orderListOptional.get().size() == 0) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("order_list_empty"));
				return forward_if_success;
			}else {
				int noOfRecords = orderService.getNoOfRecords();
				int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
				System.out.println(orderListOptional.get());
			req.getSession().setAttribute("allOrdersList", orderListOptional.get());
			req.getSession().setAttribute("noOfPagesAllUserOrders", noOfPages);
			req.getSession().setAttribute("currentPageAllUserOrders", page);
			return forward_if_success;
			} 
		} else {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("login_to_view"));
			return forward_if_fail;
		}
	}

}
