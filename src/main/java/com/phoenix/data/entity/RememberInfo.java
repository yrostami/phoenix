package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="remember_info")
public class RememberInfo {

	@Id
	@Column(name="id")
	@SequenceGenerator(name="rememberSeq", sequenceName="remember_me_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rememberSeq")
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="cookie_name")
	private String cookieName;
	
	@Column(name="token")
	private String token;
	
	@Column(name="user_role")
	private String userRole;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cookieName == null) ? 0 : cookieName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
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
		RememberInfo other = (RememberInfo) obj;
		if (cookieName == null) {
			if (other.cookieName != null)
				return false;
		} else if (!cookieName.equals(other.cookieName))
			return false;
		if (id != other.id)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (userId != other.userId)
			return false;
		if (userRole == null) {
			if (other.userRole != null)
				return false;
		} else if (!userRole.equals(other.userRole))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RememberInfo [id=" + id + ", userId=" + userId + ", cookieName=" + cookieName + ", token=" + token
				+ ", userRole=" + userRole + "]";
	}

	
}
