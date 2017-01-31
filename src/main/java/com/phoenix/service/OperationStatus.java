package com.phoenix.service;

public enum OperationStatus {

	
	SUCCESSFUL("تغییرات با موفقیت انجام شد.",0),
	FAIL("تغییرات انجام نشد.",1),
	PERMISSIONFAIL("شما اجازه دسترسی ندارید.",2);
	
	private final String message;
	private final int code;
	
	OperationStatus(String msg,int xcode)
	{
		message=msg;
		code=xcode;
	}

	public String getMassege() {
		return message;
	}

	public int getCode() {
		return code;
	}
	
	
}
