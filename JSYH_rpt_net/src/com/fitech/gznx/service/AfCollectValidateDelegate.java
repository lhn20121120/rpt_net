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
	 * ��ʹ��hibernate ���Ϊ 2012-09-14
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
	 * ������ܹ�ϵ
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            ����Ҫ�޸ĵĻ��ܹ�ϵ�Ļ�����Ϣ
	 * @return boolean �Ƿ��޸ĳɹ�
	 */
	public static boolean update(CollectValidateRelationForm crForm) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			/** 1ɾ��ԭ�еĻ��ܹ�ϵ */
			String hql = "from AfValiRelation cr where cr.id.valiId=?";
			Query query = session.createQuery(hql);
			query.setString(0, crForm.getCollectId());
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfValiRelation cr = (AfValiRelation) iter.next();
				session.delete(cr);
			}
			/** 2�����µĻ��ܹ�ϵ */
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
	 * ������ܹ�ϵ
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            ����Ҫ�޸ĵĻ��ܹ�ϵ�Ļ�����Ϣ
	 * @return boolean �Ƿ��޸ĳɹ�
	 */
	public static boolean updateCollect(CollectValidateRelationForm crForm) {

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
			String topOrgId = AFValiRelationDelegate.findTopOrg();//��ȡ���л���ID
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
