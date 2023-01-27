package commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import service.UserService;
import service.impl.UserServiceImpl;
import util.GlobalStringsProvider;

public class GetUsersListCommand implements ICommand {
	private UserService userService;


	public GetUsersListCommand() {
		UserDao userDao = new UserDaoImpl();
		this.userService = new UserServiceImpl(userDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String forward = "users.jsp";
		// List<User> userList = userService.getAll();
		int page = 1;
		int recordsPerPage = 4;
		System.out.println("Page attribute is " + req.getParameter("page"));
		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
			System.out.println("Changed page attribute to " + page);
		}

		Optional<Object> userListOptional = Optional
				.ofNullable(userService.getAll((page - 1) * recordsPerPage, recordsPerPage));

		int noOfRecords = userService.getNoOfRecords();
		int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

		if (userListOptional.isPresent()) {
			req.getSession().setAttribute("userList", userListOptional.get());
			req.getSession().setAttribute("noOfPagesAllUsers", noOfPages);
			req.getSession().setAttribute("currentPageAllUsers", page);
			return forward;
		} else {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("user_list_empty"));
			return forward;
		}
	}
}
