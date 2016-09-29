package com.phoenix.data.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.Publisher;
import com.phoenix.data.entity.UserInfo;

public interface PublisherService {

	public Publisher getUser(int userId);
	public void saveBoard(BoardInfo newBoard);
	public void savePost(BoardPost newPost);
	public List<UserInfo> getChannelSubscribers();
	public boolean isValid(BoardCategory category);
	public boolean saveFile(MultipartFile file, String path);
}
