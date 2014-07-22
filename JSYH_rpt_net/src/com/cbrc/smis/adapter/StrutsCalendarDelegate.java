
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.Calendar;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for Calendar data (i.e. 
 * com.cbrc.smis.form.CalendarForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsCalendarDelegate {


   /**
    * Create a new com.cbrc.smis.form.CalendarForm object and persist (i.e. insert) it.
    *
    * @param   calendarForm   The object containing the data for the new com.cbrc.smis.form.CalendarForm object
    * @exception   Exception   If the new com.cbrc.smis.form.CalendarForm object cannot be created or persisted.
    */
   public static com.cbrc.smis.form.CalendarForm create (com.cbrc.smis.form.CalendarForm calendarForm) throws Exception {
      com.cbrc.smis.hibernate.Calendar calendarPersistence = new com.cbrc.smis.hibernate.Calendar ();
      TranslatorUtil.copyVoToPersistence(calendarPersistence, calendarForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: calendarPersistence = (com.cbrc.smis.hibernate.Calendar)session.save(calendarPersistence);
session.save(calendarPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(calendarPersistence, calendarForm);
      return calendarForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.smis.form.CalendarForm object.
    *
    * @param   calendarForm   The com.cbrc.smis.form.CalendarForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.smis.form.CalendarForm object cannot be updated/persisted.
    */
   public static com.cbrc.smis.form.CalendarForm update (com.cbrc.smis.form.CalendarForm calendarForm) throws Exception {
      com.cbrc.smis.hibernate.Calendar calendarPersistence = new com.cbrc.smis.hibernate.Calendar ();
      TranslatorUtil.copyVoToPersistence(calendarPersistence, calendarForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(calendarPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(calendarPersistence, calendarForm);
      return calendarForm;
   }

   /**
    * Retrieve an existing com.cbrc.smis.form.CalendarForm object for editing.
    *
    * @param   calendarForm   The com.cbrc.smis.form.CalendarForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.smis.form.CalendarForm object cannot be retrieved.
    */
   public static com.cbrc.smis.form.CalendarForm edit (com.cbrc.smis.form.CalendarForm calendarForm) throws Exception {
      com.cbrc.smis.hibernate.Calendar calendarPersistence = new com.cbrc.smis.hibernate.Calendar ();
      TranslatorUtil.copyVoToPersistence(calendarPersistence, calendarForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
calendarPersistence = (com.cbrc.smis.hibernate.Calendar)session.load(com.cbrc.smis.hibernate.Calendar.class, calendarPersistence.getCalId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(calendarPersistence, calendarForm);
      return calendarForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.smis.form.CalendarForm object.
    *
    * @param   calendarForm   The com.cbrc.smis.form.CalendarForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.smis.form.CalendarForm object cannot be removed.
    */  
   public static void remove (com.cbrc.smis.form.CalendarForm calendarForm) throws Exception {
      com.cbrc.smis.hibernate.Calendar calendarPersistence = new com.cbrc.smis.hibernate.Calendar ();
      TranslatorUtil.copyVoToPersistence(calendarPersistence, calendarForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
calendarPersistence = (com.cbrc.smis.hibernate.Calendar)session.load(com.cbrc.smis.hibernate.Calendar.class, calendarPersistence.getCalId());
session.delete(calendarPersistence);
tx.commit();
session.close();
   }

   /**
    * ���ܣ�ȡ���������ͱ������м�¼
    * ��������
    * ����ֵ��List���� ������ѯ�������м�¼	
    * Retrieve all existing com.cbrc.smis.form.LogTypeForm objects.
    *
    * @exception   Exception   If the com.cbrc.smis.form.LogTypeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
	   List result = null;
		
	   DBConn conn = null;
	   Session session = null;	  
	   try{
			  conn = new DBConn();
			  session = conn.openSession();
			  result = session.find("from Calendar");
			  if(result!=null){
				  //// System.out.println("Search result: "+retVals.size());
				  ArrayList calendarType_vos = new ArrayList();
				  //����ѯ���ļ�¼�����List��
			      for (Iterator it = result.iterator(); it.hasNext(); ) {
			         Calendar calendarPersistence = (Calendar)it.next();
			         calendarType_vos.add(calendarPersistence);
			      }
			      result = calendarType_vos;
			  }	      
		  }catch(HibernateException he){
			  he.printStackTrace();
		  }finally{
			  if(conn!=null) conn.closeSession();		  
		  }
	      return result;
   }

   /**
    * Retrieve a set of existing com.cbrc.smis.form.CalendarForm objects for editing.
    *
    * @param   calendarForm   The com.cbrc.smis.form.CalendarForm object containing the data used to retrieve the objects (i.e. the criteria for the retrieval).
    * @exception   Exception   If the com.cbrc.smis.form.CalendarForm objects cannot be retrieved.
    */
   public static List select (com.cbrc.smis.form.CalendarForm calendarForm) throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.smis.hibernate.Calendar as obj1 where obj1.calName='" + calendarForm.getCalName() + "'"));
retVals.addAll(session.find("from com.cbrc.smis.hibernate.Calendar as obj1 where obj1.calMethod='" + calendarForm.getCalMethod() + "'"));
tx.commit();
session.close();
      ArrayList calendar_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.CalendarForm calendarFormTemp = new com.cbrc.smis.form.CalendarForm();
         com.cbrc.smis.hibernate.Calendar calendarPersistence = (com.cbrc.smis.hibernate.Calendar)it.next();
         TranslatorUtil.copyPersistenceToVo(calendarPersistence, calendarFormTemp);
         calendar_vos.add(calendarFormTemp);
      }
      retVals = calendar_vos;
      return retVals;
   }

}