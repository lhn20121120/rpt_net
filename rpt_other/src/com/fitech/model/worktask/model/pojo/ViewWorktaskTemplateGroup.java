package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorktaskTemplateGroup entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskTemplateGroup implements java.io.Serializable {

	// Fields

	private ViewWorktaskTemplateGroupId id;

	// Constructors

	/** default constructor */
	public ViewWorktaskTemplateGroup() {
	}

	/** full constructor */
	public ViewWorktaskTemplateGroup(ViewWorktaskTemplateGroupId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorktaskTemplateGroupId getId() {
		return this.id;
	}

	public void setId(ViewWorktaskTemplateGroupId id) {
		this.id = id;
	}

}