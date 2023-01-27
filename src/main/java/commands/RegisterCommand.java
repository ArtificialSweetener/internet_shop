package commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.impl.UserDaoImpl;

import models.User;

import service.UserService;
import service.impl.UserServiceImpl;
import util.GlobalStringsProvider;
import util.validators.EmailValidator;
import util.validators.NameValidator;
import util.validators.PasswordValidator;
import util.validators.SurnameValidator;


public class RegisterCommand implements ICommand {
	private UserService userService;

	public RegisterCommand() {
		UserDao userDao = new UserDaoImpl();
		userService = new UserServiceImpl(userDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("register command is invoked");

		String redirect = "register.jsp";

		Optional<String> userNameOptional = Optional.ofNullable(req.getParameter("user_name"));
		Optional<String> userSurnameOptional = Optional.ofNullable(req.getParameter("user_surname"));
		Optional<String> userEmailOptional = Optional.ofNullable(req.getParameter("user_email"));
		Optional<String> userPasswordOptional = Optional.ofNullable(req.getParameter("user_password"));
		Optional<String> repeatPasswordOptional = Optional.ofNullable(req.getParameter("password-repeat"));

		if (userNameOptional.isEmpty() || userSurnameOptional.isEmpty() || userEmailOptional.isEmpty()
				|| userPasswordOptional.isEmpty() || repeatPasswordOptional.isEmpty()) {
			System.out.println("empty input in form");
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("fields_empty_registration_fail"));
			return redirect;
		} else {
			String userName = userNameOptional.get();
			String userSurname = userSurnameOptional.get();
			String userEmail = userEmailOptional.get();
			System.out.println(userEmail);
			String userPassword = userPasswordOptional.get();
			String repPassword = repeatPasswordOptional.get();

			boolean validName = NameValidator.getInstance().isValid(userName);
			boolean validSurname = SurnameValidator.getInstance().isValid(userSurname);
			boolean validEmail = EmailValidator.getInstance().isValid(userEmail);
			boolean validPassword = PasswordValidator.getInstance().isValid(userPassword);

			if (!validName || !validSurname || !validEmail || !validPassword) {
				System.out.println("not valid input in form");
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("data_not_valid"));
				return redirect;
			} else if (!userPassword.equals(repPassword)) {
				System.out.println("passwords not matching");
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("pass_not_match"));
				return redirect;
			} else if (userService.findByEmail(userEmail).isPresent()) {
				System.out.println("this email is already registered on the site.");
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("email_already_registered"));
				return redirect;
			} else {
				User user = new User(userName, userSurname, userEmail, userPassword, "normal");
				userService.create(user);
				req.getSession().setAttribute("message",
						GlobalStringsProvider.getInstance().getLocalizationMap().get("registration_successful") + " "
								+ GlobalStringsProvider.getInstance().getLocalizationMap().get("welcome") + " "
								+ user.getUserName() + " " +  user.getUserSurname() + "!");
				return redirect;
			}
		}
	}
}
