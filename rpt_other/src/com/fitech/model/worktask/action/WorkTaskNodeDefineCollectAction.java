package com.fitech.model.worktask.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.framework.core.common.Config;
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
public class WorkTaskNodeDefineCollectAction extends WorkTaskBaseAction {


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
	
	private String fileName;
	
	private String orgTreePath;
	private String afTemplatePath;
	private List checkOrgList;
	private List checkTemplateList;
	
	private String concatStr;

	public String getConcatStr() {
		return concatStr;
	}

	public void setConcatStr(String concatStr) {
		this.concatStr = concatStr;
	}

	public String getOrgTreePath() {
		return orgTreePath;
	}


	public void setOrgTreePath(String orgTreePath) {
		this.orgTreePath = orgTreePath;
	}


	public String getAfTemplatePath() {
		return afTemplatePath;
	}


	public void setAfTemplatePath(String afTemplatePath) {
		this.afTemplatePath = afTemplatePath;
	}


	public List getCheckOrgList() {
		return checkOrgList;
	}


	public void setCheckOrgList(List checkOrgList) {
		this.checkOrgList = checkOrgList;
	}


	public List getCheckTemplateList() {
		return checkTemplateList;
	}


	public void setCheckTemplateList(List checkTemplateList) {
		this.checkTemplateList = checkTemplateList;
	}

	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
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
		List nodeDefineVoList=(List)this.getRequest().getSession().getAttribute("nodeList");
		int num =1 ;
		try {
			
			//生成机构json
			//查找上一节点的orgIds、templateIds信息
//			WorkTaskNodeDefineVo preNodeDefineVo=(WorkTaskNodeDefineVo)this.getHttpSession().getAttribute("nodeDefineVo"+(countNode-1));
			
			if(nodeDefineVoList.size()!=0){
				num = nodeDefineVoList.size();
				WorkTaskNodeDefineVo preNodeDefineVo=(WorkTaskNodeDefineVo)nodeDefineVoList.get(nodeDefineVoList.size()-1);
				List<String> orgList=new ArrayList<String>();
				List<String> templateList=new ArrayList<String>();
				Map orgs = preNodeDefineVo.getNodeObject();
				for(Object key : orgs.keySet()){
					orgList.add((String)key);
					String[] templates = orgs.get(key).toString().split(",");
					for (int i = 0; i < templates.length; i++) {
						templateList.add(templates[i].split("_")[0]);
					}
					break;
				}
//				String[] orgIds=preNodeDefineVo.getOrgIds().split(",");
//				String[] templateIds=preNodeDefineVo.getTemplateIds().split(",");
//				List<String> orgList=new ArrayList<String>();
//				List<String> templateList=new ArrayList<String>();
//				for (int i = 0; i < orgIds.length; i++) {
//					orgList.add(orgIds[i]);
//				}
//				for (int i = 0; i < templateIds.length; i++) {
//					templateList.add(templateIds[i].split("_")[0]);
//					
//				}
				orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,orgList);
				templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,templateList,(WorkTaskInfo)this.getHttpSession().getAttribute("task"));
			}else{
			this.getRequest().getSession().removeAttribute("num");
			orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,null);
			templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,null,(WorkTaskInfo)this.getHttpSession().getAttribute("task"));
//			orgTreeData=nodeInfoService.createAFOrgDataJSON(operator);
//			templateTreeData=nodeInfoService.createAFTemplateDataJSON(operator);
			}
			
			String orgFilePath = Config.WEBROOTPATH 
			+ "json" + Config.FILESEPARATOR + "org_tree_data_"+operator.getOperatorId()+"_"+num+".json";
			
			String templateFilePath = Config.WEBROOTPATH
			+ "json" + Config.FILESEPARATOR + "template_tree_data_"+operator.getOperatorId()+"_"+num+".json";
			 this.getRequest().setAttribute("fileNum", num);
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
					int num = (Integer)this.getRequest().getSession().getAttribute("num");
					WorkTaskNodeDefineVo dvo = (WorkTaskNodeDefineVo) list.get(num-1);
					if(dvo!=null){
						nodeDefineVo.setNodeObject(dvo.getNodeObject());
						list.remove(num-1);
					}
					list.add(nodeDefineVo);
					WorkTaskInfo task = (WorkTaskInfo)this.getHttpSession().getAttribute("task");
				//	nodeInfoService.save(this.getHttpSession(),countNode);
					nodeInfoService.save(list, null, task);
//					this.getHttpSession().removeAttribute("nodeTaskIdFlow");
					this.getHttpSession().removeAttribute("task");//增加成功后移除session里的task对象
				/*	for (int i = 0; i <= countNode; i++) {
						this.getHttpSession().removeAttribute("nodeDefineVo"+i);
					}
					countNode=0;*/
					this.getHttpSession().removeAttribute("nodeList");
					this.getRequest().getSession().removeAttribute("num");
					return "taskList";
				}else{
//					this.getHttpSession().setAttribute("nodeDefineVo"+(countNode++), nodeDefineVo);
					List list=(List)this.getRequest().getSession().getAttribute("nodeList");
					Integer num = (Integer)this.getRequest().getSession().getAttribute("num");
					if(num==null)
						num=1;
					WorkTaskNodeDefineVo dvo = null;
					if(list!=null && list.size()>0){
						dvo = (WorkTaskNodeDefineVo)list.get(num-1);
						nodeDefineVo.setNodeObject(dvo.getNodeObject());
					}
					if(dvo!=null)
						list.remove(num-1);
					WorkTaskNodeDefineVo evo = new WorkTaskNodeDefineVo();
					BeanUtils.copyProperties(evo, nodeDefineVo);
					Map map = new HashMap();
					map.putAll(nodeDefineVo.getNodeObject());
					evo.setNodeObject(map);
					list.add(evo);
					nodeNameList=this.getNodeNamesList();
				
					WorkTaskNodeDefineVo nextndo = new WorkTaskNodeDefineVo();
					BeanUtils.copyProperties(nextndo, nodeDefineVo);
					map = new HashMap();
					map.putAll(nextndo.getNodeObject());
					nextndo.setNodeObject(map);
					list.add(nextndo);
					num++;
					nodeDefineVo.getNodeObject().clear();
					this.getRequest().getSession().setAttribute("num",num);
					return forwardNodeInfoPage();
				}
			} catch (Exception e) {
				this.addActionMessage("增加失败！");
				this.printStackTrace(e);
				return INPUT;
			}
		}
		return INPUT;
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
		try {
			if(templateIds==null||"".equals(templateIds)){
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
			String hql="from WorkTaskInfo wti where wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and  wtno.id.templateId in("+templateId+"))";
			List list=nodeInfoService.findListByHsql(hql, null);
			if(list!=null&&list.size()>0){
				
				this.getResponse().getWriter().print(1);
			}else{
				
				this.getResponse().getWriter().print(0);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void editCurrentNodeRelationInfo(){
		if(this.getNodeDefineVo()!=null  && this.getNodeDefineVo().getCheckOrgId()!=null && 
				!this.getNodeDefineVo().getCheckOrgId().equals("")){
			if(this.getNodeDefineVo().getNodeObject()!=null ){
				List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
				int num = (Integer)this.getRequest().getSession().getAttribute("num");
				WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(num-1);
				if(ndVo!=null){
					nodeDefineVo.getNodeObject().putAll(ndVo.getNodeObject());
				}
				if(this.getNodeDefineVo().getNodeObject().containsKey(this.getNodeDefineVo().getCheckOrgId())){
					this.getNodeDefineVo().getNodeObject().put(this.getNodeDefineVo().getCheckOrgId(), this.getNodeDefineVo().getTemplateIds());
					if(ndVo!=null){
						ndVo.getNodeObject().putAll(this.getNodeDefineVo().getNodeObject());
					}
				}
			}
		}else{
			if(this.getNodeDefineVo().getTemplateIds()!=null && 
					this.getNodeDefineVo().getOrgIds()!=null && !this.getNodeDefineVo().getOrgIds().equals("")){
				String[] orgIds = this.getNodeDefineVo().getOrgIds().split(",");
				if(orgIds!=null && !orgIds.equals("")){
					for (int i = 0; i < orgIds.length; i++) {
						this.getNodeDefineVo().getNodeObject().put(orgIds[i],this.getNodeDefineVo().getTemplateIds());
					}
					List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
					int num = (Integer)this.getRequest().getSession().getAttribute("num");
					WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(num-1);
					if(ndVo!=null){
						ndVo.getNodeObject().putAll(nodeDefineVo.getNodeObject());
						nodeDefineVo.getNodeObject().putAll(ndVo.getNodeObject());
					}
				}
			}
		}
	}
	
	public void saveNodeCheckedInfo(){
		try {
			initMethod();
			if(this.getNodeDefineVo()!=null  && this.getNodeDefineVo().getCheckOrgId()!=null && 
					!this.getNodeDefineVo().getCheckOrgId().equals("")
					&& this.getNodeDefineVo().getTemplateIds()!=null){
				this.getNodeDefineVo().getNodeObject().put(this.getNodeDefineVo().getCheckOrgId(), this.getNodeDefineVo().getTemplateIds());
			}
			List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
			if(nodeList==null || nodeList.isEmpty()){
				
				nodeList.add(nodeDefineVo);
				this.getRequest().getSession().setAttribute("num", new Integer(1));
				this.getRequest().getSession().setAttribute("nodeList", nodeList);
			}else{
				int num = (Integer)this.getRequest().getSession().getAttribute("num");
				if((num-1)==nodeList.size()){
					nodeList.add(nodeDefineVo);
				}else{
					WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(num-1);
					if(ndVo==null){
						nodeList.add(nodeDefineVo);
					}else{
						ndVo.getNodeObject().putAll(nodeDefineVo.getNodeObject());
						nodeDefineVo.getNodeObject().putAll(ndVo.getNodeObject());
					}
				}
				this.getRequest().getSession().setAttribute("nodeList", nodeList);	
			}
			this.getResponse().getWriter().print(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public WorkTaskNodeDefineVo getCurrentNodeVo(List nodeList){
		Integer num = (Integer)this.getRequest().getSession().getAttribute("num");
		return (WorkTaskNodeDefineVo)nodeList.get(num-1);
	}
	
	public void reloadTemplateTreeData(){
		try {
			initMethod();
			List nodeList =(List) this.getRequest().getSession().getAttribute("nodeList");
			if(nodeList!=null && !nodeList.isEmpty()){
				WorkTaskNodeDefineVo dvo =getCurrentNodeVo(nodeList);
				if(dvo.getNodeObject().containsKey(this.nodeDefineVo.getCheckOrgId())){
					String templateIds = (String)dvo.getNodeObject().get(this.nodeDefineVo.getCheckOrgId());
					if(templateIds!=null && !templateIds.equals("")){
						String[] tIds = templateIds.split(",");
						checkTemplateList = new ArrayList();
						if(tIds!=null && tIds.length>0){
							for (int i = 0; i < tIds.length; i++) {
								if(tIds[i].indexOf("_")>-1)
									checkTemplateList.add(tIds[i].split("_")[0]);
							}
						}
					}
				}
			}
			if(concatStr==null)
				concatStr = "1";
			Integer count =  Integer.parseInt(concatStr);
			afTemplatePath = count+"";
			initAfTemplateTree();
			
			deleteFile(count-1+"","template");
			
			this.getResponse().getWriter().print(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void orgLevelOrder(){
		try {
			initMethod();
			if(concatStr==null)
				concatStr = "1";
			Integer count =  Integer.parseInt(concatStr);
			List nodeList =(List) this.getRequest().getSession().getAttribute("nodeList");
			String orgTreeData = workTaskInfoService.createAFOrgDataJSONByAsc(operator, null);
			String orgFilePath = WorkTaskConfig.ORG_TREE_JSON_PATH+count+".json";
			writeJson(orgFilePath, orgTreeData);
			
			deleteFile(count-1+"","org");
			this.getResponse().getWriter().print(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showAllOrgs(){
		try {
			initMethod();
			if(concatStr==null)
				concatStr = "1";
			Integer count =  Integer.parseInt(concatStr);
			orgTreePath = count+"";
			initOrgTree();
			deleteFile(count-1+"","org");
			this.getResponse().getWriter().print(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void isSaveOrgLevenType(){
		try {
			int result =0;
			List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
			if(nodeList!=null && !nodeList.isEmpty()){
				 WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(nodeList.size()-1);
				if(ndVo!=null){
					Map orgMap = ndVo.getNodeObject();
					if(orgMap.size()>0){
						for(Object key : orgMap.keySet()){
							String str = key.toString();
							if(str.equalsIgnoreCase("zh")||str.equalsIgnoreCase("fh")||str.equalsIgnoreCase("zhh")
									||str.equalsIgnoreCase("xn")){
								result=1;
							}
						}
					}
				}
			}
			this.getResponse().getWriter().print(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void isSaveOrgAttr(){
		try {
			int result =0;
			List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
			Integer num = (Integer)this.getRequest().getSession().getAttribute("num");
			if(nodeList!=null && !nodeList.isEmpty()){
				 WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(num-1);
				if(ndVo!=null){
					Map orgMap = ndVo.getNodeObject();
					if(orgMap.size()>0){
						result = 1;
						for(Object key : orgMap.keySet()){
							String str = key.toString();
							if(str.equalsIgnoreCase("zh")||str.equalsIgnoreCase("fh")||str.equalsIgnoreCase("zhh")
									||str.equalsIgnoreCase("xn")){
								result=0;
							}
						}
					}
				}
			}
			this.getResponse().getWriter().print(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearMap(){
		try {
			int result =0;
			List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
			if(nodeList!=null && !nodeList.isEmpty()){
				Integer num = (Integer)this.getRequest().getSession().getAttribute("num");
				 WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(num-1);
				if(ndVo!=null){
					ndVo.getNodeObject().clear();
					result = 1;
				}
			}
			this.getResponse().getWriter().print(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void canelCheckOrg(){
//		if(clickOrgId!=null && !clickOrgId.equals("")){
//			try {
//				List nodeList=(List)this.getRequest().getSession().getAttribute("nodeList");
//				Integer num = (Integer)this.getRequest().getSession().getAttribute("num");
//				if(nodeList!=null && !nodeList.isEmpty()){
//					 WorkTaskNodeDefineVo ndVo =(WorkTaskNodeDefineVo) nodeList.get(num-1);
//					if(ndVo!=null){
//						Map orgMap = ndVo.getNodeObject();
//						orgMap.remove(clickOrgId);
//					}
//				}
//				this.getResponse().getWriter().print(1);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public void deleteFile(String str,String deleteType){
		String filePath = "";
		if(deleteType.equals("org")){
			filePath = WorkTaskConfig.ORG_TREE_JSON_PATH+str+".json";
		}else{
			filePath = WorkTaskConfig.TEMPLATE_TREE_JSON_PATH+str+".json";
		}
		File file = new File(filePath);
		if(file.exists())
			file.delete();
	}
	
	public void initOrgTree(){
		String orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,checkOrgList);
		String orgFilePath =WorkTaskConfig.ORG_TREE_JSON_PATH+orgTreePath+".json";
		writeJson(orgFilePath, orgTreeData);
	}
	
	public void initAfTemplateTree(){
		String templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,checkTemplateList,(WorkTaskInfo)this.getHttpSession().getAttribute("task"));
		String templateFilePath =WorkTaskConfig.TEMPLATE_TREE_JSON_PATH+afTemplatePath+".json";
		writeJson(templateFilePath,templateTreeData);
	}
}
