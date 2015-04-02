package com.fitech.model.worktask.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskNodeRole;

public interface IWorkTaskNodeRoleServiceFlow extends IBaseService<WorkTaskNodeRole, String> {
	/**
	 * 根据角色id,查询该角色所拥有的全部节点对象
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	public List<WorkTaskNodeRole> findWorkTaskNodeRoles(String roleIds)throws Exception;
}
