
package com.cbrc.smis.dao;

import java.sql.SQLException;
import java.util.List;

import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.hibernate.MRepRangePK;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
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
	public Session beginTransaction() {
		session = currentSession();
		try {
			transaction = session.beginTransaction();
		} catch (HibernateException he) {
			he.printStackTrace();
		}
		return session;
	}

	/**
	 * 
	 * 方法说明:该方法用于失放会话，提交事务
	 * 
	 * @date 2007-3-7
	 * @param commit
	 *            boolean 当为true,commit;否则，rollback
	 * @return type:boolean true:提交成功 false:提交失败并回滚
	 */
	public boolean endTransaction(boolean commit) {
		boolean result;
		try {
			if (commit) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
			result = true;
		} catch (HibernateException he) {
			he.printStackTrace();
			result = false;
		}
		closeSession();
		return result;
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
