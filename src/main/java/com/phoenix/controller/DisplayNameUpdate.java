package com.phoenix.controller;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class DisplayNameUpdate {

	@NotBlank(message="رمز عبور نباید خالی باشد.")
	@Length(min=8, max=50, message="رمز عبور باید دارای حداقل هشت و حداکثر پنجاه حرف باشد.")
	private String password;
	
	@NotBlank(message="نام نمایشی نباید خالی باشد.")
	private String displayName;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
}
