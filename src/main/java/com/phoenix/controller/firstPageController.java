package com.phoenix.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class firstPageController {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getFirstPage(HttpSession session)
	{
		if((boolean)session.getAttribute("Authenticated") == true)
			return "redirect:/login/page";
		return "loginForm";
	}
}
