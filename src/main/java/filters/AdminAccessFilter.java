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

@WebFilter(urlPatterns = { "/admin/*" })
public class AdminAccessFilter implements Filter {

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

		if (session == null || session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) {
			session.invalidate();
			session = req.getSession();
			System.out.println("Session invalidated");
			MessageAttributeUtil.setMessageAttribute(req, "message.access_denied_not_an_admin");
			res.sendRedirect(contextPath + "/common_pages/login.jsp");
			return;
		}
		chain.doFilter(request, response);
	}
}