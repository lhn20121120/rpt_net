
package com.cbrc.smis.dao;

import java.io.File;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
/**
 * @author rds
 *
 * @file DAO.java
 * 
 * 建立SessionFactory对象及获取Session对象
 */
public class DBBase {
	FitechException smmisException=new FitechException(DBBase.class,true);
   /**
    * SessionFactory对象
    */
	public static SessionFactory sessionFactory = null;
   /**
    * ThreadLocal对象
    */
	private final ThreadLocal sessions = new ThreadLocal();
	
   /**
    * sessionActive
    */
	private boolean isActive = true;
	
   /**
    * 获得当前空闲的并已激活的会话Session对象,没有打开事务管理 
    * 
    * @return Session
    */
	protected Session currentSession(){
		Session session=null;
		try{
			if (sessionFactory == null) {
				// 如果sessionFactory实例为null则从JNDI中获取
				if (getSystemSessionFactory() == false) {
					throw new HibernateException("通过JNDI获取数据源失败! ");
				}
			}
			session = (Session) sessions.get();
			if (session == null) {
				session = sessionFactory.openSession();
				sessions.set(session);
				isActive=false;
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
	* 获取系统的SessionFactory对象
	* 
	* @return boolean 成功，返回true;否则，返回false
	*/
	private boolean getSystemSessionFactory() {
	  	try {
	  		//从JNDI中取得SessionFactory的实例，如果出错返回false
	  		String fileName=
//	  			Config.WEBROOTPATH + 
//				"WEB-INF" + 
//				Config.FILESEPARATOR + "classes" + 
//				Config.FILESEPARATOR + 
				"/hibernate_smis_inner.cfg.xml";
//	  		File file=new File(fileName);
//	  		if(!file.exists()){
//	  			new FitechException("文件:" + fileName + "不存在!",true);
//	  			return false;
//	  		}
	  		sessionFactory=new Configuration().configure(fileName).buildSessionFactory();
		}catch(HibernateException he){
			he.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	/**
	 * 获得当前的会话状态
	 * 
	 * @return boolean 
	 */
	public boolean getSessionActive(){
		return isActive;
	}
}
