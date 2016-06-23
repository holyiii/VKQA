package org.iii.holy.model;


public class QuestionShort {
	private Long id; //question id
	private String title;
	private long view;
	private long answerCount;
	private long voteCount;

	private TagListItem tagList = new TagListItem();
	private long createdAt;
	private long lastUpdatedAt;
	private UserItem author;
	
	public QuestionShort() {}

	

	public TagListItem getTagList() {
		return tagList;
	}

	public void setTagList(TagListItem tagList) {
		this.tagList = tagList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getView() {
		return view;
	}

	public void setView(long view) {
		this.view = view;
	}

	public long getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(long answerCount) {
		this.answerCount = answerCount;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}

	public UserItem getAuthor() {
		return author;
	}

	public void setAuthor(UserItem author) {
		this.author = author;
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


	
}
