package com.phoenix.data.entity;

public class BoardStatistics {

	private long subscriberCount;
	private long postCount;
	public BoardStatistics(long subscriberCount, long postCount) {
		this.subscriberCount = subscriberCount;
		this.postCount = postCount;
	}
	public long getSubscriberCount() {
		return subscriberCount;
	}
	public void setSubscriberCount(long subscriberCount) {
		this.subscriberCount = subscriberCount;
	}
	public long getPostCount() {
		return postCount;
	}
	public void setPostCount(long postCount) {
		this.postCount = postCount;
	}
	@Override
	public String toString() {
		return "BoardStatistics [subscriberCount=" + subscriberCount + ", postCount=" + postCount + "]";
	}	
	}
