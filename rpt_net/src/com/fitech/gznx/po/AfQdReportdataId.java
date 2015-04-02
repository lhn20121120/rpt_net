package com.fitech.gznx.po;

/**
 * AfQdReportdataId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfQdReportdataId implements java.io.Serializable {

	// Fields

	private Long repId;
	private Long rowId;
	private Long colId;

	// Constructors

	/** default constructor */
	public AfQdReportdataId() {
	}

	/** full constructor */
	public AfQdReportdataId(Long repId, Long rowId, Long colId) {
		this.repId = repId;
		this.rowId = rowId;
		this.colId = colId;
	}

	// Property accessors

	public Long getRepId() {
		return this.repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
	}

	public Long getRowId() {
		return this.rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public Long getColId() {
		return this.colId;
	}

	public void setColId(Long colId) {
		this.colId = colId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfQdReportdataId))
			return false;
		AfQdReportdataId castOther = (AfQdReportdataId) other;

		return ((this.getRepId() == castOther.getRepId()) || (this.getRepId() != null
				&& castOther.getRepId() != null && this.getRepId().equals(
				castOther.getRepId())))
				&& ((this.getRowId() == castOther.getRowId()) || (this
						.getRowId() != null
						&& castOther.getRowId() != null && this.getRowId()
						.equals(castOther.getRowId())))
				&& ((this.getColId() == castOther.getColId()) || (this
						.getColId() != null
						&& castOther.getColId() != null && this.getColId()
						.equals(castOther.getColId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRepId() == null ? 0 : this.getRepId().hashCode());
		result = 37 * result
				+ (getRowId() == null ? 0 : this.getRowId().hashCode());
		result = 37 * result
				+ (getColId() == null ? 0 : this.getColId().hashCode());
		return result;
	}

}