package com.phoenix.authentication;

import java.io.IOException;
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

public class RememberMeFilter implements Filter {

	SessionFactory sessionFactory;

	public RememberMeFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession httpSession = httpRequest.getSession();
		if (httpSession.getAttribute("Authenticated") == null) {

			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				int i = 0;
				for (; i < cookies.length; i++)
					if (cookies[i].getName().endsWith("_RMT"))
						break;
				if (i < cookies.length) {
					HttpServletResponse httpResponse = (HttpServletResponse) response;
					if (!(rememberByCookie(cookies[i], httpResponse, httpSession)))

						httpSession.setAttribute("Authenticated", false);

				} else
					httpSession.setAttribute("Authenticated", false);

			} else
				httpSession.setAttribute("Authenticated", false);
		}
		chain.doFilter(request, response);
	}

	private boolean rememberByCookie(Cookie cookie, HttpServletResponse response,
			HttpSession httpSession) {
		boolean result = false;
		Session session = null;
		Transaction tx = null;
		 try {
		session = this.sessionFactory.openSession();
		tx = session.beginTransaction();
		Query query = session
				.createQuery("FROM RememberInfo AS R WHERE" + " R.cookieName = :xcookieName AND R.token = :xtoken");
		query.setParameter("xcookieName", cookie.getName());
		query.setParameter("xtoken", cookie.getValue());
		List list = query.getResultList();
		
		// اگر جستجو دارای نتیجه باشد پس کوکی معتبر است باید اطلاعات مربوط به کاربر در شی نشست قرار داده شود.
		if (list.size() > 0) {
			RememberInfo remember = (RememberInfo) list.get(0);
			//قرار دادن لیست شناسه برد های دنبال شده کاربر در شی نشست
			Query query2 = session
					.createQuery("SELECT SB.boardId FROM SubscribedBoardInfo AS SB " + "WHERE SB.subscriberId = :xid");
			query2.setParameter("xid", remember.getUserId());
			List<Integer> list2 = query2.getResultList();
			httpSession.setAttribute("subscribedList", list2);

			// قرار دادن لیست بردهای کاربرمنتشر کننده در شی نشست
			if (remember.getUserRole().equals("Publisher")) {
				Query query3 = session.createQuery("SELECT B.id FROM BoardInfo AS B " + "WHERE B.publisherId = :xid");
				query3.setParameter("xid", remember.getUserId());
				List<Integer> list3 = query3.getResultList();
				httpSession.setAttribute("boardsList", list3);
			}

			// قرار دادن اطلاعات کاربر در شی نشست
			httpSession.setAttribute("Authenticated", true);
			httpSession.setAttribute("userId", remember.getUserId());
			httpSession.setAttribute("role", remember.getUserRole());

			cookie.setMaxAge(30 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);

			result = true;

		}
		tx.commit();
		 } catch (Exception ex) {
		 }
		session.close();
		return result;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(filterConfig.getServletContext());
		this.sessionFactory = ctx.getBean(SessionFactory.class);
	}

}
