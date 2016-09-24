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

import com.phoenix.data.entity.Board;
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
			@Valid @RequestBody BoardPost newPost, Errors error) {
		if (!error.hasErrors()) {
			List<Integer> boardsList = (List<Integer>) session.getAttribute("boardsList");
			if (boardsList.contains(boardId) && newPost.getBoardId()==boardId) 
			{
				publisherService.savePost(newPost);
				return new ResponseEntity<BoardPost>(newPost, HttpStatus.CREATED);
			}
		}
		BoardPost nullPost = null;
		return new ResponseEntity<BoardPost>(nullPost, HttpStatus.BAD_REQUEST);
	}

}
