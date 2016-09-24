package com.phoenix.data.service;

import java.util.List;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;

public interface SubscriberService {
	
	public Subscriber getUser(int userId);
	public List<Board> getAllBoards();
	public List<BoardCategory> getAllBoardCategories(); 
	public List<Board> getBoardsFromCategory(int categoryId);
	public void saveSubscribedBoardInfo(SubscribedBoardInfo sbi);

}
