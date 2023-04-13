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
 * The GetUsersListCommand class implements the ICommand interface and provides
 * functionality to get a list of all users and display them in the admin panel.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class GetUsersListCommand implements ICommand {
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(GetUsersListCommand.class);

	/**
	 * Constructor of GetUsersListCommand class.
	 * 
	 * Initializes the UserService object.
	 */
	public GetUsersListCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.userService = new UserServiceImpl(userDao);
	}

	/**
	 * Executes the command to get a list of all users and display them in the admin
	 * panel.
	 * 
	 * @param req  The HttpServletRequest object containing the request from the
	 *             client.
	 * @param resp The HttpServletResponse object for the response to be sent to the
	 *             client.
	 * @return A String representing the URL of the admin panel's users page, or the
	 *         URL of the error page if the user list is empty.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetUsersListCommand");
		String targetUrl = "/admin/users.jsp";
		String targetUrl_if_fail = "/admin/admin.jsp";
		int page = 1;
		int recordsPerPage = 4;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
		try {
			Optional<Object> userListOptional = Optional
					.ofNullable(userService.getAll((page - 1) * recordsPerPage, recordsPerPage));
			int noOfRecords = userService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
			if (userListOptional.isPresent()) {
				req.getSession().setAttribute("userList", userListOptional.get());
				req.getSession().setAttribute("noOfPagesAllUsers", noOfPages);
				req.getSession().setAttribute("currentPageAllUsers", page);
				return targetUrl;
			} else {
				MessageAttributeUtil.setMessageAttribute(req, "message.user_list_empty");
				return targetUrl;
			}
		} catch (DataProcessingException e) {
			MessageAttributeUtil.setMessageAttribute(req, "message.user_list_error");
			return targetUrl_if_fail;
		}
	}
}
