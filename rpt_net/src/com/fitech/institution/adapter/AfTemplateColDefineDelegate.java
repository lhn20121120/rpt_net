package com.fitech.institution.adapter;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.institution.po.AfTemplateColDefine;

public class AfTemplateColDefineDelegate extends DaoModel {

	private static FitechException log = new FitechException(
			AfTemplateColDefineDelegate.class);

	public static List<AfTemplateColDefine> findById(String templateId) {
		DBConn conn = null;
		Session session = null;
		List<AfTemplateColDefine> list = null;
		try {
			String hql = " from AfTemplateColDefine t where t.id.templateId='"
					+ templateId + "'";
			conn = new DBConn();

			session = conn.beginTransaction();
			list = session.createQuery(hql).list();
			conn.endTransaction(true);
			return list;
		} catch (Exception e) {
			log.printStackTrace(e);
			return null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
	}

	public static boolean save(List<AfTemplateColDefine> list) {
		DBConn conn = null;
		Session session = null;
		boolean flag = true;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				AfTemplateColDefine afTemplateColDefind = (AfTemplateColDefine) iterator
						.next();
				session.save(iterator);
			}
			// BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			log.printStackTrace(e);
			flag = false;
		} finally {
			conn.endTransaction(flag);
			if (conn != null)
				conn.closeSession();
		}
		return flag;
	}

	public static boolean saveOrUpate(AfTemplateColDefine ad) {
		DBConn conn = null;
		boolean flag = true;
		try {
			conn = new DBConn();
			conn.beginTransaction();
			 Session session = conn.openSession();
//			AfTemplateColDefine o = new AfTemplateColDefine();
//			o.setId(ad.getId());
//			o.setColName(ad.getColName());
			 session.delete(" from AfTemplateColDefine t where t.colName='"+ad.getColName()+"'");
			 session.flush();
			 AfTemplateColDefine isExit = (AfTemplateColDefine) session.get(AfTemplateColDefine.class, ad.getId());
			if(isExit==null){
				session.save(ad);
			}else{
				isExit.setColName(ad.getColName());
				session.update(isExit);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			flag = false;
		} finally {
			if (conn != null)
				conn.endTransaction(flag);
				conn.closeSession();
		}
		return flag;
	}

}
