package org.iii.holy.model;


public class UserItem {
	private String name = null;
	private Long id = null;
	private long karma = 0;
	private String email = null;
	public UserItem(){}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getKarma() {
		return karma;
	}
	public void setKarma(long karma) {
		this.karma = karma;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
