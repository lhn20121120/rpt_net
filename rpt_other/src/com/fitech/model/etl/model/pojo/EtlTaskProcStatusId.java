package com.fitech.model.etl.model.pojo;

/**
 * EtlTaskProcStatusId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskProcStatusId implements java.io.Serializable {

	// Fields

	private Integer taskMoniId;
	private Integer procId;

	// Constructors

	/** default constructor */
	public EtlTaskProcStatusId() {
	}

	/** full constructor */
	public EtlTaskProcStatusId(Integer taskMoniId, Integer procId) {
		this.taskMoniId = taskMoniId;
		this.procId = procId;
	}

	// Property accessors
	
	/**关联到ETL_TASK_MONI表的主键ID*/
	public Integer getTaskMoniId() {
		return this.taskMoniId;
	}

	public void setTaskMoniId(Integer taskMoniId) {
		this.taskMoniId = taskMoniId;
	}
	
	/**流程状态
	 * 1：代表catch阶段
	 * 2：代表import阶段
	 * 3：代表convert阶段*/
	public Integer getProcId() {
		return this.procId;
	}

	public void setProcId(Integer procId) {
		this.procId = procId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EtlTaskProcStatusId))
			return false;
		EtlTaskProcStatusId castOther = (EtlTaskProcStatusId) other;

		return ((this.getTaskMoniId() == castOther.getTaskMoniId()) || (this.getTaskMoniId() != null && castOther.getTaskMoniId() != null && this
				.getTaskMoniId().equals(castOther.getTaskMoniId())))
				&& ((this.getProcId() == castOther.getProcId()) || (this.getProcId() != null && castOther.getProcId() != null && this.getProcId()
						.equals(castOther.getProcId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTaskMoniId() == null ? 0 : this.getTaskMoniId().hashCode());
		result = 37 * result + (getProcId() == null ? 0 : this.getProcId().hashCode());
		return result;
	}

}