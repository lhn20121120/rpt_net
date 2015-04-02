package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeConductTypeId entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeConductTypeId implements java.io.Serializable {

	// Fields

	private String busiLineId;
	private String condTypeId;

	// Constructors

	/** default constructor */
	public WorkTaskNodeConductTypeId() {
	}

	/** full constructor */
	public WorkTaskNodeConductTypeId(String busiLineId, String condTypeId) {
		this.busiLineId = busiLineId;
		this.condTypeId = condTypeId;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WorkTaskNodeConductTypeId))
			return false;
		WorkTaskNodeConductTypeId castOther = (WorkTaskNodeConductTypeId) other;

		return ((this.getBusiLineId() == castOther.getBusiLineId()) || (this
				.getBusiLineId() != null
				&& castOther.getBusiLineId() != null && this.getBusiLineId()
				.equals(castOther.getBusiLineId())))
				&& ((this.getCondTypeId() == castOther.getCondTypeId()) || (this
						.getCondTypeId() != null
						&& castOther.getCondTypeId() != null && this
						.getCondTypeId().equals(castOther.getCondTypeId())));
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
		return result;
	}

}