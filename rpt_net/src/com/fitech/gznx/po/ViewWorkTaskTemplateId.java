package com.fitech.gznx.po;

/**
 * ViewWorkTaskTemplateId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskTemplateId implements java.io.Serializable {

	// Fields

	private Integer taskId;
	private String templateId;
	private String orgId;

	// Constructors

	/** default constructor */
	public ViewWorkTaskTemplateId() {
	}

	/** full constructor */
	public ViewWorkTaskTemplateId(Integer taskId, String templateId,
			String orgId) {
		this.taskId = taskId;
		this.templateId = templateId;
		this.orgId = orgId;
	}

	// Property accessors

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewWorkTaskTemplateId))
			return false;
		ViewWorkTaskTemplateId castOther = (ViewWorkTaskTemplateId) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this
				.getTaskId() != null
				&& castOther.getTaskId() != null && this.getTaskId().equals(
				castOther.getTaskId())))
				&& ((this.getTemplateId() == castOther.getTemplateId()) || (this
						.getTemplateId() != null
						&& castOther.getTemplateId() != null && this
						.getTemplateId().equals(castOther.getTemplateId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37
				* result
				+ (getTemplateId() == null ? 0 : this.getTemplateId()
						.hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		return result;
	}

}