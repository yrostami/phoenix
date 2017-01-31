package com.phoenix.service;

import java.util.List;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.PublishReqForAdmin;
import com.phoenix.data.entity.UserInfoForAdmin;

public interface AdminService {

	public List<PublishReqForAdmin> getRequests(int firstResult, int maxResult);
	public void escapePublishRequest(int reqId);
	public void acceptPublishRequest(int reqId);
	public void switchRoleToSubscriber(int userId);
	public void setMaxStorage(long maxStorage);
	public List<UserInfoForAdmin> searchUser(String role, String searchType, String searchWord);
	public int saveNewCategory(BoardCategory category);
	public int deleteCategory(int categoryId);
	public void updateCategoryName(int categoryId, String categoryNewName);
	public long getBoardCountInCategory(int categoryId);
}
