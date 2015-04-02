package com.fitech.model.worktask.service;

import java.util.List;
import java.util.Map;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskBusiLine;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskTrriger;
import com.fitech.model.worktask.model.pojo.WorkTaskType;
import com.fitech.model.worktask.security.Operator;

public interface IWorkTaskInfoService extends IBaseService<WorkTaskInfo, String>{

	public String createAFOrgDataJSON(Operator operator,List<String> list);
	
	public String createAFOrgDataJSONByAsc(Operator operator,List<String> list);
	
//	public String createAFTemplateDataJSON(Operator operator,List <String>list);
	public String createAFTemplateDataJSON(Operator operator,List <String>list,WorkTaskInfo task);
	
	public Map<String ,String> findAllRole()throws Exception;

	public WorkTaskInfo findById(Integer taskId);

	public void updateFlag(WorkTaskInfo task);

	public List<WorkTaskInfo> findListByParam(WorkTaskInfo task);
	
	public Map<String ,String> findAllFreq();
	
	public WorkTaskTrriger finAllTrriger(String trriger) throws Exception;
	
	public List<WorkTaskType> findAllTaskType();//add by wmm
	public List<WorkTaskBusiLine> findAllBusiLine();//add by wmm
	
	public Map<String,String> findAllTempaltesId(String busiLine,String userId);
	
	/**
	 * 生成树的JSON对象文件保存在WEBROOT下的 json目录
	 * @param operator
	 * @param obg_TreeData
	 * @param TreeName
	 * @throws Exception
	 */
	public void createTree(Operator operator ,String obg_TreeData,String TreeName ) throws Exception;
	
	public boolean delTask(WorkTaskInfo task);
	
}
