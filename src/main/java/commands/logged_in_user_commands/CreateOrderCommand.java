package commands.logged_in_user_commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.ItemDao;
import dao.OrderDao;
import dao.ProductDao;
import dao.impl.ItemDaoImpl;
import dao.impl.OrderDaoImpl;
import dao.impl.ProductDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.DataProcessingException;
import models.Cart;
import models.Item;
import models.Order;
import models.Product;
import models.User;
import service.ItemService;
import service.OrderService;
import service.ProductService;
import service.impl.ItemServiceImpl;
import service.impl.OrderServiceImpl;
import service.impl.ProductServiceImpl;
import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.CreateOrderFormValidator;

/**
 * A command class that implements the ICommand interface to handle the creation
 * of a new order by a logged-in user.
 * 
 * It retrieves the cart from the user's session and validates the order form
 * before creating an order object, associated items, and updating the product
 * quantities. Finally, it removes the cart from the user's session and
 * redirects them to the normal page if the order was successfully placed,
 * otherwise to the checkout page if there was an error in the order creation
 * process.
 */
public class CreateOrderCommand implements ICommand {
	private OrderService orderService;
	private ItemService itemService;
	private ProductService productService;
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(CreateOrderCommand.class);

	/**
	 * Constructs a new CreateOrderCommand object by initializing the DAO, service,
	 * and validator objects that will be used in the execution of the command.
	 */
	public CreateOrderCommand() {
		OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.orderService = new OrderServiceImpl(orderDao);
		ItemDao itemdao = new ItemDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.itemService = new ItemServiceImpl(itemdao);
		ProductDao productDao = new ProductDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.productService = new ProductServiceImpl(productDao);
		this.formValidator = new CreateOrderFormValidator(InputValidator.getInstance());
	}

	/**
	 * Executes the CreateOrderCommand by validating the order form, creating a new
	 * order object and associated items, and updating the product quantities. It
	 * then removes the cart from the user's session and sets a success message to
	 * be displayed to the user. Finally, it returns the target URL for redirection,
	 * depending on whether the order was successfully placed or not.
	 * 
	 * @param req  the HttpServletRequest object containing the request parameters
	 *             and attributes
	 * @param resp the HttpServletResponse object used to send the response
	 * @return the target URL for redirection
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
			Optional<Object> cartOpt = Optional.ofNullable(req.getSession().getAttribute("cart"));
			String orderCountryCode = req.getParameter("orderCountryCode").replaceAll("\\s", "");
			String orderPhone = req.getParameter("orderPhone").replaceAll("\\s", "");
			String orderAddress = req.getParameter("orderAddress").trim();
			if (cartOpt.isEmpty()) {
				MessageAttributeUtil.setMessageAttribute(req, "message.empty_order_fail");
				return targetUrl_if_failure;
			} else {
				if (formValidator.validate(req) == false) {
					return targetUrl_if_failure;
				} else {
					Cart cart = (Cart) cartOpt.get();
					User user = (User) currentUserOptional.get();
					Order order = new Order();
					order.setUserId(user.getUserId());
					order.setOrderDate(LocalDate.now());
					order.setOrderTime(LocalTime.now());
					order.setOrderAddress(orderAddress);
					order.setOrderPhone("+" + orderCountryCode + orderPhone);
					order.setOrderStatus("registered");
					try {
						orderService.create(order);
						for (Product product : cart.getProducts().keySet()) {
							Item item = new Item();
							item.setProductId(product.getProductId());
							item.setOrderId(order.getOrderId());
							item.setItemQuantity(cart.getProductQuantity(product));
							item.setItemPrice(cart.getProductTotalPrice(product));
							itemService.create(item);
							product.setProductQuantity(product.getProductQuantity() - cart.getProductQuantity(product));
							productService.update(product);
						}
						req.getSession().removeAttribute("cart");
						MessageAttributeUtil.setMessageAttribute(req, "message.order_placed");
						return targetUrl_if_success;
					} catch (DataProcessingException e) {
						logger.error("DataProcessingException occurred while executing CreateOrderCommand", e);
						MessageAttributeUtil.setMessageAttribute(req, "message.order_not_placed_error");
						return targetUrl_if_failure;
					}
				}
			}
		}
	}
}
