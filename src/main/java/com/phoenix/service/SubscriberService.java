package com.phoenix.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.BoardStatistics;
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
	public BoardStatistics getBoardStatistics(int boardId);
	public List<BoardPost> getPosts(int userId, int maxResult);
	public List<BoardPost> getBoardPosts(int boardId, int maxResult);
	public List<BoardPost> getPostsBefore(int userId, Timestamp timestamp, int maxResult);
	public List<BoardPost> getBoardPostsBefore(int boardId, Timestamp timestampp, int maxResult);
	public boolean isValidBoard(int BoardId);
	public int deleteSubscribedBoardInfo(int userId, int boardId);
	public List<BoardPost> getPostsAfter(int userId, Timestamp timestamp);
	public long getPostsCountAfter(int userId, Timestamp timestamp);
}
