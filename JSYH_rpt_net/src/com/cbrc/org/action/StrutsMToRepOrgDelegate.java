package com.cbrc.org.action;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.dao.DBConn;
import com.cbrc.org.form.MToRepOrgForm;
import com.cbrc.org.hibernate.MToRepOrg;
import com.cbrc.smis.util.FitechException;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MToRepOrg data (i.e. 
 * com.cbrc.org.form.MToRepOrgForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMToRepOrgDelegate {
	private static FitechException log = new FitechException(StrutsMToRepOrgDelegate.class); 

   /**
    * Create a new com.cbrc.org.form.MToRepOrgForm object and persist (i.e. insert) it.
    *
    * @param   mToRepOrgForm   The object containing the data for the new com.cbrc.org.form.MToRepOrgForm object
    * @exception   Exception   If the new com.cbrc.org.form.MToRepOrgForm object cannot be created or persisted.
    */
   public static com.cbrc.org.form.MToRepOrgForm create (com.cbrc.org.form.MToRepOrgForm mToRepOrgForm) throws Exception {
    /*  com.cbrc.org.hibernate.MToRepOrg mToRepOrgPersistence = new com.cbrc.org.hibernate.MToRepOrg ();
      TranslatorUtil.copyVoToPersistence(mToRepOrgPersistence, mToRepOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: mToRepOrgPersistence = (com.cbrc.org.hibernate.MToRepOrg)session.save(mToRepOrgPersistence);
session.save(mToRepOrgPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mToRepOrgPersistence, mToRepOrgForm);*/
      return mToRepOrgForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.org.form.MToRepOrgForm object.
    *
    * @param   mToRepOrgForm   The com.cbrc.org.form.MToRepOrgForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.org.form.MToRepOrgForm object cannot be updated/persisted.
    */
   public static com.cbrc.org.form.MToRepOrgForm update (com.cbrc.org.form.MToRepOrgForm mToRepOrgForm) throws Exception {
     /* com.cbrc.org.hibernate.MToRepOrg mToRepOrgPersistence = new com.cbrc.org.hibernate.MToRepOrg ();
      TranslatorUtil.copyVoToPersistence(mToRepOrgPersistence, mToRepOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(mToRepOrgPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mToRepOrgPersistence, mToRepOrgForm);*/
      return mToRepOrgForm;
   }

   /**
    * Retrieve an existing com.cbrc.org.form.MToRepOrgForm object for editing.
    *
    * @param   mToRepOrgForm   The com.cbrc.org.form.MToRepOrgForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MToRepOrgForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MToRepOrgForm edit (com.cbrc.org.form.MToRepOrgForm mToRepOrgForm) throws Exception {
     /* com.cbrc.org.hibernate.MToRepOrg mToRepOrgPersistence = new com.cbrc.org.hibernate.MToRepOrg ();
      TranslatorUtil.copyVoToPersistence(mToRepOrgPersistence, mToRepOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
mToRepOrgPersistence = (com.cbrc.org.hibernate.MToRepOrg)session.load(com.cbrc.org.hibernate.MToRepOrg.class, mToRepOrgPersistence.getOrgId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mToRepOrgPersistence, mToRepOrgForm);*/
      return mToRepOrgForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MToRepOrgForm object.
    *
    * @param   mToRepOrgForm   The com.cbrc.org.form.MToRepOrgForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MToRepOrgForm object cannot be removed.
    */  
   public static void remove (com.cbrc.org.form.MToRepOrgForm mToRepOrgForm) throws Exception {
    /*  com.cbrc.org.hibernate.MToRepOrg mToRepOrgPersistence = new com.cbrc.org.hibernate.MToRepOrg ();
      TranslatorUtil.copyVoToPersistence(mToRepOrgPersistence, mToRepOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
mToRepOrgPersistence = (com.cbrc.org.hibernate.MToRepOrg)session.load(com.cbrc.org.hibernate.MToRepOrg.class, mToRepOrgPersistence.getOrgId());
session.delete(mToRepOrgPersistence);
tx.commit();
session.close();*/
   }

   /**
    * Retrieve all existing com.cbrc.org.form.MToRepOrgForm objects.
    *
    * @exception   Exception   If the com.cbrc.org.form.MToRepOrgForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
    /*  List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MToRepOrg"));
tx.commit();
session.close();
      ArrayList mToRepOrg_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MToRepOrgForm mToRepOrgFormTemp = new com.cbrc.org.form.MToRepOrgForm();
         com.cbrc.org.hibernate.MToRepOrg mToRepOrgPersistence = (com.cbrc.org.hibernate.MToRepOrg)it.next();
         TranslatorUtil.copyPersistenceToVo(mToRepOrgPersistence, mToRepOrgFormTemp);
         mToRepOrg_vos.add(mToRepOrgFormTemp);
      }
      retVals = mToRepOrg_vos;*/
      return null;
   }

   /**
    * 按条件查询代报机构，并返回List集合
    * Retrieve a set of existing com.cbrc.smis.form.LogInForm objects for editing.
    * @return List 包含查询到的所有记录	
    * @param   logInForm   包含查询的条件信息（代报机构名称)
    * @param  offset 偏移量
    * @param  limit 要取的记录条数
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm objects cannot be retrieved.
    */
   	public static List select (MToRepOrgForm mToRepOrgForm,int offset,int limit)
   {
      List result = null;
		
	  DBConn conn=null;
	  Session session=null;
	  
	  try{
		  conn=new DBConn();
		  session=conn.openSession();
		  //;
		  StringBuffer hql = new StringBuffer("from MToRepOrg m where 1=1");
		  StringBuffer where = new StringBuffer("");
		  if(mToRepOrgForm!=null)
		  {
			  //依据传过来的参数条件，增加相应的查询条件
			  //代报机构名称
			  if(mToRepOrgForm.getOrgName()!=null && !mToRepOrgForm.getOrgName().equals(""))
			  	where.append(" and m.orgName='"+mToRepOrgForm.getOrgName()+"'");
		  }
		  hql.append(where.toString());
		  //根据条件取相应的记录条数
		  Query query = session.createQuery(hql.toString());
		  query.setFirstResult(offset);
		  query.setMaxResults(limit);
		  
		  result = query.list();
		  
		  if(result!=null){
			  ArrayList list = new ArrayList();
			  //将查询到的记录存放入List中
		      for (Iterator it = result.iterator(); it.hasNext(); ) {
		    	  MToRepOrgForm mToRepOrgFormTemp = new MToRepOrgForm();
		    	  MToRepOrg mToRepOrgPersistence = (MToRepOrg)it.next();
		    	 // TranslatorUtil.copyPersistenceToVo(mToRepOrgPersistence, mToRepOrgFormTemp);
		    	  list.add(mToRepOrgForm);
		      }
		      result = list;
		  }	      
	  }catch(HibernateException he){
		  log.printStackTrace(he);
	  }
	  catch(Exception e){
		  log.printStackTrace(e);
	  }
	  finally{
		  if(conn!=null) conn.closeSession();		  
	  }
      return result;
   }
   /**
    * 取得按条件查询到的记录条数
    * Retrieve a set of existing com.cbrc.smis.form.LogInForm objects for editing.
    * @return int 查询到的记录条数	
    * @param   mToRepOrgForm   包含查询的条件信息（机构名称）
    */
   
   public static int getRecordCount(MToRepOrgForm mToRepOrgForm)
   {
	      List result = null;
			
		  DBConn conn=null;
		  Session session=null;
		  
		  try{
			  conn=new DBConn();
			  session=conn.openSession();
			  StringBuffer hql = new StringBuffer("select count(*)from MToRepOrg m where 1=1 ");
			  StringBuffer where = new StringBuffer("");
			  if(mToRepOrgForm!=null)
			  {
				  //依据传过来的参数条件，增加相应的查询条件
				  //代报机构名称
				  if(mToRepOrgForm.getOrgName()!=null && !mToRepOrgForm.getOrgName().equals(""))
				  	where.append(" and m.orgName='"+mToRepOrgForm.getOrgName()+"'");
			  }
			  hql.append(where.toString());
			  //根据条件取相应的记录条数
			  Query query = session.createQuery(hql.toString());
			  
			  result=query.list();
			  
			  if(result!=null){
				  return Integer.parseInt(result.get(0).toString());
			  }	      
		  }catch(HibernateException he){
			  log.printStackTrace(he);
		  }
		  catch(Exception e){
			  log.printStackTrace(e);
		  }
		  finally{
			  if(conn!=null) conn.closeSession();		  
		  }
	      return 0;
   }

}
