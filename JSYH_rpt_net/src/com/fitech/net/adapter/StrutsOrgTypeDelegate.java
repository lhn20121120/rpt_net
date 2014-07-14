
package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgTypeForm;
import com.fitech.net.hibernate.MRegion;
import com.fitech.net.hibernate.OrgType;
/**
 *@StrutsOrgLayerDelegate  ��������Delegate
 * 
 * @author jcm
 */
public class StrutsOrgTypeDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsOrgTypeDelegate.class);

	/**
	 * �����������
	 * @param orgTypeForm OrgTypeForm
     * @exception Exception����׽�쳣���� 
     * @return int result  0--����ʧ��  1--�����ɹ�  2--Ҫ��ӵ�ָ������Ļ�������Ѿ�����
     */
   public static int create (OrgTypeForm orgTypeForm) throws Exception {
	
	   int result = 0;				//��result���
	   boolean bool = false;
	   OrgType orgTypePersistence = new OrgType();
	   	   
	   if (orgTypeForm == null || orgTypeForm.getOrg_type_name().equals("")) 
		   return result;
	   
	   //���Ӷ���ĳ�ʼ��
	   DBConn conn=null;
	   //�Ự����ĳ�ʼ��
	   Session session=null;
	   try{
		   orgTypeForm.setOrg_type_id(orgTypeForm.getOrg_layer_id());
		   
		   TranslatorUtil.copyVoToPersistence(orgTypePersistence,orgTypeForm);
		   //ʵ�������Ӷ���
		   conn =new DBConn();
		   //�Ự����Ϊ���Ӷ������������
		   session=conn.beginTransaction();
		   
		   String hql = "from OrgType ot where ot.orgTypeId = " + orgTypeForm.getOrg_type_id();
		   List list = session.find(hql);
		   
		   if(list != null && list.size() > 0){
			   result = 2;        //Ҫ���ӵĻ��������Ѿ�����
			   bool = true;
			   return result;
		   }
		
		   //�Ự���󱣴�־ò����
		   session.save(orgTypePersistence);
		   session.flush();
		   
		   result=1;       //���ӳɹ�
		   bool = true;
		   
	   }
	   catch(HibernateException e){
		   //�־ò���쳣����׽
		   result = 0;
		   bool = false;
		   log.printStackTrace(e);
	   }
	   finally{
		   //�������״̬��,��Ͽ�,��������,����
		   if(conn!=null) conn.endTransaction(bool);
	   }
	   return result;
	   
   }

   /**
    * ȡ�ð�������ѯ���ļ�¼����
    * @return int ��ѯ���ļ�¼����	
    * @param   orgTypeForm   ������ѯ��������Ϣ���������ID������������ƣ�
    */
   
   public static int getRecordCount(OrgTypeForm orgTypeForm)
   {
	   int count=0;
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("select count(*) from OrgType ot");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (orgTypeForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (orgTypeForm.getOrg_type_name() != null && !orgTypeForm.getOrg_type_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ot.orgTypeName like '%" + orgTypeForm.getOrg_type_name() + "%'");
	   }
		  
      try {
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  //conn�����ʵ����		  
    	  conn=new DBConn();
    	  //�����ӿ�ʼ�Ự
    	  session=conn.openSession();
    	  List list=session.find(hql.toString());
    	  if(list!=null && list.size()==1){
    		  count=((Integer)list.get(0)).intValue();
    	  }
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn!=null) conn.closeSession();
      }
         return count;
   }
   
   /**
    * �ϱ�Ƶ�ȵĲ�ѯ
    * 
    * @param orgTypeForm OrgTypeForm ��ѯ������
    * @return List ������ҵ���¼������List��¼�������򣬷���null
    */
   public static List select(OrgTypeForm orgTypeForm,int offset,int limit){
	   
	   //List���ϵĶ��� 
	   List refVals = null;		
		   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("from OrgType ot");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (orgTypeForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (orgTypeForm.getOrg_type_name() != null && !orgTypeForm.getOrg_type_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ot.orgTypeName like '%" + orgTypeForm.getOrg_type_name() + "%'");
	   }
		  
      try {
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
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
		    	 OrgTypeForm orgTypeFormTemp = new OrgTypeForm();
		    	 OrgType orgTypePersistence = (OrgType)it.next();
		         TranslatorUtil.copyPersistenceToVo(orgTypePersistence, orgTypeFormTemp);
		         refVals.add(orgTypeFormTemp);
		      }
    	   }
      }catch(HibernateException he){
    	  refVals = null;
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  refVals = null;  
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn != null) conn.closeSession();
      }
         return refVals;
   }
   
   
   /**
    * ����OrgTypeForm����
    *
    * @param   orgTypeForm   OrgTypeForm ������ݵĶ���
    * @exception   Exception  ���OrgTypeForm����ʧ��,��׽�׳��쳣
    */
   public static boolean update (OrgTypeForm orgTypeForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		OrgType orgTypePersistence = new OrgType();

		if (orgTypeForm == null) {
			return result;
		}
		try {
			if (orgTypeForm.getOrg_type_name() == null
					|| orgTypeForm.getOrg_type_name().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(orgTypePersistence,	orgTypeForm);
			session.update(orgTypePersistence);

			result = true;
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

   /**
	 * �༭����
	 * 
	 * @param orgTypeForm  The OrgTypeForm  �������ݵĴ���
	 * @exception Exception ��� OrgTypeForm ����ʧ���׳��쳣.
	 */
   public static boolean  edit (OrgTypeForm orgTypeForm) throws Exception {
	   boolean result = false;
	   DBConn conn = null;
	   Session session = null;
	  
	   OrgType orgTypePersistence = new OrgType ();
	   orgTypeForm = new OrgTypeForm();

	   if (orgTypeForm == null ){
		   return  result;
	   }
	   
	   try{
		   if (orgTypeForm.getOrg_type_name() == null || orgTypeForm.getOrg_type_name().equals(""))
			   return result;
		   conn = new DBConn();
		   session = conn.beginTransaction();
	   
		   orgTypePersistence = (OrgType)session.load(OrgType.class, orgTypeForm.getOrg_type_name());
	
		   TranslatorUtil.copyVoToPersistence(orgTypePersistence, orgTypeForm);
	         
	   }catch(HibernateException he){
		   log.printStackTrace(he);
	   }finally {
		   if(conn!=null) conn.endTransaction(result);
	   }
	       return result;
   }

   /**
    * ɾ������
    *
    * @param   orgTypeForm   OrgTypeForm ��ѯ���Ķ���
    * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
    */  
   public static boolean remove (OrgTypeForm orgTypeForm) throws Exception {
       //�ñ�־result
	   boolean result=false;
	   //���ӺͻỰ����ĳ�ʼ��
	   DBConn conn=null;
	   Session session=null;
	 
	   //orgTypeForm�Ƿ�Ϊ��,����result
	   if (orgTypeForm == null || orgTypeForm.getOrg_type_id() == null) 
		   return result;
	  
	     try{
	       //	���Ӷ���ͻỰ�����ʼ��
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   
		   OrgType orgLayer=(OrgType)session.load(OrgType.class,orgTypeForm.getOrg_type_id());
		   //�Ự����ɾ���־ò����
		   session.delete(orgLayer);
		   session.flush();
		   
		   //ɾ���ɹ�����Ϊtrue
		   result=true;
		  }
	   catch(HibernateException he){
		   //��׽������쳣,�׳�
		   log.printStackTrace(he);
	   }finally{
		   //�����������Ͽ����ӣ������Ự������
		   if (conn!=null) conn.endTransaction(result);
	   }
	     return result;
   }

   
	 /**
	  * ��ѯһ����¼
	  * @param orgTypeForm OrgTypeFormʵ��������
	  * @return OrgTypeForm ����һ����¼
	  */
   
   public static OrgTypeForm selectOne(OrgTypeForm orgTypeForm){
	   
	   DBConn conn = null;
	   Session session = null;
	   
	   if(orgTypeForm != null){
		   try{
			   conn = new DBConn();
			   session = conn.openSession();
			   OrgType orgTypePersistence = (OrgType)session.load(OrgType.class,orgTypeForm.getOrg_type_id());
			   TranslatorUtil.copyPersistenceToVo(orgTypePersistence,orgTypeForm);
		   }catch(HibernateException he){
			   log.printStackTrace(he);
		   }catch(Exception e){
			   log.printStackTrace(e);
		   }finally{
			   //������Ӵ��ڣ���Ͽ��������Ự������
			   if(conn != null) conn.closeSession();
		   }
	   }
	   return orgTypeForm;
   }

	/**
	 * ����һ����¼
	 */
	public static OrgType selectOne(Integer orgTypeId){
		OrgType orgType = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgType mr where mr.orgTypeId = " + orgTypeId ;
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgType = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			orgType = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgType;
	}

	/**
	 * ��ѯ���м�¼
	 * @return List ��ѯ���ļ�¼����	
	 */
	public static List findAll () throws Exception {
		
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgType t where t.orgTypeId!=1 and t.orgTypeId!=-99");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgTypeForm orgTypeFormTemp = new OrgTypeForm();
					OrgType orgTypePersistence = (OrgType)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgTypePersistence,orgTypeFormTemp);
					OrgType preOrgType = findPreOrgTypeId(orgTypePersistence.getOrgTypeId());
					if(preOrgType != null){
						orgTypeFormTemp.setPre_orgType_id(preOrgType.getOrgTypeId());
						orgTypeFormTemp.setPre_orgType_name(preOrgType.getOrgTypeName());
					}
					refVals.add(orgTypeFormTemp);
				}
			}
		}catch(HibernateException he){
			refVals = null;
			log.printStackTrace(he);
		}catch(Exception e){
			refVals = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	public static List findSubOrgTypes(String orgId) throws Exception{
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgType ot where ot.orgTypeId > (select on.orgType.orgTypeId from OrgNet on where on.orgId='" + orgId + "')");
	//	StringBuffer hql = new StringBuffer("from OrgType ot ");
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgTypeForm orgTypeFormTemp = new OrgTypeForm();
					OrgType orgTypePersistence = (OrgType)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgTypePersistence,orgTypeFormTemp);
					OrgType preOrgType = findPreOrgTypeId(orgTypePersistence.getOrgTypeId());
					if(preOrgType != null){
						orgTypeFormTemp.setPre_orgType_id(preOrgType.getOrgTypeId());
						orgTypeFormTemp.setPre_orgType_name(preOrgType.getOrgTypeName());
					}
					refVals.add(orgTypeFormTemp);
				}
			}
		}catch(HibernateException he){
			refVals = null;
			log.printStackTrace(he);
		}catch(Exception e){
			refVals = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * ������һ����������
	 */
	public static OrgType findPreOrgTypeId(Integer orgTypeId){
		OrgType orgTypeResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "select min(orgTypeId) from OrgType ot where ot.orgTypeId > " + orgTypeId ;
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgTypeResult = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			he.printStackTrace();
		}catch(Exception e){
			orgTypeResult = null ;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgTypeResult;
	}
	
	/**
	 * ������һ����������
	 */
	public static OrgType findPreOrgTypeId1(Integer orgTypeId){
		OrgType orgTypeResult = null;
		DBConn conn = null;
		Session session = null;
		List list=null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "select from OrgType ot where ot.orgTypeId < " + orgTypeId ;
			
			list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgTypeResult = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			he.printStackTrace();
		}catch(Exception e){
			orgTypeResult = null ;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		if(list==null) return null;
		if(list.size()==0) return null;
		return (OrgType)list.get(list.size()-1);
	}
	
	public static OrgType findMaxOrgTyp(){
		OrgType orgTypeResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgType orgType where orgType.orgTypeId in (select min(ot.orgTypeId) from OrgType ot)";
			
			List list = session.find(hql);
			
			if(list != null && list.size() > 0){
				orgTypeResult = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			orgTypeResult = null;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			if(conn != null) conn.closeSession();
		}
		return orgTypeResult;
	}
}
