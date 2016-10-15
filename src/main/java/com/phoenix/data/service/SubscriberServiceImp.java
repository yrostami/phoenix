package com.phoenix.data.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.data.entity.Board;
import com.phoenix.data.entity.BoardCategory;
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
	
}
