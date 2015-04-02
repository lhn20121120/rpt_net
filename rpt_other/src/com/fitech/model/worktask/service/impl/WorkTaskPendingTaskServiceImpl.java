package com.fitech.model.worktask.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.model.commoninfo.model.pojo.UserRole;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeInfo;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoni;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeMoniId;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.model.pojo.WorkTaskType;
import com.fitech.model.commoninfo.security.Operator;
import com.fitech.model.worktask.service.InfoMessionUserService;
import com.fitech.model.worktask.service.WorkTaskNodeMoniService;
import com.fitech.model.worktask.service.WorkTaskPendingTaskService;
import com.fitech.model.worktask.vo.WorkTaskPendingTaskVo;

public class WorkTaskPendingTaskServiceImpl extends DefaultBaseService<WorkTaskNodeMoni, String> implements WorkTaskPendingTaskService {

	private WorkTaskNodeMoniService nodeMoniService;
	
	private InfoMessionUserService  mUserService;
	
	public void setNodeMoniService(WorkTaskNodeMoniService nodeMoniService) {
		this.nodeMoniService = nodeMoniService;
	}
	
	public InfoMessionUserService getmUserService() {
		return mUserService;
	}

	public void setmUserService(InfoMessionUserService mUserService) {
		this.mUserService = mUserService;
	}

	@Override
	public boolean findTaskIsHjOrDJ(List<WorkTaskPendingTaskVo> pvos)
			throws Exception {
		// TODO Auto-generated method stub
		if(pvos!=null && !pvos.isEmpty())
		{
			Long taskMoniId = pvos.get(0).getTaskMoniId();
			Integer nodeId = pvos.get(0).getNodeId();
			for (int i = 1; i < pvos.size(); i++) {
				if(!taskMoniId.equals(pvos.get(i).getTaskMoniId())
						|| !nodeId.equals(pvos.get(i).getNodeId()))
					return false;
			}
		}
		return true;
	}

	@Override
	public WorkTaskType findOneWorkTaskType(String taskTypeId) throws Exception {
		// TODO Auto-generated method stub
		if(taskTypeId==null || taskTypeId.equals(""))
			return null;
		String hsql = "from WorkTaskType tp where tp.taskTypeId='"+taskTypeId+"'";
		return (WorkTaskType)this.objectDao.findObject(hsql);
	}

	@Override
	public void findBeforeNodesByNodeId(Integer nodeId,
			List<WorkTaskNodeInfo> nodeInfos) {
		// TODO Auto-generated method stub
		if(nodeId==null)
			return;
		String hsql = "from WorkTaskNodeInfo ni where ni.nodeId="+nodeId;
		WorkTaskNodeInfo nodeInfo = (WorkTaskNodeInfo)this.objectDao.findObject(hsql);
		if(nodeInfo!=null)
			nodeInfos.add(nodeInfo);
		this.findBeforeNodesByNodeId(nodeInfo==null?null:nodeInfo.getPreNodeId(),nodeInfos);
	}

	@Override
	public void performByNodeId(Integer nodeId,String term,String orgId) throws Exception {
		// TODO Auto-generated method stub
		// List operList = //查询该节点参与用户
		if(nodeId==null)
			return;
		List<Operator> operList = new ArrayList<Operator>();
		String hql = "from WorkTaskNodeRole role where role.id.nodeId="+nodeId;
		List<WorkTaskNodeRole> roles = this.objectDao.findListByHsql(hql, null);
		if(roles!=null && !roles.isEmpty()){
			for (int i = 0; i < roles.size(); i++) {
				WorkTaskNodeRole role = roles.get(i);
				hql = "from UserRole ur where ur.id.roleId="+role.getId().getRoleId();
				List<UserRole> userRoles = this.objectDao.findListByHsql(hql, null);
				if(userRoles!=null && !userRoles.isEmpty()){
					for (int j = 0; j < userRoles.size(); j++) {
						UserRole userRole = userRoles.get(j);
						hql = "select op.id.userId,op.id.userName,op.id.orgId,op.id.identificationNumber" +
								",op.id.firstName,op.id.lastName "+
								"from com.fitech.model.commoninfo.model.pojo.Operator" +
								" op where op.id.userId="+userRole.getId().getUserId()+" and op.id.orgId='"+orgId+"'";
						Object[] simpleOperator = (Object[])this.objectDao.findObject(hql);
						if(simpleOperator!=null && simpleOperator.length>0){
							Operator op = new Operator();
							op.setOperatorName((String)simpleOperator[1]);
							op.setOperatorId((Long)simpleOperator[0]);
							op.setOrgId((String)simpleOperator[2]);
							op.setIdentificationNumber((String)simpleOperator[3]);//电话号码
							op.setUserName((String)simpleOperator[4]+(String)(simpleOperator[5]==null?"":(String)simpleOperator[5]));
							
							String msg = "用户["+op.getUserName()+"]在监管报送系统中存在一项"+term+"期的待处理任务，请及时处理!";
							op.setMobileInfo(msg);
							op.setShortMessage(msg);
							operList.add(op);
						}
					}
				}
			}
			if(!operList.isEmpty()){
				mUserService.infoMessionUsers(operList);
			}
		}
		//InfoMessionUserService.InfoMessionUsers(operList); //执行相关操作
	}

	@Override
	public void performByPendingTask(WorkTaskPendingTaskVo pvos)
			throws Exception {
		// TODO Auto-generated method stub
	//	Integer nodeId = ....//查询出相关节点
		if(pvos==null || pvos.getOrgId()==null
				|| pvos.getNodeId()==null || pvos.getTaskMoniId()==null)
			return;
		WorkTaskNodeMoni nodeMoni = new WorkTaskNodeMoni();
		WorkTaskNodeMoniId nodeMoniId= new WorkTaskNodeMoniId();
		nodeMoniId.setNodeId(pvos.getNodeId());
		nodeMoniId.setOrgId(pvos.getOrgId());
		nodeMoniId.setTaskMoniId(pvos.getTaskMoniId());
		nodeMoni.setId(nodeMoniId);
		WorkTaskNodeMoni executeTaskNodeMoni = nodeMoniService.findOneWorkTaskNodeMoni(nodeMoni);//查询正在执行的节点任务
		
		WorkTaskNodeMoni nextExecTaskNodemoni = nodeMoniService.findNextOneWorkNodeMoni(executeTaskNodeMoni); //查询当前处理的节点任务的下一个节点任务
		
		if(nextExecTaskNodemoni!=null && nextExecTaskNodemoni.getId()!=null
				&& nextExecTaskNodemoni.getId().getNodeId()!=null){
			this.performByNodeId( nextExecTaskNodemoni.getId().getNodeId()
					,DateUtil.toSimpleDateFormat(pvos.getTaskTerm(), DateUtil.NORMALDATE),nextExecTaskNodemoni.getId().getOrgId());//执行相关操作
		}
	//	this.performByNodeId(nodeId);//执行相关操作
	}

}
