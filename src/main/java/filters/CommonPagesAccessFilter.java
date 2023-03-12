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

@WebFilter(urlPatterns = { "/common_pages/login.jsp", "/common_pages/register.jsp" })
public class CommonPagesAccessFilter implements Filter {

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
		}chain.doFilter(request, response);
	}
}
