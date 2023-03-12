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

public class LoginCommand implements ICommand {
	private AuthentificationService authenticationService;
	private static final Logger logger = LogManager.getLogger(LoginCommand.class);

	public LoginCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		UserService userService = new UserServiceImpl(userDao);
		UserSaltDao userSaltDao = new UserSaltDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		UserSaltService userSaltService = new UserSaltServiceImpl(userSaltDao);
		authenticationService = new AuthentificationServiceImpl(userService, userSaltService);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("Login Command works"); 
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
			System.out.println("Found user: " + user);
		} catch (AuthentificationException e) {
			MessageAttributeUtil.setMessageAttribute(req, "message.invalid_details_try_another");
			return targetUrl_if_exception;
		}
		String greeting = "Welcome " + user.getUserName() + " " + user.getUserSurname() + "!";
		req.getSession().setAttribute("greeting", greeting);
		req.getSession().setAttribute("current_user", user);
		if (user.getUserType().equals("admin")) {
			req.getSession().setAttribute("role", "admin");
			System.out.println("Admin role set with session: " + req.getSession());
			return targetUrl_admin;
		} else if (user.getUserType().equals("normal")) {
			req.getSession().setAttribute("role", "normal");
			System.out.println("Normal role set with session: " + req.getSession());
			return targetUrl_user;
		} else {
			return targetUrl_if_fail;
		}
	}

}
