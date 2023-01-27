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

public class UnblockUserCommand implements ICommand {
	private UserService userService;

	public UnblockUserCommand() {
		UserDao userDao = new UserDaoImpl();
		this.userService = new UserServiceImpl(userDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String redirect = "users.jsp";
		int page = 1;
		int recordsPerPage = 4;

		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}

		System.out.println(redirect);
		System.out.println(req.getRequestURI());

		Optional<String> idOptional = Optional.ofNullable(req.getParameter("userId"));
		if (idOptional.isPresent()) {

			User user = userService.get(Long.parseLong(req.getParameter("userId")));
			user.setIs_bloked(false);
			userService.update(user);

			Optional<Object> userListOptional = Optional
					.ofNullable(userService.getAll((page - 1) * recordsPerPage, recordsPerPage));

			int noOfRecords = userService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			req.getSession().setAttribute("userList", userListOptional.get());
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("user_unblocked"));
			req.getSession().setAttribute("noOfPagesAllUsers", noOfPages);
			req.getSession().setAttribute("currentPageAllUsers", page);
			return redirect;
		} else {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("user_not_unblocked"));
			return redirect;
		}
	}
}
