package com.phoenix.controller;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(annotations=Controller.class)
public class PhoenixControllerAdvice  {
	
	@Autowired
	HttpHeaders responseHeader;
	
	private static final Logger logger=LoggerFactory.getLogger(PhoenixControllerAdvice.class);

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Error> validationExceptionHandler(ValidationException e)
	{
		return new ResponseEntity<Error>(e.getValidationError(),
				responseHeader,
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Error> genericExceptionHandler(Exception exception, 
			HttpServletRequest request,HttpSession session)
	{
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
					sessionLog += "\n\t\t" + attributeName+" : "
							+ session.getAttribute(attributeName);	
				}
			}
		}
		
		//بدست آوردن کوکی های ارسال شده همراه با درخواست
		String cookieLog = "no cookie!";
		if(request.getCookies() != null)
		{
			Cookie[] cookies=request.getCookies();
			cookieLog ="\n";
			for(int i=0;i<cookies.length;i++)
				cookieLog += "\t\t" + cookies[i].getName() + " : " + cookies[i].getValue() + "\n";
			}
		// بدست آوردن پارامتر های ارسال شده همراه درخواست
		String parameters = "no parameter.";
		Enumeration<String> parameterNames =request.getParameterNames();
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
				"\n\tparameters: "+parameters+
				"\n\tcookie(s): "+ cookieLog +"\n";
		
		// گزارش کردن خطا و اطلاعات نشست و درخواستی که موجب خطا شده است
		logger.error(requestLog + sessionLog,exception);
		return new ResponseEntity<Error> (new Error(" خطا: سرور دچار خطای داخلی شده است."),
				responseHeader,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
