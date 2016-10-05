package com.phoenix.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	private Timestamp CreationDate;
	
	@NotBlank(message="عنوان اطلاعیه نباید خالی باشد.")
	@Length(max=150,message="عنوان اطلاعیه باید حداکثر شامل 150 حرف باشد.")
	@Column(name="title", length=150)
	private String title;
	
	@NotBlank(message="محتوای اطلاعیه نباید خالی باشد.")
	@Length(max=1500,message="محتوای اطلاعیه باید حداکثر شامل 1500 حرف باشد.")
	@Column(name="content", length=1500)
	private String content;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="file_id")
	@Fetch(FetchMode.JOIN)
	private FileInfo fileInfo;

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

	@JsonProperty
	public Timestamp getCreationDate() {
		return CreationDate;
	}

	@JsonIgnore
	public void setCreationDate(Timestamp creationDate) {
		CreationDate = creationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonProperty
	public FileInfo getFileInfo() {
		return fileInfo;
	}
	
	@JsonIgnore
	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + BoardId;
		result = prime * result + ((CreationDate == null) ? 0 : CreationDate.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((fileInfo == null) ? 0 : fileInfo.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		if (fileInfo == null) {
			if (other.fileInfo != null)
				return false;
		} else if (!fileInfo.equals(other.fileInfo))
			return false;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardPost [id=" + id + ", BoardId=" + BoardId + ", CreationDate=" + CreationDate + ", title=" + title
				+ ", content=" + content + ", fileInfo=" + fileInfo + "]";
	}
	
	
}
