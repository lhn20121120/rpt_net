package com.fitech.gznx.po;

/**
 * AfDatavalidateinfoId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfDatavalidateinfoId implements java.io.Serializable {

	// Fields

	private Long repId;
	private Long formulaId;

	// Constructors

	/** default constructor */
	public AfDatavalidateinfoId() {
	}

	/** full constructor */
	public AfDatavalidateinfoId(Long repId, Long formulaId) {
		this.repId = repId;
		this.formulaId = formulaId;
	}

	// Property accessors

	public Long getRepId() {
		return this.repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
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
		if (!(other instanceof AfDatavalidateinfoId))
			return false;
		AfDatavalidateinfoId castOther = (AfDatavalidateinfoId) other;

		return ((this.getRepId() == castOther.getRepId()) || (this.getRepId() != null
				&& castOther.getRepId() != null && this.getRepId().equals(
				castOther.getRepId())))
				&& ((this.getFormulaId() == castOther.getFormulaId()) || (this
						.getFormulaId() != null
						&& castOther.getFormulaId() != null && this
						.getFormulaId().equals(castOther.getFormulaId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRepId() == null ? 0 : this.getRepId().hashCode());
		result = 37 * result
				+ (getFormulaId() == null ? 0 : this.getFormulaId().hashCode());
		return result;
	}

}