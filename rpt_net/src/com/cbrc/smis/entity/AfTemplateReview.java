package com.cbrc.smis.entity;

public class AfTemplateReview {
	private AfTemplateReviewId id = new AfTemplateReviewId();
	
	private String reviewDate;
	private String reviewStatus;//…Û‘ƒ◊¥Ã¨£∫0Œ¥…Û‘ƒ£¨1“—…Û‘ƒ
	
	public AfTemplateReview() {
		// TODO Auto-generated constructor stub
	}
	
	public AfTemplateReview(AfTemplateReviewId id) {
		// TODO Auto-generated constructor stub
		this.id=id;
	}
	
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	
	public AfTemplateReviewId getId() {
		return id;
	}
	public void setId(AfTemplateReviewId id) {
		this.id = id;
	}
	
	
}
