package com.fitech.model.worktask.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 待办任务，业务对象
 * @author Administrator
 *
 */
public class WorkTaskNodeInfoVo extends WorkTaskBaseVo implements Serializable {
	private Long taskMoniId;
	private String taskNodeName;//任务阶段名称
	private Integer roleId;//角色id
	
	private Integer nodeId; //节点id
	
	private Integer nodeFlag;  //节点状态

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Long getTaskMoniId() {
		return taskMoniId;
	}

	public void setTaskMoniId(Long taskMoniId) {
		this.taskMoniId = taskMoniId;
	}

	public String getTaskNodeName() {
		return taskNodeName;
	}

	public void setTaskNodeName(String taskNodeName) {
		this.taskNodeName = taskNodeName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getNodeFlag() {
		return nodeFlag;
	}

	public void setNodeFlag(Integer nodeFlag) {
		this.nodeFlag = nodeFlag;
	}
	
}
