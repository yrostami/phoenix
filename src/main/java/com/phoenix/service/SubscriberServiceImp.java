package com.phoenix.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.BoardStatistics;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.Subscriber;
import com.phoenix.data.entity.SystemInfo;

@Service
public class SubscriberServiceImp implements SubscriberService {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public Subscriber getUser(int userId) {
		Subscriber subscriber= null;
		Session session = sessionFactory.getCurrentSession();
		Query userQuery = session.createQuery("FROM Subscriber AS S WHERE S.id = :xid");
		userQuery.setParameter("xid", userId);
		List list = userQuery.getResultList();
		if(list.size() > 0)
			subscriber = (Subscriber) list.get(0);
		
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
	public boolean isSubscribed(int userId, int boardId) {
		boolean subscribed = false;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM SubscribedBoardInfo AS SBI "
				+ "WHERE SBI.subscriberId = :xuserId AND SBI.boardId = :xboardId");
		query.setParameter("xuserId", userId);
		query.setParameter("xboardId", boardId);
		List list = query.getResultList();
		if(list.size()>0)
			subscribed = true;
		
		return subscribed;
	}

	@Transactional
	@Override
	public void saveSubscribedBoardInfo(SubscribedBoardInfo sbi) {
		Session session = sessionFactory.getCurrentSession();
		session.save(sbi);
	}
	
	@Override
	@Transactional
	public SystemInfo getSystemInfo() {
		SystemInfo systemInfo = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM SystemInfo AS S WHERE S.id = :xid");
		query.setParameter("xid", 100);
		List list = query.getResultList();
		if(list.size()>0)
			systemInfo = (SystemInfo) list.get(0);
		return systemInfo;
	}

	@Override
	public File getFile(String parentDir, String fileName) {
		Path filePath = Paths.get(System.getenv("PHOENIX_UPLOADED_FILES_LOCATION")+File.separator+parentDir+File.separator+fileName);
		System.out.println(filePath.toString());
		if(Files.exists(filePath))
			return filePath.toFile();
		return null;
	}

	@Override
	@Transactional
	public BoardStatistics getBoardStatistics(int boardId) {
		Session session = sessionFactory.getCurrentSession();
		Query subscriberCountQuery = session.createQuery("SELECT COUNT(*) FROM SubscribedBoardInfo AS SBI"
				+ " WHERE SBI.boardId = :xboardId");
		subscriberCountQuery.setParameter("xboardId", boardId);
		long subscriberCount = (long) subscriberCountQuery.getSingleResult();
		
		Query postCountQuery = session.createQuery("SELECT COUNT(*) FROM BoardPost AS P"
				+ " WHERE P.boardId = :xboardId");
		postCountQuery.setParameter("xboardId", boardId);
		long postCount = (long) postCountQuery.getSingleResult();
		
		return new BoardStatistics(subscriberCount, postCount);
	}
	
	@Transactional
	@Override
	public List<BoardPost> getPosts(int userId, int maxResult) {
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("SELECT SBI.boardId FROM SubscribedBoardInfo AS SBI "
				+ "WHERE SBI.subscriberId = :xuserId");
		query.setParameter("xuserId", userId);
		List<Long> boardsId = query.getResultList();
		if(boardsId.size() > 0){
		Criteria criteria = session.createCriteria(BoardPost.class);
		criteria.add(Restrictions.in("boardId", boardsId));
		criteria.addOrder(Order.desc("creationDate"));
		criteria.setMaxResults(maxResult);
		List<BoardPost> list = criteria.list();
		return list;
		}
		
		return new ArrayList<BoardPost>();
	}

	@Transactional
	@Override
	public List<BoardPost> getBoardPosts(int boardId, int maxResult) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BoardPost.class);
		criteria.add(Restrictions.eq("boardId", boardId));
		criteria.addOrder(Order.desc("creationDate"));
		criteria.setMaxResults(maxResult);
		List<BoardPost> list = criteria.list();
		return list;
	}

	@Transactional
	@Override
	public List<BoardPost> getPostsBefore(int userId, Timestamp timestamp, int maxResult) {
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("SELECT SBI.boardId FROM SubscribedBoardInfo AS SBI "
				+ "WHERE SBI.subscriberId = :xuserId");
		query.setParameter("xuserId", userId);
		List<Long> boardsId = query.getResultList();
		if(boardsId.size() > 0){
		Criteria criteria = session.createCriteria(BoardPost.class);
		criteria.add(Restrictions.in("boardId", boardsId));
		criteria.add(Restrictions.lt("creationDate", timestamp));
		criteria.addOrder(Order.desc("creationDate"));
		criteria.setMaxResults(maxResult);
		List<BoardPost> list = criteria.list();
		return list;
		}
		
		return new ArrayList<BoardPost>();
	}

	@Transactional
	@Override
	public List<BoardPost> getBoardPostsBefore(int boardId, Timestamp timestamp, int maxResult) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BoardPost.class);
		criteria.add(Restrictions.eq("boardId", boardId));
		criteria.add(Restrictions.lt("creationDate", timestamp));
		criteria.addOrder(Order.desc("creationDate"));
		criteria.setMaxResults(maxResult);
		List<BoardPost> list = criteria.list();
		return list;
	}
	
	@Transactional
	@Override
	public boolean isValidBoard(int boardId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Board AS B WHERE B.id = :xid");
		query.setParameter("xid", boardId);
		List<Board> list = query.getResultList();
		if(list.size()>0)
			return true;
		return false;
	}

	@Transactional
	@Override
	public int deleteSubscribedBoardInfo(int userId, int boardId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM SubscribedBoardInfo AS SB "
				+ "WHERE SB.subscriberId = :xuserId AND SB.boardId = :xboardId");
		query.setParameter("xuserId", userId);
		query.setParameter("xboardId", boardId);
		int rows = query.executeUpdate();
		return rows;
	}
	
}
