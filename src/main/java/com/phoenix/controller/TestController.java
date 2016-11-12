package com.phoenix.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.data.entity.UserInfo;
import com.phoenix.service.SubscriberService;



@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	HttpHeaders responseHeader;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	SubscriberService subscriberService;
	
	@Transactional(readOnly=false)
	@RequestMapping(value="/adduser/{role}", method=RequestMethod.POST)
	public ResponseEntity<String> addsubscriber(@PathVariable String role,@Valid @RequestBody UserInfo user, Errors errors) 
			throws ValidationException
	{
		if(!errors.hasErrors()){
			Session session = sessionFactory.getCurrentSession();
			user.setRole(role);
			session.save(user);
			return new ResponseEntity<String> ("انجام شد", responseHeader, HttpStatus.CREATED);
		}
		
		throw new ValidationException(errors);
	}
	
	
	@RequestMapping(value="/testfilter", method=RequestMethod.GET)
	public String testFilter(HttpSession session)
	{
		return "authenticated : " +session.getAttribute("Authenticated").toString();
	}
	
	
}
