package com.fitech.gznx.po;

/**
 * ViewWorkTaskTemplate entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskTemplate implements java.io.Serializable {

	// Fields

	private ViewWorkTaskTemplateId id;

	// Constructors

	/** default constructor */
	public ViewWorkTaskTemplate() {
	}

	/** full constructor */
	public ViewWorkTaskTemplate(ViewWorkTaskTemplateId id) {
		this.id = id;
	}

	// Property accessors

	public ViewWorkTaskTemplateId getId() {
		return this.id;
	}

	public void setId(ViewWorkTaskTemplateId id) {
		this.id = id;
	}

}