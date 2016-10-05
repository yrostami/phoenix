package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="file_info")
public class FileInfo {
	
	@Id
	@Column(name="id")
	@SequenceGenerator(name="fileInfoSeq", sequenceName="file_info_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fileInfoSeq")
	private long id;
	
	@Column(name="file_type", length=100)
	private String fileType;

	@Column(name="file_path")
	private String filePath;
	
	@Column(name="file_size")
	private long fileSize;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + (int) (fileSize ^ (fileSize >>> 32));
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
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
		FileInfo other = (FileInfo) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (fileSize != other.fileSize)
			return false;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", fileType=" + fileType + ", filePath=" + filePath
				+ ", fileSize=" + fileSize + "]";
	}
	
	
}
