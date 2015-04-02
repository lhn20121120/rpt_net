package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.ViewWorktaskTemplate;

public interface IWorkTaskTemplateService extends IBaseService<ViewWorktaskTemplate, String>{

	public List findAllBusiLine();

	public List getAllGroup(String busiLineId);

	public List getTemplateByGroupId(String templateGroupId ,String userId ,String busiLineId,String freq) ;

	public List findAllId();
	
	public String getBusiLineById(String templateId) throws Exception;

}
