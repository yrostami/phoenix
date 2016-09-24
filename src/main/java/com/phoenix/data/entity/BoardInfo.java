package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="board")
public class BoardInfo {

	@Id
	@Column(name="id")
	@SequenceGenerator(name="boardInfoSeq", sequenceName="board_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="boardInfoSeq")
	private int id;
	
	@NotBlank(message="نام نباید خالی باشد.")
	@Length(min=5, max=100, message="نام برداطلاع رسانی باید دارای حداقل پنج و حداکثر صد حرف باشد.")
	@Column(name="name", length=100)
	private String name;
	
	@NotBlank(message="درباره برد نباید خالی باشد.")
	@Column(name="about", length=1000)
	private String about;
	
	@NotNull(message="شناسه صاحب نباید خالی باشد.")
	@Column(name="publisher_id")
	private int publisherId;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	@Fetch(FetchMode.JOIN)
	private BoardCategory category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public int getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}

	public BoardCategory getCategory() {
		return category;
	}

	public void setCategory(BoardCategory category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((about == null) ? 0 : about.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + publisherId;
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
		BoardInfo other = (BoardInfo) obj;
		if (about == null) {
			if (other.about != null)
				return false;
		} else if (!about.equals(other.about))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publisherId != other.publisherId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardInfo [id=" + id + ", name=" + name + ", about=" + about + ", publisherId=" + publisherId
				+ ", category=" + category + "]";
	}

	
}
