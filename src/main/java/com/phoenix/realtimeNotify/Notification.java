package com.phoenix.realtimeNotify;

public class Notification {

	private int notificationSource;
	private String massege;
	
	public Notification(int notificationSource, String massege) {
		this.notificationSource = notificationSource;
		this.massege = massege;
	}

	public int getNotificationSource() {
		return notificationSource;
	}

	public void setNotificationSource(int notificationSource) {
		this.notificationSource = notificationSource;
	}

	public String getMassege() {
		return massege;
	}

	public void setMassege(String massege) {
		this.massege = massege;
	}

	@Override
	public String toString() {
		return "notification [notificationSource=" + notificationSource + ", massege=" + massege + "]";
	}
	
}
