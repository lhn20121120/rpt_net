package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.AfReport;
import com.fitech.model.worktask.model.pojo.ReportIn;
import com.fitech.model.worktask.model.pojo.WorkTaskRepForce;

public interface IWorkTaskRepForceService extends IBaseService<WorkTaskRepForce, String> {
	public Integer findMaxReReportNumber(WorkTaskRepForce workTaskRepForce)throws Exception;
	
	public AfReport findOneAfReport(Integer repId)throws Exception;
	
	public void saveWorkTaskRepForce(String orgId,Integer taskMoniId,Integer nodeId,
			String templateId,Long repId)throws Exception;
	
	public ReportIn findOneReportIn(Long repInId)throws Exception;
	
	public List<WorkTaskRepForce> findListWorkTaskRepForce(WorkTaskRepForce wrf)throws Exception;
	
	public void updateWorkTaskRepForce(WorkTaskRepForce wrf)throws Exception;
}
