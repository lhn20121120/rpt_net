package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeObjectMoniId entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeObjectMoniId implements java.io.Serializable {

	// Fields

	private Long taskMoniId;
	private Integer nodeId;
	private String templateId;
	private String orgId;
	private Integer nodeIoFlag;

	// Constructors

	/** default constructor */
	public WorkTaskNodeObjectMoniId() {
	}

	/** full constructor */
	public WorkTaskNodeObjectMoniId(Long taskMoniId, Integer nodeId,
			String templateId, String orgId, Integer nodeIoFlag) {
		this.taskMoniId = taskMoniId;
		this.nodeId = nodeId;
		this.templateId = templateId;
		this.orgId = orgId;
		this.nodeIoFlag = nodeIoFlag;
	}

	// Property accessors

	public Long getTaskMoniId() {
		return this.taskMoniId;
	}

	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getNodeIoFlag() {
		return this.nodeIoFlag;
	}

	public void setNodeIoFlag(Integer nodeIoFlag) {
		this.nodeIoFlag = nodeIoFlag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WorkTaskNodeObjectMoniId))
			return false;
		WorkTaskNodeObjectMoniId castOther = (WorkTaskNodeObjectMoniId) other;

		return ((this.getTaskMoniId() == castOther.getTaskMoniId()) || (this
				.getTaskMoniId() != null
				&& castOther.getTaskMoniId() != null && this.getTaskMoniId()
				.equals(castOther.getTaskMoniId())))
				&& ((this.getNodeId() == castOther.getNodeId()) || (this
						.getNodeId() != null
						&& castOther.getNodeId() != null && this.getNodeId()
						.equals(castOther.getNodeId())))
				&& ((this.getTemplateId() == castOther.getTemplateId()) || (this
						.getTemplateId() != null
						&& castOther.getTemplateId() != null && this
						.getTemplateId().equals(castOther.getTemplateId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())))
				&& ((this.getNodeIoFlag() == castOther.getNodeIoFlag()) || (this
						.getNodeIoFlag() != null
						&& castOther.getNodeIoFlag() != null && this
						.getNodeIoFlag().equals(castOther.getNodeIoFlag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTaskMoniId() == null ? 0 : this.getTaskMoniId()
						.hashCode());
		result = 37 * result
				+ (getNodeId() == null ? 0 : this.getNodeId().hashCode());
		result = 37
				* result
				+ (getTemplateId() == null ? 0 : this.getTemplateId()
						.hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37
				* result
				+ (getNodeIoFlag() == null ? 0 : this.getNodeIoFlag()
						.hashCode());
		return result;
	}

}