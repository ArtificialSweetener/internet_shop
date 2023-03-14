package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.MessageAttributeUtil;

/**
 * The AdminAccessFilter class is a servlet filter that checks if the user
 * accessing the protected admin area is an admin. If the user is not an admin,
 * the filter will invalidate the session and redirect the user to the login
 * page with an access denied message. This class implements the Filter
 * interface and is annotated with the WebFilter annotation to specify the URL
 * pattern for the filter.
 *
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
@WebFilter(urlPatterns = { "/admin/*" })
public class AdminAccessFilter implements Filter {

	/**
	 * Checks if the user accessing the admin area is an admin, if not invalidates
	 * the session and redirects the user to the login page with an access denied
	 * message.
	 *
	 * @param request  the {@link javax.servlet.ServletRequest} object that contains
	 *                 the client's request.
	 * @param response the {@link javax.servlet.ServletResponse} object that
	 *                 contains the filter's response.
	 * @param chain    the {@link javax.servlet.FilterChain} object that allows the
	 *                 request to proceed to the next filter in the chain.
	 *
	 * @throws IOException      if an I/O error occurs during the processing of the
	 *                          request.
	 * @throws ServletException if the request could not be processed.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Admin Access filter works");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String contextPath = req.getContextPath();

		System.out.println("ContextPath is: " + contextPath);
		System.out.println("Session is:  " + session);

		if (session != null) {
			if (session.getAttribute("role") != null) {
				System.out.println("user Role is:  " + session.getAttribute("role"));
				if (!session.getAttribute("role").equals("admin")) {
					session.invalidate();
					session = req.getSession();
					System.out.println("Session invalidated");
					MessageAttributeUtil.setMessageAttribute(req, "message.access_denied_not_an_admin");
					res.sendRedirect(contextPath + "/common_pages/login.jsp");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}
}
