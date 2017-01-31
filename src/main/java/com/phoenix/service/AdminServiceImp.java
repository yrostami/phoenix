package com.phoenix.service;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.PublishReqForAdmin;
import com.phoenix.data.entity.PublishRequest;
import com.phoenix.data.entity.SystemInfo;
import com.phoenix.data.entity.UserInfo;
import com.phoenix.data.entity.UserInfoForAdmin;

@Service
public class AdminServiceImp implements AdminService {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	PublisherService publisherService;

	@Transactional
	@Override
	public List<PublishReqForAdmin> getRequests(int firstResult, int maxResult) {
		Session session = sessionFactory.getCurrentSession();
		Criteria query= session.createCriteria(PublishReqForAdmin.class);
//		query.add(Restrictions.eq("checked", false));
		query.addOrder(Order.desc("creationDate"));
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.list();
	}

	@Transactional
	@Override
	public void escapePublishRequest(int reqId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM PublishRequest AS PR "
				+ "WHERE PR.id = :xid");
		query.setParameter("xid", reqId);
		List<PublishRequest> list = query.getResultList();
		if(list.size()>0)
		{
			PublishRequest req = list.get(0);
			req.setChecked(true);
			req.setAgreement(false);
		}
		
	}

	@Transactional
	@Override
	public void acceptPublishRequest(int reqId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM PublishRequest AS PR "
				+ "WHERE PR.id = :xid");
		query.setParameter("xid", reqId);
		List<PublishRequest> list = query.getResultList();
		if(list.size()>0)
		{
			PublishRequest req = list.get(0);
			req.setChecked(true);
			req.setAgreement(true);
			Query userQuery = session.createQuery("FROM UserInfo AS U WHERE U.id = :xid");
			userQuery.setParameter("xid", req.getUserId());
			List<UserInfo> userList = userQuery.getResultList();
			if(userList.size()>0)
			{
				UserInfo user = userList.get(0);
				user.setRole("Publisher");
				session.update(user);
				
				//حذف اطلاعات مربوط به  مرا به یاد داشته باش
				Query RMDeletequery = session.createQuery("DELETE FROM RememberInfo AS R"
						+ " WHERE R.userId = :xuserId");
				RMDeletequery.setParameter("xuserId", user.getId());
				RMDeletequery.executeUpdate();
			}
		}
	}

	@Transactional
	@Override
	public void switchRoleToSubscriber(int userId) {
		Session session = sessionFactory.getCurrentSession();
		Query userQuery = session.createQuery("FROM UserInfo AS U WHERE U.id = :xid");
		userQuery.setParameter("xid", userId);
		List<UserInfo> userList = userQuery.getResultList();
		if(userList.size()>0)
		{
			UserInfo user = userList.get(0);
			user.setRole("Subscriber");
			session.update(user);
			
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
			
			//حذف اطلاعات مربوط به  مرا به یاد داشته باش
			Query RMDeletequery = session.createQuery("DELETE FROM RememberInfo AS R"
					+ " WHERE R.userId = :xuserId");
			RMDeletequery.setParameter("xuserId", userId);
			RMDeletequery.executeUpdate();
		}
	}

	@Transactional
	@Override
	public void setMaxStorage(long maxStorage) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM SystemInfo AS SI WHERE SI.id = :xid");
		query.setParameter("xid", 100);
		List<SystemInfo> list = query.getResultList();
		if(list.size()>0)
		{
			SystemInfo sysInfo = list.get(0);
			sysInfo.setMaxStroge(maxStorage);
			session.update(sysInfo);
		}
	}

	@Transactional
	@Override
	public List<UserInfoForAdmin> searchUser(String role, String searchType, String searchWord) {
		Session session = sessionFactory.getCurrentSession();
		Criteria query = session.createCriteria(UserInfoForAdmin.class);
		if(role.equals("subscriber"))
			query.add(Restrictions.eq("role", "Subscriber"));
		else if(role.equals("publisher"))
			query.add(Restrictions.eq("role", "Publisher"));
		else
		{
			String[] roles = {"Publisher","Subscriber"};
			query.add(Restrictions.in("role",roles));
		}
		
		if(searchType.equals("name"))
			query.add(Restrictions.like("displayName",searchWord,MatchMode.ANYWHERE));
		else
			query.add(Restrictions.like("email",searchWord,MatchMode.ANYWHERE));
			
		return query.list();
	}

	@Transactional
	@Override
	public int saveNewCategory(BoardCategory category) {
		Session session = sessionFactory.getCurrentSession();
		return (int) session.save(category);
	}

	@Transactional
	@Override
	public int deleteCategory(int categoryId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM BoardCategory AS BC WHERE BC.id = :xid");
		query.setParameter("xid", categoryId);
		return query.executeUpdate();
	}

	@Transactional
	@Override
	public void updateCategoryName(int categoryId, String categoryNewName) {
		Query query = sessionFactory.getCurrentSession()
				.createQuery("UPDATE FROM BoardCategory AS BC SET BC.name = :xname"
						+ " WHERE BC.id = :xid");
		query.setParameter("xname", categoryNewName);
		query.setParameter("xid", categoryId);
		query.executeUpdate();
	}

	@Transactional
	@Override
	public long getBoardCountInCategory(int categoryId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BoardCategory AS BC WHERE BC.id = :xid");
		query.setParameter("xid", categoryId);
		List<BoardCategory> list = query.getResultList();
		if(list.size()>0)
		{
			Criteria criteria = session.createCriteria(BoardInfo.class);
			criteria.add(Restrictions.eq("category", list.get(0)));
			criteria.setProjection(Projections.rowCount());
			return (long) criteria.uniqueResult();
		}
		return 0;
	}
	
}