package com.fitech.specval.adapter;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.ExchangeRate;
import com.fitech.specval.form.SpecialValForm;
import com.fitech.specval.po.SpecValidateInfo;

public class StrutsSpecValidateInfoDelegate {
	
	private static FitechException log = new FitechException(
			StrutsSpecValidateInfoDelegate.class);
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����SpecValidateInfo
	 * ��ҳ�����ѯУ��
	 * @param rate				ExchangeRate		����
	 * @param apartPage			ApartPage			��ҳ����
	 * @return		List
	 */
	public static List getSpecValidateInfos(SpecialValForm dataForm ,ApartPage apartPage){
		
		List specValLst = null;
		DBConn dbConn = null;
		Session session = null;
		
		try {
			dbConn = new DBConn();
			session = dbConn.openSession();
			StringBuffer sbfHql = new StringBuffer("from SpecValidateInfo t ");
			StringBuffer sbWhere=new StringBuffer("");
			
			if(dataForm != null){
				if(dataForm.getValName() != null && !dataForm.getValName().trim().equals("")){
					sbWhere.append(sbWhere.toString().equals("")?"":" and ")
						.append(" t.valName='").append(dataForm.getValName()).append("'");
				}

				if(!sbWhere.toString().equals(""))
					sbfHql.append(" where ").append(sbWhere.toString());
			}
			
			Query query = session.createQuery("select count(t.speValId) " + sbfHql.toString());
			
			specValLst = query.list();
			
			int count=0;
			if(specValLst!=null && specValLst.size()==1){
				count=((Integer)specValLst.get(0)).intValue();
			}
			
			int offset=0;
			if(apartPage!=null){
				offset = (apartPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
				apartPage.setCount(count);
			}
			
			sbfHql.append(" order by t.speValId desc");
			
			query = session.createQuery(sbfHql.toString());
			query.setFirstResult(offset);
			query.setMaxResults(Config.PER_PAGE_ROWS);
			
			specValLst = query.list();
			
		} catch (Exception e) {
			specValLst = null;
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)	dbConn.closeSession();
		}
		return specValLst;
	}
	
	/**
	 * �������޸�һ��У��
	 * @param exchangeRate		ExchangeRate		����
	 * @return	�������޸ĳɹ�����	true	ʧ�ܷ��� false
	 */
	public static boolean saveOrUpdateSpecVal(SpecValidateInfo specValInfo,String todo){
		boolean insert = false;
		DBConn dbConn = null;
		Session session = null;
		try {
			if(specValInfo != null){
				dbConn = new DBConn();
				session = dbConn.beginTransaction();
				
				if(todo.trim().equals("new")){
						session.save(specValInfo);
						session.flush();
						insert = true;
				}else{
					session.update(specValInfo);
					session.flush();
					insert = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.endTransaction(insert);
		}
		return insert;
	}

	/**
	 * ͨ�����ʵ��������һ���
	 * @param pId		Long	����Id
	 * @return	ExchangeRate
	 */
	public static SpecValidateInfo getSpecValById(Long pId){
		
		SpecValidateInfo specValInfo = null;
		
		DBConn dbConn = null;
		Session session = null;
		
		try {
			if(pId != null){
				dbConn = new DBConn();
				session = dbConn.openSession();
				String hql = "from SpecValidateInfo e where e.speValId=:speValId";
				Query query = session.createQuery(hql);
				query.setLong("speValId",pId.longValue());
				List list = query.list();
				if(list != null) specValInfo = (SpecValidateInfo)list.get(0);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.closeSession();
		}
		return specValInfo;
	}
	
	/**
	 * ����ɾ��У�����
	 * @param pId		Long[]			 У��Id ����
	 * @return	�ɹ�����	true 	ʧ�ܷ���	false
	 */
	public static boolean deleteSpecValById(String[] pId){
		
		boolean delete = false;
		
		DBConn dbConn = null;
		Session session = null;
		
		try {
			if (pId != null) {
				dbConn = new DBConn();
				session = dbConn.beginTransaction();
				
				int length = pId.length;
				
				for (int i = 0; i < length; i++) {
					//��ȡҪɾ���Ļ���
					SpecValidateInfo specValInfo = (SpecValidateInfo) session.get(SpecValidateInfo.class, new Long(pId[i]));
					if (specValInfo != null) {
						session.delete(specValInfo);
					} else {
						delete = false;
						break;
					}
				}
				
				if (!delete) {
					session.flush();
					delete = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.endTransaction(delete);
		}
		return delete;
	}
}
