package com.fitech.model.worktask.action;

import java.io.IOException;
import java.util.Set;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.WorkTaskLock;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IOpreatorService;
import com.fitech.model.worktask.service.IWorkTaskLockService;
import com.fitech.model.worktask.service.WorkTaskNodeMoniService;
public class TaskSynchronizeAction extends DefaultBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  taskId;
	private String userId;
	private IOpreatorService opreatorService;
	private WorkTaskNodeMoniService workTaskNodeMoniService;
	public WorkTaskNodeMoniService getWorkTaskNodeMoniService() {
		return workTaskNodeMoniService;
	}

	public void setWorkTaskNodeMoniService(
			WorkTaskNodeMoniService workTaskNodeMoniService) {
		this.workTaskNodeMoniService = workTaskNodeMoniService;
	}

	public IOpreatorService getOpreatorService() {
		return opreatorService;
	}

	public void setOpreatorService(IOpreatorService opreatorService) {
		this.opreatorService = opreatorService;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void taskSynchronize() {
		System.out.println(taskId + "::"+userId);
		WorkTaskConfig.moveMapDate(userId);//释放当前用户锁定的任务
		workTaskLockService.delete(userId);
		String result = "0";
		String[] strs = taskId.split("_");
		Integer taskMoniId = Integer.parseInt(strs[0]);
		Integer nodeId = Integer.parseInt(strs[1]);
		String orgId = strs[2];
		//如果锁定任务标志为0 或者不是待处理阶段
		if(WorkTaskConfig.SYSN_LOCK_TASK_FLAG==0 || !this.workTaskNodeMoniService.isNeedCheckLockTask(taskMoniId, nodeId, orgId)){
			try {
				this.getResponse().getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		String  nowTaskId  = "";//当前Map中用该户正在执行的任务
		String usingUserId = "";
		//判断当前任务是否存在库中
		try {
			WorkTaskLock lock  = workTaskLockService.read(taskId, null);
			if(lock!=null){
				 usingUserId = lock.getUserId();
				 if(!usingUserId.equals(userId)){
					result =  usingUserId;
				 }
			}
		} catch (BaseServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
//		 Set<String> set = WorkTaskConfig.USERTASKMAP.keySet();
//		  //遍历键取到map中值
//		  for(String nowUserId : set)
//		  {
//			 
//			  nowTaskId = WorkTaskConfig.USERTASKMAP.get(nowUserId);
//			  
//			  if(nowTaskId.equals(taskId)&&!nowUserId.equals(userId)){
//				  result = nowUserId;
//		//		  WorkTaskConfig.moveMapDate(userId);
//				  break;
//			  }
//		  } 
		if(result.equals("0")){
			putMapDate(taskId, userId);
			WorkTaskLock w  = new WorkTaskLock(userId,taskId);
			workTaskLockService.save(w);
		}
//		else
//			result = opreatorService.findNameById(new Long (result));
		try {
			this.getResponse().getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void putMapDate (String taskId ,String userId){
		synchronized (WorkTaskConfig.USERTASKMAP) {
			WorkTaskConfig.USERTASKMAP.put(userId,taskId);
		}
	}
	
	public void  moveMapDate(){
		Operator operator = ((Operator) this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME));
		try {
			if(operator!=null&&operator.getUserName()!=null){
				WorkTaskConfig.moveMapDate(operator.getUserName());
				moveLock(operator.getUserName());
			}
			this.getResponse().getWriter().write("1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private IWorkTaskLockService workTaskLockService ;
	
	

	public IWorkTaskLockService getWorkTaskLockService() {
		return workTaskLockService;
	}

	public void setWorkTaskLockService(IWorkTaskLockService workTaskLockService) {
		this.workTaskLockService = workTaskLockService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void moveLock(String userId) {
		workTaskLockService.delete(userId);
	}

	public void save (WorkTaskLock w){
		workTaskLockService.save(w);
	}
}
