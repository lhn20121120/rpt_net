package com.fitech.model.worktask.vo;
/**
 * add by 王明明
 * 关联任务vo
 * @author Administrator
 *
 */
public class WorkTaskRelationTaskVo extends WorkTaskBaseVo {
	private Integer taskId;
	private String taskName;
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
	
	
}
