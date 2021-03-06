
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ColAbnormityChangeInfoForm;
import com.cbrc.smis.hibernate.ColAbnormityChangeInfo;
import com.cbrc.smis.util.FitechException;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for ReportInInfo data (i.e. 
 * com.cbrc.smis.form.ReportInInfoForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsColAbnormityChangeInfoDelegate {
	private static FitechException log=new FitechException(StrutsColAbnormityChangeInfoDelegate.class);
	
	/**
	 * 获得异常变化信息列表
	 * 
	 * @author rds
	 * @date 2006-01-03 0:07
	 * 
	 * @param repInId Integer 实际数据报表ID
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return List
	 */
	public static List find(Integer repInId,String childRepId,String versionId,String orgId){
		List resList=null;
		
		if(repInId==null || childRepId==null || versionId==null) return resList;
		
		DBConn conn=null;
		
		try{
			String hql="from ColAbnormityChangeInfo caci where caci.comp_id.repInId=" + repInId + 
				" and caci.comp_id.colName in (" + 
				"select cac.comp_id.colName from ColAbnormityChange cac where " +
				" cac.comp_id.childRepId='" + childRepId + "' and " +
				" cac.comp_id.versionId='" + versionId + "' and " +
				" cac.comp_id.orgId='" + orgId.trim() + "'" +
				")";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				ColAbnormityChangeInfo colAbnormityChangeInfo=null;
				for(int i=0;i<list.size();i++){
					colAbnormityChangeInfo=(ColAbnormityChangeInfo)list.get(i);
					ColAbnormityChangeInfoForm form=new ColAbnormityChangeInfoForm();
					TranslatorUtil.copyPersistenceToVo(colAbnormityChangeInfo,form);
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**
     * 唐磊
     * 根据传来的reportid查出所有的记录
     * @reportInId
     * @resList 返回一个包含所有记录的list集合(报表审核的信息详细查询==待用)
     * 异常变化中的实际变化情况(清单式的)
     */
    public static List getAllColAbnormityChangeInfo(Integer reportInId)
    {
        List retVals = null;
        DBConn conn = null;
        Session session = null;
        
        if(reportInId!=null)
        {
            try
            {        
                conn = new DBConn();
                session = conn.openSession();
                StringBuffer hql = new StringBuffer("from ColAbnormityChangeInfo caci where caci.comp_id.repInId="+reportInId);
                List reportList = session.createQuery(hql.toString()).list();
                if(reportList!=null && reportList.size()!=0)
                {
                    for (Iterator it = reportList.iterator(); it.hasNext();)
                    {
                    	ColAbnormityChangeInfo colAbnormityChangeInfoPersistence = new ColAbnormityChangeInfo();
                        retVals = new ArrayList();
                        while (it.hasNext()) {
                        	colAbnormityChangeInfoPersistence = (ColAbnormityChangeInfo) it.next();
                        	ColAbnormityChangeInfoForm colAbnormityChangeInfoForm = new ColAbnormityChangeInfoForm();
                            TranslatorUtil.copyPersistenceToVo(colAbnormityChangeInfoPersistence, colAbnormityChangeInfoForm);
                            retVals.add(colAbnormityChangeInfoForm);
                        }
                    }
                }
            }catch(Exception e)
            {
                log.printStackTrace(e);
                conn.closeSession();
            }
            finally
            {
                if(conn!=null) conn.closeSession();
            }          
        }
        return retVals;
    }
}