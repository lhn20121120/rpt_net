package com.fitech.model.worktask.service.impl;

import java.util.List;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;
import com.fitech.model.worktask.service.IWorkTaskNodeRoleServiceFlow;
/**
 * add by 王明明
 * 工作节点定义
 * @author Administrator
 *
 */
public class WorkTaskNodeRoleServiceImpl extends DefaultBaseService<WorkTaskNodeRole,String> implements  IWorkTaskNodeRoleServiceFlow {

	@Override
	public List<WorkTaskNodeRole> findWorkTaskNodeRoles(String roleIds)
			throws Exception {
		// TODO Auto-generated method stub
		if(roleIds==null || roleIds.equals(""))
			return null;
		String hsql = "from WorkTaskNodeRole rl where rl.id.roleId in("+roleIds+")";
		List list = this.objectDao.findListByHsql(hsql, null);
		return list;
	}
	
	
}
