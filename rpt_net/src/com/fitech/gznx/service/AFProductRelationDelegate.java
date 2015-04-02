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
	 * 插入汇总关系
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            包含要插入的基本信息
	 * @return boolean 是否插入成功
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
			// 传入的机构ID可能有多个机构ID连接而成
			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			for (int i = 0; i < arrOrgId.length; i++) {
				id = new AfProductRelationId();
				id.setCollectId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]);

				cr = new AfProductRelation();
				cr.setId(id);
				// 保存汇总关系
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
	 * 删除汇总关系
	 * 
	 * @author Nick
	 * @param orgId
	 *            String 删除汇总关系的汇总ID
	 * @return boolean 是否删除成功
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 更加传入的机构ID得到该机构的所有汇总关系的列表
	 * 
	 * @author Nick
	 * @param orgId
	 *            String 机构ID
	 * @return List 符合条件的汇总关系的列表
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
			// 传入的机构ID为汇总关系的汇总ID
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
	 * 插入汇总关系
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            包含要修改的汇总关系的基本信息
	 * @return boolean 是否修改成功
	 */
	public static boolean update(CollectRelationForm crForm) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			/** 1删除原有的汇总关系 */
			String hql = "from AfProductRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, crForm.getCollectId());
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfProductRelation cr = (AfProductRelation) iter.next();
				session.delete(cr);
			}
			/** 2增加新的汇总关系 */
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
				// 保存汇总关系
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
