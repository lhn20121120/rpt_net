package com.fitech.model.etl.model.vo;

import java.util.Date;

import com.fitech.model.etl.model.pojo.EtlTaskProcStatusId;

public class EtlTaskProcStatusVo {
	// Fields

	private EtlTaskProcStatusId id=new EtlTaskProcStatusId();
	private Integer procStatus;
	private Integer problemFlag;
	private String problemSource;
	private String problemInfo;
	private Date problemTime;

	// Constructors

	/** default constructor */
	public EtlTaskProcStatusVo() {
	}

	/** minimal constructor */
	public EtlTaskProcStatusVo(EtlTaskProcStatusId id) {
		this.id = id;
	}

	/** full constructor */
	public EtlTaskProcStatusVo(EtlTaskProcStatusId id, Integer procStatus, Integer problemFlag, String problemSource, String problemInfo,
			Date problemTime) {
		this.id = id;
		this.procStatus = procStatus;
		this.problemFlag = problemFlag;
		this.problemSource = problemSource;
		this.problemInfo = problemInfo;
		this.problemTime = problemTime;
	}

	// Property accessors
	/**主键Id*/
	public EtlTaskProcStatusId getId() {
		return this.id;
	}
	
	public void setId(EtlTaskProcStatusId id) {
		this.id = id;
	}
	
	/**流程状态
	 * 1：等待
	 * 2：正在执行
	 * 3：结束
	 * 4：中断*/
	public Integer getProcStatus() {
		return this.procStatus;
	}

	public void setProcStatus(Integer procStatus) {
		this.procStatus = procStatus;
	}
	
	/**流程标记位
	 * 0:代表无异常
	 * 1：代表异常
	 * 2：代表警告*/
	public Integer getProblemFlag() {
		return this.problemFlag;
	}

	public void setProblemFlag(Integer problemFlag) {
		this.problemFlag = problemFlag;
	}
	
	/**错误原因*/
	public String getProblemSource() {
		return this.problemSource;
	}

	public void setProblemSource(String problemSource) {
		this.problemSource = problemSource;
	}
	
	/**错误问题描述*/
	public String getProblemInfo() {
		return this.problemInfo;
	}

	public void setProblemInfo(String problemInfo) {
		this.problemInfo = problemInfo;
	}
	
	/**错误出现时间*/
	public Date getProblemTime() {
		return this.problemTime;
	}

	public void setProblemTime(Date problemTime) {
		this.problemTime = problemTime;
	}

}
