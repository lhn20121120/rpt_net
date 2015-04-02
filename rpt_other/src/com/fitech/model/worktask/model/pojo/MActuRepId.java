package com.fitech.model.worktask.model.pojo;

/**
 * MActuRepId entity. @author MyEclipse Persistence Tools
 */

public class MActuRepId implements java.io.Serializable {

	// Fields

	private Long dataRangeId;
	private String childRepId;
	private String versionId;
	private Long repFreqId;
	private Long oatId;

	// Constructors

	/** default constructor */
	public MActuRepId() {
	}

	/** full constructor */
	public MActuRepId(Long dataRangeId, String childRepId, String versionId,
			Long repFreqId, Long oatId) {
		this.dataRangeId = dataRangeId;
		this.childRepId = childRepId;
		this.versionId = versionId;
		this.repFreqId = repFreqId;
		this.oatId = oatId;
	}

	// Property accessors

	public Long getDataRangeId() {
		return this.dataRangeId;
	}

	public void setDataRangeId(Long dataRangeId) {
		this.dataRangeId = dataRangeId;
	}

	public String getChildRepId() {
		return this.childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Long getRepFreqId() {
		return this.repFreqId;
	}

	public void setRepFreqId(Long repFreqId) {
		this.repFreqId = repFreqId;
	}

	public Long getOatId() {
		return this.oatId;
	}

	public void setOatId(Long oatId) {
		this.oatId = oatId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MActuRepId))
			return false;
		MActuRepId castOther = (MActuRepId) other;

		return ((this.getDataRangeId() == castOther.getDataRangeId()) || (this
				.getDataRangeId() != null
				&& castOther.getDataRangeId() != null && this.getDataRangeId()
				.equals(castOther.getDataRangeId())))
				&& ((this.getChildRepId() == castOther.getChildRepId()) || (this
						.getChildRepId() != null
						&& castOther.getChildRepId() != null && this
						.getChildRepId().equals(castOther.getChildRepId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this
						.getVersionId() != null
						&& castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())))
				&& ((this.getRepFreqId() == castOther.getRepFreqId()) || (this
						.getRepFreqId() != null
						&& castOther.getRepFreqId() != null && this
						.getRepFreqId().equals(castOther.getRepFreqId())))
				&& ((this.getOatId() == castOther.getOatId()) || (this
						.getOatId() != null
						&& castOther.getOatId() != null && this.getOatId()
						.equals(castOther.getOatId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getDataRangeId() == null ? 0 : this.getDataRangeId()
						.hashCode());
		result = 37
				* result
				+ (getChildRepId() == null ? 0 : this.getChildRepId()
						.hashCode());
		result = 37 * result
				+ (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		result = 37 * result
				+ (getRepFreqId() == null ? 0 : this.getRepFreqId().hashCode());
		result = 37 * result
				+ (getOatId() == null ? 0 : this.getOatId().hashCode());
		return result;
	}

}