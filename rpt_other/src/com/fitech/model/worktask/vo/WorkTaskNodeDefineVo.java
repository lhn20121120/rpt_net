package com.fitech.model.worktask.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * add by 王明明
 * @author Administrator
 *
 */
public  class WorkTaskNodeDefineVo extends WorkTaskBaseVo{
	
	private Integer nodeId;//节点id
	private Integer preNodeId;//上一个节点id
	private String nodeName;//节点名称
	private Integer taskId;//任务id
	private String taskName;//任务名称
	private String conductType;//处理类型
	private String conductTypeName;//处理类型名称
	private String relationTaskId;//关联任务
	private String relationTaskName;//关联任务名称
	private Integer nodeTime;//节点时间限定
	private Integer nodeOrderId;//节点排序id
	private String busiLineId;
	private String templateId;//模板id
	private String templateName;//模板名称
	private String orgId;//机构id
	private String orgName;//机构名称
	private Integer nodeIoFlag;//输入输出标志
	private Integer roleId;//角色id
	private String roleName;//角色名称
	private String type;//角色名称
	private String orgIds;//机构
	private String templateIds;//机构
	private Map nodeObject=new HashMap();  //存入节点定义时，机构和模板对应关系  
	
	private String checkOrgId;
	private String savedOrgId;
	
	public String getCheckOrgId() {
		return checkOrgId;
	}
	public void setCheckOrgId(String checkOrgId) {
		this.checkOrgId = checkOrgId;
	}
	public String getSavedOrgId() {
		return savedOrgId;
	}
	public void setSavedOrgId(String savedOrgId) {
		this.savedOrgId = savedOrgId;
	}
	public Map getNodeObject() {
		return nodeObject;
	}
	public void setNodeObject(Map nodeObject) {
		this.nodeObject = nodeObject;
	}
	public String getBusiLineId() {
		return busiLineId;
	}
	public void setBusiLineId(String busiLineId) {
		this.busiLineId = busiLineId;
	}
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	public String getTemplateIds() {
		return templateIds;
	}
	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getPreNodeId() {
		return preNodeId;
	}
	public void setPreNodeId(Integer preNodeId) {
		this.preNodeId = preNodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	
	public String getRelationTaskId() {
		return relationTaskId;
	}
	public void setRelationTaskId(String relationTaskId) {
		this.relationTaskId = relationTaskId;
	}
	
	
	public String getConductType() {
		return conductType;
	}
	public void setConductType(String conductType) {
		this.conductType = conductType;
	}
	public Integer getNodeTime() {
		return nodeTime;
	}
	public void setNodeTime(Integer nodeTime) {
		this.nodeTime = nodeTime;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getNodeOrderId() {
		return nodeOrderId;
	}
	public void setNodeOrderId(Integer nodeOrderId) {
		this.nodeOrderId = nodeOrderId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getConductTypeName() {
		return conductTypeName;
	}
	public void setConductTypeName(String conductTypeName) {
		this.conductTypeName = conductTypeName;
	}
	public String getRelationTaskName() {
		return relationTaskName;
	}
	public void setRelationTaskName(String relationTaskName) {
		this.relationTaskName = relationTaskName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getNodeIoFlag() {
		return nodeIoFlag;
	}
	public void setNodeIoFlag(Integer nodeIoFlag) {
		this.nodeIoFlag = nodeIoFlag;
	}
	
	
	
	
	
}
