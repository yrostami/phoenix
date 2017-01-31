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
	
	@Autowired
	PublisherService publisherService;
	
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

	@Transactional
	@Override
	public UserInfo updateDisplayName(int id, String password, String displayName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("UPDATE UserInfo AS U SET U.displayName = :xdisplayName"
				+ " WHERE U.id = :xid AND U.password = :xpassword");
		query.setParameter("xdisplayName", displayName);
		query.setParameter("xid", id);
		query.setParameter("xpassword", password);
		query.executeUpdate();
		
		Query userInfoQuery = session.createQuery("FROM UserInfo AS U WHERE U.id = :xid");
		userInfoQuery.setParameter("xid", id);
		List<UserInfo> list = userInfoQuery.getResultList();
		UserInfo userInfo = null;
		if(list.size()>0)
			userInfo = list.get(0);
		return userInfo;
	}

	@Transactional
	@Override
	public int updatePassword(int id, String currentPassword, String newPassword) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("UPDATE UserInfo AS U SET U.password = :xnewPassword"
				+ " WHERE U.id = :xid AND U.password = :xpassword");
		query.setParameter("xnewPassword", newPassword);
		query.setParameter("xid", id);
		query.setParameter("xpassword", currentPassword);
		
		return query.executeUpdate();
	}
	
	@Transactional
	@Override
	public void deleteAccount(int userId, String role) {
		Session session = sessionFactory.getCurrentSession();
		if(role.equals("Subscriber"))
		{
			//حذف بردهای دنبال شده توسط کاربر
			Query subscribedBoardsDelete = session.createQuery("DELETE FROM SubscribedBoardInfo AS SBI"
					+ " WHERE SBI.subscriberId = :xuserId");
			subscribedBoardsDelete.setParameter("xuserId", userId);
			subscribedBoardsDelete.executeUpdate();
			
			//حذف اطلاعات کاربر
			Query subscriberDelete = session.createQuery("DELETE FROM UserInfo AS U"
					+ " WHERE U.id = :xuserId");
			subscriberDelete.setParameter("xuserId", userId);
			subscriberDelete.executeUpdate();
		}
		else if(role.equals("Publisher"))
		{
			//حذف بردهای دنبال شده توسط کاربر
			Query publisheSubscribedBoardsDelete = session.createQuery("DELETE FROM SubscribedBoardInfo AS SBI"
					+ " WHERE SBI.subscriberId = :xuserId");
			publisheSubscribedBoardsDelete.setParameter("xuserId", userId);
			publisheSubscribedBoardsDelete.executeUpdate();
			
			//حذف بردهای ایجاد شده توسط کاربر
			Query publisherBoards = session.createQuery("SELECT B.id FROM BoardInfo AS B"
					+ " WHERE B.publisherId = :xuserId");
			publisherBoards.setParameter("xuserId", userId);
			List<Integer> boardsList = publisherBoards.getResultList();
			
			if(boardsList.size() > 0)
			{
				for(int i = boardsList.size() - 1 ; i >= 0 ; i--)
					publisherService.deleteBoard(boardsList.get(i), userId);
			}
			
			//حذف اطلاعات کاربر
			Query publisherDelete = session.createQuery("DELETE FROM UserInfo AS U"
					+ " WHERE U.id = :xuserId");
			publisherDelete.setParameter("xuserId", userId);
			publisherDelete.executeUpdate();
		}
		
		Query notificationDelete = session.createQuery("DELETE FROM PostNotification AS PN"
				+ " WHERE PN.userId = :xuserId");
		notificationDelete.setParameter("xuserId", userId);
		notificationDelete.executeUpdate();
		
		//حذف اطلاعات مربوط به  مرا به یاد داشته باش
		Query RMDeletequery = session.createQuery("DELETE FROM RememberInfo AS R"
				+ " WHERE R.userId = :xuserId");
		RMDeletequery.setParameter("xuserId", userId);
		RMDeletequery.executeUpdate();
	}

	@Transactional
	@Override
	public boolean isValidUser(int userId, String password) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM UserInfo AS User"
				+ " WHERE User.id = :xuserId AND User.password = :xpassword");
		query.setParameter("xuserId", userId);
		query.setParameter("xpassword", password);
		
		List<UserInfo> list = query.getResultList();
		if(list.size() > 0){
			return true;
		}
		return false;
	}
	
}
