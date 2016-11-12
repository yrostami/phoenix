package com.phoenix.data.entity;

import java.sql.Timestamp;

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
	
	@Column(name="last_login")
	private Timestamp lastLogin;
	
	@Column(name="agent")
	private String agent;

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

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agent == null) ? 0 : agent.hashCode());
		result = prime * result + ((cookieName == null) ? 0 : cookieName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + userId;
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
		if (agent == null) {
			if (other.agent != null)
				return false;
		} else if (!agent.equals(other.agent))
			return false;
		if (cookieName == null) {
			if (other.cookieName != null)
				return false;
		} else if (!cookieName.equals(other.cookieName))
			return false;
		if (id != other.id)
			return false;
		if (lastLogin == null) {
			if (other.lastLogin != null)
				return false;
		} else if (!lastLogin.equals(other.lastLogin))
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
				+ ", userRole=" + userRole + ", lastLogin=" + lastLogin + ", agent=" + agent + "]";
	}
	
}
