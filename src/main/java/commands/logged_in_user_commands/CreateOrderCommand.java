package commands.logged_in_user_commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.ItemDao;
import dao.OrderDao;
import dao.impl.ItemDaoImpl;
import dao.impl.OrderDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import models.Cart;
import models.Item;
import models.Order;
import models.User;
import service.ItemService;
import service.OrderService;
import service.impl.ItemServiceImpl;
import service.impl.OrderServiceImpl;
import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.CreateOrderFormValidator;

public class CreateOrderCommand implements ICommand {
	private OrderService orderService;
	private ItemService itemService;
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(CreateOrderCommand.class);

	public CreateOrderCommand() {
		OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.orderService = new OrderServiceImpl(orderDao);
		ItemDao itemdao = new ItemDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.itemService = new ItemServiceImpl(itemdao);
		this.formValidator = new CreateOrderFormValidator(InputValidator.getInstance());
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing CreateOrderCommand");
		Optional<Object> currentUserOptional = Optional.ofNullable(req.getSession().getAttribute("current_user"));
		String targetUrl_if_failure = "/normal/checkout.jsp";
		String targetUrl_if_success = "/normal/normal.jsp";
		if (currentUserOptional.isEmpty()) {
			MessageAttributeUtil.setMessageAttribute(req, "message.login_to_order");
			return targetUrl_if_failure;
		} else {
			Optional<Object> cartListOptional = Optional.ofNullable(req.getSession().getAttribute("cartList"));
			String orderCountryCode = req.getParameter("orderCountryCode").replaceAll("\\s", "");
			String orderPhone = req.getParameter("orderPhone").replaceAll("\\s", "");
			String orderAddress = req.getParameter("orderAddress").trim();
			if (cartListOptional.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.empty_order_fail");
				return targetUrl_if_failure;
			} else {
				if (formValidator.validate(req) == false) {
					return targetUrl_if_failure;
				} else {
					@SuppressWarnings("unchecked")
					List<Cart> chosenProductList = (List<Cart>) cartListOptional.get();
					User user = (User) currentUserOptional.get();
					Order order = new Order();
					order.setUserId(user.getUserId());
					order.setOrderDate(LocalDate.now());
					order.setOrderTime(LocalTime.now());
					order.setOrderAddress(orderAddress);
					order.setOrderPhone("+" + orderCountryCode + orderPhone);
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
					MessageAttributeUtil.setMessageAttribute(req, "message.order_placed");
					return targetUrl_if_success;
				}
			}
		}
	}
}
