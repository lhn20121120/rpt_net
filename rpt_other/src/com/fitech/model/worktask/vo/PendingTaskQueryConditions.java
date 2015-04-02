package com.fitech.model.worktask.vo;

import java.io.Serializable;

/**
 * 待办任务查询条件业务对象
 * @author Administrator
 *
 */
public class PendingTaskQueryConditions extends WorkTaskBaseVo implements Serializable{
	private String taskName;
	private String freqId;
	private String orgId;
	private Integer roleId;
	private String taskTerm;
	private String returnDesc; //退回原因
	private String busiLine;
	private Long taskMoniId;
	private Integer nodeFlag;
	private Integer taskId;
	private String orgName;
	private boolean multiTaskFlag;//多任务标志
	private String roleIds;//用户角色
	private String likeOrgName;
	private String condTypeId;

	public String getCondTypeId() {
		return condTypeId;
	}
	public void setCondTypeId(String condTypeId) {
		this.condTypeId = condTypeId;
	}
	public String getLikeOrgName() {
		return likeOrgName;
	}
	public void setLikeOrgName(String likeOrgName) {
		this.likeOrgName = likeOrgName;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public boolean isMultiTaskFlag() {
		return multiTaskFlag;
	}
	public void setMultiTaskFlag(boolean multiTaskFlag) {
		this.multiTaskFlag = multiTaskFlag;
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
	public String getBusiLine() {
		return busiLine;
	}
	public void setBusiLine(String busiLine) {
		this.busiLine = busiLine;
	}
	public Integer getNodeFlag() {
		return nodeFlag;
	}
	public void setNodeFlag(Integer nodeFlag) {
		this.nodeFlag = nodeFlag;
	}
	public Long getTaskMoniId() {
		return taskMoniId;
	}
	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}
	public String getReturnDesc() {
		return returnDesc;
	}
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getTaskTerm() {
		return taskTerm;
	}
	public void setTaskTerm(String taskTerm) {
		this.taskTerm = taskTerm;
	}
}
