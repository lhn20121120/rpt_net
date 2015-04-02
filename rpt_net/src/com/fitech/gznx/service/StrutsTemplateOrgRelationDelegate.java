package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfTemplateCollRep;
import com.fitech.gznx.po.AfTemplateOrgRelation;
import com.fitech.gznx.po.AfTemplateOrgRelationId;
import com.fitech.gznx.po.AfTemplateOuterRep;

public class StrutsTemplateOrgRelationDelegate {

	private static FitechException log = new FitechException(
			StrutsTemplateOrgRelationDelegate.class);

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-27 Ӱ�����AfTemplateOrgRelation
	 * �����ӱ���ID�Ͱ汾�Ż�ȡ�䱨�ͷ�Χ��Ϣ�б�
	 * 
	 * @author rds
	 * @date 2005-12-25
	 * 
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return List ����Ϣ�б�����null
	 */
	public static List findAll(String childRepId, String versionId) {
		List resList = new ArrayList();
		;

		if (childRepId == null || versionId == null)
			return null;

		DBConn conn = null;

		try {
			String hql = "from AfTemplateOrgRelation mrr where mrr.id.templateId='"
					+ childRepId + "' and mrr.id.versionId='" + versionId + "'";

			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql);
			resList = query.list();

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return resList;
	}

	/**
	 *�Ƴ�orgId�¼�����
	 * 
	 * @param childRepid
	 * @return
	 * @author Administrator xsf
	 */
	public static boolean removeLowerOrg(String childRepId, String versionId,
			String orgId) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			String hql = " from AfTemplateOrgRelation mrr where";
			if (versionId != null && !versionId.equals("")) {
				hql += " mrr.id.versionId='" + versionId + "'";
			}
			if (childRepId != null && !childRepId.equals("")) {
				hql += " and mrr.id.templateId='" + childRepId + "'";
			}
			session.delete(hql.toString());

			session.flush();
			result = true;

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/** ��һ��ģ�����ñ��ͷ�Χ */
	public static void updateBSFW(String childRepId, String versionId,
			ArrayList orgIds) {
		if (childRepId == null)
			return;
		if (versionId == null)
			return;
		if (orgIds == null)
			return;
		if (childRepId.equals(""))
			return;
		if (versionId.equals(""))
			return;
		if (orgIds.size() == 0)
			return;
		DBConn conn = new DBConn();
		Session session = conn.beginTransaction();
		AfTemplateOrgRelationId comp_id = new AfTemplateOrgRelationId();
		comp_id.setTemplateId(childRepId);
		comp_id.setVersionId(versionId);

		if (orgIds != null && orgIds.size() > 0) {
			for (int i = 0; i < orgIds.size(); i++) {
				AfTemplateOrgRelation mrr = new AfTemplateOrgRelation();
				comp_id.setOrgId((String) orgIds.get(i));
				mrr.setId(comp_id);
				// ����Ƿ����ظ���¼
				if (!recordIsExist(comp_id)) {
					try {
						// System.out.println("record "+record+" is not exist,can insert!!!!!");
						session.save(mrr);
						session.flush();
					} catch (HibernateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			conn.endTransaction(true);
			if (conn != null)
				conn.closeSession();
		}
	}

	 /**
     * ͨ��id�õ�Ψһ�Ķ���
     * @param childRepId
     * @param versionId
     * @return
     */
    public static AfTemplateOrgRelation findById(AfTemplateOrgRelationId comp_id)
    {
    	boolean result=false;
    	DBConn conn = new DBConn();
		Session session = conn.openSession();
		StringBuffer hql = new StringBuffer();
		AfTemplateOrgRelation af = null;
		hql.append("from AfTemplateOrgRelation mrr where mrr.id.orgId='");
		hql.append(comp_id.getOrgId());
		hql.append("' and mrr.id.templateId='");
		hql.append(comp_id.getTemplateId());
		hql.append("' and mrr.id.versionId='");
		hql.append(comp_id.getVersionId());
		hql.append("'");
		List list = null;
		try {
			list = session.find(hql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
    	if(list!=null&&list.size()>0){
    		af = (AfTemplateOrgRelation) list.get(0);
    	}
    	return af;
    }
    
	
	
	
	
	/** ����Ƿ����ظ���¼ */
	public static boolean recordIsExist(AfTemplateOrgRelationId comp_id) {
		boolean flag = true;
		if (comp_id == null)
			return flag;
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		StringBuffer hql = new StringBuffer();
		hql.append("from AfTemplateOrgRelation mrr where mrr.id.orgId='");
		hql.append(comp_id.getOrgId());
		hql.append("' and mrr.id.templateId='");
		hql.append(comp_id.getTemplateId());
		hql.append("' and mrr.id.versionId='");
		hql.append(comp_id.getVersionId());
		hql.append("'");

		List list = null;
		try {
			list = session.find(hql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		if (list == null) {
			flag = false;
		}
		if (list.size() == 0) {
			flag = false;
		}
		return flag;
	}

	public static List getPreOrgRepRange(String orgId, String reportFlg) {
		List retVals = null;
		DBConn conn = null;
		try {
			String hql = "select  t.id.templateId,t.id.versionId,t.templateName"
					+ " from AfTemplate t where  t.templateType=" + reportFlg +
					// " and mrr.id.orgId=(select on.preOrgId from OrgNet on where on.orgId='"
					// + orgId
					" order by t.id.templateId,t.id.versionId";
			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();
			if (list != null && list.size() > 0) {
				retVals = new ArrayList();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Object[] object = (Object[]) iter.next();
					retVals.add(new LabelValueBean("[" + (String) object[0]
							+ "_" + (String) object[1] + "]"
							+ (String) object[2], (String) object[0] + "-"
							+ (String) object[1] + "-2"));
				}
			}
		} catch (HibernateException he) {
			retVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			retVals = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22 �鿴�û����Ƿ����ù����ͷ�Χ
	 * 
	 * @param orgId
	 * @return
	 */
	public static int getRepRangeCountByOrg(String orgId) {
		int result = 0;
		DBConn conn = null;
		Session session = null;

		try {
			if (orgId != null) {
				conn = new DBConn();
				session = conn.openSession();

				Query query = session
						.createQuery("select count(*) from AfTemplateOrgRelation mrr where mrr.id.orgId='"
								+ orgId + "'");
				List list = query.list();
				if (list != null && list.size() != 0) {
					result = ((Integer) list.get(0)).intValue();
				}
			}
		} catch (Exception e) {
			result = 0;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * 
	 * @param orgId
	 * @return
	 */
	public static boolean deleteRepRangeByOrg(String orgId) {

		boolean result = false;
		DBConn conn = null;
		Session session = null;

		try {
			if (orgId != null) {

				conn = new DBConn();
				session = conn.beginTransaction();

				String hql = "from AfTemplateOrgRelation mrr where mrr.id.orgId='"
						+ orgId + "'";
				session.delete(hql);
				result = true;
			}
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * 
	 * @param reportFlg
	 * @return
	 */
	public static List getPreOrgRepRange(String reportFlg) {
		List retVals = null;
		DBConn conn = null;
		try {
			String hql = "select  t.id.templateId,t.id.versionId,t.templateName"
					+ " from AfTemplate t where  t.templateType=" + reportFlg +
					// " and mrr.id.orgId=(select on.preOrgId from OrgNet on where on.orgId='"
					// + orgId
					" order by t.id.templateId,t.id.versionId";
			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();
			if (list != null && list.size() > 0) {
				retVals = new ArrayList();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Object[] object = (Object[]) iter.next();
					retVals.add(new LabelValueBean("[" + (String) object[0]
							+ "_" + (String) object[1] + "]"
							+ (String) object[2], (String) object[0] + "-"
							+ (String) object[1] + "-1"));
				}
			}
		} catch (HibernateException he) {
			retVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			retVals = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	}

	/** ��һ��ģ�����ñ��ͷ�Χ */
	public static boolean updateBSFW(String childRepId, String versionId) {
		boolean result = false;
		List list = new ArrayList();
		if (childRepId == null)
			return result;
		if (versionId == null)
			return result;
		if (childRepId.equals(""))
			return result;
		if (versionId.equals(""))
			return result;
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			//ɾ�����ж�Ӧ�Ķ���
			deleteAllRalation(childRepId, versionId ,session);
			//��ģ����ܹ�ϵ���в�ѯ�����м�¼
			list = StrutsTemplateOrgCollRelationDelegate.findAll(childRepId,
					versionId );
			AfTemplateOrgRelation vo = null;
			AfTemplateOrgRelationId avo = null;
			Set set = new HashSet();
			if (list != null && list.size() > 0) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					AfTemplateCollRep object = (AfTemplateCollRep) iterator
							.next();
					avo = new AfTemplateOrgRelationId();
					avo.setOrgId(object.getId().getOrgId());
					avo.setTemplateId(object.getId().getTemplateId());
					avo.setVersionId(object.getId().getVersionId());
//					vo = new AfTemplateOrgRelation();
//					vo.setId(avo);
					set.add(avo);
//					session.sa(vo);
				}
			}
			list = StrutsTemplateOrgOuterRelationDelegate.findAll(childRepId,
					versionId);
//			session.clear();
			if (list != null && list.size() > 0) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					AfTemplateOuterRep object = (AfTemplateOuterRep) iterator
							.next();
					avo = new AfTemplateOrgRelationId();
					avo.setOrgId(object.getId().getOrgId());
					avo.setTemplateId(object.getId().getTemplateId());
					avo.setVersionId(object.getId().getVersionId());
					//�ж��Ƿ��Ѿ�����
//					vo = findById(avo);
//					if(vo==null){
//						vo = new AfTemplateOrgRelation();
//						vo.setId(avo);
//						set.add(vo);
//					}
					set.add(avo);
				}
			}
			System.out.println(set.size());
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				AfTemplateOrgRelationId object = (AfTemplateOrgRelationId) iterator.next();
				vo = new AfTemplateOrgRelation();
				vo.setId(object);
				session.save(vo);
			}
			result = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * ����ģ��ţ��汾�ţ�ɾ����Ӧ�����л������͹�ϵ
	 * 
	 * @param childRepId
	 * @param versionId
	 * @param orgId
	 * @return Administrator 2013-3-21 ����05:43:57 boolean
	 */
	public static boolean deleteAllRalation(String childRepId, String versionId, Session session ) {
		boolean result = false;
//		DBConn conn = new DBConn();
//		Session session = conn.beginTransaction();
		try {
			String hql = " from AfTemplateOrgRelation mrr where";
			if (versionId != null && !versionId.equals("")) {
				hql += " mrr.id.versionId='" + versionId + "'";
			}
			if (childRepId != null && !childRepId.equals("")) {
				hql += " and mrr.id.templateId='" + childRepId + "'";
			}
			session.delete(hql.toString());

			session.flush();
			result = true;

		} catch (HibernateException he) {
			log.printStackTrace(he);
			return result;
		} catch (Exception e) {
			log.printStackTrace(e);
			return result;
		} finally {
//			if (conn != null)
//				conn.endTransaction(result);
		}
		return result;
	}
}
