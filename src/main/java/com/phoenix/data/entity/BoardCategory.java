package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="board_category")
public class BoardCategory {
	
	@Id
	@Column(name="id")
	@SequenceGenerator(name="boardCatSeq", sequenceName="board_category_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="boardCatSeq")
	private int id;
	
	@NotBlank(message="نام دسته نباید خالی باشد.")
	@Length(min=5, max=100, message="نام دسته باید دارای حداقل پنج و حداکثر صد حرف باشد.")
	@Column(name="name", length=100)
	private String name;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BoardCategory other = (BoardCategory) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardCategory [id=" + id + ", name=" + name + "]";
	}
	
	
	
}
