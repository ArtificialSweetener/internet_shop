package commands;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ItemDao;
import dao.OrderDao;
import dao.impl.ItemDaoImpl;
import dao.impl.OrderDaoImpl;
import models.Cart;
import models.Item;
import models.Order;
import models.User;
import service.ItemService;
import service.OrderService;
import service.impl.ItemServiceImpl;
import service.impl.OrderServiceImpl;
import util.GlobalStringsProvider;

public class CreateOrderCommand implements ICommand {
	private OrderService orderService;
	private ItemService itemService;

	public CreateOrderCommand() {
		OrderDao orderDao = new OrderDaoImpl();
		this.orderService = new OrderServiceImpl(orderDao);
		ItemDao itemdao = new ItemDaoImpl();
		this.itemService = new ItemServiceImpl(itemdao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		Optional<Object> currentUserOptional = Optional.ofNullable(req.getSession().getAttribute("current_user"));
		String redirect_if_failure = "checkout.jsp";
		String redirect_if_success = "normal.jsp";
		if (currentUserOptional.isEmpty()) {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("login_to_order"));
			return redirect_if_failure;
		} else {
			Optional<Object> cartListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
			Optional<String> orderPhoneOptional = Optional.ofNullable(req.getParameter("orderPhone"));
			Optional<String> orderAddressOptional = Optional.ofNullable(req.getParameter("orderAddress"));
			if (cartListOptional.isEmpty()) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("empty_order_fail"));
				return redirect_if_failure;
			} else if (orderPhoneOptional.isEmpty()) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("no_phone_order_fail"));
				return redirect_if_failure;
			} else if (orderAddressOptional.isEmpty()) {
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("no_address_order_fail"));
				return redirect_if_failure;
			} else {
				@SuppressWarnings("unchecked")
				List<Cart> chosenProductList = (List<Cart>) cartListOptional.get();
				User user = (User) currentUserOptional.get();
				Order order = new Order();
				order.setUserId(user.getUserId());
				order.setOrderDate(Date.valueOf(LocalDate.now()));
				order.setOrderTime(Time.valueOf(LocalTime.now()));
				order.setOrderAddress(orderAddressOptional.get());
				order.setOrderPhone(orderPhoneOptional.get());
				order.setOrderStatus("registered");
				orderService.create(order);
				for (Cart cart : chosenProductList) {
					Item item = new Item();
					item.setProductId(cart.getProductId());
					item.setOrderId(order.getOrderId());
					item.setItemQuantity(cart.getQuantityInCart());
					item.setItemPrice(cart.getProductPrice() * cart.getQuantityInCart());
					itemService.create(item);
				}
				// cartList
				req.getSession().removeAttribute("cartList");
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("order_placed"));
				return redirect_if_success;
			}
		}
	}

}
