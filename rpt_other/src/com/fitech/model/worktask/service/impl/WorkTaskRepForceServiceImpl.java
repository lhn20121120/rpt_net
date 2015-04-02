package com.fitech.model.worktask.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.AfReport;
import com.fitech.model.worktask.model.pojo.ReportIn;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoniId;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForce;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForceId;
import com.fitech.model.worktask.service.IWorkTaskRepForceService;
import com.fitech.model.worktask.service.WorkTaskNodeMoniService;

public class WorkTaskRepForceServiceImpl extends
	DefaultBaseService<WorkTaskRepForce, String> implements IWorkTaskRepForceService {

	private WorkTaskNodeMoniService workTaskNodeMoniService;
	
	public WorkTaskNodeMoniService getWorkTaskNodeMoniService() {
		return workTaskNodeMoniService;
	}

	public void setWorkTaskNodeMoniService(
			WorkTaskNodeMoniService workTaskNodeMoniService) {
		this.workTaskNodeMoniService = workTaskNodeMoniService;
	}

	@Override
	public Integer findMaxReReportNumber(WorkTaskRepForce workTaskRepForce)
			throws Exception {
		// TODO Auto-generated method stub
		if(workTaskRepForce==null || workTaskRepForce.getId()==null)
			return null;
		String hql = "select max(f.id.rereportNumber) from WorkTaskRepForce f where f.id.templateId='"+workTaskRepForce.getId().getTemplateId()+"'"
				+" and f.id.taskMoniId="+workTaskRepForce.getId().getTaskMoniId()+" and f.id.nodeId="+workTaskRepForce.getId().getNodeId()
				+" and f.id.orgId='"+workTaskRepForce.getId().getOrgId()+"'";
		return (Integer)this.objectDao.findObject(hql);
	}

	@Override
	public AfReport findOneAfReport(Integer repId) throws Exception {
		// TODO Auto-generated method stub
		if(repId!=null){
			String hql = "from AfReport ar where ar.repId="+new BigDecimal(repId);
			return (AfReport)this.objectDao.findObject(hql);
		}
		return null;
	}

	@Override
	public ReportIn findOneReportIn(Long repInId) throws Exception {
		// TODO Auto-generated method stub
		if(repInId!=null){
			String hql = "from ReportIn ar where ar.repInId="+repInId;
			return (ReportIn)this.objectDao.findObject(hql);
		}
		return null;
	}

	@Override
	public void saveWorkTaskRepForce(String orgId, Integer taskMoniId,
			Integer nodeId, String templateId,Long repId) throws Exception {
		// TODO Auto-generated method stub
		
		WorkTaskNodeMoni nodeMoni = new WorkTaskNodeMoni();
		WorkTaskNodeMoniId nodeMoniId = new WorkTaskNodeMoniId();
		nodeMoniId.setNodeId(nodeId);
		nodeMoniId.setOrgId(orgId);
		nodeMoniId.setTaskMoniId(Long.valueOf(taskMoniId));
		nodeMoni.setId(nodeMoniId);
//		WorkTaskNodeMoni oneNodeMoni = workTaskNodeMoniService.findOneWorkTaskNodeMoni(nodeMoni);
		
		WorkTaskRepForce workTaskRepForce = new WorkTaskRepForce();
		WorkTaskRepForceId workTaskRepForceId = new WorkTaskRepForceId();
		workTaskRepForce.setRepInId(Long.valueOf(repId));
		workTaskRepForceId.setNodeId(nodeId);
		workTaskRepForceId.setOrgId(orgId);
		workTaskRepForceId.setTaskMoniId(taskMoniId);
		workTaskRepForceId.setTemplateId(templateId);
		workTaskRepForce.setId(workTaskRepForceId);
		
		Integer rereportNumber = findMaxReReportNumber(workTaskRepForce);
		if(null==rereportNumber)
			rereportNumber = 1;
		else
			rereportNumber++;
		workTaskRepForceId.setRereportNumber(rereportNumber);
		this.objectDao.save(workTaskRepForce);
	}

	@Override
	public List<WorkTaskRepForce> findListWorkTaskRepForce(WorkTaskRepForce wrf)
			throws Exception {
		// TODO Auto-generated method stub
		if(wrf!=null){
//			Integer rereportNumber = this.findMaxReReportNumber(wrf);
//			String hsql ="select max(f.id.rereportNumber) from WorkTaskRepForce f where "
//					+" f.id.taskMoniId="+wrf.getId().getTaskMoniId()+" and f.id.nodeId="+wrf.getId().getNodeId()
//					+" and f.id.orgId='"+wrf.getId().getOrgId()+"'";
//			Integer rereportNumber = (Integer)this.objectDao.findObject(hsql);
//			if(rereportNumber==null)
//				rereportNumber = 1;
			
			String hql = "from WorkTaskRepForce f where "
					+"  f.id.taskMoniId="+wrf.getId().getTaskMoniId()+" and f.id.nodeId="+wrf.getId().getNodeId()
					+" and f.id.orgId='"+wrf.getId().getOrgId()+"'"+" and f.id.templateId='"+wrf.getId().getTemplateId()+"'" ;
			
			return this.objectDao.findListByHsql(hql, null);
		}
		return null;
	}

	@Override
	public void updateWorkTaskRepForce(WorkTaskRepForce wrf) throws Exception {
		// TODO Auto-generated method stub
		if(wrf!=null){
			String hql = "delete from WorkTaskRepForce w where w.id.taskMoniId="+wrf.getId().getTaskMoniId()+" and " +
					" w.id.nodeId="+wrf.getId().getNodeId()+" and w.id.orgId='"+wrf.getId().getOrgId()+"'";
			
			this.objectDao.deleteObjects(hql);
		}
	}

}
