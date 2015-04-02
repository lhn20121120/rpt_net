package com.fitech.model.worktask.model.pojo;

/**
 * ViewWorktaskTemplateGroupId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorktaskTemplateGroupId implements java.io.Serializable {

	// Fields

	private String busiLineId;
	private String busiLineName;
	private String templateGroupId;
	private String templateGroupName;

	// Constructors

	/** default constructor */
	public ViewWorktaskTemplateGroupId() {
	}

	/** full constructor */
	public ViewWorktaskTemplateGroupId(String busiLineId, String busiLineName,
			String templateGroupId, String templateGroupName) {
		this.busiLineId = busiLineId;
		this.busiLineName = busiLineName;
		this.templateGroupId = templateGroupId;
		this.templateGroupName = templateGroupName;
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

	public String getTemplateGroupId() {
		return this.templateGroupId;
	}

	public void setTemplateGroupId(String templateGroupId) {
		this.templateGroupId = templateGroupId;
	}

	public String getTemplateGroupName() {
		return this.templateGroupName;
	}

	public void setTemplateGroupName(String templateGroupName) {
		this.templateGroupName = templateGroupName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewWorktaskTemplateGroupId))
			return false;
		ViewWorktaskTemplateGroupId castOther = (ViewWorktaskTemplateGroupId) other;

		return ((this.getBusiLineId() == castOther.getBusiLineId()) || (this
				.getBusiLineId() != null
				&& castOther.getBusiLineId() != null && this.getBusiLineId()
				.equals(castOther.getBusiLineId())))
				&& ((this.getBusiLineName() == castOther.getBusiLineName()) || (this
						.getBusiLineName() != null
						&& castOther.getBusiLineName() != null && this
						.getBusiLineName().equals(castOther.getBusiLineName())))
				&& ((this.getTemplateGroupId() == castOther
						.getTemplateGroupId()) || (this.getTemplateGroupId() != null
						&& castOther.getTemplateGroupId() != null && this
						.getTemplateGroupId().equals(
								castOther.getTemplateGroupId())))
				&& ((this.getTemplateGroupName() == castOther
						.getTemplateGroupName()) || (this
						.getTemplateGroupName() != null
						&& castOther.getTemplateGroupName() != null && this
						.getTemplateGroupName().equals(
								castOther.getTemplateGroupName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getBusiLineId() == null ? 0 : this.getBusiLineId()
						.hashCode());
		result = 37
				* result
				+ (getBusiLineName() == null ? 0 : this.getBusiLineName()
						.hashCode());
		result = 37
				* result
				+ (getTemplateGroupId() == null ? 0 : this.getTemplateGroupId()
						.hashCode());
		result = 37
				* result
				+ (getTemplateGroupName() == null ? 0 : this
						.getTemplateGroupName().hashCode());
		return result;
	}

}