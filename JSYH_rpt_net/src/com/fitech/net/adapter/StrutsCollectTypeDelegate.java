
package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.CollectTypeForm;
import com.fitech.net.hibernate.CollectType;
import com.fitech.net.hibernate.OrgNet;
/**
 * @StrutsCollectTypeDelegate  ���ܷ�ʽDelegate
 * @2006-11-15
 * @author jcm
 */
public class StrutsCollectTypeDelegate {
	
	private static FitechException log=new FitechException(StrutsCollectTypeDelegate.class);
	   
	/**
	 * ���ܷ�ʽ������ѯ
	 * 
	 * @param collectTypeForm CollectTypeForm ��ѯ������
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static int select(CollectTypeForm collectTypeForm){
		int count = 0;			   
	
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
//		��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.collectId,ct.id.collectName,ct.id.orgId from CollectType ct");							   
		StringBuffer where = new StringBuffer("");
	 	   
		if (collectTypeForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if(collectTypeForm.getCollectName() != null && !collectTypeForm.getCollectName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ct.id.collectName like '%" + collectTypeForm.getCollectName() + "%'");			
			if(collectTypeForm.getOrgId() != null && !collectTypeForm.getOrgId().equals(""))		
				where.append((where.toString().equals("")?"":" and ") + "ct.id.orgId = '" + collectTypeForm.getOrgId() + "'");	   
		}
		try {    	
			hql.append((where.toString().equals("")?"":" where ") + where.toString());    	  

			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  		 
			List list = query.list();
			if (list != null && list.size() > 0){    		  
				count = list.size();
			}      
		}catch(HibernateException he){    	  
			count = 0;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			count = 0;  
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		}         
		return count;   
	}
	
	/**
	 * ���ܷ�ʽ��ѯ
	 * 
	 * @param collectTypeForm CollectTypeForm ��ѯ������
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static List select(CollectTypeForm collectTypeForm,int offset,int limit){
		//List���ϵĶ��� 	   
		List refVals = null;				   
	
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.collectId,ct.id.collectName,ct.id.orgId from CollectType ct");							   
		StringBuffer where = new StringBuffer("");
	 	   
		if (collectTypeForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if(collectTypeForm.getCollectName() != null && !collectTypeForm.getCollectName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ct.id.collectName like '%" + collectTypeForm.getCollectName() + "%'");			
			if(collectTypeForm.getOrgId() != null && !collectTypeForm.getOrgId().equals(""))		
				where.append((where.toString().equals("")?"":" and ") + "ct.id.orgId = '" + collectTypeForm.getOrgId() + "'");	   
		}
		try {    	
			hql.append((where.toString().equals("")?"":" where ") + where.toString());    	  
			hql.append(" order by ct.id.orgId");
    	  
			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			query.setFirstResult(offset).setMaxResults(limit);
		 
			List list = query.list();
			if (list != null){    		  
				refVals = new ArrayList();    		 
				//ѭ����ȡ���ݿ����������¼		     
				for(Iterator it = list.iterator(); it.hasNext(); ) {	
					Object[] object = (Object[])it.next();
					CollectTypeForm collectTypeFormTemp = new CollectTypeForm();		    	
					
					collectTypeFormTemp.setCollectId((Integer)object[0]);
					collectTypeFormTemp.setCollectName((String)object[1]);
					collectTypeFormTemp.setOrgId((String)object[2]);
					
					if(collectTypeFormTemp.getOrgId() != null){		        
						OrgNet orgNet = StrutsOrgNetDelegate.selectOne(collectTypeFormTemp.getOrgId());		        	 
						if(orgNet != null) collectTypeFormTemp.setOrgName(orgNet.getOrgName());		        
					}		         
					if(collectTypeFormTemp.getCollectOrgId() != null){		        	
						OrgNet collectOrgNet = StrutsOrgNetDelegate.selectOne(collectTypeFormTemp.getCollectOrgId());		        	
						if(collectOrgNet != null) collectTypeFormTemp.setCollectOrgName(collectOrgNet.getOrgName());		         
					}		         
					
					refVals.add(collectTypeFormTemp);		     
				}    	   
			}      
		}catch(HibernateException he){    	  
			refVals = null;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			refVals = null;      	  
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		}         
		return refVals;   
	}
	
	/**
	 * ���ݻ����ͻ��ܷ�ʽID���ҳ���ѡ��ı�����Ϣ
	 * @param orgId
	 * @param collectId
	 * @return
	 */
	public static List searchSelectedCollectReport(String orgId,Integer collectId){	   
		List refVals = null;				   
	
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		if (orgId == null || collectId == null) return refVals;
		
		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.childRepId,ct.id.versionId from CollectType ct where ct.id.orgId='" 
				+ orgId + "' and ct.id.collectId=" + collectId + "");							   
		
		try {    	
			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			
			List list = query.list();
			if (list != null && list.size() > 0){    		  
				refVals = new ArrayList();    		 
				//ѭ����ȡ���ݿ����������¼		     
				for(Iterator it = list.iterator(); it.hasNext(); ) {	
					Object[] object = (Object[])it.next();
					
					String childRepId = (String)object[0];
					String versionId = (String)object[1];
					
					MChildReportForm mChildReportForm = new StrutsMChildReportDelegate().getMChildReport(childRepId,versionId);					
					refVals.add(mChildReportForm);		     
				}    	   
			}      
		}catch(HibernateException he){    	  
			refVals = null;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			refVals = null;      	  
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		}         
		return refVals; 
	}
	
	/**
	 * ���ݻ����ͻ��ܷ�ʽID���ҳ���ѡ��Ļ�����Ϣ
	 * @param orgId
	 * @param collectId
	 * @return
	 */
	public static List searchSelectedCollectOrg(String orgId,Integer collectId){
		List refVals = null;				   
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		if (orgId == null || collectId == null) return refVals;
		
		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.collectOrgId from CollectType ct where ct.id.orgId='" 
				+ orgId + "' and ct.id.collectId=" + collectId + "");							   
		
		try {    	
			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			
			List list = query.list();
			if (list != null && list.size() > 0){    		  
				refVals = new ArrayList();    		 
				//ѭ����ȡ���ݿ����������¼		     
				for(Iterator it = list.iterator(); it.hasNext(); ) {	
					String collectOrgId = (String)it.next();
					OrgNet orgNet = StrutsOrgNetDelegate.selectOne(collectOrgId);									
					refVals.add(orgNet);		     
				}    	   
			}      
		}catch(HibernateException he){    	  
			refVals = null;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			refVals = null;      	  
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		}         
		return refVals; 
	}
	/**
	 * ��ѯ�û�����ָ�����ܷ�ʽ�Ƿ��Ѿ�����
	 * @param collectTypeForm
	 * @return
	 */
	public static boolean isCollectTypeExist(CollectTypeForm collectTypeForm){
		boolean bool = false;
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		if(collectTypeForm == null) return bool;
		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("from CollectType ct where ct.id.collectName='" + collectTypeForm.getCollectName() 
				+ "' and ct.id.orgId='" + collectTypeForm.getOrgId() + "'");	
		
		try {
			conn = new DBConn();    	 			
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			List list = query.list();
			
			if (list != null && list.size() > 0){    		  
				bool = true;
			}      
		}catch(HibernateException he){    	  
			bool = false;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			bool = false;  
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		} 
		return bool;
	}
	
	public static Integer getMaxCollectTypeId(){
		Integer collectId = null;
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		StringBuffer hql = new StringBuffer("select max(ct.id.collectId) from CollectType ct");	
		
		try {
			conn = new DBConn();    	 			
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			List list = query.list();
			
			if (list != null && list.size() > 0){ 
				if(list.get(0) == null)
					collectId = new Integer(1);
				else collectId = new Integer(((Integer)list.get(0)).intValue() + 1);
			}else
				collectId = new Integer(1);
		}catch(HibernateException he){    	  
			collectId = null;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			collectId = null;
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		} 
		return collectId;
	}
	
	public static boolean create(List collectTypeList){
		boolean bool = false;
		
		//���Ӷ���ĳ�ʼ��		   
		DBConn conn=null;		
		//�Ự����ĳ�ʼ��		
		Session session=null;
		
		if(collectTypeList == null) return bool;
		 
		try{			   
			//ʵ�������Ӷ���			   
			conn =new DBConn();			
			//�Ự����Ϊ���Ӷ������������			
			session=conn.beginTransaction();
			
			for(int i=0;i<collectTypeList.size();i++){
				CollectTypeForm collectTypeForm = (CollectTypeForm)collectTypeList.get(i);
				CollectType collectTypePersistence = new CollectType();
				
				TranslatorUtil.copyVoToPersistence(collectTypePersistence,collectTypeForm);
				
				session.save(collectTypePersistence);
			}			
			session.flush();
			bool = true;
		}catch(HibernateException e){			
			//�־ò���쳣����׽			   
			bool = false;	
			e.printStackTrace();
			log.printStackTrace(e);		   
		}catch (Exception e) {
			bool = false;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{		
			//�������״̬��,��Ͽ�,��������,����			
			if(conn!=null) conn.endTransaction(bool);		   
		} 
		return bool;
	}
	
	/**
	 * ���ݻ������ӱ���ID�ҳ�ָ��������ָ�������м��ֻ��ܷ�ʽ
	 * @param orgId
	 * @param childRepId
	 * @return
	 */
	public static List selectCollectTypes(String orgId,String childRepId,String versionId){
		List resList = null;
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.collectId,ct.id.collectName from CollectType ct where ct.id.orgId='" 
				+ orgId + "' and ct.id.childRepId='" + childRepId + "' and ct.id.versionId='" + versionId + "'");	
		
		try {
			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			List list = query.list();
			
			if (list != null){    		  
				resList = new ArrayList();    		 
				//ѭ����ȡ���ݿ����������¼		     
				for(Iterator it = list.iterator(); it.hasNext(); ) {	
					Object[] object = (Object[])it.next();
					CollectTypeForm collectTypeFormTemp = new CollectTypeForm();		    	
					
					collectTypeFormTemp.setCollectId((Integer)object[0]);
					collectTypeFormTemp.setCollectName((String)object[1]);
				
					resList.add(collectTypeFormTemp);		     
				}    	   
			}  
		}catch(HibernateException he){    	  
			resList = null;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			resList = null;
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		} 
		return resList;
	}
	
	/**
	 * ���ݻ��ܷ�ʽID�ҳ��û��ܷ�ʽ����Щ������������Ҫ����
	 * @param collectId
	 * @return
	 */
	public static String selectCollectOrgIds(Integer collectId){
		String collectOrgIds = "";
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.collectOrgId from CollectType ct where ct.id.collectId=" + collectId);	
		
		try {
			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			List list = query.list();
			
			if (list != null){    		   		 
				//ѭ����ȡ���ݿ����������¼		     
				for(Iterator it = list.iterator(); it.hasNext(); ) {	
					String orgId = (String)it.next();
					collectOrgIds = collectOrgIds.equals("") ? "'" + orgId + "'" : collectOrgIds + "," + "'" +orgId + "'";     
				}    	   
			}  
		}catch(HibernateException he){    	  
			collectOrgIds = "";
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			collectOrgIds = "";
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		} 
		return collectOrgIds;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @param collectId
	 * @return
	 */
	public static CollectTypeForm selectCollectType(Integer collectId){
		CollectTypeForm collectTypeForm = null;
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;	   
	   
		StringBuffer hql = new StringBuffer("select distinct ct.id.collectName from CollectType ct where ct.id.collectId=" + collectId);	
		
		try {
			//conn�����ʵ����		      	  
			conn = new DBConn();    	 
			//�����ӿ�ʼ�Ự    	  
			session = conn.openSession();
		  
			Query query = session.createQuery(hql.toString());		  
			List list = query.list();
			
			if (list != null && list.size() > 0){
				collectTypeForm = new CollectTypeForm();
				collectTypeForm.setCollectName((String)list.get(0));
				collectTypeForm.setCollectId(collectId);
			}  
		}catch(HibernateException he){    	  
			collectTypeForm = null;
			he.printStackTrace();    	 
			log.printStackTrace(he);     
		}catch(Exception e){    	  
			collectTypeForm = null;
			e.printStackTrace();    	 
			log.printStackTrace(e);      
		}finally{    	  
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn != null) conn.closeSession();      
		} 
		return collectTypeForm;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @param collectTypeForm
	 * @return
	 */
	public static boolean remove(CollectTypeForm collectTypeForm){
		boolean bool = false;
		
		//���ӺͻỰ����ĳ�ʼ��
		DBConn conn=null;
		Session session=null;
		
		if(collectTypeForm == null || collectTypeForm.getCollectId() == null)
			return bool;
		
		try{
			String hql = "from CollectType ct where ct.id.collectId=" + collectTypeForm.getCollectId();
			//���Ӷ���ͻỰ�����ʼ��
			conn = new DBConn();
			session = conn.beginTransaction();
			
			Query query = session.createQuery(hql);
			List list = query.list();
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					CollectType ct = (CollectType)list.get(i);
					session.delete(ct);
				}				
			}			
			session.flush();
			
			bool = true;
		}catch(HibernateException he){
			bool = false;
			he.printStackTrace();
			//��׽������쳣,�׳�			
			log.printStackTrace(he);
		}finally{
			//�����������Ͽ����ӣ������Ự������			  
			if (conn!=null) conn.endTransaction(bool);		   
		}
		return bool;
	}
}
