package com.cbrc.smis.adapter;

import java.util.Date;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportAgainSet;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechException;

public class StrutsReportInDelegate2 {
    private static FitechException log = new FitechException(
            StrutsReportInDelegate2.class);

    
    public static boolean selectReportInForm(com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {

    	boolean result = false;
        //创建连接
        DBConn conn=null;
        Session session=null;
        //reportform中是否有参数
        if (reportInForm==null){
        	return false;
        }
        
        try{
        	conn=new DBConn();
        	session=conn.beginTransaction();

        	//循环给持久化对象传递form对象的机构id数组的参数
        	if(reportInForm.getRepInIdArray()!=null && !reportInForm.getRepInIdArray().equals("")){
        		Integer repOutIds[]=new Integer[reportInForm.getRepInIdArray().length];
        		for(int i=0;i<reportInForm.getRepInIdArray().length;i++){
        			ReportIn reportInPersistence=(ReportIn)session.load(ReportIn.class, reportInForm.getRepInIdArray()[i]);
        			reportInPersistence.setCheckFlag(reportInForm.getCheckFlag());
        			if(reportInForm.getForseReportAgainFlag()!=null) 
        				reportInPersistence.setForseReportAgainFlag(reportInForm.getForseReportAgainFlag());
        				
        			repOutIds[i]=reportInPersistence.getRepOutId();
        		}
        		reportInForm.setRepOutIds(repOutIds);
        		result = true;
        	}else{
        		result = false;
        	}
        	
        	//异常的捕捉机制
        }catch(HibernateException he){
        	result=false;
        	log.printStackTrace(he);
        }catch(Exception e){
        	result=false;
        	log.printStackTrace(e);
        }
        finally{
        	//如果conn对象依然存在,则结束当前事务并断开连接
        	if (conn!=null)	conn.closeSession();
        }
        //更新成功返回吧!!!
        return result;
    }
    
    public static boolean insert(Integer[] repInIds,String cause){
		boolean result=false;
		if(repInIds==null || cause==null) return result;
		
		DBConn conn=null;
		Session session=null;
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			for(int i=0;i<repInIds.length;i++){
				ReportAgainSet reportAgainSetPersistence=new ReportAgainSet();
				reportAgainSetPersistence.setCause(cause);
				reportAgainSetPersistence.setRepInId(repInIds[i]);
				reportAgainSetPersistence.setSetDate(new Date());
								
				session.save(reportAgainSetPersistence);
			}
			session.flush();
			result=true;
			
		}catch(HibernateException he){
			result=false;
			log.printStackTrace(he);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
    
    public static boolean update(com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
    	//更新标志
        boolean result=true;
        //创建连接
        DBConn conn=null;
        Session session=null;
        //reportform中是否有参数
        if (reportInForm==null){
        	return false;
        }
        
        try{
        	conn=new DBConn();
        	session=conn.beginTransaction();

        	//循环给持久化对象传递form对象的机构id数组的参数
        	if(reportInForm.getRepInIdArray()!=null && !reportInForm.getRepInIdArray().equals("")){
        		Integer repOutIds[]=new Integer[reportInForm.getRepInIdArray().length];
        		for(int i=0;i<reportInForm.getRepInIdArray().length;i++){
        			ReportIn reportInPersistence=(ReportIn)session.load(ReportIn.class, reportInForm.getRepInIdArray()[i]);
        			
        			reportInPersistence.setCheckFlag(reportInForm.getCheckFlag());
        			if(reportInForm.getForseReportAgainFlag()!=null) 
        				reportInPersistence.setForseReportAgainFlag(reportInForm.getForseReportAgainFlag());
        				
        			session.update(reportInPersistence);
        		}
        		session.flush();
        		result = true;        		
        	}else{
        		result=false;
        	}
        	
        	//异常的捕捉机制
        }catch(HibernateException he){
        	result=false;
        	log.printStackTrace(he);
        }catch(Exception e){
        	result=false;
        	log.printStackTrace(e);
        }
        finally{
        	//如果conn对象依然存在,则结束当前事务并断开连接
        	if (conn!=null)	conn.endTransaction(result);
        }
        //更新成功返回吧!!!
        return result;
    }

    
}


    





