package com.phoenix.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.BoardStatistics;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;
import com.phoenix.data.entity.SystemInfo;
import com.phoenix.data.entity.UserInfo;
import com.phoenix.service.OperationStatus;
import com.phoenix.service.SubscriberService;
import com.phoenix.service.UserService;

@RestController
@RequestMapping(value="/subscriber")
public class SubscriberController {
	
	@Autowired
	HttpHeaders responseHeader;
	
	@Autowired
	SubscriberService subscriberService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/user")
	public ResponseEntity<Subscriber> getSubscriber(HttpSession session)
	{
		Subscriber subscriber = subscriberService
				.getUser((int)session.getAttribute("userId"));
		if(subscriber != null)
			return new ResponseEntity<Subscriber>(subscriber,responseHeader,HttpStatus.OK);
		
		return new ResponseEntity<Subscriber> (subscriber, HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/allboards")
	public ResponseEntity<List<Board>> getAllBoards()
	{
		List<Board> list = subscriberService.getAllBoards();
		return new ResponseEntity<List<Board>> (list,responseHeader,HttpStatus.OK);
	}
	
	@RequestMapping(value="/allcategories")
	public ResponseEntity<List<BoardCategory>> getAllCategories()
	{
		List<BoardCategory> list = subscriberService.getAllBoardCategories();
		return new ResponseEntity<List<BoardCategory>> (list,responseHeader,HttpStatus.OK);
	}
	
	@RequestMapping(value="/board/{boardId}/subscribe", method=RequestMethod.GET)
	public ResponseEntity<SubscribedBoardInfo> subscribe(@PathVariable int boardId,HttpSession session) throws ValidationException
	{
		if(subscriberService.isValidBoard(boardId)){
			int userId = (int)session.getAttribute("userId");
			if(!subscriberService.isSubscribed(userId ,boardId)){
				SubscribedBoardInfo sbi = new SubscribedBoardInfo();
				sbi.setBoardId(boardId);
				sbi.setSubscriberId(userId);
			subscriberService.saveSubscribedBoardInfo(sbi);
			return new ResponseEntity<SubscribedBoardInfo>(sbi, responseHeader, HttpStatus.OK);	
			}
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/systeminfo", method=RequestMethod.GET)
	public ResponseEntity<SystemInfo> getSystemInfo()
	{
		SystemInfo systemInfo = subscriberService.getSystemInfo();
		if(systemInfo != null)
			return new ResponseEntity<SystemInfo>(systemInfo,HttpStatus.OK);
		
		return new ResponseEntity<SystemInfo>(systemInfo,HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/getfile/{boardId}/{fileName}.{ext}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getFile(@PathVariable int boardId
			, @PathVariable String fileName, @PathVariable String ext) throws IOException
	{
		try{
		File file = subscriberService.getFile(Integer.toString(boardId), fileName+"."+ext);
		if(file != null){
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath()));
			String fileType = Files.probeContentType(file.toPath());
			System.out.println(fileType);
			System.out.println(fileType);
			System.out.println(fileType);
			header.add("Content-Type", fileType);
			FileInputStream fis = new FileInputStream(file);
			return new ResponseEntity<InputStreamResource> (new InputStreamResource(fis),
					header, HttpStatus.OK);
			}
		return new ResponseEntity<> (null, HttpStatus.NOT_FOUND);
		}catch(FileNotFoundException e)
		{
			return new ResponseEntity<> (null, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/posts", method=RequestMethod.GET)
	public ResponseEntity<List<BoardPost>> getPosts(HttpSession session)
	{
		int userId = (int) session.getAttribute("userId");
		return new ResponseEntity<List<BoardPost>>(subscriberService.getPosts(userId, 12),
				responseHeader,HttpStatus.OK);
	}
	
	@RequestMapping(value="/board/{boardId}/posts", method=RequestMethod.GET)
	public ResponseEntity<List<BoardPost>> getBoardPosts(@PathVariable int boardId)
	{
		return new ResponseEntity<List<BoardPost>>(subscriberService.getBoardPosts(boardId,6),
				responseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/posts/before/{time}", method=RequestMethod.GET)
	public ResponseEntity<List<BoardPost>> getPostsBefore(HttpSession session,
			@PathVariable long time)
	{
		Timestamp timestamp = new Timestamp(time);
		if(timestamp.after(new Timestamp(2016-1900,1,1,0,0,0,0)) 
				&& timestamp.before(new Timestamp(System.currentTimeMillis())))
		{
		int userId = (int) session.getAttribute("userId");
		return new ResponseEntity<List<BoardPost>>(
				subscriberService.getPostsBefore(userId, timestamp, 10),
				responseHeader,
				HttpStatus.OK);
		}
		return new ResponseEntity<List<BoardPost>>(null, responseHeader, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/board/{boardId}/posts/before/{time}", method=RequestMethod.GET)
	public ResponseEntity<List<BoardPost>> getBoardPostsBefore(@PathVariable int boardId,
			@PathVariable long time)
	{
		Timestamp timestamp = new Timestamp(time);
		if(timestamp.after(new Timestamp(2016-1900,1,1,0,0,0,0)) 
				&& timestamp.before(new Timestamp(System.currentTimeMillis())))
		return new ResponseEntity<List<BoardPost>>(
				subscriberService.getBoardPostsBefore(boardId, timestamp,6),
				responseHeader,
				HttpStatus.OK);
		
		return new ResponseEntity<List<BoardPost>>(null, responseHeader, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/board/{boardId}/statistics", method=RequestMethod.GET)
	public ResponseEntity<BoardStatistics> getBoardStatistics(@PathVariable int boardId, HttpSession session)
	{
		if(subscriberService.isValidBoard(boardId))
			return new ResponseEntity<BoardStatistics>(subscriberService.getBoardStatistics(boardId)
					, responseHeader, HttpStatus.OK);
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/board/{boardId}/unsubscribe", method=RequestMethod.GET)
	public ResponseEntity<OperationStatus> unsubscribe(HttpSession session, @PathVariable int boardId)
	{
		int userId = (int) session.getAttribute("userId");
		if(subscriberService.deleteSubscribedBoardInfo(userId, boardId) > 0)
			return new ResponseEntity<OperationStatus> (OperationStatus.SUCCESSFUL,
					responseHeader, HttpStatus.OK);
		return new ResponseEntity<OperationStatus> (OperationStatus.FAIL,
				responseHeader, HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/user/updatename", method=RequestMethod.POST)
	public ResponseEntity<UserInfo> updateDisplayName(@Valid @RequestBody DisplayNameUpdate nameUpdate,
			Errors error, HttpSession session) throws ValidationException
	{
		if(!error.hasErrors())
		{
			int userId = (int) session.getAttribute("userId");
			UserInfo userInfo = userService.updateDisplayName(userId,
					nameUpdate.getPassword(), nameUpdate.getDisplayName());
			return new ResponseEntity<UserInfo> (userInfo,responseHeader,HttpStatus.OK);
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value="/user/updatepassword", method=RequestMethod.POST)
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
}
