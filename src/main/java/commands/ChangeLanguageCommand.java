package commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.LocalizationDao;
import dao.impl.LocalizationDaoImpl;
import service.LocalizationService;
import service.impl.LocalizationServiceImpl;
import util.GlobalStringsProvider;

public class ChangeLanguageCommand implements ICommand {
	private LocalizationService localizationService;

	public ChangeLanguageCommand() {
		LocalizationDao localizationDao = new LocalizationDaoImpl();
		localizationService = new LocalizationServiceImpl(localizationDao);
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("ChangeLanguageCommand executed");
		String changeToLanguage = req.getParameter("language");
		//String forward = req.getParameter("pageName");
		 String redirect = req.getParameter("pageName");
		if (changeToLanguage.equals("en")) {
			req.getSession().setAttribute("language", "en");
		} else if (changeToLanguage.equals("ua")) {
			req.getSession().setAttribute("language", "ua");
		}
		if (GlobalStringsProvider.getInstance().getLanguage().equals(changeToLanguage.toUpperCase())) {
			//return forward;
			return redirect;
		} else {
			GlobalStringsProvider.getInstance().setLanguage(changeToLanguage);
			GlobalStringsProvider.getInstance().setStringsMapByLanguage(localizationService);
			//return forward;
			return redirect;
		}
	}
}
