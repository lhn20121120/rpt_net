package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskTrriger entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskTrriger implements java.io.Serializable {

	// Fields

	private String trrigerId;
	private String trrigerName;

	// Constructors

	/** default constructor */
	public WorkTaskTrriger() {
	}

	/** minimal constructor */
	public WorkTaskTrriger(String trrigerId) {
		this.trrigerId = trrigerId;
	}

	/** full constructor */
	public WorkTaskTrriger(String trrigerId, String trrigerName) {
		this.trrigerId = trrigerId;
		this.trrigerName = trrigerName;
	}

	// Property accessors

	public String getTrrigerId() {
		return this.trrigerId;
	}

	public void setTrrigerId(String trrigerId) {
		this.trrigerId = trrigerId;
	}

	public String getTrrigerName() {
		return this.trrigerName;
	}

	public void setTrrigerName(String trrigerName) {
		this.trrigerName = trrigerName;
	}

}