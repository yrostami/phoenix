package com.phoenix.service;

public enum OperationStatus {

	
	SUCCESSFUL("تغییرات با موفقیت انجام شد.",0),
	FAIL("تغییرات انجام نشد.",1),
	PERMISSIONFAIL("شما اجازه دسترسی ندارید.",2);
	
	private final String massege;
	private final int code;
	
	OperationStatus(String msg,int xcode)
	{
		massege=msg;
		code=xcode;
	}

	public String getMassege() {
		return massege;
	}

	public int getCode() {
		return code;
	}
	
	
}
