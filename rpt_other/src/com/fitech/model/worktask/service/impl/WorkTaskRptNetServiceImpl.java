package com.fitech.model.worktask.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.model.worktask.model.pojo.AfReport;
import com.fitech.model.worktask.model.pojo.AfReportAgain;
import com.fitech.model.worktask.model.pojo.ReportAgainSet;
import com.fitech.model.worktask.model.pojo.ReportIn;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForce;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForceId;
import com.fitech.model.worktask.service.IWorkTaskRepForceService;
import com.fitech.model.worktask.service.WorkTaskRptNetService;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;
/**
 * 
 * @author wmm 20130712
 *
 */
public class WorkTaskRptNetServiceImpl extends DefaultBaseService<WorkTaskNodeMoni, String> implements WorkTaskRptNetService {


	private IWorkTaskRepForceService workTaskRepForceService;
	
	public IWorkTaskRepForceService getWorkTaskRepForceService() {
		return workTaskRepForceService;
	}

	public void setWorkTaskRepForceService(
			IWorkTaskRepForceService workTaskRepForceService) {
		this.workTaskRepForceService = workTaskRepForceService;
	}
	
	@Override
	
	public String updateReport(List<WorkTaskPendingTaskVo> pvos)
			throws Exception {
		// TODO Auto-generated method stub
		String msg="";
		for (int i = 0; i < pvos.size(); i++) {
			WorkTaskPendingTaskVo vo=pvos.get(i);
			
			WorkTaskRepForce workTaskRepForce = new WorkTaskRepForce();
			WorkTaskRepForceId workTaskRepForceId = new WorkTaskRepForceId();
			workTaskRepForceId.setNodeId(vo.getNodeId());
			workTaskRepForceId.setOrgId(vo.getOrgId());
			workTaskRepForceId.setTaskMoniId(vo.getTaskMoniId().intValue());
			workTaskRepForce.setId(workTaskRepForceId);
			workTaskRepForceService.updateWorkTaskRepForce(workTaskRepForce);
			
			String tempIds="";
			String[] tempIdArray=vo.getTemplateIds().split(",");
			String taskInfoSql="select i.taskName ,i.taskTypeId ,i.freqId  from WorkTaskMoni m,WorkTaskInfo i where m.taskId=i.taskId and m.taskMoniId="+vo.getTaskMoniId();
			List taskInfoList=this.findListByHsql(taskInfoSql, null);
			if(taskInfoList!=null&&taskInfoList.size()>0){
				Object[] obj=(Object[])taskInfoList.get(0);
				vo.setFreqIds(StringUtil.freqConvert((String)obj[2]));
			}
			for (int n = 0; n < tempIdArray.length; n++) {
				if (!"".equals(tempIds)) {
					tempIds+=",";
				}
				tempIds+="'"+tempIdArray[n]+"'";
			}
			if ("yjtx".equals(vo.getBusiLine())) {

				String reportInSql="select a.rep_in_id from report_in a,m_actu_rep b  where a.times=1 and  a.child_rep_id=b.child_rep_id and a.version_id =b.version_id and b.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
				List reportInList=this.findListBySql(reportInSql, null);
//				String maxIdSql="select max(ras_id) from  report_again_set";
	//			int maxId=0;
				try{
					for (int j = 0; reportInList!=null&&j < reportInList.size(); j++) {
						int repInId=((BigDecimal)reportInList.get(j)).intValue();
						SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String setDate=format.format(new Date());
					/*	List maxList=this.findListBySql(maxIdSql, null);
						for (int k = 0;maxList!=null&& k < maxList.size(); k++) {
							maxId=((BigDecimal)maxList.get(k)).intValue();
						}*/
//						String insertSql="insert into report_again_set(ras_id,cause,set_date,rep_in_id) values("+(maxId+1)+",'任务重报',to_date('"+setDate+"','yyyy-mm-dd hh:mi:ss'),"+repInId+")";
//						this.updateBysql(insertSql);
						ReportAgainSet ra=new ReportAgainSet();
						ra.setCause("任务重报");
						ra.setRepInId(new Long(repInId));
						ra.setSetDate(new java.sql.Date(new Date().getTime()));
						this.objectDao.save(ra);
						
						ReportIn reportIn = workTaskRepForceService.findOneReportIn(Long.valueOf(repInId));
						workTaskRepForceService.saveWorkTaskRepForce(vo.getOrgId(), 
								vo.getTaskMoniId().intValue(),
								vo.getNodeId(), reportIn.getChildRepId(), Long.valueOf(repInId));
						
					}
					
					String updateSql="update report_in a set a.check_flag=-1 where a.times=1 and a.data_range_id in( select b.data_range_id from m_actu_rep b where b.child_rep_id=a.child_rep_id and a.version_id =b.version_id and b.rep_freq_id="+vo.getFreqIds()+") and a.org_id='"+vo.getOrgId()+"'   and a.child_rep_id in ("+tempIds+") and to_date(a.year||'-'||a.term,'yyyy-mm')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"','yyyy-mm')";
					this.updateBysql(updateSql);
				}catch(Exception e ){
					e.printStackTrace();
					msg+="更新异常";
//					e.printStackTrace();
				}finally{
					if(!"".equals(msg)){
						return msg;
					}
				}
			}else {
				String afReportSql="select a.rep_id  from af_report a where a.times=1  and a.rep_freq_id="+vo.getFreqIds()+" and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
				List afReportList=this.findListBySql(afReportSql, null);
//				String maxIdSql="select max(ras_id) from  af_report_again";
//				int maxId=0;
				try{
					for (int j = 0;afReportList!=null&& j < afReportList.size(); j++) {
							int repId=((BigDecimal)afReportList.get(j)).intValue();
							SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String setDate=format.format(new Date());
//							List maxList=this.findListBySql(maxIdSql, null);
//							for (int k = 0;maxList!=null&& k < maxList.size(); k++) {
//								maxId=((BigDecimal)maxList.get(k)).intValue();
	//						}
//							String insertSql="insert into af_report_again(ras_id,cause,set_date,template_type,rep_id) values("+(maxId+1)+",'任务重报',to_date('"+setDate+"','yyyy-mm-dd hh:mi:ss'),2,"+repId+")";
							AfReportAgain af=new AfReportAgain();
							af.setCause("任务重报");
							af.setRepId(repId);
							af.setSetDate(new java.sql.Date(new Date().getTime()));
							if("yjtx".equals(vo.getBusiLine()))
								af.setTemplateType(2);
							else{
								af.setTemplateType(3);
							}
							this.objectDao.save(af);
							
							AfReport afReport = workTaskRepForceService.findOneAfReport(repId);
							workTaskRepForceService.saveWorkTaskRepForce(vo.getOrgId(), 
									vo.getTaskMoniId().intValue(),
									vo.getNodeId(), afReport.getTemplateId(), Long.valueOf(repId));
						
					}
					String updateSql="update  af_report a set a.check_flag=-1 where  a.times=1 and a.rep_freq_id="+vo.getFreqIds()+"  and a.org_id='"+vo.getOrgId()+"'  and a.template_id in("+tempIds+") and to_date(year||'-'||term||'-'||day,'yyyy-mm-dd')=to_date('"+vo.getYear()+"-"+vo.getTerm()+"-"+vo.getDay()+"','yyyy-mm-dd')";
					this.updateBysql(updateSql);
				}catch(Exception e){
					msg+="更新异常";
				e.printStackTrace();
				}finally{
					if(!"".equals(msg)){
						return msg;
					}
				}
		}
			
		}
		return null;
	}

	
}
