package commands;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrderDao;
import dao.impl.OrderDaoImpl;
import models.Order;
import service.OrderService;
import service.impl.OrderServiceImpl;
import util.GlobalStringsProvider;

public class ChangeOrderStatusCommand implements ICommand {
	private OrderService orderService;

	public ChangeOrderStatusCommand() {
		OrderDao orderDao = new OrderDaoImpl();
		this.orderService = new OrderServiceImpl(orderDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		Optional<String> currentStatusOptional = Optional.ofNullable(req.getParameter("status"));
		Optional<String> orderIdOptional = Optional.ofNullable(req.getParameter("orderId"));
		Optional<Object> allOrdersListOptional = Optional.ofNullable(req.getSession().getAttribute("allOrdersList"));
		String redirect = "orders.jsp";
		if (currentStatusOptional.isEmpty()) {
			req.getSession().setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("please_select_order_stat"));
			return redirect;
		} else if (orderIdOptional.isEmpty()) {
			req.getSession().setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("order_not_chosen_status_fail"));
			return redirect;
		} else if (allOrdersListOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("no_orders_status_fail"));
			return redirect;
		} else {
			Order order = orderService.get(Long.parseLong(orderIdOptional.get()));
			if (order.getOrderStatus().equalsIgnoreCase(currentStatusOptional.get())) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("order_status_already_fail"));
				return redirect;
			} else {
				order.setOrderStatus(currentStatusOptional.get());
				orderService.update(order);
				List<Order> allOrdersList = orderService.getAll();
				req.getSession().setAttribute("allOrdersList", allOrdersList);
				req.getSession().setAttribute("message", GlobalStringsProvider.getInstance().getLocalizationMap().get("order_status_updated"));
				return redirect;
			}
		}
	}

}
