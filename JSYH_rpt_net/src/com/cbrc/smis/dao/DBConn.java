
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
	 * ����˵��:�÷�������ʧ�ŻỰ���ύ����
	 * 
	 * @date 2007-3-7
	 * @param commit
	 *            boolean ��Ϊtrue,commit;����rollback
	 * @return type:boolean true:�ύ�ɹ� false:�ύʧ�ܲ��ع�
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
