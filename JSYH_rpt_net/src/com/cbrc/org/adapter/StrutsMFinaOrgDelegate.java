package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.dao.DBConn;
import com.cbrc.org.form.DeputationForm;
import com.cbrc.org.hibernate.MFinaOrg;
import com.cbrc.org.hibernate.MOrgCode;
import com.cbrc.org.util.SynRelation;

/**
 * This is a delegate class to handle interaction with the backend persistence
 * layer of hibernate. It has a set of methods to handle persistence for
 * MFinaOrg data (i.e. com.cbrc.org.form.MFinaOrgForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMFinaOrgDelegate {

	/**
	 * Create a new com.cbrc.org.form.MFinaOrgForm object and persist (i.e.
	 * insert) it.
	 * 
	 * @param mFinaOrgForm
	 *            The object containing the data for the new
	 *            com.cbrc.org.form.MFinaOrgForm object
	 * @exception Exception
	 *                If the new com.cbrc.org.form.MFinaOrgForm object cannot be
	 *                created or persisted.
	 */
	public static com.cbrc.org.form.MFinaOrgForm create(
			com.cbrc.org.form.MFinaOrgForm mFinaOrgForm) throws Exception {
		com.cbrc.org.hibernate.MFinaOrg mFinaOrgPersistence = new com.cbrc.org.hibernate.MFinaOrg();
		TranslatorUtil.copyVoToPersistence(mFinaOrgPersistence, mFinaOrgForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO error?: mFinaOrgPersistence =
		// (com.cbrc.org.hibernate.MFinaOrg)session.save(mFinaOrgPersistence);
		session.save(mFinaOrgPersistence);
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(mFinaOrgPersistence, mFinaOrgForm);
		return mFinaOrgForm;
	}

	/**
	 * Update (i.e. persist) an existing com.cbrc.org.form.MFinaOrgForm object.
	 * 
	 * @param mFinaOrgForm
	 *            The com.cbrc.org.form.MFinaOrgForm object containing the data
	 *            to be updated
	 * @exception Exception
	 *                If the com.cbrc.org.form.MFinaOrgForm object cannot be
	 *                updated/persisted.
	 */
	public static com.cbrc.org.form.MFinaOrgForm update(
			com.cbrc.org.form.MFinaOrgForm mFinaOrgForm) throws Exception {
		com.cbrc.org.hibernate.MFinaOrg mFinaOrgPersistence = new com.cbrc.org.hibernate.MFinaOrg();
		TranslatorUtil.copyVoToPersistence(mFinaOrgPersistence, mFinaOrgForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		session.update(mFinaOrgPersistence);
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(mFinaOrgPersistence, mFinaOrgForm);
		return mFinaOrgForm;
	}

	/**
	 * Retrieve an existing com.cbrc.org.form.MFinaOrgForm object for editing.
	 * 
	 * @param mFinaOrgForm
	 *            The com.cbrc.org.form.MFinaOrgForm object containing the data
	 *            used to retrieve the object (i.e. the primary key info).
	 * @exception Exception
	 *                If the com.cbrc.org.form.MFinaOrgForm object cannot be
	 *                retrieved.
	 */
	public static com.cbrc.org.form.MFinaOrgForm edit(
			com.cbrc.org.form.MFinaOrgForm mFinaOrgForm) throws Exception {
		com.cbrc.org.hibernate.MFinaOrg mFinaOrgPersistence = new com.cbrc.org.hibernate.MFinaOrg();
		TranslatorUtil.copyVoToPersistence(mFinaOrgPersistence, mFinaOrgForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		mFinaOrgPersistence = (com.cbrc.org.hibernate.MFinaOrg) session.load(
				com.cbrc.org.hibernate.MFinaOrg.class, mFinaOrgPersistence
						.getFinaOrgCode());
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(mFinaOrgPersistence, mFinaOrgForm);
		return mFinaOrgForm;
	}

	/**
	 * Remove (delete) an existing com.cbrc.org.form.MFinaOrgForm object.
	 * 
	 * @param mFinaOrgForm
	 *            The com.cbrc.org.form.MFinaOrgForm object containing the data
	 *            to be deleted.
	 * @exception Exception
	 *                If the com.cbrc.org.form.MFinaOrgForm object cannot be
	 *                removed.
	 */
	public static void remove(com.cbrc.org.form.MFinaOrgForm mFinaOrgForm)
			throws Exception {
		com.cbrc.org.hibernate.MFinaOrg mFinaOrgPersistence = new com.cbrc.org.hibernate.MFinaOrg();
		TranslatorUtil.copyVoToPersistence(mFinaOrgPersistence, mFinaOrgForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO: is this really needed?
		mFinaOrgPersistence = (com.cbrc.org.hibernate.MFinaOrg) session.load(
				com.cbrc.org.hibernate.MFinaOrg.class, mFinaOrgPersistence
						.getFinaOrgCode());
		session.delete(mFinaOrgPersistence);
		tx.commit();
		session.close();
	}

	/**
	 * Retrieve all existing com.cbrc.org.form.MFinaOrgForm objects.
	 * 
	 * @exception Exception
	 *                If the com.cbrc.org.form.MFinaOrgForm objects cannot be
	 *                retrieved.
	 */
	public static List findAll() throws Exception {
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals.addAll(session.find("from com.cbrc.org.hibernate.MFinaOrg"));
		tx.commit();
		session.close();
		ArrayList mFinaOrg_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.org.form.MFinaOrgForm mFinaOrgFormTemp = new com.cbrc.org.form.MFinaOrgForm();
			com.cbrc.org.hibernate.MFinaOrg mFinaOrgPersistence = (com.cbrc.org.hibernate.MFinaOrg) it
					.next();
			TranslatorUtil.copyPersistenceToVo(mFinaOrgPersistence,
					mFinaOrgFormTemp);
			mFinaOrg_vos.add(mFinaOrgFormTemp);
		}
		retVals = mFinaOrg_vos;
		return retVals;
	}

	/**
	 * Retrieve a set of existing com.cbrc.org.form.MFinaOrgForm objects for
	 * editing.
	 * 
	 * @param mFinaOrgForm
	 *            The com.cbrc.org.form.MFinaOrgForm object containing the data
	 *            used to retrieve the objects (i.e. the criteria for the
	 *            retrieval).
	 * @exception Exception
	 *                If the com.cbrc.org.form.MFinaOrgForm objects cannot be
	 *                retrieved.
	 */
	public static List select(com.cbrc.org.form.MFinaOrgForm mFinaOrgForm)
			throws Exception {
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.finaOrgNm='"
								+ mFinaOrgForm.getFinaOrgNm() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.licence='"
								+ mFinaOrgForm.getLicence() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.address='"
								+ mFinaOrgForm.getAddress() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.orgId='"
								+ mFinaOrgForm.getOrgId() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.corpAddress='"
								+ mFinaOrgForm.getCorpAddress() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.openDate='"
								+ mFinaOrgForm.getOpenDate() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.bankStyle='"
								+ mFinaOrgForm.getBankStyle() + "'"));
		retVals
				.addAll(session
						.find("from com.cbrc.org.hibernate.MFinaOrg as obj1 where obj1.corpName='"
								+ mFinaOrgForm.getCorpName() + "'"));
		tx.commit();
		session.close();
		ArrayList mFinaOrg_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.org.form.MFinaOrgForm mFinaOrgFormTemp = new com.cbrc.org.form.MFinaOrgForm();
			com.cbrc.org.hibernate.MFinaOrg mFinaOrgPersistence = (com.cbrc.org.hibernate.MFinaOrg) it
					.next();
			TranslatorUtil.copyPersistenceToVo(mFinaOrgPersistence,
					mFinaOrgFormTemp);
			mFinaOrg_vos.add(mFinaOrgFormTemp);
		}
		retVals = mFinaOrg_vos;
		return retVals;
	}

	/**
	 * �ӽ��ڻ������еõ�һҳ���ڻ�����ע�뵽DeputtationForm��
	 * 
	 * @param df
	 *            DeputationForm����
	 * @param begin
	 *            һҳ����ʼ���
	 * @param count
	 *            һҳ������
	 * @throws Exception
	 */
	public static void setMFinaOrg(DeputationForm df, int begin,
			int count) throws Exception {

		DBConn dBConn = null;

		Session session = null;

		List l = null;

		String hsql = null;

		MOrgCode moc = null;

		Query query = null;

		String regionId = null;

		String orgId = df.getMToRepOrg().getOrgId();

		try {

			dBConn = new DBConn();

			session = dBConn.openSession();
			
			hsql = "from MOrgCode moc where moc.orgId=:orgId";

			query = session.createQuery(hsql);

			query.setString("orgId", orgId);

			l = query.list();

			if (l.size() != 0) {

				moc = (MOrgCode) l.get(0);

				regionId = moc.getRegionId();

			} else {

				return;
			}

			hsql = "from MFinaOrg mfo where mfo.regionId=:regionId";

			query = session.createQuery(hsql);

			query.setString("regionId", regionId);

			query.setFirstResult(begin);

			query.setMaxResults(count);

			l = query.list();

			if (l.size() == 0)

				l = null;

			df.setMFinaOrgs(l);

		} catch (Exception e) {

			df.setMFinaOrgs(null);

		} finally {

			dBConn.closeSession();
		}
	}
	
	/**
	 * �÷����������ô�����ϵ
	 * 
	 * @param df
	 * @return
	 * @throws Exception
	 */
	public static boolean setRelationToMToRepOrg(DeputationForm df) throws Exception {
		
		DBConn dBConn = null;
		
		Session session = null;

		List mfos = df.getMFinaOrgs();

		boolean isSucess = true;

		int size = mfos.size();

		MFinaOrg mfo = null;

		try {
			
			dBConn = new DBConn();
			
			session = dBConn.beginTransaction();

			for (int i = 0; i < size; i++) {

				mfo = (MFinaOrg) mfos.get(i);
				
				session.saveOrUpdateCopy(mfo);
				
				try{

				SynRelation.conduct(mfo.getOrgId(),mfo.getFinaOrgNm(),new Integer(2));  //ͬ�������Ĵ�����ϵ
				
				}catch(Exception e){
					
					/*FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, // ���ɹ���¼��־
							ConfigOncb.HANDLER, "�����ô�����ϵ��������ͬ������ʧ��", "OVER");*/
				}
				
				
			}

		} catch (Exception e) {

			isSucess = false;

		}
		
		finally{
			
			dBConn.endTransaction(true);
		}

		return isSucess;

	}


	/**
	 * ͨ������Ĵ��������� �������ݿ��з�ҳ�õ������ɸô�������������һ�����ڼ�¼
	 * ��������Ѿ������ó��ɸô������������Ľ��ڻ�����û�����ó��ɸô������������Ľ��ڻ���
	 * 
	 * @param session
	 * @param regionId
	 * @param begin
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public static List getMFinaOrgs(Session session, String regionId,
			int begin, int count) throws Exception {

		String hsql = "from MFinaOrg mfo where mfo.regionId=:regionId";

		Query query = session.createQuery(hsql);

		query.setString("regionId", regionId);

		query.setFirstResult(begin);

		query.setMaxResults(count);

		List l = query.list();

		if (l.size() == 0)

			l = null;

		return l;
	}

	
	/**
	 * @author cb
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getMFinaOrgCount() throws Exception {

		DBConn dBConn = null;

		Session session = null;

		Query query = null;

		int count = 0;

		String hsql = null;

		List l = null;

		try {

			dBConn = new DBConn();

			session = dBConn.openSession();

			hsql = "select count(*) from MFinaOrg";

			query = session.createQuery(hsql);

			l = query.list();

			count = ((Integer) l.get(0)).intValue();

		} catch (Exception e) {

			count = 0;

		} finally {

			dBConn.closeSession();
		}

		return count;
	}
}