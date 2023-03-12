package filters;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebFilter(urlPatterns = { "/*" })
public class LocalizationFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(LocalizationFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("LocalizationFilter starting");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		Optional<Object> langOpt = Optional.ofNullable(req.getSession().getAttribute("language"));
		if (langOpt.isPresent()) {		
			String language = (langOpt.get()).toString();
			System.out.println(language);
			if (!language.equals("")) {
				req.getSession().setAttribute("language", language);
			} else {
				req.getSession().setAttribute("language", "en");
			}
		} else {
			req.getSession().setAttribute("language", "en");
		}
		chain.doFilter(req, res);
		logger.info("LocalizationFilter ending");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
