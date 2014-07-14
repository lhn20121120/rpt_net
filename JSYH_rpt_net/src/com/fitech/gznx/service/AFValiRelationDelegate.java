package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.CollectRelationForm;
import com.fitech.gznx.form.CollectValidateRelationForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfValiRelation;
import com.fitech.gznx.po.AfValiRelationId;
import com.fitech.gznx.po.vOrgRel;

public class AFValiRelationDelegate {

	private static FitechException log = new FitechException(
			AFValiRelationDelegate.class);

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

			AfValiRelation cr = null;
			AfValiRelationId id = null;

			// System.out.println("orgId-->" + crForm.getOrgId());
			// ����Ļ���ID�����ж������ID���Ӷ���
			String[] arrOrgId = crForm.getOrgId().split(
					Config.SPLIT_SYMBOL_COMMA);
			for (int i = 0; i < arrOrgId.length; i++) {
				id = new AfValiRelationId();
				id.setvaliId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]);

				cr = new AfValiRelation();
				cr.setId(id);
				// ����У���ϵ
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
	 * ��ѯ���л���
	 * @return
	 */
	public static String findTopOrg(){
		DBConn conn = null;
		Session session = null;
		List resultList = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select oi.orgId from AfOrg oi where oi.preOrgId='0' and oi.isCollect=0";
			List list = session.createQuery(hql).list();
			if (list != null && list.size() != 0) {
				resultList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					resultList.add((String) list.get(i));
				}
			}

		} catch (Exception e) {
			log.printStackTrace(e);
			return null;
		} finally {
			if (session != null)
				conn.closeSession();
		}
		return (String)resultList.get(0);
	}
	
	/**
	 * ɾ��У���ϵ
	 * 
	 * @author Nick
	 * @param orgId
	 *            String ɾ��У���ϵ�Ļ���ID
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

			String hql = "from AfValiRelation cr where cr.id.valiId=?";
			Query query = session.createQuery(hql);
			query.setString(0, orgId);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				AfValiRelation cr = (AfValiRelation) iter.next();
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
	 * ��ѯ�ӻ��������req_id��ֵ
	 * ʹ��jdbc����Դ
	 * 
	 * 
	 */
	public static Map getSonReq(Connection orclCon, AfReport reportIn, Integer templateType) throws Exception {
		if (orclCon == null || reportIn == null || templateType == null)
			return null;
		PreparedStatement pStmt = null;
		ResultSet plRS = null;
		Map<String, Long> org_rep=new HashMap<String, Long>();
		
		try {
			String sql="";
			sql="select  a.org_id , b.rep_id from (select org_id from af_vali_relation t "+
			 " where t.vali_id = (select t.org_id from af_report t where t.rep_id = ?)) a,"+
			 " af_report b where a.org_id = b.org_id"+
			 " and b.year = ? and b.term = ? and b.day = ? and b.template_id=?";
			
			pStmt = orclCon.prepareStatement(sql.toUpperCase()); 
			pStmt.setLong(1, reportIn.getRepId());
			pStmt.setLong(2, reportIn.getYear());
			pStmt.setLong(3, reportIn.getTerm());
			pStmt.setLong(4, reportIn.getDay());
			pStmt.setString(5, reportIn.getTemplateId());
			plRS = pStmt.executeQuery();
			
			while(plRS.next()){
			String org=plRS.getString(1);
			Long rep=plRS.getLong(2);
			System.out.println(org);
			System.out.println(rep); 
			org_rep.put(org, rep); 
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		
		return org_rep;
		
		
	}
	//��ѯ�����ֵ
	
	public static List<Map<String,String>> parseCell(Connection orclCon, AfReport reportIn,
			Integer templateType) throws Exception {
	
		PreparedStatement pStmt = null;
		ResultSet plRS = null;
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();

		try {
			String orclSql = "";
	
			// �ж�ģ������
			orclSql += "select a.org_id,o.org_name,c.cell_name,c.row_name||'--'||c.col_name  name,b.cell_data "
				+"from af_report a, af_pbocreportdata b, af_cellinfo c, af_org o "
				+"where a.rep_id = b.rep_id and b.cell_id = c.cell_id(+)  and a.org_id = o.org_id "
				+"and a.rep_id = ?";

			pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
			pStmt.setLong(1, reportIn.getRepId());
			plRS = pStmt.executeQuery();

			

			while (plRS.next()) {
				Map<String,String> cellMap = new HashMap<String,String>();
				cellMap.put("org_id",plRS.getString("org_id".toUpperCase()));
				cellMap.put("org_name",plRS.getString("org_name".toUpperCase()));
				cellMap.put("cell_name",plRS.getString("cell_name".toUpperCase()));
				cellMap.put("name",plRS.getString("name".toUpperCase()));
				cellMap.put("cell_data",plRS.getString("cell_data".toUpperCase()));
				list.add(cellMap); 
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		return list;
	}
	
	
	//��ѯ�ӻ����ĵ�Ԫ����Ϣ
	
//��ѯ�����ֵ
	
	public static List<Map<String,String>> getSonCell(Connection orclCon, AfReport reportIn,
			Integer templateType,String repids,String cell_name) throws Exception {
	
		PreparedStatement pStmt = null;
		ResultSet plRS = null;
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		String[] ids=repids.split(",");
		for(int i=0;i<ids.length;i++){
			String id=ids[i];
		try {
			String orclSql = "";
	
			// �ж�ģ������
			orclSql += "select a.rep_id,a.org_id,o.org_name,c.cell_name,c.col_name ||'--'||c.row_name name,b.cell_data, a.template_id "
				+"from af_report a, af_pbocreportdata b, af_cellinfo c, af_org o "
				+"where a.rep_id = b.rep_id and b.cell_id = c.cell_id(+)  and a.org_id = o.org_id "
				+"and a.rep_id = ?  and c.cell_name=? ";

			pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
			pStmt.setString(1,id );
			pStmt.setString(2, cell_name);
			plRS = pStmt.executeQuery();
			while (plRS.next()) {
				Map<String,String> cellMap = new HashMap<String,String>();
				cellMap.put("org_id",plRS.getString("org_id".toUpperCase()));
				cellMap.put("org_name",plRS.getString("org_name".toUpperCase()));
				cellMap.put("cell_name",plRS.getString("cell_name".toUpperCase()));
				cellMap.put("name",plRS.getString("name".toUpperCase()));
				cellMap.put("cell_data",plRS.getString("cell_data".toUpperCase()));
				cellMap.put("template_id",plRS.getString("template_id".toUpperCase()));
				cellMap.put("rep_id",plRS.getString("rep_id".toUpperCase()));
				list.add(cellMap); 
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		}
		return list;
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
				
				
//				String hql1="from vOrgRel v where v.id.orgId='"+arrOrgId[i]+"'";
//				Query query1 = session.createQuery(hql1);
//				List<vOrgRel> orgRels=query1.list();
//				if(orgRels.size()==0)
//					continue;
//				vOrgRel orgRel=orgRels.get(0); 
//				System.out.println(orgRel.getId().getSysFlag());
				id.setvaliId(crForm.getCollectId());
				id.setOrgId(arrOrgId[i]); 
				cr = new AfValiRelation();
				cr.setId(id);
//				cr.setSysFlag(orgRel.getId().getSysFlag());
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
	
	/***
	 * ���ɻ�����ϵ�ĵ�һ��ΪSYS_FLAG��ֵ
	 * @return
	 */
	public static List getAllFirstVorgRel() {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		String hsql = "select oi.id.sysFlag from vOrgRel oi group by oi.id.sysFlag";

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

//			List list = query.list();
//
//			for (int i = 0; i < list.size(); i++) {
//
//				Object[] os = (Object[]) list.get(i);
//
//				String[] strs = new String[3];
//
//				strs[0] = ((String) os[0]).trim();
//				
//				result.add(strs);
//			}
			Object[] os = query.list().toArray();
			for(int i=0;i<os.length;i++){
				result.add(os[i]);
			}

			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	/***
	 * ���ɻ�����ϵ�ĵڶ���ΪPRE_ORGIDΪnull ����Ϊ�յ�
	 * @return
	 */
	public static List getAllSecondVorgRel(String sysFlag) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;
		//�������ɹ�ϵ������
		String hsql = "select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.id.sysFlag='"+sysFlag+"' and oi.preOrgid is null or oi.preOrgid=0 or oi.preOrgid=-1";
	//	String hsql = "select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.id.sysFlag='"+sysFlag+"' and (oi.preOrgid is null or oi.preOrgid='' or oi.preOrgid=0)";
		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();


				result.add(strs);
			}

			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	
	public static List getChildListByRelOrgId(String orgId,String sysFlag) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		if (orgId == null)
			return null;

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			StringBuffer sb = new StringBuffer(
					"select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.preOrgid='"
							+ orgId + "' and oi.id.sysFlag='"+sysFlag+"'");

			query = session.createQuery(sb.toString());

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();


				result.add(strs);

			}
			if (result.size() == 0)

				result = null;

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
