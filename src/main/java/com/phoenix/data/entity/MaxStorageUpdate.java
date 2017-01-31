package com.phoenix.data.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class MaxStorageUpdate {
	
	@NotBlank(message="رمز عبور نباید خالی باشد.")
	@Length(min=8, max=50, message="رمز عبور باید دارای حداقل هشت و حداکثر پنجاه حرف باشد.")
	private String password;
	
	@NotNull
	private long maxStorage;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getMaxStorage() {
		return maxStorage;
	}
	public void setMaxStorage(long maxStorage) {
		this.maxStorage = maxStorage;
	}
	
	

}
