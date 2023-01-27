package commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorPageCommand implements ICommand{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		String redirect = "index.jsp";
		return redirect;
	}

}
