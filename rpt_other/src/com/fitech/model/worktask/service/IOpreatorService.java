package com.fitech.model.worktask.service;

import java.util.Map;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.security.Operator;

public interface IOpreatorService extends IBaseService<ViewWorktaskRole, Long>{
	/**
	 * 查询用户
	 * @param operatorId
	 * @return
	 * @throws Exception
	 */
	public Operator findOneOperator(Long operatorId)throws Exception;
	
	/**
	 * 查找所有的角色
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	public Map findRoles(String roleIds)throws Exception;

	public String findNameById(Long operatorId) ;
	
}
