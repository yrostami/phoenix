
package com.phoenix.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String path = httpRequest.getServletPath();

		// جلوگیری از دسترسی به مسیر سرولت کنترل کننده استثناء به صورت مستقیم از
		// سوی کاربر
		if (path.startsWith("/ErrorHandler")) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.sendRedirect("/404Error");
			return;
		}

		// عدم کنترل درخواست برای صفحه ورود و منابع ایستا
		if (path.equals("/") || path.equals("") || path.startsWith("/registration") || path.startsWith("/resources/")
				|| path.startsWith("/test") || path.startsWith("/login"))
			chain.doFilter(request, response);

		// کنترل دسترسی با استفاده از اطلاعات ذخیره شده در نشست
		else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			HttpSession session = httpRequest.getSession();
			if (session.getAttribute("Authenticated") == null)
				session.setAttribute("Authenticated", false);

			if ((boolean) session.getAttribute("Authenticated") == true && session.getAttribute("role") != null) {

				String role = (String) session.getAttribute("role");
				if ((role.equals("Subscriber") && (path.startsWith("/subscriber/") || path.startsWith("/deleteaccount")))
						|| (role.equals("Publisher") && (path.startsWith("/subscriber/")
								|| path.startsWith("/publisher/") || path.startsWith("/deleteaccount")))
						|| (role.equals("Admin") && path.startsWith("/admin/")))
					chain.doFilter(request, response);
				else {
					httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}

			} else {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
