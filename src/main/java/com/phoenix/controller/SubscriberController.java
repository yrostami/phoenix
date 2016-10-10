package com.phoenix.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;
import com.phoenix.data.entity.SystemInfo;
import com.phoenix.data.service.SubscriberService;

@RestController
@RequestMapping(value="/subscriber")
public class SubscriberController {
	
	@Autowired
	HttpHeaders responseHeader;
	
	@Autowired
	SubscriberService subscriberService;
	
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
	
	@RequestMapping(value="/subscribe", method=RequestMethod.POST)
	public ResponseEntity<SubscribedBoardInfo> subscribe(@Valid @RequestBody SubscribedBoardInfo sbi,
			Errors errors, HttpSession session) throws ValidationException
	{
		if(!errors.hasErrors()){
			int userId = (int)session.getAttribute("userId");
			if(userId == sbi.getSubscriberId() && !subscriberService.isSubscribed(userId , sbi.getBoardId())){
			subscriberService.saveSubscribedBoardInfo(sbi);
			return new ResponseEntity<SubscribedBoardInfo>(sbi, responseHeader, HttpStatus.OK);	
			}
			SubscribedBoardInfo sbiNull = null;	
			return new ResponseEntity<SubscribedBoardInfo>(sbiNull,HttpStatus.NOT_ACCEPTABLE);	
		}
		throw new ValidationException(errors);	
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
	public ResponseEntity<InputStreamResource> getFile(@PathVariable String boardId
			, @PathVariable String fileName, @PathVariable String ext) throws IOException
	{
		try{
		File file = subscriberService.getFile(boardId, fileName+"."+ext);
		if(file != null){
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath()));
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			FileInputStream fis = new FileInputStream(file);
			return new ResponseEntity<InputStreamResource> (new InputStreamResource(fis),header, HttpStatus.OK);
			}
		return new ResponseEntity<> (null, HttpStatus.NOT_FOUND);
		}catch(FileNotFoundException e)
		{
			return new ResponseEntity<> (null, HttpStatus.NOT_FOUND);
		}
	}
	
}
