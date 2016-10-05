package com.phoenix.data.service;

import java.io.File;
import java.util.List;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;
import com.phoenix.data.entity.SystemInfo;

public interface SubscriberService {
	
	public Subscriber getUser(int userId);
	public List<Board> getAllBoards();
	public List<BoardCategory> getAllBoardCategories(); 
	public List<Board> getBoardsFromCategory(int categoryId);
	public void saveSubscribedBoardInfo(SubscribedBoardInfo sbi);
	public SystemInfo getSystemInfo();
	public boolean isSubscribed(int userId, int boardId);
	public File getFile(String parentDir, String fileName);
}
