
package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgLayerForm;
import com.fitech.net.hibernate.OrgLayer;
/**
 *@StrutsOrgLayerDelegate  ��������Delegate
 * 
 * @author jcm
 */
public class StrutsOrgLayerDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsOrgLayerDelegate.class);

	/**
	 * ������������
	 * @param orgLayerForm OrgLayerForm
     * @return boolean result,�����ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣���� 
     */
   public static  boolean create (OrgLayerForm orgLayerForm) throws Exception {
	
	   boolean result=false;				//��result���
	   OrgLayer orgLayerPersistence = new OrgLayer();
	   	   
	   if (orgLayerForm == null || orgLayerForm.getOrg_layer_name().equals("")) 
		   return result;
	   
	   //���Ӷ���ĳ�ʼ��
	   DBConn conn=null;
	   //�Ự����ĳ�ʼ��
	   Session session=null;
	   try{
		   
		   TranslatorUtil.copyVoToPersistence(orgLayerPersistence,orgLayerForm);
		   //ʵ�������Ӷ���
		   conn =new DBConn();
		   //�Ự����Ϊ���Ӷ������������
		   session=conn.beginTransaction();
		
		   //�Ự���󱣴�־ò����
		   session.save(orgLayerPersistence);
		   session.flush();
		   //��־Ϊtrue
		   result=true;
	   }
	   catch(HibernateException e){
		   //�־ò���쳣����׽
		   result = false;
		   log.printStackTrace(e);
	   }
	   finally{
		   //�������״̬��,��Ͽ�,��������,����
		   if(conn!=null) conn.endTransaction(result);
	   }
	   return result;
	   
   }

   /**
    * ȡ�ð�������ѯ���ļ�¼����
    * @return int ��ѯ���ļ�¼����	
    * @param   orgLayerForm   ������ѯ��������Ϣ����������ID�������������ƣ�
    */
   
   public static int getRecordCount(OrgLayerForm orgLayerForm)
   {
	   int count=0;
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("select count(*) from OrgLayer ol");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (orgLayerForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (orgLayerForm.getOrg_layer_name() != null && !orgLayerForm.getOrg_layer_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ol.orgLayerName like '%" + orgLayerForm.getOrg_layer_name() + "%'");
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
    * ��������Ĳ�ѯ
    * 
    * @param orgLayerForm OrgLayerForm ��ѯ������
    * @return List ������ҵ���¼������List��¼�������򣬷���null
    */
   public static List select(OrgLayerForm orgLayerForm,int offset,int limit){
	   
	   //List���ϵĶ��� 
	   List refVals = null;		
		   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("from OrgLayer ol");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (orgLayerForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (orgLayerForm.getOrg_layer_name() != null && !orgLayerForm.getOrg_layer_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ol.orgLayerName like '%" + orgLayerForm.getOrg_layer_name() + "%'");
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
		    	 OrgLayerForm orgLayerFormTemp = new OrgLayerForm();
		    	 OrgLayer orgLayerPersistence = (OrgLayer)it.next();
		         TranslatorUtil.copyPersistenceToVo(orgLayerPersistence, orgLayerFormTemp);
		         refVals.add(orgLayerFormTemp);
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
    * ����OrgLayerForm����
    *
    * @param   orgLayerForm   OrgLayerForm ������ݵĶ���
    * @exception   Exception  ���OrgLayerForm����ʧ��,��׽�׳��쳣
    */
   public static boolean update (OrgLayerForm orgLayerForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		OrgLayer orgLayerPersistence = new OrgLayer();

		if (orgLayerForm == null) {
			return result;
		}
		try {
			if (orgLayerForm.getOrg_layer_name() == null
					|| orgLayerForm.getOrg_layer_name().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(orgLayerPersistence,	orgLayerForm);
			session.update(orgLayerPersistence);

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
	 * @param orgLayerForm  The OrgLayerForm  �������ݵĴ���
	 * @exception Exception ��� OrgLayerForm ����ʧ���׳��쳣.
	 */
   public static boolean  edit (OrgLayerForm orgLayerForm) throws Exception {
	   boolean result = false;
	   DBConn conn = null;
	   Session session = null;
	  
	   OrgLayer orgLayerPersistence = new OrgLayer ();
	   orgLayerForm = new OrgLayerForm();

	   if (orgLayerForm == null ){
		   return  result;
	   }
	   
	   try{
		   if (orgLayerForm.getOrg_layer_name() == null || orgLayerForm.getOrg_layer_name().equals(""))
			   return result;
		   conn = new DBConn();
		   session = conn.beginTransaction();
	   
		   orgLayerPersistence = (OrgLayer)session.load(OrgLayer.class, orgLayerForm.getOrg_layer_name());
	
		   TranslatorUtil.copyVoToPersistence(orgLayerPersistence, orgLayerForm);
	         
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
    * @param   orgLayerForm   OrgLayerForm ��ѯ���Ķ���
    * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
    */  
   public static boolean remove (OrgLayerForm orgLayerForm) throws Exception {
       //�ñ�־result
	   boolean result=false;
	   //���ӺͻỰ����ĳ�ʼ��
	   DBConn conn=null;
	   Session session=null;
	 
	   //orgLayerForm�Ƿ�Ϊ��,����result
	   if (orgLayerForm == null || orgLayerForm.getOrg_layer_id() == null) 
		   return result;
	  
	     try{
	       //	���Ӷ���ͻỰ�����ʼ��
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   
		   OrgLayer orgLayer=(OrgLayer)session.load(OrgLayer.class,orgLayerForm.getOrg_layer_id());
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
	  * @param orgLayerForm OrgLayerFormʵ��������
	  * @return OrgLayerForm ����һ����¼
	  */
   
   public static OrgLayerForm selectOne(OrgLayerForm orgLayerForm){
	   
	   DBConn conn = null;
	   Session session = null;
	   
	   if(orgLayerForm != null){
		   try{
			   conn = new DBConn();
			   session = conn.openSession();
			   OrgLayer orgLayerPersistence = (OrgLayer)session.load(OrgLayer.class,orgLayerForm.getOrg_layer_id());
			   TranslatorUtil.copyPersistenceToVo(orgLayerPersistence,orgLayerForm);
		   }catch(HibernateException he){
			   log.printStackTrace(he);
		   }catch(Exception e){
			   log.printStackTrace(e);
		   }finally{
			   //������Ӵ��ڣ���Ͽ��������Ự������
			   if(conn != null) conn.closeSession();
		   }
	   }
	   return orgLayerForm;
   }



	/**
	 * ��ѯ���м�¼
	 * @return List ��ѯ���ļ�¼����	
	 */
	public static List findAll () throws Exception {
		
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgLayer");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgLayerForm orgLayerFormTemp = new OrgLayerForm();
					OrgLayer orgLayerPersistence = (OrgLayer)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgLayerPersistence,orgLayerFormTemp);
					refVals.add(orgLayerFormTemp);
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
}
