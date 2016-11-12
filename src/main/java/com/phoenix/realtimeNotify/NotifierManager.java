package com.phoenix.realtimeNotify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class NotifierManager {

	private List<Notifier> NotifiersList;

	private NotifierManager() {
		NotifiersList = new ArrayList<Notifier>();
	}

	public synchronized SseEmitter getSseEmitter(int userId) {
		int first = 0;
		int last = NotifiersList.size() - 1;
		int middle;
		int middleUserId;

		while (first <= last) {
			middle = first + ((last - first) / 2);
			middleUserId = NotifiersList.get(middle).getUserId();
			if (userId == middleUserId) {
				NotifiersList.get(middle).addSubscriber();
				return NotifiersList.get(middle).getEmitter();
			}
			if (userId < middleUserId)
				last = middle - 1;

			else
				first = middle + 1;
		}
		Notifier notifier = new Notifier(userId);
		NotifiersList.add(first, notifier);
		return notifier.getEmitter();
	}

	public synchronized void notifyByList(int[] userList, Notification notification) {

		int size = NotifiersList.size();
		if (size == 0)
			return;
		int first = 0;
		int last = size - 1;
		int middle = first + ((last - first) / 2);
		int middleUserId;
		int userIndex = 0;
		int userListSize = userList.length;
		int userId = userList[userIndex];
		boolean exist;

		while (userIndex < userListSize && first < size) {
			exist = false;
			while (first <= last) {
				middle = first + ((last - first) / 2);
				middleUserId = NotifiersList.get(middle).getUserId();
				if (userId == middleUserId) {
					exist = true;
					try {
						NotifiersList.get(middle).getEmitter().send(notification, MediaType.APPLICATION_JSON_UTF8);
					} catch (IOException e) {
					}
					break;
				}
				if (userId < middleUserId)
					last = middle - 1;
				else
					first = middle + 1;
			}
			if (exist)
				first = middle + 1;

			last = size;
			userIndex++;
			userId = userList[userIndex];
		}
	}

	public synchronized void removeNotifier(int userId) {
		int first = 0;
		int last = NotifiersList.size() - 1;
		int middle;
		int middleUserId;

		while (first <= last) {
			middle = first + ((last - first) / 2);
			middleUserId = NotifiersList.get(middle).getUserId();
			if (userId == middleUserId) {
				if (NotifiersList.get(middle).subtractSubscriber() == 0)
					NotifiersList.remove(middle);
			}
			if (userId < middleUserId)
				last = middle - 1;
			else
				first = middle + 1;
		}
	}

	private static class singletonHelper {
		private static final NotifierManager INSTANCE = new NotifierManager();
	}

	public static NotifierManager getNotifierManager() {
		return singletonHelper.INSTANCE;
	}
}