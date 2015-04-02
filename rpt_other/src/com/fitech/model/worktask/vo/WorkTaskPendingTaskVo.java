package com.fitech.model.worktask.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 待办任务，业务对象
 * @author Administrator
 *
 */
public class WorkTaskPendingTaskVo extends WorkTaskBaseVo implements Serializable {
	private String orgName; //机构名称
	private Integer taskId; //任务id
	private Long taskMoniId;
	private String taskName; //任务名称
	private String freqId;//任务频度
	private int freqIds;
	private Date taskTerm;//任务期数
	private String taskNodeName;//任务阶段名称
	private String taskState; //任务状态
	private String curState;//当前状态 add by wmm
	private Integer curStateId;//当前状态 add by wmm
	private Integer nodeId; //节点id
	private String orgId;//机构id
	private String busiLine;//业务条线
	private String workTypeId;//任务处理类型
	private Integer roleId;//角色id
	private Date lateRepDate; //最迟报送日期
	private Integer performNumber;//执行次数
	
	private String lateRepFlagName;  //迟报状态名称
	private Integer lateRepFlag;  //迟报状态
	private Integer nodeFlag;  //节点状态
	
	private String returnDesc; //退回信息
	private String templateIds; //模板id
	
	private Date endDate;//阶段任务结束时间
	
	private Date lastNodeDate;//上一个节点完成时间
	public Date getLastNodeDate() {
		return lastNodeDate;
	}
	public void setLastNodeDate(Date lastNodeDate) {
		this.lastNodeDate = lastNodeDate;
	}
	public int getFreqIds() {
		return freqIds;
	}
	public void setFreqIds(int freqIds) {
		this.freqIds = freqIds;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	private int year;//年
	private int term;//月
	private int day;//日
	
	private String condTypeId; //节点处理类型
	
	public String getCondTypeId() {
		return condTypeId;
	}
	public void setCondTypeId(String condTypeId) {
		this.condTypeId = condTypeId;
	}
	public Integer getCurStateId() {
		return curStateId;
	}
	public void setCurStateId(Integer curStateId) {
		this.curStateId = curStateId;
	}
	public String getCurState() {
		return curState;
	}
	public void setCurState(String curState) {
		this.curState = curState;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}

	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getTemplateIds() {
		return templateIds;
	}
	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}
	public String getReturnDesc() {
		return returnDesc;
	}
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}
	public Integer getNodeFlag() {
		return nodeFlag;
	}
	public void setNodeFlag(Integer nodeFlag) {
		this.nodeFlag = nodeFlag;
	}
	public String getLateRepFlagName() {
		return lateRepFlagName;
	}
	public void setLateRepFlagName(String lateRepFlagName) {
		this.lateRepFlagName = lateRepFlagName;
	}
	public Integer getLateRepFlag() {
		return lateRepFlag;
	}
	public void setLateRepFlag(Integer lateRepFlag) {
		this.lateRepFlag = lateRepFlag;
	}
	public String getBusiLine() {
		return busiLine;
	}
	public void setBusiLine(String busiLine) {
		this.busiLine = busiLine;
	}
	public Date getLateRepDate() {
		return lateRepDate;
	}
	public Integer getPerformNumber() {
		return performNumber;
	}
	public void setPerformNumber(Integer performNumber) {
		this.performNumber = performNumber;
	}
	public void setLateRepDate(Date lateRepDate) {
		this.lateRepDate = lateRepDate;
	}
	public Long getTaskMoniId() {
		return taskMoniId;
	}
	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getWorkTypeId() {
		return workTypeId;
	}
	public void setWorkTypeId(String workTypeId) {
		this.workTypeId = workTypeId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getFreqId() {
		return freqId;
	}
	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	public Date getTaskTerm() {
		return taskTerm;
	}
	public void setTaskTerm(Date taskTerm) {
		this.taskTerm = taskTerm;
	}
	public String getTaskNodeName() {
		return taskNodeName;
	}
	public void setTaskNodeName(String taskNodeName) {
		this.taskNodeName = taskNodeName;
	}
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
}
