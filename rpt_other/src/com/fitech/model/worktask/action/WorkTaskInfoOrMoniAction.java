package com.fitech.model.worktask.action;


import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.struts2.ServletActionContext;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IWorkTaskInfoOrMoniService;
import com.fitech.model.worktask.service.IWorkTaskMoniService;
import com.fitech.model.worktask.vo.WorkTaskInfoOrMoniVo;

public class WorkTaskInfoOrMoniAction extends WorkTaskBaseAction {

	private boolean exec_flag_no;
	private Integer flag; 
	private WorkTaskMoni moni;
	private String date;
	private Integer[] cks;
	private WorkTaskInfoOrMoniVo workTaskInfoOrMoniVo = new WorkTaskInfoOrMoniVo();
	private IWorkTaskInfoOrMoniService workTaskInfoOrMoniService;
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer[] getCks() {
		return cks;
	}
	public void setCks(Integer[] cks) {
		this.cks = cks;
	}
	public boolean isExec_flag_no() {
		return exec_flag_no;
	}
	public void setExec_flag_no(boolean execFlagNo) {
		exec_flag_no = execFlagNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public WorkTaskMoni getMoni() {
		return moni;
	}
	public void setMoni(WorkTaskMoni moni) {
		this.moni = moni;
	}
	public WorkTaskInfoOrMoniVo getWorkTaskInfoOrMoniVo() {
		return workTaskInfoOrMoniVo;
	}
	public void setWorkTaskInfoOrMoniVo(WorkTaskInfoOrMoniVo workTaskInfoOrMoniVo) {
		this.workTaskInfoOrMoniVo = workTaskInfoOrMoniVo;
	}
	public IWorkTaskInfoOrMoniService getWorkTaskInfoOrMoniService() {
		return workTaskInfoOrMoniService;
	}
	public void setWorkTaskInfoOrMoniService(
			IWorkTaskInfoOrMoniService workTaskInfoOrMoniService) {
		this.workTaskInfoOrMoniService = workTaskInfoOrMoniService;
	}
	@Override
	public String initMethod() {
		return null;
	}
	
	public String findWorkTaskInfoOrMoni() throws Exception{
		IWorkTaskMoniService workTaskMoniService = (IWorkTaskMoniService)this.getBean("workTaskMoniService");
		String type=this.getRequest().getParameter("type");
		if(this.getRequest().getParameter("reportFlag")!=null){
			this.getRequest().getSession().setAttribute("portalflag", this.getRequest().getParameter("reportFlag"));
			workTaskInfoOrMoniVo.setBusiLineName(this.getRequest().getParameter("reportFlag"));
		}else{
			Operator op = (Operator)this.getRequest().getSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
			if(op!=null && op.getBusiness()!=null &&
					!"null".equals(op.getBusiness())){
				String busiLine = "";
				if(op.getBusiness().equals("1")){
					//op.setBusiness(WorkTaskConfig.BUSI_LINE_YJTX);
					
					busiLine = WorkTaskConfig.BUSI_LINE_YJTX;
				}else if(op.getBusiness().equals("2")){
					//op.setBusiness(WorkTaskConfig.BUSI_LINE_RHTX);
					
					busiLine = WorkTaskConfig.BUSI_LINE_RHTX;
				}else if(op.getBusiness().equals("3")){
					//op.setBusiness(WorkTaskConfig.BUSI_LINE_QTTX);
					
					busiLine = WorkTaskConfig.BUSI_LINE_QTTX;
				}
				this.getRequest().getSession().setAttribute("portalflag", busiLine);
			}
			String reportFlag = (String)this.getRequest().getSession().getAttribute("portalflag");
			if(reportFlag!=null)
				workTaskInfoOrMoniVo.setBusiLineName(reportFlag);
		}
	    if(date == null){
	    	date =DateUtil.getLastMonth(DateUtil.getTodayDateStr());
			workTaskInfoOrMoniVo.setTaskTerm(Date.valueOf(date));
	    }else if(!date.equals("")){
	    	workTaskInfoOrMoniVo.setTaskTerm(Date.valueOf(date));
	    }
		try {
			if(type!=null&&!"".equals(type)){
				workTaskMoniService.insertWorkTaskAll(date, WorkTaskConfig.TASK_EXEC_FLAG_NOT,workTaskInfoOrMoniVo.getBusiLineName(),exec_flag_no);
				msg="任务生成成功";
			}
				PageResults results = workTaskInfoOrMoniService
				.findPendingTask(null,workTaskInfoOrMoniVo, this.getPageResults());
				this.setPageResults(results);
				
		} catch (Exception e) {
			if(type!=null&&!"".equals(type)){
				msg="任务生成失败";
			}
			e.printStackTrace();
			
		}
		return "list";
	}
	public String updateTaskMoni(){
		if(cks!=null && cks.length>0){
			for (int i = 0; i < cks.length; i++) {
				moni = workTaskInfoOrMoniService.findTaskMoni(cks[i]);
				moni.setExecFlag(flag);
				try {
					workTaskInfoOrMoniService.updateTaskInfo(moni);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "update";
	}
}
