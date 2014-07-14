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
import com.fitech.gznx.po.AfProductRelation;
import com.fitech.gznx.po.AfProductRelationId;
import com.fitech.gznx.po.vOrgRel;


public class AFProductRelationDelegate {

	private static FitechException log = new FitechException(
			AFProductRelationDelegate.class);

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

			AfProductRelation cr = null;
			AfProductRelationId id = null;

			// System.out.println("orgId-->" + crForm.getOrgId());
			// ����Ļ���ID�����ж������ID���Ӷ���
			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			for (int i = 0; i < arrOrgId.length; i++) {
				id = new AfProductRelationId();
				id.setCollectId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]);

				cr = new AfProductRelation();
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

			String hql = "from AfProductRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, orgId);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfProductRelation cr = (AfProductRelation) iter.next();
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
			String hql = "from AfProductRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, orgId);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfProductRelation cr = (AfProductRelation) iter.next();
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
			String hql = "from AfProductRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, crForm.getCollectId());
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfProductRelation cr = (AfProductRelation) iter.next();
				session.delete(cr);
			}
			/** 2�����µĻ��ܹ�ϵ */
			AfProductRelation cr = null;
			AfProductRelationId id = null;

			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			for (int i = 0; i < arrOrgId.length; i++) {
				if(crForm.getCollectId().equals(arrOrgId[i]))
					continue;
				id = new AfProductRelationId();
				
				
				String hql1="from vOrgRel v where v.id.orgId='"+arrOrgId[i]+"'";
				Query query1 = session.createQuery(hql1);
				List<vOrgRel> orgRels=query1.list();
				if(orgRels.size()==0)
					continue;
				vOrgRel orgRel=orgRels.get(0);
				
				System.out.println(orgRel.getId().getSysFlag());
				id.setCollectId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]);
				

				cr = new AfProductRelation();
				cr.setId(id);
				cr.setSysFlag(orgRel.getId().getSysFlag());
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
}
