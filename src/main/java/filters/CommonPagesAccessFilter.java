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
 * The {@code CommonPagesAccessFilter} class is a servlet filter that checks if
 * a user accessing common pages like the login and registration pages is logged
 * in or not. If the user is logged in, the filter will redirect the user to
 * their respective dashboard based on their role (normal or admin). If the user
 * is not logged in, the filter will allow the request to proceed to the next
 * filter in the chain. This class implements the {@link javax.servlet.Filter}
 * interface and is annotated with the
 * {@link javax.servlet.annotation.WebFilter} annotation to specify the URL
 * pattern for the filter.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
@WebFilter(urlPatterns = { "/common_pages/login.jsp", "/common_pages/register.jsp" })
public class CommonPagesAccessFilter implements Filter {

	/**
	 * Filters the request to check if a user accessing common pages is logged in or
	 * not. If the user is logged in, the filter will redirect the user to their
	 * respective dashboard based on their role (normal or admin). If the user is
	 * not logged in, the filter will allow the request to proceed to the next
	 * filter in the chain.
	 * 
	 * @param request  the {@link javax.servlet.ServletRequest} object that contains
	 *                 the client's request.
	 * @param response the {@link javax.servlet.ServletResponse} object that
	 *                 contains the filter's response.
	 * @param chain    the {@link javax.servlet.FilterChain} object that allows the
	 *                 request to proceed to the next filter in the chain.
	 * @throws IOException      if an I/O error occurs during the processing of the
	 *                          request.
	 * @throws ServletException if the request could not be processed.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Common Access filter works");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		String contextPath = req.getContextPath();

		System.out.println("ContextPath is: " + contextPath);
		System.out.println("Session is:  " + session);

		if (session != null) {
			System.out.println("Session is not null");
			if (session.getAttribute("role") != null) {
				MessageAttributeUtil.setMessageAttribute(req, "message.access_denied_logout_first");
				if (session.getAttribute("role").equals("normal")) {
					System.out.println("Attribute is normal");
					res.sendRedirect(contextPath + "/normal/normal.jsp");
					return;
				} else if (session.getAttribute("role").equals("admin")) {
					System.out.println("Attribute is admin");
					res.sendRedirect(contextPath + "/admin/admin.jsp");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}
}
