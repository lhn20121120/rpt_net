package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskBusiLine entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskBusiLine implements java.io.Serializable {

	// Fields

	private String busiLineId;
	private String busiLineName;

	// Constructors

	/** default constructor */
	public WorkTaskBusiLine() {
	}

	/** minimal constructor */
	public WorkTaskBusiLine(String busiLineId) {
		this.busiLineId = busiLineId;
	}

	/** full constructor */
	public WorkTaskBusiLine(String busiLineId, String busiLineName) {
		this.busiLineId = busiLineId;
		this.busiLineName = busiLineName;
	}

	// Property accessors

	public String getBusiLineId() {
		return this.busiLineId;
	}

	public void setBusiLineId(String busiLineId) {
		this.busiLineId = busiLineId;
	}

	public String getBusiLineName() {
		return this.busiLineName;
	}

	public void setBusiLineName(String busiLineName) {
		this.busiLineName = busiLineName;
	}

}