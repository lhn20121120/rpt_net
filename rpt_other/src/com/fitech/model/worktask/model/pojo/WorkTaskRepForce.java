package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskRepForce entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskRepForce implements java.io.Serializable {

	// Fields

	private WorkTaskRepForceId id;
	private Long repInId;

	// Constructors

	/** default constructor */
	public WorkTaskRepForce() {
	}

	/** minimal constructor */
	public WorkTaskRepForce(WorkTaskRepForceId id) {
		this.id = id;
	}

	/** full constructor */
	public WorkTaskRepForce(WorkTaskRepForceId id, Long repInId) {
		this.id = id;
		this.repInId = repInId;
	}

	// Property accessors

	public WorkTaskRepForceId getId() {
		return this.id;
	}

	public void setId(WorkTaskRepForceId id) {
		this.id = id;
	}

	public Long getRepInId() {
		return this.repInId;
	}

	public void setRepInId(Long repInId) {
		this.repInId = repInId;
	}

}