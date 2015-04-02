package com.fitech.institution.adapter;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.SysParameter;

public class StrutsSysSetDelegate {

	public static List loadSysCol(){
		List <SysParameter> list = null;
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn=null;	   
		Session session=null;
		try{    	 
			//��ʼ��    	  
			String hql ="from SysParameter mcu where mcu.parType= 'DetailCol'";		 	  
			//conn�����ʵ����    	  
			conn=new DBConn();    	  
			//�����ӿ�ʼ�Ự    	  
			session=conn.openSession();
    	  			
			Query query=session.createQuery(hql.toString());		  	  
			list = query.list();
		}catch(HibernateException he){    	
			he.printStackTrace();
		}catch(Exception e){    
			e.printStackTrace();      
		}finally{    	
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn!=null) conn.closeSession();      
		}
		return list;   
	}
}
