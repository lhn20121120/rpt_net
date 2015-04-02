
package com.cbrc.smis.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
/**
 * @author rds
 *
 * @file DAO.java
 * 
 * ����SessionFactory���󼰻�ȡSession����
 */
public class DBUtil {
   /**
    * ��������Դ��JNI����
    */
	private static final String JNDI_NAME="hibernate/session_factory";
	
   /**
    * SessionFactory����
    */
	private static SessionFactory sessionFactory = null;
	
   /**
    * ThreadLocal����
    */
	private static final ThreadLocal sessions = new ThreadLocal();
	
   /**
    * ��õ�ǰ���еĲ��Ѽ���ĻỰSession����,û�д�������� 
    * 
    * @return Session
    */
	public static Session currentSession(){
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
				sessions.set(session);
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
	public static void closeSession(){
	    Session session = (Session) sessions.get();
	    sessions.set(null);
	    if (session != null)
	    	try{
	    		session.close();
	    	}catch(HibernateException he){
	    		he.printStackTrace();
	    	}
	}
		
   /**
	* ��ȡϵͳ��SessionFactory����
	* 
	* @return boolean �ɹ�������true;���򣬷���false
	*/
	private static boolean getSystemSessionFactory() {
	  	try {
	  		//��JNDI��ȡ��SessionFactory��ʵ�������������false
	  		//Context ctx = new InitialContext();
	  		//sessionFactory =(SessionFactory)ctx.lookup(JNDI_NAME);
			sessionFactory=new Configuration().configure().buildSessionFactory();
		
		/*
	    } catch (NamingException e) {
			e.printStackTrace();
			return false;
		*/
		}catch(HibernateException he){
			he.printStackTrace();
			return false;
	    }
	    
	    return true;
	}	
}
