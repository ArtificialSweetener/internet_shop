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

import dao.LocalizationDao;
import dao.impl.LocalizationDaoImpl;
import service.LocalizationService;
import service.impl.LocalizationServiceImpl;
import util.GlobalStringsProvider;

@WebFilter(urlPatterns = { "/*" })
public class LocalizationMapFilter implements Filter {
	private LocalizationService localizationService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		Filter.super.init(filterConfig);
		LocalizationDao localizationDao = new LocalizationDaoImpl();
		this.localizationService = new LocalizationServiceImpl(localizationDao);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("LocalizationFilter working");
		HttpServletRequest req = (HttpServletRequest) request;
		Optional<Object> lang = Optional.ofNullable(req.getSession().getAttribute("language"));
		System.out.println(lang);
		String language;

		if (lang.isEmpty() || (!lang.get().toString().equals("en") || !lang.get().toString().equals("ua")) ) {
			if (GlobalStringsProvider.getInstance().getLocalizationMap() == null) {
				GlobalStringsProvider.getInstance().setLanguage("en");
				GlobalStringsProvider.getInstance().setStringsMapByLanguage(localizationService);
				request.setAttribute("language", "en");
				chain.doFilter(request, response);
			} else {
				chain.doFilter(request, response);
			}
		} else {
			if (GlobalStringsProvider.getInstance().getLocalizationMap() == null) {
				language = (String) lang.get().toString();
				GlobalStringsProvider.getInstance().setLanguage(language);
				GlobalStringsProvider.getInstance().setStringsMapByLanguage(localizationService);
				request.setAttribute("language", language);
				chain.doFilter(request, response);
			} else {
				language = (String) lang.get();
				if (GlobalStringsProvider.getInstance().getLanguage().equals(language)) {
					chain.doFilter(request, response);
				} else {
					GlobalStringsProvider.getInstance().setLanguage(language);
					GlobalStringsProvider.getInstance().setStringsMapByLanguage(localizationService);
					request.setAttribute("language", language);
					chain.doFilter(request, response);
				}
			}
		}
	}
}
