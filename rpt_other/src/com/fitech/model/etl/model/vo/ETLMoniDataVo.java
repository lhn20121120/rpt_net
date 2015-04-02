package com.fitech.model.etl.model.vo;

import java.util.ArrayList;
import java.util.List;

/***
 * 任务监控数据监控页面实体类
 * @author admin
 *
 */
public class ETLMoniDataVo {
	private Integer taskId;
	private String freqId;
	private String taskName;//任务名称
	private String freqInfo;//任务频度
	private String taskTerm;//数据期数
	private String startDate;//开始时间
	private String endDate;//结束时间
	private Integer taskMoniId;//任务监控id
	private Integer warngingExecTime;//超时时间
	private Integer execFlag;
    private List<EtlTaskProcStatusVo> statusVoList=new ArrayList<EtlTaskProcStatusVo>();	
	
    
    
    public Integer getWarngingExecTime() {
		return warngingExecTime;
	}
	public void setWarngingExecTime(Integer warngingExecTime) {
		this.warngingExecTime = warngingExecTime;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	/**任务对应的流程状态*/
	public List<EtlTaskProcStatusVo> getStatusVoList() {
		return statusVoList;
	}
	public void setStatusVoList(List<EtlTaskProcStatusVo> statusVoList) {
		this.statusVoList = statusVoList;
	}
	/**任务名称*/
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/**任务频度*/
	public String getFreqInfo() {
		return freqInfo;
	}
	public void setFreqInfo(String freqInfo) {
		this.freqInfo = freqInfo;
	}
	
	/**任务期数*/
	public String getTaskTerm() {
		return taskTerm;
	}
	public void setTaskTerm(String taskTerm) {
		this.taskTerm = taskTerm;
	}
	
	/**开始时间*/
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	/**结束时间*/
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**任务监控id*/
	public Integer getTaskMoniId() {
		return taskMoniId;
	}
	public void setTaskMoniId(Integer taskMoniId) {
		this.taskMoniId = taskMoniId;
	}
	/**任务频度ID*/
	public String getFreqId() {
		return freqId;
	}
	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	/***
	 * 任务是否取消
	 * 0:取消状态
	 * 1:可执行状态
	 * @return
	 */
	public Integer getExecFlag() {
		return execFlag;
	}
	public void setExecFlag(Integer execFlag) {
		this.execFlag = execFlag;
	}
	
	
}
