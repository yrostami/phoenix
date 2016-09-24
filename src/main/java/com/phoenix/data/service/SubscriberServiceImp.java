package com.phoenix.data.service;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;

@Service
public class SubscriberServiceImp implements SubscriberService {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Subscriber getUser(int userId) {
		Subscriber subscriber= null;
		Session session = sessionFactory.getCurrentSession();
		Query userQuery = session.createQuery("FROM Subscriber AS S WHERE S.id = :xid");
		userQuery.setParameter("xid", userId);
		List list = userQuery.getResultList();
		if(list.size() > 0){
			subscriber = (Subscriber) list.get(0);
			Query deleteQuery= session.createQuery("DELETE FROM PostNotification AS PN "
					+ "WHERE PN.subscriberId = :xid");
			deleteQuery.setParameter("xid", userId);
			deleteQuery.executeUpdate();
		}
		return subscriber;
	}
	
	@Transactional
	@Override
	public List<Board> getAllBoards() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Board");
		List<Board> list = query.getResultList();
		return list;
	}
	
	@Transactional
	@Override
	public List<BoardCategory> getAllBoardCategories() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BoardCategory");
		List<BoardCategory> list = query.getResultList();
		return list;
	}

	@Transactional
	@Override
	public List<Board> getBoardsFromCategory(int categoryId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Board AS B WHERE B.id = :xid");
		query.setParameter("xid", categoryId);
		List<Board> list = query.getResultList();
		return list;
	}

	@Transactional
	@Override
	public void saveSubscribedBoardInfo(SubscribedBoardInfo sbi) {
		Session session = sessionFactory.getCurrentSession();
		session.save(sbi);
	}
	
	

}
