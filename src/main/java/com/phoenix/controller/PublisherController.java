package com.phoenix.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.BoardStatistics;
import com.phoenix.data.entity.FileInfo;
import com.phoenix.data.entity.Publisher;
import com.phoenix.service.OperationStatus;
import com.phoenix.service.PublisherService;
import com.phoenix.service.SubscriberService;
import com.sun.mail.iap.Response;

@RestController
@RequestMapping(value = "/publisher")
public class PublisherController {

	@Autowired
	PublisherService publisherService;
	
	@Autowired
	SubscriberService subscriberService;

	@Autowired
	HttpHeaders responseHeader;

	@RequestMapping(value = "/user")
	public ResponseEntity<Publisher> getPublisher(HttpSession session) {
		Publisher publisher = publisherService.getUser((int) session.getAttribute("userId"));
		if (publisher != null)
			return new ResponseEntity<Publisher>(publisher, responseHeader, HttpStatus.OK);

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/board", method = RequestMethod.POST)
	public ResponseEntity<BoardInfo> createBoard(@Valid @RequestBody BoardInfo newBoard, Errors error,
			HttpSession session) throws ValidationException 
	{
		if (!error.hasErrors()) {
			int userId = (int) session.getAttribute("userId");
			if (newBoard.getPublisherId() == userId && publisherService.isValid(newBoard.getCategory())) 
			{
				int boardId = publisherService.saveBoard(newBoard);
				newBoard.setId(boardId);
				return new ResponseEntity<BoardInfo>(newBoard, HttpStatus.CREATED);
			}
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		throw new ValidationException(error);
	}

	@RequestMapping(value = "/board", method = RequestMethod.PUT)
	public ResponseEntity<BoardInfo> updateBoard(@Valid @RequestBody BoardInfo board, Errors error,
			HttpSession session) throws ValidationException 
	{
		if (!error.hasErrors()) {
			int userId = (int) session.getAttribute("userId");
			if (board.getPublisherId() == userId 
					&& publisherService.isValid(board.getCategory())
					&& publisherService.isValidOwnership(userId, board.getId())) 
			{
				publisherService.updateBoard(board);
				return new ResponseEntity<BoardInfo>(board, responseHeader, HttpStatus.CREATED);
			}
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value="/board/{boardId}", method=RequestMethod.DELETE)
	public ResponseEntity<OperationStatus> deleteBoard(@PathVariable int boardId, HttpSession session)
	{
		int userId = (int) session.getAttribute("userId");
		if(publisherService.isValidOwnership(userId, boardId))
		{
			publisherService.deleteBoard(boardId, userId);
			return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL,responseHeader, HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value = "board/{boardId}/post", method = RequestMethod.POST)
	public ResponseEntity<BoardPost> addPost(HttpSession session, @PathVariable int boardId,
			@RequestPart(name="file", required=false) MultipartFile file ,
			@Valid @RequestPart(name="boardPost") BoardPost newPost, Errors error) throws ValidationException, IllegalStateException, IOException 
	{
		if (!error.hasErrors())
		{
			int userId = (int) session.getAttribute("userId");
			if (publisherService.isValidOwnership(userId, boardId) && newPost.getBoardId() == boardId) 
			{
				FileInfo fileInfo = null;
				if(file != null && !file.isEmpty())
				{	
					if( !publisherService.savePermission(userId, file.getSize()))
						throw new ValidationException(new Error("حافظه تخصیص یافته به شما برای آپلود کافی نمی باشد."));
					String path = publisherService.saveFile(file, Integer.toString(boardId), file.getOriginalFilename());
					fileInfo = new FileInfo();
					fileInfo.setFilePath(path);
					fileInfo.setFileType(file.getContentType());
					fileInfo.setFileSize(file.getSize());
				}
				if(fileInfo != null)
				{
					publisherService.saveFileInfo(fileInfo);
					newPost.setFileInfo(fileInfo);
					publisherService.updateStrogeUsage(userId, file.getSize(),Operation.ADD);
				}
				newPost.setCreationDate(new Timestamp(System.currentTimeMillis()));
				long postId = (long)publisherService.savePost(newPost);
				newPost.setId(postId);
				return new ResponseEntity<BoardPost>(newPost, responseHeader, HttpStatus.CREATED);
			}
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value = "board/{boardId}/post/{postId}", method = RequestMethod.DELETE)
	public ResponseEntity<OperationStatus> deletePost(@PathVariable int boardId, @PathVariable long postId, HttpSession session) 
			throws ValidationException, IOException
	{
		int userId = (int) session.getAttribute("userId");
		if(publisherService.isValidOwnership(userId, boardId))
		{
			publisherService.deletePost(postId, boardId, userId);
			return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL,responseHeader,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/board/{boardId}/{start}", method=RequestMethod.GET)
	public ResponseEntity<List<BoardPost>> getMyBoardsPosts(@PathVariable int boardId, 
			@PathVariable int start, HttpSession session)
	{
		int userId = (int) session.getAttribute("userId");
		if (publisherService.isValidOwnership(userId, boardId)){
			List<BoardPost> list = publisherService.getMyBoardPosts(boardId, start);
			return new ResponseEntity<List<BoardPost>>(list,responseHeader,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);	
	}
	
	@RequestMapping(value="/board/{boardId}/statistics", method=RequestMethod.GET)
	public ResponseEntity<BoardStatistics> getBoardStatistics(@PathVariable int boardId, HttpSession session)
	{
		int userId = (int) session.getAttribute("userId");
		if (publisherService.isValidOwnership(userId, boardId))
		{
			return new ResponseEntity<BoardStatistics>(subscriberService.getBoardStatistics(boardId)
					, responseHeader, HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
	}

}
