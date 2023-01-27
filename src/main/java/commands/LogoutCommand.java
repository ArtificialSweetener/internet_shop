package commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand implements ICommand {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().invalidate();
		String forward = "login.jsp";
		return forward;
	}

}
