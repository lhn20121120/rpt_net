package com.fitech.net.adapter;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;


import net.sf.hibernate.Criteria;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.hibernate.JobLog;
import com.fitech.net.hibernate.JobLogKey;
import com.fitech.net.hibernate.OrgNet;

/**
*
* <p>标题: StrutsJobLogDelegate</p>
*
* <p>描述: JobLog对象的数据库操作 </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2007-11-15
* @version 1.0
* @version 1.1 2008-02-20
*              修改 findJobLogByCriteria 为 findJobLogByHql
*              并修改方法中内容
*/

public class StrutsJobLogDelegate
{

    private static FitechException log = new FitechException(StrutsJobLogDelegate.class);
   
    /**
     * 查询当前机构的所有下级机构的ETL数据提取情况
     * @param orgLst        List        当前机构的下级机构集合
     * @param apartPage     ApartPage   分页对象
     * @return  List
     */
    public static List findAllJobLog(List orgLst,ApartPage apartPage)
    {
       return findJobLogByHql(new JobLog(),orgLst,apartPage);
    }
    
    /**
     * 查询指定机构或所有下级机构的ETL数据提取情况
     * @param job           JobLog      JobLog对象
     * @param orgLst        List        当前机构的下级机构集合
     * @param apartPage     ApartPage   分页对象
     * @return  List
     * 2008-02-20修改 QBC 查询为HQL查询提高分页查询的效率
     * 
     */
    public static List findJobLogByHql(JobLog job,List orgLst,ApartPage apartPage)
    {
        DBConn conn = null;
        Session session = null;
        List jobLogLst = null;
        try
        {
            if(job != null && apartPage != null)
            {
                conn  = new DBConn();
                session = conn.openSession();
               
                JobLogKey jobKey = job.getId();
                String hql = "";
                
                if(jobKey != null)
                {
                    OrgNet org = jobKey.getOrg();
                    //如果用户选择了某个机构
                    if(org != null)
                    {
                        String orgId = org.getOrgId();
                        if(StringUtils.isNotEmpty(orgId))
                        //criteria.add(Expression.eq("this.id.org.orgId",orgId));
                            hql = " and this.id.org.orgId = '" + orgId + "'";                            
                    }
                    else//如果用户选择全部下级机构
                    {
                        if(orgLst == null || orgLst.isEmpty()) return null;
                        //criteria.add(Expression.in("this.id.org",orgLst));
                        hql = hql + " and this.id.org.id in (";
                        Iterator itr = orgLst.iterator();
                        String temp = "";
                        while(itr.hasNext())
                        {
                             temp += "'"+((OrgNet) itr.next()).getOrgId()+"',";
                        }
                        temp = temp.substring(0,temp.lastIndexOf(","));
                        hql = hql + temp + ")";
                    }
                    Integer year = jobKey.getYear();
                    Integer term = jobKey.getTerm();  
                    String repId = jobKey.getRepId();
                    String versionId = jobKey.getVersionId();
                    if(year != null)
                    //    criteria.add(Expression.eq("this.id.year",year));
                        hql = hql + " and this.id.year = " + year;
                    if(term != null)
                    // criteria.add(Expression.eq("this.id.term",term));
                        hql = hql + " and this.id.term = " + term;
                    if(repId != null)
                        //criteria.add(Expression.eq("this.id.repId",repId));
                        hql = hql + " and this.id.repId = " + repId;
                    if(versionId != null)
                        //criteria.add(Expression.eq("this.id.version",versionId));
                        hql = hql + " and this.id.version = " + versionId;
                }
                String repName = job.getRepName();
                if(StringUtils.isNotEmpty(repName))
                    //criteria.add(Expression.like("this.repName",repName,MatchMode.ANYWHERE));
                    hql = hql + " and this.repName like '%"+ repName + "%'";
                String jobSts = job.getJobSts();
                if(StringUtils.isNotEmpty(jobSts))
                    //criteria.add(Expression.eq("this.jobSts",jobSts));
                    hql = hql + " and this.jonSts = " + jobSts;              
                Query query = session.createQuery("select count(*) from JobLog this where 1=1 " + hql);                           
                int size = Integer.parseInt(query.uniqueResult().toString());
                if(size > 0)
                {
                    int from = (apartPage.getCurPage()-1)*Config.PER_PAGE_ROWS;  
                    if(from >= size) from = 0;
                    apartPage.setCount(size);
                    query = session.createQuery("from JobLog this where 1=1 " + hql);
                    
                    jobLogLst = query.setFirstResult(from).setMaxResults(Config.PER_PAGE_ROWS).list();
                }
                
                
                
                //提取分页记录
               /* if(jobLogLst != null && !jobLogLst.isEmpty())
                {
                    int from = (apartPage.getCurPage()-1)*Config.PER_PAGE_ROWS;  
                    int to = from + Config.PER_PAGE_ROWS;
                    int size = jobLogLst.size();
                    if(from >= size) from = 0;
                    if(to > size) to = size;
                    apartPage.setCount(size);
                    jobLogLst = jobLogLst.subList(from,to); 
                }*/
            }                    
        }
        catch(Exception e)
        {
            log.printStackTrace(e);
        }
        finally
        {
            if(conn != null)
                conn.closeSession();
        }
        return jobLogLst; 
    }
        
    /**
     * 根据主键查找ETL数据提取情况
     * @param job           JobLog      JobLog对象
     * @return  JobLog
     */
    public static JobLog findJobLogById(JobLog job)
    {
        DBConn conn = null;
        Session session = null;
        JobLog jobLog = null;
        try
        {
            if(job != null)
            {
                conn  = new DBConn();
                session = conn.openSession();                
                JobLogKey jobKey = job.getId();
                if(jobKey != null)
                {
                    Criteria criteria = session.createCriteria(JobLog.class);
                    criteria.add(Expression.eq("this.id",jobKey));
                    jobLog = (JobLog)criteria.uniqueResult();
                }
            }                    
        }
        catch(Exception e)
        {
            log.printStackTrace(e);
        }
        finally
        {
            if(conn != null)
                conn.closeSession();
        }
        return jobLog; 
    }
    /**
     * 修改指定机构的单张报表的ETL数据提起情况
     * @param job       JobLog      JobLog对象
     * @return  修改成功返回true  失败返回false
     */
    public static boolean updateJobSts(List jobLogLst)
    {
        boolean update = false;
        DBConn conn = null;
        Session session = null;
        try
        {
            if(jobLogLst != null && !jobLogLst.isEmpty())
            {
                conn  = new DBConn();
                session = conn.beginTransaction();
                Iterator itr = jobLogLst.iterator();
                int count = 0;
                while(itr.hasNext())
                {
                    JobLog job = (JobLog) itr.next();
                    //使用悲观锁防止自动运行的线程和本次操作修改同一记录
                    JobLog _job = (JobLog)session
                        .get(JobLog.class,job.getId(),LockMode.UPGRADE_NOWAIT);
                    if(_job != null)
                    {                     
                        _job.setJobSts(job.getJobSts());
                        _job.setJobLog(job.getJobLog());
                        session.update(_job);
                        count ++;
                    }                
                }
                if(count == jobLogLst.size())
                {
                    session.flush();
                    update = true;
                }
            }
        }
        catch(Exception e)
        {
            log.printStackTrace(e);
        }
        finally
        {
            if(conn != null)
                conn.endTransaction(update);
        }
        return update;
    }
}