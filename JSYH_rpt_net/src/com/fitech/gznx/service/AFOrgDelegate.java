package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.hibernate.MPurOrg;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfCollectRelation;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplateCollRep;
import com.fitech.gznx.po.AfTemplateOrgRelation;
import com.fitech.gznx.po.AfTemplateOuterRep;
import com.fitech.gznx.treexml.BaseOrgTreeIterator;
import com.fitech.gznx.treexml.OrgTreeInterface;
import com.fitech.gznx.util.TranslatorUtil;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;
import com.fitech.papp.webservice.pojo.WebSysOrg;

public class AFOrgDelegate {

	private static FitechException log = new FitechException(
			AFOrgDelegate.class);

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg
	 * �õ�һ�������
	 * 
	 * @param orgId
	 * @return
	 */
	public static AfOrg getOrgInfo(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		if (orgId == null || orgId.equals("")) {
			return null;
		}

		try {
			dbconn = new DBConn();

			session = dbconn.openSession();

			AfOrg ao = (AfOrg) session.get(AfOrg.class, orgId);

			return ao;

		} catch (Exception e) {

			log.printStackTrace(e);

			return null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ���»���Ϣ
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm����
	 * @return boolean ���»��Ƿ�ɹ�
	 */
	public static boolean setFirstNode(OrgInfoForm orgInfoForm) {
		boolean result = false;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.beginTransaction();

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class, orgInfoForm
					.getOrgId());
			// ֻҪ���¸û��preOrgId����
			orgInfo.setPreOrgId(Config.COLLECT_ORG_PARENT_ID);
			// ���»���Ϣ
			session.update(orgInfo);

			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ������л����Ϣ��ID�б�
	 * 
	 * @return
	 */
	public static List getAllOrgList() {
		DBConn conn = null;
		Session session = null;
		List resultList = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select oi.orgId from AfOrg oi";
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
		return resultList;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ����û���ȡ�ø��û��Ļ�Ȩ����Ϣ
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static List getUserOrgPurview(String userId) throws Exception {
		if (userId == null || userId.equals(""))
			return null;

		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg afo where afo.orgId in "
					+ "(select opr.org.orgId from Operator opr where opr.userName =? ) "
					+ "order by afo.orgId";

			Query query = session.createQuery(hql);
			query.setString(0, userId);
			List list = query.list();

			if (list != null && list.iterator().hasNext()) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg item = (AfOrg) list.get(i);
					result.add(item.getOrgId().toString());
				}
			}

		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ����û���ȡ�ø��û��Ļ�Ȩ����Ϣ
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static List getUserOrgPurviewHZRule(String userId) throws Exception {
		if (userId == null || userId.equals(""))
			return null;
		
		List result = null;
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from AfOrg afo where afo.orgId in "
				+ "(select opr.org.orgId from Operator opr where opr.userName =? ) "
				+ "order by afo.orgId";
			
			Query query = session.createQuery(hql);
			query.setString(0, userId);
			List list = query.list();
			
			if (list != null && list.iterator().hasNext()) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg item = (AfOrg) list.get(i);
					result.add(item);
				}
			}
			
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * title:�÷������ڸ��orgId�������������б��б� author:chenbing date:2008-2-19
	 * 
	 * @param orgId
	 * @return List:LableValueBean:��ID,�����
	 */
	public static List getChildListByOrgId(String orgId) {

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
					"select oi.orgId,oi.orgName,oi.isCollect ,oi.preOrgId from AfOrg oi where oi.preOrgId='"
							+ orgId + "' ");

			query = session.createQuery(sb.toString());

			List list = query.list();

  			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[4];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();
				strs[3] = ((String) os[3]).trim();

				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

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
							+ orgId + "' and oi.id.sysFlag='"+sysFlag+"' order by oi.id.orgId");

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
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * title:�÷������ڷ������еĶ���� author:chenbing date:2008-2-19
	 * 
	 * @return
	 */
	public static List getAllFirstOrg() {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		String hsql = "select oi.orgId,oi.orgName,oi.isCollect,oi.preOrgId from AfOrg oi where oi.preOrgId is null or oi.preOrgId = '0' ";

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[4];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();
				
				strs[3] = ((String) os[3]).trim();

				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

				result.add(strs);
			}
			//��ѯ�����ж����������AfCollectRelation���в�ѯ����û��ColletId ������colletidΪʵ���������
			hsql = "select oi.orgId,oi.orgName,oi.isCollect ,oi.preOrgId from AfOrg oi " +
					"where oi.orgId  not in " +
						"(select a.id.orgId from AfCollectRelation a where a.id.collectId not in " +
							"(select c.orgId from AfOrg c where c.preOrgId !='"
								+ Config.COLLECT_ORG_PARENT_ID
							+ "'))  and oi.preOrgId = '"
					+ Config.COLLECT_ORG_PARENT_ID + "'";
			
			query = session.createQuery(hsql);

			list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[4];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();
				
				strs[3] = ((String) os[3]).trim();
				
				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

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
	
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * title:�÷������ڷ������еĶ���� author:chenbing date:2008-2-19
	 * 
	 * @return
	 */
	public static List getFirstOrgById(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		String hsql = "select oi.orgId,oi.orgName,oi.isCollect from AfOrg oi where oi.orgId ='"+orgId+"'";

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

				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

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
	
	/***
	 * ��ɻ��ϵ�ĵ�һ��ΪSYS_FLAG��ֵ
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
	 * ��ɻ��ϵ�ĵڶ���ΪPRE_ORGIDΪnull ����Ϊ�յ�
	 * @return
	 */
	public static List getAllSecondVorgRel(String sysFlag) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;
		//����ɹ�ϵ������
		//String hsql = "select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.id.sysFlag='"+sysFlag+"'";
		String hsql = "select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.id.sysFlag='"+sysFlag+"' and (oi.preOrgid is null or oi.preOrgid='' or oi.preOrgid=0)";
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
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * Ӱ�����AfOrg
	 * ��ѯ�ӻ���Ϣ
	 * @param orgId
	 * @param session
	 * @return
	 */
	public static List selectLowerOrgList(String orgId,HttpSession session){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			
			String hql = "from AfOrg ont where ont.preOrgId='" + orgId.trim() + "' or ont.isCollect=1"; 
			
			List list = session1.find(hql.toString());
			HashMap Map=null;
			if(session.getAttribute("SelectedOrgIds")!=null){			  
				Map=(HashMap)session.getAttribute("SelectedOrgIds");			  
			}
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					AfOrg aforg = (AfOrg)list.get(i);
					OrgNet orgNet = new OrgNet();
					orgNet.setOrgId(aforg.getOrgId());
					orgNet.setOrgName(aforg.getOrgName());
					//orgNet.setOrgType(aforg.getOrgType());
					orgNet.setPreOrgId(aforg.getPreOrgId());
					//orgNet.setRegion(aforg.getRegionId());
					orgNet.setSetOrgId(aforg.getSetOrgId());
					if(Map!=null){						
						if(Map.containsKey(orgNet.getOrgId())){
							orgNet.setPreOrgId("true");//������pre org��־�Ƿ�ѡ��							
						}
					}
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ�������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	public static List selectLowerAfOrgList(String orgId,HttpSession session){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			
			String hql = "from AfOrg ont where ont.preOrgId='" + orgId.trim() + "'"; 
			
			List list = session1.find(hql.toString());
			HashMap Map=null;
			if(session.getAttribute("SelectedOrgIds")!=null){			  
				Map=(HashMap)session.getAttribute("SelectedOrgIds");			  
			}
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					AfOrg aforg = (AfOrg)list.get(i);
					/*OrgNet orgNet = new OrgNet();
					orgNet.setOrgId(aforg.getOrgId());
					orgNet.setOrgName(aforg.getOrgName());
					//orgNet.setOrgType(aforg.getOrgType());
					orgNet.setPreOrgId(aforg.getPreOrgId());
					//orgNet.setRegion(aforg.getRegionId());
					orgNet.setSetOrgId(aforg.getSetOrgId());*/
					if(Map!=null){						
						if(Map.containsKey(aforg.getOrgId())){
							aforg.setPreOrgId("true");//������pre org��־�Ƿ�ѡ��							
						}
					}
					orgNetList.add(aforg);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ�������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * �ж��Ƿ�����ͬ�Ļ����
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm����
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistSameOrgId(OrgInfoForm orgInfoForm) {
		boolean result = false;
		List list = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery("from AfOrg oi where oi.orgId=?");
			query.setString(0, orgInfoForm.getOrgId());
			list = query.list();
			if (list != null && list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * �ж��Ƿ�����ͬ�Ļ����
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm����
	 * @return boolean �Ƿ����
	 */
	public static boolean isExistSameOrgName(OrgInfoForm orgInfoForm) {
		boolean result = false;
		List list = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session
					.createQuery("from AfOrg oi where oi.orgName=?");
			query.setString(0, orgInfoForm.getOrgName());
			list = query.list();
			if (list != null && list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ���ӻ���Ϣ
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm����
	 * @param userId
	 *            String ���ӻ���û�ID
	 * @return boolean �Ƿ����ӳɹ�
	 */
	public static boolean add(OrgInfoForm orgInfoForm, String userId) {
		boolean result = false;
		List list = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL�����
		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.beginTransaction();

			/** 1 ���ӻ���Ϣ */
			// �õ����ڵ����ϸ��Ϣ
			AfOrg parentOrgInfo = AFOrgDelegate.getOrgInfo(orgInfoForm.getParentOrgId());

			AfOrg orgInfo = new AfOrg();

			orgInfo.setOrgId(orgInfoForm.getOrgId());
			orgInfo.setOrgName(orgInfoForm.getOrgName());

//			if (orgInfoForm.getOrgLevel() != null)
//				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgType()));

			if (orgInfoForm.getOrgType() != null){
				orgInfo.setOrgType(orgInfoForm.getOrgType());
				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgType()));
			}
			
			if (orgInfoForm.getOrgRegion() != null)
				orgInfo.setRegionId(orgInfoForm.getOrgRegion());

			if (orgInfoForm.getSetOrgId() != null)
				orgInfo.setSetOrgId(orgInfoForm.getSetOrgId());
			
			orgInfo.setOrgOuterId(orgInfoForm.getOrgOuterId());

			if (orgInfoForm.getParentOrgId() != null
					&& !orgInfoForm.getParentOrgId().equals("")) {
				orgInfo.setPreOrgId(orgInfoForm.getParentOrgId());
			} else {
				orgInfo.setPreOrgId(Config.COLLECT_ORG_PARENT_ID);
			}
			//�ж�������Ƿ���Ҫ����
			if (!orgInfoForm.getOrgType().equals(Config.COLLECT_ORG_PARENT_ID) && !orgInfoForm.getOrgType().equals(Config.COLLECT_ORG_PARENT_QT_ID)) {
				orgInfo.setIsCollect(Long.valueOf(Config.NOT_IS_COLLECT));
			} else {
				orgInfo.setIsCollect(Long.valueOf(Config.IS_COLLECT));
			}

			session.save(orgInfo);

			// /** 2 ���ӻ���û����иû�Ļ�Ȩ�� */
			// UserOrgPurview uop = new UserOrgPurview();
			// UserOrgPurviewId id = new UserOrgPurviewId();
			//
			// id.setOrgId(orgInfoForm.getOrgId());
			// UserInfo ui = StrutsUserInfoDelegate.selectOne(userId);
			// id.setUserInfo(ui);
			// uop.setId(id);
			// session.save(uop);
			//
			// /** 3������ӵ�����ʵ����Ҫ����ָ�꼯�����ϵ */
			// if (orgInfo.getIsCollect().equals(
			// new Integer(Config.NOT_IS_COLLECT))) {
			// // ����ָ�꼯�����ϵ
			// List lstMA = StrutsMeasureAttributeDelegate
			// .selectMAList(orgInfo);
			// MeasureOrgMapping mom = null;
			// MeasureOrgMappingId momId = null;
			// if (lstMA != null && lstMA.size() > 0) {
			// for (int i = 0; i < lstMA.size(); i++) {
			// MeasureAttribute ma = (MeasureAttribute) lstMA.get(i);
			//
			// mom = new MeasureOrgMapping();
			// momId = new MeasureOrgMappingId();
			//
			// momId.setMeasureAttribute(ma);
			// momId.setOrgId(orgInfo.getOrgId());
			//
			// mom.setId(momId);
			// session.save(mom);
			// }
			// }
			//
			// }

			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ���»���Ϣ
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm����
	 * @return boolean ���»��Ƿ�ɹ�
	 */
	public static boolean updateOrgInfo(OrgInfoForm orgInfoForm) {
		boolean result = false;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.beginTransaction();

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class, orgInfoForm
					.getOrgId());

			orgInfo.setOrgName(orgInfoForm.getOrgName());
			orgInfo.setOrgOuterId(orgInfoForm.getOrgOuterId());
			
			if (orgInfoForm.getOrgLevel() != null)
				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgLevel()));
			
			orgInfo.setOrgType(orgInfoForm.getOrgType());
			
			if (orgInfoForm.getOrgRegion() != null)
				orgInfo.setRegionId(orgInfoForm.getOrgRegion());
			
			// ���»���Ϣ
			session.update(orgInfo);

			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ɾ�����Ϣ[������������Ҫ��ɾ����ܹ�ϵ]
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm����
	 * @return boolean ɾ����Ƿ�ɹ�
	 */
	public static boolean delete(OrgInfoForm orgInfoForm) {
		boolean result = true;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		List lst = null;

		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.beginTransaction();

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class, orgInfoForm
					.getOrgId());

			if (orgInfo == null) {
				return false;
			}
			// ������������ɾ��CollectIdΪ��ǰ��ID�ļ�¼
			// �������ʵ����ɾ��OrgIdΪ��ǰ��ID�ļ�¼
//			if (orgInfo.getIsCollect().equals(Long.valueOf(Config.IS_COLLECT))) {
				// �����
				String hql = "from AfCollectRelation cr where cr.id.collectId=? or cr.id.orgId=?";//2013-12-10:LuYueFeiɾ�����а�˻�ID�ļ�¼
				Query query = session.createQuery(hql);
				query.setString(0, orgInfo.getOrgId());query.setString(1, orgInfo.getOrgId());//2013-12-10:LuYueFeiɾ�����а�˻�ID�ļ�¼
				List crLst = query.list();

				if (crLst != null && crLst.size() > 0) {
					for (int i = 0; i < crLst.size(); i++) {
						AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
						session.delete(cr);
					}
				}
/**			} else {
				// ��ʵ��
				String hql = "from AfCollectRelation cr where cr.id.orgId=?";
				Query query = session.createQuery(hql);
				query.setString(0, orgInfo.getOrgId());
				List crLst = query.list();

				if (crLst != null && crLst.size() > 0) {
					for (int i = 0; i < crLst.size(); i++) {
						AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
						session.delete(cr);
					}
				}

				// // �Զ�ɾ����ָ�꼯��Ӧ��ϵ
				// hql = "from MeasureOrgMapping mom where mom.id.orgId=?";
				// lst = session.createQuery(hql).setString(0,
				// orgInfo.getOrgId())
				// .list();
				// for (int i = 0; i < lst.size(); i++) {
				// session.delete(lst.get(i));
				// }
			}*/

			// // �Զ�ɾ��û�����л�Ȩ�޼�¼
			// String hql = "from UserOrgPurview uop where uop.id.orgId=?";
			// lst = session.createQuery(hql).setString(0, orgInfo.getOrgId())
			// .list();
			// for (int i = 0; i < lst.size(); i++) {
			// session.delete(lst.get(i));
			// }


			//ɾ���ʱ��ɾ���Ӧ��MPurOrg�еļ�¼
			String mpohql = "from MPurOrg mpo where mpo.comp_id.org.orgId=?";
			Query mpoquery = session.createQuery(mpohql);
			mpoquery.setString(0, orgInfo.getOrgId());
			List mpoLst = mpoquery.list();

			if (mpoLst != null && mpoLst.size() > 0) {
				for (int i = 0; i < mpoLst.size(); i++) {
					MPurOrg mpo = (MPurOrg) mpoLst.get(i);
					session.delete(mpo);
				}
			}		
			
			//ɾ���ʱ��ɾ���Ӧ��AfTemplateOrgRelation�еļ�¼
			String sql1 = "from AfTemplateOrgRelation ator where ator.id.orgId=?";
			
			Query atorquery = session.createQuery(sql1);
			atorquery.setString(0, orgInfo.getOrgId());
			List atorLst = atorquery.list();

			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateOrgRelation ator = (AfTemplateOrgRelation) atorLst.get(i);
					session.delete(ator);
				}
			}
			String sql2 = "from AfTemplateCollRep ator where ator.id.orgId=?";
			Query atorquery2 = session.createQuery(sql2);
			atorquery2.setString(0, orgInfo.getOrgId());
			atorLst = atorquery2.list();
			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateCollRep ator = (AfTemplateCollRep) atorLst.get(i);
					session.delete(ator);
				}
			}
			String sql3 = "from AfTemplateOuterRep ator where ator.id.orgId=?";
			Query atorquery3 = session.createQuery(sql3);
			atorquery3.setString(0, orgInfo.getOrgId());
			atorLst = atorquery3.list();
			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateOuterRep ator = (AfTemplateOuterRep) atorLst.get(i);
					session.delete(ator);
				}
			}
			session.delete(orgInfo);
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ���ID�ͻ�Ȩ���ҳ�һ�һ���ӻ�
	 * 
	 * @param orgId
	 * @param orgTemp
	 * @return
	 */
	public static List getChildList(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		if (orgId == null)
			return null;

		try {

			dbconn = new DBConn();

			session = dbconn.openSession();

			StringBuffer sb = new StringBuffer(
					"from AfOrg oi where oi.preOrgId='" + orgId + "' ");

			query = session.createQuery(sb.toString());

			List list = query.list();

			return list;

		} catch (Exception e) {

			log.printStackTrace(e);

			return null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}

	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ��ݻ�ID�õ����е������û�
	 * 
	 * @param orgId
	 *            String
	 * 
	 * @return boolean �Ƿ���������û�
	 */
	public static boolean hasUsers(String orgId) {
		DBConn conn = null;
		Session session = null;
		boolean result = true;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from Operator ui where ui.org.orgId=?";
			List list = session.createQuery(hql).setString(0, orgId).list();
			if (list != null && list.size() == 0) {
				result = false;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (session != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 *��ʹ��hibernate ���Ը� 2011-12-22
	 * ��ɻ�����XML�ļ�
	 * 
	 * @author Nick
	 * @return boolean ��ɻ����Ƿ�ɹ�
	 */
	public static boolean makeOrgTree() {

		boolean result = false;
		/** ���XML */
		OrgTreeInterface orgTree = new BaseOrgTreeIterator();
		/**��ʹ��hibernate ���Ը� 2011-12-22**/
		if (orgTree.createTreeForTagXml() && orgTree.createTreeForVorgRelXml()) {
			result = true;
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * <p>
	 * ����:��ѯ�ӻ�����
	 * </p>
	 * <p>
	 * ����:
	 * </p>
	 * <p>
	 * ���ڣ�2008-1-3
	 * </p>
	 * <p>
	 * ���ߣ��ܷ���
	 * </p>
	 */
	public static int selectSubOrgCount(OrgInfoForm orgInfoForm) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgInfoForm == null)
			return count;
		try {
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfOrg oi where oi.preOrgId='"
							+ orgInfoForm.getParentOrgId() + "'");
			StringBuffer where = new StringBuffer();

			// �������Ʋ�ѯ����
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and ont.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() == 1) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {

			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ����¼����б���Ϣ
	 * 
	 * @param orgNetForm
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectSubOrgList(OrgInfoForm orgInfoForm, int offset,
			int limit) {
		if (orgInfoForm == null)
			return null;

		List refVals = null;
		DBConn conn = null;
		Session session = null;

		try {
			// ��ѯ����HQL�����
			StringBuffer hql = new StringBuffer(
					"from AfOrg ont where ont.preOrgId='"
							+ orgInfoForm.getParentOrgId() + "'");
			StringBuffer where = new StringBuffer();

			// �������Ʋ�ѯ����
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and ont.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			where.append(" order by ont.orgName,ont.orgLevel");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				refVals = new ArrayList();
				OrgInfoForm orgInfoFormTemp = null;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg afOrg = (AfOrg) list.get(i);
						orgInfoFormTemp = new OrgInfoForm();
						TranslatorUtil.copyPersistenceToVo(afOrg,
								orgInfoFormTemp);

						refVals.add(orgInfoFormTemp);
					}
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * <p>
	 * ����:��ѯ�ӻ�����
	 * <p>
	 * ���ߣ��ܷ���
	 * </p>
	 */
	public static int selectSubOrgCount(OrgInfoForm orgInfoForm, Operator operator ,Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (operator == null)
			return count;
		try {
			
			
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfOrg oi where oi.orgId in ("
							+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") 
							+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" 
							+ templateType +"))");
			
			StringBuffer where = new StringBuffer();

			// �������Ʋ�ѯ����
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and oi.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() == 1) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {

			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ����¼����б���Ϣ
	 * 
	 * @param orgNetForm
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectSubOrgList(OrgInfoForm orgInfoForm, Operator operator, Integer templateType, int offset,
			int limit) {
		if (operator == null)
			return null;

		List refVals = null;
		DBConn conn = null;
		Session session = null;

		try {
			// ��ѯ����HQL�����
			StringBuffer hql = new StringBuffer(
					"from AfOrg ont where ont.orgId in ("
					+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") 
					+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" 
					+ templateType +"))");
			
			StringBuffer where = new StringBuffer();

			// �������Ʋ�ѯ����
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and ont.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			
			where.append(" order by ont.orgId,ont.orgLevel");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				refVals = new ArrayList();
				OrgInfoForm orgInfoFormTemp = null;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg afOrg = (AfOrg) list.get(i);
						orgInfoFormTemp = new OrgInfoForm();
						TranslatorUtil.copyPersistenceToVo(afOrg,
								orgInfoFormTemp);

						refVals.add(orgInfoFormTemp);
					}
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ��ȡ��֧���б�
	 * 
	 * @param preOrgId
	 *            �ϼ���ID
	 * @return List
	 */
	public static List selectSubOrgList(String preOrgId) {
		if (preOrgId == null)
			return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;

		try {
			conn = new DBConn();
			session1 = conn.openSession();

			orgNetList = new ArrayList();

			String hql = "from AfOrg ont where ont.preOrgId = '"
					+ preOrgId.trim() + "'";

			List list = session1.find(hql.toString());

			if (list != null && list.size() > 0) {
				OrgInfoForm orgNetFormTemp = null;
				AfOrg orgNetPersistence = null;

				for (int i = 0; i < list.size(); i++) {
					orgNetPersistence = (AfOrg) list.get(i);
					orgNetFormTemp = new OrgInfoForm();
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence,
							orgNetFormTemp);

					orgNetList.add(orgNetFormTemp);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return orgNetList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg
	 * ��ݻ�id��ѯ����Ϣ
	 * 
	 * @param orgId
	 *            ��id
	 * @return AfOrg
	 */
	public static AfOrg selectOne(String orgId) {
		AfOrg orgNetResult = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.orgId = '" + orgId.trim()
					+ "'";
			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				orgNetResult = (AfOrg) list.get(0);
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			orgNetResult = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return orgNetResult;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg MPurOrg
	 * ����û���IDȡ�ø��û����Ѿ����ù�Ļ�
	 * 
	 * @param userGrpId
	 *            �û���ID
	 * @param powType
	 *            Ȩ������
	 * @return
	 */
	public static List getUserGrpOrgPopedom(Long userGrpId, Integer powType) {
		List result = null;
		DBConn conn = null;
		Session session = null;
		try {
			if (userGrpId != null) {
				conn = new DBConn();
				session = conn.openSession();
				String hql = "select distinct on.orgId,on.orgName from AfOrg on where on.orgId in (select distinct mpo.comp_id.org.orgId from MPurOrg mpo "
						+ "where mpo.comp_id.MUserGrp.userGrpId="
						+ userGrpId
						+ " and mpo.comp_id.powType=" + powType + ")";

				Query query = session.createQuery(hql);
				List list = query.list();
				if (list != null && list.size() > 0) {
					result = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						Object[] object = (Object[]) list.get(i);
						OrgNetForm form = new OrgNetForm();
						form.setOrg_id(object[0].toString());
						form.setOrg_name(object[1].toString());
						result.add(form);
					}
				}
			}
		} catch (Exception ex) {
			result = null;
			log.printStackTrace(ex);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg
	 * ȡ�õ�ǰ����¼���(���֧�������)
	 * 
	 * @param orgId
	 * @return
	 */
	public static String selectSubOrgIds(String orgId) {

		String lowerOrgIds = "";
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.preOrgId = '" + orgId.trim()
					+ "' or ont.isCollect=1";

			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {

				for (int i = 0; i < list.size(); i++) {
					AfOrg orgNet = (AfOrg) list.get(i);
					lowerOrgIds = lowerOrgIds.equals("") ? "'"
							+ orgNet.getOrgId() + "'" : lowerOrgIds + "," + "'"
							+ orgNet.getOrgId() + "'";
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			lowerOrgIds = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return lowerOrgIds;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg
	 * ��ݻ�id����ѯ�û��������ӻ�
	 * 
	 * @param orgId
	 * @return
	 */
	public static String selectAllLowerOrgIds(String orgId) {
		String allLowerOrgIds = "";
		String lowerOrgIds = "'" + orgId.trim() + "'";
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			while (true) {
				String temp = lowerOrgIds;
				lowerOrgIds = "";
				String hql = "from AfOrg ont where ont.preOrgId in (" + temp + ")";

				List list = session.find(hql.toString());

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg orgNet = (AfOrg) list.get(i);
						lowerOrgIds = lowerOrgIds.equals("") ? "'"
								+ orgNet.getOrgId() + "'" : lowerOrgIds + ",'"
								+ orgNet.getOrgId() + "'";
					}
				}
				
				if (lowerOrgIds.equals("")){
					hql = "from AfOrg ont where ont.isCollect=1";

					list = session.find(hql.toString());
		
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							AfOrg orgNet = (AfOrg) list.get(i);
							lowerOrgIds = lowerOrgIds.equals("") ? "'"
									+ orgNet.getOrgId() + "'" : lowerOrgIds + ",'"
									+ orgNet.getOrgId() + "'";
						}
					}
					allLowerOrgIds = lowerOrgIds.equals("") ? allLowerOrgIds
							: allLowerOrgIds + "," + lowerOrgIds;
					break;
				}else
					allLowerOrgIds = allLowerOrgIds.equals("") ? lowerOrgIds
							: allLowerOrgIds + "," + lowerOrgIds;
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			lowerOrgIds = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return allLowerOrgIds;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ݻ�IDȡ����Ӧ����Ϣ
	 * 
	 * @param orgIds
	 * @return List ���б�
	 */
	public static List selectOrgByIds(String orgIds,String reportflg) {
		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			orgIds = orgIds +" and viewOrgRep.childRepId in (select t.id.templateId from " +
					"AfTemplate t where t.templateType="+reportflg+") group by viewOrgRep.orgId ";
			String hql = "from AfOrg ont where ont.orgId in(" + orgIds
					+ ") order by ont.regionId,ont.preOrgId,ont.orgId";
			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg orgNet = (AfOrg) list.get(i);
					result.add(orgNet);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg
	 * ��ݻ�IDȡ����Ӧ����Ϣ
	 * 
	 * @param orgIds
	 * @return List ���б�
	 */
	public static List selectOrgByIds(String orgIds) {
		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();			
			String hql = "from AfOrg ont where ont.orgId in(" + orgIds
					+ ") order by ont.orgId";
			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg orgNet = (AfOrg) list.get(i);
					result.add(orgNet);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ݻ�id����ѯ�û��������ӻ�
	 * @param orgId
	 * @return
	 */
	public static String selectRegionId(String orgId){
		String regionId="";
		String lowerOrgIds = "'"+orgId.trim()+"'";
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.orgId=" + orgId; 
			
			List list = session.find(hql.toString());
						
			if(list != null && list.size() > 0){
				AfOrg orgNet = (AfOrg)list.get(0);
				regionId = orgNet.getRegionId() != null?String.valueOf(orgNet.getRegionId()):"";
				
			}
				
			
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			lowerOrgIds = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ�������Ự������
			if(conn != null) conn.closeSession();
		}
		return regionId;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �õ�Ӧ�����б������
	 * @param templateId ģ��id 
	 * @param versionId �汾��
	 * @param orgId �ñ��?�ͻ�
	 * @param childOrgIds �û���Ȩ��
	 * @param type ��������
	 * @return int
	 */
	public int getMustOrgCount(String templateId, String versionId, String childOrgIds,
			String orgId, Integer type){
		int  count = 0;	
		DBConn conn = null;
		Session session = null;
		
		if(templateId == null || templateId.equals("")
				|| versionId == null || versionId.equals("")
				)
			return count;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			String sql = "";
			//���л������
			if (type.equals(Integer.valueOf(1)) || type.equals(Integer.valueOf(4))){
				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
					"(select distinct M.id.orgId from AfViewReport M where " +
					//"M.comp_id.orgId in (" + childOrgIds + ") and " +
					"M.id.templateId='" + templateId + "' " +
					" and  M.id.versionId='" + versionId + "'";
					//") and org.isCollect=1";
					if(type.equals(Integer.valueOf(1)))
						sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID+"'";
					if(type.equals(Integer.valueOf(4)))
						sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_QT_ID+"'";
					
			}
			//���������
			else if(type.equals(Integer.valueOf(2))){
				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M,AfCollectRelation afc" +
				" where afc.id.orgId=M.id.orgId " +
				//" and M.id.orgId in (" + childOrgIds + ")" +
				" and M.id.templateId='" + templateId + 
				"' and M.id.versionId='" + versionId + "'" +
				" and afc.id.collectId='"+ orgId +"')";
			
			}else if(type.equals(Integer.valueOf(8))){//�������
				String formula = com.fitech.gznx.service.AFTemplateCollRuleDelegate.getCollFormulaName(orgId, templateId, versionId);
				if(formula!=null&&formula.length()>0){
					return com.fitech.gznx.service.AFTemplateCollRuleDelegate.getNeedReportNUM(formula,orgId);
				}
			}
			//���л�֧��
			else{
/*				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M where M.id.orgId in " +
				"(" + childOrgIds + ")  and M.id.templateId='" + templateId + "' "			
					+" and M.id.versionId='" + versionId + "')";*/
				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M where M.id.orgId in " +
				"(select aor.orgId from AfOrg aor where aor.preOrgId='"+ orgId + "')" +
				//" and aor.orgId in (" + childOrgIds + ")) " +
				" and M.id.templateId='" + templateId + "' " +
				" and M.id.versionId='" + versionId + "')";
			
			}
			
			List list = session.createQuery(sql).list();
			
			if(list!=null && list.size()>0){
					count = ((Integer)list.get(0)).intValue();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null)			
				conn.closeSession();
		}
		return count;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfOrg AfViewReport AfCollectRelation
	 * �õ�Ӧ�����б�
	 * @param childRepId
	 * @param versionId
	 * @param childOrgIds
	 * @return
	 */
	public List getMustOrgList(String templateId, String versionId, String childOrgIds,
			String orgId, Integer type){
		List result = null;	
		DBConn conn = null;
		Session session = null;
		
		if(templateId == null || templateId.equals("")
				|| versionId == null || versionId.equals("")
				/*|| childOrgIds == null || childOrgIds.equals("")*/)
			return result;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String sql = "";

			//���л������
			if (type.equals(Integer.valueOf(1)) || type.equals(Integer.valueOf(4))){
				sql = "select org from AfOrg org where org.orgId in " +
				"(select distinct M.comp_id.orgId from AfViewReport M where " 
				//	+ "M.comp_id.orgId in (" + childOrgIds + ")  and " 
					+ "M.comp_id.templateId='" + templateId + "' "			
					+ "  and  M.comp_id.versionId='" + versionId + "'";
				//	+ ") and org.isCollect=1";
				if(type.equals(Integer.valueOf(1)))
					sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID+"'";
				if(type.equals(Integer.valueOf(4)))
					sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_QT_ID+"'";
			
			}
			//���������
			else if(type.equals(Integer.valueOf(2)) || type.equals(Integer.valueOf(5))){
				sql = "select org from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M,AfCollectRelation afc" +
				" where afc.id.orgId=M.id.orgId " +
				//" and M.id.orgId in (" + childOrgIds + ")" +
				" and M.id.templateId='" + templateId + 
				"' and M.id.versionId='" + versionId + "'" +
				" and afc.id.collectId='"+ orgId +"')";
			
			}
			//���л�֧��
			else{
				sql = "select org from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M where M.id.orgId in " +
				//"(" + childOrgIds + ")
				"(select aor.orgId from AfOrg aor where aor.preOrgId='"+ orgId + "')" +
				//"' and aor.orgId in (" + childOrgIds + ")) " +
				"and M.id.templateId='" + templateId + "' "	+
				"and M.id.versionId='" + versionId + "')";
			
			}
			StringBuffer sb = new StringBuffer(sql);
			sb.append(" and (org.orgId in (select r.id.orgId from AfTemplateCollRep r where r.id.templateId='" + templateId + "' and r.id.versionId='" + versionId + "'))");
			List list=session.createQuery(sb.toString()).list();
			
			if(list!=null && list.size()>0){
				result = new ArrayList();
				for(int j=0;j<list.size();j++){
					AfOrg org = (AfOrg)list.get(j);
					result.add(org);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null)			
				conn.closeSession();
		}
		return result;
	}
	/**
	 * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
	 * �޸����б�Ļ���Ϣ
	 * @param orgInfoForm
	 * @return
	 */
	public static boolean updateAllOrgInfo(OrgInfoForm orgInfoForm) {
		StringBuffer addsql= null;
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	boolean result=false;
    	try {
			conn = new DBConn();
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	addsql = new StringBuffer();

	    	addsql.append("update AF_ORG set PRE_ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where PRE_ORG_ID=").append(orgInfoForm.getNewOrgId());
	    	stmt.addBatch(addsql.toString().toLowerCase());

	    	addsql = new StringBuffer();
			addsql.append("update ORG set PRE_ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where PRE_ORG_ID=").append(orgInfoForm.getNewOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());			

			addsql = new StringBuffer();
			addsql.append("update AF_REPORT set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update REPORT_IN set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update DEPARTMENT set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update M_PUR_ORG set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update m_rep_range set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update m_user_grp set set_org_id=").append(orgInfoForm.getNewOrgId()).
			append(" where set_org_id=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update OPERATOR set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
	    	
			
			addsql = new StringBuffer();
			addsql.append("update report_in set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update AF_ORG set ORG_NAME='").append(orgInfoForm.getOrgName()).append("',REGION_ID='").
			append(orgInfoForm.getOrgRegion()).append("',ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update ORG set ORG_NAME='").append(orgInfoForm.getOrgName()).append("',REGION_ID='").
			append(orgInfoForm.getOrgRegion()).append("',ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update AF_COLLECT_RELATION set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update AF_TEMPLATE_ORG_RELATION set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
	    	
			stmt.executeBatch();
			result = true;
		}  catch (Throwable e1) {
			e1.printStackTrace();
			result= false;
		}finally{
			try {
				if(result == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(result);
				if(stmt != null) 
					stmt.close();
				if(connection != null)
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * ��ѯ���л�
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
	 * ��ѯ�¼����Ƿ���������
	 * @param orgId
	 * @return
	 */
	public static boolean isExistAttrOrgId(String orgId) {
		boolean result = false;
		List list = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try {
			// conn�����ʵ��
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			String hsql = "from AfCollectRelation af where af.id.collectId='" + orgId + "' and af.id.orgId in(select org.orgId from AfOrg org where org.isCollect=1)";
			Query query = session.createQuery(hsql);
			list = query.list();
			if (list != null && list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ�������Ự
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
    public static boolean isExistSameOrgId(String  orgId) {
        boolean result = false;
        List list = null;
        // 连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        try {
            // conn对象的实例化
            conn = new DBConn();
            // 打开连接开始会话
            session = conn.openSession();
            Query query = session.createQuery("from AfOrg oi where oi.orgId=?");
            query.setString(0, orgId);
            list = query.list();
            if (list != null && list.size() > 0) {
                result = true;
            }
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            // 如果连接存在，则断开，结束会话
            if (conn != null)
                conn.closeSession();
        }
        return result;
    }

    public static boolean isExistSameOrgName(String  orgName) {
        boolean result = false;
        List list = null;
        // 连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        try {
            // conn对象的实例化
            conn = new DBConn();
            // 打开连接开始会话
            session = conn.openSession();
            Query query = session
                    .createQuery("from AfOrg oi where oi.orgName=?");
            query.setString(0, orgName);
            list = query.list();
            if (list != null && list.size() > 0) {
                result = true;
            }
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            // 如果连接存在，则断开，结束会话
            if (conn != null)
                conn.closeSession();
        }
        return result;
    }
    
    public static boolean add(WebSysOrg orgInfoForm) {
        boolean result = false;
        List list = null;
        // 连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        // 查询条件HQL的生成
        try {
            // conn对象的实例化
            conn = new DBConn();
            // 打开连接开始会话
            session = conn.beginTransaction();

            /** 1 增加机构信息 */
            // 得到父节点的详细信息
            AfOrg parentOrgInfo = AFOrgDelegate.getOrgInfo(orgInfoForm.getHigherOrgId());

            AfOrg orgInfo = new AfOrg();
            orgInfo.setOrgId(orgInfoForm.getOrgId());
            orgInfo.setOrgName(orgInfoForm.getOrgName());
            orgInfo.setBeginDate(orgInfoForm.getStartDate());
            //orgInfo.setOrgType(orgInfoForm.getOrgLevel());
//			if (orgInfoForm.getOrgLevel() != null)
//				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgType()));

            if (orgInfoForm.getOrgLevel() != null){
                orgInfo.setOrgType(orgInfoForm.getOrgLevel());
                orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgLevel()));
            }

            if (orgInfoForm.getOrgAreaId() != null)
                orgInfo.setRegionId(orgInfoForm.getOrgAreaId());

//			if (orgInfoForm.getSetOrgId() != null)
//				orgInfo.setSetOrgId(orgInfoForm.getSetOrgId());

            orgInfo.setOrgOuterId(orgInfoForm.getViewOrgId());

            if (orgInfoForm.getHigherOrgId() != null
                    && !orgInfoForm.getHigherOrgId().equals("")) {
                orgInfo.setPreOrgId(orgInfoForm.getHigherOrgId());
            } else {
                orgInfo.setPreOrgId(Config.VIRTUAL_TOPBANK);
            }
            //判断虚拟机构是否需要汇总
            if (orgInfoForm.getOrgType()!=null&&!orgInfoForm.getOrgType().equals(Config.COLLECT_ORG_PARENT_ID) && !orgInfoForm.getOrgType().equals(Config.COLLECT_ORG_PARENT_QT_ID)) {
                orgInfo.setIsCollect(Long.valueOf(Config.NOT_IS_COLLECT));
            } else {
                orgInfo.setIsCollect(Long.valueOf(Config.IS_COLLECT));
            }

            session.save(orgInfo);

            // /** 2 增加机构的用户具有该机构的机构权限 */
            // UserOrgPurview uop = new UserOrgPurview();
            // UserOrgPurviewId id = new UserOrgPurviewId();
            //
            // id.setOrgId(orgInfoForm.getOrgId());
            // UserInfo ui = StrutsUserInfoDelegate.selectOne(userId);
            // id.setUserInfo(ui);
            // uop.setId(id);
            // session.save(uop);
            //
            // /** 3如果增加的是真实机构，需要增加指标集对象关系 */
            // if (orgInfo.getIsCollect().equals(
            // new Integer(Config.NOT_IS_COLLECT))) {
            // // 增加指标集对象关系
            // List lstMA = StrutsMeasureAttributeDelegate
            // .selectMAList(orgInfo);
            // MeasureOrgMapping mom = null;
            // MeasureOrgMappingId momId = null;
            // if (lstMA != null && lstMA.size() > 0) {
            // for (int i = 0; i < lstMA.size(); i++) {
            // MeasureAttribute ma = (MeasureAttribute) lstMA.get(i);
            //
            // mom = new MeasureOrgMapping();
            // momId = new MeasureOrgMappingId();
            //
            // momId.setMeasureAttribute(ma);
            // momId.setOrgId(orgInfo.getOrgId());
            //
            // mom.setId(momId);
            // session.save(mom);
            // }
            // }
            //
            // }

            session.flush();
            result = true;
        } catch (Exception e) {
            log.printStackTrace(e);
            result = false;
        } finally {
            // 如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
        
    }
    
    public static boolean delete(String orgId) {
        boolean result = true;
        // 连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        List lst = null;

        try {
            // conn对象的实例化
            conn = new DBConn();
            // 打开连接开始会话
            session = conn.beginTransaction();

            AfOrg orgInfo = (AfOrg) session.get(AfOrg.class,orgId);

            if (orgInfo == null) {
                return false;
            }
            // 如果是虚拟机构，则删除CollectId为当前机构ID的记录
            // 如果是真实机构，则删除OrgId为当前机构ID的记录
            if (orgInfo.getIsCollect().equals(Long.valueOf(Config.IS_COLLECT))) {
                // 虚拟机构
                String hql = "from AfCollectRelation cr where cr.id.collectId=?";
                Query query = session.createQuery(hql);
                query.setString(0, orgInfo.getOrgId());
                List crLst = query.list();

                if (crLst != null && crLst.size() > 0) {
                    for (int i = 0; i < crLst.size(); i++) {
                        AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
                        session.delete(cr);
                    }
                }
            } else {
                // 真实机构
                String hql = "from AfCollectRelation cr where cr.id.orgId=?";
                Query query = session.createQuery(hql);
                query.setString(0, orgInfo.getOrgId());
                List crLst = query.list();

                if (crLst != null && crLst.size() > 0) {
                    for (int i = 0; i < crLst.size(); i++) {
                        AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
                        session.delete(cr);
                    }
                }

                // // 自动删除机构和指标集对应关系
                // hql = "from MeasureOrgMapping mom where mom.id.orgId=?";
                // lst = session.createQuery(hql).setString(0,
                // orgInfo.getOrgId())
                // .list();
                // for (int i = 0; i < lst.size(); i++) {
                // session.delete(lst.get(i));
                // }
            }

            // // 自动删除该机构的所有机构权限记录
            // String hql = "from UserOrgPurview uop where uop.id.orgId=?";
            // lst = session.createQuery(hql).setString(0, orgInfo.getOrgId())
            // .list();
            // for (int i = 0; i < lst.size(); i++) {
            // session.delete(lst.get(i));
            // }


            //删除机构时，删除对应的MPurOrg中的记录
            String mpohql = "from MPurOrg mpo where mpo.comp_id.org.orgId=?";
            Query mpoquery = session.createQuery(mpohql);
            mpoquery.setString(0, orgInfo.getOrgId());
            List mpoLst = mpoquery.list();

            if (mpoLst != null && mpoLst.size() > 0) {
                for (int i = 0; i < mpoLst.size(); i++) {
                    MPurOrg mpo = (MPurOrg) mpoLst.get(i);
                    session.delete(mpo);
                }
            }

            //删除机构时，删除对应的AfTemplateOrgRelation中的记录
            String sql1 = "from AfTemplateOrgRelation ator where ator.id.orgId=?";

            Query atorquery = session.createQuery(sql1);
            atorquery.setString(0, orgInfo.getOrgId());
            List atorLst = atorquery.list();

            if (atorLst != null && atorLst.size() > 0) {
                for (int i = 0; i < atorLst.size(); i++) {
                    AfTemplateOrgRelation ator = (AfTemplateOrgRelation) atorLst.get(i);
                    session.delete(ator);
                }
            }
            String sql2 = "from AfTemplateCollRep ator where ator.id.orgId=?";
            Query atorquery2 = session.createQuery(sql2);
            atorquery2.setString(0, orgInfo.getOrgId());
            atorLst = atorquery2.list();
            if (atorLst != null && atorLst.size() > 0) {
                for (int i = 0; i < atorLst.size(); i++) {
                    AfTemplateCollRep ator = (AfTemplateCollRep) atorLst.get(i);
                    session.delete(ator);
                }
            }
            String sql3 = "from AfTemplateOuterRep ator where ator.id.orgId=?";
            Query atorquery3 = session.createQuery(sql3);
            atorquery3.setString(0, orgInfo.getOrgId());
            atorLst = atorquery3.list();
            if (atorLst != null && atorLst.size() > 0) {
                for (int i = 0; i < atorLst.size(); i++) {
                    AfTemplateOuterRep ator = (AfTemplateOuterRep) atorLst.get(i);
                    session.delete(ator);
                }
            }
            session.delete(orgInfo);
//			session.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.printStackTrace(e);
            result = false;
        } finally {
            // 如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }
}
