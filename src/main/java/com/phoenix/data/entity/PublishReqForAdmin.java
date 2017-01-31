package com.phoenix.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="publish_request")
public class PublishReqForAdmin {

	@JsonIgnore
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="description")
	private String description;
	
	@JsonIgnore
	@Column(name="creation_date")
	private Timestamp creationDate;
	
	@JsonIgnore
	@Column(name="agreement")
	private boolean agreement;

	@JsonIgnore
	@Column(name="checked")
	private boolean checked;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@Fetch(FetchMode.JOIN)
	private UserInfoForAdmin user;

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonIgnore
	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	public Timestamp getCreationDate() {
		return creationDate;
	}
	
	@JsonIgnore
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty
	public boolean isAgreement() {
		return agreement;
	}
	
	@JsonIgnore
	public void setAgreement(boolean agreement) {
		this.agreement = agreement;
	}

	@JsonProperty
	public boolean isChecked() {
		return checked;
	}

	@JsonIgnore
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public UserInfoForAdmin getUser() {
		return user;
	}

	public void setUser(UserInfoForAdmin user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (agreement ? 1231 : 1237);
		result = prime * result + (checked ? 1231 : 1237);
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		PublishReqForAdmin other = (PublishReqForAdmin) obj;
		if (agreement != other.agreement)
			return false;
		if (checked != other.checked)
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "publishReqForAdmin [id=" + id + ", description=" + description + ", creationDate=" + creationDate
				+ ", agreement=" + agreement + ", checked=" + checked + ", user=" + user + "]";
	}
	
}
