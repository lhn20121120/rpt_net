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

import org.apache.struts2.ServletActionContext;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObject;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectService;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskRoleService;
import com.fitech.model.worktask.vo.ViewWorkTaskRoleVo;
import com.fitech.model.worktask.vo.WorkTaskNodeDefineVo;
import com.fitech.model.worktask.vo.WorkTaskNodeRoleVo;
import com.fitech.model.worktask.vo.WorkTaskOrgInfoVo;
import com.fitech.model.worktask.vo.WorkTaskRelationTaskVo;
import com.fitech.model.worktask.vo.WorkTaskTemplateInfoVo;
/**
 * add by 王明明  at 20130621
 * 节点定义
 * @author Administrator
 *
 */
public class WorkTaskNodeDefineAction extends WorkTaskBaseAction {


	private IWorkTaskNodeInfoService workTaskNodeInfoService;//节点信息定义
	private IWorkTaskRoleService workTaskRoleService;//节点角色处理对象
	private IWorkTaskNodeObjectService workTaskNodeObjectService;//节点处理对象
	private IWorkTaskNodeInfoServiceFlow nodeInfoService;//节点信息定义
	private IWorkTaskNodeObjectServiceFlow nodeObjectService;//节点处理对象
	private IWorkTaskInfoService workTaskInfoService;
	private WorkTaskNodeDefineVo nodeDefineVo;//节点定义
	private List<WorkTaskOrgInfoVo> orgList;
	private List<WorkTaskTemplateInfoVo> templateList;
	private WorkTaskNodeInfo node;//节点 定义对象
	private WorkTaskNodeRole role;//节点  角色对象
	private WorkTaskNodeRoleVo roleVo;
	private WorkTaskInfo  task ;//预修改的task对象
	private int countNode=0;//增加的节点数
	private String nodeNamesStr;//已新增的节点名
	private String orgTreeFileName;//树名称
	private String templateTreeFileName;//树名称
	
	
	


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


	public String getNodeNamesStr() {
		return nodeNamesStr;
	}


	public void setNodeNamesStr(String nodeNamesStr) {
		String nodeNames="";
		List list=(List)this.getHttpSession().getAttribute("nodeList");
		for (int i = 0; list!=null&&i <list.size(); i++) {
			WorkTaskNodeDefineVo infoVo=(WorkTaskNodeDefineVo)list.get(i);
			
			if(!"".equals(nodeNames)){
				nodeNames+=",";
			}
			nodeNames+=infoVo.getNodeName();
		}
		this.nodeNamesStr=nodeNames;
	}


	public WorkTaskInfo getTask() {
		return task;
	}


	public void setTask(WorkTaskInfo task) {
		this.task = task;
	}


	public WorkTaskNodeRoleVo getRoleVo() {
		return roleVo;
	}


	public void setRoleVo(WorkTaskNodeRoleVo roleVo) {
		this.roleVo = roleVo;
	}
	private List roleList;//角色下拉列表
	private List relationTaskList;//关联任务下拉列表
	private String orgIds;//
	private String templateIds;
	private List nodeNameList;
	private WorkTaskNodeObject object;//工作任务节点处理对象
	private Operator operator;



	public Operator getOperator() {
		return operator;
	}


	public void setOperator(Operator operator) {
		this.operator = operator;
	}


	public void setWorkTaskNodeInfoService(
			IWorkTaskNodeInfoService workTaskNodeInfoService) {
		this.workTaskNodeInfoService = workTaskNodeInfoService;
	}


	public void setWorkTaskRoleService(IWorkTaskRoleService workTaskRoleService) {
		this.workTaskRoleService = workTaskRoleService;
	}


	public void setNode(WorkTaskNodeInfo node) {
		this.node = node;
	}


	public void setRole(WorkTaskNodeRole role) {
		this.role = role;
	}


	public void setRoleList(List roleList) {
		this.roleList = roleList;
	}


	public void setObject(WorkTaskNodeObject object) {
		this.object = object;
	}


	public WorkTaskNodeInfo getNode() {
		return node;
	}


	public WorkTaskNodeObject getObject() {
		return object;
	}

	public WorkTaskNodeRole getRole() {
		return role;
	}

	public IWorkTaskNodeInfoService getWorkTaskNodeInfoService() {
		return workTaskNodeInfoService;
	}
	public IWorkTaskRoleService getWorkTaskRoleService() {
		return workTaskRoleService;
	}
	
	
	public IWorkTaskNodeObjectService getWorkTaskNodeObjectService() {
		return workTaskNodeObjectService;
	}
	public void setWorkTaskNodeObjectService(
			IWorkTaskNodeObjectService workTaskNodeObjectService) {
		this.workTaskNodeObjectService = workTaskNodeObjectService;
	}
	@Override
	
	
	public String initMethod() throws Exception {
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		return null;
	}
	
	
	public IWorkTaskInfoService getWorkTaskInfoService() {
		return workTaskInfoService;
	}


	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}


	public String preUpdateNodeInfo()throws Exception{
		System.out.println("preUpdateNodeInfo+++++++++++++++++++++++");
		System.out.println(task);
		System.out.println("查询该任务节点的对象 和相关信息,展现原来设定时候的页面");
		return task.toString();
		
	}
	
	
	public String forwardNodeInfoPage(){
/*		Operator opr=new Operator();
		opr.setSuperManager(true);
		opr.setOperatorId(12345L);*/
		if(this.getRequest().getSession().getAttribute("nodeList")==null){
			this.getRequest().getSession().setAttribute("nodeList", new ArrayList<WorkTaskNodeDefineVo>());
		}
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String orgTreeData="";
		String templateTreeData="";
		
		
		try {
			
			//生成机构json
			//查找上一节点的orgIds、templateIds信息
//			WorkTaskNodeDefineVo preNodeDefineVo=(WorkTaskNodeDefineVo)this.getHttpSession().getAttribute("nodeDefineVo"+(countNode-1));
			List nodeDefineVoList=(List)this.getRequest().getSession().getAttribute("nodeList");
			if(nodeDefineVoList.size()!=0){
				WorkTaskNodeDefineVo preNodeDefineVo=(WorkTaskNodeDefineVo)nodeDefineVoList.get(nodeDefineVoList.size()-1);
				String[] orgIds=preNodeDefineVo.getOrgIds().split(",");
				String[] templateIds=preNodeDefineVo.getTemplateIds().split(",");
				List<String> orgList=new ArrayList<String>();
				List<String> templateList=new ArrayList<String>();
				for (int i = 0; i < orgIds.length; i++) {
					orgList.add(orgIds[i]);
				}
				for (int i = 0; i < templateIds.length; i++) {
					templateList.add(templateIds[i].split("_")[0]);
					
				}
				orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,orgList);
				templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,templateList,(WorkTaskInfo)this.getHttpSession().getAttribute("task"));
			}else if(nodeDefineVo!=null){
				String[] orgIds=nodeDefineVo.getOrgIds().split(",");
				String[] templateIds=nodeDefineVo.getTemplateIds().split(",");
				List<String> orgList=new ArrayList<String>();
				List<String> templateList=new ArrayList<String>();
				for (int i = 0; i < orgIds.length; i++) {
					orgList.add(orgIds[i]);
				}
				for (int i = 0; i < templateIds.length; i++) {
					templateList.add(templateIds[i].split("_")[0]);
					
				}
				orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,orgList);
				templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,templateList,(WorkTaskInfo)this.getHttpSession().getAttribute("task"));
			}else{
				orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,null);
				templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,null,(WorkTaskInfo)this.getHttpSession().getAttribute("task"));
	//			orgTreeData=nodeInfoService.createAFOrgDataJSON(operator);
	//			templateTreeData=nodeInfoService.createAFTemplateDataJSON(operator);
			}
			this.setOrgTreeFileName("org_tree_data_"+operator.getOperatorId() + "_" + DateUtil.getTodayDetailNumber()+".json");
			String orgFilePath = Config.WEBROOTPATH+Config.FILESEPARATOR 
			+ "json" + Config.FILESEPARATOR + this.getOrgTreeFileName();
			this.setTemplateTreeFileName("template_tree_data_"+operator.getOperatorId()+ "_" + DateUtil.getTodayDetailNumber()+".json");
			String templateFilePath = Config.WEBROOTPATH +Config.FILESEPARATOR
			+ "json" + Config.FILESEPARATOR + this.getTemplateTreeFileName();
			writeJson(orgFilePath, orgTreeData);
			writeJson(templateFilePath,templateTreeData);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ServletActionContext.getResponse().setHeader("pragma", "no-cache");
		ServletActionContext.getResponse().setHeader("cache-control", "no-no-cache");
		ServletActionContext.getResponse().setHeader("expires", "0");
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
	 */
	public String saveNodeInfo(){
		String type=this.getRequest().getParameter("type");//获取类型判断跳转的页面
		task=(WorkTaskInfo)this.getHttpSession().getAttribute("task");//获取session里的task对象
		if(nodeDefineVo.getNodeName()!=null && !nodeDefineVo.getNodeName().equals("")){
			try {
				this.clearErrorsAndMessages();//清空消息
				
//				nodeInfoService.save(nodeDefineVo,orgIds,templateIds,task);
//				nodeNameList=this.getNodeNamesList(nodeDefineVo.getTaskId());
//				nodeNameList=this.getNodeNamesList();
//				this.getHttpSession().setAttribute("nodeTaskIdFlow", nodeDefineVo.getTaskId());
//				System.out.println(nodeDefineVo);
				
//				this.getHttpSession().removeAttribute("task");//增加成功后移除session里的task对象
				if ("complete".equals(type)) {
//					this.getHttpSession().setAttribute("nodeDefineVo"+countNode, nodeDefineVo);
					List list=(List)this.getRequest().getSession().getAttribute("nodeList");
					list.add(nodeDefineVo);
					nodeInfoService.save(this.getHttpSession(),countNode);
//					this.getHttpSession().removeAttribute("nodeTaskIdFlow");
					this.getHttpSession().removeAttribute("task");//增加成功后移除session里的task对象
				/*	for (int i = 0; i <= countNode; i++) {
						this.getHttpSession().removeAttribute("nodeDefineVo"+i);
					}
					countNode=0;*/
					this.getHttpSession().removeAttribute("nodeList");
					return "taskList";
				}else{
//					this.getHttpSession().setAttribute("nodeDefineVo"+(countNode++), nodeDefineVo);
					List list=(List)this.getRequest().getSession().getAttribute("nodeList");
					list.add(nodeDefineVo);
					nodeNameList=this.getNodeNamesList();
					return SUCCESS;
				}
			} catch (Exception e) {
				this.addActionMessage("增加失败！");
				this.printStackTrace(e);
				return INPUT;
			}
		}
		return INPUT;
	}
	
	
	public String back(){
		List list=(List)this.getRequest().getSession().getAttribute("nodeList");
		if(list==null || list.isEmpty()){
			WorkTaskInfo taskInfo =(WorkTaskInfo)this.getRequest().getSession().getAttribute("task");
			this.getRequest().setAttribute("task", taskInfo);
			return "back_task";
		}else{
			nodeDefineVo= (WorkTaskNodeDefineVo)list.get(list.size()-1);
			list.remove(list.size()-1);
			nodeNameList=this.getNodeNamesList();
			return forwardNodeInfoPage();
		}
	}
	
	public String quit(){
		for (int i = 0; i <= countNode; i++) {
			this.getHttpSession().removeAttribute("nodeDefineVo"+i);
		}
		countNode=0;
		return "quit";
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
			hsql.append("from ViewWorktaskRole vwr order by  vwr.id.roleName ");
			resList=nodeInfoService.findListByHsql(hsql.toString(), null);
			for (int i = 0; i < resList.size(); i++) {
				ViewWorktaskRole taskRole=(ViewWorktaskRole)resList.get(i);
				ViewWorkTaskRoleVo roleVo=new ViewWorkTaskRoleVo();
				
				List list=(List)this.getRequest().getSession().getAttribute("nodeList");
				if(list==null || list.isEmpty()){
					roleVo.setRoleId(taskRole.getId().getRoleId());
					roleVo.setRoleName(taskRole.getId().getRoleName());
					roleList.add(roleVo);
				}else{
					Integer roleId = null;
					for (int j = 0; j < list.size(); j++) {
						WorkTaskNodeDefineVo dvo = (WorkTaskNodeDefineVo)list.get(j);
						if(dvo!=null && dvo.getRoleId()!=null
								&& taskRole.getId().getRoleId().equals(dvo.getRoleId().longValue())){
							roleId = dvo.getRoleId();
							break;
						}
					}
					if(roleId==null){
						roleVo.setRoleId(taskRole.getId().getRoleId());
						roleVo.setRoleName(taskRole.getId().getRoleName());
						roleList.add(roleVo);
					}
						
				}
				
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
	public WorkTaskNodeDefineVo getNodeDefineVo() {
		return nodeDefineVo;
	}
	public void setNodeDefineVo(WorkTaskNodeDefineVo nodeDefineVo) {
		this.nodeDefineVo = nodeDefineVo;
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
	
	public List getNodeNamesList() {
		List result=new ArrayList();
		List nodeNames=new ArrayList();
		try {
			/*if(nodeDefineVo!=null){
			String hql="from WorkTaskNodeInfo t  where t.taskId= "+taskId+" order by t.nodeId ";
			result=nodeInfoService.findListByHsql(hql, null);
			for (int i = 0; i < result.size(); i++) {
				WorkTaskNodeInfo nodeInfo=(WorkTaskNodeInfo)result.get(i);
				WorkTaskNodeDefineVo infoVo=new WorkTaskNodeDefineVo();
				infoVo.setNodeName(nodeInfo.getNodeName());
				nodeNames.add(infoVo);
				
				
				
			}
			}*/
			List list=(List)this.getHttpSession().getAttribute("nodeList");
			for (int i = 0; list!=null&&i <list.size(); i++) {
				WorkTaskNodeDefineVo infoVo=(WorkTaskNodeDefineVo)list.get(i);
				
				nodeNames.add(infoVo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return nodeNames;
	}
	public void setNodeNameList(List nodeNameList) {
		this.nodeNameList = nodeNameList;
	}
	public List getNodeNameList() {
		return nodeNameList;
	}
	
	/*
	 * 判断所选模板是否存在
	 */
	public void isExistTemplate(){
		String templateId="";
		WorkTaskInfo task=(WorkTaskInfo)this.getRequest().getSession().getAttribute("task");
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String startDate=format.format(task.getStartDate());
		String endDate=format.format(task.getEndDate());
		String freq=task.getFreqId();
		try {
			if(!"day".equals(freq) && !WorkTaskConfig.FREQ_YEAR_BEGIN_CARRY.equals(freq)){
					
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
					String vwto="";//机构
					String vwt="";//报表
					String taskName="";//任务名称
					flag:
					for (int i = 0; i < orgIdArry.length; i++) {
						for (int j = 0; j < templateIdArry.length; j++) {
							//新增任务的开始日期位于已有任务的开始日期和结束日期之间
							String hql="from WorkTaskInfo wti  where wti.freqId='"+freq+"' and  wti.startDate<=to_date('"+startDate+"','yyyy-MM-dd') and wti.endDate>=to_date('"+startDate+"','yyyy-MM-dd') and  wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							//新增任务的开始日期小于已有任务的开始日期，结束日日期位于已有任务的开始日期和结束日期之间
							String hql_2="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate>=to_date('"+startDate+"','yyyy-MM-dd') and to_date('"+endDate+"','yyyy-MM-dd') between wti.startDate and wti.endDate  and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							//新增任务的开始日期和结束日期位于已有任务的开始日期和结束日期之间
							String hql_3="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate<=to_date('"+startDate+"','yyyy-MM-dd') and to_date('"+endDate+"','yyyy-MM-dd')<=wti.endDate   and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
							//新增任务的开始日期和结束日期包含了已有任务的开始日期和结束日期
							String hql_4="from WorkTaskInfo wti where wti.freqId='"+freq+"' and wti.startDate>=to_date('"+startDate+"','yyyy-MM-dd') and to_date('"+endDate+"','yyyy-MM-dd')>=wti.endDate   and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and wtno.id.orgId='"+orgIdArry[i]+"'  and  wtno.id.templateId in("+templateIdArry[j]+"))";
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
					/*String hql="from WorkTaskInfo wti where wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and  wtno.id.templateId in("+templateId+"))";
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
			}else{
				this.getResponse().getWriter().write(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
