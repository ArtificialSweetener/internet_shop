package commands;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import service.UserService;
import service.impl.UserServiceImpl;
import util.GlobalStringsProvider;

public class BlockUserCommand implements ICommand {
	private UserService userService;

	public BlockUserCommand() {
		UserDao userDao = new UserDaoImpl();
		this.userService = new UserServiceImpl(userDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		// long id = Long.parseLong(req.getParameter("userId"));
		String redirect = "users.jsp";
		int page = 1;
		int recordsPerPage = 4;

		if (req.getParameter("page") != null) {
			page = Integer.parseInt(req.getParameter("page"));
		}
		Optional<String> idOptional = Optional.ofNullable(req.getParameter("userId"));

		if (idOptional.isPresent()) {
			
			userService.delete(Long.parseLong(idOptional.get()));
			Optional<Object> userListOptional = Optional
					.ofNullable(userService.getAll((page - 1) * recordsPerPage, recordsPerPage));
			
			int noOfRecords = userService.getNoOfRecords();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			req.getSession().setAttribute("userList", userListOptional.get());
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("user_blocked"));
			req.getSession().setAttribute("noOfPagesAllUsers", noOfPages);
			req.getSession().setAttribute("currentPageAllUsers", page);
			return redirect;
		} else {
			req.getSession().setAttribute("message",
					GlobalStringsProvider.getInstance().getLocalizationMap().get("user_not_blocked"));
			return redirect;
		}
	}
}
