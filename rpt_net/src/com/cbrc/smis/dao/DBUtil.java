
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
 * 建立SessionFactory对象及获取Session对象
 */
public class DBUtil {
   /**
    * 连接数据源的JNI名称
    */
	private static final String JNDI_NAME="hibernate/session_factory";
	
   /**
    * SessionFactory对象
    */
	private static SessionFactory sessionFactory = null;
	
   /**
    * ThreadLocal对象
    */
	private static final ThreadLocal sessions = new ThreadLocal();
	
   /**
    * 获得当前空闲的并已激活的会话Session对象,没有打开事务管理 
    * 
    * @return Session
    */
	public static Session currentSession(){
		Session session=null;
		try{
			if (sessionFactory == null) {
				// 如果sessionFactory实例为null则从JNDI中获取
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
	* 关闭当前的会话Session
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
	* 获取系统的SessionFactory对象
	* 
	* @return boolean 成功，返回true;否则，返回false
	*/
	private static boolean getSystemSessionFactory() {
	  	try {
	  		//从JNDI中取得SessionFactory的实例，如果出错返回false
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
