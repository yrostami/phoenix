package com.phoenix.authentication;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ExceptionHandler
 */
//@WebServlet(name = "PhoenixExceptionHandler", urlPatterns = { "/ErrorHandler" })
public class PhoenixExceptionHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger mylogger = LoggerFactory.getLogger(PhoenixExceptionHandler.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhoenixExceptionHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//بدست آوردن خطا از درخواست
		Throwable ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		HttpSession session = request.getSession();
		
		// بدست آوردن اطلاعات مربوط به نشست
		String sessionLog = "session: Does not exist! \n";
		if(session.isNew() == false)
		{
			sessionLog = "session: no attribut!.\n";
			Enumeration<String> sessionAttributes=session.getAttributeNames();
			if(sessionAttributes.hasMoreElements())
			{
				sessionLog= "Session:\n\tAttribute(s):";
				String attributeName = " ";
				while(sessionAttributes.hasMoreElements())
				{
					attributeName = sessionAttributes.nextElement();
					sessionLog += "\n\t\t" + attributeName +" : "
							+ session.getAttribute(attributeName);	
				}
			}
		}
		//بدست آوردن کوکی های ارسال شده همراه با درخواست
		String cookieLog = "no cookie!";
		if(request.getCookies()!= null)
		{
			Cookie[] cookies=request.getCookies();
			cookieLog ="\n";
			for(int i=0;i<cookies.length;i++)
				cookieLog += "\t\t" + cookies[i].getName() + " : " + cookies[i].getValue() + "\n";
		}
		// بدست آوردن پارامتر های ارسال شده همراه درخواست
		String parameters = "no parameter.";
		Enumeration<String> parameterNames = request.getParameterNames();
		if(parameterNames.hasMoreElements())
		{
			parameters = "";
			String temp=null;
			while(parameterNames.hasMoreElements())
			{
				temp=parameterNames.nextElement();
				parameters +="\n\t\t" + temp + " : " + request.getParameter(temp);
			}
		}
		//فرمت کردن اطلاعات در خواست
		String requestLog="\nrequest:"+
				"\n\tparameters: "+ parameters+
				"\n\tcookie(s): "+ cookieLog +"\n";
		
		// گزارش کردن خطا و اطلاعات نشست و درخواستی که موجب خطا شده است
		mylogger.error(requestLog + sessionLog,ex);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().println("خطای داخلی سرور");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
