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
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="post_id")
	private long postId;
	
	@Column(name="board_id")
	private int boardId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + boardId;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (postId ^ (postId >>> 32));
		result = prime * result + userId;
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
		if (boardId != other.boardId)
			return false;
		if (id != other.id)
			return false;
		if (postId != other.postId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PostNotification [id=" + id + ", userId=" + userId + ", postId=" + postId + ", boardId=" + boardId
				+ "]";
	}
	
}
