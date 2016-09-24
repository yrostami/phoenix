package com.phoenix.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;
import com.phoenix.data.service.SubscriberService;

@RestController
@RequestMapping(value="/subscriber")
public class SubscriberController {
	
	@Autowired
	HttpHeaders responseHeader;
	
	@Autowired
	SubscriberService subscriberService;
	
	@RequestMapping(value="/user")
	public ResponseEntity<Subscriber> getPublisher(HttpSession session)
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
	
	@RequestMapping(value="/subscribe", method=RequestMethod.POST)
	public ResponseEntity<SubscribedBoardInfo> subscribe(@Valid @RequestBody SubscribedBoardInfo sbi,
			Errors errors, HttpSession session) throws ValidationException
	{
		if(!errors.hasErrors()){
			int userId = (int)session.getAttribute("userId");
			List<Integer> subscribedList = (List<Integer>) session.getAttribute("subscribedList");
			if(userId == sbi.getSubscriberId() && !subscribedList.contains(sbi.getBoardId())){
			subscriberService.saveSubscribedBoardInfo(sbi);
			return new ResponseEntity<SubscribedBoardInfo>(sbi, responseHeader, HttpStatus.OK);	
			}
			SubscribedBoardInfo sbiNull = null;	
			return new ResponseEntity<SubscribedBoardInfo>(sbiNull,HttpStatus.NOT_ACCEPTABLE);	
		}
		throw new ValidationException(errors);
		
	}
	
}
