
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MRepTypeForm;
import com.cbrc.smis.hibernate.MRepType;
import com.cbrc.smis.util.FitechException;
/**
 * @StrutsMRepTypeDelegate  �������ͱ�Delegate
 * 
 * @author ����
 */
public class StrutsMRepTypeDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsMRepTypeDelegate.class);

/**
    * �����������
    * 
    * @param mRepTypeForm MRepTypeForm
    * @return boolean result,�����ɹ�����true,���򷵻�false
    * @exception Exception����׽�쳣����
    */
   public static  boolean create (MRepTypeForm mRepTypeForm) throws Exception {
	
	   boolean result=false;				//��result���
	   MRepType mRepTypePersistence = new MRepType ();
	   	   
	   if (mRepTypeForm==null ) 
	   {
		   return  result;
	   }
	   //���Ӷ���ĳ�ʼ��
	   DBConn conn=null;
	   //�Ự����ĳ�ʼ��
	   Session session=null;
	   
	   try
	   {
		   //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mCurUnitForm��ʾ�����)
		   if (mRepTypeForm.getRepTypeName()==null  || mRepTypeForm.getRepTypeName().equals(""))
		   {
			   return result;
		   }
		   
		   TranslatorUtil.copyVoToPersistence(mRepTypePersistence,mRepTypeForm);
		   //ʵ�������Ӷ���
		   conn =new DBConn();
		   //�Ự����Ϊ���Ӷ������������
		   session=conn.beginTransaction();
		  
		   //�Ự���󱣴�־ò����
		   session.save(mRepTypePersistence);
		   session.flush();
		   TranslatorUtil.copyPersistenceToVo(mRepTypePersistence,mRepTypeForm);
		   //��־Ϊtrue
		   result=true;
	   }
	   catch(HibernateException e)
	   {
		   //�־ò���쳣����׽
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
    * @param   mRepTypeForm   ������ѯ��������Ϣ��������𣬱���������ƣ�
    */
   
   public static int getRecordCount(MRepTypeForm mRepTypeForm)
   {
	   int count=0;
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("select count(*) from MRepType mrt");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRepTypeForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (mRepTypeForm.getRepTypeName() != null && !mRepTypeForm.getRepTypeName().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mrt.repTypeName like '%" + mRepTypeForm.getRepTypeName()+ "%'");
	   }
		  
      try
      {	   //List���ϵĲ���
    	  //��ʼ��
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
    * ���ұ������
    * 
    * @param mRepTypeForm MRepTypeForm ��ѯ������
    * @return List ������ҵ���¼������List��¼�������򣬷���null
    */
   public static List select(MRepTypeForm mRepTypeForm,int offset,int limit){
	   
	   //	 List���ϵĶ��� 
	   List refVals=null;		
		   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("from MRepType mrt ");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRepTypeForm != null)
	   {
		   // �����������ж�,�������Ʋ���Ϊ��
		   if (mRepTypeForm.getRepTypeName() != null && !mRepTypeForm.getRepTypeName().equals(""))
				where.append((where.toString().equals("") ? "" : " and ") + "mrt.repTypeName like '%" + mRepTypeForm.getRepTypeName() + "%'");
	   }

      try
      {	   //List���ϵĲ���
    	  //��ʼ��
    	  hql.append((where.toString().equals("") ? "" : " where ") + where.toString());
    	  //conn�����ʵ����		  
    	  conn=new DBConn();
    	  //�����ӿ�ʼ�Ự
    	  session=conn.openSession();
    	  //��Ӽ�����Session
		  //List list=session.find(hql.toString());
		  Query query=session.createQuery(hql.toString());
		 
		  query.setFirstResult(offset).setMaxResults(limit);
		  List list=query.list();
		  
    	  if (list!=null){
    		  refVals = new ArrayList();
    		  //ѭ����ȡ���ݿ����������¼
		      for (Iterator it = list.iterator(); it.hasNext(); ) {
		         MRepTypeForm mRepTypeFormTemp = new MRepTypeForm();
		         MRepType mRepTypePersistence = (MRepType)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeFormTemp);
		         refVals.add(mRepTypeFormTemp);
		      }
    	   }
      }catch(HibernateException he){
    	  refVals=null;
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  refVals=null;  
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn!=null) conn.closeSession();
      }
         return refVals;
   }
   
   
   /**
    * ���� MRepTypeForm ����
    *
    * @param   mRepTypeForm  MRepTypeForm ��������ݸ���
    * @exception   Exception   ���MRepTypeForm ����û�и���
    */
   public static boolean update (MRepTypeForm mRepTypeForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRepType mRepTypePersistence = new MRepType();

		if (mRepTypeForm == null) {
			return result;
		}
		try {
			if (mRepTypeForm.getRepTypeName()== null
					|| mRepTypeForm.getRepTypeName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(mRepTypePersistence,
					mRepTypeForm);
			session.update(mRepTypePersistence);

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
	 * @param mRepTypeForm  MRepTypeForm ����һ�����ݵĶ���
	 * @exception Exception ���MRepTypeForm ����û������
	 */
   public static boolean  edit (MRepTypeForm mRepTypeForm) throws Exception {
	   boolean result=false;
	   DBConn conn=null;
	   Session session=null;
	  
	   MRepType mRepTypePersistence = new MRepType ();
	   mRepTypeForm=new MRepTypeForm();
	   mRepTypeForm.getRepTypeName();
	   
	   if (mRepTypeForm==null ) 
	   {
		   return  result;
	   }
	   
	   try
	   {
	   if (mRepTypeForm.getRepTypeName()==null && mRepTypeForm.getRepTypeName().equals(""))
	   {
		   return result;
	   }
	   conn=new DBConn();
	   session=conn.beginTransaction();
	   
	   
	   mRepTypePersistence = (MRepType)session.load(MRepType.class, mRepTypeForm.getRepTypeName());

	   TranslatorUtil.copyVoToPersistence(mRepTypePersistence, mRepTypeForm);
         
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
    * @param   mRepTypeForm   MRepTypeForm ��ѯ���Ķ���
    * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
    */  
   public static boolean remove (MRepTypeForm mRepTypeForm) throws Exception {
      //�ñ�־result
	   boolean result=false;
	   //���ӺͻỰ����ĳ�ʼ��
	   DBConn conn=null;
	   Session session=null;
	 
	   //mCurUnit�Ƿ�Ϊ��,����result
	   if (mRepTypeForm==null || mRepTypeForm.getRepTypeId()==null) return result;
	  
	     try{
	    	 //	���Ӷ���ͻỰ�����ʼ��
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   //��mRepTypeForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
		   MRepType mRepType=(MRepType)session.load(MRepType.class,mRepTypeForm.getRepTypeId());
		   //�Ự����ɾ���־ò����
		   session.delete(mRepType);
		   //session.flush();
		   
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
  * ��ѯһ����¼,���ص�EditAction��
  * @param mRepTypeForm MRepTypeForm
  * @return MRepTypeForm ����һ����¼
  */
   
public static MRepTypeForm selectOne(MRepTypeForm mRepTypeForm){
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   	     
	   if (mRepTypeForm != null) 
		   // System.out.println("mRepTypeForm:"+mRepTypeForm.getRepTypeId()+"===="+mRepTypeForm.getRepTypeName());
      try
      {	   
    	  if (mRepTypeForm.getRepTypeId() != null && !mRepTypeForm.getRepTypeId().equals(""))
    	  //conn�����ʵ����		  
    	  conn=new DBConn();
    	  //�����ӿ�ʼ�Ự
    	  session=conn.openSession();
    	
    	  MRepType mRepTypePersistence = (MRepType)session.load(MRepType.class, mRepTypeForm.getRepTypeId());
    	 TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeForm);
    	
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn!=null) conn.closeSession();
      }
	    return mRepTypeForm;
   }



   /**
    * ��ȡ���еı���������Ϣ�б�
    *
    * @return List
    * @exception   Exception   If the MRepTypeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = null;
      
      DBConn conn=null;
      
      try{
    	  conn=new DBConn();
    	  
		  List list=conn.openSession().find("from MRepType");

	      if(list!=null && list.size()>0){
	    	  retVals=new ArrayList();
		      for (Iterator it = list.iterator(); it.hasNext(); ) {
		         MRepTypeForm mRepTypeFormTemp = new MRepTypeForm();
		         com.cbrc.smis.hibernate.MRepType mRepTypePersistence = (com.cbrc.smis.hibernate.MRepType)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeFormTemp);
		         retVals.add(mRepTypeFormTemp);
		      }
	      }
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }finally{
    	  if(conn!=null) conn.closeSession();
      }
      
      return retVals;
   }
   /**
    * ��ȡFR���еı���������Ϣ�б�
    *
    * @return List
    * @exception   Exception   If the MRepTypeForm objects cannot be retrieved.
    */
   public static List findAllFR () throws Exception {
      List retVals = null;
      
      DBConn conn=null;
      
      try{
    	  conn=new DBConn();
    	  
		  List list=conn.openSession().find("from MRepType");

	      if(list!=null && list.size()>0){
	    	  retVals=new ArrayList();
		      for (Iterator it = list.iterator(); it.hasNext(); ) {
		         MRepTypeForm mRepTypeFormTemp = new MRepTypeForm();
		         com.cbrc.smis.hibernate.MRepType mRepTypePersistence = (com.cbrc.smis.hibernate.MRepType)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeFormTemp);
		         retVals.add(mRepTypeFormTemp);
		      }
	      }
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }finally{
    	  if(conn!=null) conn.closeSession();
      }
      
      return retVals;
   }
}
