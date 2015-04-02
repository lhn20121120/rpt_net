
package com.cbrc.org.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
/**
 * @author rds
 * 
 * 会话连接管理类
 */
public class DBConn extends DBBase{
   /**
    * 连接会话Session
    */ 
	private Session session=null;
	
   /**
	* 事务处理Transaction
	*/
	private Transaction transaction=null;

   /**
	* 获取带事务管理的会话Session
	*
	* @return Session
	*/
	public Session beginTransaction(){
		session = currentSession();
		try{
			transaction = session.beginTransaction();
		}catch(HibernateException he){
			he.printStackTrace();
		}
		return session;
	}

   /**
	* 失放会话，提交事务
	* 
	* @param commit boolean 当为true,commit;否则，rollback
	* @return void
	*/
	public void endTransaction(boolean commit){
		try{
			if (commit) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
		}catch(HibernateException he){
			he.printStackTrace();
		}
		closeSession();
	}
	
   /**
	* 获取不带事务的会话Session
	* 
	* @return Session
	*/
	public Session openSession(){
		return currentSession();	
	}
	
   /**
	* 关闭会话
	* 
	* @return void
	*/
	public void closeSession(){
		super.closeSession();	
		this.session=null;
	}
}
