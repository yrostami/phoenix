package com.phoenix.controller;

import java.io.File;
import java.security.SecureRandom;
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

import com.phoenix.authentication.MD5Hash;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.Publisher;
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

		return new ResponseEntity<Publisher>(publisher, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/board", method = RequestMethod.POST)
	public ResponseEntity<BoardInfo> createboard(@Valid @RequestBody BoardInfo newBoard, Errors error,
			HttpSession session) {
		System.out.print(!error.hasErrors()+"\n");
		if (!error.hasErrors()) {
			int userId = (int) session.getAttribute("userId");
			if (newBoard.getPublisherId() == userId && publisherService.isValid(newBoard.getCategory())) {
				publisherService.saveBoard(newBoard);
				List<Integer> boardsList = (List<Integer>) session.getAttribute("boardsList");
				boardsList.add(newBoard.getId());
				session.setAttribute("boardsList", boardsList);
				return new ResponseEntity<BoardInfo>(newBoard, HttpStatus.CREATED);
			}
		}
		BoardInfo nullBoard = null;
		return new ResponseEntity<BoardInfo>(nullBoard, HttpStatus.BAD_REQUEST);
	}


	@RequestMapping(value = "/{boardId}/post", method = RequestMethod.POST)
	public ResponseEntity<BoardPost> addPost(HttpSession session, @PathVariable int boardId,
			@RequestPart("file") MultipartFile file ,
			@Valid @RequestPart("boardPost") BoardPost newPost, Errors error) throws ValidationException 
	{
		if (!error.hasErrors())
		{
			@SuppressWarnings("unchecked")
			List<Integer> boardsList = (List<Integer>) session.getAttribute("boardsList");
			if (boardsList.contains(boardId) && newPost.getBoardId() == boardId) 
			{
				boolean fileSaved = true;
				if( !file.isEmpty())
				{	
					MD5Hash hash= new MD5Hash();
					SecureRandom srand = new SecureRandom();
					String fileName = hash.getHashFrom(Integer.toString(srand.nextInt()) 
							+ file.getOriginalFilename());
					String path = File.separator + Integer.toString(boardId) + File.separator + fileName;
					fileSaved = publisherService.saveFile(file, path);
					newPost.setFilePath(path);
					newPost.setFileType(file.getContentType());
				}
				if(fileSaved)
				{
					publisherService.savePost(newPost);
					return new ResponseEntity<BoardPost>(newPost, HttpStatus.CREATED);
				}
			}
		}
		System.out.println(newPost);
		throw new ValidationException(error);
//		BoardPost nullPost = null;
//		return new ResponseEntity<BoardPost> (nullPost, HttpStatus.BAD_REQUEST);
	}

}
