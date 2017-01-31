package com.phoenix.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phoenix.data.entity.UserInfo;
import com.phoenix.service.OperationStatus;
import com.phoenix.service.UserService;

@Controller

public class RegistrationController {

	@Autowired
	HttpHeaders responseHeader;
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String getPage() {
		return "registration";
	}
	
	@RequestMapping(value = "/registration/emailValidation", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<RegistrationResponse> isValidUsername(@RequestBody String email) {
		
		if(!userService.emailDuplication(email))
			return new ResponseEntity<RegistrationResponse>(new RegistrationResponse(true, "این ایمیل  قابل استفاده  است."),
					responseHeader, HttpStatus.OK);
		return new ResponseEntity<RegistrationResponse>(new RegistrationResponse(false, "این ایمیل قبلا استفاده شده است."),
				responseHeader, HttpStatus.OK);
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<RegistrationResponse> doRegistration(@RequestBody UserInfo userInfo, Errors error)
			throws ValidationException {
		if (!error.hasErrors()) {
			if(userService.emailDuplication(userInfo.getEmail()))
				throw new ValidationException(new Error("ایمیل  قبلا استفاده شده  است."));
			
			userInfo.setActive(true);
			userInfo.setRole("Subscriber");
			userService.saveUserInfo(userInfo);
			return new ResponseEntity<RegistrationResponse>(RegistrationResponse.getSuccess(), responseHeader,
					HttpStatus.OK);
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value="/deleteaccount", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<OperationStatus> deleteAccount(HttpSession session, @RequestBody String password)
	{
		if(password != null){
			if(password.length() >= 8 && password.length() <= 50){
				int userId = (int) session.getAttribute("userId");
				if(!userService.isValidUser(userId, password))
					return new ResponseEntity<OperationStatus>(OperationStatus.PERMISSIONFAIL,
							responseHeader, HttpStatus.NOT_ACCEPTABLE);
				
				String role = (String) session.getAttribute("role");
				userService.deleteAccount(userId, role);
				session.removeAttribute("Authenticated");
				session.removeAttribute("role");
				session.removeAttribute("userId");
				session.invalidate();
				
				return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL,
						responseHeader,HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
	}
}
