package com.phoenix.authentication;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.data.entity.RememberInfo;
import com.phoenix.data.entity.UserInfo;

public class AuthenticationFilter implements Filter {

	SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(filterConfig.getServletContext());
		this.sessionFactory = ctx.getBean(SessionFactory.class);
	}

	@Override
	public void destroy() {
		this.sessionFactory.close();
		this.sessionFactory = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession httpSession = httpRequest.getSession();
		AuthenticationInfo authInfo = null;
		String path = httpRequest.getServletPath();
		
		if ((boolean) httpSession.getAttribute("Authenticated") == false
				&& (path.startsWith("/login/webLogin")
						|| path.startsWith("/login/appLogin")))
		{
		ObjectMapper mapper = new ObjectMapper();
		try{
		authInfo = mapper.readValue(httpRequest.getInputStream(), AuthenticationInfo.class);
		}catch (JsonMappingException e) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}catch (JsonParseException e) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (authInfo != null)
		{	
			// اتصال به پایگاه داده برای بررسی نام کاربری و رمز عبور	
			Transaction tx = null;
			Session session = null;
			try {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
				Query query1 = session.createQuery("FROM UserInfo AS User WHERE"
						+ " User.email = :xemail AND User.password = :xpassword");
				query1.setParameter("xemail", authInfo.getEmail());
				query1.setParameter("xpassword", authInfo.getPassword()); 
				@SuppressWarnings("rawtypes")
				List list1 =query1.getResultList();
				if (list1.size() > 0) {
					//اگر جستجو دارای نتیجه باشد پس نام کاربری و رمزعبور معتبر است پس اطلاعات کاربر را در شی نشست قرار می دهیم	
					UserInfo user = (UserInfo) list1.get(0);
					httpSession.setAttribute("Authenticated", true);
					httpSession.setAttribute("userId", user.getId());
					httpSession.setAttribute("role", user.getRole());
					
						// انجام عمل "مرا به یاد داشته باش" برای کاربری که این گزینه را انتخاب کرده باشد
						if (authInfo.getRememberMe() && !user.getRole().equals("Admin")) {
							HttpServletResponse httpResponse = (HttpServletResponse) response;
							RememberInfo rememberInfo = Remember(httpRequest, user);
							session.save(rememberInfo);
							Cookie rememberCookie = new Cookie(rememberInfo.getCookieName(), rememberInfo.getToken());
							rememberCookie.setMaxAge(30 * 24 * 60 * 60);
							rememberCookie.setPath("/");
							httpResponse.addCookie(rememberCookie);
						}
					
				}
				tx.commit();
			} catch (Exception ex) {
				ex.printStackTrace();
				tx.rollback();
				logger.error("AuthenticationFilter\n",ex);
			}
			session.close();
		} 
		}

		chain.doFilter(request, response);
	}

	private RememberInfo Remember(HttpServletRequest request, UserInfo user) {
		String rememberData = Integer.toString(user.getId());
		if (request.getHeader("User-Agent") != null)
			rememberData += request.getHeader("User-Agent");
		SecureRandom random = new SecureRandom();
		String cookieName = new String(random.nextInt() + "_RMT");
		String rememberToken = MD5Hash.getHashFrom(rememberData);
		RememberInfo rememberInfo = new RememberInfo();
		rememberInfo.setUserId(user.getId());
		rememberInfo.setUserRole(user.getRole());
		rememberInfo.setAgent(request.getHeader("User-Agent"));
		rememberInfo.setLastLogin(new Timestamp(System.currentTimeMillis()));
		rememberInfo.setCookieName(cookieName);
		rememberInfo.setToken(rememberToken);			
		return rememberInfo;
	}

	
	
}
