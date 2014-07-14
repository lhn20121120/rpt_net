
package com.cbrc.org.dao;

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
 * ����SessionFactory���󼰻�ȡSession����
 */
public class DBBase {
	FitechException smmisException=new FitechException(DBBase.class,true);
   /**
    * ��������Դ��JNI����
    */
	private static final String JNDI_NAME="hibernate/session_factory";
	
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
					throw new HibernateException("ͨ��JNDI��ȡ����Դʧ��! ");
				}
			}
			//smmisException.println("��ʼ��ȡsession......");
			session = (Session) sessions.get();
			if (session == null) {
				session = sessionFactory.openSession();
				//smmisException.println("����һ��session......");
				sessions.set(session);
				isActive=false;
			}
			//smmisException.println("��ȡ���session......");
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
		//smmisException.println("����closeSession����......");
		if(isActive==true) return;
		
		//smmisException.println("closeSession......");
	    Session session = (Session) sessions.get();
	    sessions.set(null);
	    if (session != null)
	    	try{
	    		session.close();
	    	}catch(HibernateException he){
	    		he.printStackTrace();
	    	}
	    //smmisException.println("�˳�closeSession����......");
	}
		
   /**
	* ��ȡϵͳ��SessionFactory����
	* 
	* @return boolean �ɹ�������true;���򣬷���false
	*/
	private boolean getSystemSessionFactory() {
	  	try {
	  		//��JNDI��ȡ��SessionFactory��ʵ�������������false
	  		//Context ctx = new InitialContext();
	  		//sessionFactory =(SessionFactory)ctx.lookup(JNDI_NAME);
	  		
	  		String fileName=Config.WEBROOTPATH + 
				"WEB-INF" + 
				Config.FILESEPARATOR + "classes" + 
				Config.FILESEPARATOR + "hibernate_org.cfg.xml";
	  		File file=new File(fileName);
	  		if(!file.exists()){
	  			new FitechException("�ļ�:" + fileName + "������!",true);
	  			return false;
	  		}
	  		sessionFactory=new Configuration().configure(file).buildSessionFactory();
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
	
	/**
	 * ��õ�ǰ�ĻỰ״̬
	 * 
	 * @return boolean 
	 */
	public boolean getSessionActive(){
		return isActive;
	}
}
