package com.phoenix.controller;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class PasswordUpdate {
	
	@NotBlank(message="رمز عبور نباید خالی باشد.")
	@Length(min=8, max=50, message="رمز عبور باید دارای حداقل هشت و حداکثر پنجاه حرف باشد.")
	private String currentPassword;
	
	@NotBlank(message="رمز عبور نباید خالی باشد.")
	@Length(min=8, max=50, message="رمز عبور باید دارای حداقل هشت و حداکثر پنجاه حرف باشد.")
	private String newPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "passwordUpdate [currentPassword=" + currentPassword + ", newPassword=" + newPassword + "]";
	}
	
	
}
