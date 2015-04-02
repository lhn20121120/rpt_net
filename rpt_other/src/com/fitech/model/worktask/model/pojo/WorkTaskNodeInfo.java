package com.fitech.model.worktask.model.pojo;

/**
 * WorkTaskNodeInfo entity. @author MyEclipse Persistence Tools
 */

public class WorkTaskNodeInfo implements java.io.Serializable {

	// Fields

	private Integer nodeId;
	private String nodeName;
	private Integer taskId;
	private String condTypeId;
	private String relationTaskId;
	private Integer nodeTime;
	private Integer preNodeId;

	// Constructors

	/** default constructor */
	public WorkTaskNodeInfo() {
	}

	/** full constructor */
	public WorkTaskNodeInfo(String nodeName, Integer taskId, String condTypeId,
			String relationTaskId, Integer nodeTime, Integer preNodeId) {
		this.nodeName = nodeName;
		this.taskId = taskId;
		this.condTypeId = condTypeId;
		this.relationTaskId = relationTaskId;
		this.nodeTime = nodeTime;
		this.preNodeId = preNodeId;
	}

	// Property accessors

	public Integer getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getCondTypeId() {
		return this.condTypeId;
	}

	public void setCondTypeId(String condTypeId) {
		this.condTypeId = condTypeId;
	}

	public String getRelationTaskId() {
		return this.relationTaskId;
	}

	public void setRelationTaskId(String relationTaskId) {
		this.relationTaskId = relationTaskId;
	}

	public Integer getNodeTime() {
		return this.nodeTime;
	}

	public void setNodeTime(Integer nodeTime) {
		this.nodeTime = nodeTime;
	}

	public Integer getPreNodeId() {
		return this.preNodeId;
	}

	public void setPreNodeId(Integer preNodeId) {
		this.preNodeId = preNodeId;
	}

}