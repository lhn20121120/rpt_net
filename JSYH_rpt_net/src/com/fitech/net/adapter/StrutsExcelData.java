package com.fitech.net.adapter;

import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateShape;

public class StrutsExcelData {
	// Catch到本类的抛出的所有异常
	private static FitechException log = new FitechException(
			StrutsExcelData.class);

	public static List getData1(Map map) {
		DBConn conn = null;
		Session session = null;
		
		String repInId=(String)map.get("repInId");
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from ReportInInfo r left join fetch r.MCell where r.comp_id.repInId="+repInId;

			Query q = session.createQuery(hql);
			
			return q.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static List getData2(Map map) {
		DBConn conn = null;
		Session session = null;
		
		String repInId=(String)map.get("repInId");
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfPbocreportdata r where r.id.repId="+repInId;

			Query q = session.createQuery(hql);
			
			return q.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	/**
	 * 根据报表Id查询报表信息
	 * @param repId
	 * @return
	 */
	public static AfReport getAfReport(String repId){
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfReport a where a.repId="+Long.parseLong(repId);
			
			return (AfReport) session.createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static AfCellinfo getCellInfo(Long cellId){
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfCellinfo a where a.cellId="+cellId;

			return (AfCellinfo) session.createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static List getData3(Map map) {
		DBConn conn = null;
		Session session = null;
		
		String repInId=(String)map.get("repInId");
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfOtherreportdata r where r.id.repId="+repInId;

			Query q = session.createQuery(hql);
			
			return q.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static List<AfTemplateShape> getTemplateDate(String versionId,String childRepId){
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "select a from AfTemplateShape a where a.id.templateId='"+childRepId+"' and a.id.versionId='"+versionId+"'";
			Query q = session.createQuery(hql);
			return q.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static AfTemplateShape getTemplate(AfTemplateShape af){
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfTemplateShape a where a.id.templateId='"+af.getId().getTemplateId()+"' and a.id.versionId='"+af.getId().getVersionId()+"' and a.id.cellName='"+af.getId().getCellName()+"'";
			return (AfTemplateShape)session.createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static AfTemplate getTemplateSimple(String versionId,String childRepId){
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from  AfTemplate a where a.id.templateId='"+childRepId+"' and a.id.versionId='"+versionId+"'";
			return (AfTemplate)session.createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.closeSession();
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return null;
	}
	
	public static boolean deleteAFlist(List<AfTemplateShape> list){
		DBConn conn = null;
		boolean res=false;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					AfTemplateShape as=getTemplate(list.get(i));
					if(as!=null){
						session.delete(as);
					}
				}
				res=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			res=false;
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.endTransaction(res);
				if (session != null)
					session.close();
			} catch (HibernateException e) {
				log.printStackTrace(e);
			}
		}
		return res;
	}
}
