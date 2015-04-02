package com.fitech.model.etl.model.pojo;

/**
 * EtlTaskFreqId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskFreqId implements java.io.Serializable {

	// Fields

	private Integer taskId;
	private String freqId;

	// Constructors

	/** default constructor */
	public EtlTaskFreqId() {
	}

	/** full constructor */
	public EtlTaskFreqId(Integer taskId, String freqId) {
		this.taskId = taskId;
		this.freqId = freqId;
	}

	// Property accessors

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EtlTaskFreqId))
			return false;
		EtlTaskFreqId castOther = (EtlTaskFreqId) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null && castOther.getTaskId() != null && this.getTaskId()
				.equals(castOther.getTaskId())))
				&& ((this.getFreqId() == castOther.getFreqId()) || (this.getFreqId() != null && castOther.getFreqId() != null && this.getFreqId()
						.equals(castOther.getFreqId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getFreqId() == null ? 0 : this.getFreqId().hashCode());
		return result;
	}

}