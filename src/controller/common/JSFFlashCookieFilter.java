package controller.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * слага пътя на всички бисквитки равен на "/"
 * по този начин Flash Scope-а е достъпен и в друго поддърво на JSF web приложението
 * 
 * ако не се използва този филтър Flash Scope-а е достъпен само от страници намиращи се в същото поддърво като страницата която е записала там
 *
 */
public class JSFFlashCookieFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse myResponse = new JSFFlashCookiePathFixer((HttpServletResponse)response, "/");
		chain.doFilter(request, myResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}
