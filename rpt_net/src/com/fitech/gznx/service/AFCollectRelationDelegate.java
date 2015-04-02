package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.CollectRelationForm;
import com.fitech.gznx.po.AfCollectRelation;
import com.fitech.gznx.po.AfCollectRelationId;
import com.fitech.gznx.po.AfOrg;


public class AFCollectRelationDelegate {

	private static FitechException log = new FitechException(
			AFCollectRelationDelegate.class);

	/**
	 * ������ܹ�ϵ
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            ����Ҫ����Ļ�����Ϣ
	 * @return boolean �Ƿ����ɹ�
	 */
	public static boolean add(CollectRelationForm crForm) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;

		if (crForm == null) {
			return false;
		}
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			AfCollectRelation cr = null;
			AfCollectRelationId id = null;

			// System.out.println("orgId-->" + crForm.getOrgId());
			// ����Ļ���ID�����ж������ID���Ӷ���
			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			for (int i = 0; i < arrOrgId.length; i++) {
				id = new AfCollectRelationId();
				id.setCollectId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]);

				cr = new AfCollectRelation();
				cr.setId(id);
				// ������ܹ�ϵ
				session.save(cr);
			}
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ɾ�����ܹ�ϵ
	 * 
	 * @author Nick
	 * @param orgId
	 *            String ɾ�����ܹ�ϵ�Ļ���ID
	 * @return boolean �Ƿ�ɾ���ɹ�
	 */
	public static boolean delete(String orgId) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.length() == 0) {
			return false;
		}
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			String hql = "from AfCollectRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, orgId);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfCollectRelation cr = (AfCollectRelation) iter.next();
				session.delete(cr);
			}
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
/**
 * ��ѯ���м�¼�������� orgid����List����
 * @return list
 */
	public static List getAllOrgId() {
		boolean result = true;
		DBConn conn = null;
		Session session = null;
		List lst = new ArrayList();
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			// ����Ļ���IDΪ���ܹ�ϵ�Ļ���ID
			String hql = "from AfCollectRelation ";
			Query query = session.createQuery(hql);
			Iterator iter = query.iterate();
			while (iter.hasNext()) {
				AfCollectRelation cr = (AfCollectRelation) iter.next();
				lst.add(cr.getId().getOrgId());
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return lst;
	}
	
	
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * ���Ӵ���Ļ���ID�õ��û��������л��ܹ�ϵ���б�
	 * 
	 * @author Nick
	 * @param orgId
	 *            String ����ID
	 * @return List ���������Ļ��ܹ�ϵ���б�
	 */
	public static List getCRList(String orgId) {
		boolean result = true;
		DBConn conn = null;
		Session session = null;
		List lst = new ArrayList();

		if (orgId == null || orgId.length() == 0) {
			return null;
		}
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			// ����Ļ���IDΪ���ܹ�ϵ�Ļ���ID
			String hql = "from AfCollectRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, orgId);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfCollectRelation cr = (AfCollectRelation) iter.next();
				lst.add(cr.getId().getOrgId());
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return lst;
	}

	/**
	 * ������ܹ�ϵ
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            ����Ҫ�޸ĵĻ��ܹ�ϵ�Ļ�����Ϣ
	 * @return boolean �Ƿ��޸ĳɹ�
	 */
	public static boolean update(CollectRelationForm crForm) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			/** 1ɾ��ԭ�еĻ��ܹ�ϵ */
			String hql = "from AfCollectRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, crForm.getCollectId());
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfCollectRelation cr = (AfCollectRelation) iter.next();
				session.delete(cr);
			}
			/** 2�����µĻ��ܹ�ϵ */
			AfCollectRelation cr = null;
			AfCollectRelationId id = null;

			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			String topOrgId = AFOrgDelegate.findTopOrg();//��ȡ���л���ID
			for (int i = 0; i < arrOrgId.length; i++) {
				if(!arrOrgId[i].equals(topOrgId) && !arrOrgId[i].equals(crForm.getCollectId())){//���ܹ�ϵ�ų����� �ų�����
					id = new AfCollectRelationId();
					id.setCollectId(crForm.getCollectId());
					
					id.setOrgId(arrOrgId[i]);

					cr = new AfCollectRelation();
					cr.setId(id);
					// ������ܹ�ϵ
					session.save(cr);
				}			
			}
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	/**
	 * ��ѯ���ܻ���ID
	 * @param orgId
	 * @return
	 */
	public static String findCollectOrgId(String orgId){
		String result = null;
		DBConn dbconn = null;
		Session session = null;
		try {
			dbconn = new DBConn();
			session = dbconn.openSession();
			String hsql = "select cr.id.collectId from AfCollectRelation cr where cr.id.orgId='" + orgId + "'";
			List list =session.find(hsql);
			if(list.size()!=0)
				result = (String)list.get(0);
		} catch (Exception e) {
			log.printStackTrace(e);
			result = null;
		} finally {
			if (session != null)
				dbconn.closeSession();
		}
		return result;
	}
}
