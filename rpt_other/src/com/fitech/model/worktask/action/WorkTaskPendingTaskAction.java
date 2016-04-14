package com.fitech.model.worktask.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.commoninfo.model.pojo.TMsgInfo;
import com.fitech.model.commoninfo.service.ITMsgInfoService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.AfReport;
import com.fitech.model.worktask.model.pojo.ReportIn;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoniId;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForce;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForceId;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskMoniService;
import com.fitech.model.worktask.service.IWorkTaskNodeRoleServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskRepForceService;
import com.fitech.model.worktask.service.IWorkTaskValidateService;
import com.fitech.model.worktask.service.WorkTaskNodeMoniService;
import com.fitech.model.worktask.service.WorkTaskPendingTaskService;
import com.fitech.model.worktask.service.WorkTaskRptNetService;
import com.fitech.model.worktask.vo.PendingTaskQueryConditions;
import com.fitech.model.worktask.vo.WorkTaskIndexVo;
import com.fitech.model.worktask.vo.WorkTaskNodeInfoVo;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

/**
 * 待办业务处理action
 * @author Administrator
 *
 */
public class WorkTaskPendingTaskAction extends WorkTaskBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -245094946075069467L;
	
	private IWorkTaskRepForceService workTaskRepForceService;
	
	private String bussiness;
	
	private String[] cks;
	
	private String str;
	
	private Operator op;
	
	private Long taskMoniIds;
	
	private Integer nodeIds;
	
	private String orgIds;
	
	private WorkTaskNodeMoni wMoni;
	
	private IWorkTaskValidateService validateService;
	
	private WorkTaskIndexVo tVo;

	private String type;
	
	private String taskType;
	
	private List<WorkTaskNodeInfoVo> nodeMonis;
	
	private PendingTaskQueryConditions pendingTaskQueryConditions;
	
	private List<WorkTaskIndexVo> indexYJTasks;
	
	private List<WorkTaskIndexVo> indexRHTasks;
	
	private List<WorkTaskIndexVo> indexQTTasks;
	
	private List<WorkTaskInfo> workTaskInfos;
	
	private IWorkTaskNodeRoleServiceFlow workTaskNodeRoleServiceFlow;
	private IWorkTaskMoniService workTaskMoniService;
	
	private WorkTaskPendingTaskService pendingTaskService;
	
	private WorkTaskNodeMoniService workTaskNodeMoniService;
	
	private IWorkTaskInfoService workTaskInfoService;
	
	private Integer repFlag; //重报状态
	
	private Integer taskId;
	
	private String taskNodeIds;
	
	private ITMsgInfoService tMsgInfoService;
	
	private List<TMsgInfo> msgInfoList;//增加公告消息 的列表
	
	private String repType; //是否加入重报时间 1是 0 否
	
	private Integer repDay; //重报天数
	
	private String taskName;
	
	private String tasktemplateIds;
	
	private String orgTreeFileName;

	public String getOrgTreeFileName() {
		return orgTreeFileName;
	}

	public void setOrgTreeFileName(String orgTreeFileName) {
		this.orgTreeFileName = orgTreeFileName;
	}

	public String getTasktemplateIds() {
		return tasktemplateIds;
	}

	public void setTasktemplateIds(String tasktemplateIds) {
		this.tasktemplateIds = tasktemplateIds;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setWorkTaskRepForceService(
			IWorkTaskRepForceService workTaskRepForceService) {
		this.workTaskRepForceService = workTaskRepForceService;
	}

	public Integer getRepDay() {
		return repDay;
	}

	public void setRepDay(Integer repDay) {
		this.repDay = repDay;
	}

	public String getRepType() {
		return repType;
	}

	public void setRepType(String repType) {
		this.repType = repType;
	}

	public List<WorkTaskIndexVo> getIndexQTTasks() {
		return indexQTTasks;
	}

	public void setIndexQTTasks(List<WorkTaskIndexVo> indexQTTasks) {
		this.indexQTTasks = indexQTTasks;
	}

	public ITMsgInfoService gettMsgInfoService() {
		return tMsgInfoService;
	}

	public void settMsgInfoService(ITMsgInfoService tMsgInfoService) {
		this.tMsgInfoService = tMsgInfoService;
	}

	public List<TMsgInfo> getMsgInfoList() {
		return msgInfoList;
	}

	public void setMsgInfoList(List<TMsgInfo> msgInfoList) {
		this.msgInfoList = msgInfoList;
	}

	public String getBussiness() {
		return bussiness;
	}

	public void setBussiness(String bussiness) {
		this.bussiness = bussiness;
	}

	public String getTaskNodeIds() {
		return taskNodeIds;
	}

	public void setTaskNodeIds(String taskNodeIds) {
		this.taskNodeIds = taskNodeIds;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public IWorkTaskMoniService getWorkTaskMoniService() {
		return workTaskMoniService;
	}

	public void setWorkTaskMoniService(IWorkTaskMoniService workTaskMoniService) {
		this.workTaskMoniService = workTaskMoniService;
	}

	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getRepFlag() {
		return repFlag;
	}

	public void setRepFlag(Integer repFlag) {
		this.repFlag = repFlag;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public Integer getNodeIds() {
		return nodeIds;
	}

	public void setNodeIds(Integer nodeIds) {
		this.nodeIds = nodeIds;
	}

	public Long getTaskMoniIds() {
		return taskMoniIds;
	}

	public void setTaskMoniIds(Long taskMoniIds) {
		this.taskMoniIds = taskMoniIds;
	}

	public List<WorkTaskInfo> getWorkTaskInfos() {
		return workTaskInfos;
	}

	public void setWorkTaskInfos(List<WorkTaskInfo> workTaskInfos) {
		this.workTaskInfos = workTaskInfos;
	}

	public List<WorkTaskNodeInfoVo> getNodeMonis() {
		return nodeMonis;
	}

	public void setNodeMonis(List<WorkTaskNodeInfoVo> nodeMonis) {
		this.nodeMonis = nodeMonis;
	}

	public List<WorkTaskIndexVo> getIndexYJTasks() {
		return indexYJTasks;
	}

	public void setIndexYJTasks(List<WorkTaskIndexVo> indexYJTasks) {
		this.indexYJTasks = indexYJTasks;
	}

	public List<WorkTaskIndexVo> getIndexRHTasks() {
		return indexRHTasks;
	}

	public void setIndexRHTasks(List<WorkTaskIndexVo> indexRHTasks) {
		this.indexRHTasks = indexRHTasks;
	}

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

	public void setValidateService(IWorkTaskValidateService validateService) {
		this.validateService = validateService;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WorkTaskIndexVo gettVo() {
		return tVo;
	}

	public void settVo(WorkTaskIndexVo tVo) {
		this.tVo = tVo;
	}

	public WorkTaskNodeMoni getwMoni() {
		return wMoni;
	}

	public void setwMoni(WorkTaskNodeMoni wMoni) {
		this.wMoni = wMoni;
	}

	public void setWorkTaskNodeRoleServiceFlow(
			IWorkTaskNodeRoleServiceFlow workTaskNodeRoleServiceFlow) {
		this.workTaskNodeRoleServiceFlow = workTaskNodeRoleServiceFlow;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String[] getCks() {
		return cks;
	}

	public void setCks(String[] cks) {
		this.cks = cks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setWorkTaskNodeMoniService(
			WorkTaskNodeMoniService workTaskNodeMoniService) {
		this.workTaskNodeMoniService = workTaskNodeMoniService;
	}

	public PendingTaskQueryConditions getPendingTaskQueryConditions() {
		return pendingTaskQueryConditions;
	}

	@Override
	public String initMethod() throws Exception {
		
		return null;
	}
	
	public void setPendingTaskQueryConditions(
			PendingTaskQueryConditions pendingTaskQueryConditions) {
		this.pendingTaskQueryConditions = pendingTaskQueryConditions;
	}

	public void setPendingTaskService(WorkTaskPendingTaskService pendingTaskService) {
		this.pendingTaskService = pendingTaskService;
	}
	/**
	 * 查看所有当前角色的待办任务
	 * @return
	 */
	public String findPendingTasks(){
		op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String busiline = this.getRequest().getParameter("reportFlag");
		if(op!=null){
			if(pendingTaskQueryConditions==null){
				// add by wmm start
				StringBuffer sb=new StringBuffer();
				String taskType="";
				sb.append(" select distinct wti.taskTypeId from ");
				sb.append(" WorkTaskInfo wti,WorkTaskNodeInfo wtni,WorkTaskNodeObject wtno,WorkTaskNodeRole nr  ");
				sb.append(" where wti.taskId=wtni.taskId  and wtni.nodeId=wtno.id.nodeId and nr.id.nodeId=wtno.id.nodeId and wtno.id.nodeIoFlag=1 and nr.id.roleId in("+op.getRoleIds()+")");
				try {
					List list=workTaskNodeMoniService.findListByHsql(sb.toString(), null);
					for (int i = 0; i < list.size(); i++) {
						String taskTypeId=(String)list.get(i);
						if(!"".equals(taskType)){
							taskType+=",";
						}
						 taskType+=taskTypeId;
					}
				} catch (BaseServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// add by wmm end
				pendingTaskQueryConditions = new PendingTaskQueryConditions();
				pendingTaskQueryConditions.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
				pendingTaskQueryConditions.setTaskId(new Integer(-1));
				if(taskType.contains("djsh")&&!taskType.contains("hzrw")){
					
					pendingTaskQueryConditions.setOrgName("全部机构");
					pendingTaskQueryConditions.setOrgId(WorkTaskConfig.LIST_SELECTED_ALL);
				}else if(!taskType.contains("djsh")&&taskType.contains("hzrw")){
					pendingTaskQueryConditions.setOrgName(op.getOrgName());
					pendingTaskQueryConditions.setOrgId(op.getOrgId());
				}
				else if(taskType.contains("djsh")&&taskType.contains("hzrw")){
					pendingTaskQueryConditions.setMultiTaskFlag(true);
					pendingTaskQueryConditions.setOrgName("全部机构");
					pendingTaskQueryConditions.setOrgId(WorkTaskConfig.LIST_SELECTED_ALL);
				}else if(!taskType.contains("djsh")&&!taskType.contains("hzrw")){
					pendingTaskQueryConditions.setOrgName(op.getOrgName());
					pendingTaskQueryConditions.setOrgId(op.getOrgId());
				}
			}
			//生成机构json
			String orgTreeData = workTaskInfoService.createAFOrgDataJSON(op, null);
			orgTreeData = getParseJsonOrgData(orgTreeData);
			this.setOrgTreeFileName("org_tree_data_"+op.getOperatorId() + "_" + DateUtil.getTodayDetailNumber() +".json");
			String orgFilePath=Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + this.getOrgTreeFileName();
			writeJson(orgFilePath, orgTreeData);
		}
		try {
			if(op!=null){
				if(type!=null && type.equals("index")){
					if(tVo!=null){
						// add by wmm start
						StringBuffer sb=new StringBuffer();
						String taskType="";
						sb.append(" select distinct wti.taskTypeId from ");
						sb.append(" WorkTaskInfo wti,WorkTaskNodeInfo wtni,WorkTaskNodeObject wtno,WorkTaskNodeRole nr  ");
						sb.append(" where wti.taskId=wtni.taskId  and wtni.nodeId=wtno.id.nodeId and nr.id.nodeId=wtno.id.nodeId and wtno.id.nodeIoFlag=1 and nr.id.roleId in("+op.getRoleIds()+")");
						try {
							List list=workTaskNodeMoniService.findListByHsql(sb.toString(), null);
							for (int i = 0; i < list.size(); i++) {
								String taskTypeId=(String)list.get(i);
								if(!"".equals(taskType)){
									taskType+=",";
								}
								 taskType+=taskTypeId;
							}
						} catch (BaseServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// add by wmm end
						pendingTaskQueryConditions = new PendingTaskQueryConditions();
						pendingTaskQueryConditions.setTaskTerm(tVo.getTaskTerm().toString());
						pendingTaskQueryConditions.setTaskMoniId(tVo.getTaskMoniId());
						pendingTaskQueryConditions.setNodeFlag(tVo.getNodeFlag());
						if(tVo.getBusiLine()!=null)
							busiline= tVo.getBusiLine();
						if(taskType.contains("djsh")&&!taskType.contains("hzrw")){
//							ViewWorktaskOrg vOrg = workTaskNodeMoniService.findOneWorkTaskorg(op.getOrgId());
//							if(vOrg!=null){
//								pendingTaskQueryConditions.setOrgName("全部机构");
//								pendingTaskQueryConditions.setOrgId(WorkTaskConfig.LIST_SELECTED_ALL);
//							}else{
//								pendingTaskQueryConditions.setOrgName(op.getOrgName());
//								pendingTaskQueryConditions.setOrgId(op.getOrgId());
//							}
							pendingTaskQueryConditions.setOrgName("全部机构");
							pendingTaskQueryConditions.setOrgId(WorkTaskConfig.LIST_SELECTED_ALL);
						}else if(!taskType.contains("djsh")&&taskType.contains("hzrw")){
							pendingTaskQueryConditions.setOrgName(op.getOrgName());
							pendingTaskQueryConditions.setOrgId(op.getOrgId());
						}
						else if(taskType.contains("djsh")&&taskType.contains("hzrw")){
							pendingTaskQueryConditions.setMultiTaskFlag(true);
							pendingTaskQueryConditions.setOrgName(op.getOrgName());
							pendingTaskQueryConditions.setOrgId(op.getOrgId());
						}else if(!taskType.contains("djsh")&&!taskType.contains("hzrw")){
							pendingTaskQueryConditions.setOrgName(op.getOrgName());
							pendingTaskQueryConditions.setOrgId(op.getOrgId());
						}
					}
				}
				if(busiline!=null && !"".equals(busiline) 
						&& !"null".equals(busiline)){
					this.getRequest().getSession().setAttribute("reportPortalFlag", busiline);
				}
				
				if(ServletActionContext.getContext().getSession().get("reportPortalFlag")!=null){
					pendingTaskQueryConditions.setBusiLine(ServletActionContext.getContext().getSession().get("reportPortalFlag").toString());
				}else{
					if(busiline==null)
						pendingTaskQueryConditions.setBusiLine("-1");
					else
						pendingTaskQueryConditions.setBusiLine(busiline);
				}
				List<WorkTaskNodeRole> nodes = workTaskNodeRoleServiceFlow
					.findWorkTaskNodeRoles(op.getRoleIds());//根据当前用户的角色去找到它拥有的节点对象
				if(nodes!=null){
					PageResults results = workTaskNodeMoniService
					.findPendingTask(null,pendingTaskQueryConditions,
							this.getPageResults(),nodes,op.getOperatorId(),op.getOrgId(),null,null);
						this.setPageResults(results);
						
					workTaskInfos = workTaskNodeMoniService.findAllWorkTaskInfosByParams(null, 
							pendingTaskQueryConditions, null, nodes, op.getOperatorId());
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 查看所有当前角色的已提交任务
	 * @return
	 */
	public String findBWSCTasks(){
		try {
			op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
			String reportPortalFlag = (String)ServletActionContext.getContext().getSession().get("reportPortalFlag");
			if(op!=null){
				if(pendingTaskQueryConditions==null){
					pendingTaskQueryConditions = new PendingTaskQueryConditions();
					pendingTaskQueryConditions.setNodeFlag(WorkTaskConfig.NODE_FLAG_COMM);
					pendingTaskQueryConditions.setTaskId(new Integer(-1));
					pendingTaskQueryConditions.setOrgName(op.getOrgName());
					pendingTaskQueryConditions.setOrgId(op.getOrgId());
					pendingTaskQueryConditions.setTaskTerm(DateUtil.getLastMonth(DateUtil.getTodayDateStr()));
				}
				if(this.getRequest().getParameter("reportFlag")!=null){
					reportPortalFlag = this.getRequest().getParameter("reportFlag");
					this.getRequest().getSession().setAttribute("reportPortalFlag", reportPortalFlag);
				}
				//生成机构json
				String orgTreeData = workTaskInfoService.createAFOrgDataJSON(op, null);
				orgTreeData = getParseJsonOrgData(orgTreeData);
				
				String orgFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
				+ "json" + Config.FILESEPARATOR + "org_tree_data_"+op.getOperatorId()+".json";
				
				writeJson(orgFilePath, orgTreeData);
				pendingTaskQueryConditions.setBusiLine(reportPortalFlag);
				List<WorkTaskNodeRole> nodes = workTaskNodeRoleServiceFlow
				.findWorkTaskNodeRoles(op.getRoleIds());//根据当前用户的角色去找到它拥有的节点对象
				if(nodes!=null){
					String ids = workTaskNodeMoniService.findRepTaskInfoNodeIds();
					PageResults results = workTaskNodeMoniService
					.findPendingTask(null,pendingTaskQueryConditions,
							this.getPageResults(),nodes,op.getOperatorId(),op.getOrgId()
							,repFlag,ids);
						this.setPageResults(results);
						
					workTaskInfos = workTaskNodeMoniService.findAllWorkTaskInfosByParams(null, 
							pendingTaskQueryConditions, null, nodes, op.getOperatorId());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "bwsc_list";
	}
	/**
	 * 查询所有重报任务
	 * @return
	 */
	public String findAllRepTaskInfo(){
		try {
			op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
			String reportPortalFlag = (String)ServletActionContext.getContext().getSession().get("reportPortalFlag");
			if(op!=null){
				if(pendingTaskQueryConditions==null){
					pendingTaskQueryConditions = new PendingTaskQueryConditions();
					pendingTaskQueryConditions.setNodeFlag(WorkTaskConfig.NODE_FLAG_COMM);
					pendingTaskQueryConditions.setTaskId(new Integer(-1));
					
					pendingTaskQueryConditions.setOrgName(op.getOrgName());
					pendingTaskQueryConditions.setOrgId(op.getOrgId());
					pendingTaskQueryConditions.setTaskTerm(DateUtil.getLastMonth(DateUtil.getTodayDateStr()));
				}
				//生成机构json
				String orgTreeData = workTaskInfoService.createAFOrgDataJSON(op, null);
				orgTreeData = getParseJsonOrgData(orgTreeData);
				
				String orgFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
				+ "json" + Config.FILESEPARATOR + "org_tree_data_"+op.getOperatorId()+".json";
				
				writeJson(orgFilePath, orgTreeData);
				if(this.getRequest().getParameter("reportFlag")!=null){
					reportPortalFlag = this.getRequest().getParameter("reportFlag");
					this.getRequest().getSession().setAttribute("reportPortalFlag", reportPortalFlag);
				}
				
				pendingTaskQueryConditions.setBusiLine(reportPortalFlag);
				List<WorkTaskNodeRole> nodes = workTaskNodeRoleServiceFlow
				.findWorkTaskNodeRoles(op.getRoleIds());//根据当前用户的角色去找到它拥有的节点对象
				if(nodes!=null){
					String ids = workTaskNodeMoniService.findRepTaskInfoNodeIds();
					PageResults results = workTaskNodeMoniService
					.findPendingTask(null,pendingTaskQueryConditions,
							this.getPageResults(),nodes,op.getOperatorId(),op.getOrgId()
							,repFlag,ids);
						this.setPageResults(results);
						
					workTaskInfos = workTaskNodeMoniService.findAllWorkTaskInfosByParams(null, 
							pendingTaskQueryConditions, null, nodes, op.getOperatorId());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "task_info_rep";
	}
	/**
	 * 提交任务
	 * @return
	 */
	public String commitTask(){
		if(cks!=null){
			try {
				List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService.findPendingTaskVos(cks);
				if(pvos!=null && !pvos.isEmpty()){
					String result = validateService.validate(pvos);
					if(result==null){
						//add by wmm start 拆分任务
						op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
						StringBuffer sb=new StringBuffer();
						String taskType="";
						sb.append(" select distinct wti.taskTypeId from ");
						sb.append(" WorkTaskInfo wti,WorkTaskNodeInfo wtni,WorkTaskNodeObject wtno,WorkTaskNodeRole nr  ");
						sb.append(" where wti.taskId=wtni.taskId  and wtni.nodeId=wtno.id.nodeId and nr.id.nodeId=wtno.id.nodeId and wtno.id.nodeIoFlag=1 and nr.id.roleId in("+op.getRoleIds()+")");
						try {
							List list=workTaskNodeMoniService.findListByHsql(sb.toString(), null);
							for (int i = 0; i < list.size(); i++) {
								String taskTypeId=(String)list.get(i);
								if(!"".equals(taskType)){
									taskType+=",";
								}
								 taskType+=taskTypeId;
							}
						} catch (BaseServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(taskType.contains("djsh")){
							for (int i = 0; i <pvos.size(); i++) {
								List<String> noPassTemplateList=validateService.getNoPassTemplateIds(pvos.get(i),"commit");
								if(noPassTemplateList!=null&&noPassTemplateList.size()>0){
									
									workTaskMoniService.insertWorkTaskSplit(noPassTemplateList, Integer.valueOf(pvos.get(i).getTaskMoniId()+""), pvos.get(i).getNodeId(), pvos.get(i).getOrgId(), pvos.get(i).getTaskTerm());
								}
							}
							if(pendingTaskQueryConditions==null)
								pendingTaskQueryConditions = new PendingTaskQueryConditions();
							pendingTaskQueryConditions.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
						}
						//add by wmm end
						boolean res= workTaskNodeMoniService.updateWorkTaskNodeMoni(cks, 1);
						workTaskNodeMoniService.resetReport(pvos);
						
						if(res){
							//提交成功
							for (int i = 0; i <pvos.size(); i++) {
								//根据当前提交的任务查找下一个节点的待处理待办任务，根据任务节点角色找到相关用户并执行相关的操作(发送邮件,短信提醒)
								pendingTaskService.performByPendingTask(pvos.get(i));
							}
							msg ="提交成功";
						}else{
							//提交失败
							msg ="提交失败";
						}
					}else{
						msg = result;
						taskMoniIds = pvos.get(0).getTaskMoniId();
						nodeIds = pvos.get(0).getNodeId();
						orgIds = pvos.get(0).getOrgId();
					}
				}
				
				findPendingTasks();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	/**
	 * 退回任务
	 * @return
	 */
	public String backTask(){
		if(cks!=null){
			try {
				List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService.findPendingTaskVos(cks);
				if(pvos!=null && !pvos.isEmpty()){
					boolean res = pendingTaskService.findTaskIsHjOrDJ(pvos);
					taskMoniIds = pvos.get(0).getTaskMoniId();
					nodeIds = pvos.get(0).getNodeId();
					orgIds = pvos.get(0).getOrgId();
					if(res){
						String result = validateService.validateReturn(pvos);//2013-08-13
						//String result=null;
						if(result==null){
							str = StringUtil.converArrayToString(cks, "#");
							WorkTaskInfo taskInfo = workTaskInfoService.findById(pvos.get(0).getTaskId());
//							if(taskInfo!=null){
//								WorkTaskType tp = pendingTaskService.findOneWorkTaskType(taskInfo.getTaskTypeId());
//							}
							WorkTaskNodeMoni moni = new WorkTaskNodeMoni();
							WorkTaskNodeMoniId moniId = new WorkTaskNodeMoniId();
							moniId.setNodeId(pvos.get(0).getNodeId());
							moniId.setOrgId(pvos.get(0).getOrgId());
							moniId.setTaskMoniId(pvos.get(0).getTaskMoniId());
							moni.setId(moniId);
							WorkTaskNodeMoni curNodeMoni = workTaskNodeMoniService.findOneWorkTaskNodeMoni(moni);
							if(curNodeMoni!=null && curNodeMoni.getPreNodeId().equals(new Integer(-1))){
								msg = "任务第一阶段不可以执行退回操作!";
								return findPendingTasks();
							}
							if(curNodeMoni!=null){
								WorkTaskNodeMoni nextNodeMoni = workTaskNodeMoniService.findNextOneWorkNodeMoni(curNodeMoni);
								if(nextNodeMoni==null)
									repType = "1";
							}
							taskType = taskInfo.getTaskTypeId();
							nodeIds = pvos.get(0).getNodeId();
							
							return "backTask_pending";
						}else{
							msg = result;
						}
					}else{
						msg = "所选任务不处在同一流程,不能退回!";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return findPendingTasks();
	}
	/**
	 * 查看审核不通过时，任务退回的详细信息
	 * @return
	 */
	public String findBackTaskDetail(){
		try {
			if(wMoni!=null)
				wMoni = workTaskNodeMoniService.findOneWorkTaskNodeMoni(wMoni);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "work_baclTask_detail";
	}
	/**
	 * 保存退回工作任务
	 * @return
	 */
	public String saveBackTask(){
		boolean result = false;
		if(str!=null && !str.equals("")){
			try {
				cks = StringUtil.converStringToArray(str, "#");
				if(cks!=null){
					//add by wmm start 拆分任务
					List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService.findPendingTaskVos(cks);
					op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
					StringBuffer sb=new StringBuffer();
					String taskType="";
					sb.append(" select distinct wti.taskTypeId from ");
					sb.append(" WorkTaskInfo wti,WorkTaskNodeInfo wtni,WorkTaskNodeObject wtno,WorkTaskNodeRole nr  ");
					sb.append(" where wti.taskId=wtni.taskId  and wtni.nodeId=wtno.id.nodeId and nr.id.nodeId=wtno.id.nodeId and wtno.id.nodeIoFlag=1 and nr.id.roleId in("+op.getRoleIds()+")");
					try {
						List list=workTaskNodeMoniService.findListByHsql(sb.toString(), null);
						for (int i = 0; i < list.size(); i++) {
							String taskTypeId=(String)list.get(i);
							if(!"".equals(taskType)){
								taskType+=",";
							}
							 taskType+=taskTypeId;
						}
					} catch (BaseServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
					WorkTaskRptNetService workTaskRptNetService = (WorkTaskRptNetService)this.getBean("workTaskRptNetService");
					String returnDesc  = pendingTaskQueryConditions.getReturnDesc();
					if(taskType.contains("djsh")){
						for (int i = 0; i <pvos.size(); i++) {
							//不复核退回条件保留下来的报表ID
							WorkTaskPendingTaskVo vo = pvos.get(i);
							String totalIds[] = vo.getTemplateIds().split(",");
							
							List <String>totalList  = new ArrayList<String>();
							for (int j = 0; j < totalIds.length; j++) {
								String templateId = totalIds[j];
								totalList.add(templateId);
							}
							List<String> noPassTemplateList=validateService.getNoPassTemplateIds(vo,"goback");
							if(noPassTemplateList!=null&&noPassTemplateList.size()>0){
								workTaskMoniService.insertWorkTaskSplit(noPassTemplateList, Integer.valueOf(vo.getTaskMoniId()+""), vo.getNodeId(), vo.getOrgId(), vo.getTaskTerm());
							}
							totalList.removeAll(noPassTemplateList);
							if(totalList!=null&&totalList.size()>0){
//								System.out.println(vo.getOrgName()+ vo.getYear()+ vo.getTerm() + vo.getTaskName()+"下" +vo.getOrgName());
								workTaskRptNetService.writLog(vo,op.getUserName(),returnDesc,totalList);
							}
						}
					}
					workTaskNodeMoniService.backWorkTaskNodeMoni(cks, 
							pendingTaskQueryConditions!=null?pendingTaskQueryConditions.getReturnDesc():null
									,taskNodeIds,nodeIds,repDay);
//					List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService.findPendingTaskVos(cks);
					result = true ; 
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		if(pendingTaskQueryConditions==null)
			pendingTaskQueryConditions = new PendingTaskQueryConditions();
		pendingTaskQueryConditions.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
		if(result)
			msg = "退回任务成功!";
		else
			msg = "退回任务失败!";
		return findPendingTasks();
	}
	/**
	 * 手动拆分任务
	 * @return
	 */
	public String splitTask(){
		if(taskName!=null && tasktemplateIds!=null && wMoni!=null &&
				wMoni.getId()!=null){
			try {
				String[] tls = tasktemplateIds.split(",");
				List templateList = new ArrayList();
				if(tls!=null && tls.length>0){
					for (int i = 0; i < tls.length; i++) {
						templateList.add(tls[i]);
					}
				}
				if(!templateList.isEmpty()){
					String hsql = "select count(*) from WorkTaskNodeObjectMoni where id.taskMoniId=" + wMoni.getId().getTaskMoniId()+ " and id.orgId='" + wMoni.getId().getOrgId() + "' and id.nodeId="+wMoni.getId().getNodeId()+" and id.nodeIoFlag=1";
					List remainObject = (List)workTaskNodeMoniService.findListByHsql(hsql, null);
					if(remainObject!=null && !remainObject.isEmpty()){
						int count = ((Long)remainObject.get(0)).intValue();
						if(count<=templateList.size()){
							msg = "不可选择当前任务下所有的报表进行拆分!";
							return findPendingTasks();
						}
					}
					WorkTaskMoni moni = workTaskMoniService.findOneWorkTaskMoni(wMoni.getId().getTaskMoniId());
					workTaskMoniService.insertWorkTaskSplitByManual(templateList, wMoni.getId().getTaskMoniId().intValue(), 
							wMoni.getId().getNodeId(), wMoni.getId().getOrgId(), moni.getTaskTerm(), taskName);
					
					msg = taskName+"任务拆分成功!";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return findPendingTasks();
	}
	
	public void handlerReport(){
		try {
			if(wMoni!=null){
				wMoni = workTaskNodeMoniService.findOneWorkTaskNodeMoni(wMoni);
				this.getRequest().getSession().setAttribute("executeNodeTask", wMoni);
				if(wMoni!=null && wMoni.getId()!=null){
					String[] strs = workTaskNodeMoniService.findByParamsTemplateIds(wMoni);
					if(strs!=null && strs.length==WorkTaskConfig.LENGTH){
						//0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id,6:任务频度,7:任务状态,8:节点id
						this.getResponse().getWriter().print(strs[0]+"#"+strs[1]+
								"#"+strs[2]+"#"+strs[3]+"#"+strs[4]+"#"+strs[5]+"#"+strs[6]+"#"+strs[7]+"#"+strs[8]);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String initSearch(){
		try {
			//Operator op = new Operator();//拿到当前用户所拥有的角色
			op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
			if(op!=null){
				msgInfoList=tMsgInfoService.getUnreadNotice(op.getOperatorId());
				List<WorkTaskNodeRole> nodes = workTaskNodeRoleServiceFlow
						.findWorkTaskNodeRoles(op.getRoleIds());//根据当前用户的角色去找到它拥有的节点对象
				if(nodes!=null){
					if(pendingTaskQueryConditions==null)
						pendingTaskQueryConditions = new PendingTaskQueryConditions();
					
					pendingTaskQueryConditions.setOrgId(op.getOrgId());
					pendingTaskQueryConditions.setRoleIds(op.getRoleIds());
					if(op.getBusiness()!=null && !"null".equals(op.getBusiness())){
						if(op.getBusiness().equals("1")){
							bussiness = WorkTaskConfig.BUSI_LINE_YJTX;
							pendingTaskQueryConditions.setBusiLine(bussiness);
							indexYJTasks= workTaskNodeMoniService.findAllWorkTask(pendingTaskQueryConditions,
									nodes,op.getOperatorId(),op.getOrgId());	
						}else if(op.getBusiness().equals("2")){
							bussiness = WorkTaskConfig.BUSI_LINE_RHTX;
							pendingTaskQueryConditions.setBusiLine(bussiness);
							indexRHTasks= workTaskNodeMoniService.findAllWorkTask(pendingTaskQueryConditions,
									nodes,op.getOperatorId(),op.getOrgId());	
						}else if(op.getBusiness().equals("3")){
							bussiness = WorkTaskConfig.BUSI_LINE_QTTX;
							pendingTaskQueryConditions.setBusiLine(bussiness);
							indexQTTasks= workTaskNodeMoniService.findAllWorkTask(pendingTaskQueryConditions,
									nodes,op.getOperatorId(),op.getOrgId());
						}
					}else{
						pendingTaskQueryConditions.setBusiLine(WorkTaskConfig.BUSI_LINE_YJTX);
						indexYJTasks= workTaskNodeMoniService.findAllWorkTask(pendingTaskQueryConditions,
								nodes,op.getOperatorId(),op.getOrgId());
						pendingTaskQueryConditions.setBusiLine(WorkTaskConfig.BUSI_LINE_RHTX);
						indexRHTasks = workTaskNodeMoniService.findAllWorkTask(pendingTaskQueryConditions,
								nodes,op.getOperatorId(),op.getOrgId());
						pendingTaskQueryConditions.setBusiLine(WorkTaskConfig.BUSI_LINE_QTTX);
						indexQTTasks = workTaskNodeMoniService.findAllWorkTask(pendingTaskQueryConditions,
								nodes,op.getOperatorId(),op.getOrgId());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Config.INCLUDE_COMMONINFO_FLAG==1)
			return "index_task";
		return "index_task_old";
	}
	
	public String findAllNodeMonis(){
		try {
			if(taskId!=null)
				nodeMonis = workTaskNodeMoniService.findAllNodeMonisByTaskMoniId(taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "seach_nodeInfo";
	}
	
	
	public String searchNodeInfos(){
		try {
			if(nodeIds!=null){
				nodeMonis = new ArrayList<WorkTaskNodeInfoVo>();
				List<WorkTaskNodeInfo> nodeInfos = new ArrayList<WorkTaskNodeInfo>();
				pendingTaskService.findBeforeNodesByNodeId(nodeIds, nodeInfos);
				if(!nodeInfos.isEmpty()){
					nodeInfos.remove(0);
					for (int i = nodeInfos.size()-1; i >=0; i--) {
						WorkTaskNodeInfoVo nodeInfoVo = new WorkTaskNodeInfoVo();
						WorkTaskNodeInfo nodeInfo = nodeInfos.get(i);
						nodeInfoVo.setNodeId(nodeInfo.getNodeId());
						nodeInfoVo.setTaskNodeName(nodeInfo.getNodeName());
						nodeMonis.add(nodeInfoVo);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "seach_nodeInfo";
	}
	private String taskTaget;
	public String getTaskTaget() {
		return taskTaget;
	}

	public void setTaskTaget(String taskTaget) {
		this.taskTaget = taskTaget;
	}
	
	/**
	 * 重报任务 缓冲方法，
	 * @return
	 */
	public String newTurnToRep() {
		System.out.println(taskName);
		System.out.println(tasktemplateIds);
		System.out.println(wMoni.getId());
		System.out.println(taskTaget);
		System.out.println("**************"+pendingTaskQueryConditions.getTaskTerm());
		repType = "1";
		return "rep_task_new";
	}
	
	/**
	 * 提交任务重报，可选重报哪些报表 ， 单只能单任务 节点 。
	 * @return
	 */
	public String newCommiRepTask() {
		//                  286,113,817000000,yjtx,15-2-1 1:00:00.000
//		tasktemplateIds = 	G0300_1510_1_817000000_1,G1500_1010_1_817000000_1
		String returnDesc = pendingTaskQueryConditions.getReturnDesc();//重报原因
		String date = pendingTaskQueryConditions.getTaskTerm();
		if (taskName != null && tasktemplateIds != null && wMoni != null
				&& wMoni.getId() != null && returnDesc != null ) {
//			if(taskName.indexOf("重报")==-1)
//				taskName += "(重报)";
			String reTaskName = taskName;
			String[] templateIds  = tasktemplateIds.split(",");
			String templateStr = "";
			//List <String >noPassTemplateList = new ArrayList<String>();
			for (int i = 0; i < templateIds.length; i++) {
				String templateId = templateIds[i];
				String temId[]  = templateId.split("_");
				if(i >0 ){
					templateStr += ",";
				}
				templateStr += temId[0] ;
				//noPassTemplateList.add(temId[0]+"_"+temId[1]);
			}
			WorkTaskMoni splitMoni = splitSelectReport(reTaskName, templateStr, wMoni);
			
			//286,113,817000000,yjtx,15-2-1 1:00:00.000
			if(splitMoni!=null&&!StringUtil.isEmpty(taskTaget)){
				taskTaget  = splitMoni.getTaskMoniId()+taskTaget.substring(taskTaget.indexOf(",", 1));
			}
			repSelectReport( taskTaget  ,pendingTaskQueryConditions,repDay);
			op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
			WorkTaskRptNetService workTaskRptNetService = (WorkTaskRptNetService)this.getBean("workTaskRptNetService");
			try {
				String orgId = taskTaget.split(",")[2];
				String term  = taskTaget.split(",")[4].substring(0,taskTaget.split(",")[4].lastIndexOf("-"));
				String terms[] = term.split("-");
				String year = terms[0];
				String month = (terms[1].equals("1")||terms[1].equals("01"))?"12":(Integer.parseInt(terms[1])-1)+"";
				workTaskRptNetService.writLog(taskName,orgId,null,year+"-"+month,op.getUserName(),returnDesc,templateStr);
			} catch (Exception e) {
				e.printStackTrace();
				Exception ea = new Exception("重报日志异常");
				ea.printStackTrace();		
			}
		}
		return findAllRepTaskInfo();
	}
	/**
	 * 自动把 需要重报的报表拆分出来 。
	 * 
	 * @param taskName
	 *            拆分后的 任务名称 
	 * @param tasktemplateIds
	 *            需要拆分的模版ID  
	 * @param wMoni
	 *            WorkTaskNodeMoni对象
	 */
	private WorkTaskMoni splitSelectReport(String taskName, String tasktemplateIds,
			WorkTaskNodeMoni wMoni) {
		System.out.println("自动把 需要重报的报表拆分出来 。");
		WorkTaskMoni splitMoni = null;
		if (taskName != null && tasktemplateIds != null && wMoni != null
				&& wMoni.getId() != null) {
			try {
				String[] tls = tasktemplateIds.split(",");
				List<String> templateList = new ArrayList<String>();
				if (tls != null && tls.length > 0) {
					for (int i = 0; i < tls.length; i++) {
						templateList.add(tls[i]);
					}
				}
				if (!templateList.isEmpty()) {
					String hsql = "select count(*) from WorkTaskNodeObjectMoni where id.taskMoniId="
							+ wMoni.getId().getTaskMoniId()
							+ " and id.orgId='"
							+ wMoni.getId().getOrgId()
							+ "' and id.nodeId="
							+ wMoni.getId().getNodeId()
							+ " and id.nodeIoFlag=1";
					List remainObject = (List) workTaskNodeMoniService
							.findListByHsql(hsql, null);
					if (remainObject != null && !remainObject.isEmpty()) {
						int count = ((Long) remainObject.get(0)).intValue();
						if (count <= templateList.size()) {
							msg = "不可选择当前任务下所有的报表进行拆分!";
							System.out.println(msg);
							return null;//不拆分任务调用原有的重报 代码 
						}
					}
					WorkTaskMoni moni = workTaskMoniService
							.findOneWorkTaskMoni(wMoni.getId().getTaskMoniId());
				    splitMoni = workTaskMoniService.insertWorkTaskSplitByManualnew(
							templateList, wMoni.getId().getTaskMoniId()
									.intValue(), wMoni.getId().getNodeId(),
							wMoni.getId().getOrgId(), moni.getTaskTerm(),
							taskName);

					msg = taskName + "任务拆分成功!";
					System.out.println(msg);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return splitMoni;
	}

	
	private void repSelectReport(String moniInfo ,PendingTaskQueryConditions pendingTaskQueryConditions ,Integer repDay) {
		System.out.println("将拆分出来的 任务 设定重报");
		WorkTaskRptNetService workTaskRptNetService = (WorkTaskRptNetService) this
				.getBean("workTaskRptNetService");
		op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String message="";
		try {
			if (moniInfo != null && !moniInfo.equals("")) {
				cks = StringUtil.converStringToArray(moniInfo, "#");
				if (cks != null) {
					List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService
							.findPendingTaskVos(cks);
					if (pvos != null && !pvos.isEmpty()) {
						String result = workTaskRptNetService
								.updateReport(op.getUserName(),pvos , null);
						if (result == null) {
							boolean res = workTaskNodeMoniService
									.saveRepTaskInfo(pvos,
											pendingTaskQueryConditions, repDay);
							if (res){
								msg = "重报成功!";
							}
						} else {
							msg = result;
							taskMoniIds = pvos.get(0).getTaskMoniId();
							nodeIds = pvos.get(0).getNodeId();
							orgIds = pvos.get(0).getOrgId();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (msg == null || msg.equals(""))
			msg = "重报失败!";

	}

	
	public String turnToRep(){
		if(cks!=null){
			try {
				str = StringUtil.converArrayToString(cks, "#");
				List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService.findPendingTaskVos(cks);
				if(pvos!=null && !pvos.isEmpty()){
					WorkTaskNodeMoni moni = new WorkTaskNodeMoni();
					WorkTaskNodeMoniId moniId = new WorkTaskNodeMoniId();
					moniId.setNodeId(pvos.get(0).getNodeId());
					moniId.setOrgId(pvos.get(0).getOrgId());
					moniId.setTaskMoniId(pvos.get(0).getTaskMoniId());
					moni.setId(moniId);
					WorkTaskNodeMoni curNodeMoni = workTaskNodeMoniService.findOneWorkTaskNodeMoni(moni);
					if(curNodeMoni!=null){
						WorkTaskNodeMoni nextNodeMoni = workTaskNodeMoniService.findNextOneWorkNodeMoni(curNodeMoni);
						if(nextNodeMoni==null)
							repType = "1";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "rep_task";
	}
	
	public void saveRepInfo(){
		try {
			WorkTaskNodeMoni executeMoni = (WorkTaskNodeMoni)this.getRequest().getSession().getAttribute("executeNodeTask");
			String repId = this.getRequest().getParameter("repIds");
			if(executeMoni!=null && repId!=null){
				String[] repIds = repId.split(",");
				if(repIds!=null){
					WorkTaskRepForce workTaskRepForce = new WorkTaskRepForce();
					WorkTaskRepForceId workTaskRepForceId = new WorkTaskRepForceId();
					workTaskRepForceId.setNodeId(executeMoni.getId().getNodeId());
					workTaskRepForceId.setOrgId(executeMoni.getId().getOrgId());
					workTaskRepForceId.setTaskMoniId(executeMoni.getId().getTaskMoniId().intValue());
					workTaskRepForce.setId(workTaskRepForceId);
					workTaskRepForceService.updateWorkTaskRepForce(workTaskRepForce);
					for (int i = 0; i < repIds.length; i++) {
						if(executeMoni.getBusiLine().equalsIgnoreCase(WorkTaskConfig.BUSI_LINE_YJTX)){
							ReportIn reportIn = workTaskRepForceService.findOneReportIn(Long.valueOf(repIds[i]));
							workTaskRepForceService.saveWorkTaskRepForce(executeMoni.getId().getOrgId(), 
									executeMoni.getId().getTaskMoniId().intValue(),
									executeMoni.getId().getNodeId(), reportIn.getChildRepId(), Long.valueOf(repIds[i]));
						}else{
							AfReport afReport = workTaskRepForceService.findOneAfReport(new Integer(repIds[i]));
							workTaskRepForceService.saveWorkTaskRepForce(executeMoni.getId().getOrgId(), 
									executeMoni.getId().getTaskMoniId().intValue(),
									executeMoni.getId().getNodeId(), afReport.getTemplateId(), Long.valueOf(repIds[i]));
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String commiRepTask(){
		WorkTaskRptNetService rptNetService = (WorkTaskRptNetService)this.getBean("workTaskRptNetService");
		try {
			if(rptNetService!=null){
				if(str!=null && !str.equals("")){
					cks = StringUtil.converStringToArray(str, "#");
					if(cks!=null){
						List<WorkTaskPendingTaskVo> pvos = workTaskNodeMoniService.findPendingTaskVos(cks);
						if(pvos!=null && !pvos.isEmpty()){
							String result = rptNetService.updateReport(op.getUserName(),pvos,"");
							if(result==null){
								boolean res = workTaskNodeMoniService.saveRepTaskInfo(pvos, 
										pendingTaskQueryConditions,repDay);
								if(res)
									msg = "重报成功!";
							}else{
								msg = result;
								taskMoniIds = pvos.get(0).getTaskMoniId();
								nodeIds = pvos.get(0).getNodeId();
								orgIds = pvos.get(0).getOrgId();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(msg==null || msg.equals(""))
			msg = "重报失败!";
		return findAllRepTaskInfo();
	}
	
	public void searchPowType(){
		if(wMoni!=null && wMoni.getId()!=null){
			try {
				op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
				if(op!=null){
					int num = 0;
					ViewWorktaskOrg vOrg = workTaskNodeMoniService.findOneWorkTaskorg(op.getOrgId());
					if(vOrg==null){
						boolean res = workTaskNodeMoniService.findPowTypeByOrgId(wMoni.getId().getOrgId()
								, wMoni.getId().getNodeId(),op.getOperatorId());
						if(res)
							num=1;
					}else
						num=1;
					this.getResponse().getWriter().print(num);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 输出json
	 */
	public void writeJson(String filePath,String treeJson){
		try {
			File file = new File(filePath);
			
			if(file.exists());
				file.delete();
			
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));

			bw.write(treeJson);
			bw.flush();
			bw.close();
			
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public String getParseJsonOrgData(String orgTreeData){
		String treeData = "";
		if(orgTreeData!=null && !orgTreeData.equals("")){
			String str = orgTreeData.substring(1, orgTreeData.length()-1);
			treeData+="[{\"id\":\""+WorkTaskConfig.LIST_SELECTED_ALL+"\",\"text\":\"全部机构\",\"children\":["+str+"]}]";
		}
		return treeData;
	}
}
