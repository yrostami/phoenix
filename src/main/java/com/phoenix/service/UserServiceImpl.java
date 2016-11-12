package com.phoenix.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.controller.AuthenticationResponse;
import com.phoenix.data.entity.UserInfo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public int saveUserInfo(UserInfo userInfo) {
		Session session = sessionFactory.getCurrentSession();
		int id = (int)session.save(userInfo);
		return id;
	}

	@Transactional
	@Override
	public AuthenticationResponse getAuthenticationResponse(int userId) {
		AuthenticationResponse user = null;
		UserInfo userInfo = getUserInfo(userId);
		if(userInfo != null)
		{
			user = new AuthenticationResponse(userInfo.getEmail()
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
	
	@Override
	public boolean isValidEmail(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
            return true;
        else
            return false;
    }

	@Transactional
	@Override
	public boolean emailDuplication(String email) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM UserInfo AS U WHERE U.email = :xemail");
		query.setParameter("xemail", email);
		List<UserInfo> list = query.getResultList();
		if(list.size() > 0)
			return true;
		return false;
	}
	
	

}
