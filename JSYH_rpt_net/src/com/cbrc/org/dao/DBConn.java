
package com.cbrc.org.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
/**
 * @author rds
 * 
 * �Ự���ӹ�����
 */
public class DBConn extends DBBase{
   /**
    * ���ӻỰSession
    */ 
	private Session session=null;
	
   /**
	* ������Transaction
	*/
	private Transaction transaction=null;

   /**
	* ��ȡ���������ĻỰSession
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
	* ʧ�ŻỰ���ύ����
	* 
	* @param commit boolean ��Ϊtrue,commit;����rollback
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
	* ��ȡ��������ĻỰSession
	* 
	* @return Session
	*/
	public Session openSession(){
		return currentSession();	
	}
	
   /**
	* �رջỰ
	* 
	* @return void
	*/
	public void closeSession(){
		super.closeSession();	
		this.session=null;
	}
}
