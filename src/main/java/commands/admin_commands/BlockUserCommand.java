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
import exception.DataProcessingException;
import service.UserService;
import service.impl.UserServiceImpl;
import util.MessageAttributeUtil;

/**
 * The BlockUserCommand class is a command object that implements the ICommand
 * interface and is used to handle requests to block a user in the system. It
 * uses UserService to delete the user and redirects the user to the appropriate
 * view after processing the request.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class BlockUserCommand implements ICommand {
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(BlockUserCommand.class);

	/**
	 * Constructs a BlockUserCommand object and initializes its userService instance
	 * variable.
	 */
	public BlockUserCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.userService = new UserServiceImpl(userDao);
	}

	/**
	 * Executes the BlockUserCommand by deleting the specified user and redirecting
	 * to the users view.
	 *
	 * @param req  the HttpServletRequest object containing the request information
	 * @param resp the HttpServletResponse object containing the response
	 *             information
	 * @return a String representing the URL of the appropriate view to redirect the
	 *         user to
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing BlockUserCommand");
		String targetUrl = "/admin/users.jsp";
		int page = (Integer) req.getSession().getAttribute("currentPageAllUsers");
		int recordsPerPage = 4;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
		Optional<String> idOptional = Optional.ofNullable(req.getParameter("userId"));
		if (idOptional.isPresent()) {
			try {
				userService.delete(Long.parseLong(idOptional.get()));

				Optional<Object> userListOptional = Optional
						.ofNullable(userService.getAll((page - 1) * recordsPerPage, recordsPerPage));

				int noOfRecords = userService.getNoOfRecords();
				int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
				req.getSession().setAttribute("userList", userListOptional.get());
				MessageAttributeUtil.setMessageAttribute(req, "message.user_blocked");
				req.getSession().setAttribute("noOfPagesAllUsers", noOfPages);
			} catch (DataProcessingException e) {
				logger.error("A DataProcessingException occurred while executing BlockUserCommand", e);
				MessageAttributeUtil.setMessageAttribute(req, "message.user_not_blocked_error");
				return targetUrl;
			}
			req.getSession().setAttribute("currentPageAllUsers", page);
			return targetUrl;
		} else {
			MessageAttributeUtil.setMessageAttribute(req, "message.user_not_blocked");
			return targetUrl;
		}
	}
}
