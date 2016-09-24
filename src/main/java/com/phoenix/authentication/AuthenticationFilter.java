package com.phoenix.authentication;

import java.io.IOException;
import java.security.SecureRandom;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.phoenix.data.entity.RememberInfo;
import com.phoenix.data.entity.UserInfo;

public class AuthenticationFilter implements Filter {

	SessionFactory sessionFactory;

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
		
		if ((boolean) httpSession.getAttribute("Authenticated") == false 
				&& httpRequest.getParameter("password") != null 
				&& httpRequest.getParameter("username") != null)
		{
			String username = httpRequest.getParameter("username");
			String password = httpRequest.getParameter("password");
			boolean rememberMe = false;

			if (httpRequest.getParameter("rememberMe") != null)
				rememberMe = httpRequest.getParameter("rememberMe").equals("on");
			
			// اتصال به پایگاه داده برای بررسی نام کاربری و رمز عبور
			
			Transaction tx = null;
			Session session = null;
			try {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
				Query query1 = session.createQuery("FROM UserInfo AS User WHERE"
						+ " User.username = :xusername AND User.password = :xpassword");
				query1.setParameter("xusername", username);
				query1.setParameter("xpassword", password); 
				List list1 = query1.getResultList();
				if (list1.size() > 0) {
					//اگر جستجو دارای نتیجه باشد پس نام کاربری و رمزعبور معتبر است پس اطلاعات کاربر را در شی نشست قرار می دهیم
					
					UserInfo user = (UserInfo) list1.get(0);
					httpSession.setAttribute("Authenticated", true);
					httpSession.setAttribute("userId", user.getId());
					httpSession.setAttribute("role", user.getRole());
					
					if( !(user.getRole().equals("Admin"))){
						// قرار دادن لیست شناسه بردهای دنبال شده در شی نشست
						Query query2 = session.createQuery("SELECT SB.boardId FROM SubscribedBoardInfo AS SB "
								+ "WHERE SB.subscriberId = :xid");
						query2.setParameter("xid", user.getId());
						List<Integer> list2 = query2.getResultList();
						httpSession.setAttribute("subscribedList",list2);
						
						// قرار دادن لیست بردهای کاربرمنتشر کننده در شی نشست
						if(user.getRole().equals("Publisher")){
							Query query3 = session.createQuery("SELECT B.id FROM BoardInfo AS B "
									+ "WHERE B.publisherId = :xid");
							query3.setParameter("xid", user.getId());
							List<Integer> list3= query3.getResultList();
							httpSession.setAttribute("boardsList", list3);
						}
						
						// انجام عمل "مرا به یاد داشته باش" برای کاربری که این گزینه را انتخاب کرده باشد
						if (rememberMe) {
							HttpServletResponse httpResponse = (HttpServletResponse) response;
							RememberInfo rememberInfo = Remember(httpRequest, user);
							session.save(rememberInfo);
							Cookie rememberCookie = new Cookie(rememberInfo.getCookieName(), rememberInfo.getToken());
							rememberCookie.setMaxAge(30 * 24 * 60 * 60);
							rememberCookie.setPath("/");
							httpResponse.addCookie(rememberCookie);
						}
					}
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
			}
			session.close();
		} 

		chain.doFilter(request, response);
	}

	private RememberInfo Remember(HttpServletRequest request, UserInfo user) {
		String rememberData = Integer.toString(user.getId());
		if (request.getHeader("User-Agent") != null)
			rememberData += request.getHeader("User-Agent");
		SecureRandom random = new SecureRandom();
		String cookieName = new String(random.nextInt() + "_RMT");
		MD5Hash hash = new MD5Hash();
		String rememberToken = hash.getHashFrom(rememberData);
		RememberInfo rememberInfo = new RememberInfo();
		rememberInfo.setUserId(user.getId());
		rememberInfo.setUserRole(user.getRole());
		rememberInfo.setCookieName(cookieName);
		rememberInfo.setToken(rememberToken);			
		return rememberInfo;
	}

}
