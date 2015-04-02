package com.fitech.model.worktask.service.impl;

import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.ViewWorktaskTemplate;
import com.fitech.model.worktask.service.IWorkTaskTemplateService;

public class WorkTaskTemplateServiceImpl extends
		DefaultBaseService<ViewWorktaskTemplate, String> implements
		IWorkTaskTemplateService {

	@Override
	public List findAllBusiLine() {
		List list = null;
		String hsql = "select  distinct(vt.id.busiLineId)  from ViewWorktaskTemplate vt ";
		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List getAllGroup(String busiLineId) {
		List list = null;
		String hsql = "select  vtg.id.templateGroupId ,vtg.id.templateGroupName from ViewWorktaskTemplateGroup vtg where vtg.id.busiLineId =  '"+busiLineId+"'";
		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List getTemplateByGroupId(String templateGroupId ,String userId,String busiLineId ,String freq) {
		List list = null;
		String hsql = "";
		String userName = (String)this.objectDao.findObject("select id.userName from ViewWorkTaskOperator where id.userId=" + userId);
        if(freq==null){
        	if(!userName.trim().equals(WorkTaskConfig.ADMIN))		
            	hsql = " select  vt.id.templateId ,vt.id.templateName ,vt.id.versionId from ViewWorktaskTemplate vt where vt.id.templateGroupId ='" +templateGroupId+"' and vt.id.busiLineId ='" +busiLineId+"' and vt.id.usingFlag=1 and vt.id.templateId in (select distinct(t.childRepId) from ViewOrgRep t where t.userId = '"+userId+"' and t.powType=1)";
            else
            	hsql = " select  vt.id.templateId ,vt.id.templateName ,vt.id.versionId from ViewWorktaskTemplate vt where vt.id.templateGroupId ='" +templateGroupId+"' and vt.id.busiLineId ='" +busiLineId+"' and vt.id.usingFlag=1";
        }else{
        	if(!userName.trim().equals(WorkTaskConfig.ADMIN))		
            	hsql = " select  vt.id.templateId ,vt.id.templateName ,vt.id.versionId from ViewWorktaskTemplate vt where vt.id.repFreqId='"+freq+"' and vt.id.templateGroupId ='" +templateGroupId+"' and vt.id.busiLineId ='" +busiLineId+"' and vt.id.usingFlag=1 and vt.id.templateId in (select distinct(t.childRepId) from ViewOrgRep t where t.userId = '"+userId+"' and t.powType=1)";
            else
            	hsql = " select  vt.id.templateId ,vt.id.templateName ,vt.id.versionId from ViewWorktaskTemplate vt where vt.id.repFreqId='"+freq+"' and vt.id.templateGroupId ='" +templateGroupId+"' and vt.id.busiLineId ='" +busiLineId+"' and vt.id.usingFlag=1";
        }
        try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List findAllId() {
		List list = null;
		String hsql = "select  vt.id.templateId  from ViewWorktaskTemplate vt ";
		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String getBusiLineById(String templateId) throws Exception {
		String hsql = "select  vt.id.busiLineId  from ViewWorktaskTemplate vt  where  vt.id.templateId = '"+templateId+"'";
		
		List list = this.findListByHsql(hsql, null);
		String  BusiLineId	=	(String) list.get(0);
		return  BusiLineId;
		
	}

	

}
