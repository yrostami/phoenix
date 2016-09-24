package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="post_notification")
public class PostNotification {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	@SequenceGenerator(name="pnSeq", sequenceName="post_notification_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pnSeq")
	private long id;
	
	@Column(name="subscriber_id")
	private int subscriberId;
	
	@Column(name="post_id")
	private long postId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (postId ^ (postId >>> 32));
		result = prime * result + subscriberId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostNotification other = (PostNotification) obj;
		if (id != other.id)
			return false;
		if (postId != other.postId)
			return false;
		if (subscriberId != other.subscriberId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PostNotification [id=" + id + ", subscriberId=" + subscriberId + ", postId=" + postId + "]";
	}
	
	
	
}
