/*
 * Created on 2005-5-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.dao;

import java.io.File;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import com.gather.common.Config;
import com.gather.common.SMMISException;
/**
 * @author rds
 *
 * @file DAO.java
 * 
 * ����SessionFactory���󼰻�ȡSession����
 */
public class DBBase {
	SMMISException smmisException=new SMMISException(DBBase.class,true);
   /**
    * SessionFactory����
    */
	public static SessionFactory sessionFactory = null;
	
   /**
    * ThreadLocal����
    */
	private final ThreadLocal sessions = new ThreadLocal();
	
   /**
    * sessionActive
    */
	private boolean isActive = true;
	
   /**
    * ��õ�ǰ���еĲ��Ѽ���ĻỰSession����,û�д�������� 
    * 
    * @return Session
    */
	protected Session currentSession(){
		Session session=null;
		try{
			if (sessionFactory == null) {
				// ���sessionFactoryʵ��Ϊnull���JNDI�л�ȡ
				if (getSystemSessionFactory() == false) {
					throw new HibernateException("Exception geting SessionFactory from JNDI ");
				}
			}
			session = (Session) sessions.get();
			if (session == null) {
				session = sessionFactory.openSession();
				//smmisException.println("����һ��session......");
				sessions.set(session);
				isActive=false;
			}
		}catch(HibernateException he){
			he.printStackTrace();
		}
	    return session;
	}

   /**
	* �رյ�ǰ�ĻỰSession
	* 
	* @return void
	*/
	protected  void closeSession(){
		if(isActive==true) return;
		
	    Session session = (Session) sessions.get();
	    sessions.set(null);
	    try{
    		if (session != null) session.close();
    	}catch(HibernateException he){
    		he.printStackTrace();
    	}
	    	
	}
		
   /**
	* ��ȡϵͳ��SessionFactory����
	* 
	* @return boolean �ɹ�������true;���򣬷���false
	*/
	private boolean getSystemSessionFactory() {
	  	try {
	  		//��JNDI��ȡ��SessionFactory��ʵ�������������false
	  		String fileName = com.cbrc.smis.common.Config.WEBROOTPATH +
			  "WEB-INF" +
			  Config.FILESEPARATOR + "classes" + 
			  Config.FILESEPARATOR + "hibernate.cfg.xml";
	  		
	  		File file=new File(fileName);
	  		if(!file.exists()){
	  			new SMMISException("�ļ�:" + fileName + "������!",true);
	  			return false;
	  		}
	  		sessionFactory = new Configuration().configure().buildSessionFactory();
		}catch(HibernateException he){
			he.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	/**
	 * ��õ�ǰ�ĻỰ״̬
	 * 
	 * @return boolean 
	 */
	public boolean getSessionActive(){
		return isActive;
	}
}
