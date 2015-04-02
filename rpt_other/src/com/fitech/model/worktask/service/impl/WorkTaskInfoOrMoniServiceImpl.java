package com.fitech.model.worktask.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;
import com.fitech.model.worktask.service.IWorkTaskInfoOrMoniService;
import com.fitech.model.worktask.vo.WorkTaskInfoOrMoniVo;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

/**
 * 查找工作任务定义
 * @author 姜明青
 *
 */
public class WorkTaskInfoOrMoniServiceImpl 
				extends DefaultBaseService<WorkTaskInfo, String>
				implements IWorkTaskInfoOrMoniService{

	@Override
	public PageResults findPendingTask(Map parameterMap, WorkTaskInfoOrMoniVo monivo,
			PageResults pageResults)
			throws Exception {
		// TODO Auto-generated method stub
		if(pageResults == null){
			pageResults = new PageResults();
		}
		pageResults.setPageSize(Config.PageSize);
		if(parameterMap == null){
			parameterMap = new HashMap();
		}
		if(monivo != null ){
			if(monivo.getTaskName() != null && !monivo.getTaskName().trim().equals("")){
				parameterMap.put("taskName", monivo.getTaskName());
			}
			if(monivo.getFreqId() != null && !monivo.getFreqId().toString().trim().equals("-1")){
				parameterMap.put("freqId", monivo.getFreqId());
			}
			if(monivo.getTaskTerm() != null && !monivo.getTaskTerm().toString().trim().equals("")){
				parameterMap.put("taskTerm", monivo.getTaskTerm()==null?DateUtil.getLastMonth(DateUtil.getTodayDateStr()):monivo.getTaskTerm());
			}
			if(monivo.getBusiLineName() != null && !monivo.getBusiLineName().toString().trim().equals("")){
				parameterMap.put("busiLine", monivo.getBusiLineName());
			}
		}
		StringBuffer hsql = new StringBuffer();
		hsql.append("SELECT m.taskName,i.freqId,t.trrigerName,b.busiLineName,m.taskTerm,m.execFlag,m.taskMoniId FROM WorkTaskInfo i,WorkTaskMoni m,WorkTaskTrriger t,WorkTaskBusiLine b ");
		hsql.append("WHERE i.taskId = m.taskId AND i.trrigerId = t.trrigerId AND i.busiLineId=b.busiLineId");
		if(parameterMap.containsKey("taskName")){
			hsql.append(" AND m.taskName LIKE '%"+monivo.getTaskName()+"%'");
		}
		if(parameterMap.containsKey("freqId")){
			hsql.append(" AND i.freqId = '"+monivo.getFreqId()+"'");
		}
		if(parameterMap.containsKey("busiLine")){
			hsql.append(" AND i.busiLineId = '"+monivo.getBusiLineName()+"'");
		}
		if(parameterMap.containsKey("taskTerm")){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String  date=format.format(monivo.getTaskTerm());
			hsql.append(" AND m.taskTerm =to_date('"+date+"','yyyy-MM-dd')");
		}
		PageResults prs = this.objectDao.findPageByHsql(hsql.toString(), parameterMap, pageResults.getPageSize(), pageResults.getCurrentPage());
		return prs;
	}

	@Override
	public WorkTaskMoni findTaskMoni(Integer monid) {
		if(monid == null)return null;
		String hsql ="FROM WorkTaskMoni m WHERE m.taskMoniId = "+monid;
		WorkTaskMoni taskMoni = (WorkTaskMoni) this.objectDao.findObject(hsql);
		return taskMoni;
	}

	@Override
	public void updateTaskInfo(WorkTaskMoni moni) throws Exception {
		this.objectDao.update(moni);
	}

}
