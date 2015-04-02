package com.fitech.model.etl.model.pojo;

import java.util.Date;

/**
 * EtlTaskProcStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTaskProcStatus implements java.io.Serializable {

	// Fields

	private EtlTaskProcStatusId id=new EtlTaskProcStatusId();
	private Integer procStatus;
	private Integer problemFlag;
	private String problemSource;
	private String problemInfo;
	private Date problemTime;
	private Integer readedFlag;

	// Constructors

	public EtlTaskProcStatus(EtlTaskProcStatusId id, Integer procStatus, Integer problemFlag, String problemSource, String problemInfo,
			Date problemTime, Integer readedFlag) {
		super();
		this.id = id;
		this.procStatus = procStatus;
		this.problemFlag = problemFlag;
		this.problemSource = problemSource;
		this.problemInfo = problemInfo;
		this.problemTime = problemTime;
		this.readedFlag = readedFlag;
	}
	
	/***/
	public Integer getReadedFlag() {
		return readedFlag;
	}

	public void setReadedFlag(Integer readedFlag) {
		this.readedFlag = readedFlag;
	}

	/** default constructor */
	public EtlTaskProcStatus() {
	}

	/** minimal constructor */
	public EtlTaskProcStatus(EtlTaskProcStatusId id) {
		this.id = id;
	}

	/** full constructor */
	public EtlTaskProcStatus(EtlTaskProcStatusId id, Integer procStatus, Integer problemFlag, String problemSource, String problemInfo,
			Date problemTime) {
		this.id = id;
		this.procStatus = procStatus;
		this.problemFlag = problemFlag;
		this.problemSource = problemSource;
		this.problemInfo = problemInfo;
		this.problemTime = problemTime;
	}

	// Property accessors

	public EtlTaskProcStatusId getId() {
		return this.id;
	}

	public void setId(EtlTaskProcStatusId id) {
		this.id = id;
	}
	
	/***
	 * 流程状态
	 *   1：等待
	 *   2：正在执行
	 *	 3：结束
	 *	 4：中断
	 * @return
	 */
	public Integer getProcStatus() {
		return this.procStatus;
	}

	public void setProcStatus(Integer procStatus) {
		this.procStatus = procStatus;
	}
	
	
	public Integer getProblemFlag() {
		return this.problemFlag;
	}

	public void setProblemFlag(Integer problemFlag) {
		this.problemFlag = problemFlag;
	}

	public String getProblemSource() {
		return this.problemSource;
	}

	public void setProblemSource(String problemSource) {
		this.problemSource = problemSource;
	}

	public String getProblemInfo() {
		return this.problemInfo;
	}

	public void setProblemInfo(String problemInfo) {
		this.problemInfo = problemInfo;
	}

	public Date getProblemTime() {
		return this.problemTime;
	}

	public void setProblemTime(Date problemTime) {
		this.problemTime = problemTime;
	}

}