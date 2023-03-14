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

/**
 * The CreateOrderCommand class implements the ICommand interface and represents
 * the command to create a new order based on the items in the cart and user's
 * order details. This command is responsible for validating the user's input,
 * creating a new order object and inserting it into the database, as well as
 * inserting the items in the order into the database. It also removes the cart
 * from the user's session and sets the necessary attributes on the request
 * object for rendering the normal.jsp page in case of success or checkout.jsp
 * page in case of failure.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class CreateOrderCommand implements ICommand {
	private OrderService orderService;
	private ItemService itemService;
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(CreateOrderCommand.class);

	/**
	 * Constructor of the CreateOrderCommand class. Initializes the orderService,
	 * itemService, and formValidator objects using the DAO implementations and
	 * ConnectionPoolManager to interact with the database.
	 */
	public CreateOrderCommand() {
		OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.orderService = new OrderServiceImpl(orderDao);
		ItemDao itemdao = new ItemDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.itemService = new ItemServiceImpl(itemdao);
		this.formValidator = new CreateOrderFormValidator(InputValidator.getInstance());
	}

	/**
	 * Executes the CreateOrderCommand. Validates the user's input, creates a new
	 * order object and inserts it into the database, as well as inserting the items
	 * in the order into the database. Also removes the cart from the user's session
	 * and sets the necessary attributes on the request object for rendering the
	 * normal.jsp page in case of success or checkout.jsp page in case of failure.
	 * 
	 * @param req  The HttpServletRequest object containing the request information.
	 * @param resp The HttpServletResponse object containing the response
	 *             information.
	 * @return A String representing the URL of the page to be rendered.
	 */
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
