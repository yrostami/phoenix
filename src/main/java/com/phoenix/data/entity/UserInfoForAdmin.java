package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="user_info")
public class UserInfoForAdmin {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="email", length=320)
	private String email;
	
	@JsonIgnore
	private String password;
	
	@Column(name="display_name", length=100)
	private String displayName;
	
	@Column(name="role")
	private String role;
	
	@JsonIgnore
	@Column(name="stroge_usage")
	private long strogeUsage;
	
	@JsonIgnore
	@Column(name="active")
	private boolean active;

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonIgnore
	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@JsonProperty
	public long getStrogeUsage() {
		return strogeUsage;
	}

	@JsonIgnore
	public void setStrogeUsage(long strogeUsage) {
		this.strogeUsage = strogeUsage;
	}


}
