package com.fitech.gznx.po;

/**
 * AfPbocreportdataId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPbocreportdataId implements java.io.Serializable {

	// Fields

	private Long repId;
	private Long cellId;

	// Constructors

	/** default constructor */
	public AfPbocreportdataId() {
	}

	/** full constructor */
	public AfPbocreportdataId(Long repId, Long cellId) {
		this.repId = repId;
		this.cellId = cellId;
	}

	// Property accessors

	public Long getRepId() {
		return this.repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
	}

	public Long getCellId() {
		return this.cellId;
	}

	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfPbocreportdataId))
			return false;
		AfPbocreportdataId castOther = (AfPbocreportdataId) other;

		return ((this.getRepId() == castOther.getRepId()) || (this.getRepId() != null
				&& castOther.getRepId() != null && this.getRepId().equals(
				castOther.getRepId())))
				&& ((this.getCellId() == castOther.getCellId()) || (this
						.getCellId() != null
						&& castOther.getCellId() != null && this.getCellId()
						.equals(castOther.getCellId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRepId() == null ? 0 : this.getRepId().hashCode());
		result = 37 * result
				+ (getCellId() == null ? 0 : this.getCellId().hashCode());
		return result;
	}

}