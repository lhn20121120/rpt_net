package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.action.CollectRelationAction;
import com.fitech.gznx.form.CollectValidateRelationForm;
import com.fitech.gznx.po.AfCollectRelation;
import com.fitech.gznx.po.AfCollectRelationId;
import com.fitech.gznx.po.AfValiRelation;
import com.fitech.gznx.po.AfValiRelationId;
import com.fitech.gznx.po.vOrgRel;

public class AfCollectValidateDelegate {
	
	private static FitechException log = new FitechException(
			CollectRelationAction.class);
	/**
	 * 已使用hibernate 孙大为 2012-09-14
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
			String hql = "from AfValiRelation cr where cr.id.valiId=?";
			Query query = session.createQuery(hql);
			query.setString(0, orgId);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfValiRelation cr = (AfValiRelation) iter.next();
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
	public static boolean update(CollectValidateRelationForm crForm) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			/** 1删除原有的汇总关系 */
			String hql = "from AfValiRelation cr where cr.id.valiId=?";
			Query query = session.createQuery(hql);
			query.setString(0, crForm.getCollectId());
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfValiRelation cr = (AfValiRelation) iter.next();
				session.delete(cr);
			}
			/** 2增加新的汇总关系 */
			AfValiRelation cr = null;
			AfValiRelationId id = null;

			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			String[] neworgs = clearStr(arrOrgId);
			arrOrgId = neworgs!=null?neworgs:arrOrgId;
			for (int i = 0; i < arrOrgId.length; i++) {
				if(crForm.getCollectId().equals(arrOrgId[i]))
					continue;
				id = new AfValiRelationId();
				
				
				String hql1="from vOrgRel v where v.id.orgId='"+arrOrgId[i]+"'";
				Query query1 = session.createQuery(hql1);
				List<vOrgRel> orgRels=query1.list();
				if(orgRels.size()==0)
					continue;
				vOrgRel orgRel=orgRels.get(0); 
				System.out.println(orgRel.getId().getSysFlag());
				id.setvaliId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]); 
				cr = new AfValiRelation();
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
	
	/**
	 * 插入汇总关系
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            包含要修改的汇总关系的基本信息
	 * @return boolean 是否修改成功
	 */
	public static boolean updateCollect(CollectValidateRelationForm crForm) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			/** 1删除原有的汇总关系 */
			String hql = "from AfCollectRelation cr where cr.id.collectId=?";
			Query query = session.createQuery(hql);
			query.setString(0, crForm.getCollectId());
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfCollectRelation cr = (AfCollectRelation) iter.next();
				session.delete(cr);
			}
			/** 2增加新的汇总关系 */
			AfCollectRelation cr = null;
			AfCollectRelationId id = null;

			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			String topOrgId = AFValiRelationDelegate.findTopOrg();//获取总行机构ID
			for (int i = 0; i < arrOrgId.length; i++) {
				if(!arrOrgId[i].equals(topOrgId) && !arrOrgId[i].equals(crForm.getCollectId())){//汇总关系排除总行 排除自身
					id = new AfCollectRelationId();
					id.setCollectId(crForm.getCollectId());
					
					id.setOrgId(arrOrgId[i]);

					cr = new AfCollectRelation();
					cr.setId(id);
					// 保存汇总关系
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
	
	
	private static String[] clearStr(String[] strs){
		if(strs!=null && strs.length>0){
			for (int i = 0; i < strs.length; i++) {
				if(strs[i].indexOf("_")!=-1){
					strs[i] = strs[i].substring(strs[i].indexOf("_")+1, strs[i].length());
				}
			}
		}
		return strs;
	}
	
}
