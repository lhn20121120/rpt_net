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
		//连接对象和会话对象初始化	   
		DBConn conn=null;	   
		Session session=null;
		try{    	 
			//初始化    	  
			String hql ="from SysParameter mcu where mcu.parType= 'DetailCol'";		 	  
			//conn对象的实例化    	  
			conn=new DBConn();    	  
			//打开连接开始会话    	  
			session=conn.openSession();
    	  			
			Query query=session.createQuery(hql.toString());		  	  
			list = query.list();
		}catch(HibernateException he){    	
			he.printStackTrace();
		}catch(Exception e){    
			e.printStackTrace();      
		}finally{    	
			//如果连接存在，则断开，结束会话，返回    	  
			if(conn!=null) conn.closeSession();      
		}
		return list;   
	}
}
