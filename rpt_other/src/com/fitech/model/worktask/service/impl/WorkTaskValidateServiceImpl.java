package com.fitech.model.worktask.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.service.IWorkTaskValidateService;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

public class WorkTaskValidateServiceImpl extends DefaultBaseService<WorkTaskNodeMoni, String>
		implements IWorkTaskValidateService {

	@Override
	public void resetByFill(WorkTaskPendingTaskVo vo,List passTemplateIdList) throws Exception {
		// TODO Auto-generated method stub
		if(vo!=null && vo.getBusiLine()!=null &&
				!vo.getBusiLine().equals("")){
			String tempIds="";
			String notPassTempIds="";
			String taskInfoSql="select i.taskName ,i.taskTypeId ,i.freqId  from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			if(taskInfoList!=null&&taskInfoList.size()>0){
				Object[] obj=(Object[])taskInfoList.get(0);
				vo.setFreqIds(StringUtil.freqConvert(((String)obj[2])));
			}
			
			String[] tempIdArray=vo.getTemplateIds().split(",");
			for (int j = 0; j < tempIdArray.length; j++) {
				if (!"".equals(tempIds)) {
					tempIds+=",";
				}
				tempIds+="'"+tempIdArray[j]+"'";
			}
			for (int i = 0; passTemplateIdList!=null&&i <passTemplateIdList.size(); i++) {
				if(!"".equals(notPassTempIds)) notPassTempIds+=",";
				notPassTempIds+="'"+passTemplateIdList.get(i)+"'";
			}
			if(vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_YJTX)){
				String yjBusiLine="update report_in a set a.check_flag=3 where  a.data_range_id in( select b.data_range_id from m_actu_rep b where b.child_rep_id=a.child_rep_id and b.version_id=a.version_id and b.rep_freq_id="+vo.getFreqIds()+") and  a.org_id='"+vo.getOrgId()+"'  and a.child_rep_id" +
				" in ("+tempIds+")and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
				if(!"".equals(notPassTempIds)){
					yjBusiLine+=" and a.child_rep_id not in("+notPassTempIds+")";
				}
				this.updateBysql(yjBusiLine);
			}else if(vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_RHTX)||vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_QTTX)){
				String rhBusiLine="update af_report set check_flag=3 where  rep_freq_id="+vo.getFreqIds()+" and  org_id='"+vo.getOrgId()+"' and template_id " +
				"in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
				if(!"".equals(notPassTempIds)){
					
					rhBusiLine+="  and template_id not in("+notPassTempIds+")";
				}
				this.updateBysql(rhBusiLine);
			}
			updateReportDate(vo,passTemplateIdList);
		}
	}
	
	@Override
	public void updateReportDate(WorkTaskPendingTaskVo vo,
			List passTemplateIdList) throws Exception {
		// TODO Auto-generated method stub
		if(vo!=null && vo.getBusiLine()!=null &&
				!vo.getBusiLine().equals("")){
			String tempIds="";
			String notPassTempIds="";
			String taskInfoSql="select i.taskName ,i.taskTypeId ,i.freqId  from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			if(taskInfoList!=null&&taskInfoList.size()>0){
				Object[] obj=(Object[])taskInfoList.get(0);
				vo.setFreqIds(StringUtil.freqConvert((String)obj[2]));
			}
			String[] tempIdArray=vo.getTemplateIds().split(",");
			for (int j = 0; j < tempIdArray.length; j++) {
				if (!"".equals(tempIds)) {
					tempIds+=",";
				}
				tempIds+="'"+tempIdArray[j]+"'";
			}
			for (int i = 0; passTemplateIdList!=null&&i <passTemplateIdList.size(); i++) {
				if(!"".equals(notPassTempIds)) notPassTempIds+=",";
				notPassTempIds+="'"+passTemplateIdList.get(i)+"'";
			}
			String reportDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getEndDate());
			if(vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_YJTX)){
				String yjBusiLine="update report_in a set a.report_date=to_date('"+reportDate+"','yyyy-MM-dd hh24:mi:ss') where a.data_range_id in (select b.data_range_id from m_actu_rep b where b.child_rep_id=a.child_rep_id and b.version_id=a.version_id and b.rep_freq_id="+vo.getFreqIds()+") and a.org_id='"+vo.getOrgId()+"'  and a.child_rep_id" +
				" in ("+tempIds+")and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
				if(!"".equals(notPassTempIds)){
					yjBusiLine+=" and a.child_rep_id not in("+notPassTempIds+")";
				}
				this.updateBysql(yjBusiLine);
			}else if(vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_RHTX)||vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_QTTX)){
				String rhBusiLine="update af_report set report_date=to_date('"+reportDate+"','yyyy-MM-dd hh24:mi:ss') where rep_freq_id="+vo.getFreqIds()+" and org_id='"+vo.getOrgId()+"' and template_id " +
				"in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
				if(!"".equals(notPassTempIds)){
					
					rhBusiLine+="  and template_id not in("+notPassTempIds+")";
				}
				this.updateBysql(rhBusiLine);
			}
		}
	}

	@Override
	public void resetByCheck(WorkTaskPendingTaskVo vo,List passTemplateIdList) throws Exception {
		// TODO Auto-generated method stub
		if(vo!=null && vo.getBusiLine()!=null &&
				!vo.getBusiLine().equals("")){
			String tempIds="";
			String notPassTempIds="";
			String taskInfoSql="select i.taskName ,i.taskTypeId ,i.freqId  from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			if(taskInfoList!=null&&taskInfoList.size()>0){
				Object[] obj=(Object[])taskInfoList.get(0);
				vo.setFreqIds(StringUtil.freqConvert((String)obj[2]));
			}
			String[] tempIdArray=vo.getTemplateIds().split(",");
			for (int j = 0; j < tempIdArray.length; j++) {
				if (!"".equals(tempIds)) {
					tempIds+=",";
				}
				tempIds+="'"+tempIdArray[j]+"'";
			}
			for (int i = 0; passTemplateIdList!=null&&i <passTemplateIdList.size(); i++) {
				if(!"".equals(notPassTempIds)) notPassTempIds+=",";
				notPassTempIds+="'"+passTemplateIdList.get(i)+"'";
			}
			if(vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_YJTX)){
				String yjBusiLine="update report_in a set a.check_flag=0 where a.data_range_id in(select b.data_range_id from m_actu_rep b where b.child_rep_id=a.child_rep_id and a.version_id=b.version_id and b.rep_freq_id ="+vo.getFreqIds()+") and a.org_id='"+vo.getOrgId()+"'  and a.child_rep_id" +
				" in ("+tempIds+")and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
				if(!"".equals(notPassTempIds)){
					yjBusiLine+=" and a.child_rep_id not in("+notPassTempIds+")";
				}
				this.updateBysql(yjBusiLine);
			}else if(vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_RHTX)||vo.getBusiLine().equals(WorkTaskConfig.BUSI_LINE_QTTX)){
				String rhBusiLine="update af_report set check_flag=0 where rep_freq_id="+vo.getFreqIds()+" and org_id='"+vo.getOrgId()+"' and template_id " +
				"in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
				if(!"".equals(notPassTempIds)){
					
					rhBusiLine+="  and template_id not in("+notPassTempIds+")";
				}
				this.updateBysql(rhBusiLine);
			}
			updateReportDate(vo,passTemplateIdList);
		}
	}

	@Override
	public String validate(List<WorkTaskPendingTaskVo> pvos) throws Exception {
		// TODO Auto-generated method stub
		String msg="";
label1:	for (int i = 0; i < pvos.size(); i++) {
			WorkTaskPendingTaskVo vo=pvos.get(i);
			String tempIds="";
			
			String[] tempIdArray=vo.getTemplateIds().split(",");
			for (int n = 0; n < tempIdArray.length; n++) {
				if (!"".equals(tempIds)) {
					tempIds+=",";
				}
				tempIds+="'"+tempIdArray[n]+"'";
			}
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String taskTerm=format.format(vo.getTaskTerm());
			String nodeInfoHql="from WorkTaskNodeInfo ni where ni.nodeId="+vo.getNodeId();//查找节点信息表，
			String taskInfoSql="select i.taskName ,i.taskTypeId,i.freqId from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			for (int j = 0; j < taskInfoList.size(); j++) {
				Object[] obj=(Object[])taskInfoList.get(j);
				vo.setTaskName((String)obj[0]);
				vo.setWorkTypeId((String)obj[1]);
				vo.setFreqIds(StringUtil.freqConvert((String)obj[2]));
			}
			WorkTaskNodeInfo nodeInfo=(WorkTaskNodeInfo)this.findObject(nodeInfoHql);
			String condTypeId=nodeInfo.getCondTypeId(); 
//			String condTypeId="0";
		if("hzrw".equals(vo.getWorkTypeId())){
			if ("rhtx".equals(vo.getBusiLine())||WorkTaskConfig.BUSI_LINE_QTTX.equals(vo.getBusiLine())) {
					
					if ("fill".equals(condTypeId)) {//0 代表填报，1代表审核
						//查询不需要强制校验的报表
						String afReportSql="select a.check_flag,a.rep_name,b.org_name,a.tbl_inner_validate_flag,a.tbl_outer_validate_flag  from af_report a,af_org b  where a.times=1  and rep_freq_id="+vo.getFreqIds()+" and a.org_id=b.org_id  and a.org_id=b.org_id and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
						
						List afReportList=this.findListBySql(afReportSql, null);
						
						if ((afReportList==null||afReportList.size()==0)) {
							return msg+="未处理【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表";
						}
						//遍历不需要强制校验的报表
						for (int j = 0;afReportList!=null&& j < afReportList.size(); j++) {
							Object[] obj=(Object[])afReportList.get(j);
							String checkFlag=((BigDecimal)obj[0]).toString();
							String repName=(String)obj[1];
							String orgName=(String)obj[2];
							if("1".equals(checkFlag)){
								continue;
							}
							if(!"0".equals(checkFlag)){//3代表未报送，4代表已填报
								if(!"".equals(msg))  msg+=",";
								msg+="【"+vo.getTaskName()+"】任务下,期数为【"+taskTerm+"】,机构名称是【"+orgName+"】下的【"+repName+"】未报送";;
								return msg;
							}
						
					}
						}else if("check".equals(condTypeId)){
						//查询不需要强制校验的报表
						String afReportSql="select a.check_flag,a.rep_name,b.org_name,a.tbl_inner_validate_flag,a.tbl_outer_validate_flag  from af_report a,af_org b  where a.times=1 and rep_freq_id="+vo.getFreqIds()+"   and a.org_id=b.org_id  and a.org_id=b.org_id and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
						
						List afReportList=this.findListBySql(afReportSql, null);
						
						if ((afReportList==null||afReportList.size()==0)) {
							return msg+="未处理【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表";
						}
						//遍历不需要强制校验的报表
						for (int j = 0;afReportList!=null&& j < afReportList.size(); j++) {
							Object[] obj=(Object[])afReportList.get(j);
							String checkFlag=((BigDecimal)obj[0]).toString();
							String repName=(String)obj[1];
							String orgName=(String)obj[2];
						if(!"1".equals(checkFlag)){//1代表已审核
							if(!"".equals(msg))  msg+=",";
							msg+="【"+vo.getTaskName()+"】任务下,期数为【"+taskTerm+"】,机构名称是【"+orgName+"】下的【"+repName+"】审核未通过，请审核";
							return msg;
						}
					}
				}
	
			}else  if("yjtx".equals(vo.getBusiLine())){
					if ("fill".equals(condTypeId)) {//0 代表填报，1代表审核
						//查询强制校验
						String reportInSql="select a.check_flag,a.rep_name,c.org_name from report_in a,m_actu_rep b,af_org c where a.times=1 and a.org_id=c.org_id and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
						List reportInList=this.findListBySql(reportInSql, null);
						if(reportInList==null||reportInList.size()==0 || reportInList.size()<tempIds.split(",").length){//需要加入未生成报表的报送判断
							return msg+="未处理【"+vo.getTaskName()+"】任务下的【"+vo.getTaskTerm()+"】期的报表";
						}
						for (int j = 0; reportInList!=null&&j < reportInList.size(); j++) {
							Object[] obj=(Object[])reportInList.get(j);
							String checkFlag=((BigDecimal)obj[0]).toString();
							String repName=(String)obj[1];
							String orgName=(String)obj[2];
							if("1".equals(checkFlag)) continue;
						if(!"0".equals(checkFlag)){//0代表已填报
							if(!"".equals(msg))  msg+=",";
							msg+="【"+vo.getTaskName()+"】任务下,期数为【"+taskTerm+"】,机构名称是【"+orgName+"】下的【"+repName+"】未报送";
							return msg;
						}
					}
						}else if("check".equals(condTypeId)){
						//查询强制校验
						String reportInSql="select a.check_flag,a.rep_name,c.org_name from report_in a,m_actu_rep b ,af_org c where a.times=1  and a.org_id=c.org_id and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+"  and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
						List reportInList=this.findListBySql(reportInSql, null);
						if(reportInList==null||reportInList.size()==0){
							return msg+="未处理【"+vo.getTaskName()+"】任务下的【"+vo.getTaskTerm()+"】期的报表";
						}
						for (int j = 0; reportInList!=null&&j < reportInList.size(); j++) {
							Object[] obj=(Object[])reportInList.get(j);
							String checkFlag=((BigDecimal)obj[0]).toString();
							String repName=(String)obj[1];
							String orgName=(String)obj[2];
						if(!"1".equals(checkFlag)){//1代表已审核
							if(!"".equals(msg))  msg+=",";
							msg+="【"+vo.getTaskName()+"】任务下,期数为【"+taskTerm+"】,机构名称是【"+orgName+"】下的【"+repName+"】审核未通过，请审核";
							return msg;
						}
					}
				}
		}
			}
		else if("djsh".equals(vo.getWorkTypeId())){

			if ("rhtx".equals(vo.getBusiLine())||WorkTaskConfig.BUSI_LINE_QTTX.equals(vo.getBusiLine())) {
				//查询不需要强制校验的报表
				int countFill=0;//用来计算填报个数
				int countCheck=0;//用来计算审核个数
				String afReportSql="select a.check_flag,a.rep_name,b.org_name,a.tbl_inner_validate_flag,a.tbl_outer_validate_flag  from af_report a,af_org b  where a.times=1 and a.org_id=b.org_id  and rep_freq_id="+vo.getFreqIds()+" and a.org_id=b.org_id and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
				
				List afReportList=this.findListBySql(afReportSql, null);
				
				if ((afReportList==null||afReportList.size()==0)) {
					return msg+="未处理【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表";
				}
				//遍历不需要强制校验的报表
				for (int j = 0;afReportList!=null&& j < afReportList.size(); j++) {
						Object[] obj=(Object[])afReportList.get(j);
						String checkFlag=((BigDecimal)obj[0]).toString();
						String repName=(String)obj[1];
						String orgName=(String)obj[2];
					
					if ("fill".equals(condTypeId)) {//0 代表填报，1代表审核
							if("0".equals(checkFlag)||"1".equals(checkFlag)){//0已报送，3代表未填报，4代表已填报
								continue label1;
							}else{
								countFill++;
							}
						
					}else if("check".equals(condTypeId)){
						if("1".equals(checkFlag)){//1代表已审核
							continue label1;
						}else{
							countCheck++;
						}
					}
				}
				if(countFill==afReportList.size()){
					return msg+=vo.getTaskName() + "至少有一张报表为已报送！";
				}
				if(countCheck==afReportList.size()){
					return msg+=vo.getTaskName() + "至少有一张报表要审核通过！";
				}
	
			}else  if("yjtx".equals(vo.getBusiLine())){
				//查询强制校验
				int countFill=0;//用来计算填报个数
				int countCheck=0;//用来计算审核个数
				String reportInSql="select a.check_flag,a.rep_name from report_in a,m_actu_rep b where a.times=1   and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+"  and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
				List reportInList=this.findListBySql(reportInSql, null);
				if(reportInList==null||reportInList.size()==0){
					return msg+="未处理【"+vo.getTaskName()+"】任务下的【"+vo.getTaskTerm()+"】期的报表";
				}
				for (int j = 0; reportInList!=null&&j < reportInList.size(); j++) {
					Object[] obj=(Object[])reportInList.get(j);
					String checkFlag=((BigDecimal)obj[0]).toString();
					String repName=(String)obj[1];
					if ("fill".equals(condTypeId)) {//0 代表填报，1代表审核
						if("0".equals(checkFlag)||"1".equals(checkFlag)){//0代表已填报
							continue label1;
						}else{
							countFill++;
						}
					}else if("check".equals(condTypeId)){
						if("1".equals(checkFlag)){//1代表已审核
							continue label1;
						}else{
							countCheck++;
						}
					}
				}
				if(countFill==reportInList.size()){
					return msg+= vo.getTaskName() + "至少有一张报表为已报送！";
				}
				if(countCheck==reportInList.size()){
					return msg+= vo.getTaskName() + "至少有一张报表要审核通过！";
				}
		}
			
		}
		}
		return null;
	}
	
	/**
	 * 退回校验检查
	 */
	@Override
	public String validateReturn(List<WorkTaskPendingTaskVo> pvos)
			throws Exception {
		// TODO Auto-generated method stub
		String msg="";
	
    	for (int i = 0; i < pvos.size(); i++) {
			WorkTaskPendingTaskVo vo=pvos.get(i);
			String tempIds="";
			String[] tempIdArray=vo.getTemplateIds().split(",");
			for (int n = 0; n < tempIdArray.length; n++) {
				if (!"".equals(tempIds)) {
					tempIds+=",";
				}
				tempIds+="'"+tempIdArray[n]+"'";
			}
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String taskTerm=format.format(vo.getTaskTerm());
			String nodeInfoHql="from WorkTaskNodeInfo ni where ni.nodeId="+vo.getNodeId();//查找节点信息表，
			String taskInfoSql="select i.taskName,i.freqId from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			for (int j = 0; j < taskInfoList.size(); j++) {
				Object[] obj=(Object[])taskInfoList.get(j);
				vo.setTaskName((String)obj[0]);
				vo.setFreqIds(StringUtil.freqConvert((String)obj[1]));
			}
			WorkTaskNodeInfo nodeInfo=(WorkTaskNodeInfo)this.findObject(nodeInfoHql);
			String condTypeId=nodeInfo.getCondTypeId();
			if ("rhtx".equals(vo.getBusiLine())||WorkTaskConfig.BUSI_LINE_QTTX.equals(vo.getBusiLine())) {
				if(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC.equals(condTypeId)){
					String afReportSql="select a.check_flag,a.rep_name,b.org_name  from af_report a,af_org b where (a.check_flag='0' or a.check_flag='1' ) and rep_freq_id="+vo.getFreqIds()+"  and a.org_id=b.org_id and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
					List afReportList=this.findListBySql(afReportSql, null);
					if (afReportList!=null && afReportList.size()==tempIdArray.length) {
						//return msg+="未对【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表做过任何处理，请处理相应报表";
						  return msg+="至少有一条审核不通过";
					}
					else{
						afReportSql="select a.check_flag,a.rep_name,b.org_name  from af_report a,af_org b where a.check_flag='1' and a.org_id=b.org_id and rep_freq_id="+vo.getFreqIds()+"  and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
						afReportList=this.findListBySql(afReportSql, null);
						if(afReportList!=null&&afReportList.size()==tempIdArray.length){
							return msg+="当该任务下的状态全部为审核通过时，不能退回该任务";
						}
					}
				}else if(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL.equals(condTypeId)){
					String afReportSql="select a.check_flag,a.rep_name,b.org_name  from af_report a,af_org b where a.check_flag='3' and a.org_id=b.org_id  and rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
					List afReportList=this.findListBySql(afReportSql, null);
					if (afReportList!=null && afReportList.size()==tempIdArray.length) {
						return msg+="未对【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表做过任何处理，请处理相应报表";
					}
					else{
						afReportSql="select a.check_flag,a.rep_name,b.org_name  from af_report a,af_org b where a.check_flag='0' and a.org_id=b.org_id  and rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
						afReportList=this.findListBySql(afReportSql, null);
						if(afReportList!=null&&afReportList.size()==tempIdArray.length){
							return msg+="当该任务下的报表状态全部为已报送时，不能退回该任务";
						}
					}
					afReportSql="select a.check_flag,a.rep_name,b.org_name  from af_report a,af_org b where a.check_flag!=0 and a.check_flag!=3 and a.org_id=b.org_id and rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
					afReportList=this.findListBySql(afReportSql, null);
					if(afReportList==null || afReportList.size()==0){
						return msg+="当前任务下没有需要退回的报表";
					}
				}
			}else  if("yjtx".equals(vo.getBusiLine())){
				if(WorkTaskConfig.WORK_TASK_COND_TYPE_CHEC.equals(condTypeId)){
					String reportInSql="select a.check_flag,a.rep_name from report_in a,m_actu_rep b  where (a.check_flag='0' or a.check_flag='1') and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id  and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
					List reportInList=this.findListBySql(reportInSql, null);
					if(reportInList!=null && reportInList.size()==tempIdArray.length){
						//return msg+="未对【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表做过处理，请处理该任务的报表";
						return msg+="至少有一条审核不通过";
					}
					else{
						reportInSql="select a.check_flag,a.rep_name from report_in a,m_actu_rep b where a.check_flag='1'  and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
						reportInList=this.findListBySql(reportInSql, null);
						if(reportInList!=null&&reportInList.size()==tempIdArray.length){
							return msg+="当该任务下的状态全部为审核通过时，不能退回该任务";
						}
					}
				}else if(WorkTaskConfig.WORK_TASK_COND_TYPE_FILL.equals(condTypeId)){
					String reportInSql="select a.check_flag,a.rep_name from report_in a,m_actu_rep b where a.check_flag='3' and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
					List reportInList=this.findListBySql(reportInSql, null);
					if(reportInList!=null && reportInList.size()==tempIdArray.length){
						return msg+="未对【"+vo.getTaskName()+"】任务下的【"+taskTerm+"】期的报表做过处理，请处理该任务的报表";
					}
					else{
						reportInSql="select a.check_flag,a.rep_name from report_in a,m_actu_rep b where a.check_flag='0' and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
						reportInList=this.findListBySql(reportInSql, null);
						if(reportInList!=null&&reportInList.size()==tempIdArray.length){
							return msg+="当该任务下的报表状态全部为已报送时，不能退回该任务";
						}
					}
					reportInSql="select a.check_flag,a.rep_name from report_in a,m_actu_rep b where a.check_flag!=0 and a.check_flag!=3  and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
					reportInList=this.findListBySql(reportInSql, null);
					if(reportInList==null || reportInList.size()==0){
						return msg+="当前任务下没有需要退回的报表";
					}
				}
		}
			
		}
		return null;
	}

	@Override
	public List<String> getNoPassTemplateIds(WorkTaskPendingTaskVo pvos,String operatorType)
			throws Exception {
		// TODO Auto-generated method stub
		//返回当前执行任务下的报表，除了已报送，审核通过的报表,其他的报表需要做任务拆分
		List<String> templateList=new ArrayList<String>();

			WorkTaskPendingTaskVo vo=pvos;
			String[] tempIdArray=vo.getTemplateIds().split(",");
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String taskTerm=format.format(vo.getTaskTerm());
			String nodeInfoHql="from WorkTaskNodeInfo ni where ni.nodeId="+vo.getNodeId();//查找节点信息表，
			String taskInfoSql="select i.taskName ,i.taskTypeId ,i.freqId  from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			for (int j = 0; j < taskInfoList.size(); j++) {
				Object[] obj=(Object[])taskInfoList.get(j);
				vo.setTaskName((String)obj[0]);
				vo.setWorkTypeId((String)obj[1]);
				vo.setFreqIds(StringUtil.freqConvert((String)obj[2]));
				
			}
			WorkTaskNodeInfo nodeInfo=(WorkTaskNodeInfo)this.findObject(nodeInfoHql);
			String condTypeId=nodeInfo.getCondTypeId();
			String checkFlag = null;
			String otherFlag = null;
			//根据节点操作类型是(填报，审核)判断checkflag(0-未审核  1审核通过)
			if("commit".equals(operatorType)){
				if ("fill".equals(condTypeId)) {//0 代表填报，1代表审核
					checkFlag = "0";
				}else if("check".equals(condTypeId)){
					checkFlag = "1";
				}
			}else if("goback".equals(operatorType)){
				if ("fill".equals(condTypeId)) {//0 代表填报，1代表审核
					//checkFlag = "3";
					otherFlag = "3";
				}else if("check".equals(condTypeId)){
					checkFlag = "-1";
				}
			}
			//查找当前执行任务下的所有报表编号循环
			if(tempIdArray!=null && tempIdArray.length>0){
				for (int i = 0; i < tempIdArray.length; i++) {
					String sql= "";
					if (WorkTaskConfig.BUSI_LINE_RHTX.equals(vo.getBusiLine())||WorkTaskConfig.BUSI_LINE_QTTX.equals(vo.getBusiLine())) {
						sql="select a.template_id,a.check_flag from af_report a,af_org b  where a.times=1 and a.org_id=b.org_id and rep_freq_id="+vo.getFreqIds()+" and a.org_id=b.org_id and a.org_id='"+vo.getOrgId()+"'  and a.template_id in('"+tempIdArray[i]+"') and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
					}else  if("yjtx".equals(vo.getBusiLine())){
						sql="select a.child_rep_id,a.check_flag from report_in a,m_actu_rep b where a.times=1  and a.child_rep_id=b.child_rep_id and a.version_id=b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ('"+tempIdArray[i]+"') and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
					}
					if(!"".equals(sql)){
						List afReportList=this.findListBySql(sql, null);
						//将当前执行任务的下的报表不存在与report_in||af_report表中也添加到集合，
						if(afReportList==null || afReportList.isEmpty()){
							templateList.add(tempIdArray[i]);
						}else{
							Object[] rts = (Object[])afReportList.get(0);
							if(rts!=null && rts.length==2){
								//将报表状态不是已报送，审核通过，则添加到集合
								if(checkFlag!=null){
									if("commit".equals(operatorType)&&"fill".equals(condTypeId)){//如果是填报提交，则把不是已报送和合格的留下
										
										if(!checkFlag.equals(rts[1].toString())&&!"1".equals(rts[1].toString())){//mod by wmm 
											templateList.add(tempIdArray[i]);
										}
									}
									if("commit".equals(operatorType)&&"check".equals(condTypeId)){//如果是审核提交，则把不是审核通过的留下
										if(!checkFlag.equals(rts[1].toString())){//mod by wmm 已报送和审核通过的都不放到集合中
											templateList.add(tempIdArray[i]);
										}
									}if("goback".equals(operatorType)&&"check".equals(condTypeId)){//如果是审核退回，则把未审核和审核通过的留下
										if(!checkFlag.equals(rts[1].toString())){//mod by wmm 如果是审核退回，则将审核通过和未审核的留下
											templateList.add(tempIdArray[i]);
										}
									}
								}else{//如果是填报退回，则把未填报和已报送的留下
									if(rts[1].toString().equals(otherFlag)||"0".equals(rts[1].toString())){//mod by wmm 如果是填报退回，将3未填报和0已报送的留下
										templateList.add(tempIdArray[i]);
									}
								}
							}
						}
					}
				}
			}
		return templateList;

	}
}
