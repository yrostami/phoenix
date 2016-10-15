package com.phoenix.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phoenix.data.entity.AuthenticationResponse;
import com.phoenix.data.service.UserService;

@Controller
@RequestMapping("/login")
public class loginController {
	
	@Autowired
	HttpHeaders responseHeader;
	
	@Autowired
	private UserService userService;
	

	@RequestMapping("/page")
	public String getPage(HttpSession session)
	{
		if((boolean) session.getAttribute("Authenticated") == true)
		{
			String role = (String)session.getAttribute("role");
			if(role.equals("Subscriber")) return "subscriber";
			else if(role.equals("Publisher")) return "publisher";
			else if(role.equals("Admin")) return "admin";
			return "redirect:/";
		}
		session.setAttribute("login", false);
		return "redirect:/";
	}
	
	@RequestMapping("/webLogin")
	public ResponseEntity<String> webLogin(HttpSession session)
	{
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/plain; charset=utf-8");
		if((boolean) session.getAttribute("Authenticated") == true)
		{
			return new ResponseEntity<String>("http://localhost:8080/phoenix/login/page", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Authentication Fail.", HttpStatus.UNAUTHORIZED);
	}
	
	@RequestMapping("/appLogin")
	@ResponseBody
	public ResponseEntity<AuthenticationResponse> appLogin(HttpSession  session)
	{
		AuthenticationResponse user = null;
		if((boolean) session.getAttribute("Authenticated") == true)
		{
			int userId = (int) session.getAttribute("userId");
			user = userService.getAuthenticationResponse(userId);
		}
		if(user != null)
			return new ResponseEntity<AuthenticationResponse>(user, responseHeader, HttpStatus.OK);

		return new ResponseEntity<AuthenticationResponse>(user,HttpStatus.NOT_ACCEPTABLE);
	}
}