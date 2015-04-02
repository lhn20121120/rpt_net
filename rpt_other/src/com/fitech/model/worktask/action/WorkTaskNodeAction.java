package com.fitech.model.worktask.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObject;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRoleId;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectService;
import com.fitech.model.worktask.service.IWorkTaskNodeRepTimeService;
import com.fitech.model.worktask.service.IWorkTaskRoleService;
import com.fitech.model.worktask.vo.WorkTaskNodeRoleVo;
/**
 * add by LK at 20130621
 * 节点定义
 * @author Administrator
 *
 */
public class WorkTaskNodeAction extends WorkTaskBaseAction {

	private IWorkTaskNodeInfoService workTaskNodeInfoService;//节点信息定义
	private IWorkTaskRoleService workTaskRoleService;//节点角色处理对象
	private IWorkTaskNodeObjectService workTaskNodeObjectService;//节点处理对象
	private IWorkTaskNodeRepTimeService workTaskNodeRepTimeService;//报送时间处理对象
	private IWorkTaskInfoService workTaskInfoService;
	private WorkTaskNodeInfo node;//节点 定义对象
	private WorkTaskNodeRoleVo roleVo;
	private WorkTaskInfo  task ;//预修改的task对象
	private String orgIds;//
	private String templateIds;
	private WorkTaskNodeObject object;//工作任务节点处理对象
	private Operator operator; //登录用户对象
	private String zTime,fTime ,cTime;
	private Map<String ,String> roleMap;
	
	public IWorkTaskNodeRepTimeService getWorkTaskNodeRepTimeService() {
		return workTaskNodeRepTimeService;
	}

	public void setWorkTaskNodeRepTimeService(
			IWorkTaskNodeRepTimeService workTaskNodeRepTimeService) {
		this.workTaskNodeRepTimeService = workTaskNodeRepTimeService;
	}

	public IWorkTaskInfoService getWorkTaskInfoService() {
		return workTaskInfoService;
	}

	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}

	public Map<String, String> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<String, String> roleMap) {
		this.roleMap = roleMap;
	}

	public String getzTime() {
		return zTime;
	}

	public void setzTime(String zTime) {
		this.zTime = zTime;
	}

	public String getfTime() {
		return fTime;
	}

	public void setfTime(String fTime) {
		this.fTime = fTime;
	}

	public String getcTime() {
		return cTime;
	}

	public void setcTime(String cTime) {
		this.cTime = cTime;
	}

	public IWorkTaskNodeInfoService getWorkTaskNodeInfoService() {
		return workTaskNodeInfoService;
	}

	public void setWorkTaskNodeInfoService(
			IWorkTaskNodeInfoService workTaskNodeInfoService) {
		this.workTaskNodeInfoService = workTaskNodeInfoService;
	}

	public IWorkTaskRoleService getWorkTaskRoleService() {
		return workTaskRoleService;
	}

	public void setWorkTaskRoleService(IWorkTaskRoleService workTaskRoleService) {
		this.workTaskRoleService = workTaskRoleService;
	}

	public IWorkTaskNodeObjectService getWorkTaskNodeObjectService() {
		return workTaskNodeObjectService;
	}

	public void setWorkTaskNodeObjectService(
			IWorkTaskNodeObjectService workTaskNodeObjectService) {
		this.workTaskNodeObjectService = workTaskNodeObjectService;
	}

	public WorkTaskNodeInfo getNode() {
		return node;
	}

	public void setNode(WorkTaskNodeInfo node) {
		this.node = node;
	}

	public WorkTaskNodeRoleVo getRoleVo() {
		return roleVo;
	}

	public void setRoleVo(WorkTaskNodeRoleVo roleVo) {
		this.roleVo = roleVo;
	}

	public WorkTaskInfo getTask() {
		return task;
	}

	public void setTask(WorkTaskInfo task) {
		this.task = task;
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

	public WorkTaskNodeObject getObject() {
		return object;
	}

	public void setObject(WorkTaskNodeObject object) {
		this.object = object;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public String initMethod() throws Exception {
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		return null;
	}
	
	public String preUpdateNodeInfo()throws Exception{
		System.out.println("preUpdateNodeInfo+++++++++++++++++++++++");
		System.out.println(task);
		System.out.println("查询该任务节点的对象 和相关信息,展现原来设定时候的页面");
		
		this.getHttpSession().setAttribute("task", task);//将Task对象放入session.
		Operator operator = (Operator)this.getHttpSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);//测试用 
//		operator = new  Operator();//测试用 
//		operator.setSuperManager(true);//测试用 ，实际应该从 session中取
		//映射机构树和 模版树 的选中
		List checkOrgList = workTaskNodeObjectService.findCheckOrgListByTaskId(task.getTaskId().toString());
		List checkTemplateList =  workTaskNodeObjectService.findCheckTemplateListByTaskId(task.getTaskId().toString());;
		String orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,checkOrgList);//得到机构树JSON
//		String templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,checkTemplateList);//得到报表树JSON
		String templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,checkTemplateList,task);//得到报表树JSON
		workTaskInfoService.createTree(operator,orgTreeData,"org");
		workTaskInfoService.createTree(operator,templateTreeData,"template");			
		roleMap = workTaskInfoService.findAllRole();//得到角色下拉框 内容构建 map集合
		//映射处理类型角色
		List roleList  = workTaskRoleService.findRoleByTaskId(task.getTaskId());
		if(roleList!=null&&roleList.size()>0){
			roleVo = new WorkTaskNodeRoleVo();
			roleVo.setTb_roleId((Integer)roleList.get(0));
			roleVo.setFh_roleId((Integer)roleList.get(1));
		}
		//映射报送时间
		List repTimeList = workTaskNodeRepTimeService.findRepTimeByTaskId(task.getTaskId());
		if(repTimeList!=null&&repTimeList.size()>0){
			for (int i = 0; i < repTimeList.size(); i++) {
				Object[] objs =(Object[]) repTimeList.get(i);
				if(objs[0].equals(WorkTaskConfig.ORG_LEVLE_ZH))
					zTime = objs[1]+"";
				if(objs[0].equals(WorkTaskConfig.ORG_LEVLE_FH))
					fTime = objs[1]+"";
				if(objs[0].equals(WorkTaskConfig.ORG_LEVLE_CH))
					cTime = objs[1]+"";
			}
		}
		return "preUpdateNodeInfo_info";
		
	}
	
	
	public String saveHzNodeInfo()throws Exception{
		System.out.println("saveHzNodeInfo+++++++++++++++++++++++");
		WorkTaskInfo task = (WorkTaskInfo) this.getHttpSession().getAttribute("task");
		this.getHttpSession().removeAttribute("task");//移除session中的Task对象
		
	
		
		WorkTaskNodeRole fhrole = new WorkTaskNodeRole(new WorkTaskNodeRoleId());
		WorkTaskNodeRole tbrole = new WorkTaskNodeRole(new WorkTaskNodeRoleId());
		
		
		if(task!=null){
			tbrole.getId().setRoleId(roleVo.getTb_roleId()) ;
			node = new WorkTaskNodeInfo();
			node.setCondTypeId(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL);
			node.setPreNodeId(WorkTaskConfig.PRE_OBJECT_NULL);
			WorkTaskNodeInfo fhnode = new WorkTaskNodeInfo ();
			BeanUtils.copyProperties(fhnode,node);
			fhnode.setCondTypeId(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC);
			fhrole.getId().setRoleId(roleVo.getFh_roleId()) ;
			workTaskNodeInfoService.save(task, node,fhnode, tbrole,fhrole, object, orgIds, templateIds,zTime,fTime,cTime);
		}else{
			this.addFieldError("error", "请返回重新设定任务！");
		}
		
		return "saveHzNodeInfo_success";
	}
	
	
	public String editHzNodeInfo()throws Exception{
		System.out.println("editHzNodeInfo+++++++++++++++++++++++");
		
		
		WorkTaskInfo task = (WorkTaskInfo) this.getHttpSession().getAttribute("task");
		this.getHttpSession().removeAttribute("task");//移除session中的Task对象
		workTaskInfoService.update(task);
//		workTaskNodeInfoService.deleteInfo(task);
		workTaskNodeInfoService.deleteRalation(task);
		workTaskNodeObjectService.deleteByTaskId(task.getTaskId());
		workTaskRoleService.deleteByTaskId(task.getTaskId());
		workTaskNodeRepTimeService.deleteByTaskId(task.getTaskId());

		WorkTaskNodeRole fhrole = new WorkTaskNodeRole(new WorkTaskNodeRoleId());
		WorkTaskNodeRole tbrole = new WorkTaskNodeRole(new WorkTaskNodeRoleId());
		
		
		if(task!=null){
			tbrole.getId().setRoleId(roleVo.getTb_roleId()) ;
			node = new WorkTaskNodeInfo();
			node.setCondTypeId(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL);
			node.setPreNodeId(WorkTaskConfig.PRE_OBJECT_NULL);
			WorkTaskNodeInfo fhnode = new WorkTaskNodeInfo ();
			BeanUtils.copyProperties(fhnode,node);
			fhnode.setCondTypeId(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC);
			fhrole.getId().setRoleId(roleVo.getFh_roleId()) ;
			workTaskNodeInfoService.edit(task, node,fhnode, tbrole,fhrole, object, orgIds, templateIds,zTime,fTime,cTime);
		}else{
			this.addFieldError("error", "请返回重新修改任务！");
		}
	
		return "editHzNodeInfo_success";
	}
	
	/*
	 * 判断所选模板是否存在 add by wmm 
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
			List list=workTaskInfoService.findListByHsql(hql, null);
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
	/*
	 * 判断所选模板是否存在 add by wmm 
	 */
	public void isExistTemplateEdit(){
		String templateId="";
		String taskId=this.getRequest().getParameter("taskId");
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
			String hql="from WorkTaskInfo wti where wti.taskId<>"+taskId+" and wti.taskId in(select distinct wtni.taskId from WorkTaskNodeObject wtno,WorkTaskNodeInfo wtni where wtno.id.nodeId=wtni.nodeId and wtno.id.nodeIoFlag=1 and  wtno.id.templateId in("+templateId+"))";
			List list=workTaskInfoService.findListByHsql(hql, null);
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
	
}
