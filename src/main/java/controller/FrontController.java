package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commands.command_factory.CommandFactory;
import commands.icommand.ICommand;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@MultipartConfig(maxFileSize = 16177215)
public class FrontController extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(FrontController.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("Received GET request");
		String forward = handleRequest(req, resp);
		System.out.println(forward);
		logger.debug("Forwarding to page: " + forward);
		logger.debug("Returning view: " + forward);
		resp.sendRedirect(req.getContextPath() + forward);
		// req.getRequestDispatcher(forward).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("Received POST request");
		String redirect = handleRequest(req, resp);
		logger.debug("Redirecting to page: " + redirect);
		System.out.println(redirect);
		resp.sendRedirect(req.getContextPath() + "/" + redirect);
	}

	private String handleRequest(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Invoking private handleRequest method of the FrontController class");
		ICommand command = CommandFactory.getCommand(req);
		logger.debug("Got command: " + command.toString());
		return command.execute(req, resp);
	}
}
