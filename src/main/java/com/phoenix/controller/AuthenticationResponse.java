package com.phoenix.controller;

public class AuthenticationResponse {
	
	private String username;
	private String displayName;
	private String role;
	
	public AuthenticationResponse(String username, String displayName, String role) {
		super();
		this.username = username;
		this.displayName = displayName;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	@Override
	public String toString() {
		return "AuthenticationResponse [username=" + username + ", displayName=" + displayName + ", role=" + role + "]";
	}
	
}
