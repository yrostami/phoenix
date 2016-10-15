package com.phoenix.data.service;

import com.phoenix.data.entity.AuthenticationResponse;
import com.phoenix.data.entity.UserInfo;

public interface UserService {
	
	public AuthenticationResponse getAuthenticationResponse(int userId);
	public UserInfo getUserInfo(int userId);

}
