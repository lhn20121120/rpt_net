package com.fitech.model.worktask.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrgId;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeConductType;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoniId;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForce;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForceId;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskMoniService;
import com.fitech.model.worktask.service.IWorkTaskOrgService;
import com.fitech.model.worktask.service.IWorkTaskRepForceService;
import com.fitech.model.worktask.service.IWorkTaskValidateService;
import com.fitech.model.worktask.service.WorkTaskNodeMoniService;
import com.fitech.model.worktask.service.WorkTaskPendingTaskService;
import com.fitech.model.worktask.vo.PendingTaskQueryConditions;
import com.fitech.model.worktask.vo.WorkTaskIndexVo;
import com.fitech.model.worktask.vo.WorkTaskNodeInfoVo;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

/**
 * 节点执行任务
 * @author Administrator
 *
 */
public class WorkTaskNodeMoniServiceImpl extends DefaultBaseService<WorkTaskNodeMoni, String> implements WorkTaskNodeMoniService {

	private IWorkTaskMoniService workTaskMoniService;
	
	private IWorkTaskValidateService validateService;
	
	private IWorkTaskOrgService workTaskOrgService;
	
	private WorkTaskPendingTaskService pendingTaskService;
	
	private IWorkTaskInfoService workTaskInfoService;
	
	private IWorkTaskRepForceService workTaskRepForceService;
	
	public IWorkTaskRepForceService getWorkTaskRepForceService() {
		return workTaskRepForceService;
	}

	public WorkTaskPendingTaskService getPendingTaskService() {
		return pendingTaskService;
	}

	public void setPendingTaskService(WorkTaskPendingTaskService pendingTaskService) {
		this.pendingTaskService = pendingTaskService;
	}

	public void setWorkTaskRepForceService(
			IWorkTaskRepForceService workTaskRepForceService) {
		this.workTaskRepForceService = workTaskRepForceService;
	}

	public IWorkTaskInfoService getWorkTaskInfoService() {
		return workTaskInfoService;
	}

	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}

	public IWorkTaskOrgService getWorkTaskOrgService() {
		return workTaskOrgService;
	}

	public void setWorkTaskOrgService(IWorkTaskOrgService workTaskOrgService) {
		this.workTaskOrgService = workTaskOrgService;
	}

	public IWorkTaskValidateService getValidateService() {
		return validateService;
	}

	public void setValidateService(IWorkTaskValidateService validateService) {
		this.validateService = validateService;
	}

	public IWorkTaskMoniService getWorkTaskMoniService() {
		return workTaskMoniService;
	}

	public void setWorkTaskMoniService(IWorkTaskMoniService workTaskMoniService) {
		this.workTaskMoniService = workTaskMoniService;
	}

	@Override
	public void backWorkTaskNodeMoni(String[] strs,String rerunDesc,String taskNodeIds,
						Integer nodeIds,Integer repDay) throws Exception {
		// TODO Auto-generated method stub
		List<WorkTaskNodeMoni> monis = createWorkTaskNodeMoni(strs);
		String hsql = "";
		if(monis!=null && monis.size()>0){
			for(WorkTaskNodeMoni moni : monis){
				WorkTaskNodeMoni conditioMoni = findOneWorkTaskNodeMoni(moni);
				List<WorkTaskNodeMoni> bwtMonis = new ArrayList<WorkTaskNodeMoni>();
				findAllBackWorkTaskMoni(conditioMoni,bwtMonis,taskNodeIds!=null?Integer.parseInt(taskNodeIds):null);
				if(bwtMonis!=null && bwtMonis.size()>0){
					for (int i = 0; i < bwtMonis.size(); i++) {
						WorkTaskNodeMoni wnm = new WorkTaskNodeMoni();
						WorkTaskNodeMoniId wnmId = new WorkTaskNodeMoniId();
						BeanUtils.copyProperties(wnm, bwtMonis.get(i));
						BeanUtils.copyProperties(wnmId, bwtMonis.get(i).getId());
						wnm.setId(wnmId);
						if((i+1)==bwtMonis.size()){
							wnm.setNodeFlag(WorkTaskConfig.NODE_FLAG_REFU);//设置当前节点状态为不通过
							//对当前审核不通过的任务执行短信提醒操作
							
							WorkTaskMoni taskMoni = this.findOneWorkTaskMoni(wnm.getId().getTaskMoniId());
							pendingTaskService.performByNodeId(wnm.getId().getNodeId()
									,DateUtil.toSimpleDateFormat(taskMoni.getTaskTerm(), DateUtil.NORMALDATE)
									,wnm.getId().getOrgId());
						}else{
							wnm.setNodeFlag(WorkTaskConfig.NODE_FLAG_REWA);//设置当前节点状态为退回等待
						}
						if(repDay!=null){
							if(repDay!=null){
								if(WorkTaskConfig.REPORT_TIME_UNIT_DAY.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
									wnm.setLateRepDate(DateUtil.getNextDay(wnm.getLateRepDate(), repDay));
								}else if(WorkTaskConfig.REPORT_TIME_UNIT_HOUR.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
									Calendar cd = Calendar.getInstance();
									cd.setTime(wnm.getLateRepDate());
									cd.add(Calendar.HOUR, repDay);
									wnm.setLateRepDate(cd.getTime());
								}
							}
						}
						wnm.setRerepFlag(WorkTaskConfig.REREP_FLAG_YES);//设置重报标志为1
						wnm.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
						wnm.setStartDate(new Timestamp(System.currentTimeMillis())); //设置此节点任务的开始时间
						wnm.getId().setPerformNumber(wnm.getId().getPerformNumber()+1); //将执行次数加一
						wnm.setReturnDesc(rerunDesc);
						this.objectDao.save(wnm);    //退回节点任务时重新插入一条新的记录，
						
						bwtMonis.get(i).setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_NOT); //将原来的节点任务的执行状态更新为0
						this.objectDao.update(bwtMonis.get(i));
					}
				}
				//将退回节点对象状态设置成退回等待状态
				moni = findOneWorkTaskNodeMoni(moni);
				WorkTaskNodeMoni wnm = new WorkTaskNodeMoni();
				WorkTaskNodeMoniId wnmId = new WorkTaskNodeMoniId();
				BeanUtils.copyProperties(wnm, moni);
				BeanUtils.copyProperties(wnmId, moni.getId());
				wnm.setId(wnmId);
				if(repDay!=null){
//					wnm.setLateRepDate(DateUtil.getNextDay(moni.getLateRepDate(), repDay));
//					wnm.setLateRerepDate(repDay);
					
					if(WorkTaskConfig.REPORT_TIME_UNIT_DAY.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
						wnm.setLateRepDate(DateUtil.getNextDay(wnm.getLateRepDate(), repDay));
					}else if(WorkTaskConfig.REPORT_TIME_UNIT_HOUR.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
						Calendar cd = Calendar.getInstance();
						cd.setTime(wnm.getLateRepDate());
						cd.add(Calendar.HOUR, repDay);
						wnm.setLateRepDate(cd.getTime());
					}
					wnm.setLateRerepDate(repDay);
				}
				wnm.setReturnDesc(rerunDesc);
				wnm.setNodeFlag(WorkTaskConfig.NODE_FLAG_REWA);
				wnm.setRerepFlag(WorkTaskConfig.REREP_FLAG_YES);
				wnm.getId().setPerformNumber(wnm.getId().getPerformNumber()+1);
				this.objectDao.save(wnm);    //退回节点任务时重新插入一条新的记录，
				
				moni.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_NOT);
				this.objectDao.update(moni);

			}
		}
	}
	/**
	 * 将所有退回任务的信息从字符数组转换成对象
	 * 当work_task_type_id的值为normal 时，会正常退回上一任务，
	 * 当值为fill时循环往上一任务寻找填报任务，找到返回
	 * 当值为check时循环往上一任务寻找复核任务，找到返回
	 * @param strs
	 * @return
	 * @throws Exception
	 */
	public void findAllBackWorkTaskMoni(WorkTaskNodeMoni conditioMoni,
			List<WorkTaskNodeMoni> backTaskMonis,Integer taskNodeIds)throws Exception{
		if(conditioMoni==null)
			return;
		String checkType=WorkTaskConfig.WORK_TASK_COND_TYPE_ID;
		if(checkType==null)return;
		WorkTaskNodeMoni wm=new WorkTaskNodeMoni();
		WorkTaskNodeMoniId wmId = new WorkTaskNodeMoniId();
		
		BeanUtils.copyProperties(wm, conditioMoni);
		BeanUtils.copyProperties(wmId, conditioMoni.getId());
		
		wm.setId(wmId);
		wm.getId().setNodeId(wm.getPreNodeId());
		
		WorkTaskNodeMoni normalMoni = findOneWorkTaskNodeMoni(wm);
		if(normalMoni!=null){
			if(checkType.equalsIgnoreCase(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL)){
				WorkTaskNodeInfo tbNodeInfo = findOneWorkTaskNodeInfo(normalMoni.getId().getNodeId());
				if(tbNodeInfo!=null){
					if(tbNodeInfo.getCondTypeId().equals(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL)){
						backTaskMonis.add(normalMoni);
						return;
					}else{
						backTaskMonis.add(normalMoni);
					}
				}
			}else if(checkType.equalsIgnoreCase(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC)){
				WorkTaskNodeInfo fhNodeInfo = findOneWorkTaskNodeInfo(normalMoni.getId().getNodeId());
				if(fhNodeInfo!=null){
					if(fhNodeInfo.getCondTypeId().equals(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC)){
						backTaskMonis.add(normalMoni);
						return;
					}else{
						backTaskMonis.add(normalMoni);
					}
				}
			}else if(checkType.equalsIgnoreCase("normal")){
				backTaskMonis.add(normalMoni);
				return;
			}else if(checkType.equalsIgnoreCase("custom")){
				if(taskNodeIds!=null && normalMoni.getId().getNodeId().equals(taskNodeIds)){
					backTaskMonis.add(normalMoni);
					return;
				}else{
					backTaskMonis.add(normalMoni);
				}
			}else if(checkType.equalsIgnoreCase("top")){
				backTaskMonis.add(normalMoni);
			}
		}
		findAllBackWorkTaskMoni(normalMoni, backTaskMonis, taskNodeIds);
	}

	public void saveWorkTaskMoni(WorkTaskNodeMoni wMoni)throws Exception{
		this.objectDao.save(wMoni);
	}
	
	public WorkTaskNodeMoni findOneWorkTaskNodeMoni(WorkTaskNodeMoni moni)
		throws Exception {
		// TODO Auto-generated method stub
		String hsql = "from WorkTaskNodeMoni wm where wm.id.taskMoniId="+moni.getId().getTaskMoniId()+
		" and wm.id.nodeId="+moni.getId().getNodeId()+" and wm.id.orgId ='"+moni.getId().getOrgId()
		+"' and wm.finalExecFlag=1";
		
		return (WorkTaskNodeMoni)this.findObject(hsql);
	}
	
	@Override
	public boolean updateWorkTaskNodeMoni(String[] strs,int updateType)
			throws Exception {
		// TODO Auto-generated method stub
		boolean res = false;
		if(strs==null || strs.length==0)
			return res;
		List<WorkTaskNodeMoni> monis = createWorkTaskNodeMoni(strs);
		String hsql = "";
		if(monis!=null && monis.size()>0){
			for(WorkTaskNodeMoni nodeMoni : monis){
				nodeMoni = this.findOneWorkTaskNodeMoni(nodeMoni);//如果当前节点处于重报标志那它的上一节点肯定是也处于重报标志
				if(nodeMoni!=null && !nodeMoni.getPreNodeId().equals(WorkTaskConfig.PRE_OBJECT_NULL)){
					WorkTaskNodeMoni wMoni =null;
					if(nodeMoni.getRerepFlag()!=null && nodeMoni.getRerepFlag().equals(WorkTaskConfig.LATE_REP_FLAG_YES)){
//						hsql = "from WorkTaskNodeMoni wm where wm.id.taskMoniId="+nodeMoni.getId().getTaskMoniId()+
//						" and wm.id.nodeId="+nodeMoni.getPreNodeId()+" and wm.id.orgId = "+nodeMoni.getId().getOrgId()
//						+" and wm.rerepFlag="+nodeMoni.getRerepFlag();
//						wMoni = (WorkTaskNodeMoni)this.objectDao.findObject(hsql);
						String sql = " select mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER,mn.BUSI_LINE,"+
						  " mn.ROLE_ID,mn.PRE_TASK_ID,mn.LATE_REREP_DATE,mn.NODE_FLAG,mn.PRE_NODE_ID,"+
						  " mn.FINAL_EXEC_FLAG,mn.RETURN_DESC,mn.START_DATE,mn.END_DATE,mn.LATE_REP_DATE,"+
						  " mn.REREP_FLAG,mn.REREP_TIME,mn.LATE_REP_FLAG,mn.LEAV_REP_FLAG "+
						   " from work_task_node_moni mn inner join"+
						   " (select w.task_moni_id task_moni_id,w.node_id node_id,w.org_id org_id,max(w.perform_number) perform_number from work_task_node_moni w"+
						   " where w.task_moni_id="+nodeMoni.getId().getTaskMoniId()+" and w.node_id="+nodeMoni.getPreNodeId()+" and w.org_id='"+nodeMoni.getId().getOrgId()+"'"+
						  " and w.REREP_FLAG="+nodeMoni.getRerepFlag()+" group by w.task_moni_id,w.node_id,w.org_id)m "+
						  " on mn.task_moni_id=m.task_moni_id and mn.node_id=m.node_id and mn.org_id=m.org_id and mn.perform_number=m.perform_number"+
						  " order by mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER";
						wMoni = (WorkTaskNodeMoni)this.objectDao.findBysql(WorkTaskNodeMoni.class, sql);
					}else{
//						hsql = "from WorkTaskNodeMoni wm where wm.id.taskMoniId="+nodeMoni.getId().getTaskMoniId()+
//						" and wm.id.nodeId="+nodeMoni.getPreNodeId()+" and wm.id.orgId = "+nodeMoni.getId().getOrgId();
						//+" and wm.id.performNumber="+nodeMoni.getId().getPerformNumber();
						String sql = " select mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER,mn.BUSI_LINE,"+
						  " mn.ROLE_ID,mn.PRE_TASK_ID,mn.LATE_REREP_DATE,mn.NODE_FLAG,mn.PRE_NODE_ID,"+
						  " mn.FINAL_EXEC_FLAG,mn.RETURN_DESC,mn.START_DATE,mn.END_DATE,mn.LATE_REP_DATE,"+
						  " mn.REREP_FLAG,mn.REREP_TIME,mn.LATE_REP_FLAG,mn.LEAV_REP_FLAG "+
						   " from work_task_node_moni mn inner join"+
						   " (select w.task_moni_id task_moni_id,w.node_id node_id,w.org_id org_id,max(w.perform_number) perform_number from work_task_node_moni w"+
						   " where w.task_moni_id="+nodeMoni.getId().getTaskMoniId()+" and w.node_id="+nodeMoni.getPreNodeId()+" and w.org_id='"+nodeMoni.getId().getOrgId()+"'"+
						  " group by w.task_moni_id,w.node_id,w.org_id)m "+
						  " on mn.task_moni_id=m.task_moni_id and mn.node_id=m.node_id and mn.org_id=m.org_id and mn.perform_number=m.perform_number"+
						  " order by mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER";
						wMoni = (WorkTaskNodeMoni)this.objectDao.findBysql(WorkTaskNodeMoni.class, sql);
						
					}
					
					if(wMoni!=null){
						wMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_PASS);
						this.objectDao.update(wMoni);
					}
				}
				switch(updateType){
					case 1: //是普通的节点一执行到节点二，将节点一的状态更新为已提交,节点二的状态更新为待处理
						WorkTaskNodeMoni wm = this.findOneWorkTaskNodeMoni(nodeMoni);
						if(wm!=null){
							wm.setNodeFlag(WorkTaskConfig.NODE_FLAG_COMM);
							wm.setEndDate(new java.sql.Timestamp(DateUtil.getTodayDate().getTime()));
							wm.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);  //将正在执行的节点对象的最终执行状态更新为1
							Date currentDate = new Date();
							if(currentDate.compareTo(wm.getLateRepDate())>0){
								wm.setLateRepFlag(WorkTaskConfig.LATE_REP_FLAG_YES);
							}
							this.objectDao.update(wm);
							
							WorkTaskNodeMoni nextWorkTaskNodeMoni = this.findNextWorkTaskNodeMoni(wm);
							if(nextWorkTaskNodeMoni!=null){
								nextWorkTaskNodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
								this.objectDao.update(nextWorkTaskNodeMoni);
							}
							
							res =true;
						}
						break;
					case 2:
						break;
				}
			}
		}
		return res;
	}
	/**
	 * 找到现有工作任务的下一节点任务
	 * @param moni
	 * @return
	 * @throws Exception
	 */
	public WorkTaskNodeMoni findNextWorkTaskNodeMoni(WorkTaskNodeMoni moni)throws Exception{
		String hsql = "select n.nodeId from WorkTaskNodeInfo n where n.preNodeId="+moni.getId().getNodeId();
		Integer nodeId = (Integer)this.objectDao.findObject(hsql);
		if(nodeId!=null){
			WorkTaskNodeMoni wm=new WorkTaskNodeMoni();
			WorkTaskNodeMoniId wmId = new WorkTaskNodeMoniId();
			
			BeanUtils.copyProperties(wm, moni);
			BeanUtils.copyProperties(wmId, moni.getId());
			wm.setId(wmId);
			if(!wm.getPreNodeId().equals(WorkTaskConfig.PRE_OBJECT_NULL)){
				
			}
			wm.getId().setNodeId(nodeId);
			wm = findOneWorkTaskNodeMoni(wm);
			if(wm!=null && wm.getRerepFlag()!=null && 
					wm.getRerepFlag().equals(WorkTaskConfig.LATE_REP_FLAG_YES)){
//				hsql = "from WorkTaskNodeMoni w where w.id.orgId='"+moni.getId().getOrgId()+"'"+
//						" and w.id.taskMoniId="+moni.getId().getTaskMoniId()+
//						 " and w.id.nodeId="+nodeId
//						 +" and w.rerepFlag="+moni.getRerepFlag();   //当节点状态为退出等待时，
				hsql = " select mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER,mn.BUSI_LINE,"+
				  " mn.ROLE_ID,mn.PRE_TASK_ID,mn.LATE_REREP_DATE,mn.NODE_FLAG,mn.PRE_NODE_ID,"+
				  " mn.FINAL_EXEC_FLAG,mn.RETURN_DESC,mn.START_DATE,mn.END_DATE,mn.LATE_REP_DATE,"+
				  " mn.REREP_FLAG,mn.REREP_TIME,mn.LATE_REP_FLAG,mn.LEAV_REP_FLAG "+
				   " from work_task_node_moni mn inner join"+
				   " (select w.task_moni_id task_moni_id,w.node_id node_id,w.org_id org_id,max(w.perform_number) perform_number from work_task_node_moni w"+
				   " where w.task_moni_id="+wm.getId().getTaskMoniId()+" and w.node_id="+nodeId+" and w.org_id='"+wm.getId().getOrgId()+"'"+
				  " and w.REREP_FLAG="+wm.getRerepFlag()+" group by w.task_moni_id,w.node_id,w.org_id)m "+
				  " on mn.task_moni_id=m.task_moni_id and mn.node_id=m.node_id and mn.org_id=m.org_id and mn.perform_number=m.perform_number"+
				  " order by mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER";
			}else{
//				hsql = "from WorkTaskNodeMoni w where w.id.orgId='"+moni.getId().getOrgId()+"'"+
//				" and w.id.taskMoniId="+moni.getId().getTaskMoniId()+
//				 " and w.id.nodeId="+nodeId +" and w.id.performNumber="+moni.getId().getPerformNumber();
				hsql = " select mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER,mn.BUSI_LINE,"+
				  " mn.ROLE_ID,mn.PRE_TASK_ID,mn.LATE_REREP_DATE,mn.NODE_FLAG,mn.PRE_NODE_ID,"+
				  " mn.FINAL_EXEC_FLAG,mn.RETURN_DESC,mn.START_DATE,mn.END_DATE,mn.LATE_REP_DATE,"+
				  " mn.REREP_FLAG,mn.REREP_TIME,mn.LATE_REP_FLAG,mn.LEAV_REP_FLAG "+
				   " from work_task_node_moni mn inner join"+
				   " (select w.task_moni_id task_moni_id,w.node_id node_id,w.org_id org_id,max(w.perform_number) perform_number from work_task_node_moni w"+
				   " where w.task_moni_id="+wm.getId().getTaskMoniId()+" and w.node_id="+nodeId+" and w.org_id='"+wm.getId().getOrgId()+"'"+
				  " group by w.task_moni_id,w.node_id,w.org_id)m "+
				  " on mn.task_moni_id=m.task_moni_id and mn.node_id=m.node_id and mn.org_id=m.org_id and mn.perform_number=m.perform_number"+
				  " order by mn.TASK_MONI_ID,mn.NODE_ID,mn.ORG_ID,mn.PERFORM_NUMBER";
			}
			return (WorkTaskNodeMoni)this.objectDao.findBysql(WorkTaskNodeMoni.class, hsql);
		}
		return null;
	}
	
	public WorkTaskNodeMoni findNextOneWorkNodeMoni(WorkTaskNodeMoni nodeMoni)throws Exception{
		String hsql = "select n.nodeId from WorkTaskNodeInfo n where n.preNodeId="+nodeMoni.getId().getNodeId();
		Integer nodeId = (Integer)this.objectDao.findObject(hsql);
		if(nodeId!=null){
			WorkTaskNodeMoni wm=new WorkTaskNodeMoni();
			WorkTaskNodeMoniId wmId = new WorkTaskNodeMoniId();
			
			BeanUtils.copyProperties(wm, nodeMoni);
			BeanUtils.copyProperties(wmId, nodeMoni.getId());
			wm.setId(wmId);
			wm.getId().setNodeId(nodeId);
			wm = findOneWorkTaskNodeMoni(wm);
			return wm;
		}
		return null;
	}
	
	public WorkTaskNodeInfo findOneWorkTaskNodeInfo(Integer nodeId)throws Exception{
		String hsql = "from WorkTaskNodeInfo wi where wi.nodeId="+nodeId;
		return (WorkTaskNodeInfo)this.objectDao.findObject(hsql);
	}
	
	@Override
	public WorkTaskNodeMoni saveWorkTaskNodeModi(String[] strs)
			throws Exception {
		// TODO Auto-generated method stub
		if(strs==null || strs.length<6)
			return null;
		WorkTaskNodeMoni nodeMoni = new WorkTaskNodeMoni();
		WorkTaskNodeMoniId nodeMoniId = new WorkTaskNodeMoniId();
		String hsql = "";
		/**
		 * 找当前任务是否有下一个任务(begin)
		 */
		hsql = "select w.taskId from WorkTaskInfo w where w.preTaskId =(select m.taskId from WorkTaskMoni m where m.taskMoniId="+Long.parseLong(strs[0])+") ";
		Integer taskId = (Integer)this.objectDao.findObject(hsql);
		if(taskId!=null){
			hsql = "select m.taskMoniId from WorkTaskMoni m where m.taskId = "+taskId;
			Long taskMoniId =(Long)this.objectDao.findObject(hsql);
			if(taskMoniId!=null){
				nodeMoniId.setTaskMoniId(taskMoniId);
				hsql = "select m.taskId from WorkTaskMoni m where m.taskMoniId="+Long.parseLong(strs[0]);
				Integer preTaskId = (Integer)this.objectDao.findObject(hsql);
				if(preTaskId!=null){
					nodeMoni.setPreTaskId(taskId);
					hsql = "select w.nodeId from WorkTaskNodeInfo w where w.taskId ="+taskId+" and w.preNodeId="+WorkTaskConfig.PRE_OBJECT_NULL;
					Integer currentNodeId = (Integer)this.objectDao.findObject(hsql);
					nodeMoniId.setNodeId(currentNodeId);
				}
			}
		}//end
		else{
			/**
			 * 找当前节点的下一节点(begin)
			 */
			hsql = "select w.nodeId from WorkTaskNodeInfo w where w.preNodeId = "+Integer.parseInt(strs[2]);
			Integer nodeId = (Integer)this.objectDao.findObject(hsql);
			if(nodeId!=null)
				nodeMoniId.setNodeId(nodeId);//end
			nodeMoniId.setTaskMoniId(Long.parseLong(strs[0]));
			nodeMoni.setPreNodeId(Integer.parseInt(strs[2]));
			nodeMoni.setPreTaskId(WorkTaskConfig.PRE_OBJECT_NULL);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
			Date lateRepDate = df.parse(strs[5]);
			Date currentDate = new Date();
			if(lateRepDate.compareTo(currentDate)<0){
				nodeMoni.setLateRepFlag(WorkTaskConfig.LATE_REP_FLAG_YES);
			}
		}
		nodeMoni.setBusiLine(strs[4]);
		nodeMoni.setFinalExecFlag(new Integer(1));
		nodeMoniId.setOrgId(strs[3]);
		nodeMoniId.setPerformNumber(new Integer(0));
		nodeMoni.setRoleId(Integer.parseInt(strs[1]));
		nodeMoni.setId(nodeMoniId);
//		nodeMoni.setEndDate(endDate);
		nodeMoni.setLateRepFlag(new Integer(0));
//		nodeMoni.setLateRerepDate(lateRerepDate);
		nodeMoni.setLeavRepFlag(new Integer(0));
		nodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
//		nodeMoni.setRerepTime(rerepTime);
		nodeMoni.setStartDate(new java.sql.Timestamp(DateUtil.getTodayDate().getTime()));
		this.objectDao.save(nodeMoni);
		//更新节点对象
		WorkTaskNodeMoni conditionMoni = new WorkTaskNodeMoni();
		WorkTaskNodeMoniId conditionMoniId = new WorkTaskNodeMoniId();
		conditionMoniId.setTaskMoniId(Long.parseLong(strs[0]));
		conditionMoniId.setNodeId(Integer.parseInt(strs[2]));
		conditionMoniId.setOrgId(nodeMoni.getId().getOrgId());
		conditionMoni.setId(conditionMoniId);
		return conditionMoni;
	}

	@Override
	public PageResults findPendingTask(Map parameterMap,
			PendingTaskQueryConditions pendingTaskQueryConditions,
			PageResults pageResults,List<WorkTaskNodeRole> nodes,
			Long userId,String orgId,Integer repFlag,String ids) throws Exception {
		// TODO Auto-generated method stub
		PageResults prs=null;
		if(pageResults==null)
			pageResults = new PageResults<WorkTaskPendingTaskVo>();
		pageResults.setPageSize(Config.PageSize);  //设置每页显示5条数据
		if(parameterMap==null)
			parameterMap=new HashMap();
		if(pendingTaskQueryConditions!=null&&!pendingTaskQueryConditions.isMultiTaskFlag()){
		if(pendingTaskQueryConditions !=null){
			 if(pendingTaskQueryConditions.getTaskName()!=null&&
					   !pendingTaskQueryConditions.getTaskName().trim().equals("")){
				 parameterMap.put("taskName", pendingTaskQueryConditions.getTaskName());
			 }
			 if(pendingTaskQueryConditions.getTaskId()!=null
					 && !pendingTaskQueryConditions.getTaskId().equals(new Integer(-1))){
				 parameterMap.put("taskId", pendingTaskQueryConditions.getTaskId());
			 }
			 if(pendingTaskQueryConditions.getTaskTerm()!=null //带入期数查询条件
					 &&!pendingTaskQueryConditions.getTaskTerm().trim().equals("")){
				 parameterMap.put("taskTerm", pendingTaskQueryConditions.getTaskTerm());
			}
			if(pendingTaskQueryConditions.getFreqId()!=null
					   && !pendingTaskQueryConditions.getFreqId().equals("-1")){
				 parameterMap.put("freqId", pendingTaskQueryConditions.getFreqId());
			}
			if(pendingTaskQueryConditions.getLikeOrgName()!=null &&
					!"".equals(pendingTaskQueryConditions.getLikeOrgName())){
				 parameterMap.put("orgName", pendingTaskQueryConditions.getLikeOrgName());
			}
			if(pendingTaskQueryConditions.getOrgId()!=null &&
					!WorkTaskConfig.LIST_SELECTED_ALL.equals(pendingTaskQueryConditions.getOrgId())){
				 parameterMap.put("OrgId", pendingTaskQueryConditions.getOrgId());
			}
			if(pendingTaskQueryConditions.getTaskMoniId()!=null){
				parameterMap.put("taskMoniId",pendingTaskQueryConditions.getTaskMoniId());
			}
			if(pendingTaskQueryConditions.getNodeFlag()!=null &&
					!pendingTaskQueryConditions.getNodeFlag().equals(new Integer(-1))){
				parameterMap.put("nodeFlag",pendingTaskQueryConditions.getNodeFlag());
			}
			if(pendingTaskQueryConditions.getBusiLine()!=null
					&& !pendingTaskQueryConditions.getBusiLine().equals("-1")){
				parameterMap.put("busiLine", pendingTaskQueryConditions.getBusiLine().trim());
			}
			if(pendingTaskQueryConditions.getCondTypeId()!=null &&
					!"".equals(pendingTaskQueryConditions.getCondTypeId())
					&&!"-1".equals(pendingTaskQueryConditions.getCondTypeId())){
				parameterMap.put("condTypeId", pendingTaskQueryConditions.getCondTypeId());
			}
		}
		StringBuffer hsql = new StringBuffer();
		// 最终执行标志finalExecFlag为1
		hsql.append("select nMoni from WorkTaskNodeMoni nMoni,WorkTaskMoni moni,WorkTaskNodeInfo ni");
		
		ViewWorktaskOrg vOrg = this.findOneWorkTaskorg(orgId);
		
		hsql.append(",ViewWorktaskOrg org");
		
		if(parameterMap.containsKey("taskName")||
				parameterMap.containsKey("freqId")){
			hsql.append(",WorkTaskInfo taskInfo");
		}
		
		hsql.append(" where moni.taskMoniId=nMoni.id.taskMoniId and nMoni.finalExecFlag=1 and moni.execFlag=1");
		
		hsql.append(" and nMoni.id.nodeId=ni.nodeId ");
		
		hsql.append(" and org.id.orgId=nMoni.id.orgId");
		
		if(parameterMap.containsKey("taskName")||
				parameterMap.containsKey("freqId")){
			hsql.append(" and moni.taskId=taskInfo.taskId ");
		}
		
		if(parameterMap.containsKey("taskId")){
			hsql.append(" and moni.taskId="+pendingTaskQueryConditions.getTaskId());
		}
		
		if(parameterMap.containsKey("taskName")){
			hsql.append(" and moni.taskName like '%"+pendingTaskQueryConditions.getTaskName().trim().replaceAll(" ","")+"%'");
		}
		
		if(parameterMap.containsKey("freqId")){
			hsql.append(" and taskInfo.freqId='"+pendingTaskQueryConditions.getFreqId().trim()+"'");
		}
		
		if(parameterMap.containsKey("orgName")){
			hsql.append(" and org.id.orgName like '%"+parameterMap.get("orgName")+"%'");
		}
		
		if(parameterMap.containsKey("nodeFlag")){
			if(ids!=null && !ids.equals("")){
				hsql.append(" and nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_COMM);
			}else
				hsql.append(" and nMoni.nodeFlag="+pendingTaskQueryConditions.getNodeFlag());
		}
		if(ids!=null && !ids.equals("")){
			hsql.append(" and nMoni.id.nodeId in("+ids+") and nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_COMM);
		}
		if(parameterMap.containsKey("taskMoniId")){
			hsql.append(" and nMoni.id.taskMoniId="+pendingTaskQueryConditions.getTaskMoniId());
		}
		if(parameterMap.containsKey("busiLine")){
			hsql.append(" and nMoni.busiLine='"+pendingTaskQueryConditions.getBusiLine()+"'");
		}
		if(parameterMap.containsKey("condTypeId")){
			hsql.append(" and ni.condTypeId='"+pendingTaskQueryConditions.getCondTypeId()+"'");
		}
		
		String nodeIds = this.getWorkTaskNodes(nodes);
		
		hsql.append(" and nMoni.id.nodeId in("+nodeIds+")");
		
		String orgStr = "";//将当前用户所属机构的所有子机构
		if(pendingTaskQueryConditions.getOrgId()!=null &&
				WorkTaskConfig.LIST_SELECTED_ALL.equals(pendingTaskQueryConditions.getOrgId())){
			List orgList = new ArrayList();
			workTaskOrgService.getChildListByOrgId(orgList,orgId,null);
			if(orgList!=null && !orgList.isEmpty()){
				for (int i = 0; i < orgList.size(); i++) {
					String[] strs = (String[]) orgList.get(i);
					if (!"".equals(orgStr)) {
						orgStr+=",";
					}
					orgStr+="'"+strs[0]+"'";
				}
			}
		}
		if(!"".equals(orgStr)){
			orgStr +=",'"+orgId+"'";
		}
		
		String orgIds =findAllViewWtPurOrgsByRoleIds(userId);//查询当前用户所有的用户组权限 用户id和机构id对应关系
		
		String searchOrgId = "";
		
		if(parameterMap.containsKey("OrgId")){
			searchOrgId+="'"+parameterMap.get("OrgId")+"'";
		}else{
			if(!"".equals(orgIds)){
				if(!"".equals(searchOrgId))
					searchOrgId+=","+orgIds;
				else
					searchOrgId+=orgIds;
			}
			if(!"".equals(orgStr)){
				if(!"".equals(searchOrgId))
					searchOrgId+=","+orgStr;
				else
					searchOrgId+=orgStr;
			}else{
				if(!"".equals(searchOrgId))
					searchOrgId+=",'"+orgId+"'";
				else
					searchOrgId+="'"+orgId+"'";
			}
		}
		
		if(!"".equals(searchOrgId)){
			hsql.append(" and nMoni.id.orgId in("+searchOrgId+")");
		}
		
		if(parameterMap.containsKey("taskTerm")){
			hsql.append(" and moni.taskTerm=to_date('"+pendingTaskQueryConditions.getTaskTerm().trim()+"','yyyy-MM-dd')");
		}
			
		hsql.append(" order by org.id.orgLevel,org.id.orgName");
		
		 prs = this.objectDao.findPageByHsql(hsql.toString(), parameterMap, pageResults.getPageSize(), pageResults.getCurrentPage());
		parameterMap.clear();
		if(prs==null)
			return null;
		
		List<WorkTaskNodeMoni> wtns = prs.getResults();
		List<WorkTaskPendingTaskVo> vos = new ArrayList<WorkTaskPendingTaskVo>();
		if(wtns!=null && !wtns.isEmpty()){
			for(WorkTaskNodeMoni node : wtns){
				if(node==null)
					continue;
				WorkTaskPendingTaskVo wto = new WorkTaskPendingTaskVo();
				//add by wmm start 将处于待处理阶段的节点名称取出来
				String curStateHql="select ni.nodeName,ni.nodeId from WorkTaskNodeMoni nMoni,WorkTaskNodeInfo ni where nMoni.id.nodeId=ni.nodeId and nMoni.finalExecFlag=1 and  (nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WADC+" or nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REFU+") and nMoni.id.taskMoniId="+node.getId().getTaskMoniId()+"  and nMoni.id.orgId='"+node.getId().getOrgId()+"'";
				List curStateList=this.objectDao.findListByHsql(curStateHql, null);
				for (int i = 0; curStateList!=null&&i < curStateList.size(); i++) {
					Object[] objs=(Object[])curStateList.get(i);
					wto.setCurState((String)objs[0]);
					wto.setCurStateId((Integer)objs[1]);
				}
				//add by end
				wto.setBusiLine(node.getBusiLine());
				wto.setPerformNumber(node.getId().getPerformNumber());
				wto.setLateRepDate(node.getLateRepDate());
				wto.setNodeFlag(node.getNodeFlag());
				Date currentDate = new Date();
				if(currentDate.compareTo(node.getLateRepDate())>0){
					node.setLateRepFlag(WorkTaskConfig.LATE_REP_FLAG_YES);
					this.update(node);
				}
				wto.setLateRepFlag(node.getLateRepFlag());
				wto.setLateRepFlagName(node.getLateRepFlag().equals(WorkTaskConfig.LATE_REP_FLAG_YES)?"迟报":"正常");
				if(pendingTaskQueryConditions!=null){
					if(pendingTaskQueryConditions.getRoleId()!=null)
						wto.setRoleId(pendingTaskQueryConditions.getRoleId());
				}
				String hqlNode= null;
				if(node.getId()!=null &&  node.getId().getOrgId()!=null){
					hqlNode = "select v.id.orgName from ViewWorktaskOrg v where v.id.orgId=:orgId";
					parameterMap.put("orgId", node.getId().getOrgId());
					List list = this.objectDao.findListByHsql(hqlNode, parameterMap);
					if(list!=null && !list.isEmpty()){
						String orgName = (String)list.get(0);
						wto.setOrgName(orgName);
						wto.setOrgId(node.getId().getOrgId());
					}
					hqlNode =null;
					parameterMap.clear();
				}
				WorkTaskMoni moni = workTaskMoniService.findOneWorkTaskMoni(node.getId().getTaskMoniId());
				
				WorkTaskNodeInfo workTaskNodeInfo=null;
				if(node.getId()!=null && node.getId().getNodeId()!=null){
					hqlNode = "from WorkTaskNodeInfo w where w.nodeId="+node.getId().getNodeId();
					Object obj = this.objectDao.findObject(hqlNode);
					if(obj!=null && obj instanceof WorkTaskNodeInfo){
						workTaskNodeInfo = (WorkTaskNodeInfo)obj;
						wto.setTaskNodeName(workTaskNodeInfo.getNodeName());
						wto.setNodeId(workTaskNodeInfo.getNodeId());
						wto.setCondTypeId(findWorkTaskType(workTaskNodeInfo.getCondTypeId()));
					}
				}
				if(node.getNodeFlag()!=null){
					wto.setTaskState(this.getNodeStateName(node.getNodeFlag()));
				}
				if(moni!=null){
					Integer taskId = moni.getTaskId();
					wto.setTaskMoniId(moni.getTaskMoniId());
					wto.setTaskName(moni.getTaskName());
					if(taskId!=null){
						hqlNode = "from WorkTaskInfo w where w.taskId="+taskId;
						WorkTaskInfo workTaskInfo = (WorkTaskInfo)this.objectDao.findObject(hqlNode);
						if(workTaskInfo!=null){
							wto.setTaskId(taskId);
							//wto.setTaskName(workTaskInfo.getTaskName());
							String freqId = workTaskInfo.getFreqId();
							wto.setFreqId(getFreqIdName(freqId));
							wto.setWorkTypeId(workTaskInfo.getTaskTypeId());
						}
					}
					wto.setTaskTerm(moni.getTaskTerm());
				}
				vos.add(wto);
			}
		}
		prs.setResults(vos);
		
		}if(pendingTaskQueryConditions!=null&&pendingTaskQueryConditions.isMultiTaskFlag()){

			if(pendingTaskQueryConditions !=null){
				 if(pendingTaskQueryConditions.getTaskName()!=null&&
						   !pendingTaskQueryConditions.getTaskName().trim().equals("")){
					 parameterMap.put("taskName", pendingTaskQueryConditions.getTaskName());
				 }
				 if(pendingTaskQueryConditions.getTaskId()!=null
						 && !pendingTaskQueryConditions.getTaskId().equals(new Integer(-1))){
					 parameterMap.put("taskId", pendingTaskQueryConditions.getTaskId());
				 }
				 if(pendingTaskQueryConditions.getTaskTerm()!=null //带入期数查询条件
						 &&!pendingTaskQueryConditions.getTaskTerm().trim().equals("")){
					 parameterMap.put("taskTerm", pendingTaskQueryConditions.getTaskTerm());
				}
				if(pendingTaskQueryConditions.getFreqId()!=null
						   && !pendingTaskQueryConditions.getFreqId().equals("-1")){
					 parameterMap.put("freqId", pendingTaskQueryConditions.getFreqId());
				}
				if(pendingTaskQueryConditions.getOrgId()!=null &&
						!WorkTaskConfig.LIST_SELECTED_ALL.equals(pendingTaskQueryConditions.getOrgId())){
					 parameterMap.put("OrgId", pendingTaskQueryConditions.getOrgId());
				}
				if(pendingTaskQueryConditions.getLikeOrgName()!=null &&
						!"".equals(pendingTaskQueryConditions.getLikeOrgName())){
					 parameterMap.put("orgName", pendingTaskQueryConditions.getLikeOrgName());
				}
				if(pendingTaskQueryConditions.getTaskMoniId()!=null){
					parameterMap.put("taskMoniId",pendingTaskQueryConditions.getTaskMoniId());
				}
				if(pendingTaskQueryConditions.getNodeFlag()!=null &&
						!pendingTaskQueryConditions.getNodeFlag().equals(new Integer(-1))){
					parameterMap.put("nodeFlag",pendingTaskQueryConditions.getNodeFlag());
				}
				if(pendingTaskQueryConditions.getBusiLine()!=null
						&& !pendingTaskQueryConditions.getBusiLine().equals("-1")){
					parameterMap.put("busiLine", pendingTaskQueryConditions.getBusiLine().trim());
				}
				if(pendingTaskQueryConditions.getCondTypeId()!=null &&
						!"".equals(pendingTaskQueryConditions.getCondTypeId())
						&&!"-1".equals(pendingTaskQueryConditions.getCondTypeId())){
					parameterMap.put("condTypeId", pendingTaskQueryConditions.getCondTypeId());
				}
			}
			StringBuffer hsql_hzrw = new StringBuffer();
			StringBuffer hsql_djsh=new StringBuffer();
			// 最终执行标志finalExecFlag为1
			hsql_hzrw.append("select nMoni from WorkTaskNodeMoni nMoni,WorkTaskMoni moni,WorkTaskNodeInfo ni");
			hsql_djsh.append("select nMoni from WorkTaskNodeMoni nMoni,WorkTaskMoni moni,WorkTaskNodeInfo ni");
			
			ViewWorktaskOrg vOrg = this.findOneWorkTaskorg(orgId);
//			if(vOrg==null){
				hsql_hzrw.append(",ViewWorktaskOrg org");
				hsql_djsh.append(",ViewWorktaskOrg org");
//			}
//			hsql.append("select nMoni from WorkTaskNodeMoni nMoni,WorkTaskMoni moni");
//			if(parameterMap.containsKey("taskName")||
//					parameterMap.containsKey("freqId")){
				hsql_hzrw.append(",WorkTaskInfo taskInfo");
				hsql_djsh.append(",WorkTaskInfo taskInfo");
//			}
			hsql_hzrw.append(" where moni.taskMoniId=nMoni.id.taskMoniId and nMoni.finalExecFlag=1 and moni.execFlag=1");
			hsql_djsh.append(" where moni.taskMoniId=nMoni.id.taskMoniId and nMoni.finalExecFlag=1 and moni.execFlag=1");
			
			hsql_hzrw.append(" and nMoni.id.nodeId=ni.nodeId ");
			hsql_djsh.append(" and nMoni.id.nodeId=ni.nodeId ");
			
//			if(vOrg==null){
				hsql_hzrw.append(" and org.id.orgId=nMoni.id.orgId");
				hsql_djsh.append(" and org.id.orgId=nMoni.id.orgId");
//			}
			
//			if(parameterMap.containsKey("taskName")||
///					parameterMap.containsKey("freqId")){
				hsql_hzrw.append(" and moni.taskId=taskInfo.taskId and taskInfo.taskTypeId='hzrw'");
				hsql_djsh.append(" and moni.taskId=taskInfo.taskId and taskInfo.taskTypeId='djsh'");
//				hsql.append(" and moni.taskId=taskInfo.taskId ");
//			}
			if(parameterMap.containsKey("taskId")){
				hsql_hzrw.append(" and moni.taskId="+pendingTaskQueryConditions.getTaskId());
				hsql_djsh.append(" and moni.taskId="+pendingTaskQueryConditions.getTaskId());
			}
			if(parameterMap.containsKey("taskName")){
				hsql_hzrw.append(" and moni.taskName like '%"+pendingTaskQueryConditions.getTaskName().trim().replaceAll(" ","")+"%'");
				hsql_djsh.append(" and moni.taskName like '%"+pendingTaskQueryConditions.getTaskName().trim().replaceAll(" ","")+"%'");
			}
			if(parameterMap.containsKey("freqId")){
				hsql_hzrw.append(" and taskInfo.freqId='"+pendingTaskQueryConditions.getFreqId().trim()+"'");
				hsql_djsh.append(" and taskInfo.freqId='"+pendingTaskQueryConditions.getFreqId().trim()+"'");
			}
			
			if(parameterMap.containsKey("orgName")){
				hsql_hzrw.append(" and org.id.orgName like '%"+parameterMap.get("orgName")+"%'");
				hsql_djsh.append(" and org.id.orgName like '%"+parameterMap.get("orgName")+"%'");
			}
			
			if(parameterMap.containsKey("nodeFlag")){
				if(ids!=null && !ids.equals("")){
					hsql_hzrw.append(" and nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_COMM);
					hsql_djsh.append(" and nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_COMM);
				}else
					hsql_hzrw.append(" and nMoni.nodeFlag="+pendingTaskQueryConditions.getNodeFlag());
					hsql_djsh.append(" and nMoni.nodeFlag="+pendingTaskQueryConditions.getNodeFlag());
			}
			if(ids!=null && !ids.equals("")){
				hsql_hzrw.append(" and nMoni.id.nodeId in("+ids+") and nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_COMM);
				hsql_djsh.append(" and nMoni.id.nodeId in("+ids+") and nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_COMM);
			}
			if(parameterMap.containsKey("taskMoniId")){
				hsql_hzrw.append(" and nMoni.id.taskMoniId="+pendingTaskQueryConditions.getTaskMoniId());
				hsql_djsh.append(" and nMoni.id.taskMoniId="+pendingTaskQueryConditions.getTaskMoniId());
			}
			if(parameterMap.containsKey("busiLine")){
				hsql_hzrw.append(" and nMoni.busiLine='"+pendingTaskQueryConditions.getBusiLine()+"'");
				hsql_djsh.append(" and nMoni.busiLine='"+pendingTaskQueryConditions.getBusiLine()+"'");
			}
			
			if(parameterMap.containsKey("condTypeId")){
				hsql_hzrw.append(" and ni.condTypeId='"+pendingTaskQueryConditions.getCondTypeId()+"'");
				hsql_djsh.append(" and ni.condTypeId='"+pendingTaskQueryConditions.getCondTypeId()+"'");
			}
			
			String nodeIds = this.getWorkTaskNodes(nodes);
			hsql_hzrw.append(" and nMoni.id.nodeId in("+nodeIds+")");
			hsql_djsh.append(" and nMoni.id.nodeId in("+nodeIds+")");
			
			String orgStr = "";
			if(pendingTaskQueryConditions.getOrgId()!=null
					   &&pendingTaskQueryConditions.getOrgId().trim().equals(WorkTaskConfig.LIST_SELECTED_ALL)){
				List orgList = workTaskOrgService.getChildListByOrgId(orgId,null);
				if(orgList!=null && !orgList.isEmpty()){
					for (int i = 0; i < orgList.size(); i++) {
						String[] strs = (String[]) orgList.get(i);
						if (!"".equals(orgStr)) {
							orgStr+=",";
						}
						orgStr+="'"+strs[0]+"'";
					}
				}
			}
			if(!"".equals(orgStr)){
				orgStr +=",'"+orgId+"'";
			}
			String orgIds =findAllViewWtPurOrgsByRoleIds(userId);//查询当前用户所有的用户组权限 用户id和机构id对应关系
			
			String searchOrgId = "";
			
			if(parameterMap.containsKey("OrgId")){
				searchOrgId+="'"+parameterMap.get("OrgId")+"'";
			}else{
				if(!"".equals(orgIds)){
					if(!"".equals(searchOrgId))
						searchOrgId+=","+orgIds;
					else
						searchOrgId+=orgIds;
				}
				if(!"".equals(orgStr)){
					if(!"".equals(searchOrgId))
						searchOrgId+=","+orgStr;
					else
						searchOrgId+=orgStr;
				}else{
					if(!"".equals(searchOrgId))
						searchOrgId+=",'"+orgId+"'";
					else
						searchOrgId+="'"+orgId+"'";
				}
			}
			
			if(!"".equals(searchOrgId)){
				hsql_hzrw.append(" and nMoni.id.orgId in("+searchOrgId+")");
				hsql_djsh.append(" and nMoni.id.orgId in("+searchOrgId+")");
			}
		
			if(parameterMap.containsKey("taskTerm")){
				hsql_hzrw.append(" and moni.taskTerm=to_date('"+pendingTaskQueryConditions.getTaskTerm().trim()+"','yyyy-MM-dd')");
				hsql_djsh.append(" and moni.taskTerm=to_date('"+pendingTaskQueryConditions.getTaskTerm().trim()+"','yyyy-MM-dd')");
			}
//			if(vOrg==null){
				
				hsql_hzrw.append(" order by org.id.orgLevel,org.id.orgName");
				hsql_djsh.append(" order by org.id.orgLevel,org.id.orgName");
//			}
			List<WorkTaskNodeMoni> wtns_hzrw = this.objectDao.findListByHsql(hsql_hzrw.toString(), null);
			List<WorkTaskNodeMoni> wtns_djsh = this.objectDao.findListByHsql(hsql_djsh.toString(), null);
			parameterMap.clear();
			if(wtns_hzrw==null&&wtns_djsh==null)
				return null;
			List<WorkTaskPendingTaskVo> vos = new ArrayList<WorkTaskPendingTaskVo>();
			
			
	
			
			if(wtns_hzrw!=null && !wtns_hzrw.isEmpty()){
				for(WorkTaskNodeMoni node : wtns_hzrw){
					if(node==null)
						continue;
					WorkTaskPendingTaskVo wto = new WorkTaskPendingTaskVo();
					//add by wmm start 将处于待处理阶段的节点名称取出来
					String curStateHql="select ni.nodeName,ni.nodeId from WorkTaskNodeMoni nMoni,WorkTaskNodeInfo ni where nMoni.id.nodeId=ni.nodeId and nMoni.finalExecFlag=1 and (nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WADC+" or nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REFU+") and nMoni.id.taskMoniId="+node.getId().getTaskMoniId()+"  and nMoni.id.orgId='"+node.getId().getOrgId()+"'";
					List curStateList=this.objectDao.findListByHsql(curStateHql, null);
					for (int i = 0; curStateList!=null&&i < curStateList.size(); i++) {
						Object[] objs=(Object[])curStateList.get(i);
						wto.setCurState((String)objs[0]);
						wto.setCurStateId((Integer)objs[1]);
					}
					//add by end
					wto.setBusiLine(node.getBusiLine());
					wto.setPerformNumber(node.getId().getPerformNumber());
					wto.setLateRepDate(node.getLateRepDate());
					wto.setNodeFlag(node.getNodeFlag());
					Date currentDate = new Date();
					if(currentDate.compareTo(node.getLateRepDate())>0){
						node.setLateRepFlag(WorkTaskConfig.LATE_REP_FLAG_YES);
						this.update(node);
					}
					wto.setLateRepFlag(node.getLateRepFlag());
					wto.setLateRepFlagName(node.getLateRepFlag().equals(WorkTaskConfig.LATE_REP_FLAG_YES)?"迟报":"正常");
					if(pendingTaskQueryConditions!=null){
						if(pendingTaskQueryConditions.getRoleId()!=null)
							wto.setRoleId(pendingTaskQueryConditions.getRoleId());
					}
					String hqlNode= null;
					if(node.getId()!=null &&  node.getId().getOrgId()!=null){
						hqlNode = "select v.id.orgName from ViewWorktaskOrg v where v.id.orgId=:orgId";
						parameterMap.put("orgId", node.getId().getOrgId());
						List list = this.objectDao.findListByHsql(hqlNode, parameterMap);
						if(list!=null && !list.isEmpty()){
							String orgName = (String)list.get(0);
							wto.setOrgName(orgName);
							wto.setOrgId(node.getId().getOrgId());
						}
						hqlNode =null;
						parameterMap.clear();
					}
					WorkTaskMoni moni = workTaskMoniService.findOneWorkTaskMoni(node.getId().getTaskMoniId());
					
					WorkTaskNodeInfo workTaskNodeInfo=null;
					if(node.getId()!=null && node.getId().getNodeId()!=null){
						hqlNode = "from WorkTaskNodeInfo w where w.nodeId="+node.getId().getNodeId();
						Object obj = this.objectDao.findObject(hqlNode);
						if(obj!=null && obj instanceof WorkTaskNodeInfo){
							workTaskNodeInfo = (WorkTaskNodeInfo)obj;
							wto.setTaskNodeName(workTaskNodeInfo.getNodeName());
							wto.setNodeId(workTaskNodeInfo.getNodeId());
							wto.setCondTypeId(findWorkTaskType(workTaskNodeInfo.getCondTypeId()));
						}
					}
					if(node.getNodeFlag()!=null){
						wto.setTaskState(this.getNodeStateName(node.getNodeFlag()));
					}
					if(moni!=null){
						Integer taskId = moni.getTaskId();
						wto.setTaskMoniId(moni.getTaskMoniId());
						wto.setTaskName(moni.getTaskName());
						if(taskId!=null){
							hqlNode = "from WorkTaskInfo w where w.taskId="+taskId;
							WorkTaskInfo workTaskInfo = (WorkTaskInfo)this.objectDao.findObject(hqlNode);
							if(workTaskInfo!=null){
								wto.setTaskId(taskId);
								//wto.setTaskName(workTaskInfo.getTaskName());
								String freqId = workTaskInfo.getFreqId();
								wto.setFreqId(getFreqIdName(freqId));
								wto.setWorkTypeId(workTaskInfo.getTaskTypeId());
							}
						}
						wto.setTaskTerm(moni.getTaskTerm());
					}
					vos.add(wto);
				}
			}
			if(wtns_djsh!=null && !wtns_djsh.isEmpty()){

				for(WorkTaskNodeMoni node : wtns_djsh){
					if(node==null)
						continue;
					WorkTaskPendingTaskVo wto = new WorkTaskPendingTaskVo();
					//add by wmm start 将处于待处理阶段的节点名称取出来
					String curStateHql="select ni.nodeName,ni.nodeId from WorkTaskNodeMoni nMoni,WorkTaskNodeInfo ni where nMoni.id.nodeId=ni.nodeId and nMoni.finalExecFlag=1 and (nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WADC+" or nMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REFU+") and nMoni.id.taskMoniId="+node.getId().getTaskMoniId()+"  and nMoni.id.orgId='"+node.getId().getOrgId()+"'";
					List curStateList=this.objectDao.findListByHsql(curStateHql, null);
					for (int i = 0; curStateList!=null&&i < curStateList.size(); i++) {
						Object[] objs=(Object[])curStateList.get(i);
						
						wto.setCurState((String)objs[0]);
						wto.setCurStateId((Integer)objs[1]);
					}
					//add by end
					wto.setBusiLine(node.getBusiLine());
					wto.setPerformNumber(node.getId().getPerformNumber());
					wto.setLateRepDate(node.getLateRepDate());
					wto.setNodeFlag(node.getNodeFlag());
					Date currentDate = new Date();
					if(currentDate.compareTo(node.getLateRepDate())>0){
						node.setLateRepFlag(WorkTaskConfig.LATE_REP_FLAG_YES);
						this.update(node);
					}
					wto.setLateRepFlag(node.getLateRepFlag());
					wto.setLateRepFlagName(node.getLateRepFlag().equals(WorkTaskConfig.LATE_REP_FLAG_YES)?"迟报":"正常");
					if(pendingTaskQueryConditions!=null){
						if(pendingTaskQueryConditions.getRoleId()!=null)
							wto.setRoleId(pendingTaskQueryConditions.getRoleId());
					}
					String hqlNode= null;
					if(node.getId()!=null &&  node.getId().getOrgId()!=null){
						hqlNode = "select v.id.orgName from ViewWorktaskOrg v where v.id.orgId=:orgId";
						parameterMap.put("orgId", node.getId().getOrgId());
						List list = this.objectDao.findListByHsql(hqlNode, parameterMap);
						if(list!=null && !list.isEmpty()){
							String orgName = (String)list.get(0);
							wto.setOrgName(orgName);
							wto.setOrgId(node.getId().getOrgId());
						}
						hqlNode =null;
						parameterMap.clear();
					}
					WorkTaskMoni moni = workTaskMoniService.findOneWorkTaskMoni(node.getId().getTaskMoniId());
					
					WorkTaskNodeInfo workTaskNodeInfo=null;
					if(node.getId()!=null && node.getId().getNodeId()!=null){
						hqlNode = "from WorkTaskNodeInfo w where w.nodeId="+node.getId().getNodeId();
						Object obj = this.objectDao.findObject(hqlNode);
						if(obj!=null && obj instanceof WorkTaskNodeInfo){
							workTaskNodeInfo = (WorkTaskNodeInfo)obj;
							wto.setTaskNodeName(workTaskNodeInfo.getNodeName());
							wto.setNodeId(workTaskNodeInfo.getNodeId());
							wto.setCondTypeId(findWorkTaskType(workTaskNodeInfo.getCondTypeId()));
						}
					}
					if(node.getNodeFlag()!=null){
						wto.setTaskState(this.getNodeStateName(node.getNodeFlag()));
					}
					if(moni!=null){
						Integer taskId = moni.getTaskId();
						wto.setTaskMoniId(moni.getTaskMoniId());
						wto.setTaskName(moni.getTaskName());
						if(taskId!=null){
							hqlNode = "from WorkTaskInfo w where w.taskId="+taskId;
							WorkTaskInfo workTaskInfo = (WorkTaskInfo)this.objectDao.findObject(hqlNode);
							if(workTaskInfo!=null){
								wto.setTaskId(taskId);
								//wto.setTaskName(workTaskInfo.getTaskName());
								String freqId = workTaskInfo.getFreqId();
								wto.setFreqId(getFreqIdName(freqId));
								wto.setWorkTypeId(workTaskInfo.getTaskTypeId());
							}
						}
						wto.setTaskTerm(moni.getTaskTerm());
					}
					vos.add(wto);
				}
			
			}
//			if(prs==null){
				prs=new PageResults<WorkTaskPendingTaskVo>();
				prs.setPageSize(pageResults.getPageSize());
				prs.setCurrentPage(pageResults.getCurrentPage());
//			}
			prs.setResults(vos);
		}
		if(prs.getResults()!=null && prs.getResults().size()!=0){
			findLastNodeRepDate(prs.getResults());//获取上一个节点提交日期
		}
		return prs;
	}
	/**
	 *  根据任务列表获取上一个节点提交日期
	  * @Title: findLastNodeRepDate 
	  * @date: Oct 30, 2014  11:32:20 AM
	  * @param vos
	  * @throws Exception void
	 */
	public void findLastNodeRepDate(List<WorkTaskPendingTaskVo> vos)throws Exception{
		StringBuffer tmids = new StringBuffer("");
		for (int i = 0; i < vos.size(); i++) {
			WorkTaskPendingTaskVo wtpt = vos.get(i);
			tmids.append("(nm.id.taskMoniId ="+ wtpt.getTaskMoniId().intValue()+")");
			if(i!=vos.size()-1){
				tmids.append(" or ");
			}
		}
		String hsql = "from WorkTaskNodeMoni nm where nm.finalExecFlag=1 and " + tmids+ ")";
		System.out.println(hsql);
		List<WorkTaskNodeMoni> moniList = this.findListByHsql(hsql, null);
		for(WorkTaskPendingTaskVo vo : vos){
			int taskMoniId = vo.getTaskMoniId().intValue();
			int nodeId = vo.getNodeId().intValue();
			int lastNodeId = nodeId - 1;
			String orgId = vo.getOrgId();
			for(WorkTaskNodeMoni moni:moniList){
				if(moni.getId().getTaskMoniId().intValue() == taskMoniId 
						&& moni.getId().getNodeId().intValue() == lastNodeId
						&& moni.getId().getOrgId().trim().equals(orgId.trim())){
					vo.setLastNodeDate(moni.getEndDate());
					break;
				}
			}
		}
	}
	public String findWorkTaskType(String condTypeId){
		String hsql = "select wt.condTypeName from WorkTaskCondType wt where wt.condTypeId='"+condTypeId+"'";
		return (String)this.objectDao.findObject(hsql);
	}
	
	@Override
	public List<WorkTaskInfo> findAllWorkTaskInfosByParams(Map parameterMap,
			PendingTaskQueryConditions pendingTaskQueryConditions,
			PageResults pageResults, List<WorkTaskNodeRole> nodes, Long userId)
			throws Exception {
		// TODO Auto-generated method stub
		StringBuffer hsql = new StringBuffer();
		// 最终执行标志finalExecFlag为1
		hsql.append("select tInfo from WorkTaskInfo tInfo where tInfo.taskId in(");
		hsql.append("select moni.taskId from WorkTaskNodeMoni nMoni,WorkTaskMoni moni");
		hsql.append(" where moni.taskMoniId=nMoni.id.taskMoniId and nMoni.finalExecFlag=1 and moni.execFlag=1");
		String nodeIds = this.getWorkTaskNodes(nodes);
		hsql.append(" and nMoni.id.nodeId in("+nodeIds+")");
		if(pendingTaskQueryConditions!=null){
			if(pendingTaskQueryConditions.getBusiLine()!=null &&
					!"-1".equals(pendingTaskQueryConditions.getBusiLine())){
				hsql.append(" and nMoni.busiLine='"+pendingTaskQueryConditions.getBusiLine()+"'");
			}
			if(pendingTaskQueryConditions.getTaskId()!=null &&
					!new Integer(-1).equals(pendingTaskQueryConditions.getTaskId())){
				hsql.append(" and moni.taskId="+pendingTaskQueryConditions.getTaskId());
			}
		}
		hsql.append(")");
		return this.objectDao.findListByHsql(hsql.toString(), null);
	}

	/**
	 * 返回频度名称
	 * @param freqId
	 * @return
	 */	
	private String getFreqIdName(String freqId){
		String freqIdName="";
		if(freqId.equalsIgnoreCase(WorkTaskConfig.FREQ_DAY)){
			freqIdName = "日";
		}else if(freqId.equalsIgnoreCase(WorkTaskConfig.FREQ_HALF_YEAR)){
			freqIdName = "半年";
		}else if(freqId.equalsIgnoreCase(WorkTaskConfig.FREQ_MONTH)){
			freqIdName = "月";
		}else if(freqId.equalsIgnoreCase(WorkTaskConfig.FREQ_SEASON)){
			freqIdName = "季";
		}else if(freqId.equalsIgnoreCase(WorkTaskConfig.FREQ_YEAR)){
			freqIdName = "年";
		}else if(freqId.equalsIgnoreCase(WorkTaskConfig.FREQ_YEAR_BEGIN_CARRY)){
			freqIdName="年初结转";
		}
		return freqIdName;
	}
	/**
	 * 返回节点状态名称
	 * @param nodeFlag
	 * @return
	 */
	private String getNodeStateName(Integer nodeFlag){
		String stateName="";
		switch(nodeFlag){
		case WorkTaskConfig.NODE_FLAG_COMM:
			stateName = "已提交";
			break;
		case WorkTaskConfig.NODE_FLAG_PASS:
			stateName = "审核通过";
			break;
		case WorkTaskConfig.NODE_FLAG_REFU:
			stateName = "审核未通过";
			break;
		case WorkTaskConfig.NODE_FLAG_REWA:
			stateName = "退回等待";
			break;
		case WorkTaskConfig.NODE_FLAG_WADC:
			stateName = "待处理";
			break;
		case WorkTaskConfig.NODE_FLAG_WAIT:
			stateName = "等待";
			break;
		}
		return stateName;
	}
	
	public List<WorkTaskNodeMoni> createWorkTaskNodeMoni(String[] strs)throws Exception{
		List<WorkTaskNodeMoni> monis = null;
		if(strs!=null && strs.length>0){
			monis = new ArrayList<WorkTaskNodeMoni>();
			for(String str : strs){
				if(str!=null && !str.equals("")){
					String[] ats = str.split(",");
					//[taskMoniId,nodeId,orgId,busiLine,lateRepDate]ats数组中存放的元素
					if(ats!=null && ats.length>0){
						
						WorkTaskNodeMoni moni = new WorkTaskNodeMoni();
						WorkTaskNodeMoniId moniId = new WorkTaskNodeMoniId();
						moni.setBusiLine(ats[3]);
						moni.setLateRepDate(new SimpleDateFormat("yyyy-mm-dd").parse(ats[4]));
						moniId.setNodeId(Integer.parseInt(ats[1]));
						moniId.setOrgId(ats[2]);
						moniId.setTaskMoniId(Long.valueOf(ats[0]));
						moni.setId(moniId);
						monis.add(moni);
					}
				}
			}
		}
		return monis;
	}
	/**
	 *将所有的节点角色对象的节点id拼接成(1,2,3)形式便于查询
	 * @param nodes
	 * @return
	 */
	public String getWorkTaskNodes(List<WorkTaskNodeRole> nodes)throws Exception{
		String str = "";
		if(nodes!=null && nodes.size()>0){
			for(WorkTaskNodeRole nodeRole : nodes){
				if(nodeRole.getId()!=null)
					str+=nodeRole.getId().getNodeId()+",";
			}
			if(!str.equals(""))
				str = str.substring(0,str.length()-1);
		}else
			str="-1";
		return str;
	}

	public WorkTaskMoni findOneWorkTaskMoni(Long taskMoniId)throws Exception{
		String hql = "from WorkTaskMoni moni where moni.taskMoniId="+taskMoniId;
		return (WorkTaskMoni)this.objectDao.findObject(hql);
	}
	/**
	 * 0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id,6:任务频度,7:任务状态,8:节点id
	 */
	public String[] findByParamsTemplateIds(WorkTaskNodeMoni moni)
			throws Exception {
		// TODO Auto-generated method stub
		WorkTaskNodeMoni nMoni = null;
		String[] strs= {"","","","","","","","",""};
		WorkTaskConfig.LENGTH = strs.length;
		if(moni!=null){
			nMoni = this.findOneWorkTaskNodeMoni(moni);
			strs[5] = nMoni.getId().getOrgId();
			if(nMoni!=null){
				WorkTaskNodeInfo nodeInfo  = findOneWorkTaskNodeInfo(nMoni.getId().getNodeId());
				if(nodeInfo!=null){
					strs[8] = String.valueOf(nodeInfo.getPreNodeId());
					if(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL.equals(nodeInfo.getCondTypeId())
							&& new Integer(WorkTaskConfig.NODE_FLAG_WAIT).equals(nMoni.getNodeFlag())){
						String hsql = "from WorkTaskNodeInfo wi where wi.preNodeId="+nodeInfo.getNodeId();
						WorkTaskNodeInfo nInfo = (WorkTaskNodeInfo)this.objectDao.findObject(hsql);
						if(nInfo!=null){
							nMoni.getId().setNodeId(nInfo.getNodeId());
							nMoni = this.findOneWorkTaskNodeMoni(nMoni);
						}
					}
				}
				if(nMoni!=null){
					strs[7] = nMoni.getNodeFlag()+"";
					String hql = "select om.id.templateId from WorkTaskNodeObjectMoni om where om.id.taskMoniId="+nMoni.getId().getTaskMoniId()+
						" and om.id.nodeId="+nMoni.getId().getNodeId()+" and om.id.orgId='"+nMoni.getId().getOrgId()+"' and om.id.nodeIoFlag="+WorkTaskConfig.NODE_IO_FLAG_EXPORT;
					List list = this.objectDao.findListByHsql(hql, null);
					for (int i = 0; i < list.size(); i++) {
						String str = (String)list.get(i);
						if(str!=null && !strs.equals(""))
							strs[0] += str+","; 
					}
					if(strs[0]!=null && !strs[0].equals("")){
						strs[0] = strs[0].substring(0,strs[0].length()-1);
						strs[1] = nMoni.getBusiLine();
						WorkTaskMoni mi = this.findOneWorkTaskMoni(nMoni.getId().getTaskMoniId());
						WorkTaskNodeInfo nInfo = this.findOneWorkTaskNodeInfo(nMoni.getId().getNodeId());
						String condTypeId = nInfo.getCondTypeId();
						if(mi!=null){
							hql = "from WorkTaskInfo wInfo where wInfo.taskId = "+mi.getTaskId();
							WorkTaskInfo wInfo = (WorkTaskInfo)this.findObject(hql);
							if(nMoni.getNodeFlag()!=null && !nMoni.getNodeFlag().equals(WorkTaskConfig.NODE_FLAG_REFU)
									&& !nMoni.getNodeFlag().equals(WorkTaskConfig.NODE_FLAG_WADC)){
								condTypeId = WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC;
							}
							if(wInfo!=null){
								strs[6] = getParseFreqId(wInfo.getFreqId());
								strs[2] = getTaskTerm(mi.getTaskTerm());
								hql = "from WorkTaskNodeConductType wtype where wtype.id.busiLineId='"+wInfo.getBusiLineId()
										+"' and wtype.id.condTypeId='"+condTypeId+"'";
								WorkTaskNodeConductType wType=(WorkTaskNodeConductType)this.objectDao.findObject(hql);
								if(wType!=null){
									strs[3]= wType.getNodeUrl();
									strs[4]=wType.getFormName();
								}
							}
						}
					}else
						strs=null;
				}
			}
		}
		return strs;
	}

	public String getParseFreqId(String freqId){
		String[][] freqIds =new String[][]{{WorkTaskConfig.FREQ_HALF_YEAR,"3"}
			,{WorkTaskConfig.FREQ_MONTH,"1"},{WorkTaskConfig.FREQ_SEASON,"2"}
			,{WorkTaskConfig.FREQ_YEAR,"4"},{WorkTaskConfig.FREQ_YEAR_BEGIN_CARRY,"9"},
			{WorkTaskConfig.FREQ_DAY,"6"}};
		if(freqId!=null && !freqId.equals("")){
			if(freqIds!=null && freqIds.length>0){
				for(String[] s1s : freqIds){
					if(s1s[0].equals(freqId))
							return s1s[1];
				}
			}
//			if(freqId.equals(WorkTaskConfig.FREQ_DAY)){
//				return "";
//			}else if(freqId.equals(WorkTaskConfig.FREQ_HALF_YEAR)){
//				return "3";
//			}else if(freqId.equals(WorkTaskConfig.FREQ_MONTH)){
//				return "1";
//			}else if(freqId.equals(WorkTaskConfig.FREQ_SEASON)){
//				return "2";
//			}else if(freqId.equals(WorkTaskConfig.FREQ_YEAR)){
//				return "4";
//			}
		}
		return null;
	}
	
	@Override
	public List<WorkTaskIndexVo> findAllWorkTask(PendingTaskQueryConditions pc,
				List<WorkTaskNodeRole> nodes,Long userId,String orgId)
			throws Exception {
		// TODO Auto-generated method stub
		List<WorkTaskIndexVo> wos=null;
		// add by wmm start
		StringBuffer sb=new StringBuffer();
		String taskType="";
		sb.append(" select distinct wti.taskTypeId from ");
		sb.append(" WorkTaskInfo wti,WorkTaskNodeInfo wtni,WorkTaskNodeObject wtno,WorkTaskNodeRole nr ");
		sb.append(" where wti.taskId=wtni.taskId  and wtni.nodeId=wtno.id.nodeId and nr.id.nodeId=wtno.id.nodeId and wtno.id.nodeIoFlag=1 and nr.id.roleId in("+pc.getRoleIds()+") ");
		try {
			List list=this.objectDao.findListByHsql(sb.toString(), null);
			for (int i = 0; i < list.size(); i++) {
				String taskTypeId=(String)list.get(i);
				if(!"".equals(taskType)){
					taskType+=",";
				}
				 taskType+=taskTypeId;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add by wmm end
		if(taskType.contains("hzrw")&&!taskType.contains("djsh")){
			if(nodes!=null && nodes.size()>0){
				wos = new ArrayList<WorkTaskIndexVo>();
				StringBuffer hsql = new StringBuffer();
				hsql.append("select distinct wMoni.nodeFlag,count(wMoni.nodeFlag),moni.taskTerm");
				hsql.append(" from WorkTaskNodeMoni wMoni,WorkTaskMoni moni");
				hsql.append(" where wMoni.id.taskMoniId = moni.taskMoniId ");
				hsql.append(" and wMoni.id.nodeId in("+this.getWorkTaskNodes(nodes)+")");
				hsql.append(" and moni.execFlag=1");
				hsql.append(" and (wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WADC);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WAIT);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REWA);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REFU+")");
				if(pc!=null && pc.getBusiLine()!=null
						&& !pc.getBusiLine().equals("")){
					hsql.append(" and wMoni.busiLine='"+pc.getBusiLine()+"'");
				}
				ViewWorktaskOrg vOrg = this.findOneWorkTaskorg(orgId);
				if(pc!=null && pc.getOrgId()!=null
						&& !pc.getOrgId().equals(WorkTaskConfig.LIST_SELECTED_ALL) && vOrg==null){
					
					hsql.append(" and wMoni.id.orgId='"+pc.getOrgId()+"'");
				}
				if(vOrg==null){
					String orgIds =findAllViewWtPurOrgsByRoleIds(userId);
					if(orgIds.equals(""))
						orgIds = "-1";
					hsql.append(" and wMoni.id.orgId in("+orgIds+")");
				}
				hsql.append(" and wMoni.finalExecFlag="+WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
				hsql.append(" group by wMoni.nodeFlag,moni.taskTerm");
				hsql.append(" order by moni.taskTerm");
				
				List list = this.findListByHsql(hsql.toString(), null);
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[])list.get(i);
						WorkTaskIndexVo wo = new WorkTaskIndexVo();
						wo.setNodeFlag((Integer)objs[0]);
						wo.setTaskNum((Long)objs[1]);
						wo.setTaskDate(((Date)objs[2]));
						wos.add(wo);
					}
				}
			}
		}else if(!taskType.contains("hzrw")&&taskType.contains("djsh")){
			if(nodes!=null && nodes.size()>0){
				wos = new ArrayList<WorkTaskIndexVo>();
				StringBuffer hsql = new StringBuffer();
				hsql.append("select distinct wMoni.nodeFlag,count(wMoni.nodeFlag),moni.taskTerm");
				hsql.append(" from WorkTaskNodeMoni wMoni,WorkTaskMoni moni");
				hsql.append(" where wMoni.id.taskMoniId = moni.taskMoniId ");
				hsql.append(" and wMoni.id.nodeId in("+this.getWorkTaskNodes(nodes)+")");
				hsql.append(" and moni.execFlag=1");
				hsql.append(" and (wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WADC);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WAIT);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REWA);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REFU+")");
				if(pc!=null && pc.getBusiLine()!=null
						&& !pc.getBusiLine().equals("")){
					hsql.append(" and wMoni.busiLine='"+pc.getBusiLine()+"'");
				}
				ViewWorktaskOrg vOrg = this.findOneWorkTaskorg(orgId);
				
				if(vOrg==null){
					String orgIds =findAllViewWtPurOrgsByRoleIds(userId);
					if(orgIds.equals(""))
						orgIds = "-1";
					hsql.append(" and (");
					hsql.append(" wMoni.id.orgId in("+orgIds+")");
					if(pc!=null && pc.getOrgId()!=null
							&& !pc.getOrgId().equals(WorkTaskConfig.LIST_SELECTED_ALL)&&vOrg==null){
						
						hsql.append(" or wMoni.id.orgId='"+pc.getOrgId()+"'");
					}
					hsql.append(")");
				}
				hsql.append(" and wMoni.finalExecFlag="+WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
				hsql.append(" group by wMoni.nodeFlag,moni.taskTerm");
				hsql.append(" order by moni.taskTerm");
				
				List list = this.findListByHsql(hsql.toString(), null);
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[])list.get(i);
						WorkTaskIndexVo wo = new WorkTaskIndexVo();
						wo.setNodeFlag((Integer)objs[0]);
						wo.setTaskNum((Long)objs[1]);
						wo.setTaskDate(((Date)objs[2]));
						wos.add(wo);
					}
				}
			}
		}else{
			if(nodes!=null && nodes.size()>0){
				wos = new ArrayList<WorkTaskIndexVo>();
				StringBuffer hsql = new StringBuffer();
				hsql.append("select distinct wMoni.nodeFlag,count(wMoni.nodeFlag),moni.taskTerm");
				hsql.append(" from WorkTaskNodeMoni wMoni,WorkTaskMoni moni");
				hsql.append(" where wMoni.id.taskMoniId = moni.taskMoniId ");
				hsql.append(" and wMoni.id.nodeId in("+this.getWorkTaskNodes(nodes)+")");
				hsql.append(" and moni.execFlag=1");
				hsql.append(" and (wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WADC);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_WAIT);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REWA);
				hsql.append(" or wMoni.nodeFlag="+WorkTaskConfig.NODE_FLAG_REFU+")");
				if(pc!=null && pc.getBusiLine()!=null
						&& !pc.getBusiLine().equals("")){
					hsql.append(" and wMoni.busiLine='"+pc.getBusiLine()+"'");
				}
				ViewWorktaskOrg vOrg = this.findOneWorkTaskorg(orgId);
				if(pc!=null && pc.getOrgId()!=null
						&& !pc.getOrgId().equals(WorkTaskConfig.LIST_SELECTED_ALL)&&vOrg==null){
					
					hsql.append(" and wMoni.id.orgId='"+pc.getOrgId()+"'");
				}
				if(vOrg==null){
					String orgIds =findAllViewWtPurOrgsByRoleIds(userId);
					if(orgIds.equals(""))
						orgIds = "-1";
					hsql.append(" and wMoni.id.orgId in("+orgIds+")");
				}
				hsql.append(" and wMoni.finalExecFlag="+WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
				hsql.append(" group by wMoni.nodeFlag,moni.taskTerm");
				hsql.append(" order by moni.taskTerm");
				
				List list = this.findListByHsql(hsql.toString(), null);
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[])list.get(i);
						WorkTaskIndexVo wo = new WorkTaskIndexVo();
						wo.setNodeFlag((Integer)objs[0]);
						wo.setTaskNum((Long)objs[1]);
						wo.setTaskDate(((Date)objs[2]));
						wos.add(wo);
					}
				}
			}
		}
		return wos;
	}

	@Override
	public List<WorkTaskPendingTaskVo> findPendingTaskVos(String[] strs)
			throws Exception {
		// TODO Auto-generated method stub
		List<WorkTaskPendingTaskVo> pvos = new ArrayList<WorkTaskPendingTaskVo>();
		List<WorkTaskNodeMoni> monis = createWorkTaskNodeMoni(strs);
		if(monis!=null && !monis.isEmpty()){
			for(WorkTaskNodeMoni mi : monis){
				mi = findOneWorkTaskNodeMoni(mi);
				WorkTaskPendingTaskVo vo = new WorkTaskPendingTaskVo();
				if(mi!=null){
					String hql ="from WorkTaskMoni moni where moni.taskMoniId="+mi.getId().getTaskMoniId();
					WorkTaskMoni moni = (WorkTaskMoni)this.objectDao.findObject(hql);
					if(moni!=null){
						vo.setTaskId(moni.getTaskId());
						vo.setTaskTerm(moni.getTaskTerm());
						if(moni.getTaskTerm()!=null){
							Calendar cl = Calendar.getInstance();
							cl.setTime(moni.getTaskTerm());
							vo.setDay(cl.get(Calendar.DATE));
							vo.setYear(cl.get(Calendar.YEAR));
							vo.setTerm(cl.get(Calendar.MONTH)+1);
						}
					}
					vo.setBusiLine(mi.getBusiLine());
					vo.setTaskMoniId(mi.getId().getTaskMoniId());
					vo.setNodeFlag(mi.getNodeFlag());
					vo.setNodeId(mi.getId().getNodeId());
					vo.setOrgId(mi.getId().getOrgId());
					vo.setRoleId(mi.getRoleId());
				}
				String[] templateIds = findByParamsTemplateIds(mi);
				if(strs!=null && strs[0]!=null){
					vo.setTemplateIds(templateIds[0]);
				}
				pvos.add(vo);
			}
		}
		return pvos;
	}
	
	private String getTaskTerm(Date taskTrem){
		String taskTermDate = "";
		if(taskTrem!=null){
			taskTermDate = DateUtil.toSimpleDateFormat(taskTrem, DateUtil.NORMALDATE);
		}
		return taskTermDate;
	}

	public boolean findPowTypeByOrgId(String orgId,Integer nodeId,Long userId)throws Exception{
		if(orgId!=null && !orgId.equals("")
				&& nodeId!=null && userId!=null){
			String hsql = "select vPurOrg.id.powType from ViewWorkTaskPurOrg vPurOrg where vPurOrg.id.orgId='"+orgId+"'"+" and vPurOrg.id.userId="+userId;
			List<Long> list = (List<Long>)this.objectDao.findListByHsql(hsql, null);
			if(list!=null && !list.isEmpty()){
				hsql = "from WorkTaskNodeInfo nInfo where nInfo.nodeId="+nodeId;
				WorkTaskNodeInfo nInfo = (WorkTaskNodeInfo)this.findObject(hsql);
				if(nInfo!=null){//如果当前节点类型是填报，则应该powtype为3
					if(nInfo.getCondTypeId().equals(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL)){
						if(list.contains(Long.valueOf(WorkTaskConfig.POWERTYPEREPORT+"")))
							return true;
					}//如果当前节点类型是复核，则应该powtype为1
					else if(nInfo.getCondTypeId().equals(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC)){
						if(list.contains(Long.valueOf(WorkTaskConfig.POWERTYPECHECK)))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public String findAllViewWtPurOrgsByRoleIds(Long userId)
			throws Exception {
		// TODO Auto-generated method stub
		String str = "";
		if(userId!=null){
			String hsql = "select vto.id.orgId from ViewWorkTaskPurOrg vto where vto.id.userId in("+userId+") group by vto.id.orgId";
			List list = this.objectDao.findListByHsql(hsql, null);
			if(list!=null && !list.isEmpty()){
				for (int i = 0; i < list.size(); i++) {
					String orgId = (String)list.get(i);
					str +="'"+orgId+"',";
				}
				str = str.substring(0, str.length()-1);
			}
		}
		return str;
	}

	@Override
	public List<WorkTaskNodeInfoVo> findAllNodeMonisByTaskMoniId(Integer taskId)
			throws Exception {
		// TODO Auto-generated method stub
		List<WorkTaskNodeInfoVo> pvos = null;
		if(taskId!=null){
			pvos = new ArrayList<WorkTaskNodeInfoVo>();
			String hsql = "from WorkTaskNodeInfo nodeInfo where nodeInfo.taskId="+taskId+" order by nodeInfo.nodeId";
			List<WorkTaskNodeInfo> nodeInfos = this.objectDao.findListByHsql(hsql, null);
			if(nodeInfos!=null && !nodeInfos.isEmpty()){
				for(WorkTaskNodeInfo nodeInfo : nodeInfos){
					WorkTaskNodeInfoVo pvo = new WorkTaskNodeInfoVo();
					pvo.setTaskNodeName(nodeInfo.getNodeName());
					pvo.setNodeId(nodeInfo.getNodeId());
					pvos.add(pvo);
				}
			}
		}
		return pvos;
	}

	@Override
	public ViewWorktaskOrg findOneWorkTaskorg(String orgId) throws Exception {
		// TODO Auto-generated method stub
		ViewWorktaskOrg vwo=null;
		ViewWorktaskOrgId vwoi=null;
		if(orgId!=null && !orgId.equals("")){
			String hql = "select vo.org_id,vo.org_name,vo.pre_org_Id,vo.region_id,vo.set_org_id,vo.org_level,vo.org_type,vo.begin_date,vo.org_attr,vo.org_outer_id,vo.is_collect from View_Worktask_Org vo where vo.pre_Org_Id='0' and "+
						" vo.org_Level=1 and vo.is_Collect=0 and vo.org_id='"+orgId+"'";
			List list   = workTaskOrgService.findListBySql(hql, null);
			for(int i=0;list!=null&&i<list.size();i++){
				vwo=new ViewWorktaskOrg();
				vwoi=new ViewWorktaskOrgId();
				Object[] obj= (Object[])list.get(0);
				vwoi.setOrgId((String)obj[0]);
				vwoi.setOrgName((String)obj[1]);
				vwoi.setPreOrgId((String)obj[2]);
				vwoi.setRegionId(((BigDecimal)obj[3]).longValue());
				vwoi.setSetOrgId((String)obj[4]);
				vwoi.setOrgLevel(((BigDecimal)obj[5]).toString());
				vwoi.setOrgType((String)obj[6]);
				vwoi.setBeginDate((String)obj[7]);
				vwoi.setOrgAttr((String)obj[8]);
				vwoi.setOrgOuterId((String)obj[9]);
				vwoi.setIsCollect(((BigDecimal)obj[10]).intValue());
				vwo.setId(vwoi);
			}
		}
	return vwo;
	}

	@Override
	public String findRepTaskInfoNodeIds()
			throws Exception {
		// TODO Auto-generated method stub
		String str = "";
		String hql ="from WorkTaskNodeInfo nInfo where nInfo.nodeId not in("+
		 			" select preNodeId from WorkTaskNodeInfo)";
		List<WorkTaskNodeInfo> infos = this.objectDao.findListByHsql(hql, null);
		if(infos!=null && !infos.isEmpty()){
			for(WorkTaskNodeInfo nodeInfo : infos){
				str +=nodeInfo.getNodeId()+",";
			}
			if(!str.equals(""))
				str = str.substring(0, str.length()-1);
		}
		return str;
	}
	
	private void putTaskNode(List<WorkTaskNodeMoni> srcMonis,WorkTaskNodeMoni param){
		WorkTaskNodeMoni tMoni = findTaskNode(srcMonis,param);
		if(tMoni==null){
			srcMonis.add(param);
			return ;
		}else{
			findTaskNode(srcMonis, param);
		}
	}
	
	private WorkTaskNodeMoni findTaskNode(List<WorkTaskNodeMoni> srcMonis,WorkTaskNodeMoni param){
		if(srcMonis!=null && !srcMonis.isEmpty()){
			for (int i = 0; i < srcMonis.size(); i++) {
				WorkTaskNodeMoni moni = srcMonis.get(i);
				if(moni.getId().getTaskMoniId().equals(param.getId().getTaskMoniId())
						&& moni.getId().getOrgId().equals(param.getId().getOrgId())
						&& moni.getPreNodeId().equals(param.getId().getNodeId())){
					return moni;
				}
			}
		}
		return null;
	}

	@Override
	public boolean saveRepTaskInfo(List<WorkTaskPendingTaskVo> pvos,
			PendingTaskQueryConditions pendingTaskQueryConditions,Integer repDay)
			throws Exception {
		// TODO Auto-generated method stub
		boolean res =false;
		if(pvos!=null && pvos.size()>0){
			for(WorkTaskPendingTaskVo pvo : pvos){
				WorkTaskNodeMoni mi = new WorkTaskNodeMoni();
				WorkTaskNodeMoniId miId = new WorkTaskNodeMoniId();
				miId.setNodeId(pvo.getNodeId());
				miId.setOrgId(pvo.getOrgId());
				miId.setTaskMoniId(pvo.getTaskMoniId());
				mi.setId(miId);
				
				WorkTaskNodeMoni conditioMoni = findOneWorkTaskNodeMoni(mi);
				List<WorkTaskNodeMoni> backMonis = new ArrayList<WorkTaskNodeMoni>();
				findAllPreTask(conditioMoni, backMonis);
				if(backMonis!=null && !backMonis.isEmpty()){
					for(WorkTaskNodeMoni backMoni : backMonis){
						WorkTaskNodeMoni wnm = new WorkTaskNodeMoni();
						WorkTaskNodeMoniId wnmId = new WorkTaskNodeMoniId();
						BeanUtils.copyProperties(wnm, backMoni);
						BeanUtils.copyProperties(wnmId,backMoni.getId());
						wnm.setId(wnmId);
						
						if(backMoni.getPreNodeId().equals(WorkTaskConfig.PRE_OBJECT_NULL)){
							wnm.setNodeFlag(WorkTaskConfig.NODE_FLAG_REFU);//设置当前节点状态为不通过
							//对当前审核不通过的任务执行短信提醒操作
							WorkTaskMoni taskMoni = this.findOneWorkTaskMoni(wnm.getId().getTaskMoniId());
							pendingTaskService.performByNodeId(wnm.getId().getNodeId()
									,DateUtil.toSimpleDateFormat(taskMoni.getTaskTerm(), DateUtil.NORMALDATE)
									,wnm.getId().getOrgId());
						}else{
							wnm.setNodeFlag(WorkTaskConfig.NODE_FLAG_REWA);//设置当前节点状态为退回等待
						}
						if(repDay!=null){
							if(WorkTaskConfig.REPORT_TIME_UNIT_DAY.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
								wnm.setLateRepDate(DateUtil.getNextDay(wnm.getLateRepDate(), repDay));
							}else if(WorkTaskConfig.REPORT_TIME_UNIT_HOUR.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
								Calendar cd = Calendar.getInstance();
								cd.setTime(wnm.getLateRepDate());
								cd.add(Calendar.HOUR, repDay);
								wnm.setLateRepDate(cd.getTime());
							}
						}
						wnm.setRerepFlag(WorkTaskConfig.REREP_FLAG_YES);//设置重报标志为1
						wnm.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
						wnm.setStartDate(new Timestamp(System.currentTimeMillis())); //设置此节点任务的开始时间
						wnm.getId().setPerformNumber(wnm.getId().getPerformNumber()+1); //将执行次数加一
						wnm.setReturnDesc(pendingTaskQueryConditions.getReturnDesc());
						this.objectDao.save(wnm);
						
						backMoni.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_NOT); //将原来的节点任务的执行状态更新为0
						this.objectDao.update(backMoni);
					}
				}
				WorkTaskNodeMoni wtMoni = new WorkTaskNodeMoni();
				WorkTaskNodeMoniId wtMoniId = new WorkTaskNodeMoniId();
				BeanUtils.copyProperties(wtMoni, conditioMoni);
				BeanUtils.copyProperties(wtMoniId,conditioMoni.getId());
				wtMoni.setId(wtMoniId);
				
				if(repDay!=null){
					if(WorkTaskConfig.REPORT_TIME_UNIT_DAY.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
						wtMoni.setLateRepDate(DateUtil.getNextDay(wtMoni.getLateRepDate(), repDay));
					}else if(WorkTaskConfig.REPORT_TIME_UNIT_HOUR.equals(WorkTaskConfig.REPORT_TIME_UNIT)){
						Calendar cd = Calendar.getInstance();
						cd.setTime(wtMoni.getLateRepDate());
						cd.add(Calendar.HOUR, repDay);
						wtMoni.setLateRepDate(cd.getTime());
					}
					wtMoni.setLateRerepDate(repDay);
				}
				wtMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_REWA);//设置当前节点状态为退回等待
				wtMoni.setRerepFlag(WorkTaskConfig.REREP_FLAG_YES);//设置重报标志为1
				wtMoni.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
				wtMoni.setStartDate(new Timestamp(System.currentTimeMillis())); //设置此节点任务的开始时间
				wtMoni.getId().setPerformNumber(wtMoni.getId().getPerformNumber()+1); //将执行次数加一
				wtMoni.setReturnDesc(pendingTaskQueryConditions.getReturnDesc());
				this.objectDao.save(wtMoni);
				
				conditioMoni.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_NOT); //将原来的节点任务的执行状态更新为0
				this.objectDao.update(conditioMoni);
				
				res = true;
			}
		}
		return res;
	}
	
	private void findAllPreTask(WorkTaskNodeMoni moni,List<WorkTaskNodeMoni> monis){
		String hsql = "from WorkTaskNodeMoni wm where wm.id.taskMoniId="+moni.getId().getTaskMoniId()+
		" and wm.id.nodeId="+moni.getPreNodeId()+" and wm.id.orgId ='"+moni.getId().getOrgId()
		+"' and wm.finalExecFlag=1";
		WorkTaskNodeMoni nodeMoni = (WorkTaskNodeMoni)this.objectDao.findObject(hsql);
		if(nodeMoni!=null){
			if(nodeMoni.getPreNodeId().equals(WorkTaskConfig.PRE_OBJECT_NULL)){
				monis.add(nodeMoni);
				return;
			}else{
				monis.add(nodeMoni);
				findAllPreTask(nodeMoni, monis);
			}
		}
	}
	/**
	 * 在任务提交时，检查下一个阶段任务是填报还是审核，是填报将任务下的报表状态更新为3，是审核将任务下的报表更新为0
	 */
	public void resetReport(List<WorkTaskPendingTaskVo> pvos)throws Exception{
		if(pvos!=null && !pvos.isEmpty()){
			for(WorkTaskPendingTaskVo pvo : pvos){
				List<String> noPassTemplateList=validateService.getNoPassTemplateIds(pvo,"commit");
				WorkTaskNodeMoni nodeMoni = new WorkTaskNodeMoni();
				WorkTaskNodeMoniId nodeMoniId = new WorkTaskNodeMoniId();
				nodeMoniId.setNodeId(pvo.getNodeId());
				nodeMoniId.setOrgId(pvo.getOrgId());
				nodeMoniId.setTaskMoniId(pvo.getTaskMoniId());
				nodeMoni.setId(nodeMoniId);
				
				WorkTaskNodeMoni searchNodeMoni = findOneWorkTaskNodeMoni(nodeMoni);
				pvo.setEndDate(searchNodeMoni.getEndDate());
				if(searchNodeMoni!=null){
					String hsql = "from WorkTaskMoni moni where moni.taskMoniId="+searchNodeMoni.getId().getTaskMoniId();
					WorkTaskMoni taskMoni = (WorkTaskMoni)this.objectDao.findObject(hsql);
					if(taskMoni!=null){
						WorkTaskInfo taskInfo = workTaskInfoService.findById(taskMoni.getTaskId());
						if(taskInfo!=null){//此处判断不是汇总流程时才做重置操作
							if(!taskInfo.getTaskTypeId().equals(WorkTaskConfig.TASK_TYPE_HZRW)){
								WorkTaskNodeMoni nextNodeMoni = findNextOneWorkNodeMoni(searchNodeMoni);
								if(nextNodeMoni!=null){
									pvo.setBusiLine(searchNodeMoni.getBusiLine());
									WorkTaskNodeInfo nodeInfo = findOneWorkTaskNodeInfo(nextNodeMoni.getId().getNodeId());
									if(nodeInfo!=null){
										if(nodeInfo.getCondTypeId().equals(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL)){
											validateService.resetByFill(pvo,noPassTemplateList);
										}else if(nodeInfo.getCondTypeId().equals(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC)){
											validateService.resetByCheck(pvo,noPassTemplateList);
										}
									}
								}else
									validateService.updateReportDate(pvo, noPassTemplateList);
							}
						}
					}
					
				}
			}
		}
	}
	public boolean isNeedCheckLockTask(Integer taskMoniId,Integer nodeId,String orgId){
		boolean result = false;
		String hsql = "select m.nodeFlag from WorkTaskNodeMoni m where m.id.taskMoniId=" + taskMoniId + " and m.id.nodeId=" + nodeId + " and m.id.orgId='" + orgId + "' and m.finalExecFlag=1";
		Integer nodeFlag = (Integer)this.objectDao.findObject(hsql);
		if(nodeFlag!=null && (nodeFlag.intValue()==WorkTaskConfig.NODE_FLAG_WADC || nodeFlag.intValue()==WorkTaskConfig.NODE_FLAG_REFU))
			result = true;
		return result;
	}
}
