package org.iii.holy.model;


public class Auth {
	private boolean success = false;
	
	private UserItem user;
	
	public Auth() {}
	public Auth(boolean success) {
		this.setSuccess(success);
	}

	
	public UserItem getUser() {
		return user;
	}
	public void setUser(UserItem user) {
		this.user = user;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
