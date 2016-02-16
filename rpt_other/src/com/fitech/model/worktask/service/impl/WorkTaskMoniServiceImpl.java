package com.fitech.model.worktask.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.AfReport;
import com.fitech.model.worktask.model.pojo.ReportIn;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoniId;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObjectMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObjectMoniId;
import com.fitech.model.worktask.service.IWorkTaskMoniService;
import com.fitech.model.worktask.service.WorkTaskPendingTaskService;

public class WorkTaskMoniServiceImpl extends DefaultBaseService<WorkTaskMoni, Long> implements IWorkTaskMoniService {
	
	private WorkTaskPendingTaskService workTaskPendingTaskService;

	@Override
	public WorkTaskMoni findOneWorkTaskMoni(Long taskMoniId) throws Exception {
		// TODO Auto-generated method stub
		WorkTaskMoni moni = null;
		if(taskMoniId!=null)
			moni = this.objectDao.read(taskMoniId);	
		return moni;
	}
	public WorkTaskPendingTaskService getWorkTaskPendingTaskService() {
		return workTaskPendingTaskService;
	}
	public void setWorkTaskPendingTaskService(
			WorkTaskPendingTaskService workTaskPendingTaskService) {
		this.workTaskPendingTaskService = workTaskPendingTaskService;
	}
	public void insertWorkTaskAll(Long taskMoniId,String taskTerm,Integer execFlag,boolean isDelExist) throws Exception {
		WorkTaskMoni moni = (WorkTaskMoni)this.getObjectDao().getHibernateTemplate().get(WorkTaskMoni.class, taskMoniId);
		String hsql = "from WorkTaskInfo where taskId=" + moni.getTaskId();
		WorkTaskInfo info = (WorkTaskInfo)this.findObject(hsql);
		if(moni == null){
			moni = new WorkTaskMoni();
			moni.setTaskId(info.getTaskId());
			moni.setTaskName(info.getTaskName());
			moni.setTaskTerm(DateUtil.getDateByString(taskTerm));
			moni.setOverFlag(WorkTaskConfig.MONI_OVER_FLAG_NOT);
			moni.setStartDate(DateUtil.getTodayTimeStamp());
			moni.setPriWorkTaskId(new Long(WorkTaskConfig.PRE_OBJECT_NULL));
			moni.setExecFlag(WorkTaskConfig.TASK_EXEC_FLAG_NOT);
			String[] strs = taskTerm.split("-");
			moni.setYear(Integer.parseInt(strs[0]));
			moni.setTerm(Integer.parseInt(strs[1]));
			moni.setDay(Integer.parseInt(strs[2]));
			this.save(moni);//保存执行任务表
		}
		hsql = "select w1.id.orgId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT
		+ " and w1.id.nodeId in (select nodeId from WorkTaskNodeInfo where taskId=" + info.getTaskId()+ ") group by w1.id.orgId";
		List<String> orgIdList = this.findListByHsql(hsql, null);
		for(int j=0;j<orgIdList.size();j++)
			this.insertWorkTaskByOrg(info,moni,null, orgIdList.get(j),DateUtil.getDateByString(taskTerm),isDelExist);
		if(isDelExist){
			hsql = "delete from WorkTaskNodeObjectMoni where id.orgId not in(" + StringUtil.converArrayToString(orgIdList,",","'") + ")";
			this.objectDao.deleteObjects(hsql);//删除不存在的节点对象
			hsql = "delete from WorkTaskNodeMoni where id.orgId not in(" + StringUtil.converArrayToString(orgIdList,",","'") + ")";
			this.objectDao.deleteObjects(hsql);//删除不存在的节点机构
		}
	}
	public void insertWorkTaskAll(String taskTerm,Integer execFlag,String busiLineId,boolean isDelExist) throws Exception {
		List<Integer> taskIdList = new ArrayList();
		String hsql = "";
		hsql = "select taskId from WorkTaskInfo where busiLineId='" + busiLineId + "' and trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + WorkTaskConfig.FREQ_DAY + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
		taskIdList.addAll(this.findListByHsql(hsql, null));
		if(DateUtil.isMonthDay(taskTerm)){
			hsql = "select taskId from WorkTaskInfo where busiLineId='" + busiLineId + "' and trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + WorkTaskConfig.FREQ_MONTH + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
			taskIdList.addAll(this.findListByHsql(hsql, null));
		}
		if(DateUtil.isSeasonDay(taskTerm)){
			hsql = "select taskId from WorkTaskInfo where busiLineId='" + busiLineId + "' and trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + WorkTaskConfig.FREQ_SEASON + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
			taskIdList.addAll(this.findListByHsql(hsql, null));
		}
		if(DateUtil.isHalfYearDay(taskTerm)){
			hsql = "select taskId from WorkTaskInfo where busiLineId='" + busiLineId + "' and trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + WorkTaskConfig.FREQ_HALF_YEAR + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
			taskIdList.addAll(this.findListByHsql(hsql, null));
		}
		if(DateUtil.isYearDay(taskTerm)){
			hsql = "select taskId from WorkTaskInfo where busiLineId='" + busiLineId + "' and trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + WorkTaskConfig.FREQ_YEAR + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
			taskIdList.addAll(this.findListByHsql(hsql, null));
		}
		if(DateUtil.isYearBegin(taskTerm)){
			hsql = "select taskId from WorkTaskInfo where busiLineId='" + busiLineId + "' and trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + WorkTaskConfig.FREQ_YEAR_BEGIN_CARRY + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
			taskIdList.addAll(this.findListByHsql(hsql, null));
		}
		if(taskIdList.size()!=0){
			for(int i=0;i<taskIdList.size();i++){
				Integer taskId = taskIdList.get(i);
				hsql = "from WorkTaskInfo where taskId=" + taskId;
				WorkTaskInfo info = (WorkTaskInfo)this.findObject(hsql);
			//	WorkTaskMoni moni = this.getWorkTaskMoni(taskIdList.get(i), taskTerm);
				WorkTaskMoni moni = null;
				moni = new WorkTaskMoni();
				moni.setTaskId(info.getTaskId());
				moni.setTaskName(info.getTaskName());
				moni.setTaskTerm(DateUtil.getDateByString(taskTerm));
				moni.setOverFlag(WorkTaskConfig.MONI_OVER_FLAG_NOT);
				moni.setStartDate(DateUtil.getTodayTimeStamp());
				moni.setPriWorkTaskId(new Long(WorkTaskConfig.PRE_OBJECT_NULL));
				moni.setExecFlag(WorkTaskConfig.TASK_EXEC_FLAG_NOT);
				String[] strs = taskTerm.split("-");
				moni.setYear(Integer.parseInt(strs[0]));
				moni.setTerm(Integer.parseInt(strs[1]));
				moni.setDay(Integer.parseInt(strs[2]));
				this.save(moni);//保存执行任务表
				hsql = "select w1.id.orgId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT
				+ " and w1.id.nodeId in (select nodeId from WorkTaskNodeInfo where taskId=" + taskIdList.get(i)+ ") group by w1.id.orgId";
				List<String> orgIdList = this.findListByHsql(hsql, null);
				for(int j=0;j<orgIdList.size();j++)
					this.insertWorkTaskByOrg(info,moni,null, orgIdList.get(j),DateUtil.getDateByString(taskTerm),isDelExist);
				hsql = "select count(*) from WorkTaskNodeMoni w where w.id.taskMoniId=" + moni.getTaskMoniId().intValue();
				int countForNodeMoni = ((Long)this.findObject(hsql)).intValue();
				if(countForNodeMoni==0)
					this.delete(moni);//如果该任务监控下没有节点，则删除该任务监控对象
				if(isDelExist && orgIdList!=null && orgIdList.size()!=0){
					String[] dateStr = taskTerm.split("-");
					int year = Integer.parseInt(dateStr[0]);
					int term = Integer.parseInt(dateStr[1]);
					int day =  Integer.parseInt(dateStr[2]);
					int freqId = StringUtil.freqConvert(info.getFreqId());
					hsql = "from WorkTaskNodeObjectMoni where id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + taskTerm + "','" + DateUtil.NORMALDATE + "')) and  id.orgId not in(" + StringUtil.converArrayToString(orgIdList,",","'") + ")";
					/**************更新被删除机构管理报表的状态(begin)*****************/
					List<Object[]> notExistChildList = this.objectDao.findListByHsql("select id.orgId,id.templateId " + hsql + " group by id.orgId,id.templateId", null);
					if(notExistChildList!=null && notExistChildList.size()!=0){
						for(int m=0;m<notExistChildList.size();m++){
							Object[] os = (Object[])notExistChildList.get(m);
							String orgId_del = (String)os[0];
							String templateId_del = (String)os[1];
							String updatesql1 = "from ReportIn ri where ri.dataRangeId in (select mr.id.dataRangeId from MActuRep mr where mr.id.childRepId=ri.childRepId and mr.id.versionId=ri.versionId and mr.id.repFreqId=" + freqId + ") and ri.orgId='" + orgId_del + "' and ri.year=" + year + " and ri.term=" + term + " and ri.childRepId='" + templateId_del + "'";//更新不存在的输出银监报表状态为初始状态
							ReportIn ri = (ReportIn)this.objectDao.findObject(updatesql1);
							if(ri!=null){
								ri.setCheckFlag(3);
								this.objectDao.update(ri);
							}
							String updatesql2 = "from AfReport ar where ar.repFreqId=" + new BigDecimal(freqId) + " and ar.orgId='" + orgId_del + "' and ar.year=" + new BigDecimal(year) + " and ar.term=" + new BigDecimal(term) + " and ar.day=" + new BigDecimal(day) + " and ar.templateId='" + templateId_del + "'";//更新不存在的输出人行报表状态为初始状态
							AfReport afReport = (AfReport)this.objectDao.findObject(updatesql2);
							if(afReport!=null){
								afReport.setCheckFlag(new BigDecimal(3));
								this.objectDao.update(afReport);
							}
						}
					}
					/**************更新被删除机构管理报表的状态(end)*****************/
					this.objectDao.deleteObjects(hsql);//删除不存在的机构节点对象
					hsql = "delete from WorkTaskNodeMoni where id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + taskTerm + "','" + DateUtil.NORMALDATE + "')) and  id.orgId not in(" + StringUtil.converArrayToString(orgIdList,",","'") + ")";
					this.objectDao.deleteObjects(hsql);//删除不存在的节点机构
				}
			}
		}
	}
	public void insertWorkTaskAll(String freqId,boolean isDelExist) throws Exception {
		String taskTerm = DateUtil.getTodayDateStr();
//		if(!freqId.equals(WorkTaskConfig.FREQ_DAY) && !freqId.equals(WorkTaskConfig.FREQ_YEAR_BEGIN_CARRY))
			taskTerm = DateUtil.getLastDay((taskTerm));
		List<Integer> taskIdList = null;
		String hsql = "";
		hsql = "select taskId from WorkTaskInfo where trrigerId='" + WorkTaskConfig.TRRIGER_TYPE_AUTO + "' and freqId='" + freqId + "' and publicFlag=" + WorkTaskConfig.IS_PUBLIC_FLAG_YES + " and startDate<=to_date('" + taskTerm + "','yyyy-mm-dd') and to_date('" + taskTerm + "','yyyy-mm-dd')<=endDate" ;
		taskIdList = this.findListByHsql(hsql, null);
		if(taskIdList.size()!=0){
			for(int i=0;i<taskIdList.size();i++){
				Integer taskId = taskIdList.get(i);
				hsql = "from WorkTaskInfo where taskId=" + taskId;
				WorkTaskInfo info = (WorkTaskInfo)this.findObject(hsql);
				WorkTaskMoni moni = null;
				moni = new WorkTaskMoni();
				moni.setTaskName(info.getTaskName());
				moni.setTaskId(info.getTaskId());
				moni.setTaskTerm(DateUtil.getDateByString(taskTerm));
				moni.setOverFlag(WorkTaskConfig.MONI_OVER_FLAG_NOT);
				moni.setStartDate(DateUtil.getTodayTimeStamp());
				moni.setPriWorkTaskId(new Long(WorkTaskConfig.PRE_OBJECT_NULL));
				moni.setExecFlag(WorkTaskConfig.TASK_EXEC_FLAG_YES);
				String[] strs = taskTerm.split("-");
				moni.setYear(Integer.parseInt(strs[0]));
				moni.setTerm(Integer.parseInt(strs[1]));
				moni.setDay(Integer.parseInt(strs[2]));
				this.save(moni);//保存执行任务表
				hsql = "select w1.id.orgId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT
				+ " and w1.id.nodeId in (select nodeId from WorkTaskNodeInfo where taskId=" + taskIdList.get(i)+ ") group by w1.id.orgId";
				List<String> orgIdList = this.findListByHsql(hsql, null);
				for(int j=0;j<orgIdList.size();j++)
					this.insertWorkTaskByOrg(info,moni,null, orgIdList.get(j),moni.getTaskTerm(),isDelExist);
				hsql = "select count(*) from WorkTaskNodeMoni w where w.id.taskMoniId=" + moni.getTaskMoniId().intValue();
				int countForNodeMoni = ((Long)this.findObject(hsql)).intValue();
				if(countForNodeMoni==0)
					this.delete(moni);//如果该任务监控下没有节点，则删除该任务监控对象
				if(isDelExist){
					String[] dateStr = taskTerm.split("-");
					int year = Integer.parseInt(dateStr[0]);
					int term = Integer.parseInt(dateStr[1]);
					int day =  Integer.parseInt(dateStr[2]);
					int freqNumber = StringUtil.freqConvert(info.getFreqId());
					hsql = "from WorkTaskNodeObjectMoni where id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + taskTerm + "','" + DateUtil.NORMALDATE + "')) and id.orgId not in(" + StringUtil.converArrayToString(orgIdList,",","'") + ")";
					/**************更新被删除机构管理报表的状态(begin)*****************/
					List<Object[]> notExistChildList = this.objectDao.findListByHsql("select id.orgId,id.templateId " + hsql + " group by id.orgId,id.templateId", null);
					if(notExistChildList!=null && notExistChildList.size()!=0){
						for(int m=0;m<notExistChildList.size();m++){
							Object[] os = (Object[])notExistChildList.get(m);
							String orgId_del = (String)os[0];
							String templateId_del = (String)os[1];
							String updatesql1 = "from ReportIn ri where ri.dataRangeId in (select mr.id.dataRangeId from MActuRep mr where mr.id.childRepId=ri.childRepId and mr.id.versionId=ri.versionId and mr.id.repFreqId=" + freqNumber + ") and ri.orgId='" + orgId_del + "' and ri.year=" + year + " and ri.term=" + term + " and ri.childRepId='" + templateId_del + "'";//更新不存在的输出银监报表状态为初始状态
							ReportIn ri = (ReportIn)this.objectDao.findObject(updatesql1);
							if(ri!=null){
								ri.setCheckFlag(3);
								this.objectDao.update(ri);
							}
							String updatesql2 = "from AfReport ar where ar.repFreqId=" + new BigDecimal(freqNumber) + " and ar.orgId='" + orgId_del + "' and ar.year=" + new BigDecimal(year) + " and ar.term=" + new BigDecimal(term) + " and ar.day=" + new BigDecimal(day) + " and ar.templateId='" + templateId_del + "'";//更新不存在的输出人行报表状态为初始状态
							AfReport afReport = (AfReport)this.objectDao.findObject(updatesql2);
							if(afReport!=null){
								afReport.setCheckFlag(new BigDecimal(3));
								this.objectDao.update(afReport);
							}
						}
					}
					/**************更新被删除机构管理报表的状态(end)*****************/
					this.objectDao.deleteObjects(hsql);//删除不存在的节点机构对象
					hsql = "from WorkTaskNodeMoni where id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + taskTerm + "','" + DateUtil.NORMALDATE + "'))  and id.orgId not in(" + StringUtil.converArrayToString(orgIdList,",","'") + ")";
					this.objectDao.deleteObjects(hsql);//删除不存在的节点机构
				}
			}
		}
	}
	public void insertWorkTaskByOrg(WorkTaskInfo info,WorkTaskMoni moni,List<String> tempIdList,String orgId,Date taskTerm,boolean isDelExist)throws Exception{
		String hsql = "from WorkTaskNodeInfo where taskId=" + info.getTaskId();
		List<WorkTaskNodeInfo> nodeList = (List<WorkTaskNodeInfo>)this.findListByHsql(hsql, null); //取出所有节点定义
		for(int i=0;i<nodeList.size();i++){//循环插入节点待办事务
			WorkTaskNodeInfo nodeInfo = nodeList.get(i);
			if(isDelExist){
				if(tempIdList==null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String[] dateStr = format.format(taskTerm).split("-");
					int year = Integer.parseInt(dateStr[0]);
					int term = Integer.parseInt(dateStr[1]);
					int day =  Integer.parseInt(dateStr[2]);
					int freqId = StringUtil.freqConvert(info.getFreqId());
					String delhsql1 = "from WorkTaskNodeObjectMoni where id.orgId='" + orgId + "' and id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + DateUtil.dateToString(taskTerm) + "','" + DateUtil.NORMALDATE + "')) and id.nodeId=" + nodeInfo.getNodeId() + " and id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT + " and id.templateId not in("
					+ "select w1.id.templateId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT + " and w1.id.orgId='" + orgId + "'"
					+ " and w1.id.nodeId=" + nodeList.get(i).getNodeId()
					+ ")";
					List<String> notExistChildList = this.objectDao.findListByHsql("select id.templateId " + delhsql1, null);
					/****************更新任务相关联的输出报表状态(begin)*******************/
					if(notExistChildList!=null && notExistChildList.size()!=0){
						String updatesql1 = "from ReportIn ri where ri.dataRangeId in (select mr.id.dataRangeId from MActuRep mr where mr.id.childRepId=ri.childRepId and mr.id.versionId=ri.versionId and mr.id.repFreqId=" + freqId + ") and ri.orgId='" + orgId + "' and ri.year=" + year + " and ri.term=" + term + " and ri.childRepId in("
							+ StringUtil.converArrayToString(notExistChildList, ",","'")
							+ ")";//更新不存在的输出银监报表状态为初始状态
						List<ReportIn> ril = this.objectDao.findListByHsql(updatesql1, null);
						for(int j=0;j<ril.size();j++){
							ReportIn ri = ril.get(j);
							ri.setCheckFlag(3);
							this.objectDao.update(ri);
						}
						String updatesql2 = "from AfReport ar where ar.repFreqId=" + new BigDecimal(freqId) + " and ar.orgId='" + orgId + "' and ar.year=" + new BigDecimal(year) + " and ar.term=" + new BigDecimal(term) + " and ar.day=" + new BigDecimal(day) + " and ar.templateId in("
						    + StringUtil.converArrayToString(notExistChildList, ",","'")
							+ ")";//更新不存在的输出人行报表状态为初始状态
						List<AfReport> arl = this.objectDao.findListByHsql(updatesql2, null);
						for(int j=0;j<arl.size();j++){
							AfReport afReport = arl.get(j);
							afReport.setCheckFlag(new BigDecimal(3));
							this.objectDao.update(afReport);
						}
					}
					/****************更新任务相关联的输出报表状态(end)*******************/
					
					this.objectDao.deleteObjects(delhsql1);//删除在模板维护中被取消的节点输出模板
					
					String delhsql2 = "from WorkTaskNodeObjectMoni where id.orgId='" + orgId + "' and id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + DateUtil.dateToString(taskTerm) + "','" + DateUtil.NORMALDATE + "')) and id.nodeId=" + nodeInfo.getNodeId() + " and id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_IMPORT + " and id.templateId not in("
						+ "select w1.id.templateId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_IMPORT + " and w1.id.orgId='" + orgId + "'"
						+ " and w1.id.nodeId=" + nodeList.get(i).getNodeId()
						+ ")";
					notExistChildList = this.objectDao.findListByHsql("select id.templateId " + delhsql2, null);
					/****************更新任务相关联的输入报表状态(begin)*******************/
					if(notExistChildList!=null && notExistChildList.size()!=0){
						String updatesql1 = "from ReportIn ri where ri.dataRangeId in (select mr.id.dataRangeId from MActuRep mr where mr.id.childRepId=ri.childRepId and mr.id.versionId=ri.versionId and mr.id.repFreqId=" + freqId + ") and ri.orgId='" + orgId + "' and year=" + year + " and term=" + term + " and ri.childRepId in("
							+ StringUtil.converArrayToString(notExistChildList, ",", "'")
							+ ")";//更新不存在的输入人行报表状态为初始状态
						List<ReportIn> ril = this.objectDao.findListByHsql(updatesql1, null);
						for(int j=0;j<ril.size();j++){
							ReportIn ri = ril.get(j);
							ri.setCheckFlag(3);
							this.objectDao.update(ri);
						}
						String updatesql2 = "from AfReport ar where ar.repFreqId=" + new BigDecimal(freqId) + " and ar.orgId='" + orgId + "' and ar.year=" + new BigDecimal(year) + " and ar.term=" + new BigDecimal(term) + " and ar.day=" + new BigDecimal(day) + " and ar.templateId in("
							+ StringUtil.converArrayToString(notExistChildList, ",", "'")
							+ ")";//更新不存在的输入人行报表状态为初始状态
						List<AfReport> arl = this.objectDao.findListByHsql(updatesql2, null);
						for(int j=0;j<arl.size();j++){
							AfReport afReport = arl.get(j);
							afReport.setCheckFlag(new BigDecimal(3));
							this.objectDao.update(afReport);
						}
					}
					/****************更新任务相关联的输入报表状态(end)*******************/
					
					this.objectDao.deleteObjects(delhsql2);//删除在模板维护中被取消的节点输入模板
					
					
					String delhsql3 = "from WorkTaskNodeMoni wm where wm.id.orgId='" + orgId + "' and wm.id.taskMoniId in(" + "select w.taskMoniId from WorkTaskMoni w where w.taskId=" + info.getTaskId() + " and w.taskTerm=to_date('" + DateUtil.dateToString(taskTerm) + "','" + DateUtil.NORMALDATE + "')) and wm.id.nodeId=" + nodeInfo.getNodeId()
										+ " and wm.id.taskMoniId not in (select wom.id.taskMoniId from WorkTaskNodeObjectMoni wom where wom.id.taskMoniId=wm.id.taskMoniId and wom.id.orgId=wm.id.orgId and wom.id.nodeId=wm.id.nodeId)" ;
					this.objectDao.deleteObjects(delhsql3);//删除在模板维护中不存在的节点
				    
				}
			}
			List<String> templateIdList = null;
			if(tempIdList==null || tempIdList.size()==0){
				hsql = "select w1.id.templateId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT + " and w1.id.orgId='" + orgId + "'"
				 + " and w1.id.nodeId=" + nodeList.get(i).getNodeId()
				 + " and w1.id.templateId not in(select wom.id.templateId from WorkTaskMoni wm,WorkTaskNodeMoni w2,WorkTaskNodeObjectMoni wom where wm.taskMoniId=w2.id.taskMoniId and w2.id.taskMoniId=wom.id.taskMoniId and w2.id.nodeId=wom.id.nodeId and w2.id.orgId=wom.id.orgId and wom.id.nodeIoFlag=" 
				 + WorkTaskConfig.NODE_IO_FLAG_EXPORT 
				 + " and w2.id.nodeId=" + nodeList.get(i).getNodeId()
				 + " and w2.finalExecFlag=" + WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES 
				 + " and w2.id.orgId='" + orgId + "'" 
				 + " and wm.taskId=" + info.getTaskId() 
				 + " and wm.taskTerm=to_date('" + DateUtil.dateToString(taskTerm) + "','" + DateUtil.NORMALDATE + "')" + ")";
				templateIdList = (List<String>)this.findListByHsql(hsql, null);
			}else{
				templateIdList = tempIdList;
			}
			Integer repTimeLimit = null;
			if(info.getTaskTypeId().trim().equals(WorkTaskConfig.TASK_TYPE_HZRW)){//如果是汇总流程
				hsql = "select repTimeLimit from WorkTaskNodeRepTime where id.nodeId=" + nodeInfo.getNodeId() + " and id.orgLevel in (select id.orgLevel from ViewWorktaskOrg where id.orgId='" + orgId + "')";
				repTimeLimit = (Integer)this.objectDao.findObject(hsql);
			}else{
				repTimeLimit = nodeInfo.getNodeTime();
			}
			if(repTimeLimit==null)
				repTimeLimit = 0;
			Date lateRepDate = DateUtil.getNextDay(taskTerm, info.getTriggerShifting() + repTimeLimit);//默认上报报表时间单位为日
			if(WorkTaskConfig.REPORT_TIME_UNIT.equals(WorkTaskConfig.REPORT_TIME_UNIT_HOUR)){
				Calendar cal=Calendar.getInstance();
				cal.setTime(taskTerm);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(format.format(taskTerm));
				cal.add(Calendar.HOUR,(24 + info.getTriggerShifting().intValue() + repTimeLimit.intValue()));
				lateRepDate = cal.getTime();
				System.out.println(format.format(lateRepDate));
			}
			WorkTaskNodeMoniId id = new WorkTaskNodeMoniId();
			id.setTaskMoniId(moni.getTaskMoniId());
			id.setNodeId(nodeInfo.getNodeId());
			id.setOrgId(orgId);
			id.setPerformNumber(1);
//			WorkTaskNodeMoni nodeMoni = (WorkTaskNodeMoni)this.objectDao.getHibernateTemplate().get(WorkTaskNodeMoni.class, id);
			WorkTaskNodeMoni nodeMoni = null;
			nodeMoni = new WorkTaskNodeMoni();
			nodeMoni.setId(id);
			nodeMoni.setStartDate(new java.sql.Timestamp(DateUtil.getTodayDate().getTime()));
			nodeMoni.setEndDate(new java.sql.Timestamp(DateUtil.getTodayDate().getTime()));
			nodeMoni.setFinalExecFlag(WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
			nodeMoni.setBusiLine(info.getBusiLineId());
			nodeMoni.setPreTaskId(info.getPreTaskId());
			nodeMoni.setPreNodeId(nodeInfo.getPreNodeId());
			if(nodeInfo.getPreNodeId().intValue()==WorkTaskConfig.PRE_OBJECT_NULL)
				nodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
			else
				nodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_WAIT);
			nodeMoni.setRerepFlag(WorkTaskConfig.REREP_FLAG_NOT);
			nodeMoni.setLateRepFlag(WorkTaskConfig.LATE_REP_FLAG_NOT);
			nodeMoni.setLeavRepFlag(WorkTaskConfig.LEAV_REP_FLAG_NOT);
			nodeMoni.setLateRepDate(lateRepDate);
			this.objectDao.save(nodeMoni);//保存任务节点执行表
			for(int j=0;j<templateIdList.size();j++){
				String templateId = templateIdList.get(j);
				WorkTaskNodeObjectMoniId workNodeObjectId = new WorkTaskNodeObjectMoniId();
				workNodeObjectId.setTaskMoniId(moni.getTaskMoniId());
				workNodeObjectId.setNodeId(nodeInfo.getNodeId());
				workNodeObjectId.setOrgId(orgId);
				workNodeObjectId.setNodeIoFlag(WorkTaskConfig.NODE_IO_FLAG_EXPORT);
				workNodeObjectId.setTemplateId(templateId);
				WorkTaskNodeObjectMoni workNodeObject = null;
				workNodeObject = new WorkTaskNodeObjectMoni();
				workNodeObject.setId(workNodeObjectId);
				this.objectDao.save(workNodeObject);
			}
			hsql = "select w1.id.templateId from WorkTaskNodeObject w1 where w1.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_IMPORT + " and w1.id.orgId='" + orgId + "'"
			 + " and w1.id.nodeId=" + nodeList.get(i).getNodeId()
			 + " and w1.id.templateId not in(select wom.id.templateId from WorkTaskMoni wm,WorkTaskNodeMoni w2,WorkTaskNodeObjectMoni wom where wm.taskMoniId=w2.id.taskMoniId and w2.id.taskMoniId=wom.id.taskMoniId and w2.id.nodeId=wom.id.nodeId and w2.id.orgId=wom.id.orgId and wom.id.nodeIoFlag=" 
				 + WorkTaskConfig.NODE_IO_FLAG_IMPORT 
				 + " and w2.id.nodeId=" + nodeList.get(i).getNodeId()
				 + " and w2.finalExecFlag=" + WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES 
				 + " and w2.id.orgId='" + orgId + "'" 
				 + " and wm.taskId=" + info.getTaskId() 
				 + " and wm.taskTerm=to_date('" + DateUtil.dateToString(taskTerm) + "','" + DateUtil.NORMALDATE + "')" + ")";
			templateIdList = (List<String>)this.findListByHsql(hsql, null);
			for(int j=0;j<templateIdList.size();j++){
				String templateId = templateIdList.get(j);
				WorkTaskNodeObjectMoniId workNodeObjectId = new WorkTaskNodeObjectMoniId();
				workNodeObjectId.setTaskMoniId(moni.getTaskMoniId());
				workNodeObjectId.setNodeId(nodeInfo.getNodeId());
				workNodeObjectId.setOrgId(orgId);
				workNodeObjectId.setNodeIoFlag(WorkTaskConfig.NODE_IO_FLAG_IMPORT);
				workNodeObjectId.setTemplateId(templateId);
				WorkTaskNodeObjectMoni workNodeObject = null;
				workNodeObject = new WorkTaskNodeObjectMoni();
				workNodeObject.setId(workNodeObjectId);
				this.objectDao.save(workNodeObject);
			}
			hsql = "select count(*) from WorkTaskNodeObjectMoni wn where wn.id.nodeIoFlag=" + WorkTaskConfig.NODE_IO_FLAG_EXPORT + " and wn.id.taskMoniId=" + id.getTaskMoniId().intValue() + " and wn.id.nodeId=" + id.getNodeId().intValue() + " and wn.id.orgId='" + id.getOrgId() + "'";
			int tempCount = ((Long)this.objectDao.findObject(hsql)).intValue();
			if(tempCount==0){
				this.objectDao.delete(nodeMoni);//删除多余的节点监控
				hsql = "from WorkTaskNodeObjectMoni wn where wn.id.taskMoniId=" + id.getTaskMoniId().intValue() + " and wn.id.nodeId=" + id.getNodeId().intValue() + " and wn.id.orgId='" + id.getOrgId() + "'";
				this.objectDao.deleteObjects(hsql);
			}
			//		this.workTaskPendingTaskService.performByNodeId(nodeInfo.getNodeId(), DateUtil.dateToString(taskTerm), orgId);
		}
	}
	public WorkTaskMoni insertWorkTaskSplit(List<String> notPassTempateIdList,Integer taskMoniId,Integer nodeId,String orgId,Date taskTerm)throws Exception{
		if(notPassTempateIdList==null || notPassTempateIdList.size()==0)
			return null;
		String hsql = "delete from WorkTaskNodeObjectMoni where id.taskMoniId=" + taskMoniId + " and id.orgId='" + orgId + "' and id.templateId in(" + StringUtil.converArrayToString(notPassTempateIdList,",","'") + ")";
		this.objectDao.deleteObjects(hsql); //在原先任务的所有节点中删除未处理的报表
		WorkTaskMoni moniOld = (WorkTaskMoni)this.objectDao.findObject("from WorkTaskMoni where taskMoniId=" + taskMoniId);
		WorkTaskInfo info = (WorkTaskInfo)this.objectDao.findObject("from WorkTaskInfo where taskId=" + moniOld.getTaskId());
		WorkTaskMoni moni = new WorkTaskMoni();
		moni.setTaskName(info.getTaskName());
		moni.setTaskId(moniOld.getTaskId());
		moni.setTaskTerm(moniOld.getTaskTerm());
		moni.setOverFlag(WorkTaskConfig.MONI_OVER_FLAG_NOT);
		moni.setStartDate(DateUtil.getTodayTimeStamp());
		moni.setPriWorkTaskId(new Long(WorkTaskConfig.PRE_OBJECT_NULL));//前置对象为空
		moni.setExecFlag(WorkTaskConfig.TASK_EXEC_FLAG_YES);
		String[] strs = DateUtil.dateToString(taskTerm).split("-");//年月日
		moni.setYear(Integer.parseInt(strs[0]));
		moni.setTerm(Integer.parseInt(strs[1]));
		moni.setDay(Integer.parseInt(strs[2]));
		this.save(moni);//保存未通过报表执行任务表
		this.insertWorkTaskByOrg(info, moni, notPassTempateIdList, orgId, taskTerm,false);
		WorkTaskNodeInfo currNodeInfo = (WorkTaskNodeInfo)this.objectDao.findObject("from WorkTaskNodeInfo where nodeId=" + nodeId );//获取当前节点对象基础信息
		//获取当前新添加任务节点
		WorkTaskNodeMoni currNodeMoni = (WorkTaskNodeMoni)this.objectDao.findObject("from WorkTaskNodeMoni where id.taskMoniId=" + moni.getTaskMoniId().intValue() + " and id.nodeId=" + currNodeInfo.getNodeId() + " and id.orgId='" + orgId + "' and finalExecFlag=" + WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES);
		currNodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_WADC);
		this.objectDao.update(currNodeMoni);//将当前节点更新为待处理状态
		Integer proNodeId = currNodeInfo.getPreNodeId();
		while(proNodeId!=-1){
			WorkTaskNodeInfo lastNodeInfo = (WorkTaskNodeInfo)this.objectDao.findObject("from WorkTaskNodeInfo where nodeId=" + proNodeId); 
			hsql = "from WorkTaskNodeMoni where id.taskMoniId=" + moni.getTaskMoniId().intValue() + " and id.nodeId=" + lastNodeInfo.getNodeId() + " and id.orgId='" + orgId + "' and finalExecFlag=" + WorkTaskConfig.NODE_MONI_FINAL_FLAG_YES;
			WorkTaskNodeMoni lastNodeMoni = (WorkTaskNodeMoni)this.objectDao.findObject(hsql);//获取上一个节点监控对象
			if(proNodeId==currNodeInfo.getPreNodeId())//如果是当前节点的上一个对象
				lastNodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_COMM);//则置为已经提交
			else
				lastNodeMoni.setNodeFlag(WorkTaskConfig.NODE_FLAG_PASS);//否则置为审核通过
			this.objectDao.update(lastNodeMoni);
//			for(int j=0;j<notPassTempateIdList.size();j++){
//				String templateId = notPassTempateIdList.get(j);
//				WorkTaskNodeObjectMoni workNodeObject = new WorkTaskNodeObjectMoni();
//				WorkTaskNodeObjectMoniId workNodeObjectId = new WorkTaskNodeObjectMoniId();
//				workNodeObjectId.setTaskMoniId(moni.getTaskMoniId());
//				workNodeObjectId.setNodeId(lastNodeInfo.getNodeId());
//				workNodeObjectId.setOrgId(orgId);
//				workNodeObjectId.setNodeIoFlag(WorkTaskConfig.NODE_IO_FLAG_IMPORT);
//				workNodeObjectId.setTemplateId(templateId);
//				workNodeObject.setId(workNodeObjectId);
//				this.objectDao.save(workNodeObject);
//			}
			proNodeId = lastNodeInfo.getPreNodeId();//获取上一个节点对象
		}
		return moni;
	}
	public void insertWorkTaskSplitByManual(List<String> tempateIdList,Integer taskMoniId,Integer nodeId,String orgId,Date taskTerm,String inputTaskName)throws Exception{
		WorkTaskMoni moni = this.insertWorkTaskSplit(tempateIdList, taskMoniId, nodeId, orgId, taskTerm);
		moni.setTaskName(inputTaskName);
		this.objectDao.update(moni);
//		String hsql = "select count(*) from WorkTaskNodeObjectMoni where id.taskMoniId=" + taskMoniId + " and id.orgId='" + orgId + "' and id.templateId in(" + StringUtil.converArrayToString(tempateIdList,",","'") + ")";
//		Integer remainObject = (Integer)this.objectDao.findObject(hsql);
//		if(remainObject.intValue()==0){//判断原任务拆分后的任务报表是否为0，如果为零则删除原任务节点
//			String delSql = "delete from WorkTaskNodeMoni where id.taskMoniId=" + taskMoniId + " and id.orgId='" + orgId+"'";
//			this.objectDao.deleteObjects(delSql);
//		}
//		hsql = "select count(*) from WorkTaskNodeMoni where id.taskMoniId=" + taskMoniId;
//		Integer remainNodeMnoi = (Integer)this.objectDao.findObject(hsql);
//		if(remainNodeMnoi.intValue()==0){//判断原任务拆分后的节点是否为0，如果为零则删除原任务
//			String delSql = "delete from WorkTaskMoni where taskMoniId=" + taskMoniId;
//			this.objectDao.deleteObjects(delSql);
//		}
	}
	private WorkTaskMoni getWorkTaskMoni(Integer taskId,String taskTerm ) throws Exception{
		WorkTaskMoni result = null;
		String hsql = "from WorkTaskMoni w where w.taskId=" + taskId + " and w.taskTerm=to_date('" + taskTerm + "','" + DateUtil.NORMALDATE + "')";
		result = (WorkTaskMoni)this.objectDao.findObject(hsql);
		return result;
	}
	@Override
	public WorkTaskMoni insertWorkTaskSplitByManualnew(List<String> tempateIdList,Integer taskMoniId,Integer nodeId,String orgId,Date taskTerm,String inputTaskName)throws Exception{
		WorkTaskMoni moni = this.insertWorkTaskSplit(tempateIdList, taskMoniId, nodeId, orgId, taskTerm);
		moni.setTaskName(inputTaskName);
		this.objectDao.update(moni);
		return moni;
	}
}
