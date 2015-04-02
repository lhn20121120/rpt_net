package com.cbrc.smis.entity;

import java.io.Serializable;

public class AfTemplateReviewId implements  Serializable{
	private String templateId;
	private String versionId;
	private String term;
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public AfTemplateReviewId() {
		// TODO Auto-generated constructor stub
	}
	
	public AfTemplateReviewId(String templateId,String versionId,String term) {
		// TODO Auto-generated constructor stub
		this.templateId=templateId;
		this.versionId=versionId;
		this.term = term;
	}
	
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
