package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfTemplateValiSche;
import com.fitech.gznx.po.AfValidateScheme;

public class AfTemplateValiScheDelegate {

	private static FitechException log = new FitechException(
			AfTemplateValiScheDelegate.class);
	/**
	 * ��ѯ����У������
	 * @return
	 */
	public static List<AfValidateScheme> findAfValidateScheme(){
		DBConn dbconn = null;
		Session session = null;
		List<AfValidateScheme> list = new ArrayList<AfValidateScheme>();
		try {
			String sql = "from AfValidateScheme ";
			dbconn = new DBConn();
			session  = dbconn.openSession();
			list = session.createQuery(sql).list();
		} catch (HibernateException e) {
			log.printStackTrace(e);
		}finally{
			if(dbconn!=null){
				dbconn.closeSession();
			}
		}
		return list;
	}
	/**
	 * ����ģ���У������
	 * @param template
	 */
	public static void addAfTemplateValiSche(AfTemplateValiSche template){
		boolean result = false;
		DBConn dbconn = null;
		Session session = null;
		try {
			dbconn = new DBConn();
			session = dbconn.beginTransaction();
			session.save(template);
			result = true;
		} catch (HibernateException e) {
			log.printStackTrace(e);
		}finally{
			if(dbconn!=null){
				dbconn.endTransaction(result);
			}
		}
	}
	/**
	 * ��ѯָ����ģ��
	 * @param templateId
	 * @return
	 */
	public static AfTemplateValiSche findAfTemplateValiSche(String templateId,String versionId){
		DBConn dbconn = null;
		Session session = null;
		AfTemplateValiSche temp = null;
		try {
			dbconn = new DBConn();
			session = dbconn.openSession();
			String hql = "from AfTemplateValiSche tv where tv.id.templateId='"+templateId+"' and tv.id.versionId='"+versionId+"'";
			temp = (AfTemplateValiSche) session.createQuery(hql).uniqueResult();
		} catch (HibernateException e) {
			log.printStackTrace(e);
		}finally{
			if(dbconn!=null){
				dbconn.closeSession();
			}
		}
		return temp;
	}
	/**
	 * �޸�ָ��ģ���У������
	 * @param template
	 */
	public static void updateAfTemplateValiSche(AfTemplateValiSche template){
		boolean result = false;
		DBConn dbconn = null;
		Session session = null;
		try {
			dbconn = new DBConn();
			session = dbconn.beginTransaction();
			session.update(template);
			result = true;
		} catch (HibernateException e) {
			log.printStackTrace(e);
		}finally{
			if(dbconn!=null){
				dbconn.endTransaction(result);
			}
		}
	}
}
