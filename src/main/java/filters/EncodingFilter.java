package filters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = { "/*" })
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
