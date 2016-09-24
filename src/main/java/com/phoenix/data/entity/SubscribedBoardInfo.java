package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="subscribed_board")
public class SubscribedBoardInfo {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	@SequenceGenerator(name="sbSeq", sequenceName="subscribed_board_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sbSeq")
	private int id;
	
	@NotNull(message="شناسه دنبال کننده نباید خالی باشد.")
	@JsonIgnore
	@Column(name="subscriber_id")
	private int subscriberId;
	
	@NotNull(message="شناسه کانال نباید خالی باشد.")
	@Column(name="board_id")
	private int boardId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@JsonIgnore
	public int getSubscriberId() {
		return subscriberId;
	}

	@JsonProperty
	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
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
		result = prime * result + id;
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
		SubscribedBoardInfo other = (SubscribedBoardInfo) obj;
		if (boardId != other.boardId)
			return false;
		if (id != other.id)
			return false;
		if (subscriberId != other.subscriberId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubscribedBoardInfo [id=" + id + ", subscriberId=" + subscriberId + ", boardId=" + boardId + "]";
	}
	
	
}
