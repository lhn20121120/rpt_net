package com.fitech.model.worktask.model.pojo;

/**
 * MActuRep entity. @author MyEclipse Persistence Tools
 */

public class MActuRep implements java.io.Serializable {

	// Fields

	private MActuRepId id;
	private Long delayTime;
	private Long normalTime;

	// Constructors

	/** default constructor */
	public MActuRep() {
	}

	/** minimal constructor */
	public MActuRep(MActuRepId id) {
		this.id = id;
	}

	/** full constructor */
	public MActuRep(MActuRepId id, Long delayTime, Long normalTime) {
		this.id = id;
		this.delayTime = delayTime;
		this.normalTime = normalTime;
	}

	// Property accessors

	public MActuRepId getId() {
		return this.id;
	}

	public void setId(MActuRepId id) {
		this.id = id;
	}

	public Long getDelayTime() {
		return this.delayTime;
	}

	public void setDelayTime(Long delayTime) {
		this.delayTime = delayTime;
	}

	public Long getNormalTime() {
		return this.normalTime;
	}

	public void setNormalTime(Long normalTime) {
		this.normalTime = normalTime;
	}

}