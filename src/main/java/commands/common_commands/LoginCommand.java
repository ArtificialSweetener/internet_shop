package commands.common_commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commands.icommand.ICommand;
import dao.UserDao;
import dao.UserSaltDao;
import dao.impl.UserDaoImpl;
import dao.impl.UserSaltDaoImpl;
import dbconnection_pool.ConnectionPoolManager;
import exception.AuthentificationException;
import models.User;
import service.AuthentificationService;
import service.UserSaltService;
import service.UserService;
import service.impl.AuthentificationServiceImpl;
import service.impl.UserSaltServiceImpl;
import service.impl.UserServiceImpl;
import util.MessageAttributeUtil;

/**
 * This class represents a Login Command that implements the ICommand interface.
 * It retrieves the user email and password from the HTTP request object and
 * uses the authenticationService to authenticate the user. If the
 * authentication fails, the user is redirected to the login page with an error
 * message, otherwise the user is redirected to their corresponding page
 * (admin/normal) and their information is stored in the session object.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class LoginCommand implements ICommand {
	private AuthentificationService authenticationService;
	private static final Logger logger = LogManager.getLogger(LoginCommand.class);

	/**
	 * Constructor that initializes the authenticationService with the necessary
	 * services and DAOs.
	 */
	public LoginCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		UserService userService = new UserServiceImpl(userDao);
		UserSaltDao userSaltDao = new UserSaltDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		UserSaltService userSaltService = new UserSaltServiceImpl(userSaltDao);
		authenticationService = new AuthentificationServiceImpl(userService, userSaltService);
	}

	/**
	 * Executes the login command by retrieving the user email and password from the
	 * request, authenticating the user using the authenticationService and
	 * redirecting them to their corresponding page. If the authentication fails,
	 * the user is redirected to the login page with an error message.
	 * 
	 * @param req  the HTTP request object
	 * @param resp the HTTP response object
	 * @return the URL of the JSP page to which the user is redirected
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing LoginCommand");
		String email = req.getParameter("user_email");
		String password = req.getParameter("user_password");
		User user = null;
		String targetUrl_if_exception = "common_pages/login.jsp";
		String targetUrl_admin = "admin/admin.jsp";
		String targetUrl_user = "normal/normal.jsp";
		String targetUrl_if_fail = "common_pages/User type is not identified.jsp";
		try {
			user = authenticationService.login(email, password);
			logger.info("Found user: " + user);
		} catch (AuthentificationException e) {
			logger.error("An AuthentificationException occurred while executing LoginCommand", e);
			MessageAttributeUtil.setMessageAttribute(req, "message.invalid_details_try_another");
			return targetUrl_if_exception;
		}
		String greeting = "Welcome " + user.getUserName() + " " + user.getUserSurname() + "!";
		req.getSession().setAttribute("greeting", greeting);
		req.getSession().setAttribute("current_user", user);
		if (user.getUserType().equals("admin")) {
			req.getSession().setAttribute("role", "admin");
			logger.info("Admin role set with session: " + req.getSession());
			return targetUrl_admin;
		} else if (user.getUserType().equals("normal")) {
			req.getSession().setAttribute("role", "normal");
			logger.info("Normal role set with session: " + req.getSession());
			return targetUrl_user;
		} else {
			return targetUrl_if_fail;
		}
	}

}
