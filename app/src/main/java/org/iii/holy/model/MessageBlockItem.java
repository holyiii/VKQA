package org.iii.holy.model;


public class MessageBlockItem {


    private Long id;
    
    private String content;

    private UserItem author;

	private long voteCount = 0;
    private long createdAt;
    private long lastUpdatedAt;
    
    public MessageBlockItem(){}
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public UserItem getAuthor() {
		return author;
	}
	public void setAuthor(UserItem author) {
		this.author = author;
	}
	public long getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(long lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
	
}
