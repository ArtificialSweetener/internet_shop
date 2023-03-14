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
 * The NormalUserAccessFilter class is a filter that checks if the user
 * accessing a specific URL has a "normal" role. If the user does not have the
 * "normal" role, the filter invalidates the session and redirects the user to
 * the login page with an error message.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
@WebFilter(urlPatterns = { "/normal/*" })
public class NormalUserAccessFilter implements Filter {
	/**
	 * The doFilter method checks if the user accessing a specific URL has a
	 * "normal" role. If the user does not have the "normal" role, the filter
	 * invalidates the session and redirects the user to the login page with an
	 * error message.
	 * 
	 * @param request  the ServletRequest object containing the client's request
	 * @param response the ServletResponse object containing the filter's response
	 * @param chain    the FilterChain object containing the filter chain
	 * 
	 * @throws IOException      if an input or output error occurs while the filter
	 *                          is processing the request or response
	 * @throws ServletException if the request or response could not be processed by
	 *                          the filter
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
		System.out.println("user Role is:  " + session.getAttribute("role"));

		if (session != null) {
			if (session.getAttribute("role") != null) {
				if (!session.getAttribute("role").equals("normal")) {
					session.invalidate();
					session = req.getSession();
					System.out.println("Session invalidated");
					MessageAttributeUtil.setMessageAttribute(req,
							"message.access_denied_not_logged_in_as_a_regular_user");
					res.sendRedirect(contextPath + "/common_pages/login.jsp");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}
}
