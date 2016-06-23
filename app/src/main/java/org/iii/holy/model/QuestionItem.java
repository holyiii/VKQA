package org.iii.holy.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionItem extends MessageBlockItem {
	private String title;
	private TagListItem tagList = new TagListItem();
	private List<AnswerItem> answers = new ArrayList<>();
	private long solutionId;
	private List<CommentItem> comments = new ArrayList<>();
	
	public QuestionItem(){}


	public List<AnswerItem> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerItem> answers) {
		this.answers = answers;
	}

	public long getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(long solutionId) {
		this.solutionId = solutionId;
	}

	public List<CommentItem> getComments() {
		return comments;
	}

	public void setComments(List<CommentItem> comments) {
		this.comments = comments;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public TagListItem getTagList() {
		return tagList;
	}
	public void setTagList(TagListItem tagList) {
		this.tagList = tagList;
	}

}
