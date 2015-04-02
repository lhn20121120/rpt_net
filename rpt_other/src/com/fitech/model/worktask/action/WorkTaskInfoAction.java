package com.fitech.model.worktask.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.Session;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.model.pojo.ViewWorktaskTemplate;
import com.fitech.model.worktask.model.pojo.WorkTaskBusiLine;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.model.pojo.WorkTaskTrriger;
import com.fitech.model.worktask.model.pojo.WorkTaskType;
import com.fitech.model.worktask.security.IOperator;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskInfoService;

public class WorkTaskInfoAction extends WorkTaskBaseAction {
	private Map<String ,String> freqMap;
	private Operator operator ;

	private List <ViewWorktaskOrg> orgList;
	private WorkTaskNodeRole role;
	private String startDate ;
	private String endDate ;
	
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

	private Map<String ,String> roleMap;
	private WorkTaskInfo task ;
	private WorkTaskInfo task1;

	private List <WorkTaskInfo> taskList;
	private List <ViewWorktaskTemplate> templateList;
	private List <WorkTaskInfo> tasklist = new ArrayList<WorkTaskInfo>();
	private WorkTaskTrriger taskTrriger;
	private IWorkTaskInfoService workTaskInfoService;
	private List<WorkTaskBusiLine> busiLineList;//add by wmm
	private List<WorkTaskType> taskTypeList;//add by wmm

	public WorkTaskTrriger getTaskTrriger() {
		return taskTrriger;
	}
	public void setTaskTrriger(WorkTaskTrriger taskTrriger) {
		this.taskTrriger = taskTrriger;
	}
	public List<WorkTaskInfo> getTasklist() {
		return tasklist;
	}
	public void setTasklist(List<WorkTaskInfo> tasklist) {
		this.tasklist = tasklist;
	}
	public List<WorkTaskBusiLine> getBusiLineList() {
		return busiLineList;
	}
	public void setBusiLineList(List<WorkTaskBusiLine> busiLineList) {
		this.busiLineList = busiLineList;
	}
	public List<WorkTaskType> getTaskTypeList() {
		return taskTypeList;
	}
	public void setTaskTypeList(List<WorkTaskType> taskTypeList) {
		this.taskTypeList = taskTypeList;
	}
	/**
	 *	删除任务 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		
		workTaskInfoService.delete(task);
		
		return SUCCESS;
	}
	/**
	 * 查询所有任务列表
	 */
	public String findAll() throws Exception {
		this.getHttpSession().removeAttribute("nodeList");//add by wmm 移除存放在session里的节点列表
		operator  = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		String reportFlag = null;
		if(this.getRequest().getParameter("reportFlag")!=null){
			reportFlag = this.getRequest().getParameter("reportFlag");
			this.getHttpSession().setAttribute("portalflag", reportFlag);
		}
		else if(operator!=null && operator.getBusiness()!=null
				&& !"null".equals(operator.getBusiness())){
			if(operator.getBusiness().equals("1")){
			//	operator.setBusiness(WorkTaskConfig.BUSI_LINE_YJTX);
				reportFlag = WorkTaskConfig.BUSI_LINE_YJTX;
			}else if(operator.getBusiness().equals("2")){
				//operator.setBusiness(WorkTaskConfig.BUSI_LINE_RHTX);
				reportFlag = WorkTaskConfig.BUSI_LINE_RHTX;
			}else if(operator.getBusiness().equals("3")){
				//operator.setBusiness(WorkTaskConfig.BUSI_LINE_QTTX);
				reportFlag = WorkTaskConfig.BUSI_LINE_QTTX;
			}
			//reportFlag =operator.getBusiness();
			this.getHttpSession().setAttribute("portalflag", reportFlag);
		}else
			reportFlag = (String)this.getHttpSession().getAttribute("portalflag");
		freqMap = workTaskInfoService.findAllFreq();
		taskList = workTaskInfoService.findAll();
		for(int i = 0;i<taskList.size();i++){
			WorkTaskInfo info = new WorkTaskInfo();
			taskTrriger = workTaskInfoService.finAllTrriger(taskList.get(i).getTrrigerId());
			info.setTrrigerId(taskTrriger.getTrrigerName());
			info.setTaskName(taskList.get(i).getTaskName());
			info.setStartDate(taskList.get(i).getStartDate());
			info.setEndDate(taskList.get(i).getEndDate());
			info.setFreqId(taskList.get(i).getFreqId());
			info.setPublicFlag(taskList.get(i).getPublicFlag());
			info.setTaskId(taskList.get(i).getTaskId());
			if(reportFlag!=null
					&& reportFlag.equals(taskList.get(i).getBusiLineId())){
				tasklist.add(info);
			}else if(reportFlag==null){
				tasklist.add(info);
			}
		}
		return "findAll_list";
	}
	
	/**
	 * @return 展示Task基本信息提供修改页面
	 * @throws Exception
	 */
	public String  findInfoById()throws Exception{
		freqMap = workTaskInfoService.findAllFreq();
		task = workTaskInfoService.findById(task.getTaskId());
		startDate= task.getStartDate().toString().substring(0,10);
		endDate= task.getEndDate().toString().substring(0,10);
		busiLineList=workTaskInfoService.findAllBusiLine();//add by wmm
		taskTypeList=workTaskInfoService.findAllTaskType();//add by wmm
		return "findInfoById_info";
	}
	/**
	 * 根据条件检索任务列表
	 */
	public String findListByParam() throws Exception {
		freqMap = workTaskInfoService.findAllFreq();
		taskList = workTaskInfoService.findListByParam(task1);
		tasklist = new ArrayList<WorkTaskInfo>();
		for(int i = 0;i<taskList.size();i++){
			WorkTaskInfo info = new WorkTaskInfo();
			taskTrriger = workTaskInfoService.finAllTrriger(taskList.get(i).getTrrigerId());
			info.setTrrigerId(taskTrriger.getTrrigerName());
			info.setTaskName(taskList.get(i).getTaskName());
			info.setStartDate(taskList.get(i).getStartDate());
			info.setEndDate(taskList.get(i).getEndDate());
			info.setFreqId(taskList.get(i).getFreqId());
			info.setPublicFlag(taskList.get(i).getPublicFlag());
			info.setTaskId(taskList.get(i).getTaskId());
			if(this.getRequest().getSession().getAttribute("portalflag")!=null
					&& this.getRequest().getSession().getAttribute("portalflag").toString().equals(taskList.get(i).getBusiLineId())){
				tasklist.add(info);
			}else if(this.getRequest().getSession().getAttribute("portalflag")==null){
				tasklist.add(info);
			}
		}
		return "findListByParam_list";
	}

	public Map<String, String> getFreqMap() {
		return freqMap;
	}
	public WorkTaskNodeRole getRole() {
		return role;
	}

	public Map<String, String> getRoleMap() {
		return roleMap;
	}

	public WorkTaskInfo getTask() {
		return task;
	}

	public WorkTaskInfo getTask1() {
		return task1;
	}

	public List<WorkTaskInfo> getTaskList() {
		return taskList;
	}

	public IWorkTaskInfoService getWorkTaskInfoService() {
		return workTaskInfoService;
	}
	
	public String delTask()throws Exception{
		workTaskInfoService.delTask(task);
		return "delTask_success";
	}

	@Override
	public String initMethod() throws Exception {
		HttpSession session = this.getHttpSession();
		if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
		return null;
	}

	/**
	 *	添加任务 前
	 * @return
	 * @throws Exception
	 */
	public String preAdd() throws Exception {
		String business = null;
		if(this.getHttpSession().getAttribute("portalflag")!=null){
			business = this.getHttpSession().getAttribute("portalflag").toString();
		}
		if(this.getRequest().getAttribute("task")!=null){
			task = (WorkTaskInfo)this.getRequest().getAttribute("task");
		}
		freqMap = workTaskInfoService.findAllFreq();
		if(this.task==null)
			this.task = new WorkTaskInfo();
		this.getTask().setFreqId(WorkTaskConfig.FREQ_MONTH);
		taskTypeList=workTaskInfoService.findAllTaskType();//add by wmm
		if(business!=null){
			WorkTaskBusiLine bus = new WorkTaskBusiLine();
			if(business.equals(WorkTaskConfig.BUSI_LINE_YJTX)){
				bus.setBusiLineName("银监条线");
			}else if(business.equals(WorkTaskConfig.BUSI_LINE_RHTX)){
				bus.setBusiLineName("人行条线");
			}else if(business.equals(WorkTaskConfig.BUSI_LINE_QTTX)){
				bus.setBusiLineName("其他条线");
			}
			bus.setBusiLineId(this.getHttpSession().getAttribute("portalflag").toString());
			busiLineList = new ArrayList<WorkTaskBusiLine>();
			busiLineList.add(bus);
		}else{
			busiLineList=workTaskInfoService.findAllBusiLine();//add by wmm
		}
		return "preAdd_pre";
	}

	/**
	 *	新增任务 
	 * @return
	 * @throws Exception
	 */
	
	public String save() throws Exception {
		System.out.println(task);
		if(task==null||task.getEndDate()==null||task.getEndDate().equals("")
				||task.getStartDate()==null||task.getStartDate().equals("")
				||task.getTaskName()==null||task.getTaskName().equals("")
				||task.getTriggerShifting()==null||task.getTriggerShifting().equals("")
				||task.getTaskTypeId()==null||task.getTaskTypeId().equals("-1")){
			this.addFieldError("error", "数据填写不完整！");
			freqMap = workTaskInfoService.findAllFreq();
			busiLineList=workTaskInfoService.findAllBusiLine();//add by wmm
			taskTypeList=workTaskInfoService.findAllTaskType();//add by wmm
			return "save_input";
		}
		
		this.getHttpSession().setAttribute("task", task);//将Task对象放入session.
		if(task.getTaskTypeId().equals("hzrw")){
			Operator operator = (Operator)this.getHttpSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);//测试用 
//			operator.setSuperManager(true);//测试用 ，实际应该从 session中取
//			operator.setOperatorId(new Long("60385"));
			String orgTreeData ="";
			String templateTreeData="";
			try {
				//System.out.println(operator.getOperatorId());
				orgTreeData = workTaskInfoService.createAFOrgDataJSON(operator ,null);
				templateTreeData = workTaskInfoService.createAFTemplateDataJSON(operator,null,task);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			workTaskInfoService.createTree(operator,orgTreeData,"org");
			workTaskInfoService.createTree(operator,templateTreeData,"template");			
			roleMap = workTaskInfoService.findAllRole();//得到角色下拉框 内容构建 map集合
			
			return "save_hz";
		}else if(task.getTaskTypeId().equals("djsh")){
			return "save_zf";
		}else if(WorkTaskConfig.TASK_TYPE_ZFBS.equals(task.getTaskTypeId())){//添加总分报送流程
			return "save_zfbs";
		}else{
			return "save_input";
		}
	}

	
	public void setFreqMap(Map<String, String> freqMap) {
		this.freqMap = freqMap;
	}

	public void setRole(WorkTaskNodeRole role) {
		this.role = role;
	}
	
	
	public void setRoleMap(Map<String, String> roleMap) {
		this.roleMap = roleMap;
	}
	
	public void setTask(WorkTaskInfo task) {
		this.task = task;
	}
	

	public void setTask1(WorkTaskInfo task1) {
		this.task1 = task1;
	}
	
	public void setTaskList(List<WorkTaskInfo> taskList) {
		this.taskList = taskList;
	}
	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}
	
	/**
	 *	修改任务 
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
//		WorkTaskInfo dbtask = workTaskInfoService.findById(task.getTaskId());
//		System.out.println(task);
		workTaskInfoService.update(task);
		return "update_success";
	}
	
	public String  updateFlag()throws Exception{
		WorkTaskInfo dbtask = workTaskInfoService.findById(task.getTaskId());
		if(dbtask!=null){
			dbtask.setPublicFlag(task.getPublicFlag());
			workTaskInfoService.updateFlag(dbtask);
		}else{
			this.addFieldError("error", "请输入正确的任务任务编号");
			this.LOG.error( "请输入正确的任务任务编号");
		}
		System.out.println(dbtask);
		return "updateFlag_success";
	}
}
