package com.fitech.model.worktask.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.AfTemplateCollRep;
import com.fitech.model.worktask.model.pojo.AfTemplateCollRepId;
import com.fitech.model.worktask.model.pojo.AfTemplateOrgRelation;
import com.fitech.model.worktask.model.pojo.AfTemplateOrgRelationId;
import com.fitech.model.worktask.model.pojo.MRepRange;
import com.fitech.model.worktask.model.pojo.MRepRangeId;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObject;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeObjectId;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRepTime;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRepTimeId;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRoleId;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IAfTemplateCollRepService;
import com.fitech.model.worktask.service.IAfTemplateOrgRelationService;
import com.fitech.model.worktask.service.IMRepRangeService;
import com.fitech.model.worktask.service.IWorkTaskInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoService;
import com.fitech.model.worktask.service.IWorkTaskNodeInfoServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectService;
import com.fitech.model.worktask.service.IWorkTaskNodeObjectServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskNodeRepTimeService;
import com.fitech.model.worktask.service.IWorkTaskNodeRoleServiceFlow;
import com.fitech.model.worktask.service.IWorkTaskRoleService;
import com.fitech.model.worktask.service.IWorkTaskTemplateService;
import com.fitech.model.worktask.vo.WorkTaskNodeDefineVo;

/**
 * add by 王明明 工作节点定义
 * 
 * @author Administrator
 * 
 */


public class WorkTaskNodeInfoServiceImpl extends
		DefaultBaseService<WorkTaskNodeInfo, String> implements
		IWorkTaskNodeInfoService,IWorkTaskNodeInfoServiceFlow {
	private IWorkTaskNodeObjectServiceFlow objectService;
	private IWorkTaskNodeRoleServiceFlow nodeRoleService;
	private IWorkTaskNodeInfoService workTaskNodeInfoService;// 节点信息定义
	public IWorkTaskNodeInfoService getWorkTaskNodeInfoService() {
		return workTaskNodeInfoService;
	}

	public void setWorkTaskNodeInfoService(
			IWorkTaskNodeInfoService workTaskNodeInfoService) {
		this.workTaskNodeInfoService = workTaskNodeInfoService;
	}

	private IWorkTaskRoleService workTaskRoleService;// 节点角色处理对象
	private IWorkTaskNodeObjectService workTaskNodeObjectService;//节点处理对象
	private IWorkTaskInfoService workTaskInfoService;//任务信息处理对象
	private IMRepRangeService mRepRangeService;
	private IAfTemplateCollRepService afTemplateCollRepService;
	private IWorkTaskTemplateService workTaskTemplateService;
	private IWorkTaskNodeRepTimeService workTaskNodeRepTimeService;

	

	public IWorkTaskNodeRepTimeService getWorkTaskNodeRepTimeService() {
		return workTaskNodeRepTimeService;
	}

	public void setWorkTaskNodeRepTimeService(
			IWorkTaskNodeRepTimeService workTaskNodeRepTimeService) {
		this.workTaskNodeRepTimeService = workTaskNodeRepTimeService;
	}

	public IWorkTaskTemplateService getWorkTaskTemplateService() {
		return workTaskTemplateService;
	}

	public void setWorkTaskTemplateService(
			IWorkTaskTemplateService workTaskTemplateService) {
		this.workTaskTemplateService = workTaskTemplateService;
	}

	public IAfTemplateCollRepService getAfTemplateCollRepService() {
		return afTemplateCollRepService;
	}

	public void setAfTemplateCollRepService(
			IAfTemplateCollRepService afTemplateCollRepService) {
		this.afTemplateCollRepService = afTemplateCollRepService;
	}

	public IMRepRangeService getmRepRangeService() {
		return mRepRangeService;
	}

	public void setmRepRangeService(IMRepRangeService mRepRangeService) {
		this.mRepRangeService = mRepRangeService;
	}

	private IAfTemplateOrgRelationService afTemplateOrgRelationService;
	
	public IAfTemplateOrgRelationService getAfTemplateOrgRelationService() {
		return afTemplateOrgRelationService;
	}

	public void setAfTemplateOrgRelationService(
			IAfTemplateOrgRelationService afTemplateOrgRelationService) {
		this.afTemplateOrgRelationService = afTemplateOrgRelationService;
	}

	public IWorkTaskInfoService getWorkTaskInfoService() {
		return workTaskInfoService;
	}

	public void setWorkTaskInfoService(IWorkTaskInfoService workTaskInfoService) {
		this.workTaskInfoService = workTaskInfoService;
	}


	public IWorkTaskRoleService getWorkTaskRoleService() {
		return workTaskRoleService;
	}

	public void setWorkTaskRoleService(IWorkTaskRoleService workTaskRoleService) {
		this.workTaskRoleService = workTaskRoleService;
	}


	public IWorkTaskNodeObjectService getWorkTaskNodeObjectService() {
		return workTaskNodeObjectService;
	}

	public void setWorkTaskNodeObjectService(
			IWorkTaskNodeObjectService workTaskNodeObjectService) {
		this.workTaskNodeObjectService = workTaskNodeObjectService;
	}

	@Override
	public void save(WorkTaskInfo task, WorkTaskNodeInfo tbNode,
			WorkTaskNodeInfo fhNode, WorkTaskNodeRole tbRole,
			WorkTaskNodeRole fhRole, WorkTaskNodeObject object, String orgIds,
			String templateIds,String zTime,String fTime,String cTime) throws Exception {
		this.saveTask(task);
		tbNode.setNodeName(task.getTaskName()+"填报");
		tbNode.setRelationTaskId(WorkTaskConfig.PRE_OBJECT_NULL+"");
		int nodeId = saveNodeAndObject(task, tbNode, tbRole, object, orgIds,
				templateIds);
		saveRepTime(nodeId ,zTime,fTime,cTime);
		fhNode.setPreNodeId(nodeId);
		fhNode.setNodeName(task.getTaskName()+"审核");
		fhNode.setRelationTaskId(WorkTaskConfig.PRE_OBJECT_NULL+"");
		nodeId = saveNodeAndObject(task, fhNode, fhRole, object, orgIds, templateIds);
		saveRepTime(nodeId ,zTime,fTime,cTime);
		saveRalation(task, orgIds, templateIds);
	}

	
	@Override
	public void saveRepTime(Integer nodeId , String zTime,
			String fTime, String cTime)throws Exception {
		List<WorkTaskNodeRepTime> repTimeList = new ArrayList<WorkTaskNodeRepTime>();
		
		if(zTime!=null&&!zTime.trim().equals("")){
			WorkTaskNodeRepTime o  = new WorkTaskNodeRepTime(new WorkTaskNodeRepTimeId());
			o.getId().setNodeId(nodeId);
			o.getId().setOrgLevel(WorkTaskConfig.ORG_LEVLE_ZH);
			o.setRepTimeLimit(new Integer(zTime));
			repTimeList.add(o);

		}
		if(fTime!=null&&!fTime.trim().equals("")){
			WorkTaskNodeRepTime o  = new WorkTaskNodeRepTime(new WorkTaskNodeRepTimeId());
			o.getId().setNodeId(nodeId);
			o.getId().setOrgLevel(WorkTaskConfig.ORG_LEVLE_FH);
			o.setRepTimeLimit(new Integer(fTime));
			repTimeList.add(o);

		}
		if(cTime!=null&&!cTime.trim().equals("")){
			WorkTaskNodeRepTime o  = new WorkTaskNodeRepTime(new WorkTaskNodeRepTimeId());
			o.getId().setNodeId(nodeId);
			o.getId().setOrgLevel(WorkTaskConfig.ORG_LEVLE_CH);
			o.setRepTimeLimit(new Integer(cTime));
			repTimeList.add(o);
		}
		if (repTimeList.size() > 0)
//			for (int i = 0; i < repTimeList.size(); i++) {
//				System.out.println(repTimeList.get(i));
//			}
			workTaskNodeRepTimeService.saveOrUpdate(repTimeList);
	}
	
	@Override 
	public void deleteInfo(WorkTaskInfo task)throws Exception {
//		workTaskNodeInfoService.deleteInfo(task);
		
		workTaskNodeInfoService.deleteRalation(task);
		workTaskNodeObjectService.deleteByTaskId(task.getTaskId());
		workTaskRoleService.deleteByTaskId(task.getTaskId());
		workTaskNodeRepTimeService.deleteByTaskId(task.getTaskId());
	}
	@Override 
	public void saveRalation(WorkTaskInfo task, String orgIds,
			String templateIds) throws Exception {
		List<AfTemplateCollRep> atcList = new ArrayList<AfTemplateCollRep>();
		List<AfTemplateOrgRelation> atorList = new ArrayList<AfTemplateOrgRelation>();
		List<MRepRange> mrrList = new ArrayList<MRepRange>();
		MRepRange mrr = null;
		AfTemplateCollRep atc = null;
		AfTemplateOrgRelation ator = null;
		System.out.println(orgIds);
		String[] orgIdss = changeAll(orgIds);
		System.out.println(templateIds);
		List templateIdList = workTaskTemplateService.findAllId();
		
		String[] templateIdss = changeAll(templateIds);
		for (int i = 0; i < templateIdss.length; i++) {
			for (int j = 0; j < orgIdss.length; j++) {
				if (!templateIdList.contains(templateIdss[i].split("_")[0]))
					continue;
				if (workTaskTemplateService.getBusiLineById(templateIdss[i].split("_")[0]).equals(WorkTaskConfig.BUSI_LINE_YJTX)) {//银监
				
					mrr = new MRepRange(new MRepRangeId());
					mrr.getId().setOrgId(orgIdss[j]);
					mrr.getId().setChildRepId(templateIdss[i].split("_")[0]);
					mrr.getId().setVersionId(templateIdss[i].split("_")[1]);
					mrrList.add(mrr);
				}else{// 人行 , 其他
					ator = new AfTemplateOrgRelation(
							new AfTemplateOrgRelationId());
					ator.getId().setOrgId(orgIdss[j]);
					ator.getId().setTemplateId(templateIdss[i].split("_")[0]);
					ator.getId().setVersionId(templateIdss[i].split("_")[1]);
					atorList.add(ator);

					atc = new AfTemplateCollRep(new AfTemplateCollRepId());
					atc.getId().setOrgId(orgIdss[j]);
					atc.getId().setTemplateId(templateIdss[i].split("_")[0]);
					atc.getId().setVersionId(templateIdss[i].split("_")[1]);
					atcList.add(atc);
					
				}
			}
		}
		if (mrrList.size() > 0){
			for (int i = 0; i < mrrList.size(); i++) {
				MRepRange mr = mrrList.get(i);
				this.objectDao.getHibernateTemplate().merge(mr);
			}
		}
		if (atorList.size() > 0){
			for (int i = 0; i < atorList.size(); i++) {
				AfTemplateOrgRelation ar = atorList.get(i);
				this.objectDao.getHibernateTemplate().merge(ar);
			}
		}
		if (atcList.size() > 0){
			for (int i = 0; i < atcList.size(); i++) {
				AfTemplateCollRep ac = atcList.get(i);
				this.objectDao.getHibernateTemplate().merge(ac);
			}
		}
	}

	
	
	public Integer saveNodeAndObject(WorkTaskInfo task, WorkTaskNodeInfo node,
			WorkTaskNodeRole role, WorkTaskNodeObject object, String orgIds,
			String templateIds) throws Exception {

		List<WorkTaskNodeObject> objectList = new ArrayList<WorkTaskNodeObject>();
		node.setTaskId(task.getTaskId());
		//node.setPreNodeId(WorkTaskConfig.PRE_OBJECT_NULL);
		// node.setRelationTaskId(WorkTaskConfig.LAST_OBJECT_NULL);
		save(node);
		role.getId().setNodeId(node.getNodeId());
		System.out.println(role);
		workTaskRoleService.save(role);
		System.out.println(orgIds);
		String[] orgIdss = changeAll(orgIds);
		System.out.println(templateIds);
		String[] templateIdss = changeAll(templateIds);
		System.out.println(object);
		WorkTaskNodeObject o = null;
		List templateIdList = workTaskTemplateService.findAllId();

		for (int i = 0; i < templateIdss.length; i++) {
			for (int j = 0; j < orgIdss.length; j++) {
				if (object == null) {
					o = new WorkTaskNodeObject(new WorkTaskNodeObjectId());
				}
				o.getId().setNodeId(node.getNodeId());
				o.getId().setOrgId(orgIdss[j]);
				String templateId = templateIdss[i].split("_")[0];
				if (!templateIdList.contains(templateId))
					continue;
				o.getId().setTemplateId(templateIdss[i].split("_")[0]);

				o.getId().setNodeIoFlag(1);
				System.out.println(o);
				objectList.add(o);

			}
		}

		if (objectList.size() > 0)
			workTaskNodeObjectService.saveOrUpdate(objectList);
		System.out.println(node.getNodeId());
		return node.getNodeId();
	}

	public void updateObject(int nodeId,
			WorkTaskNodeRole role, WorkTaskNodeObject object, String orgIds,
			String templateIds) throws Exception {

		List<WorkTaskNodeObject> objectList = new ArrayList<WorkTaskNodeObject>();
		
		role.getId().setNodeId(nodeId);
		
		workTaskRoleService.save(role);
	
		String[] orgIdss = changeAll(orgIds);
		
		String[] templateIdss = changeAll(templateIds);
		
		WorkTaskNodeObject o = null;

		for (int i = 0; i < templateIdss.length; i++) {
			for (int j = 0; j < orgIdss.length; j++) {
				if (object == null) {
					o = new WorkTaskNodeObject(new WorkTaskNodeObjectId());
				}
				o.getId().setNodeId(nodeId);
				o.getId().setOrgId(orgIdss[j]);
				String templateId = templateIdss[i].split("_")[0];
				List templateIdList = workTaskTemplateService.findAllId();
				if (!templateIdList.contains(templateId))
					continue;
				o.getId().setTemplateId(templateIdss[i].split("_")[0]);

				o.getId().setNodeIoFlag(1);
				System.out.println(o);
				objectList.add(o);

			}
		}

		if (objectList.size() > 0)
			workTaskNodeObjectService.saveOrUpdate(objectList);
		
	}

	
	/**
	 * 更新节点信息
	 */
	@Override
	public void update(WorkTaskNodeDefineVo vo, String orgIds,
			String templateIds,WorkTaskInfo task,Map<String,List> org_templates) throws BaseServiceException,
			IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		WorkTaskNodeInfo nodeInfo = null;

		if (vo != null) {
			//更新任务
			if(task!=null){
				String taskInfoHql="from WorkTaskInfo ti where ti.taskId="+task.getTaskId();
				WorkTaskInfo taskInfo=(WorkTaskInfo)this.findObject(taskInfoHql);
				taskInfo.setBusiLineId(task.getBusiLineId());
				taskInfo.setEndDate(task.getEndDate());
				taskInfo.setFreqId(task.getFreqId());
				taskInfo.setTaskName(task.getTaskName());
				taskInfo.setPublicFlag(task.getPublicFlag());
				taskInfo.setTaskTime(task.getTaskTime());
				taskInfo.setTaskTypeId(task.getTaskTypeId());
				taskInfo.setTriggerShifting(task.getTriggerShifting());
				taskInfo.setTrrigerId(task.getTrrigerId());
				workTaskInfoService.update(taskInfo);
				
			}
			// 更新节点
			String nodeInfoHql = "from WorkTaskNodeInfo ni where ni.taskId="
					+ vo.getTaskId() + " and ni.nodeId=" + vo.getNodeId();
			try {
				nodeInfo = (WorkTaskNodeInfo) this.findObject(nodeInfoHql);
				nodeInfo.setCondTypeId(vo.getConductType());
				nodeInfo.setNodeName(vo.getNodeName());
				nodeInfo.setNodeTime(vo.getNodeTime());
				nodeInfo.setRelationTaskId(vo.getRelationTaskId());
				this.update(nodeInfo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// 更新节点角色表
			String nodeRoleHql = "from WorkTaskNodeRole nr where nr.id.nodeId="
					+ vo.getNodeId();
			String deleteNodeRoleHql = "delete WorkTaskNodeRole nr where nr.id.nodeId="
					+ vo.getNodeId();
			try {
				this.updateByHsql(deleteNodeRoleHql, null);
				WorkTaskNodeRole nodeRole = new WorkTaskNodeRole();
				WorkTaskNodeRoleId nodeRoleId = new WorkTaskNodeRoleId();
				nodeRoleId.setNodeId(vo.getNodeId());
				nodeRoleId.setRoleId(vo.getRoleId());
				nodeRole.setId(nodeRoleId);
				nodeRoleService.save(nodeRole);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// 更新节点对象信息，首先将节点对应的记录都删除，然后重新插入
			// 输入输出都增加
			String nodeObjectHql = "delete WorkTaskNodeObject no where no.id.nodeId="
					+ vo.getNodeId();
			// String
			// nodeHql0="from WorkTaskNodeObject no where   no.id.nodeId="+vo.getNodeId();
			List<WorkTaskNodeObject> nodeHql0List = null;

			try {
				objectService.updateByHsql(nodeObjectHql, null);
				// nodeHql0List=objectService.findListByHsql(nodeHql0, null);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if(org_templates==null){
						String[] orgIdArr = vo.getOrgIds().split(",");
						String[] templateIdArr = vo.getTemplateIds().split(",");
		
						for (int i = 0; i < orgIdArr.length; i++) {
		
							for (int j = 0; j < templateIdArr.length; j++) {
		
								WorkTaskNodeObjectId objectId = new WorkTaskNodeObjectId();
								WorkTaskNodeObject nodeObject = new WorkTaskNodeObject();
								objectId.setNodeId(vo.getNodeId());
								objectId.setOrgId(orgIdArr[i]);
		
								objectId.setTemplateId(templateIdArr[j].split("_")[0]);
								objectId.setNodeIoFlag(0);// 0代表输入
								nodeObject.setId(objectId);
								objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
								objectId = new WorkTaskNodeObjectId();
								nodeObject = new WorkTaskNodeObject();
								objectId.setNodeId(vo.getNodeId());
								objectId.setOrgId(orgIdArr[i]);
		
								objectId.setTemplateId(templateIdArr[j].split("_")[0]);
								objectId.setNodeIoFlag(1);// 0代表输入
								nodeObject.setId(objectId);
								objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
		
							}
						}
					//更新关系表
						try {
							this.saveRalation(task, vo.getOrgIds(), vo.getTemplateIds());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			}else{
					for(String key : org_templates.keySet()){
							List templates = org_templates.get(key);
							String charStr = getOrgLevelStr(key);
							if(charStr==null){
								for (int i = 0; i < templates.size(); i++) {
									String temlateId= (String)templates.get(i);
									WorkTaskNodeObjectId objectId = new WorkTaskNodeObjectId();
									WorkTaskNodeObject nodeObject = new WorkTaskNodeObject();
									objectId.setNodeId(vo.getNodeId());
									objectId.setOrgId(key);
			
									objectId.setTemplateId(temlateId.split("_")[0]);
									objectId.setNodeIoFlag(0);// 0代表输入
									nodeObject.setId(objectId);
									objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
									objectId = new WorkTaskNodeObjectId();
									nodeObject = new WorkTaskNodeObject();
									objectId.setNodeId(vo.getNodeId());
									objectId.setOrgId(key);
			
									objectId.setTemplateId(temlateId.split("_")[0]);
									objectId.setNodeIoFlag(1);// 0代表输入
									nodeObject.setId(objectId);
									objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
								}
							}else{
								List charOrgs = findAllOrgsByCollect(key);
								List charTemplates = org_templates.get(key);
								for (int i = 0; i < charOrgs.size(); i++) {
									String orgId = (String)charOrgs.get(i);
									for (int k = 0; k < charTemplates.size(); k++) {
										String temlateId= (String)charTemplates.get(k);
										WorkTaskNodeObjectId objectId = new WorkTaskNodeObjectId();
										WorkTaskNodeObject nodeObject = new WorkTaskNodeObject();
										objectId.setNodeId(vo.getNodeId());
										objectId.setOrgId(orgId);
				
										objectId.setTemplateId(temlateId.split("_")[0]);
										objectId.setNodeIoFlag(0);// 0代表输入
										nodeObject.setId(objectId);
										objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
										objectId = new WorkTaskNodeObjectId();
										nodeObject = new WorkTaskNodeObject();
										objectId.setNodeId(vo.getNodeId());
										objectId.setOrgId(orgId);
				
										objectId.setTemplateId(temlateId.split("_")[0]);
										objectId.setNodeIoFlag(1);// 0代表输入
										nodeObject.setId(objectId);
										objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
									}
								}
							}
					}
					//更新关系表
					try {
						for(String key : org_templates.keySet()){
							List templates = org_templates.get(key);
							String charStr = getOrgLevelStr(key);
							if(charStr==null){
								String tds= "";
								for (int i = 0; i < templates.size(); i++) {
									tds+=(String)templates.get(i)+",";
								}
								this.saveRalation(task, key+",", tds);
							}else{
								List charOrgs = findAllOrgsByCollect(key);
								List charTemplates = org_templates.get(key);
								String charOrgStr ="";
								String charTemplateStr = "";
								for (int i = 0; i < charOrgs.size(); i++) {
									charOrgStr+=charOrgs.get(i)+",";
								}
								for (int i = 0; i < charTemplates.size(); i++) {
									charTemplateStr+=charTemplates.get(i)+",";
								}
								this.saveRalation(task, charOrgStr, charTemplateStr);
							}
							
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		} 
	}
	
	
	public List findAllOrgsByCollect(String collectType) {
		// TODO Auto-generated method stub
		int collect = 0;
		try {
			if(collectType.equals("zh")){
				collect = 1;
			}else if(collectType.equals("fh")){
				collect = 2;
			}else if(collectType.equals("zhh")){
				collect = 3;
			}else if(collectType.equals("xn")){
				collect = -99;
			}
			String hql = "select oi.id.orgId,oi.id.orgName from  ViewWorktaskOrg oi where oi.id.orgLevel="+collect;
			 return this.findListByHsql(hql, null);
		} catch (BaseServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getOrgLevelStr(String key){
		Map map = new HashMap();
		map.put("zh","zh");
		map.put("fh", "fh");
		map.put("zhh", "zhh");
		map.put("xn", "xn");
		if(map.containsKey(key))
			return (String)map.get(key);
		return null;
	}
	
	@Override
	public void edit(WorkTaskInfo task, WorkTaskNodeInfo tbNode,
			WorkTaskNodeInfo fhNode, WorkTaskNodeRole tbRole,
			WorkTaskNodeRole fhRole, WorkTaskNodeObject object, String orgIds,
			String templateIds, String zTime, String fTime, String cTime)
			throws Exception {
//		workTaskInfoService.update(task);
//		workTaskNodeObjectService.deleteByTaskId(task.getTaskId());
//		workTaskRoleService.deleteByTaskId(task.getTaskId());
//		workTaskNodeRepTimeService.deleteByTaskId(task.getTaskId());
//		
//		this.deleteInfo(task);
		List <Integer>nodeIds = this.findNodeIdListByTaskId(task.getTaskId());
		saveRepTime(nodeIds.get(0) ,zTime,fTime,cTime);
		updateObject(nodeIds.get(0), tbRole, object, orgIds, templateIds);
		saveRepTime(nodeIds.get(1) ,zTime,fTime,cTime);
		updateObject(nodeIds.get(1), fhRole, object, orgIds, templateIds);
		//deleteRalation(task);
		saveRalation(task, orgIds, templateIds);  //徽商银行临时修改，在修改时不允许设置自动设置报送范围
	}

	@Override
	public void deleteRalation(WorkTaskInfo task) throws Exception {
		List checkOrgList = workTaskNodeObjectService.findCheckOrgListByTaskId(task.getTaskId().toString());
		List checkTemplateList =  workTaskNodeObjectService.findCheckTemplateListByTaskId(task.getTaskId().toString());
		String orgIds = ""  ;
		String templateIds="";
		if(checkOrgList !=null){
			for (int i = 0; i < checkOrgList.size(); i++) {
				orgIds +="'"+ checkOrgList.get(0)+"',";
			}
			orgIds = orgIds.substring(0,orgIds.length()-1);
		}
		if(checkOrgList !=null){
			for (int i = 0; i < checkTemplateList.size(); i++) {
				templateIds +="'"+ checkTemplateList.get(0)+"',";
			}
			templateIds = templateIds.substring(0,templateIds.length()-1);
		}
		String hql = "delete from MRepRange m where m.id.orgId in ("+orgIds+") and m.id.childRepId in ("+templateIds+")";
		this.objectDao.deleteObjects(hql);
		hql = "delete from AfTemplateOrgRelation m where m.id.orgId in ("+orgIds+") and m.id.templateId in ("+templateIds+")";
		this.objectDao.deleteObjects(hql);
		hql = "delete from AfTemplateCollRep m where m.id.orgId  in ("+orgIds+") and m.id.templateId in ("+templateIds+")";
		this.objectDao.deleteObjects(hql);
		
		//		String hql ,hql2 = "";
//		for (int i = 0; i < checkOrgList.size(); i++) {
//			for (int j = 0; j < checkTemplateList.size(); j++) {
//				if (checkTemplateList.get(j).equals(WorkTaskConfig.BUSI_LINE_YJTX)) {//银监
//					hql = "delete from MRepRange m where m.id.orgId='"+checkOrgList.get(i)+"' and m.id.childRepId'"+checkTemplateList.get(j)+"'";
//					this.objectDao.deleteObjects(hql);
//					
//				}else{// 人行 , 其他
//					hql = "delete from AfTemplateOrgRelation m where m.id.orgId='"+checkOrgList.get(i)+"' and m.id.templateId'"+checkTemplateList.get(j)+"'";
//					this.objectDao.deleteObjects(hql);
//					hql2 = "delete from AfTemplateCollRep m where m.id.orgId='"+checkOrgList.get(i)+"' and m.id.templateId'"+checkTemplateList.get(j)+"'";
//					this.objectDao.deleteObjects(hql2);
//				}
//			}
//		}
	}

	@Override
	public List<Integer> findNodeIdListByTaskId(Integer taskId)
			throws Exception {
		
		List result = null;
		String hql = "select t.nodeId from WorkTaskNodeInfo t where t.taskId="
				+ taskId+" order by t.nodeId asc ";
		result = this.findListByHsql(hql, null);
		return result;
	}

	
	/**
	 * @param s
	 * @return
	 * LK
	 */
	private String[] changeAll(String s) {
		if (s.endsWith(",")) {
			s = s.substring(0, s.length() - 1);
		}
		String ss[] = s.split(",");
		
//		System.out.println(ss);
		return ss;
	}

	
	
	
	
	public WorkTaskInfo saveTask(WorkTaskInfo task) throws Exception {
		if (task != null) {
			workTaskInfoService.save(task);
		}
		return task;
	}
	
	/**
	 * 增加节点
	 */
	public void save(WorkTaskNodeDefineVo vo,String orgIds,String templateIds,WorkTaskInfo task) 
	throws BaseServiceException ,IllegalAccessException, InvocationTargetException{
	WorkTaskNodeInfo nodeInfo=null;
	if(vo!=null){//非空判断
		//增加任务
		if(task!=null){
			
			workTaskInfoService.save(task);
			vo.setTaskId(this.getTaskId(task));
		}
		//增加节点
		int nodeId=getNodeId(vo.getTaskId());
		nodeInfo=new WorkTaskNodeInfo();
		nodeInfo.setNodeName(vo.getNodeName());
		nodeInfo.setNodeTime(vo.getNodeTime());
		if (nodeId==0) {
			
			nodeInfo.setPreNodeId(-1);
			
		}else{
			nodeInfo.setPreNodeId(getNodeId(vo.getTaskId()));
		}
		nodeInfo.setCondTypeId(vo.getConductType());
		nodeInfo.setRelationTaskId(vo.getRelationTaskId());
		nodeInfo.setTaskId(vo.getTaskId());
		this.save(nodeInfo);
		
		//增加节点角色表
		WorkTaskNodeRole nodeRole=new WorkTaskNodeRole();
		WorkTaskNodeRoleId nodeRoleId=new WorkTaskNodeRoleId();
		nodeRoleId.setNodeId(this.getNodeId(vo.getTaskId()));
		nodeRoleId.setRoleId(vo.getRoleId());
		nodeRole.setId(nodeRoleId);
		nodeRoleService.save(nodeRole);
		//增加节点对象信息
		//输入输出都增加
		String[] orgIdArr=orgIds.split(",");
		String[] templateIdArr=templateIds.split(",");
		for (int i = 0; i < orgIdArr.length; i++) {
			
			for (int j = 0; j < templateIdArr.length; j++) {
				WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
				WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
				objectId.setNodeId(getNodeId(vo.getTaskId()));
				objectId.setOrgId(orgIdArr[i]);
				
				objectId.setTemplateId(templateIdArr[j]);
				objectId.setNodeIoFlag(0);//0代表输入
				nodeObject.setId(objectId);
				objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
				 objectId=new WorkTaskNodeObjectId();
				 nodeObject=new WorkTaskNodeObject();
				objectId.setNodeId(getNodeId(vo.getTaskId()));
				objectId.setOrgId(orgIdArr[i]);
				
				objectId.setTemplateId(templateIdArr[j]);
				objectId.setNodeIoFlag(1);//0代表输入
				nodeObject.setId(objectId);
				objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
			}
		}
		
		//增加报送范围
		try {
			saveRalation(task, orgIds, templateIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		
	}
	
}
	/**
	 * 取得任务id
	 */
	public int getTaskId(WorkTaskInfo task){
		int taskId=0;
		List<WorkTaskInfo> taskList=null; 
		String hql="from WorkTaskInfo where taskName='"+task.getTaskName()+"'";
		try {
			taskList=workTaskInfoService.findListByHsql(hql, null);
			for (int i = 0; i < taskList.size(); i++) {
				WorkTaskInfo taskInfo=taskList.get(i);
				taskId=taskInfo.getTaskId();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return taskId;
	}
	/**
	 * 更新节点信息
	 */
	@Override
	public void update(WorkTaskNodeDefineVo vo, String orgIds,
			String templateIds,WorkTaskInfo task) throws BaseServiceException,
			IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		WorkTaskNodeInfo nodeInfo = null;

		if (vo != null) {
			//更新任务
			if(task!=null){
				String taskInfoHql="from WorkTaskInfo ti where ti.taskId="+task.getTaskId();
				WorkTaskInfo taskInfo=(WorkTaskInfo)this.findObject(taskInfoHql);
				taskInfo.setBusiLineId(task.getBusiLineId());
				taskInfo.setEndDate(task.getEndDate());
				taskInfo.setFreqId(task.getFreqId());
				taskInfo.setTaskName(task.getTaskName());
				taskInfo.setPublicFlag(task.getPublicFlag());
				taskInfo.setTaskTime(task.getTaskTime());
				taskInfo.setTaskTypeId(task.getTaskTypeId());
				taskInfo.setTriggerShifting(task.getTriggerShifting());
				taskInfo.setTrrigerId(task.getTrrigerId());
				workTaskInfoService.update(taskInfo);
				
			}
			// 更新节点
			String nodeInfoHql = "from WorkTaskNodeInfo ni where ni.taskId="
					+ vo.getTaskId() + " and ni.nodeId=" + vo.getNodeId();
			try {
				nodeInfo = (WorkTaskNodeInfo) this.findObject(nodeInfoHql);
				nodeInfo.setCondTypeId(vo.getConductType());
				nodeInfo.setNodeName(vo.getNodeName());
				nodeInfo.setNodeTime(vo.getNodeTime());
				nodeInfo.setRelationTaskId(vo.getRelationTaskId());
				this.update(nodeInfo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// 更新节点角色表
			String nodeRoleHql = "from WorkTaskNodeRole nr where nr.id.nodeId="
					+ vo.getNodeId();
			String deleteNodeRoleHql = "delete WorkTaskNodeRole nr where nr.id.nodeId="
					+ vo.getNodeId();
			try {
				this.updateByHsql(deleteNodeRoleHql, null);
				WorkTaskNodeRole nodeRole = new WorkTaskNodeRole();
				WorkTaskNodeRoleId nodeRoleId = new WorkTaskNodeRoleId();
				nodeRoleId.setNodeId(vo.getNodeId());
				nodeRoleId.setRoleId(vo.getRoleId());
				nodeRole.setId(nodeRoleId);
				nodeRoleService.save(nodeRole);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// 更新节点对象信息，首先将节点对应的记录都删除，然后重新插入
			// 输入输出都增加
			String nodeObjectHql = "delete WorkTaskNodeObject no where no.id.nodeId="
					+ vo.getNodeId();
			// String
			// nodeHql0="from WorkTaskNodeObject no where   no.id.nodeId="+vo.getNodeId();
			List<WorkTaskNodeObject> nodeHql0List = null;

			try {
				objectService.updateByHsql(nodeObjectHql, null);
				// nodeHql0List=objectService.findListByHsql(nodeHql0, null);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String[] orgIdArr = vo.getOrgIds().split(",");
			String[] templateIdArr = vo.getTemplateIds().split(",");

			for (int i = 0; i < orgIdArr.length; i++) {

				for (int j = 0; j < templateIdArr.length; j++) {

					WorkTaskNodeObjectId objectId = new WorkTaskNodeObjectId();
					WorkTaskNodeObject nodeObject = new WorkTaskNodeObject();
					objectId.setNodeId(vo.getNodeId());
					objectId.setOrgId(orgIdArr[i]);

					objectId.setTemplateId(templateIdArr[j].split("_")[0]);
					objectId.setNodeIoFlag(0);// 0代表输入
					nodeObject.setId(objectId);
					objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
					objectId = new WorkTaskNodeObjectId();
					nodeObject = new WorkTaskNodeObject();
					objectId.setNodeId(vo.getNodeId());
					objectId.setOrgId(orgIdArr[i]);

					objectId.setTemplateId(templateIdArr[j].split("_")[0]);
					objectId.setNodeIoFlag(1);// 0代表输入
					nodeObject.setId(objectId);
					objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表

				}
			}
		//更新关系表
			try {
				this.saveRalation(task, vo.getOrgIds(), vo.getTemplateIds());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				task = new WorkTaskInfo();
				task.setTaskId(vo.getTaskId());
				updateReport(vo,vo.getOrgIds(), vo.getTemplateIds(), task,vo.getNodeTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateReport(WorkTaskNodeDefineVo vo, String orgIds,
			String templateIds, WorkTaskInfo task,Integer limitTime) throws BaseServiceException,
			IllegalAccessException, InvocationTargetException,Exception {
		// TODO Auto-generated method stub
		if(orgIds!=null && templateIds!=null && task!=null
				&& task.getTaskId()!=null){
			String hsql = "from WorkTaskInfo w where w.taskId="+task.getTaskId();
			WorkTaskInfo taskInfo = (WorkTaskInfo)this.objectDao.findObject(hsql);
			if(taskInfo!=null && taskInfo.getTaskTypeId().equals(WorkTaskConfig.TASK_TYPE_DJSH)){
				hsql = "from WorkTaskNodeInfo wn where wn.taskId="+taskInfo.getTaskId();
				List<WorkTaskNodeInfo> nodeInfos = this.objectDao.findListByHsql(hsql, null);
				if(nodeInfos!=null && !nodeInfos.isEmpty()){
					for (int k = 0; k < nodeInfos.size(); k++) {
						WorkTaskNodeInfo nodeInfo = nodeInfos.get(k);
						if(nodeInfo.getNodeId().equals(vo.getNodeId()))
							continue;
						nodeInfo.setNodeTime(limitTime);
						this.objectDao.update(nodeInfo);
						// 更新节点对象信息，首先将节点对应的记录都删除，然后重新插入
						// 输入输出都增加
						String nodeObjectHql = "delete WorkTaskNodeObject no where no.id.nodeId="
								+ nodeInfo.getNodeId();
						// String
						// nodeHql0="from WorkTaskNodeObject no where   no.id.nodeId="+vo.getNodeId();
						List<WorkTaskNodeObject> nodeHql0List = null;

						try {
							this.objectDao.updateByHsql(nodeObjectHql, null);
							// nodeHql0List=objectService.findListByHsql(nodeHql0, null);

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						String[] orgIdArr = vo.getOrgIds().split(",");
						String[] templateIdArr = vo.getTemplateIds().split(",");

						for (int i = 0; i < orgIdArr.length; i++) {

							for (int j = 0; j < templateIdArr.length; j++) {

								WorkTaskNodeObjectId objectId = new WorkTaskNodeObjectId();
								WorkTaskNodeObject nodeObject = new WorkTaskNodeObject();
								objectId.setNodeId(nodeInfo.getNodeId());
								objectId.setOrgId(orgIdArr[i]);

								objectId.setTemplateId(templateIdArr[j].split("_")[0]);
								objectId.setNodeIoFlag(0);// 0代表输入
								nodeObject.setId(objectId);
								objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表
								objectId = new WorkTaskNodeObjectId();
								nodeObject = new WorkTaskNodeObject();
								objectId.setNodeId(nodeInfo.getNodeId());
								objectId.setOrgId(orgIdArr[i]);

								objectId.setTemplateId(templateIdArr[j].split("_")[0]);
								objectId.setNodeIoFlag(1);// 0代表输入
								nodeObject.setId(objectId);
								objectService.save(nodeObject);// 一个节点，一个机构，对应n条报表

							}
						}
					//更新关系表
//						try {
//							this.saveRalation(task, vo.getOrgIds(), vo.getTemplateIds());
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					}
					}
				}
			}
	}

	/**
	 * 向前新增
	 * @return
	 */
	public void insertPreNode(WorkTaskNodeDefineVo vo, String orgIds,
			String templateIds,WorkTaskInfo task) throws BaseServiceException,
			IllegalAccessException, InvocationTargetException{
		WorkTaskNodeInfo nodeInfo = null;

		if (vo != null) {
			//更新任务
			if(task!=null){
				String taskInfoHql="from WorkTaskInfo ti where ti.taskId="+task.getTaskId();
				WorkTaskInfo taskInfo=(WorkTaskInfo)this.findObject(taskInfoHql);
				taskInfo.setBusiLineId(task.getBusiLineId());
				taskInfo.setEndDate(task.getEndDate());
				taskInfo.setFreqId(task.getFreqId());
				taskInfo.setTaskName(task.getTaskName());
				taskInfo.setPublicFlag(task.getPublicFlag());
				taskInfo.setTaskTime(task.getTaskTime());
				taskInfo.setTaskTypeId(task.getTaskTypeId());
				taskInfo.setTriggerShifting(task.getTriggerShifting());
				taskInfo.setTrrigerId(task.getTrrigerId());
				workTaskInfoService.update(taskInfo);
				
			}
			//增加节点
		
			nodeInfo=new WorkTaskNodeInfo();
			nodeInfo.setNodeName(vo.getNodeName());
			nodeInfo.setNodeTime(vo.getNodeTime());									
			nodeInfo.setPreNodeId(vo.getPreNodeId());
			
			nodeInfo.setCondTypeId(vo.getConductType());
			nodeInfo.setRelationTaskId(vo.getRelationTaskId());
			nodeInfo.setTaskId(vo.getTaskId());
			this.save(nodeInfo);
			//更新节点
			String nodeSql="from WorkTaskNodeInfo where nodeId="+vo.getNodeId();
			WorkTaskNodeInfo nodeInfoUpdate=(WorkTaskNodeInfo)this.findObject(nodeSql);
			int nodeId=getNodeId(vo.getTaskId());
			nodeInfoUpdate.setPreNodeId(nodeId);
			this.update(nodeInfoUpdate);
			//增加节点角色表
			WorkTaskNodeRole nodeRole=new WorkTaskNodeRole();
			WorkTaskNodeRoleId nodeRoleId=new WorkTaskNodeRoleId();
			nodeRoleId.setNodeId(this.getNodeId(vo.getTaskId()));
			nodeRoleId.setRoleId(vo.getRoleId());
			nodeRole.setId(nodeRoleId);
			nodeRoleService.save(nodeRole);
			//增加节点对象信息
			//输入输出都增加
			String[] orgIdArr=orgIds.split(",");
			String[] templateIdArr=templateIds.split(",");
			for (int i = 0; i < orgIdArr.length; i++) {
				
				for (int j = 0; j < templateIdArr.length; j++) {
					WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
					WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
					objectId.setNodeId(getNodeId(vo.getTaskId()));
					objectId.setOrgId(orgIdArr[i]);
					
					objectId.setTemplateId(templateIdArr[j].split("_")[0]);
					objectId.setNodeIoFlag(0);//0代表输入
					nodeObject.setId(objectId);
					objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
					 objectId=new WorkTaskNodeObjectId();
					 nodeObject=new WorkTaskNodeObject();
					objectId.setNodeId(getNodeId(vo.getTaskId()));
					objectId.setOrgId(orgIdArr[i]);
					
					objectId.setTemplateId(templateIdArr[j].split("_")[0]);
					objectId.setNodeIoFlag(1);//0代表输入
					nodeObject.setId(objectId);
					objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
				}
			}
			//更新关系表
			try {
				this.saveRalation(task, orgIds, templateIds);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 向后新增
	 * @return
	 */
	public void insertBackNode(WorkTaskNodeDefineVo vo, String orgIds,
			String templateIds,WorkTaskInfo task) throws BaseServiceException,
			IllegalAccessException, InvocationTargetException{
		WorkTaskNodeInfo nodeInfo = null;
		
		if (vo != null) {
			//更新任务
			if(task!=null){
				String taskInfoHql="from WorkTaskInfo ti where ti.taskId="+task.getTaskId();
				WorkTaskInfo taskInfo=(WorkTaskInfo)this.findObject(taskInfoHql);
				taskInfo.setBusiLineId(task.getBusiLineId());
				taskInfo.setEndDate(task.getEndDate());
				taskInfo.setFreqId(task.getFreqId());
				taskInfo.setTaskName(task.getTaskName());
				taskInfo.setPublicFlag(task.getPublicFlag());
				taskInfo.setTaskTime(task.getTaskTime());
				taskInfo.setTaskTypeId(task.getTaskTypeId());
				taskInfo.setTriggerShifting(task.getTriggerShifting());
				taskInfo.setTrrigerId(task.getTrrigerId());
				workTaskInfoService.update(taskInfo);
				
			}
			//增加节点
			
			nodeInfo=new WorkTaskNodeInfo();
			nodeInfo.setNodeName(vo.getNodeName());
			nodeInfo.setNodeTime(vo.getNodeTime());									
//			nodeInfo.setPreNodeId(vo.getNodeId());
			
			nodeInfo.setCondTypeId(vo.getConductType());
			nodeInfo.setRelationTaskId(vo.getRelationTaskId());
			nodeInfo.setTaskId(vo.getTaskId());
			this.save(nodeInfo);
			//更新节点
			String nodeSql="from WorkTaskNodeInfo where preNodeId="+vo.getNodeId();
			WorkTaskNodeInfo nodeInfoUpdate=(WorkTaskNodeInfo)this.findObject(nodeSql);
			if (nodeInfoUpdate!=null) {
				
				int nodeId=getNodeId(vo.getTaskId());
				nodeInfoUpdate.setPreNodeId(nodeId);
				this.update(nodeInfoUpdate);
			}
			//更新新增节点的preNodeId
			String newNodeSql="from WorkTaskNodeInfo where nodeId="+getNodeId(vo.getTaskId());
			WorkTaskNodeInfo newNodeInfoUpdate=(WorkTaskNodeInfo)this.findObject(newNodeSql);
			newNodeInfoUpdate.setPreNodeId(vo.getNodeId());
			this.update(newNodeInfoUpdate);
			//增加节点角色表
			WorkTaskNodeRole nodeRole=new WorkTaskNodeRole();
			WorkTaskNodeRoleId nodeRoleId=new WorkTaskNodeRoleId();
			nodeRoleId.setNodeId(this.getNodeId(vo.getTaskId()));
			nodeRoleId.setRoleId(vo.getRoleId());
			nodeRole.setId(nodeRoleId);
			nodeRoleService.save(nodeRole);
			//增加节点对象信息
			//输入输出都增加
			String[] orgIdArr=orgIds.split(",");
			String[] templateIdArr=templateIds.split(",");
			for (int i = 0; i < orgIdArr.length; i++) {
				
				for (int j = 0; j < templateIdArr.length; j++) {
					WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
					WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
					objectId.setNodeId(getNodeId(vo.getTaskId()));
					objectId.setOrgId(orgIdArr[i]);
					
					objectId.setTemplateId(templateIdArr[j].split("_")[0]);
					objectId.setNodeIoFlag(0);//0代表输入
					nodeObject.setId(objectId);
					objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
					objectId=new WorkTaskNodeObjectId();
					nodeObject=new WorkTaskNodeObject();
					objectId.setNodeId(getNodeId(vo.getTaskId()));
					objectId.setOrgId(orgIdArr[i]);
					
					objectId.setTemplateId(templateIdArr[j].split("_")[0]);
					objectId.setNodeIoFlag(1);//0代表输入
					nodeObject.setId(objectId);
					objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
				}
			}
			//更新关系表
			try {
				this.saveRalation(task, orgIds, templateIds);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public IWorkTaskNodeObjectServiceFlow getObjectService() {
		return objectService;
	}

	public void setObjectService(IWorkTaskNodeObjectServiceFlow objectService) {
		this.objectService = objectService;
	} 
	
	/**
	 * 获取节点id
	 */
	public int getNodeId(int taskId) {
		int nodeId = 0;
		List result = null;
		String hql = "select max(t.nodeId) from WorkTaskNodeInfo t where t.taskId="
				+ taskId;
		try {
			result = this.findListByHsql(hql, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		for (int i = 0; result != null && i < result.size(); i++) {
			Integer nodeStr = (Integer) result.get(i);
			if (nodeStr != null)
				nodeId = nodeStr;
		}
		return nodeId;
	}

	/**
	 * 生成org Json
	 */
	public String createAFOrgDataJSON(Operator operator) {
		List orgList = null;
		if (operator!=null&&operator.isSuperManager())// 超级用户查询所有机构
			orgList = getAllFirstOrg();
		else 
			// 查询用户机构
			orgList = getFirstOrgById(operator.getOrgId());
		StringBuffer treeJSON = new StringBuffer("[");

		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",");
			treeJSON.append("\"text\":" + "\"" + strs[1] + "\"");

			treeJSON.append(iteratorCreate(strs[0]));// 递归加载子机构

			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");
		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}

	/**
	 * 生成org Json
	 */
	public String createEditAFOrgDataJSON(Operator operator, String taskId,
			String nodeId) {
		List orgList = null;
		if (true)// 超级用户查询所有机构
			orgList = getAllFirstOrg();
		else
			// 查询用户机构
			orgList = getFirstOrgById(operator.getOrgId());
		StringBuffer treeJSON = new StringBuffer("[");
		String[] orgIds = ((String) this.getNodeObjectList(taskId, nodeId).get(
				0)).split(",");

		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",");
			treeJSON.append("\"text\":" + "\"" + strs[1] + "\"");
			for (int j = 0; j < orgIds.length; j++) {
				if (strs[0].equals(orgIds[j])) {
					treeJSON.append(",\"checked\":true");
				}
			}
			treeJSON.append(iteratorCreateEdit(strs[0], orgIds));// 递归加载子机构

			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");
		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}

	/**
	 * 生成template Json
	 */
	public String createAFTemplateDataJSON(Operator operator) {
		List templateList = null;
		if (operator!=null&&operator.isSuperManager())// 超级用户查询所有报表
			templateList = getAllTemplate(operator);
		else
			// 查询用户报表
			templateList =getAllTemplate(operator);
		StringBuffer treeJSON = new StringBuffer("[");

		for (int i = 0; i < templateList.size(); i++) {
			String[] strs = (String[]) templateList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",");
			treeJSON.append("\"text\":" + "\"" + strs[1] + "\"");

			treeJSON.append("}");
			if (i != templateList.size() - 1)
				treeJSON.append(",");
		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}

	/**
	 * 生成template Json
	 */
	public String createEditAFTemplateDataJSON(Operator operator,
			String taskId, String nodeId) {
		List templateList = null;
		if (operator!=null&&operator.isSuperManager())// 超级用户查询所有报表
			templateList = getAllTemplate(operator);
		else
			// 查询用户报表
			templateList = getAllTemplate(operator);
		Set<String> set = (Set<String>) this.getNodeObjectList(taskId, nodeId)
				.get(1);
		String[] templateIds = set.toArray(new String[set.size()]);
		StringBuffer treeJSON = new StringBuffer("[");

		for (int i = 0; i < templateList.size(); i++) {
			String[] strs = (String[]) templateList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",");
			treeJSON.append("\"text\":" + "\"" + strs[1] + "\"");
			for (int j = 0; j < templateIds.length; j++) {
				if (strs[0].equals(templateIds[j])) {
					treeJSON.append(",\"checked\":true");
				}
			}

			treeJSON.append("}");
			if (i != templateList.size() - 1)
				treeJSON.append(",");
		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}

	/**
	 * 根据节点id，找到对应的机构编号和模板
	 * 
	 * @param id
	 * @return
	 */
	public List getNodeObjectList(Integer nodeId) {
		List result = null;
		List list = null;
		List templateList = null;
		String hsql = "select t.id.orgId from WorkTaskNodeObject t where t.id.nodeIoFlag=0 and t.id.nodeId="
				+ nodeId;
		String orgIds = "";
		Set templateIds = new HashSet();
		result = new ArrayList();

		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			int count = 0;// 目前只考虑多个机构对应的报表相同的情况
			String orgId = (String) list.get(i);
			if (orgIds != "")
				orgIds += ",";
			orgIds += orgId;
			// 将每个机构对应的template找出来，存到set里，保证不会重复
			if (count == 0) {
				String hql = "select t.id.templateId from WorkTaskNodeObject t where t.id.nodeIoFlag=0 and t.orgId='"
						+ orgId + "' and t.id.nodeId=" + nodeId;
				try {
					templateList = this.findListByHsql(hql, null);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				for (int j = 0; j < templateList.size(); j++) {
					String temp = (String) templateList.get(i);
					templateIds.add(temp);
				}
				count++;
			}

		}
		result.add(orgIds);
		result.add(templateIds);
		return result;

	}

	/**
	 * 根据节点id,任务id，找到对应的机构编号和模板
	 * 
	 * @param id
	 * @return
	 */
	public List getNodeObjectList(String taskId, String nodeId) {
		List result = null;
		List list = null;
		List templateList = null;
		String hsql = "select distinct t.id.orgId from WorkTaskNodeObject t where t.id.nodeIoFlag=0 and t.id.nodeId="
				+ nodeId;
		String orgIds = "";
		Set templateIds = new HashSet();
		result = new ArrayList();

		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			int count = 0;// 目前只考虑多个机构对应的报表相同的情况
			String orgId = (String) list.get(i);
			if (!orgIds.equals(""))
				orgIds += ",";
			orgIds += orgId;
			// 将每个机构对应的template找出来，存到set里，保证不会重复
			if (count == 0) {
				String hql = "select t.id.templateId from WorkTaskNodeObject t where t.id.nodeIoFlag=0 and t.id.orgId='"
						+ orgId + "' and t.id.nodeId=" + nodeId;
				try {
					templateList = this.findListByHsql(hql, null);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				for (int j = 0; j < templateList.size(); j++) {
					String temp = (String) templateList.get(j);
					templateIds.add(temp);
				}
				count++;
			}

		}
		result.add(orgIds);
		result.add(templateIds);
		return result;

	}

	private String iteratorCreate(String id) {
		List orgList = getChildListByOrgId(id);

		if (orgList == null || orgList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",\"text\":\"" + strs[1]
					+ "\"");

			treeJSON.append(iteratorCreate(strs[0]));
			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");

		}
		treeJSON.append("]");
		return treeJSON.toString();
	}

	private String iteratorCreateEdit(String id, String[] arry) {
		List orgList = getChildListByOrgId(id);

		if (orgList == null || orgList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0] + "\",\"text\":\"" + strs[1]
					+ "\"");
			for (int j = 0; j < arry.length; j++) {
				if (strs[0].equals(arry[j])) {
					treeJSON.append(",\"checked\":true");
				}
			}
			treeJSON.append(iteratorCreateEdit(strs[0], arry));
			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");

		}
		treeJSON.append("]");
		return treeJSON.toString();
	}

	public List getAllFirstOrg() {

		List result = null;
		List list = null;
		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect,oi.id.preOrgId from ViewWorktaskOrg oi where oi.id.preOrgId is null or oi.id.preOrgId = '0' ";

		result = new ArrayList();

		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {

			Object[] os = (Object[]) list.get(i);

			String[] strs = new String[4];

			strs[0] = ((String) os[0]).trim();

			strs[1] = ((String) os[1]).trim();

			strs[3] = ((String) os[3]).trim();

			Integer integer = (Integer) os[2];

			if (integer == null
					|| integer.toString().equals(WorkTaskConfig.NOT_IS_COLLECT))

				strs[2] = WorkTaskConfig.NOT_IS_COLLECT;

			else

				strs[2] = WorkTaskConfig.IS_COLLECT;

			result.add(strs);
		}
		// 查询出所有顶层虚拟机构（在AfCollectRelation表中查询所有没有ColletId ，或者colletid为实体机构的虚拟机构）
		String sql = "select oi.org_Id,oi.org_Name,oi.is_Collect ,oi.pre_Org_Id from View_Worktask_Org oi "
				+ "where oi.org_Id  not in "
				+ "(select a.org_Id from AF_COLLECT_RELATION a where a.collect_Id not in "
				+ "(select c.org_Id from View_Worktask_Org c where c.pre_Org_Id !='"
				+ WorkTaskConfig.COLLECT_ORG_PARENT_ID
				+ "'))  and oi.pre_Org_Id = '"
				+ WorkTaskConfig.COLLECT_ORG_PARENT_ID + "'";
		System.out.println(sql);

		try {
			list = this.findListBySql(sql, null);
		} catch (BaseServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < list.size(); i++) {

			Object[] os = (Object[]) list.get(i);

			String[] strs = new String[4];

			strs[0] = ((String) os[0]).trim();

			strs[1] = ((String) os[1]).trim();

			strs[3] = ((String) os[3]).trim();

			System.out.println(os[2]);
			Integer integer = new Integer(os[2].toString());

			if (integer == null
					|| integer.toString().equals(WorkTaskConfig.NOT_IS_COLLECT))

				strs[2] = WorkTaskConfig.NOT_IS_COLLECT;

			else

				strs[2] = WorkTaskConfig.IS_COLLECT;

			result.add(strs);
		}

		if (result.size() == 0)

			result = null;

		return result;
	}

	public List getAllTemplate(Operator operator) {

		List result = null;
		List list = null;
		String hsql = "select t.id.templateId,t.id.templateName from ViewWorktaskTemplate t ";
		if(operator.getChildRepReportPopedom()!=null&&!"".equals(operator.getChildRepReportPopedom())){
			hsql+=" and t.id.templateId in ("+operator.getChildRepReportPopedom()+")";
		}
		result = new ArrayList();

		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {

			Object[] os = (Object[]) list.get(i);

			String[] strs = new String[2];

			strs[0] = ((String) os[0]).trim();

			strs[1] = ((String) os[1]).trim();

			result.add(strs);
		}

		return result;
	}

	public List getFirstOrgById(String orgId) {

		List result = null;

		result = new ArrayList();

		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect from ViewWorktaskOrg oi where oi.id.orgId = "
				+ orgId;

		List list;
		try {
			list = this.findListByHsql(hsql, null);

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();

				Integer integer = new Integer(os[2].toString());

				if (integer == null
						|| integer.toString().equals(
								WorkTaskConfig.NOT_IS_COLLECT))

					strs[2] = WorkTaskConfig.NOT_IS_COLLECT;

				else

					strs[2] = WorkTaskConfig.IS_COLLECT;

				result.add(strs);
			}
		} catch (BaseServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result.size() == 0)

			result = null;

		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 title:该方法用于根据orgId返回其下属机构列表列表 author:chenbing
	 * date:2008-2-19
	 * 
	 * @param orgId
	 * @return List:LableValueBean:机构ID,机构名称
	 */
	public List getChildListByOrgId(String id) {
		List result = null;
		result = new ArrayList();
		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect ,oi.id.preOrgId from ViewWorktaskOrg oi where oi.id.preOrgId='"
				+ id + "' ";
		List list = null;
		try {
			list = this.findListByHsql(hsql, null);

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();

				Integer integer = new Integer(os[2].toString());

				if (integer == null
						|| integer.toString().equals(
								WorkTaskConfig.NOT_IS_COLLECT))

					strs[2] = WorkTaskConfig.NOT_IS_COLLECT;

				else

					strs[2] = WorkTaskConfig.IS_COLLECT;
				result.add(strs);
			}
		} catch (BaseServiceException e) {
			e.printStackTrace();
			this.log.error(e.getMessage());
		}
		if (result.size() == 0)

			result = null;

		return result;

	}

	public IWorkTaskNodeRoleServiceFlow getNodeRoleService() {
		return nodeRoleService;
	}

	public void setNodeRoleService(IWorkTaskNodeRoleServiceFlow nodeRoleService) {
		this.nodeRoleService = nodeRoleService;
	}

	@Override
	public void save(HttpSession session, int countNode)
			throws BaseServiceException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		WorkTaskInfo task=(WorkTaskInfo)session.getAttribute("task");
		WorkTaskNodeInfo nodeInfo=null;
		List list=(List)session.getAttribute("nodeList");
//		if(vo!=null){//非空判断
			//增加任务
			if(task!=null){
				
				workTaskInfoService.save(task);
				
			}
			for (int n = 0; n <list.size();n++) {
				WorkTaskNodeDefineVo vo=null;
				WorkTaskNodeDefineVo preVo=null;
				if(n==0){
//				 vo=(WorkTaskNodeDefineVo)session.getAttribute("nodeDefineVo"+n);
//				 preVo=(WorkTaskNodeDefineVo)session.getAttribute("nodeDefineVo"+n);
				 vo=(WorkTaskNodeDefineVo)list.get(n);
				 preVo=(WorkTaskNodeDefineVo)list.get(n);
				}else{
//					 vo=(WorkTaskNodeDefineVo)session.getAttribute("nodeDefineVo"+n);
//					 preVo=(WorkTaskNodeDefineVo)session.getAttribute("nodeDefineVo"+(n-1));
					 vo=(WorkTaskNodeDefineVo)list.get(n);
					 preVo=(WorkTaskNodeDefineVo)list.get(n-1);
				}
				//增加节点
				vo.setTaskId(this.getTaskId(task));
				int nodeId=getNodeId(vo.getTaskId());
				nodeInfo=new WorkTaskNodeInfo();
				nodeInfo.setNodeName(vo.getNodeName());
				nodeInfo.setNodeTime(vo.getNodeTime());
				if (nodeId==0) {
					
					nodeInfo.setPreNodeId(-1);
					
				}else{
					nodeInfo.setPreNodeId(getNodeId(vo.getTaskId()));
				}
				nodeInfo.setCondTypeId(vo.getConductType());
				nodeInfo.setRelationTaskId(vo.getRelationTaskId());
				nodeInfo.setTaskId(vo.getTaskId());
				this.save(nodeInfo);
				
				//增加节点角色表
				WorkTaskNodeRole nodeRole=new WorkTaskNodeRole();
				WorkTaskNodeRoleId nodeRoleId=new WorkTaskNodeRoleId();
				nodeRoleId.setNodeId(getNodeId(vo.getTaskId()));
				nodeRoleId.setRoleId(vo.getRoleId());
				nodeRole.setId(nodeRoleId);
				nodeRoleService.save(nodeRole);
				//增加节点对象信息
				//输入输出都增加
				String[] orgIdArr=vo.getOrgIds().split(",");
				String[] templateIdArr=vo.getTemplateIds().split(",");
				String[] orgIdArrPre=preVo.getOrgIds().split(",");
				String[] templateIdArrPre=preVo.getTemplateIds().split(",");
				if(n==0){
					
					for (int i = 0; i < orgIdArr.length; i++) {
						
						for (int j = 0; j < templateIdArr.length; j++) {
							WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
							WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
							objectId.setNodeId(getNodeId(vo.getTaskId()));
							objectId.setOrgId(orgIdArr[i]);
							
							objectId.setTemplateId(templateIdArr[j].split("_")[0]);
							objectId.setNodeIoFlag(0);//0代表输入
							nodeObject.setId(objectId);
							objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
							objectId=new WorkTaskNodeObjectId();
							nodeObject=new WorkTaskNodeObject();
							objectId.setNodeId(getNodeId(vo.getTaskId()));
							objectId.setOrgId(orgIdArr[i]);
							
							objectId.setTemplateId(templateIdArr[j].split("_")[0]);
							objectId.setNodeIoFlag(1);//0代表输入
							nodeObject.setId(objectId);
							objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
						}
					}
				}else{

					
					for (int i = 0; i < orgIdArrPre.length; i++) {
						
						for (int j = 0; j < templateIdArrPre.length; j++) {
							WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
							WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
							objectId.setNodeId(getNodeId(vo.getTaskId()));
							objectId.setOrgId(orgIdArrPre[i]);
							
							objectId.setTemplateId(templateIdArrPre[j].split("_")[0]);
							objectId.setNodeIoFlag(0);//0代表输入
							nodeObject.setId(objectId);
							objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
							
						}
					}
					for (int i = 0; i < orgIdArr.length; i++) {
						
						for (int j = 0; j < templateIdArr.length; j++) {
							WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
							WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
							objectId.setNodeId(getNodeId(vo.getTaskId()));
							objectId.setOrgId(orgIdArr[i]);
							
							objectId.setTemplateId(templateIdArr[j].split("_")[0]);
							objectId.setNodeIoFlag(1);//1代表输出
							nodeObject.setId(objectId);
							objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
							
						}
					}
				
				}
				
				//增加报送范围
				try {
					saveRalation(task, vo.getOrgIds(), vo.getTemplateIds());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		
		
			
//		}
		
	}

	@Override
	public void save(List<WorkTaskNodeDefineVo> nodeVos, String type,
			WorkTaskInfo task) throws Exception {
		// TODO Auto-generated method stub
		if(nodeVos==null || nodeVos.isEmpty() || task==null)
			return;
			//增加任务
		workTaskInfoService.save(task);
		if(task.getTaskId()!=null){
			for (int n = 0; n <nodeVos.size();n++) {
				WorkTaskNodeDefineVo vo=null;
				WorkTaskNodeDefineVo preVo=null;
				if(n==0){
				 vo=(WorkTaskNodeDefineVo)nodeVos.get(n);
				 preVo=(WorkTaskNodeDefineVo)nodeVos.get(n);
				}else{
					 vo=(WorkTaskNodeDefineVo)nodeVos.get(n);
					 preVo=(WorkTaskNodeDefineVo)nodeVos.get(n-1);
				}
				//增加节点
				vo.setTaskId(this.getTaskId(task));
				int nodeId=getNodeId(vo.getTaskId());
				WorkTaskNodeInfo nodeInfo=new WorkTaskNodeInfo();
				nodeInfo.setNodeName(vo.getNodeName());
				nodeInfo.setNodeTime(vo.getNodeTime());
				if (nodeId==0) {
					
					nodeInfo.setPreNodeId(-1);
					
				}else{
					nodeInfo.setPreNodeId(getNodeId(vo.getTaskId()));
				}
				nodeInfo.setCondTypeId(vo.getConductType());
				nodeInfo.setRelationTaskId(vo.getRelationTaskId());
				nodeInfo.setTaskId(vo.getTaskId());
				this.save(nodeInfo);
				
				//增加节点角色表
				WorkTaskNodeRole nodeRole=new WorkTaskNodeRole();
				WorkTaskNodeRoleId nodeRoleId=new WorkTaskNodeRoleId();
				nodeRoleId.setNodeId(getNodeId(vo.getTaskId()));
				nodeRoleId.setRoleId(vo.getRoleId());
				nodeRole.setId(nodeRoleId);
				nodeRoleService.save(nodeRole);
				//增加节点对象信息
				//输入输出都增加
//				String[] orgIdArr=vo.getOrgIds().split(",");
//				String[] templateIdArr=vo.getTemplateIds().split(",");
//				String[] orgIdArrPre=preVo.getOrgIds().split(",");
//				String[] templateIdArrPre=preVo.getTemplateIds().split(",");
				Map orgMappingRelations = vo.getNodeObject();
				for(Object key : orgMappingRelations.keySet()){
					String org = (String)key;
					String templateStr = (String)orgMappingRelations.get(org);
					String[] templates = templateStr.split(",");
					for (int i = 0; i < templates.length; i++) {
						WorkTaskNodeObjectId objectId=new WorkTaskNodeObjectId();
						WorkTaskNodeObject nodeObject=new WorkTaskNodeObject();
						objectId.setNodeId(getNodeId(vo.getTaskId()));
						objectId.setOrgId(org);
						
						objectId.setTemplateId(templates[i].split("_")[0]);
						objectId.setNodeIoFlag(0);//0代表输入
						nodeObject.setId(objectId);
						objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
						objectId=new WorkTaskNodeObjectId();
						nodeObject=new WorkTaskNodeObject();
						objectId.setNodeId(getNodeId(vo.getTaskId()));
						objectId.setOrgId(org);
						
						objectId.setTemplateId(templates[i].split("_")[0]);
						objectId.setNodeIoFlag(1);//0代表输入
						nodeObject.setId(objectId);
						objectService.save(nodeObject);//一个节点，一个机构，对应n条报表
					}
					//增加报送范围
					saveRalation(task, org,templateStr);
				}
				
			} 
		}
	}

	

}
