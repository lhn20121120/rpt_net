package com.fitech.gznx.po;

/**
 * QdReportInInfoId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class QdReportInInfoId implements java.io.Serializable {

	// Fields

	private Long repInId;
	private Long rowId;
	private Long colId;

	// Constructors

	/** default constructor */
	public QdReportInInfoId() {
	}

	/** full constructor */
	public QdReportInInfoId(Long repInId, Long rowId, Long colId) {
		this.repInId = repInId;
		this.rowId = rowId;
		this.colId = colId;
	}

	// Property accessors

	public Long getRepInId() {
		return this.repInId;
	}

	public void setRepInId(Long repInId) {
		this.repInId = repInId;
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
		if (!(other instanceof QdReportInInfoId))
			return false;
		QdReportInInfoId castOther = (QdReportInInfoId) other;

		return ((this.getRepInId() == castOther.getRepInId()) || (this
				.getRepInId() != null
				&& castOther.getRepInId() != null && this.getRepInId().equals(
				castOther.getRepInId())))
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
				+ (getRepInId() == null ? 0 : this.getRepInId().hashCode());
		result = 37 * result
				+ (getRowId() == null ? 0 : this.getRowId().hashCode());
		result = 37 * result
				+ (getColId() == null ? 0 : this.getColId().hashCode());
		return result;
	}

}