package com.fitech.model.worktask.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObject;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectService;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectServiceFlow;

public class WorkTaskNodeObjectServiceImpl extends DefaultBaseService<WorkTaskNodeObject,String> implements IWorkTaskNodeObjectService,IWorkTaskNodeObjectServiceFlow {

	@Override
	public List findCheckOrgListByTaskId(String taskId) throws Exception {
		List <String >orgIdList = new ArrayList<String > ();
		String hsql = "select distinct(t.id.orgId) from WorkTaskNodeObject t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		orgIdList = this.findListByHsql(hsql, null);
		if (orgIdList.size() == 0)
			orgIdList = null;
		return orgIdList;
	}

	@Override
	public List findCheckTemplateListByTaskId(String taskId) throws Exception {
		List <String >templateIdList = new ArrayList<String > ();
		String hsql = "select distinct(t.id.templateId) from WorkTaskNodeObject t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		templateIdList = this.findListByHsql(hsql, null);
		if (templateIdList.size() == 0)
			templateIdList = null;
		return templateIdList;
	}
	public List findCheckOrgListByTaskIdFlow(String taskId,String nodeId) throws Exception {
		List <String >orgIdList = new ArrayList<String > ();
		String hsql = "select distinct(t.id.orgId) from WorkTaskNodeObject t where t.id.nodeId="+nodeId+"  and t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		orgIdList = this.findListByHsql(hsql, null);
		if (orgIdList.size() == 0)
			orgIdList = null;
		return orgIdList;
	}
	
	@Override
	public List findCheckTemplateListByTaskIdFlow(String taskId,String nodeId) throws Exception {
		List <String >templateIdList = new ArrayList<String > ();
		String hsql = "select distinct(t.id.templateId) from WorkTaskNodeObject t where t.id.nodeId="+nodeId+" and t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		templateIdList = this.findListByHsql(hsql, null);
		if (templateIdList.size() == 0)
			templateIdList = null;
		return templateIdList;
	}

	@Override
	public void deleteByTaskId(Integer taskId) throws Exception {
		String hsql = "delete from WorkTaskNodeObject t where t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		this.objectDao.deleteObjects(hsql);
		
	}
	
	public Map<String,List> findNodetIsNotNormalEdi(String taskId, String nodeId)
			throws Exception {
		// TODO Auto-generated method stub
		List <String >orgIdList = new ArrayList<String > ();
		String hsql = "select distinct(t.id.orgId) from WorkTaskNodeObject t where t.id.nodeId="+nodeId+"  and t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
		orgIdList = this.findListByHsql(hsql, null);
		Map<String,List> org_count = new HashMap<String, List>(); 
		for(String orgId : orgIdList){
			List <String > templateList = new ArrayList<String > ();
			hsql = "select distinct(t.id.templateId) from WorkTaskNodeObject t where t.id.orgId='"+orgId+"' and  t.id.nodeId="+nodeId+"  and t.id.nodeId in (select i.nodeId from WorkTaskNodeInfo i where i.taskId = "+taskId+" )";
			templateList = this.findListByHsql(hsql, null);
			org_count.put(orgId, templateList);
		}
		int count =0;
		int tempNum =0;
		if(!org_count.isEmpty()){
			for(String orgId : org_count.keySet()){
				if(count>0){
					if(tempNum==org_count.get(orgId).size())
						count++;
				}else{
					tempNum = org_count.get(orgId).size();
					count++;
				}
			}
			if(count==org_count.size()){
				boolean res = contentIs(org_count);
				if(!res)
					return org_count;
			}else
				return org_count;
		}
		return null;
	}
	
	public boolean contentIs(Map<String,List> org_count){
		int firstSize =0;
		int count=0;
		Map map = new HashMap();   //G01,G02     G02,G03
		for(String orgId : org_count.keySet()){
			if(count==0){
				firstSize = org_count.get(orgId).size();
			}
			for (int i = 0; i < org_count.get(orgId).size(); i++) {
				map.put(org_count.get(orgId).get(i), org_count.get(orgId).get(i));
			}
			count++;
		}
		if(firstSize==map.size())
			return true;
		return false;
	}
}
