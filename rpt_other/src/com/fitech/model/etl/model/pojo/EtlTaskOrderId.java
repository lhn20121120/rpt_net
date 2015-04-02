package com.fitech.model.etl.model.pojo;

/**
 * EtlTaskOrderId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskOrderId implements java.io.Serializable {

	// Fields

	private Integer taskId;
	private Integer preTaskId;

	// Constructors

	/** default constructor */
	public EtlTaskOrderId() {
	}

	/** full constructor */
	public EtlTaskOrderId(Integer taskId, Integer preTaskId) {
		this.taskId = taskId;
		this.preTaskId = preTaskId;
	}

	// Property accessors

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getPreTaskId() {
		return this.preTaskId;
	}

	public void setPreTaskId(Integer preTaskId) {
		this.preTaskId = preTaskId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EtlTaskOrderId))
			return false;
		EtlTaskOrderId castOther = (EtlTaskOrderId) other;

		return ((this.getTaskId() == castOther.getTaskId()) || (this.getTaskId() != null && castOther.getTaskId() != null && this.getTaskId()
				.equals(castOther.getTaskId())))
				&& ((this.getPreTaskId() == castOther.getPreTaskId()) || (this.getPreTaskId() != null && castOther.getPreTaskId() != null && this
						.getPreTaskId().equals(castOther.getPreTaskId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTaskId() == null ? 0 : this.getTaskId().hashCode());
		result = 37 * result + (getPreTaskId() == null ? 0 : this.getPreTaskId().hashCode());
		return result;
	}

}