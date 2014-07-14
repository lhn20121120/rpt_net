package com.fitech.net.adapter;

import java.util.List;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;

/**
*
* <p>Title: StrutsReportMappingDelegate </p>
*
* <p>Description 报表映射数据库操作代理类</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-12
* @version 1.0
*/
//未使用
public class StrutsReportMappingDelegate {

    private static FitechException log=new FitechException(StrutsReportMappingDelegate.class);
    
    public StrutsReportMappingDelegate() {   }
    
    /**
     * 获取所有的报表映射记录
     * @return  List
     */
    public static List findAll(){
        List reportMappingLst = null;
        DBConn conn=null;
        Session session=null;
              
        try{                           
            conn = new DBConn();
            session = conn.openSession();
            String hql = "from ReportMapping";
            reportMappingLst = session.createQuery(hql).list();          
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();   
        }
        return reportMappingLst;
    }
}
