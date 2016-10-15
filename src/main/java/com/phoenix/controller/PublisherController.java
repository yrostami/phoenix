package com.phoenix.controller;

import java.io.IOException;
import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.FileInfo;
import com.phoenix.data.entity.Publisher;
import com.phoenix.data.service.OperationStatus;
import com.phoenix.data.service.PublisherService;

@RestController
@RequestMapping(value = "/publisher")
public class PublisherController {

	@Autowired
	PublisherService publisherService;

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
	public ResponseEntity<BoardInfo> createboard(@Valid @RequestBody BoardInfo newBoard, Errors error,
			HttpSession session) throws ValidationException 
	{
		if (!error.hasErrors()) {
			int userId = (int) session.getAttribute("userId");
			if (newBoard.getPublisherId() == userId && publisherService.isValid(newBoard.getCategory())) {
				publisherService.saveBoard(newBoard);
				return new ResponseEntity<BoardInfo>(newBoard, HttpStatus.CREATED);
			}
		}
		throw new ValidationException(error);
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
					publisherService.increaseStrogeUsage(userId, file.getSize());
				}
				newPost.setCreationDate(new Timestamp(System.currentTimeMillis()));
				publisherService.savePost(newPost);
				return new ResponseEntity<BoardPost>(newPost, HttpStatus.CREATED);
			}
		}
		throw new ValidationException(error);
	}
	
	@RequestMapping(value = "board/{boardId}/{postId}", method = RequestMethod.DELETE)
	public ResponseEntity<OperationStatus> deletePost(@PathVariable int boardId, @PathVariable long postId, HttpSession session) 
			throws ValidationException
	{
		int userId = (int) session.getAttribute("userId");
		if(publisherService.isValidOwnership(userId, boardId))
		{
			boolean result = publisherService.deletePost(postId, boardId);
			if(result)
				return new ResponseEntity<OperationStatus>(OperationStatus.SUCCESSFUL,responseHeader,HttpStatus.OK);
			return new ResponseEntity<OperationStatus>(OperationStatus.FAIL,responseHeader,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<OperationStatus>(OperationStatus.PERMISSIONFAIL,responseHeader,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@RequestMapping(value="/board/{boardId}/{start}", method=RequestMethod.GET)
	public ResponseEntity<List<BoardPost>> getMyBoardsPosts(@PathVariable int boardId, 
			@PathVariable int start, HttpSession session)
	{
		int userId = (int) session.getAttribute("userId");
		if (publisherService.isValidOwnership(userId, boardId)){
			List<BoardPost> list = publisherService.getMyBoardsPosts(boardId, start);
			return new ResponseEntity<List<BoardPost>>(list,responseHeader,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);	
	}

}
