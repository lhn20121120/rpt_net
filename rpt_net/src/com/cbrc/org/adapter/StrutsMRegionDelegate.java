
package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MRegion data (i.e. 
 * com.cbrc.org.form.MRegionForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMRegionDelegate {


   /**
    * Create a new com.cbrc.org.form.MRegionForm object and persist (i.e. insert) it.
    *
    * @param   mRegionForm   The object containing the data for the new com.cbrc.org.form.MRegionForm object
    * @exception   Exception   If the new com.cbrc.org.form.MRegionForm object cannot be created or persisted.
    */
   public static com.cbrc.org.form.MRegionForm create (com.cbrc.org.form.MRegionForm mRegionForm) throws Exception {
      com.cbrc.org.hibernate.MRegion mRegionPersistence = new com.cbrc.org.hibernate.MRegion ();
      TranslatorUtil.copyVoToPersistence(mRegionPersistence, mRegionForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: mRegionPersistence = (com.cbrc.org.hibernate.MRegion)session.save(mRegionPersistence);
session.save(mRegionPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionForm);
      return mRegionForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.org.form.MRegionForm object.
    *
    * @param   mRegionForm   The com.cbrc.org.form.MRegionForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.org.form.MRegionForm object cannot be updated/persisted.
    */
   public static com.cbrc.org.form.MRegionForm update (com.cbrc.org.form.MRegionForm mRegionForm) throws Exception {
      com.cbrc.org.hibernate.MRegion mRegionPersistence = new com.cbrc.org.hibernate.MRegion ();
      TranslatorUtil.copyVoToPersistence(mRegionPersistence, mRegionForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(mRegionPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionForm);
      return mRegionForm;
   }

   /**
    * Retrieve an existing com.cbrc.org.form.MRegionForm object for editing.
    *
    * @param   mRegionForm   The com.cbrc.org.form.MRegionForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MRegionForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MRegionForm edit (com.cbrc.org.form.MRegionForm mRegionForm) throws Exception {
      com.cbrc.org.hibernate.MRegion mRegionPersistence = new com.cbrc.org.hibernate.MRegion ();
      TranslatorUtil.copyVoToPersistence(mRegionPersistence, mRegionForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
mRegionPersistence = (com.cbrc.org.hibernate.MRegion)session.load(com.cbrc.org.hibernate.MRegion.class, mRegionPersistence.getRegionId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionForm);
      return mRegionForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MRegionForm object.
    *
    * @param   mRegionForm   The com.cbrc.org.form.MRegionForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MRegionForm object cannot be removed.
    */  
   public static void remove (com.cbrc.org.form.MRegionForm mRegionForm) throws Exception {
      com.cbrc.org.hibernate.MRegion mRegionPersistence = new com.cbrc.org.hibernate.MRegion ();
      TranslatorUtil.copyVoToPersistence(mRegionPersistence, mRegionForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
mRegionPersistence = (com.cbrc.org.hibernate.MRegion)session.load(com.cbrc.org.hibernate.MRegion.class, mRegionPersistence.getRegionId());
session.delete(mRegionPersistence);
tx.commit();
session.close();
   }

   /**
    * Retrieve all existing com.cbrc.org.form.MRegionForm objects.
    *
    * @exception   Exception   If the com.cbrc.org.form.MRegionForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MRegion"));
tx.commit();
session.close();
      ArrayList mRegion_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MRegionForm mRegionFormTemp = new com.cbrc.org.form.MRegionForm();
         com.cbrc.org.hibernate.MRegion mRegionPersistence = (com.cbrc.org.hibernate.MRegion)it.next();
         TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionFormTemp);
         mRegion_vos.add(mRegionFormTemp);
      }
      retVals = mRegion_vos;
      return retVals;
   }

   /**
    * Retrieve a set of existing com.cbrc.org.form.MRegionForm objects for editing.
    *
    * @param   mRegionForm   The com.cbrc.org.form.MRegionForm object containing the data used to retrieve the objects (i.e. the criteria for the retrieval).
    * @exception   Exception   If the com.cbrc.org.form.MRegionForm objects cannot be retrieved.
    */
   public static List select (com.cbrc.org.form.MRegionForm mRegionForm) throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MRegion as obj1 where obj1.regionTypId='" + mRegionForm.getRegionTypId() + "'"));
retVals.addAll(session.find("from com.cbrc.org.hibernate.MRegion as obj1 where obj1.regionName='" + mRegionForm.getRegionName() + "'"));
tx.commit();
session.close();
      ArrayList mRegion_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MRegionForm mRegionFormTemp = new com.cbrc.org.form.MRegionForm();
         com.cbrc.org.hibernate.MRegion mRegionPersistence = (com.cbrc.org.hibernate.MRegion)it.next();
         TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionFormTemp);
         mRegion_vos.add(mRegionFormTemp);
      }
      retVals = mRegion_vos;
      return retVals;
   }

}
