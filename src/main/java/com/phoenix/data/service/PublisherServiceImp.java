package com.phoenix.data.service;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.Publisher;
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
