package com.fitech.model.worktask.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.ViewWorktaskRole;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IOpreatorService;

public class OperatorServiceImpl extends DefaultBaseService<ViewWorktaskRole, Long> implements IOpreatorService {

	@Override
	public Operator findOneOperator(Long operatorId) throws Exception {
		// TODO Auto-generated method stub
		Operator op = new Operator();
		String sql ="select o.telephone_number,o.SUPER_MANAGER from operator o where o.user_id="+operatorId;
		List list = this.objectDao.findListBySql(sql, null);
		if(list!=null){
			Object[] objs = (Object[])list.get(0);
			if(objs!=null && objs.length==2){
				op.setTelephone((String)objs[0]);
				op.setSuperManager("1".equals((String)objs[1])?true:false);
			}
		}
		return op;
	}

	
	@Override
	public  String  findNameById (Long operatorId) {
		// TODO Auto-generated method stub
		Operator op = new Operator();
		String sql ="select o.first_name||o.last_name from operator o where o.user_id="+operatorId;
		List list = new ArrayList();
		try {
			list = this.objectDao.findListBySql(sql, null);
		} catch (BaseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String objs = "";
		if(list.size()>0){
			objs = (String)list.get(0);
		}
		return objs;
	}
	@Override
	public Map findRoles(String roleIds) throws Exception {
		// TODO Auto-generated method stub
		Map roles = new HashMap();
		if(roleIds!=null && !roleIds.equals("")){
			String hsql = "from ViewWorktaskRole role where role.id.roleId in ("+roleIds+")";
			List ls = this.objectDao.findListByHsql(hsql, null);
			if(ls!=null && ls.size()>0){
				for (int i = 0; i < ls.size(); i++) {
					ViewWorktaskRole role = (ViewWorktaskRole)ls.get(i);
					if(role!=null && role.getId()!=null){
						roles.put(role.getId().getRoleId(),role.getId().getRoleName());
					}
				}
			}
		}
		return roles;
	}


}
