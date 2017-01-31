package com.phoenix.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.MaxStorageUpdate;
import com.phoenix.data.entity.PublishReqForAdmin;
import com.phoenix.data.entity.SystemInfo;
import com.phoenix.data.entity.UserInfo;
import com.phoenix.data.entity.UserInfoForAdmin;
import com.phoenix.service.AdminService;
import com.phoenix.service.OperationStatus;
import com.phoenix.service.PublisherService;
import com.phoenix.service.SubscriberService;
import com.phoenix.service.UserService;

@RestController
@RequestMapping(value="/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	SubscriberService subscriberService;
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpHeaders responseHeader;
	
	@RequestMapping(value="/systeminfo", method=RequestMethod.GET)
	public ResponseEntity<SystemInfo> getSystemInfo()
	{
		SystemInfo systemInfo = subscriberService.getSystemInfo();
		if(systemInfo != null)
			return new ResponseEntity<SystemInfo>(systemInfo,HttpStatus.OK);
		
		return new ResponseEntity<SystemInfo>(systemInfo,HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/publishrequest/{firstResult}", method=RequestMethod.GET)
	public ResponseEntity<List<PublishReqForAdmin>> getRequests(@PathVariable int firstResult)
	{
		return new ResponseEntity<List<PublishReqForAdmin>> 
		(adminService.getRequests(firstResult, 25), responseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/publishrequest/escape/{reqId}", method=RequestMethod.GET)
	public ResponseEntity<OperationStatus> escapeRequest(@PathVariable int reqId)
	{
		adminService.escapePublishRequest(reqId);
		return new ResponseEntity(OperationStatus.SUCCESSFUL, responseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/publishrequest/accept/{reqId}", method=RequestMethod.GET)
	public ResponseEntity<OperationStatus> acceptRequest(@PathVariable int reqId)
	{
		adminService.acceptPublishRequest(reqId);
		return new ResponseEntity(OperationStatus.SUCCESSFUL, responseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/switchtosubscriber/{userId}", method=RequestMethod.GET)
	public ResponseEntity<OperationStatus> switchRoleToSubscriber(@PathVariable int userId)
	{
		adminService.switchRoleToSubscriber(userId);
		return new ResponseEntity(OperationStatus.SUCCESSFUL, responseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteuser/{userId}", method=RequestMethod.GET)
	public ResponseEntity<OperationStatus> deleteUser(@PathVariable int userId)
	{
		UserInfo user = userService.getUserInfo(userId);
		if(user != null){
			adminService.switchRoleToSubscriber(userId);
			return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL, responseHeader, HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/updatesetmaxstorage", method=RequestMethod.POST)
	public ResponseEntity<OperationStatus> setMaxStorage(@Valid @RequestBody MaxStorageUpdate maxStorage
			,Errors error, HttpSession session) throws ValidationException
	{
		if(!error.hasErrors())
		{
		int userId = (int) session.getAttribute("userId");
		if(userService.isValidUser(userId, maxStorage.getPassword()))
		{
			adminService.setMaxStorage(maxStorage.getMaxStorage());
			return new ResponseEntity(OperationStatus.SUCCESSFUL, responseHeader, HttpStatus.OK);
		}
		return new ResponseEntity<OperationStatus>(OperationStatus.FAIL,responseHeader,
				HttpStatus.NOT_ACCEPTABLE);
		
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value="/updatepassword", method=RequestMethod.POST)
	public ResponseEntity<OperationStatus> updatePassword(@Valid @RequestBody PasswordUpdate passUpdate,
			Errors error, HttpSession session) throws ValidationException
	{
		if(!error.hasErrors())
		{
			int userId = (int) session.getAttribute("userId");
			int rows = userService.updatePassword(userId, passUpdate.getCurrentPassword(), passUpdate.getNewPassword());
			if(rows>0)
			return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL,responseHeader,
					HttpStatus.OK);
			return new ResponseEntity<OperationStatus>(OperationStatus.FAIL,responseHeader,
					HttpStatus.NOT_ACCEPTABLE);
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value="/searchuser/{role}/{searchType}", method=RequestMethod.POST)
	public ResponseEntity<List<UserInfoForAdmin>> searchUsers(@RequestBody String searchWord
			, @PathVariable String role, @PathVariable String searchType)
	{
		if(searchWord.isEmpty())
			return new ResponseEntity<> (null, HttpStatus.NOT_ACCEPTABLE);
		
		if((role.equals("subscriber") || role.equals("publisher") || role.equals("allusers"))
				&& (searchType.equals("name") || searchType.equals("email")))
		{
			return new ResponseEntity<List<UserInfoForAdmin>> (adminService.searchUser(role,searchType,searchWord)
					, responseHeader, HttpStatus.OK);
		}
		return new ResponseEntity<> (null, HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/category", method=RequestMethod.POST)
	public ResponseEntity<BoardCategory> createNewCategory(@Valid @RequestBody BoardCategory category, Errors errors)
			throws ValidationException
	{
		if(!errors.hasErrors())
		{
			int id = adminService.saveNewCategory(category);
			category.setId(id);
			return new ResponseEntity<BoardCategory>(category,responseHeader,HttpStatus.CREATED);
			
		}
		throw new ValidationException(errors);
	}
	
	@RequestMapping(value="/allcategories")
	public ResponseEntity<List<BoardCategory>> getAllCategories()
	{
		List<BoardCategory> list = subscriberService.getAllBoardCategories();
		return new ResponseEntity<List<BoardCategory>> (list,responseHeader,HttpStatus.OK);
	}
	
	@RequestMapping(value="/category/{categoryId}", method=RequestMethod.DELETE)
	public ResponseEntity<OperationStatus> deleteBoardCategory(@PathVariable int categoryId)
	{
		if(adminService.getBoardCountInCategory(categoryId) == 0)
		{
			adminService.deleteCategory(categoryId);
			return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL
					,responseHeader,HttpStatus.OK);
		}
		return new ResponseEntity<OperationStatus>(OperationStatus.FAIL
				,responseHeader,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/category/{categoryId}/updatename", method=RequestMethod.PUT)
	public ResponseEntity<OperationStatus> updateBoardCategoryname(@RequestBody String newName
			,@PathVariable int categoryId)
	{
		
		if(!newName.isEmpty() && newName.length()<=100)
		{
			adminService.updateCategoryName(categoryId, newName);
			return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL
					,responseHeader,HttpStatus.OK); 
		}
		return new ResponseEntity<OperationStatus>(OperationStatus.FAIL
				,responseHeader,HttpStatus.NOT_ACCEPTABLE);
	} 
}
