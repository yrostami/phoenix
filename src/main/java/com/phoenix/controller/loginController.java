package com.phoenix.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class loginController {

	@RequestMapping(value="/webLogin")
	public String webLogin(HttpSession session)
	{
		if((boolean) session.getAttribute("Authenticated") == true)
		{
			String role = (String)session.getAttribute("role");
			if(role.equals("Subscriber")) return "subscriber";
			else if(role.equals("Publisher")) return "publisher";
			else if(role.equals("Admin")) return "admin";
		}
		session.setAttribute("login", false);
		return "redirect:/";
	}  
	
	@RequestMapping(value="/appLogin")
	@ResponseBody
	public ResponseEntity<String> appLogin(HttpSession  session)
	{
		if((boolean) session.getAttribute("Authenticated") == true)
			return new ResponseEntity<String>("",HttpStatus.OK);
		return new ResponseEntity<String>("",HttpStatus.NOT_ACCEPTABLE);
	}
}
