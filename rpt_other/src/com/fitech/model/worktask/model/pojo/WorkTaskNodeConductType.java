package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeConductType entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeConductType implements java.io.Serializable {

	// Fields

	private WorkTaskNodeConductTypeId id;
	private String nodeUrl;
	private String formName;

	// Constructors

	/** default constructor */
	public WorkTaskNodeConductType() {
	}

	/** minimal constructor */
	public WorkTaskNodeConductType(WorkTaskNodeConductTypeId id) {
		this.id = id;
	}

	/** full constructor */
	public WorkTaskNodeConductType(WorkTaskNodeConductTypeId id,
			String nodeUrl, String formName) {
		this.id = id;
		this.nodeUrl = nodeUrl;
		this.formName = formName;
	}

	// Property accessors

	public WorkTaskNodeConductTypeId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeConductTypeId id) {
		this.id = id;
	}

	public String getNodeUrl() {
		return this.nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

}