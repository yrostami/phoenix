package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="system_info")
public class SystemInfo {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="max_stroge")
	private long maxStroge;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMaxStroge() {
		return maxStroge;
	}

	public void setMaxStroge(long maxStroge) {
		this.maxStroge = maxStroge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (int) (maxStroge^ (maxStroge >>> 32));
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
		SystemInfo other = (SystemInfo) obj;
		if (id != other.id)
			return false;
		if (maxStroge != other.maxStroge)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SystemInfo [id=" + id + ", maxStroge=" + maxStroge + "]";
	}
	
	

}
