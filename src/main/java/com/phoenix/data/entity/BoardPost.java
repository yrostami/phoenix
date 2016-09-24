package com.phoenix.data.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="board_post")
public class BoardPost {

	@Id
	@Column(name="id")
	@SequenceGenerator(name="bpSeq", sequenceName="board_post_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bpSeq")
	private long id;
	
	
	@NotNull(message="شناسه کانال مورد نظر نباید خالی باشد.")
	@Column(name="board_id")
	private int BoardId;
	
	@JsonIgnore
	@Column(name="creation_date")
	private Date CreationDate;
	
	@NotBlank(message="محتوای اطلاعیه نباید خالی باشد.")
	@Column(name="content", length=1500)
	private String content;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBoardId() {
		return BoardId;
	}

	public void setBoardId(int boardId) {
		BoardId = boardId;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + BoardId;
		result = prime * result + ((CreationDate == null) ? 0 : CreationDate.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		BoardPost other = (BoardPost) obj;
		if (BoardId != other.BoardId)
			return false;
		if (CreationDate == null) {
			if (other.CreationDate != null)
				return false;
		} else if (!CreationDate.equals(other.CreationDate))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardPost [id=" + id + ", BoardId=" + BoardId + ", CreationDate=" + CreationDate + ", content="
				+ content + "]";
	}

		
}
