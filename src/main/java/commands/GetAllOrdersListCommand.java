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


public class GetAllOrdersListCommand implements ICommand {
	private OrderService orderService;

	public GetAllOrdersListCommand() {
		OrderDao orderDao = new OrderDaoImpl();
		this.orderService = new OrderServiceImpl(orderDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String forward = "orders.jsp";
		
		int page = 1;
		int recordsPerPage = 4;
		System.out.println("Page attribute is " + req.getParameter("page"));
			if(req.getParameter("page") != null) {
	            page = Integer.parseInt(req.getParameter("page"));
			System.out.println("Changed page attribute to " + page);
			}
			
		Optional<Object> orderListOptional = Optional.ofNullable(orderService.getAll((page-1)*recordsPerPage,
                recordsPerPage));
		
		 int noOfRecords = orderService.getNoOfRecords();
	        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

		if (orderListOptional.isPresent()) {

			@SuppressWarnings("unchecked")
			List<Order> orderList = (List<Order>) orderListOptional.get();
			
			req.getSession().setAttribute("allOrdersList", orderList);
			req.getSession().setAttribute("noOfPagesAllOrders", noOfPages);
			req.getSession().setAttribute("currentPageAllOrders", page);
			return forward;
		} else {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("order_list_empty"));
			return forward;
		}
	}
}
