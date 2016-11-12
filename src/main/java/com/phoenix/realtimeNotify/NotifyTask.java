	package com.phoenix.realtimeNotify;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.phoenix.data.entity.PostNotification;
import com.phoenix.data.entity.SubscribedBoardInfo;

@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotifyTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(NotifyTask.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	int boardId;
	long postId;
	
public NotifyTask(int boardId, long postId) {
		this.boardId = boardId;
		this.postId = postId;
	}

	@Override
	public void run() {
		Transaction tx = null;
		Session session = null;
		try{
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("FROM SubscribedBoardInfo AS SB WHERE "
				+"SB.boardId = :xboardId");
		query.setParameter("xboardId",boardId);
		List subscribers = query.getResultList();
		PostNotification pn = null;
//		int[] subscribersId = new int[subscribers.size()];
		for(int i=subscribers.size()-1;i>=0;i--)
		{
			SubscribedBoardInfo sbi = (SubscribedBoardInfo) subscribers.get(i);
			pn = new PostNotification();
			pn.setPostId(postId);
			pn.setSubscriberId(sbi.getSubscriberId());
			session.save(pn);
			
			if ( i % 20 == 0 ) 
			{
		        session.flush();
		        session.clear();
		    }
//			subscribersId[i] = sbi.getSubscriberId();
		}
		tx.commit();
//		NotifierManager.getNotifierManager().notifyByList(subscribersId,
//				new Notification(boardId,"شما یک اعلان جدید دارید."));
		}catch(Exception ex){
			logger.error("\nNotifyTask:\n\tboard id: "+boardId+"\n\tpost id: "+postId, ex);
		}
	}

}
