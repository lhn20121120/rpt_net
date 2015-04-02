package com.fitech.model.worktask.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectService;
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
public class WorkTaskEditNodeInfoAction extends WorkTaskBaseAction {
	
	private Logger log = Logger.getLogger(this.getClass());
	private IWorkTaskNodeInfoServiceFlow nodeInfoService;//节点信息定义
	private IWorkTaskNodeObjectServiceFlow nodeObjectService;//节点处理对象
	private IWorkTaskNodeObjectService workTaskNodeObjectService;//节点处理对象
	private IWorkTaskInfoService workTaskInfoService;
     private WorkTaskNodeDefineVo nodeInfoVo;//节点定义
	private List<WorkTaskOrgInfoVo> orgList;//机构列表
	private List<WorkTaskTemplateInfoVo> templateList;//模板列表
	private List roleList;//角色下拉列表
	private List relationTaskList;//关联任务下拉列表
	private String orgIds;//
	private String templateIds;
	private List nodeNameList;
	private List taskNodeList;
	private Operator operator;
	private WorkTaskInfo task;
	private String startDate ;
	private String endDate ;
	private String nodeNamesStr;
	private String orgTreeFileName;//树名称
	private String templateTreeFileName;//树名称
	
	
	public String getNodeNamesStr() {
		return nodeNamesStr;
	}



	public void setNodeNamesStr(String nodeNamesStr) {
		String nodeNames="";
		String taskId=getRequest().getParameter("taskId");
		String nodeId=getRequest().getParameter("nodeId");
		
		String nodeNameHql="select ni.nodeName from WorkTaskNodeInfo ni where ni.taskId="+taskId+" and ni.nodeId<>"+nodeId;
		try {
			List nodeNameList=nodeInfoService.findListByHsql(nodeNameHql, null);
			for (int i = 0;nodeNameList!=null&& i < nodeNameList.size(); i++) {
				String nodeName=(String)nodeNameList.get(i);
				if(!"".equals(nodeNames)){
					nodeNames+=",";
				}
				nodeNames+=nodeName;
			}
		} catch (BaseServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.nodeNamesStr = nodeNames;
	}



	public IWorkTaskNodeObjectService getWorkTaskNodeObjectService() {
		return workTaskNodeObjectService;
	}



	public void setWorkTaskNodeObjectService(
			IWorkTaskNodeObjectService workTaskNodeObjectService) {
		this.workTaskNodeObjectService = workTaskNodeObjectService;
	}



	public IWorkTaskInfoService getWorkTaskInfoService() {
		return workTaskInfoService;
	}



	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}



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



	public Operator getOperator() {
		return operator;
	}



	public void setOperator(Operator operator) {
		this.operator = operator;
	}



	@Override
	
	
	public String initMethod() throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		return null;
	}
	
	
	
	public List getTaskNodeList() {
		String taskId=getRequest().getParameter("taskId");
		List<WorkTaskNodeDefineVo> taskNodeList =new ArrayList<WorkTaskNodeDefineVo>();
		List resultList=new ArrayList();
		String hql="select  ti.taskId,ti.taskName from WorkTaskInfo ti where ti.taskId="+Integer.valueOf(taskId);
		try {
			resultList=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] objs=(Object[])resultList.get(i);
				WorkTaskNodeDefineVo nodeDefineVo=new WorkTaskNodeDefineVo();
				nodeDefineVo.setTaskId((Integer)objs[0]);
				nodeDefineVo.setTaskName((String)objs[1]);
				taskNodeList.add(nodeDefineVo);
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info(e);
		}
		return taskNodeList;
	}



	public void setTaskNodeList(List taskNodeList) {
		this.taskNodeList = taskNodeList;
	}

	/**
	 * 修改节点信息
	 */
	public String editNodeInfo(){
		
/*		Operator opr=new Operator();
		opr.setSuperManager(true);
		opr.setOperatorId(12345L);*/
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String orgTreeData="";
		String templateTreeData="";
		WorkTaskNodeDefineVo nodeInfoVo=new WorkTaskNodeDefineVo();
		String  taskId=getRequest().getParameter("taskId");
		String  nodeId=getRequest().getParameter("nodeId");
		String  busiLineId=getRequest().getParameter("busiLineId");
		
		WorkTaskNodeInfo nodeInfo=null;
		String  hql="select ni.nodeId,ni.nodeName,ni.taskId,ni.condTypeId,ni.relationTaskId ,ni.nodeTime,ni.preNodeId ,nr.id.roleId  from WorkTaskNodeInfo ni ,WorkTaskNodeRole nr where ni.nodeId=nr.id.nodeId  and ni.nodeId="+Integer.valueOf(nodeId)+" and ni.taskId="+Integer.valueOf(taskId);
		try {
			List resList=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; resList!=null&&i < resList.size(); i++) {
				Object[] obj=(Object[])resList.get(i);
				nodeInfoVo.setNodeId((Integer)obj[0]);
				nodeInfoVo.setNodeName((String)obj[1]);
				nodeInfoVo.setTaskId((Integer)obj[2]);
				nodeInfoVo.setConductType((String)obj[3]);
				nodeInfoVo.setRelationTaskId((String)obj[4]);
				nodeInfoVo.setNodeTime((Integer)obj[5]);
				nodeInfoVo.setPreNodeId((Integer)obj[6]);
				nodeInfoVo.setRoleId((Integer)obj[7]);
	
			}
			nodeInfoVo.setBusiLineId(busiLineId);
			this.setNodeInfoVo(nodeInfoVo);
			this.setNodeNamesStr(null);
			
			//生成机构json
			List checkOrgList = workTaskNodeObjectService.findCheckOrgListByTaskIdFlow(taskId,nodeId);
			List checkTemplateList =  workTaskNodeObjectService.findCheckTemplateListByTaskIdFlow(taskId,nodeId);;
			 orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,checkOrgList);//得到机构树JSON
			 WorkTaskInfo workInfo = null;
			 if(taskId!=null){
				 workInfo = workTaskInfoService.findById(Integer.valueOf(taskId));
			 }
			 WorkTaskInfo task=new WorkTaskInfo();
			 task.setBusiLineId(busiLineId);
			 if(workInfo!=null){
				 task.setFreqId(workInfo.getFreqId());
			 }
			 templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,checkTemplateList,task);//得到报表树JSON
//			orgTreeData=nodeInfoService.createEditAFOrgDataJSON(operator,taskId,nodeId);
//			templateTreeData=nodeInfoService.createEditAFTemplateDataJSON(operator,taskId,nodeId);
			this.setOrgTreeFileName("edit_org_tree_data_"+operator.getOperatorId() + "_" + DateUtil.getTodayDetailNumber()+".json");
			String orgFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + this.getOrgTreeFileName();
			this.setTemplateTreeFileName("template_tree_data_"+operator.getOperatorId()+ "_" + DateUtil.getTodayDetailNumber()+".json");
			String templateFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + this.getTemplateTreeFileName();
			
			writeJson(orgFilePath, orgTreeData);
			writeJson(templateFilePath,templateTreeData);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info(e);
		}
		return INPUT;
	}
	public String getOrgTreeFileName() {
		return orgTreeFileName;
	}



	public void setOrgTreeFileName(String orgTreeFileName) {
		this.orgTreeFileName = orgTreeFileName;
	}



	public String getTemplateTreeFileName() {
		return templateTreeFileName;
	}



	public void setTemplateTreeFileName(String templateTreeFileName) {
		this.templateTreeFileName = templateTreeFileName;
	}



	/**
	 * 向前插入
	 */
	public String preAddNodeInfo(){
/*		Operator opr=new Operator();
		opr.setSuperManager(true);
		opr.setOperatorId(12345L);*/
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String orgTreeData="";
		String templateTreeData="";
		WorkTaskNodeDefineVo nodeInfoVo=new WorkTaskNodeDefineVo();
		String  taskId=getRequest().getParameter("taskId");
		String  nodeId=getRequest().getParameter("nodeId");
		String busiLineId=getRequest().getParameter("busiLineId");
		
		WorkTaskNodeInfo nodeInfo=null;
		String  hql="select ni.nodeId,ni.nodeName,ni.taskId,ni.condTypeId,ni.relationTaskId ,ni.nodeTime,ni.preNodeId ,nr.id.roleId  from WorkTaskNodeInfo ni ,WorkTaskNodeRole nr where ni.nodeId=nr.id.nodeId  and ni.nodeId="+Integer.valueOf(nodeId)+" and ni.taskId="+Integer.valueOf(taskId);
		try {
			List resList=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; resList!=null&&i < resList.size(); i++) {
				Object[] obj=(Object[])resList.get(i);
				nodeInfoVo.setNodeId((Integer)obj[0]);
				nodeInfoVo.setNodeName((String)obj[1]);
				nodeInfoVo.setTaskId((Integer)obj[2]);
				nodeInfoVo.setConductType((String)obj[3]);
				nodeInfoVo.setRelationTaskId((String)obj[4]);
				nodeInfoVo.setNodeTime((Integer)obj[5]);
				nodeInfoVo.setPreNodeId((Integer)obj[6]);
				nodeInfoVo.setRoleId((Integer)obj[7]);
	
			}
			
			this.setNodeInfoVo(nodeInfoVo);
			this.setNodeNamesStr(null);
			
			WorkTaskInfo task=new WorkTaskInfo();
			task.setBusiLineId(busiLineId);
			//生成机构json
			orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,null);
			templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,null,task);
			String orgFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + "edit_org_tree_data_"+operator.getOperatorId()+".json";
			
			String templateFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + "edit_template_tree_data_"+operator.getOperatorId()+".json";
			
			writeJson(orgFilePath, orgTreeData);
			writeJson(templateFilePath,templateTreeData);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info(e);
		}
		return "preAddNodeInfo";
	}
	/**
	 * 向前增加
	 */
	public String preAddNode(){
		WorkTaskInfo task=(WorkTaskInfo)this.getHttpSession().getAttribute("taskNodeFlow");
		if(nodeInfoVo.getNodeName()!=null && !nodeInfoVo.getNodeName().equals("")){
			try {
				this.clearErrorsAndMessages();//清空消息
				
				nodeInfoService.insertPreNode(nodeInfoVo,orgIds,templateIds,task);
				this.getRequest().setAttribute("taskInfoListId", nodeInfoVo.getTaskId());
				this.getHttpSession().removeAttribute("taskNodeFlow");
				return SUCCESS;
			} catch (Exception e) {
				this.addActionMessage("增加失败！");
				this.printStackTrace(e);
				return "preAddNodeInfo";
			}
		}
		return "preAddNodeInfo"; 
	}
	/**
	 * 向后增加
	 */
	public String backAddNodeInfo(){
/*		Operator opr=new Operator();
		opr.setSuperManager(true);
		opr.setOperatorId(12345L);*/
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String orgTreeData="";
		String templateTreeData="";
		WorkTaskNodeDefineVo nodeInfoVo=new WorkTaskNodeDefineVo();
		String  taskId=getRequest().getParameter("taskId");
		String  nodeId=getRequest().getParameter("nodeId");
		String busiLineId=getRequest().getParameter("busiLineId");
		WorkTaskNodeInfo nodeInfo=null;
		String  hql="select ni.nodeId,ni.nodeName,ni.taskId,ni.condTypeId,ni.relationTaskId ,ni.nodeTime,ni.preNodeId ,nr.id.roleId  from WorkTaskNodeInfo ni ,WorkTaskNodeRole nr where ni.nodeId=nr.id.nodeId  and ni.nodeId="+Integer.valueOf(nodeId)+" and ni.taskId="+Integer.valueOf(taskId);
		try {
			List resList=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; resList!=null&&i < resList.size(); i++) {
				Object[] obj=(Object[])resList.get(i);
				nodeInfoVo.setNodeId((Integer)obj[0]);
				nodeInfoVo.setNodeName((String)obj[1]);
				nodeInfoVo.setTaskId((Integer)obj[2]);
				nodeInfoVo.setConductType((String)obj[3]);
				nodeInfoVo.setRelationTaskId((String)obj[4]);
				nodeInfoVo.setNodeTime((Integer)obj[5]);
				nodeInfoVo.setPreNodeId((Integer)obj[6]);
				nodeInfoVo.setRoleId((Integer)obj[7]);
	
			}
			
			this.setNodeInfoVo(nodeInfoVo);
			this.setNodeNamesStr(null);
			
			WorkTaskInfo task=new WorkTaskInfo();
			task.setBusiLineId(busiLineId);
			//生成机构json
			orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,null);
			templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,null,task);
			String orgFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + "edit_org_tree_data_"+operator.getOperatorId()+".json";
			
			String templateFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + "edit_template_tree_data_"+operator.getOperatorId()+".json";
			
			writeJson(orgFilePath, orgTreeData);
			writeJson(templateFilePath,templateTreeData);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info(e);
		}
		return "backAddNodeInfo";
	}
	/**
	 * 向后增加
	 */
	public String backAddNode(){
		WorkTaskInfo task=(WorkTaskInfo)this.getHttpSession().getAttribute("taskNodeFlow");
		if(nodeInfoVo.getNodeName()!=null && !nodeInfoVo.getNodeName().equals("")){
			try {
				this.clearErrorsAndMessages();//清空消息
				
				nodeInfoService.insertBackNode(nodeInfoVo,orgIds,templateIds,task);
				this.getRequest().setAttribute("taskInfoListId", nodeInfoVo.getTaskId());
				this.getHttpSession().removeAttribute("taskNodeFlow");
				return SUCCESS;
			} catch (Exception e) {
				this.addActionMessage("增加失败！");
				this.printStackTrace(e);
				return "backAddNodeInfo";
			}
		}
		return "backAddNodeInfo"; 
	}
	/**
	 * 得到某个任务下的所有节点
	 * @return
	 */
	public String displayNodeList(){
		String taskId=getRequest().getParameter("taskId");
		List<WorkTaskNodeDefineVo> taskNodeList =new ArrayList<WorkTaskNodeDefineVo>();
		List resultList=new ArrayList();
		String hql="select  ti.taskId,ti.taskName,ni.nodeId,ni.nodeName,ni.nodeTime from WorkTaskNodeInfo ni,WorkTaskInfo ti where ti.taskId="+Integer.valueOf(taskId)+" and ni.taskId=ti.taskId";
		try {
			resultList=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; i < resultList.size(); i++) {
				Object[] objs=(Object[])resultList.get(i);
				WorkTaskNodeDefineVo nodeDefineVo=new WorkTaskNodeDefineVo();
				nodeDefineVo.setTaskId((Integer)objs[0]);
				nodeDefineVo.setTaskName((String)objs[1]);
				nodeDefineVo.setNodeId((Integer)objs[2]);
				nodeDefineVo.setNodeName((String)objs[3]);
				nodeDefineVo.setNodeTime((Integer)objs[4]);
				taskNodeList.add(nodeDefineVo);
			}
			this.setTaskNodeList(taskNodeList);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info(e);
		}
		
		return INPUT;
	}
	public String forwardNodeInfoPage(){
/*		Operator opr=new Operator();
		opr.setSuperManager(true);
		opr.setOperatorId(12345L);*/
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String orgTreeData="";
		String templateTreeData="";
		
		
		try {
			
			//生成机构json
			orgTreeData=nodeInfoService.createAFOrgDataJSON(operator);
			templateTreeData=nodeInfoService.createAFTemplateDataJSON(operator);
			String orgFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + "org_tree_data_"+operator.getOperatorId()+".json";
			
			String templateFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + "template_tree_data_"+operator.getOperatorId()+".json";
			
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
	 * 更新节点定义
	 * @return
	 */
	public String updateNodeInfo(){
		WorkTaskInfo task=(WorkTaskInfo)this.getHttpSession().getAttribute("taskNodeFlow");
		if(nodeInfoVo.getNodeName()!=null && !nodeInfoVo.getNodeName().equals("")){
			try {
				this.clearErrorsAndMessages();//清空消息
				
				nodeInfoService.update(nodeInfoVo,orgIds,templateIds,task);
				this.getRequest().setAttribute("taskInfoListId", nodeInfoVo.getTaskId());
				this.getHttpSession().removeAttribute("taskNodeFlow");
				return SUCCESS;
			} catch (Exception e) {
				this.addActionMessage("增加失败！");
				this.printStackTrace(e);
				return INPUT;
			}
		}
		return INPUT;
	}
	
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
	

	/**
	 * 角色下拉列选框
	 * @return
	 */
	public List getRoleList(){
		roleList=new ArrayList();
		List resList=new ArrayList();
		StringBuffer hsql=new StringBuffer();
		
		try {
			hsql.append("from ViewWorktaskRole vwr order by vwr.id.roleName");
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
		List hqlList=new ArrayList();
		StringBuffer hsql=new StringBuffer();
		try {
			hsql.append("from WorkTaskInfo ");
			hqlList=nodeInfoService.findListByHsql(hsql.toString(), null);
			for (int i = 0; i < hqlList.size(); i++) {
				WorkTaskInfo taskInfo=(WorkTaskInfo)hqlList.get(i);
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
	
	public List getNodeNameList() {
		List result=new ArrayList();
		nodeNameList=new ArrayList();
		String hql="from WorkTaskNodeInfo t  where t.taskId= "+nodeInfoVo.getTaskId()+" order by t.nodeId ";
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
	}
	public void setNodeNameList(List nodeNameList) {
		this.nodeNameList = nodeNameList;
	}



	public WorkTaskNodeDefineVo getNodeInfoVo() {
		return nodeInfoVo;
	}



	public void setNodeInfoVo(WorkTaskNodeDefineVo nodeInfoVo) {
		this.nodeInfoVo = nodeInfoVo;
	}
	/*
	 * 判断所选模板是否存在
	 */
	public void isExistTemplate(){
		String templateId="";
		String taskId=this.getRequest().getParameter("taskId");
		String taskHql="from WorkTaskInfo  wti where  wti.taskId="+taskId;
		List taskList=null;
		try {
			taskList = nodeInfoService.findListByHsql(taskHql, null);
		} catch (BaseServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String startDate=format.format(((WorkTaskInfo)taskList.get(0)).getStartDate());
		String endDate =format.format(((WorkTaskInfo)taskList.get(0)).getEndDate());
		String freq=((WorkTaskInfo)taskList.get(0)).getFreqId();
		if(!"day".equals(freq) && !WorkTaskConfig.FREQ_YEAR_BEGIN_CARRY.equals(freq)){//如果是日频度则，
			
			try {
				if(templateIds==null||"".equals(templateIds)){
					this.getResponse().getWriter().print(0);
					return;
				}
				if (orgIds==null||"".equals(orgIds)) {
					this.getResponse().getWriter().print(0);
					return;
				}
				else{
					String[] tempArry=templateIds.split(",");
					for (int i = 0; i < tempArry.length; i++) {
						if(!"".equals(templateId)) templateId+=",";
						templateId+="'"+tempArry[i].split("_")[0]+"'";
					}
				}
				String[] orgIdArry=orgIds.split(",");
				String[] templateIdArry=templateId.split(",");
				int count=0;
				String vwto="";
				String vwt="";
				String taskName="";
				flag:
					for (int i = 0; i < orgIdArry.length; i++) {
						for (int j = 0; j < templateIdArry.length; j++) {
							//新增任务的开始日期位于已有任务的开始日期和结束日期之间
							String hql="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate<=to_date('"+startDate+"','yyyy-MM-dd') and wti.endDate>=to_date('"+startDate+"','yyyy-MM-dd')  and  wti.taskId<>"+taskId+"  and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							//新增任务的开始日期小于已有任务的开始日期，结束日日期位于已有任务的开始日期和结束日期之间
							String hql_2="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate>=to_date('"+startDate+"','yyyy-MM-dd') and to_date('"+endDate+"','yyyy-MM-dd') between wti.startDate and wti.endDate  and  wti.taskId<>"+taskId+"  and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							//新增任务的开始日期和结束日期位于已有任务的开始日期和结束日期之间
							String hql_3="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate<=to_date('"+startDate+"','yyyy-MM-dd') and to_date('"+endDate+"','yyyy-MM-dd')<=wti.endDate and  wti.taskId<>"+taskId+"   and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							//新增任务的开始日期和结束日期位于已有任务的开始日期和结束日期之间
							String hql_4="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate>=to_date('"+startDate+"','yyyy-MM-dd') and to_date('"+endDate+"','yyyy-MM-dd')>=wti.endDate and  wti.taskId<>"+taskId+"   and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							String orgHql="select org_name from view_worktask_org where org_id='"+orgIdArry[i]+"'";
							String templateHql="select template_name from View_Worktask_Template vwt where vwt.template_Id="+templateIdArry[j];
							List list=nodeInfoService.findListByHsql(hql, null);
							List list_2=nodeInfoService.findListByHsql(hql_2, null);
							List list_3=nodeInfoService.findListByHsql(hql_3, null);
							List list_4=nodeInfoService.findListByHsql(hql_4, null);
							
							if(list!=null&&list.size()>0){
								taskName=((WorkTaskInfo)list.get(0)).getTaskName();
								List  vwtoList=nodeInfoService.findListBySql(orgHql, null);
								vwto=(String)vwtoList.get(0);
								List vwtList=nodeInfoService.findListBySql(templateHql,null);
								vwt=(String)vwtList.get(0);
								
								break flag;
							}else if(list_2!=null&&list_2.size()>0){
								taskName=((WorkTaskInfo)list_2.get(0)).getTaskName();
								List  vwtoList=nodeInfoService.findListBySql(orgHql, null);
								vwto=(String)vwtoList.get(0);
								List vwtList=nodeInfoService.findListBySql(templateHql,null);
								vwt=(String)vwtList.get(0);
								
								break flag;
								
							}else if(list_3!=null&&list_3.size()>0){
								taskName=((WorkTaskInfo)list_3.get(0)).getTaskName();
								List  vwtoList=nodeInfoService.findListBySql(orgHql, null);
								vwto=(String)vwtoList.get(0);
								List vwtList=nodeInfoService.findListBySql(templateHql,null);
								vwt=(String)vwtList.get(0);
								
								break flag;
							}else if(list_4!=null&&list_4.size()>0){
								taskName=((WorkTaskInfo)list_4.get(0)).getTaskName();
								List  vwtoList=nodeInfoService.findListBySql(orgHql, null);
								vwto=(String)vwtoList.get(0);
								List vwtList=nodeInfoService.findListBySql(templateHql,null);
								vwt=(String)vwtList.get(0);
								
								break flag;
							}
							
						}
						count++;
					}
				/*String hql="from WorkTaskInfo wti where wti.taskId<>"+taskId+"   and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and  wtno.id.templateId in("+templateId+"))";
			List list=nodeInfoService.findListByHsql(hql, null);
			if(list!=null&&list.size()>0){
				
				this.getResponse().getWriter().print(1);
			}else{
				
				this.getResponse().getWriter().print(0);
			}*/
				if(count==orgIdArry.length){
					this.getResponse().getWriter().write(0);
					
				}
				else{
					HttpServletResponse response=this.getResponse();
					response.setCharacterEncoding("UTF-8");
					PrintWriter out=response.getWriter();
					out.print("1="+vwto+"="+vwt+"="+taskName);
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				this.getResponse().getWriter().write(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	

}
