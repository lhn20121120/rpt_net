package com.fitech.model.etl.model.pojo;

/**
 * EtlLoadFileDetailId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlLoadFileDetailId implements java.io.Serializable {

	// Fields

	private Integer fileId;
	private Integer fileColumnId;

	// Constructors

	/** default constructor */
	public EtlLoadFileDetailId() {
	}

	/** full constructor */
	public EtlLoadFileDetailId(Integer fileId, Integer fileColumnId) {
		this.fileId = fileId;
		this.fileColumnId = fileColumnId;
	}

	// Property accessors

	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	

	public Integer getFileColumnId() {
		return fileColumnId;
	}

	public void setFileColumnId(Integer fileColumnId) {
		this.fileColumnId = fileColumnId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EtlLoadFileDetailId))
			return false;
		EtlLoadFileDetailId castOther = (EtlLoadFileDetailId) other;

		return ((this.getFileId() == castOther.getFileId()) || (this.getFileId() != null && castOther.getFileId() != null && this.getFileId()
				.equals(castOther.getFileId())))
				&& ((this.getFileColumnId() == castOther.getFileColumnId()) || (this.getFileColumnId() != null
						&& castOther.getFileColumnId() != null && this.getFileColumnId().equals(castOther.getFileColumnId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getFileId() == null ? 0 : this.getFileId().hashCode());
		result = 37 * result + (getFileColumnId() == null ? 0 : this.getFileColumnId().hashCode());
		return result;
	}

}