package filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;

/**
 * Filters the request to set the character encoding of the request to UTF-8.
 * This class implements the {@link javax.servlet.Filter} interface and is
 * annotated with the {@link javax.servlet.annotation.WebFilter} annotation to
 * specify the URL pattern for the filter.
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
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
//@WebFilter(urlPatterns = { "/*" })
public class EncodingFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(EncodingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("EncodingFilter starting");
		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
		logger.info("EncodingFilter ending");
	}
}
