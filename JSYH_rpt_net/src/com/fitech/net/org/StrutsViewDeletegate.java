package com.fitech.net.org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.org.form.OrgnetForm;


public class StrutsViewDeletegate {
	private static FitechException log = new FitechException(StrutsViewDeletegate.class);
	
	public static List GetNextChildren(String orgId)
	{
		List retVals = null;
		DBConn conn = null;
		try {
			StringBuffer hql = new StringBuffer(
					"select distinct m.orgId,m.orgName"
							+ " from Orgnet m where m.parent_Org_Id='"+orgId.trim()+"'");
			
			conn = new DBConn();			
			Session session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();
		
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				retVals = new ArrayList();
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					OrgnetForm orgnetForm=new OrgnetForm();
					orgnetForm.setOrgId(item[0] != null ? (String) item[0] : "");
					orgnetForm.setOrgName(item[1] != null ? (String) item[1] : "");
					retVals.add(orgnetForm);
				}
			}
		}catch (HibernateException he) {			
			he.printStackTrace();
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		
		return retVals;
	}
	
	public static List GetAllChildren(String orgId)
	{
		List retVals=new ArrayList();
		List preList=null;//上级列表
		List CList=null;//当前列表
		List tempList=new ArrayList();
		tempList=GetNextChildren(orgId);
		if(tempList==null)return retVals;
		preList=tempList;
		retVals.add(tempList);
		while(true)
		{			
			for(int i=0;i<preList.size();i++)
			{
				tempList=GetNextChildren(((OrgnetForm)preList.get(i)).getOrgId());
				if(tempList!=null)
				{
					if(CList==null)
						CList=tempList;
					else
					{
						for(int j=0;j<tempList.size();j++)
						{
							CList.add((OrgnetForm)tempList.get(j));
						}
					}
				}
			}
			if(CList==null)break;
			else
			{
				retVals.add(CList);
				preList=CList;
				CList=null;				
			}
		}
		return retVals;
	}
}
