package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import commands.CommandFactory;
import commands.ICommand;

@MultipartConfig(maxFileSize = 16177215)
@WebServlet("/FrontController")
public class FrontController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String forward = handleRequest(req, resp);
		req.getRequestDispatcher(forward).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String redirect = handleRequest(req, resp);
		resp.sendRedirect(redirect);
		System.out.println("post request");
	}

	private String handleRequest(HttpServletRequest req, HttpServletResponse resp) {
		ICommand icommand = CommandFactory.getCommand(req);
		return icommand.execute(req, resp);
	}
}
