package filters;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The LocalizationFilter class is a servlet filter that intercepts all incoming
 * requests
 * 
 * and sets the session attribute "language" to the language specified in the
 * request, if any.
 * 
 * If the request does not contain a language parameter, the session attribute
 * is set to "en".
 * 
 * This filter is designed to be used in a multilingual web application.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

//@WebFilter(urlPatterns = { "/*" })
public class LocalizationFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(LocalizationFilter.class);

	/**
	 * The doFilter method intercepts incoming requests and sets the session
	 * attribute "language" to the language specified in the request, if any. If the
	 * request does not contain a language parameter, the session attribute is set
	 * to "en". The filter then passes the request and response objects to the next
	 * filter or servlet in the chain.
	 *
	 * @param request  the ServletRequest object representing the client's request
	 * @param response the ServletResponse object representing the server's response
	 * @param chain    the FilterChain object used to pass the request and response
	 *                 to the next filter or servlet in the chain
	 * @throws IOException      if an I/O error occurs during the processing of the
	 *                          request
	 * @throws ServletException if a servlet-specific error occurs during the
	 *                          processing of the request
	 */
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
