package com.phoenix.controller;

public class RegistrationResponse {

	private boolean success;
	private String massege;
	public RegistrationResponse(boolean success, String massege) {
		this.success = success;
		this.massege = massege;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMassege() {
		return massege;
	}
	public void setMassege(String massege) {
		this.massege = massege;
	}
	
	@Override
	public String toString() {
		return "RegistrationResponse [success=" + success + ", massege=" + massege + "]";
	}
	
	public static RegistrationResponse getSuccess()
	{
		return new RegistrationResponse(true, "ثبت نام با موفقیت انجام شد."
				+"\n"+"یک ایمیل برای تایید برای شما ارسال شد. حساب کاربری شما پس از تایید ایمیل فعال خواهد شد.");
	}
}
