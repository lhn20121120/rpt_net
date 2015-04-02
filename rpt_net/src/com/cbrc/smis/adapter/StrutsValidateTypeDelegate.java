
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.hibernate.ValidateType;
import com.cbrc.smis.util.FitechException;
/**
 *@StrutsValidateTypeDelegate У������Delegate
 * 
 * @author ����
 */
public class StrutsValidateTypeDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsValidateTypeDelegate.class);

/**
    * У���������
    * 
    * @param mRepFreqForm ValidateTypeForm
    * @return boolean result,�����ɹ�����true,���򷵻�false
    * @exception Exception����׽�쳣����
    */
   public static  boolean create (ValidateTypeForm validateTypeForm) throws Exception {
	
	   boolean result=false;				//��result���
	   ValidateType validateTypePersistence = new ValidateType ();
	   	   
	   if (validateTypeForm==null || validateTypeForm.getValidateTypeName().equals("")) 
	   {
		   return  result;
	   }
	   //���Ӷ���ĳ�ʼ��
	   DBConn conn=null;
	   //�Ự����ĳ�ʼ��
	   Session session=null;
	   
	   try
	   {
		   //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,validateTypeForm��ʾ�����)
		   if (validateTypeForm.getValidateTypeName()==null  || validateTypeForm.getValidateTypeName().equals(""))
		   {
			   return result;
		   }
		
		   TranslatorUtil.copyVoToPersistence(validateTypePersistence,validateTypeForm);
		   //ʵ�������Ӷ���
		   conn =new DBConn();
		   //�Ự����Ϊ���Ӷ������������
		   session=conn.beginTransaction();
		   
		   //�Ự���󱣴�־ò����
		   session.save(validateTypePersistence);
		   session.flush();
		   TranslatorUtil.copyPersistenceToVo(validateTypePersistence,validateTypeForm);
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
    * @param   validateTypeForm   ������ѯ��������Ϣ��У�����ID��У��������ƣ�
    */
   
   public static int getRecordCount(ValidateTypeForm validateTypeForm)
   {
	   int count=0;
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("select count(*) from ValidateType vt");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (validateTypeForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (validateTypeForm.getValidateTypeName() != null && !validateTypeForm.getValidateTypeName().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "vt.validateTypeName like '%" + validateTypeForm.getValidateTypeName() + "%'");
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
    * У�����Ĳ�ѯ
    * 
    * @param validateTypeForm ValidateTypeForm ��ѯ������
    * @return List ������ҵ���¼������List��¼�������򣬷���null
    */
   public static List select(ValidateTypeForm validateTypeForm,int offset,int limit){
	   
	   //List���ϵĶ��� 
	   List refVals=null;		   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("from ValidateType vt");						
	   StringBuffer where = new StringBuffer("");	   
	   if (validateTypeForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (validateTypeForm.getValidateTypeName() != null && !validateTypeForm.getValidateTypeName().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "vt.validateTypeName like '%" + validateTypeForm.getValidateTypeName() + "%'");
	   }
		  
      try{    	     	
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  hql.append(" order by vt.validateTypeName");
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
		         ValidateTypeForm validateTypeFormTemp = new ValidateTypeForm();
		         ValidateType validateTypePersistence = (ValidateType)it.next();
		         TranslatorUtil.copyPersistenceToVo(validateTypePersistence, validateTypeFormTemp);
		         refVals.add(validateTypeFormTemp);
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
    * ����ValidateTypeForm����
    *
    * @param   validateTypeForm   ValidateTypeForm ������ݵĶ���
    * @exception   Exception  ���ValidateTypeForm����ʧ��,��׽�׳��쳣
    */
   public static boolean update (ValidateTypeForm validateTypeForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		ValidateType validateTypePersistence = new ValidateType();

		if (validateTypeForm == null) {
			return result;
		}
		try {
			if (validateTypeForm.getValidateTypeName() == null
					|| validateTypeForm.getValidateTypeName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(validateTypePersistence,
					validateTypeForm);
			session.update(validateTypePersistence);

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
	 * @param validateTypeForm  The ValidateTypeForm  �������ݵĴ���
	 * @exception Exception ��� ValidateTypeForm ����ʧ���׳��쳣.
	 */
   public static boolean  edit (ValidateTypeForm validateTypeForm) throws Exception {
	   boolean result=false;
	   DBConn conn=null;
	   Session session=null;
	  
	   ValidateType validateTypePersistence = new ValidateType ();
	   validateTypeForm=new ValidateTypeForm();
	   validateTypeForm.getValidateTypeName();
	   
	   if (validateTypeForm==null ) 
	   {
		   return  result;
	   }
	   
	   try
	   {
	   if (validateTypeForm.getValidateTypeName()==null && validateTypeForm.getValidateTypeName().equals(""))
	   {
		   return result;
	   }
	   conn=new DBConn();
	   session=conn.beginTransaction();
	   
	   
	   validateTypePersistence = (ValidateType)session.load(ValidateType.class, validateTypeForm.getValidateTypeName());

	   TranslatorUtil.copyVoToPersistence(validateTypePersistence, validateTypeForm);
         
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
    * @param   validateTypeForm   ValidateTypeForm ��ѯ���Ķ���
    * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
    */  
   public static boolean remove (ValidateTypeForm validateTypeForm) throws Exception {
      //�ñ�־result
	   boolean result=false;
	   //���ӺͻỰ����ĳ�ʼ��
	   DBConn conn=null;
	   Session session=null;
	 
	   //validateType�Ƿ�Ϊ��,����result
	   if (validateTypeForm==null && validateTypeForm.getValidateTypeId()==null) return result;
	  
	     try{
	    	 //	���Ӷ���ͻỰ�����ʼ��
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   //��validateTypeForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
		   ValidateType validateType=(ValidateType)session.load(ValidateType.class,validateTypeForm.getValidateTypeId());
		   TranslatorUtil.copyPersistenceToVo(validateType,validateTypeForm);
		   //�Ự����ɾ���־ò����
		   session.delete(validateType);
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
  * ��ѯһ����¼,���ص�EditAction��
  * @param validateTypeForm ValidateTypeFormʵ��������
  * @return ValidateTypeForm ����һ����¼
  */
   
public static ValidateTypeForm selectOne(ValidateTypeForm validateTypeForm){
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   	     
	   if (validateTypeForm != null) 
	 try
      {	   
    	  if (validateTypeForm.getValidateTypeId() != null && !validateTypeForm.getValidateTypeId().equals(""))
    	  //conn�����ʵ����		  
    	  conn=new DBConn();
    	  //�����ӿ�ʼ�Ự
    	  session=conn.openSession();
    	
    	  ValidateType validateTypePersistence = (ValidateType)session.load(ValidateType.class, validateTypeForm.getValidateTypeId());
    	
    	  TranslatorUtil.copyPersistenceToVo(validateTypePersistence, validateTypeForm);
    	
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn!=null) conn.closeSession();
      }
	    return validateTypeForm;
   }



   /**
    * Retrieve all existing ValidateTypeForm objects.
    *
    * @exception   Exception   If the ValidateTypeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.smis.hibernate.ValidateType"));
tx.commit();
session.close();
      ArrayList validateType_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         ValidateTypeForm validateTypeFormTemp = new ValidateTypeForm();
         com.cbrc.smis.hibernate.ValidateType validateTypePersistence = (com.cbrc.smis.hibernate.ValidateType)it.next();
         TranslatorUtil.copyPersistenceToVo(validateTypePersistence, validateTypeFormTemp);
         validateType_vos.add(validateTypeFormTemp);
      }
      retVals = validateType_vos;
      return retVals;
   }
   
   /*����StrutsDataValidateInfoDelegate�е�selectValidateTypeId������ѯValidateType���е�Validate_Type_Name
    * @author ����
    * @param reportInParticularForm
    * @validateTypeName ���ص�Ϊ���ҵ�У����������
    
   
   public static String selectValidateTypeName(ReportInParticularForm reportInParticularForm){
	   String validateTypeName="";
	   DBConn conn=null;
	   Session session=null;
	   List list=null;
	   int typeId=StrutsDataValidateInfoDelegate.selectValidateTypeId(reportInParticularForm);
	   try {
			if (typeId != 0&& typeId>0) {
				conn = new DBConn();
				session = conn.openSession();

				String hql = "from ValidateType vt where 1=1";
				hql += " and vi.validateTypeId="
						+ typeId +"";

				Query query = session.createQuery(hql.toString());
				list = query.list();
				if (list != null && list.size() != 0) {
					validateTypeName = ((ValidateType) list.get(0)).getValidateTypeName();
					// System.out.println("validateTypeName in <<selectValidateTypeName() of StrutsValidateTypeDelegate>>========================="+validateTypeName);
				}

			}
   	}catch(HibernateException he){
   		log.printStackTrace(he);
   	}catch(Exception e){
   		log.printStackTrace(e);
   	}finally{
   		if (conn!=null)conn.closeSession();
   	}
   	return validateTypeName;
   }*/
}

