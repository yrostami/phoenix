package com.phoenix.service;

import java.io.IOException;
import java.util.List;

import org.springframework.expression.Operation;
import org.springframework.web.multipart.MultipartFile;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.FileInfo;
import com.phoenix.data.entity.Publisher;
import com.phoenix.data.entity.SystemInfo;
import com.phoenix.data.entity.UserInfo;

public interface PublisherService {

	public Publisher getUser(int userId);
	public int saveBoard(BoardInfo newBoard);
	public void updateBoard(BoardInfo board);
	public void deleteBoard(int boardId, int userId);
	public long savePost(BoardPost newPost);
	public List<UserInfo> getBoardSubscribers(int boardId);
	public boolean isValid(BoardCategory category);
	public String saveFile(MultipartFile file, String parentDir, String fileName)throws IllegalStateException, IOException;
	public boolean savePermission(int userId, long fileSize);
	public void saveFileInfo(FileInfo fileInfo);
	public boolean isValidOwnership(int userId, int boardId);
	public void updateStrogeUsage(int userId, long increaseValue, Operation op);
	public List<BoardPost> getMyBoardPosts(int boardId, int startResult);
	public void deletePost(long postId, int boardId, int userId)throws IOException;
	public void deleteFile(String parentDir, String fileName) throws IOException;
}
