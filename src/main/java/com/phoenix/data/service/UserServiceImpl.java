package com.phoenix.data.service;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.data.entity.AuthenticationResponse;
import com.phoenix.data.entity.UserInfo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public AuthenticationResponse getAuthenticationResponse(int userId) {
		AuthenticationResponse user = null;
		UserInfo userInfo = getUserInfo(userId);
		if(userInfo != null)
		{
			user = new AuthenticationResponse(userInfo.getUsername()
					, userInfo.getDisplayName(), userInfo.getRole());
		}
		return user;
	}

	@Transactional
	@Override
	public UserInfo getUserInfo(int userId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM UserInfo AS U WHERE U.id = :xid");
		query.setParameter("xid", userId);
		List<UserInfo> list = query.getResultList();
		if(list.size() > 0)
			return (UserInfo) list.get(0);
		return null;
	}

}
