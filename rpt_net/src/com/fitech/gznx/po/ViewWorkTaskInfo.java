package com.fitech.gznx.po;

/**
 * ViewWorkTaskInfo entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskInfo implements java.io.Serializable {

	// Fields

	private ViewWorkTaskInfoId id;

	// Constructors

	/** default constructor */
	public ViewWorkTaskInfo() {
	}

	/** full constructor */
	public ViewWorkTaskInfo(ViewWorkTaskInfoId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorkTaskInfoId getId() {
		return this.id;
	}

	public void setId(ViewWorkTaskInfoId id) {
		this.id = id;
	}

}