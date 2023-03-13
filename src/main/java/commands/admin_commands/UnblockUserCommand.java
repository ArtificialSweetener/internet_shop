package commands.admin_commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import models.User;

import service.UserService;
import service.impl.UserServiceImpl;
import util.MessageAttributeUtil;

/**
 * The UnblockUserCommand class is responsible for unblocking a user. Implements
 * the ICommand interface.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class UnblockUserCommand implements ICommand {
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(UnblockUserCommand.class);

	/**
	 * The constructor for the UnblockUserCommand class.
	 */
	public UnblockUserCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.userService = new UserServiceImpl(userDao);
	}

	/**
	 * Executes the UnblockUserCommand by unblocking a user.
	 * 
	 * @param req  the HttpServletRequest object
	 * @param resp the HttpServletResponse object
	 * @return the target URL for the user
	 */

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing UnblockUserCommand");
		String targetUrl = "/admin/users.jsp";
		int page = (Integer) req.getSession().getAttribute("currentPageAllUsers");
		int recordsPerPage = 4;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
		Optional<String> idOptional = Optional.ofNullable(req.getParameter("userId"));
		if (idOptional.isPresent()) {
			Optional<User> userOpt = userService.get(Long.parseLong(req.getParameter("userId")));

			if (userOpt.isPresent()) {
				User user = userOpt.get();
				user.setIs_bloked(false);
				userService.update(user);
				Optional<Object> userListOptional = Optional
						.ofNullable(userService.getAll((page - 1) * recordsPerPage, recordsPerPage));

				int noOfRecords = userService.getNoOfRecords();
				int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
				req.getSession().setAttribute("userList", userListOptional.get());
				MessageAttributeUtil.setMessageAttribute(req, "message.user_unblocked");
				req.getSession().setAttribute("noOfPagesAllUsers", noOfPages);
				req.getSession().setAttribute("currentPageAllUsers", page);
				return targetUrl;
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.user_not_unblocked"); // refactor
				return targetUrl;
			}
		} else {
			MessageAttributeUtil.setMessageAttribute(req, "message.user_not_unblocked");
			return targetUrl;
		}
	}
}
