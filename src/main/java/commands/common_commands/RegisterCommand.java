package commands.common_commands;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
import models.User;
import models.UserSalt;
import service.UserSaltService;
import service.UserService;
import service.impl.UserSaltServiceImpl;
import service.impl.UserServiceImpl;
import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.RegisterFormValidator;

/**
 * The RegisterCommand class implements the ICommand interface and represents
 * the command to register a new user in the system.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class RegisterCommand implements ICommand {
	private UserService userService;
	private UserSaltService userSaltService;
	private FormValidator formValidator;
	private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

	/**
	 * Constructs a new RegisterCommand object and initializes the UserService,
	 * UserSaltService and FormValidator instances.
	 */
	public RegisterCommand() {
		UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		userService = new UserServiceImpl(userDao);
		UserSaltDao userSaltDao = new UserSaltDaoImpl(ConnectionPoolManager.getInstance().getConnectionPool());
		userSaltService = new UserSaltServiceImpl(userSaltDao);
		this.formValidator = new RegisterFormValidator(InputValidator.getInstance());
	}

	/**
	 * Executes the register command.
	 * 
	 * @param req  the HttpServletRequest object that contains the request the
	 *             client has made of the servlet.
	 * @param resp the HttpServletResponse object that contains the response the
	 *             servlet sends to the client.
	 * 
	 * @return a string with the URL where the user will be redirected.
	 */
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Executing RegisterCommand");

		String targetUrl = "/common_pages/register.jsp";

		String userName = req.getParameter("user_name").trim();
		String userSurname = req.getParameter("user_surname").trim();
		String userEmail = req.getParameter("user_email").trim();
		String userPassword = req.getParameter("user_password").trim();
		String repeatPassword = req.getParameter("password-repeat").trim();

		if (userName.isEmpty() || userSurname.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()
				|| repeatPassword.isEmpty()) {
			System.out.println("empty input in form");
			MessageAttributeUtil.setMessageAttribute(req, "message.fields_empty_registration_fail");
			return targetUrl;
		} else if (formValidator.validate(req) == false) {
			return targetUrl;
		} else if (!userPassword.equals(repeatPassword)) {
			System.out.println("passwords not matching");
			MessageAttributeUtil.setMessageAttribute(req, "message.pass_not_match");
			return targetUrl;
		} else if (userService.findByEmail(userEmail).isPresent()) {
			System.out.println("this email is already registered on the site.");
			MessageAttributeUtil.setMessageAttribute(req, "message.email_already_registered");
			return targetUrl;
		} else {
			User user = new User();
			user.setUserName(userName);
			user.setUserSurname(userSurname);
			user.setUserEmail(userEmail);
			user.setUserType("normal");

			// userPassword Encryption
			MessageDigest md;
			try {
				byte[] salt = userSaltService.generateRandomSaltBytes();
				md = MessageDigest.getInstance("SHA-256");
				md.update(salt);
				byte[] hashedPassword = md.digest(userPassword.getBytes(StandardCharsets.UTF_8));
				String hashedPasswordString = Base64.getEncoder().encodeToString(hashedPassword);
				String saltString = Base64.getEncoder().encodeToString(salt);

				user.setUserPassword(hashedPasswordString);
				userService.create(user);

				UserSalt userSalt = new UserSalt(user.getUserId(), saltString);
				userSaltService.create(userSalt);

				System.out.println(userSalt.getUserSaltId());
				System.out.println(userSalt.getUserId());
			} catch (NoSuchAlgorithmException e) {
				MessageAttributeUtil.setMessageAttribute(req, "message.could_not_do_password_encryption");
			}

			MessageAttributeUtil.setMessageAttribute(req, "message.registration_successful");
			return targetUrl;
		}
	}
}
