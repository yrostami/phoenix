package com.phoenix.realtimeNotify;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class Notifier {	
	private static final Logger mylogger = LoggerFactory.getLogger(Notifier.class);
	
	private int subscriberCount;
	private int userId;
	private SseEmitter emitter;
	
	public Notifier(int userId) {
		this.subscriberCount = 1;
		this.userId = userId;
		this.emitter = new SseEmitter();
	}
	
	public void sendNotification(Notification notification)
	{
		try {
			emitter.send(notification, MediaType.APPLICATION_JSON_UTF8);
		} catch (IOException e) {
			mylogger.error("\nsendNotificationError: "+notification,e);
		}
	}

	public int getSubscriberCount() {
		return subscriberCount;
	}

	public void setSubscriberCount(int subscriberCount) {
		this.subscriberCount = subscriberCount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public SseEmitter getEmitter() {
		return emitter;
	}

	public void setEmitter(SseEmitter emitter) {
		this.emitter = emitter;
	}
	
	public int addSubscriber()
	{
		subscriberCount++;
		return subscriberCount;
	}
	
	public int subtractSubscriber()
	{
		subscriberCount--;
		return subscriberCount;
	}

	@Override
	public String toString() {
		return "Notifiable [sessionId=" + subscriberCount + ", userId=" + userId + ", emiter=" + emitter + "]";
	}
}