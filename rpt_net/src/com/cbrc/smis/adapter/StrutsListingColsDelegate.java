package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ListingColsForm;
import com.cbrc.smis.hibernate.ListingCols;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.util.FitechException;

/**
 * �嵥ʽ�����ж��ձ������
 * 
 * @author rds
 * @serialData 2005-12-12 20:37
 */
public class StrutsListingColsDelegate {
	private static FitechException log=new FitechException(StrutsListingColsDelegate.class);
	
	/**
	 * ������������
	 * 
	 * @param session HttpSession ���ӻỰ
	 * @param cols List ����Ϣ�б�
	 * @param mChildReport MChildReport
	 * @return boolean
	 */
	public static boolean insertpatch(Session session,List cols,MChildReport mChildReport){
		boolean result=true;
		
		if(session==null || cols==null || mChildReport==null) return result;
		
		try{
			for(int i=0;i<cols.size();i++){
				ListingCols persistence=new ListingCols();
				ListingColsForm form=(ListingColsForm)cols.get(i);
				form.setChildRepId(mChildReport.getComp_id().getChildRepId());
				form.setVersionId(mChildReport.getComp_id().getVersionId());
				TranslatorUtil.copyVoToPersistence(persistence,form);
				session.save(persistence);
			}
			result=true;
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}
		
		return result;
	}
	
	/**
	 * �����ӱ���ID���汾�ź����ݱ�������жϴ����Ƿ����
	 * 
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
	 * @param dbColName String ����
	 * @return boolean ���ڣ�����true;���򣬷���false
	 */
	public static boolean isColNameExist(String childRepId,String versionId,String dbColName){
		boolean result=false;
		
		if(childRepId=="" || versionId=="" || dbColName=="") return result;
		
		DBConn conn=null;
		
		try{
			String hql="select count(*) from ListingCols lc where " +  
				"lc.comp_id.childRepId='" + childRepId + "' and " +
				"lc.comp_id.versionId='" + versionId + "' and " +
				"lc.dbColName='" + dbColName + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				result=true;
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return result;
	}
	
	/**
	 * �����ӱ���ID���汾�ź����ݱ�������жϴ����Ƿ����
	 * 
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
	 * @return List
	 */
	public static List findAll(String childRepId,String versionId){
		List resList=null;
		
		if(childRepId==null && versionId==null) return resList;
		
		DBConn conn=null;
		
		try{
			String hql="from ListingCols lc where " +  
				"lc.comp_id.childRepId='" + childRepId + "' and " +
				"lc.comp_id.versionId='" + versionId + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					ListingCols listingCols=(ListingCols)list.get(i);
					ListingColsForm form=new ListingColsForm();
					
					TranslatorUtil.copyPersistenceToVo(listingCols,form);
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**
	 * �����ӱ���ID���汾�Ż�ȡ��ǰ��������е���
	 * 
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
	 * @return List
	 */
	public static List findCols(String childRepId,String versionId){
		ArrayList cols=null;
		
		List resList=findAll(childRepId,versionId);
		if(resList!=null && resList.size()>0){
			cols=new ArrayList();
			ListingColsForm form=null;
			for(int i=0;i<resList.size();i++){
				form=(ListingColsForm)resList.get(i);
				if(form.getDbColName()!=null && !form.getDbColName().equals("")) 
					cols.add(form.getDbColName().toUpperCase());
			}
		}
		
		return cols;
	}
}
