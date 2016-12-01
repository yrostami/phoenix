package com.phoenix.authentication;

import java.io.IOException;

import javax.persistence.Query;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Logout implements Filter {

	SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(Logout.class);

	@Override
	public void destroy() {
		this.sessionFactory.close();
		this.sessionFactory = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException 
	{
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpRes = (HttpServletResponse) res;
		HttpSession httpSession = httpReq.getSession();
		
		Session session = null;
		Transaction tx = null;
		try{
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		if ((boolean) httpSession.getAttribute("Authenticated")) 
		{
			int userId = (int) httpSession.getAttribute("userId");
			Cookie[] cookies = httpReq.getCookies();
			for (int i = cookies.length - 1; i >= 0; i--) {
				if (cookies[i].getName().endsWith("_RMT")) {
					cookies[i].setMaxAge(0);
					cookies[i].setPath(httpReq.getContextPath());
					httpRes.addCookie(cookies[i]);
					
					Query query = session.createQuery("DELETE FROM RememberInfo AS R "
							+ "WHERE R.userId = :xuserId AND R.cookieName = :xcookieName");
					query.setParameter("xuserId", userId);
					query.setParameter("xcookieName", cookies[i].getName());
					query.executeUpdate();
				}
			}
			
			httpSession.removeAttribute("Authenticated");
			httpSession.removeAttribute("userId");
			httpSession.removeAttribute("role");
		}
		httpRes.setStatus(HttpServletResponse.SC_OK);
		httpSession.invalidate();
		
		tx.commit();
		}catch(Exception ex)
		{
			ex.printStackTrace();
			tx.rollback();
			logger.error("LogoutFilter",ex);
			httpRes.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		session.close();
		return;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(filterConfig.getServletContext());
		this.sessionFactory = ctx.getBean(SessionFactory.class);
	}
}
