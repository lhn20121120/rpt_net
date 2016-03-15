package com.fitech.model.worktask.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.model.pojo.ViewWorktaskOrg;
import com.fitech.model.worktask.service.IWorkTaskOrgService;

public class WorkTaskOrgServiceImpl extends
		DefaultBaseService<ViewWorktaskOrg, String> implements
		IWorkTaskOrgService {

	public List getAllFirstOrg() {

		List result = null;
		List list = null;
		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect,oi.id.preOrgId from ViewWorktaskOrg oi " +
				"where oi.id.preOrgId is null or oi.id.preOrgId = '0' or  oi.id.preOrgId = '-99'  order by oi.id.isCollect";

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

		if (result.size() == 0)

			result = null;

		return result;
	}

	public List getFirstOrgById(String orgId,Long userId) {

		List result = null;

		result = new ArrayList();

		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect from ViewWorktaskOrg oi where oi.id.orgId = '"
				+ orgId+"' or (oi.id.orgId in (select vt.id.orgId from ViewWorkTaskPurOrg vt,ViewWorktaskOrg vo where vt.id.orgId=vo.id.orgId and vt.id.userId="+userId+"  and vo.id.preOrgId='-99' )) ";

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
	 * 
	 *
	 * 
	 * @param orgId
	 * @return List:LableValueBean:机构ID,机构名称
	 */
	public List getChildListByOrgId(String id ,String userId) {
		List result = null;
		result = new ArrayList();
		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect ,oi.id.preOrgId from ViewWorktaskOrg oi where oi.id.preOrgId='"
				+ id+"'" ;//+ "' and oi.id.orgId in ( select distinct(t.orgId) from ViewOrgRep t where t.userId = '"+userId+"' and t.powType=1 )";
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

	@Override
	public void getChildListByOrgId(List ls, String id, String userId) {
		// TODO Auto-generated method stub
		if(ls==null)
			return;
		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect ,oi.id.preOrgId from ViewWorktaskOrg oi where oi.id.preOrgId='"
				+ id+"'" ;//+ "' and oi.id.orgId in ( select distinct(t.orgId) from ViewOrgRep t where t.userId = '"+userId+"' and t.powType=1 )";
		try {
			List list = this.findListByHsql(hsql, null);
			if(list != null){
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
					ls.add(strs);
					
					getChildListByOrgId(ls, strs[0], userId);
				}
			}
		} catch (BaseServiceException e) {
			e.printStackTrace();
			this.log.error(e.getMessage());
		}
	}

	@Override
	public String  getOrgNameByOrgId(String orgId) {
		String name  = "";
		String hsql = "select oi.id.orgId,oi.id.orgName from ViewWorktaskOrg oi where oi.id.orgId='"+ orgId+"'" ;
		try {
			List list = this.findListByHsql(hsql, null);
			if(list != null){
				for (int i = 0; i < list.size(); i++) {
					Object[] os = (Object[]) list.get(i);
					name = ((String) os[1]).trim();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return name;
	}

}
