package commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import exception.AuthentificationException;
import models.User;

import service.AuthentificationService;
import service.UserService;
import service.impl.AuthentificationServiceImpl;
import service.impl.UserServiceImpl;
import util.GlobalStringsProvider;

public class LoginCommand implements ICommand {
	private AuthentificationService authenticationService;

	public LoginCommand() {
		UserDao userDao = new UserDaoImpl();
		UserService userService = new UserServiceImpl(userDao);
		authenticationService = new AuthentificationServiceImpl(userService);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String email = req.getParameter("user_email");
		String password = req.getParameter("user_password");
		User user = null;
		String redirect_if_exception = "login.jsp";
		String redirect_admin = "admin.jsp";
		String redirect_user = "normal.jsp";
		String redirect_if_fail = "User type is not identified.jsp";
		try {
			user = authenticationService.login(email, password);
			System.out.println("Found user: " + user);
		} catch (AuthentificationException e) {
			req.setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("invalid_details_try_another"));
			return redirect_if_exception;
		}
		String greeting = "Welcome " + user.getUserName() + " " + user.getUserSurname() + "!";
		req.getSession().setAttribute("greeting", greeting);
		req.getSession().setAttribute("current_user", user);
		if (user.getUserType().equals("admin")) {
			return redirect_admin;
		} else if (user.getUserType().equals("normal")) {
			return redirect_user;
		} else {
			return redirect_if_fail;
		}
	}

}
