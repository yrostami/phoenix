package com.phoenix.data.service;

import java.io.IOException;
import java.util.List;

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
	public void saveBoard(BoardInfo newBoard);
	public void savePost(BoardPost newPost);
	public List<UserInfo> getChannelSubscribers();
	public boolean isValid(BoardCategory category);
	public String saveFile(MultipartFile file, String parentDir, String fileName)throws IllegalStateException, IOException;
	public void saveFileInfo(FileInfo fileInfo);
	public boolean isValidOwnership(int userId, int boardId);
	public void increaseStrogeUsage(int userId, long increaseValue);
}
