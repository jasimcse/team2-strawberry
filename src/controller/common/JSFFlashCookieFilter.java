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
 * ����� ���� �� ������ ��������� ����� �� "/"
 * �� ���� ����� Flash Scope-� � �������� � � ����� �������� �� JSF web ������������
 * 
 * ��� �� �� �������� ���� ������ Flash Scope-� � �������� ���� �� �������� �������� �� � ������ �������� ���� ���������� ����� � �������� ���
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
