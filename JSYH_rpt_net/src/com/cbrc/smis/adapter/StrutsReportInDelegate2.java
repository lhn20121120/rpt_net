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
        //��������
        DBConn conn=null;
        Session session=null;
        //reportform���Ƿ��в���
        if (reportInForm==null){
        	return false;
        }
        
        try{
        	conn=new DBConn();
        	session=conn.beginTransaction();

        	//ѭ�����־û����󴫵�form����Ļ���id����Ĳ���
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
        	
        	//�쳣�Ĳ�׽����
        }catch(HibernateException he){
        	result=false;
        	log.printStackTrace(he);
        }catch(Exception e){
        	result=false;
        	log.printStackTrace(e);
        }
        finally{
        	//���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
        	if (conn!=null)	conn.closeSession();
        }
        //���³ɹ����ذ�!!!
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
    	//���±�־
        boolean result=true;
        //��������
        DBConn conn=null;
        Session session=null;
        //reportform���Ƿ��в���
        if (reportInForm==null){
        	return false;
        }
        
        try{
        	conn=new DBConn();
        	session=conn.beginTransaction();

        	//ѭ�����־û����󴫵�form����Ļ���id����Ĳ���
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
        	
        	//�쳣�Ĳ�׽����
        }catch(HibernateException he){
        	result=false;
        	log.printStackTrace(he);
        }catch(Exception e){
        	result=false;
        	log.printStackTrace(e);
        }
        finally{
        	//���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
        	if (conn!=null)	conn.endTransaction(result);
        }
        //���³ɹ����ذ�!!!
        return result;
    }

    
}


    





