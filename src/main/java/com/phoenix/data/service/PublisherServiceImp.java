package com.phoenix.data.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.PostNotification;
import com.phoenix.data.entity.Publisher;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.UserInfo;

@Service
public class PublisherServiceImp implements PublisherService {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Publisher getUser(int userId) {
		Publisher publisher= null;
		Session session = sessionFactory.getCurrentSession();
		Query userQuery = session.createQuery("FROM Publisher AS P WHERE P.id = :xid");
		userQuery.setParameter("xid", userId);
		List list = userQuery.getResultList();
		if(list.size() > 0){
			publisher = (Publisher) list.get(0);
			Query deleteQuery= session.createQuery("DELETE FROM PostNotification AS PN "
					+ "WHERE PN.subscriberId = :xid");
			deleteQuery.setParameter("xid", userId);
			deleteQuery.executeUpdate();
		}
		return publisher;
	}
	
	@Override
	@Transactional
	public void saveBoard(BoardInfo newBoard) {
		Session session = sessionFactory.getCurrentSession();
		session.save(newBoard);
	}
	
	@Override
	@Transactional
	public void savePost(BoardPost newPost) {
		Session session = sessionFactory.getCurrentSession();
		session.save(newPost);
		Query query = session.createQuery("FROM SubscribedBoardInfo AS SB WHERE "
				+"SB.boardId = :xboardId");
		query.setParameter("xboardId", newPost.getBoardId());
		List subscribers = query.getResultList();
		PostNotification pn = null;
		for(int i=subscribers.size()-1;i>=0;i--)
		{
			SubscribedBoardInfo sbi = (SubscribedBoardInfo) subscribers.get(i);
			pn = new PostNotification();
			pn.setPostId(newPost.getId());
			pn.setSubscriberId(sbi.getSubscriberId());
			session.save(pn);
			if ( i % 20 == 0 ) 
			{
		        session.flush();
		        session.clear();
		    }
		}
	}
	
	@Override
	public boolean saveFile(MultipartFile file, String path) {
		boolean SuccessfullyDone = true;
		File saveFile = new File(System.getenv("PHOENIX_UPLOADED_FILES_LOCATION") + path);
		try {
			file.transferTo(saveFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			SuccessfullyDone = false;
		} catch (IOException e) {
			e.printStackTrace();
			SuccessfullyDone = false;
		}
		return SuccessfullyDone;
	}

	@Override
	@Transactional
	public boolean isValid(BoardCategory category) {
		boolean valid = false;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BoardCategory AS Cat "
				+ "WHERE Cat.id = :xid AND Cat.name = :xname");
		query.setParameter("xid", category.getId());
		query.setParameter("xname", category.getName());
		List list=query.getResultList();
		if(list.size()>0) 
			valid = true;
		return valid;
	}

	@Override
	public List<UserInfo> getChannelSubscribers() {
		// TODO Auto-generated method stub
		return null;
	}

}
