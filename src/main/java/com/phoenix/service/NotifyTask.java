package com.phoenix.service;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

import com.phoenix.data.entity.PostNotification;

public class NotifyTask implements Runnable {

	private int boardId;
	private long postId;
	private ApplicationContext appContext;

	public NotifyTask(int boardId, long postId) {
		super();
		this.boardId = boardId;
		this.postId = postId;
		ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
		appContext = applicationContextProvider.getApplicationContext();
	}

	@Override
	public void run() {
		SessionFactory sessionFactory = appContext.getBean(SessionFactory.class);
		Session session;
		Transaction tx;
		session = sessionFactory.openSession();
		tx = session.getTransaction();
		try {
			tx.begin();
			Query subscribersQuery = session.createQuery("SELECT SBI.subscriberId" 
					+ " FROM SubscribedBoardInfo AS SBI" 
					+ " WHERE SBI.boardId = :xboardId");
			subscribersQuery.setParameter("xboardId", boardId);
			List<Integer> subscribersIdList = (List<Integer>) subscribersQuery.getResultList();

			for (int i = subscribersIdList.size() - 1; i >= 0; i--) {
				PostNotification notification = new PostNotification();
				notification.setBoardId(boardId);
				notification.setPostId(postId);
				notification.setUserId(subscribersIdList.get(i));
				session.save(notification);

				if (i % 20 == 0)
					session.flush();
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
		session.close();
	}

}
