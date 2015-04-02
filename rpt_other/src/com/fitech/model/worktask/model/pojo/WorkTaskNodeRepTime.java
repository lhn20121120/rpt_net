package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeRepTime entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeRepTime implements java.io.Serializable {

	// Fields

	private WorkTaskNodeRepTimeId id;
	private Integer repTimeLimit;

	// Constructors

	/** default constructor */
	public WorkTaskNodeRepTime() {
	}

	/** minimal constructor */
	public WorkTaskNodeRepTime(WorkTaskNodeRepTimeId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "WorkTaskNodeRepTime [id=" + id + ", repTimeLimit="
				+ repTimeLimit + "]";
	}

	/** full constructor */
	public WorkTaskNodeRepTime(WorkTaskNodeRepTimeId id, Integer repTimeLimit) {
		this.id = id;
		this.repTimeLimit = repTimeLimit;
	}

	// Property accessors

	public WorkTaskNodeRepTimeId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeRepTimeId id) {
		this.id = id;
	}

	public Integer getRepTimeLimit() {
		return this.repTimeLimit;
	}

	public void setRepTimeLimit(Integer repTimeLimit) {
		this.repTimeLimit = repTimeLimit;
	}

}