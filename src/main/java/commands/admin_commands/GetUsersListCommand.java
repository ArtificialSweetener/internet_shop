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
import service.UserService;
import service.impl.UserServiceImpl;
import util.MessageAttributeUtil;

public class GetUsersListCommand implements ICommand {
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(GetUsersListCommand.class);

	public GetUsersListCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		this.userService = new UserServiceImpl(userDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing GetUsersListCommand");
		String targetUrl = "/admin/users.jsp";
		int page = 1;
		int recordsPerPage = 4;
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
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
	}
}
