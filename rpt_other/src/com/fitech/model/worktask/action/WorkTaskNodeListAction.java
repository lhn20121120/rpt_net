package com.fitech.model.worktask.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fitech.framework.core.common.Config;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectServiceFlow;
import com.fitech.model.worktask.vo.ViewWorkTaskRoleVo;
import com.fitech.model.worktask.vo.WorkTaskNodeDefineVo;
import com.fitech.model.worktask.vo.WorkTaskOrgInfoVo;
import com.fitech.model.worktask.vo.WorkTaskRelationTaskVo;
import com.fitech.model.worktask.vo.WorkTaskTemplateInfoVo;
/**
 * add by 王明明  at 20130621
 * 节点定义
 * @author Administrator
 *
 */
public class WorkTaskNodeListAction extends WorkTaskBaseAction {
	
	private Logger log = Logger.getLogger(this.getClass());
	private IWorkTaskNodeInfoServiceFlow nodeInfoService;//节点信息定义
	private IWorkTaskNodeObjectServiceFlow nodeObjectService;//节点处理对象
//  private WorkTaskNodeDefineVo nodeListVo;//节点定义
	private List<WorkTaskOrgInfoVo> orgList;//机构列表
	private List<WorkTaskTemplateInfoVo> templateList;//模板列表
	private List roleList;//角色下拉列表
	private List relationTaskList;//关联任务下拉列表
	private String orgIds;//
	private String templateIds;
	private List nodeNameList;
	private List taskNodeList;
	private String taskId;
	private WorkTaskInfo task;
	private String startDate ;
	private String endDate ;
	
	public WorkTaskInfo getTask() {
		return task;
	}



	public void setTask(WorkTaskInfo task) {
		this.task = task;
	}



	public String getStartDate() {
		return startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	@Override
	
	
	public String initMethod() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public List getTaskNodeList() {
		return taskNodeList;
	}



	public void setTaskNodeList(List taskNodeList) {
		this.taskNodeList = taskNodeList;
	}



	/**
	 * 得到某个任务下的所有节点
	 * @return
	 */
	public String displayNodeList(){
		//将任务保存到session中
		this.getHttpSession().setAttribute("taskNodeFlow", task);
		String taskId=getRequest().getParameter("taskId");
		String taskTypeId = getRequest().getParameter("taskTypeId");
		getRequest().setAttribute("taskTypeId",taskTypeId);
		String type=getRequest().getParameter("type");
		if(taskId==null){
			taskId=this.getTaskId();
		}
		List<WorkTaskNodeDefineVo> taskNodeList =new ArrayList<WorkTaskNodeDefineVo>();
		List resultList=new ArrayList();
		String hql="select  ti.taskId,ti.taskName,ni.nodeId,ni.nodeName,ni.nodeTime,ti.busiLineId from WorkTaskNodeInfo ni,WorkTaskInfo ti where ti.taskId="+Integer.valueOf(taskId)+" and ni.taskId=ti.taskId start with ni.preNodeId=-1   connect by prior ni.nodeId=ni.preNodeId";
		String sql="select a.task_id,a.task_name,a.node_id,a.node_name,a.node_time,a.pre_node_id,a.cond_type_name,a.role_name,a.busi_Line_Id from (select  ti.task_id,ti.task_name,ni.node_id,ni.node_name,ni.node_time,ni.pre_node_id,ct.cond_type_name,tr.role_name,ti.busi_Line_Id from Work_Task_Node_Info ni,Work_Task_Info ti ,Work_Task_Cond_Type ct,View_WorkTask_Role tr,Work_Task_Node_Role nr  where tr.role_Id=nr.role_Id and nr.node_Id=ni.node_Id and ct.cond_Type_Id=ni.cond_Type_Id and  ti.task_Id="+Integer.valueOf(taskId)+" and ni.task_Id=ti.task_Id ) a start with a.pre_Node_Id=-1   connect by prior a.node_Id=a.pre_Node_Id";
		try {
//			resultList=nodeInfoService.findListByHsql(hql, null);
			resultList=nodeInfoService.findListBySql(sql, null);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] objs=(Object[])resultList.get(i);
				WorkTaskNodeDefineVo nodeDefineVo=new WorkTaskNodeDefineVo();
				nodeDefineVo.setTaskId(((BigDecimal)objs[0]).intValue());
				nodeDefineVo.setTaskName((String)objs[1]);
				nodeDefineVo.setNodeId(((BigDecimal)objs[2]).intValue());
				nodeDefineVo.setNodeName((String)objs[3]);
				nodeDefineVo.setNodeTime(((BigDecimal)objs[4]).intValue());
				nodeDefineVo.setPreNodeId(((BigDecimal)objs[5]).intValue());
				nodeDefineVo.setConductTypeName((String)objs[6]);
				nodeDefineVo.setRoleName((String)objs[7]);
				nodeDefineVo.setBusiLineId((String)objs[8]);
				taskNodeList.add(nodeDefineVo);
			}
			this.setTaskNodeList(taskNodeList);
			if("back".equals(type)){
				this.getRequest().setAttribute("taskInfoListId", taskId);
			}
			else if(task!=null){
				
				this.getRequest().setAttribute("taskInfoListId", task.getTaskId());
			}
			else{
				this.getRequest().setAttribute("taskInfoListId", taskId);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info(e);
		}
		
		return INPUT;
	}
	public String forwardNodeInfoPage(){
		Operator opr=new Operator();
		opr.setSuperManager(true);
		opr.setOperatorId(12345L);
		String orgTreeData="";
		String templateTreeData="";
		
		
		try {
			
			//生成机构json
			orgTreeData=nodeInfoService.createAFOrgDataJSON(opr);
			templateTreeData=nodeInfoService.createAFTemplateDataJSON(opr);
			String orgFilePath = Config.WEBROOTPATH 
			+ "json" + Config.FILESEPARATOR + "org_tree_data_123.json";
			
			String templateFilePath = Config.WEBROOTPATH 
			+ "json" + Config.FILESEPARATOR + "template_tree_data_123.json";
			
			writeJson(orgFilePath, orgTreeData);
			writeJson(templateFilePath,templateTreeData);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return INPUT;
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
	/**
	 * 增加节点定义
	 * @return
	 *//*
	public String saveNodeInfo(){
		if(nodeDefineVo.getNodeName()!=null && !nodeDefineVo.getNodeName().equals("")){
			try {
				this.clearErrorsAndMessages();//清空消息
				
				nodeInfoService.save(nodeDefineVo,orgIds,templateIds);
				return SUCCESS;
			} catch (Exception e) {
				this.addActionMessage("增加失败！");
				this.printStackTrace(e);
				return INPUT;
			}
		}
		return INPUT;
	}
	*/
	/**
	 * 根据任务id，显示节点
	 * @return
	 */
	public List findNodeNamesByTaskId(){
		List resList=new ArrayList();
		
		return resList;
	}
	/**
	 * 根据任务，查看该任务包含的节点信息
	 * @return
	 */
	public List findNodeInfoList(){
		ArrayList resList=new ArrayList();
		return resList;
	}
	/**
	 *对节点信息进行修改 
	 * @return
	 */
	
	public String updateNodeInfo(){
		return SUCCESS;
	}
	/**
	 * 角色下拉列选框
	 * @return
	 */
	public List getRoleList(){
		roleList=new ArrayList();
		List resList=new ArrayList();
		StringBuffer hsql=new StringBuffer();
		
		try {
			hsql.append("from ViewWorktaskRole ");
			resList=nodeInfoService.findListByHsql(hsql.toString(), null);
			for (int i = 0; i < resList.size(); i++) {
				ViewWorktaskRole taskRole=(ViewWorktaskRole)resList.get(i);
				ViewWorkTaskRoleVo roleVo=new ViewWorkTaskRoleVo();
				roleVo.setRoleId(taskRole.getId().getRoleId());
				roleVo.setRoleName(taskRole.getId().getRoleName());
				roleList.add(roleVo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return roleList;
	}
	public IWorkTaskNodeInfoServiceFlow getNodeInfoService() {
		return nodeInfoService;
	}
	public void setNodeInfoService(IWorkTaskNodeInfoServiceFlow nodeInfoService) {
		this.nodeInfoService = nodeInfoService;
	}
	public IWorkTaskNodeObjectServiceFlow getNodeObjectService() {
		return nodeObjectService;
	}
	public void setNodeObjectService(IWorkTaskNodeObjectServiceFlow nodeObjectService) {
		this.nodeObjectService = nodeObjectService;
	}
	/**
	 * 机构
	 * @return
	 */
	public List<WorkTaskOrgInfoVo> getOrgList() {
		orgList=new ArrayList();
		List resList=new ArrayList();
		StringBuffer hsql=new StringBuffer();
		
		try {
			hsql.append("from ViewWorktaskOrg  v  ");//待修改，需加上操作员机构权限
			resList=nodeInfoService.findListByHsql(hsql.toString(), null);
			for (int i = 0; i < resList.size(); i++) {
				ViewWorktaskOrg orgVo=(ViewWorktaskOrg)resList.get(i);
				WorkTaskOrgInfoVo orgBean=new WorkTaskOrgInfoVo();
				orgBean.setOrgId(orgVo.getId().getOrgId());
				orgBean.setOrgName(orgVo.getId().getOrgName());;
				orgList.add(orgBean);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return orgList;
	}
	
	public void setOrgList(List<WorkTaskOrgInfoVo> orgList) {
		this.orgList = orgList;
	}
	public List<WorkTaskTemplateInfoVo> getTemplateList() {
		return templateList;
	}
	public void setTemplateList(List<WorkTaskTemplateInfoVo> templateList) {
		this.templateList = templateList;
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
	/**
	 * 新增节点页面关联任务下拉列表
	 */
	public List getRelationTaskList() {
		List resList=new ArrayList();
		StringBuffer hsql=new StringBuffer();
		try {
			hsql.append("from WorkTaskInfo ");
			resList=nodeInfoService.findListByHsql(hsql.toString(), null);
			for (int i = 0; i < resList.size(); i++) {
				WorkTaskInfo taskInfo=(WorkTaskInfo)resList.get(i);
				WorkTaskRelationTaskVo relationTask=new WorkTaskRelationTaskVo();
				relationTask.setTaskId(taskInfo.getTaskId());
				relationTask.setTaskName(taskInfo.getTaskName());
				resList.add(relationTask);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resList;
	}
	public void setRelationTaskList(List relationTaskList) {
		this.relationTaskList = relationTaskList;
	}
	/**
	 * 新增节点列表
	 * @return
	 */
	
/*	public List getNodeNameList() {
		List result=new ArrayList();
		nodeNameList=new ArrayList();
		String hql="from WorkTaskNodeInfo t  where t.taskId= "+nodeDefineVo.getTaskId()+" order by t.nodeId ";
		try {
			result=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; i < result.size(); i++) {
				WorkTaskNodeInfo nodeInfo=(WorkTaskNodeInfo)result.get(i);
				WorkTaskNodeDefineVo infoVo=new WorkTaskNodeDefineVo();
				infoVo.setNodeName(nodeInfo.getNodeName());
				nodeNameList.add(infoVo);
				
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return nodeNameList;
	}*/
	public void setNodeNameList(List nodeNameList) {
		this.nodeNameList = nodeNameList;
	}
	

}
