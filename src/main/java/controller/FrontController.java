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

/**
 * The FrontController class acts as the central controller for handling HTTP
 * requests. It extends the HttpServlet class and provides implementation for
 * both the doGet() and doPost() methods. It also implements the MultipartConfig
 * annotation for handling file uploads.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
@MultipartConfig(maxFileSize = 16177215)
public class FrontController extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(FrontController.class);
	private static final long serialVersionUID = 1L;

	/**
	 * This method overrides the doGet() method of the HttpServlet class. It handles
	 * GET requests and calls the handleRequest() method to delegate to the
	 * appropriate command for processing.
	 * 
	 * @param req  The HttpServletRequest object containing the request information
	 * @param resp The HttpServletResponse object for sending the response
	 * 
	 * @throws ServletException If there is an error in the servlet processing
	 * @throws IOException      If there is an error in input/output handling
	 */
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

	/**
	 * This method overrides the doPost() method of the HttpServlet class. It
	 * handles POST requests and calls the handleRequest() method to delegate to the
	 * appropriate command for processing.
	 * 
	 * @param req  The HttpServletRequest object containing the request information
	 * @param resp The HttpServletResponse object for sending the response
	 * 
	 * @throws ServletException If there is an error in the servlet processing
	 * @throws IOException      If there is an error in input/output handling
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("Received POST request");
		String redirect = handleRequest(req, resp);
		logger.debug("Redirecting to page: " + redirect);
		System.out.println(redirect);
		resp.sendRedirect(req.getContextPath() + "/" + redirect);
	}

	/**
	 * Private method for handling the request by retrieving the appropriate command
	 * object from the CommandFactory and executing it.
	 * 
	 * @param req  the HTTP request object
	 * @param resp the HTTP response object
	 * @return a String representing the view to be displayed
	 */
	private String handleRequest(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("Invoking private handleRequest method of the FrontController class");
		ICommand command = CommandFactory.getCommand(req);
		logger.debug("Got command: " + command.toString());
		return command.execute(req, resp);
	}
}
