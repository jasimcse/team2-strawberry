package controller.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityController implements Filter {
	
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String uri = ((HttpServletRequest)request).getRequestURI();

		// get user controller from the session
		CurrentEmployee currEmployee = (CurrentEmployee)((HttpServletRequest)request).getSession().
				getAttribute("currentEmployee");
		
		AllPages allPages = (AllPages)((HttpServletRequest)request).getSession().getServletContext().
				getAttribute("allPages");

		// if the user is not logged in, redirect to login page, also check if a
		// static resource is requested (the uri will contain
		// javax.faces.resource)
		if ((currEmployee == null ||
			(allPages.isLoggedUsersOnly(uri) && !currEmployee.isLoggedIn()) ||
			(currEmployee.isLoggedIn() && !allPages.getReadRight(uri, currEmployee.getPosition()))) &&
		   !uri.endsWith("ZapazvaneNa4as.jsf") &&
		   !uri.contains("javax.faces.resource")) {
			
			((HttpServletResponse)response).sendRedirect("/clients/ZapazvaneNa4as.jsf");
		} else {
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
