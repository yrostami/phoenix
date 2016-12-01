package com.phoenix.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.expression.Operation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.phoenix.authentication.AuthenticationFilter;
import com.phoenix.authentication.MD5Hash;
import com.phoenix.data.entity.BoardCategory;
import com.phoenix.data.entity.BoardInfo;
import com.phoenix.data.entity.BoardPost;
import com.phoenix.data.entity.FileInfo;
import com.phoenix.data.entity.PostNotification;
import com.phoenix.data.entity.Publisher;
import com.phoenix.data.entity.SubscribedBoardInfo;
import com.phoenix.data.entity.UserInfo;
import com.phoenix.realtimeNotify.Notification;
import com.phoenix.realtimeNotify.NotifierManager;
import com.phoenix.realtimeNotify.NotifyTask;

@Service
public class PublisherServiceImp implements PublisherService {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	ApplicationContext applicationContext;
	
	private static final Logger logger = LoggerFactory.getLogger(PublisherServiceImp.class);
	
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
		}
		return publisher;
	}
	
	@Override
	@Transactional
	public int saveBoard(BoardInfo newBoard) {
		Session session = sessionFactory.getCurrentSession();
		int boardId = (int) session.save(newBoard);
		return boardId;
	}
	
	@Transactional
	@Override
	public void updateBoard(BoardInfo board) {
		sessionFactory.getCurrentSession().update(board);
	}

	@Transactional
	@Override
	public void deleteBoard(int boardId, int userId) 
	{
		Session session = sessionFactory.getCurrentSession();
		//حذف اطلاعات دنبال کننده ها
		Query subscribersQuery = session.createQuery("DELETE FROM SubscribedBoardInfo AS SBI "
				+ "WHERE SBI.boardId = :xboardId");
		subscribersQuery.setParameter("xboardId", boardId);
		subscribersQuery.executeUpdate();
		//حذف پست های برد
		Query postsQuery = session.createQuery("FROM BoardPost AS P "
				+ "WHERE P.boardId = :xboardId");
		postsQuery.setParameter("xboardId", boardId);
		List<BoardPost> posts = postsQuery.getResultList();
		if(posts.size() > 0){
			for(BoardPost post : posts)
				deletePost(post, userId);}
		//حذف برد
		Query boardDeleteQuery = session.createQuery("DELETE FROM BoardInfo AS B "
				+ "WHERE B.id = :xboardId");
		boardDeleteQuery.setParameter("xboardId", boardId);
		boardDeleteQuery.executeUpdate();
	}
	
	@Transactional
	public void deletePost(BoardPost post, int userId)
	{
		Session session = sessionFactory.getCurrentSession(); 
//		//حذف اعلان های پست
//		Query notificationDeleteQuery = session.createQuery("DELETE FROM PostNotification AS PN "
//				+ "WHERE PN.postId = :xpostId");
//		notificationDeleteQuery.setParameter("xpostId", post.getId());
//		notificationDeleteQuery.executeUpdate();
		//حذف پست و فایل مربوطه
			if(post.getFileInfo() != null){
				String fileName = post.getFileInfo().getFilePath();
				fileName = fileName.substring(fileName.lastIndexOf("/")+1);
				try {
					deleteFile(Integer.toString(post.getBoardId()), fileName);
				} catch (IOException e) {
					logger.error("post delete for board delet:\n\tfile name: "+fileName, e);
				}
				updateStrogeUsage(userId, post.getFileInfo().getFileSize(), Operation.SUBTRACT);
				session.delete(post.getFileInfo());
			}
			session.delete(post);
	}

	@Transactional
	@Override
	public boolean isValidOwnership(int userId, int boardId) {
		boolean valid = false;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BoardInfo AS BI WHERE "
				+ "BI.publisherId = :xuserId AND BI.id = :xboardId");
		query.setParameter("xuserId", userId);
		query.setParameter("xboardId", boardId);
		List list = query.getResultList();
		if(list.size()>0)
			valid = true;
		return valid;
	}

	@Override
	@Transactional
	public long savePost(BoardPost newPost) {
		Session session = sessionFactory.getCurrentSession();
		long postId = (long) session.save(newPost);
//		taskExecutor.execute(applicationContext.getBean(NotifyTask.class,newPost.getBoardId(),postId));
		return postId;
	}
	
	@Transactional
	@Override
	public boolean savePermission(int userId, long fileSize) {
		Session session = sessionFactory.getCurrentSession();
		long userStrogeUsage = -1;
		long strogeSize = -1;
		Query getUserStrogeUsage = session.createQuery("SELECT U.strogeUsage FROM UserInfo AS U "
				+ "WHERE U.id = :xid");
		getUserStrogeUsage.setParameter("xid", userId);
		Query getStrogeSize = session.createQuery("SELECT SI.maxStroge FROM SystemInfo AS SI "
				+ "WHERE SI.id = 100");
		List list = getUserStrogeUsage.getResultList();
		if(list.size()>0)
			userStrogeUsage = (long) list.get(0);
		list = getStrogeSize.getResultList();
		if (list.size()>0)
			strogeSize = (long) list.get(0);
		if(userStrogeUsage != -1 && strogeSize != -1
				&& (userStrogeUsage + fileSize) <= strogeSize )
			return true;
		
		return false;
	}

	@Override
	public String saveFile(MultipartFile file, String parentDir, String fileName)
			throws IllegalStateException, IOException 
	{
		Path saveFilePath = Paths.get(System.getenv("PHOENIX_UPLOADED_FILES_LOCATION") + File.separator
				+ parentDir + File.separator + fileName);
		if(Files.notExists(saveFilePath.getParent()))
			Files.createDirectories(saveFilePath.getParent());
		while(Files.exists(saveFilePath))
		{
			fileName = MD5Hash.getHashFrom(new SecureRandom().nextInt()+fileName)//ایجاد یک رشته تصادفی 
						+ fileName.substring(fileName.lastIndexOf("."));// اضافه کردن فرمت
			saveFilePath = Paths.get(System.getenv("PHOENIX_UPLOADED_FILES_LOCATION") 
					+ File.separator + parentDir + File.separator + fileName);
		}
		Files.createFile(saveFilePath);
		file.transferTo(saveFilePath.toFile());
		return new String("/" + parentDir + "/" + fileName);
	}

	@Override
	@Transactional
	public void saveFileInfo(FileInfo fileInfo) {
		Session session = sessionFactory.getCurrentSession();
		session.save(fileInfo);
	}
	
	@Override
	@Transactional
	public void updateStrogeUsage(int userId, long Value, Operation op) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM UserInfo AS U WHERE U.id = :xuserId");
		query.setParameter("xuserId", userId);
		List list = query.getResultList();
		if(list.size()>0)
		{
			UserInfo userInfo = (UserInfo) list.get(0);
			if (op.equals(Operation.ADD))
				userInfo.setStrogeUsage(userInfo.getStrogeUsage() + Value);
			else if (op.equals(Operation.SUBTRACT))
				userInfo.setStrogeUsage(userInfo.getStrogeUsage() - Value);
			
			session.update(userInfo);
		}
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
	@Transactional
	public List<BoardPost> getMyBoardPosts(int boardId, int startResult) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM BoardPost AS BP "
				+ "WHERE BP.boardId = :xboardId "
				+ "ORDER BY BP.creationDate DESC");
		query.setParameter("xboardId", boardId);
		query.setFirstResult(startResult);
		query.setMaxResults(10);
		@SuppressWarnings("unchecked")
		List<BoardPost> list = (List<BoardPost>) query.getResultList();
		return list;
	}
	
	@Transactional
	@Override
	public void deletePost(long postId, int boardId, int userId) throws IOException {
		Session session = sessionFactory.getCurrentSession();
		//حذف اعلان های پست
//		Query notificationDeleteQuery = session.createQuery("DELETE FROM PostNotification AS PN "
//				+ "WHERE PN.postId = :xpostId");
//		notificationDeleteQuery.setParameter("xpostId", postId);
//		notificationDeleteQuery.executeUpdate();
		
		//حذف پست و فایل مربوطه
		Query query = session.createQuery("FROM BoardPost AS BP WHERE "
				+ "BP.id = :xid AND BP.boardId = :xboardId");
		query.setParameter("xid", postId);
		query.setParameter("xboardId", boardId);
		List<BoardPost> list= query.getResultList();
		BoardPost post = null;
		if(list.size() > 0){
			post = list.get(0);
			if(post.getFileInfo() != null){
				String fileName = post.getFileInfo().getFilePath();
				fileName = fileName.substring(fileName.lastIndexOf("/")+1);
				deleteFile(Integer.toString(post.getBoardId()), fileName);
				updateStrogeUsage(userId, post.getFileInfo().getFileSize(), Operation.SUBTRACT);
				session.delete(post.getFileInfo());
			}
			session.delete(post);
		}
	}

	@Override
	public void deleteFile(String parentDir, String fileName) throws IOException {
		Path filePath = Paths.get(System.getenv("PHOENIX_UPLOADED_FILES_LOCATION") + File.separator
				+ parentDir + File.separator + fileName);
		if(Files.exists(filePath))
			Files.delete(filePath);
	}

	@Override
	public List<UserInfo> getChannelSubscribers() {
		return null;
	}

}
