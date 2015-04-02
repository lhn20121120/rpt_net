package com.fitech.model.etl.model.vo;

import java.util.Date;

import org.springframework.context.ApplicationContext;

public class ETLTaskMoniVo {

	// Fields

	private Integer taskMoniId;
	private Integer taskId;
	private String freqId;
	private String taskDate;
	private String taskTerm;
	private Date startDate;
	private Date endDate;
	private Integer overFlag;
	private String objectName;
	private Integer mainTaskFlag;
	private ETLFreqInfoVo etlFreqInfoVo;
	private Integer execFlag;
	
	
	//此字段不做数据库关联 只用于页面执行时间的赋值查询
	private Date executeTime=new Date();
	private String beginSearchTime;
	private String endSearchTime;
	
	private ApplicationContext appContext;
	// Constructors

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	/** default constructor */
	public ETLTaskMoniVo() {
	}

	/** full constructor */
	public ETLTaskMoniVo(Integer taskId, String freqId, String taskDate, String taskTerm, Date startDate, Date endDate, Integer overFlag) {
		this.taskId = taskId;
		this.freqId = freqId;
		this.taskDate = taskDate;
		this.taskTerm = taskTerm;
		this.startDate = startDate;
		this.endDate = endDate;
		this.overFlag = overFlag;
	}
	// Property accessors
	
	/**主键自增*/
	public Integer getTaskMoniId() {
		return this.taskMoniId;
	}

	public void setTaskMoniId(Integer taskMoniId) {
		this.taskMoniId = taskMoniId;
	}

	/**频度id 关联到频度表*/
	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	
	/**任务id 关联到任务表*/
	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	
	/**数据期数*/
	public String getTaskTerm() {
		return this.taskTerm;
	}

	public void setTaskTerm(String taskTerm) {
		this.taskTerm = taskTerm;
	}
	
	/**开始时间*/
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**结束时间*/
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	
	/**结束标志*/
	public Integer getOverFlag() {
		return overFlag;
	}

	public void setOverFlag(Integer overFlag) {
		this.overFlag = overFlag;
	}

	public ETLFreqInfoVo getEtlFreqInfoVo() {
		return etlFreqInfoVo;
	}

	public void setEtlFreqInfoVo(ETLFreqInfoVo etlFreqInfoVo) {
		this.etlFreqInfoVo = etlFreqInfoVo;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Integer getMainTaskFlag() {
		return mainTaskFlag;
	}

	public void setMainTaskFlag(Integer mainTaskFlag) {
		this.mainTaskFlag = mainTaskFlag;
	}

	public String getBeginSearchTime() {
		return beginSearchTime;
	}

	public void setBeginSearchTime(String beginSearchTime) {
		this.beginSearchTime = beginSearchTime;
	}

	public String getEndSearchTime() {
		return endSearchTime;
	}

	public void setEndSearchTime(String endSearchTime) {
		this.endSearchTime = endSearchTime;
	}

	public Integer getExecFlag() {
		return execFlag;
	}

	public void setExecFlag(Integer execFlag) {
		this.execFlag = execFlag;
	}
	
	
}
