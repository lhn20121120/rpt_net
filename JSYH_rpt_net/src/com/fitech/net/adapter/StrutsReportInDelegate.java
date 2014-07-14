package com.fitech.net.adapter;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechException;
/**
*
* <p>Title: StrutsReportInDelegate </p>
*
* <p>Description 内网实际表单表对象数据库操作代理类</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-14
* @version 1.0
*
*/
public class StrutsReportInDelegate {

    private static FitechException log=new FitechException(StrutsReportInDelegate.class);
    
    public StrutsReportInDelegate(){}
    /**
     * 根据主键查找ReportIn
     * @param repInId       Integer         内网实际表单表Id
     * @return
     */
    public static ReportIn findById(Integer repInId)
    {
        ReportIn reportIn = null;       
        DBConn conn=null;
        Session session=null;    
        
        if( repInId == null) return null;     
            
        try{
            conn = new DBConn();
            session = conn.openSession();
            reportIn = (ReportIn)session.get(ReportIn.class,repInId);
            if(reportIn != null)
                reportIn.getReportInInfos().iterator();
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();         
        }
        return reportIn;
    }
    
    /**
     * 查找当期同种已转上报数据的内网实际表单表对象
     * @param repInId       Integer         内网实际表单表Id
     * @return  ReportIn
     */
    public static ReportIn findHasTranslation(Integer repInId,String orgId)
    {
        ReportIn reportIn = null;       
        DBConn conn=null;
        Session session=null;    
        
        if( repInId == null || orgId == null) return null;     
            
        try{
            conn = new DBConn();
            session = conn.openSession();
            //获取转上报数据的内网实际表单表对象
            ReportIn _reportIn = (ReportIn)session.get(ReportIn.class,repInId);
            if(_reportIn != null){
                String hql = "from ReportIn t where t.times = (select max(p.times) ";
                        hql += "from ReportIn p where p.MChildReport.comp_id.childRepId='";
                        hql += _reportIn.getMChildReport().getComp_id().getChildRepId();
                        hql += "' and p.MChildReport.comp_id.versionId='";
                        hql += _reportIn.getMChildReport().getComp_id().getVersionId();
                        hql += "' and p.year=" + _reportIn.getYear();
                        hql += " and p.term=" + _reportIn.getTerm();
                        hql += " and p.orgId='" + orgId;
                        hql += "' and p.times > 0)";
                        hql += " and t.MChildReport.comp_id.childRepId='";
                        hql += _reportIn.getMChildReport().getComp_id().getChildRepId();
                        hql += "' and t.MChildReport.comp_id.versionId='";
                        hql += _reportIn.getMChildReport().getComp_id().getVersionId();
                        hql += "' and t.year=" + _reportIn.getYear();
                        hql += " and t.term=" + _reportIn.getTerm();
                        hql += " and t.orgId='" + orgId +"'";
                reportIn = (ReportIn)session.createQuery(hql).uniqueResult();
            }
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();         
        }
        return reportIn;
    }
}