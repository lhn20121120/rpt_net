package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorktaskTemplate entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskTemplate implements java.io.Serializable {

	// Fields

	private ViewWorktaskTemplateId id;

	// Constructors

	/** default constructor */
	public ViewWorktaskTemplate() {
	}

	/** full constructor */
	public ViewWorktaskTemplate(ViewWorktaskTemplateId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorktaskTemplateId getId() {
		return this.id;
	}

	public void setId(ViewWorktaskTemplateId id) {
		this.id = id;
	}

}