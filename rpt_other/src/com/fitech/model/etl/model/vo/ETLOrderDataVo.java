package com.fitech.model.etl.model.vo;

public class ETLOrderDataVo{
	private Integer taskId;
	private String taskName;
	private Integer isOrder=0;
	
	/**任务id*/
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	/**任务名称*/
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	/**是否是前置任务 0为不是 1为是 默认为0*/
	public Integer getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(Integer isOrder) {
		this.isOrder = isOrder;
	}
	
	
}
