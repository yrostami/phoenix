package com.phoenix.service;

import com.phoenix.controller.AuthenticationResponse;
import com.phoenix.data.entity.UserInfo;

public interface UserService {
	
	public AuthenticationResponse getAuthenticationResponse(int userId);
	public UserInfo getUserInfo(int userId);
	public boolean isValidEmail(String email);
	public boolean emailDuplication(String email);
	public int saveUserInfo(UserInfo userInfo);
	public UserInfo updateDisplayName(int id, String password, String displayName);
	public int updatePassword(int id, String currentPassword, String newPassword);
	public void deleteAccount(int userId, String role);
	public boolean isValidUser(int userId, String password);
}
