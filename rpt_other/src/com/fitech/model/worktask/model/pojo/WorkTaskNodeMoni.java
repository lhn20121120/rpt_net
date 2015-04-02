package com.fitech.model.worktask.model.pojo;

import java.sql.Timestamp;

import java.util.Date;

/**
 * WorkTaskNodeMoni entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeMoni implements java.io.Serializable {

	// Fields

	private WorkTaskNodeMoniId id;
	private String busiLine;
	private Integer roleId;
	private Integer preTaskId;
	private Integer lateRerepDate;
	private Integer nodeFlag;
	private Integer preNodeId;
	private Integer finalExecFlag;
	private String returnDesc;
	private Timestamp startDate;
	private Timestamp endDate;
	private Date lateRepDate;
	private Integer rerepFlag;
	private Integer rerepTime;
	private Integer lateRepFlag;
	private Integer leavRepFlag;

	// Constructors

	/** default constructor */
	public WorkTaskNodeMoni() {
	}

	/** minimal constructor */
	public WorkTaskNodeMoni(WorkTaskNodeMoniId id) {
		this.id = id;
	}

	/** full constructor */
	public WorkTaskNodeMoni(WorkTaskNodeMoniId id, String busiLine,
			Integer roleId, Integer preTaskId, Integer lateRerepDate,
			Integer nodeFlag, Integer preNodeId, Integer finalExecFlag,
			String returnDesc, Timestamp startDate, Timestamp endDate,
			Date lateRepDate, Integer rerepFlag, Integer rerepTime,
			Integer lateRepFlag, Integer leavRepFlag) {
		this.id = id;
		this.busiLine = busiLine;
		this.roleId = roleId;
		this.preTaskId = preTaskId;
		this.lateRerepDate = lateRerepDate;
		this.nodeFlag = nodeFlag;
		this.preNodeId = preNodeId;
		this.finalExecFlag = finalExecFlag;
		this.returnDesc = returnDesc;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lateRepDate = lateRepDate;
		this.rerepFlag = rerepFlag;
		this.rerepTime = rerepTime;
		this.lateRepFlag = lateRepFlag;
		this.leavRepFlag = leavRepFlag;
	}

	// Property accessors

	public WorkTaskNodeMoniId getId() {
		return this.id;
	}

	public void setId(WorkTaskNodeMoniId id) {
		this.id = id;
	}

	public String getBusiLine() {
		return this.busiLine;
	}

	public void setBusiLine(String busiLine) {
		this.busiLine = busiLine;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPreTaskId() {
		return this.preTaskId;
	}

	public void setPreTaskId(Integer preTaskId) {
		this.preTaskId = preTaskId;
	}

	public Integer getLateRerepDate() {
		return this.lateRerepDate;
	}

	public void setLateRerepDate(Integer lateRerepDate) {
		this.lateRerepDate = lateRerepDate;
	}

	public Integer getNodeFlag() {
		return this.nodeFlag;
	}

	public void setNodeFlag(Integer nodeFlag) {
		this.nodeFlag = nodeFlag;
	}

	public Integer getPreNodeId() {
		return this.preNodeId;
	}

	public void setPreNodeId(Integer preNodeId) {
		this.preNodeId = preNodeId;
	}

	public Integer getFinalExecFlag() {
		return this.finalExecFlag;
	}

	public void setFinalExecFlag(Integer finalExecFlag) {
		this.finalExecFlag = finalExecFlag;
	}

	public String getReturnDesc() {
		return this.returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Date getLateRepDate() {
		return this.lateRepDate;
	}

	public void setLateRepDate(Date lateRepDate) {
		this.lateRepDate = lateRepDate;
	}

	public Integer getRerepFlag() {
		return this.rerepFlag;
	}

	public void setRerepFlag(Integer rerepFlag) {
		this.rerepFlag = rerepFlag;
	}

	public Integer getRerepTime() {
		return this.rerepTime;
	}

	public void setRerepTime(Integer rerepTime) {
		this.rerepTime = rerepTime;
	}

	public Integer getLateRepFlag() {
		return this.lateRepFlag;
	}

	public void setLateRepFlag(Integer lateRepFlag) {
		this.lateRepFlag = lateRepFlag;
	}

	public Integer getLeavRepFlag() {
		return this.leavRepFlag;
	}

	public void setLeavRepFlag(Integer leavRepFlag) {
		this.leavRepFlag = leavRepFlag;
	}

}