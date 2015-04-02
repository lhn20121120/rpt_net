package com.fitech.net.adapter;

import java.util.List;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;

/**
*
* <p>Title: StrutsReportMappingDelegate </p>
*
* <p>Description ����ӳ�����ݿ����������</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-12
* @version 1.0
*/
//δʹ��
public class StrutsReportMappingDelegate {

    private static FitechException log=new FitechException(StrutsReportMappingDelegate.class);
    
    public StrutsReportMappingDelegate() {   }
    
    /**
     * ��ȡ���еı���ӳ���¼
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
