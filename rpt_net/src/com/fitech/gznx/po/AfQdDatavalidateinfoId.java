package com.fitech.gznx.po;

/**
 * AfQdDatavalidateinfoId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfQdDatavalidateinfoId implements java.io.Serializable {

	// Fields

	private Long repId;
	private Long validateTypeId;
	private Long formulaId;

	// Constructors

	/** default constructor */
	public AfQdDatavalidateinfoId() {
	}

	/** full constructor */
	public AfQdDatavalidateinfoId(Long repId, Long validateTypeId,
			Long formulaId) {
		this.repId = repId;
		this.validateTypeId = validateTypeId;
		this.formulaId = formulaId;
	}

	// Property accessors

	public Long getRepId() {
		return this.repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
	}

	public Long getValidateTypeId() {
		return this.validateTypeId;
	}

	public void setValidateTypeId(Long validateTypeId) {
		this.validateTypeId = validateTypeId;
	}

	public Long getFormulaId() {
		return this.formulaId;
	}

	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AfQdDatavalidateinfoId))
			return false;
		AfQdDatavalidateinfoId castOther = (AfQdDatavalidateinfoId) other;

		return ((this.getRepId() == castOther.getRepId()) || (this.getRepId() != null
				&& castOther.getRepId() != null && this.getRepId().equals(
				castOther.getRepId())))
				&& ((this.getValidateTypeId() == castOther.getValidateTypeId()) || (this
						.getValidateTypeId() != null
						&& castOther.getValidateTypeId() != null && this
						.getValidateTypeId().equals(
								castOther.getValidateTypeId())))
				&& ((this.getFormulaId() == castOther.getFormulaId()) || (this
						.getFormulaId() != null
						&& castOther.getFormulaId() != null && this
						.getFormulaId().equals(castOther.getFormulaId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRepId() == null ? 0 : this.getRepId().hashCode());
		result = 37
				* result
				+ (getValidateTypeId() == null ? 0 : this.getValidateTypeId()
						.hashCode());
		result = 37 * result
				+ (getFormulaId() == null ? 0 : this.getFormulaId().hashCode());
		return result;
	}

}