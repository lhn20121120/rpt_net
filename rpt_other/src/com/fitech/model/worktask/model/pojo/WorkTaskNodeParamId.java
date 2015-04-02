package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeParamId entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeParamId implements java.io.Serializable {

	// Fields

	private String busiLineId;
	private String condTypeId;
	private String paramName;

	// Constructors

	/** default constructor */
	public WorkTaskNodeParamId() {
	}

	/** full constructor */
	public WorkTaskNodeParamId(String busiLineId, String condTypeId,
			String paramName) {
		this.busiLineId = busiLineId;
		this.condTypeId = condTypeId;
		this.paramName = paramName;
	}

	// Property accessors

	public String getBusiLineId() {
		return this.busiLineId;
	}

	public void setBusiLineId(String busiLineId) {
		this.busiLineId = busiLineId;
	}

	public String getCondTypeId() {
		return this.condTypeId;
	}

	public void setCondTypeId(String condTypeId) {
		this.condTypeId = condTypeId;
	}

	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WorkTaskNodeParamId))
			return false;
		WorkTaskNodeParamId castOther = (WorkTaskNodeParamId) other;

		return ((this.getBusiLineId() == castOther.getBusiLineId()) || (this
				.getBusiLineId() != null
				&& castOther.getBusiLineId() != null && this.getBusiLineId()
				.equals(castOther.getBusiLineId())))
				&& ((this.getCondTypeId() == castOther.getCondTypeId()) || (this
						.getCondTypeId() != null
						&& castOther.getCondTypeId() != null && this
						.getCondTypeId().equals(castOther.getCondTypeId())))
				&& ((this.getParamName() == castOther.getParamName()) || (this
						.getParamName() != null
						&& castOther.getParamName() != null && this
						.getParamName().equals(castOther.getParamName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getBusiLineId() == null ? 0 : this.getBusiLineId()
						.hashCode());
		result = 37
				* result
				+ (getCondTypeId() == null ? 0 : this.getCondTypeId()
						.hashCode());
		result = 37 * result
				+ (getParamName() == null ? 0 : this.getParamName().hashCode());
		return result;
	}

}