package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.util.FitechException;

/**
 * @StrutsMRepFreqDelegate �ϱ�Ƶ�ȱ�Delegate
 * 
 * @author ����
 */
public class StrutsMRepFreqDelegate {
	// Catch��������׳��������쳣
	private static FitechException log = new FitechException(
			StrutsMRepFreqDelegate.class);

	/**
	 * �ϱ�Ƶ������
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm
	 * @return boolean result,�����ɹ�����true,���򷵻�false
	 * @exception Exception����׽�쳣����
	 */
	public static boolean create(MRepFreqForm mRepFreqForm) throws Exception {

		boolean result = false; // ��result���
		MRepFreq mRepFreqPersistence = new MRepFreq();

		if (mRepFreqForm == null || mRepFreqForm.getRepFreqName().equals("")) {
			return result;
		}
		// ���Ӷ���ĳ�ʼ��
		DBConn conn = null;
		// �Ự����ĳ�ʼ��
		Session session = null;

		try {
			// ��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mRepFreqForm��ʾ�����)
			if (mRepFreqForm.getRepFreqName() == null
					|| mRepFreqForm.getRepFreqName().equals("")) {
				return result;
			}

			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			// ʵ�������Ӷ���
			conn = new DBConn();
			// �Ự����Ϊ���Ӷ������������
			session = conn.beginTransaction();
			// mRepFreqForm�����ʵ����

			// �Ự���󱣴�־ò����
			session.save(mRepFreqPersistence);
			session.flush();
			TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
					mRepFreqForm);
			// ��־Ϊtrue
			result = true;
		} catch (HibernateException e) {
			// �־ò���쳣����׽
			log.printStackTrace(e);
		} finally {
			// �������״̬��,��Ͽ�,��������,����
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;

	}

	/**
	 * ȡ�ð�������ѯ���ļ�¼����
	 * 
	 * @return int ��ѯ���ļ�¼����
	 * @param mRepFreqForm
	 *            ������ѯ��������Ϣ���ϱ�Ƶ��ID���ϱ�Ƶ�����ƣ�
	 */

	public static int getRecordCount(MRepFreqForm mRepFreqForm) {
		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		StringBuffer hql = new StringBuffer(
				"select count(*) from MRepFreq mdrt");
		StringBuffer where = new StringBuffer("");

		if (mRepFreqForm != null) {
			// �����������ж�,�������Ʋ���Ϊ��
			if (mRepFreqForm.getRepFreqName() != null
					&& !mRepFreqForm.getRepFreqName().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "mdrt.repFreqName like '%"
						+ mRepFreqForm.getRepFreqName() + "%'");
		}

		try { // List���ϵĲ���
			// ��ʼ��
			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() == 1) {
				count = ((Integer) list.get(0)).intValue();
			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * �ϱ�Ƶ�ȵĲ�ѯ
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm ��ѯ������
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static List select(MRepFreqForm mRepFreqForm, int offset, int limit) {

		// List���ϵĶ���
		List refVals = null;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		StringBuffer hql = new StringBuffer("from MRepFreq mrf");
		StringBuffer where = new StringBuffer("");

		if (mRepFreqForm != null) {
			// �����������ж�,�������Ʋ���Ϊ��
			if (mRepFreqForm.getRepFreqName() != null
					&& !mRepFreqForm.getRepFreqName().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "mrf.repFreqName like '%"
						+ mRepFreqForm.getRepFreqName() + "%'");
		}

		try { // List���ϵĲ���
			// ��ʼ��

			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			// ��Ӽ�����Session
			// List list=session.find(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				refVals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * ����MRepFreqForm����
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm ������ݵĶ���
	 * @exception Exception
	 *                ���MRepFreqForm����ʧ��,��׽�׳��쳣
	 */
	public static boolean update(MRepFreqForm mRepFreqForm) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRepFreq mRepFreqPersistence = new MRepFreq();

		if (mRepFreqForm == null) {
			return result;
		}
		try {
			if (mRepFreqForm.getRepFreqName() == null
					|| mRepFreqForm.getRepFreqName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			session.update(mRepFreqPersistence);

			result = true;
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * �༭����
	 * 
	 * @param mRepFreqForm
	 *            The MRepFreqForm �������ݵĴ���
	 * @exception Exception
	 *                ��� MRepFreqForm ����ʧ���׳��쳣.
	 */
	public static boolean edit(MRepFreqForm mRepFreqForm) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRepFreq mRepFreqPersistence = new MRepFreq();
		mRepFreqForm = new MRepFreqForm();
		mRepFreqForm.getRepFreqName();

		if (mRepFreqForm == null) {
			return result;
		}

		try {
			if (mRepFreqForm.getRepFreqName() == null
					&& mRepFreqForm.getRepFreqName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			mRepFreqPersistence = (MRepFreq) session.load(MRepFreq.class,
					mRepFreqForm.getRepFreqName());

			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ɾ������
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm ��ѯ���Ķ���
	 * @return boolean ���ɾ���ɹ��򷵻�true,����false
	 */
	public static boolean remove(MRepFreqForm mRepFreqForm) throws Exception {
		// �ñ�־result
		boolean result = false;
		// ���ӺͻỰ����ĳ�ʼ��
		DBConn conn = null;
		Session session = null;

		// mRepFreq�Ƿ�Ϊ��,����result
		if (mRepFreqForm == null && mRepFreqForm.getRepFreqId() == null)
			return result;

		try {
			// ���Ӷ���ͻỰ�����ʼ��
			conn = new DBConn();
			session = conn.beginTransaction();
			// ��mRepFreqForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
			MRepFreq mRepFreq = (MRepFreq) session.load(MRepFreq.class,
					mRepFreqForm.getRepFreqId());
			TranslatorUtil.copyPersistenceToVo(mRepFreq, mRepFreqForm);
			// �Ự����ɾ���־ò����
			session.delete(mRepFreq);
			session.flush();

			// ɾ���ɹ�����Ϊtrue
			result = true;
		} catch (HibernateException he) {
			// ��׽������쳣,�׳�
			log.printStackTrace(he);
		} finally {
			// �����������Ͽ����ӣ������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ��ѯһ����¼,���ص�EditAction��
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqFormʵ��������
	 * @return MRepFreqForm ����һ����¼
	 */

	public static MRepFreqForm selectOne(MRepFreqForm mRepFreqForm) {

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		if (mRepFreqForm != null)
			try {
				if (mRepFreqForm.getRepFreqId() != null
						&& !mRepFreqForm.getRepFreqId().equals(""))
					// conn�����ʵ����
					conn = new DBConn();
				// �����ӿ�ʼ�Ự
				session = conn.openSession();

				MRepFreq mRepFreqPersistence = (MRepFreq) session.load(
						MRepFreq.class, mRepFreqForm.getRepFreqId());

				TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
						mRepFreqForm);

			} catch (HibernateException he) {
				log.printStackTrace(he);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				// ������Ӵ��ڣ���Ͽ��������Ự������
				if (conn != null)
					conn.closeSession();
			}
		return mRepFreqForm;
	}

	/**
	 * ��ѯһ����¼,���ص�EditAction��
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqFormʵ��������
	 * @return MRepFreqForm ����һ����¼
	 */

	public static MRepFreq selectOne(Integer repFreqId) {

		MRepFreq mRepFreqPersistence = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		if (repFreqId == null || repFreqId.equals(""))
			return mRepFreqPersistence;

		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();

			mRepFreqPersistence = (MRepFreq) session.load(
					MRepFreq.class, repFreqId);

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return mRepFreqPersistence;
	}

	/**
	 * ��ѯ���м�¼
	 * 
	 * @return List ��ѯ���ļ�¼����
	 */
	public static List findAll() throws Exception {

		// List���ϵĶ���
		List refVals = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		// ��ѯ����HQL������
		String rep_freq = "('��','��','����','��')";
		// StringBuffer hql = new StringBuffer("from MRepFreq a where
		// a.repFreqName in ").append(rep_freq);
		StringBuffer hql = new StringBuffer("from MRepFreq a ");

		try {

			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			// ��Ӽ�����Session
			List list = session.find(hql.toString());

			if (list != null) {
				refVals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * ��ѯ���м�¼
	 * 
	 * @return List ��ѯ���ļ�¼����
	 */
	public static List findAllFeq() throws Exception {

		// List���ϵĶ���
		List refVals = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		// ��ѯ����HQL������
		String rep_freq = "('��','Ѯ','��','��','����','��')";
		StringBuffer hql = new StringBuffer(
				"from MRepFreq a where a.repFreqName in ").append(rep_freq);

		try {

			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			// ��Ӽ�����Session
			List list = session.find(hql.toString());

			if (list != null) {
				refVals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * ��ѯ���м�¼
	 * 
	 * @return List ��ѯ���ļ�¼����
	 */
	public static List findRHAllFeq() throws Exception {

		// List���ϵĶ���
		List refVals = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		// ��ѯ����HQL������
		String rep_freq = "('��','Ѯ','��','��','����','��','�����ת','��(����)','Ѯ(����)','��(����)')";
		StringBuffer hql = new StringBuffer(
				"from MRepFreq a where a.repFreqName in ").append(rep_freq);

		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			// ��Ӽ�����Session
			List list = session.find(hql.toString());

			if (list != null) {
				refVals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

}
