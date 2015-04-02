
package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MRegionTyp data (i.e. 
 * com.cbrc.org.form.MRegionTypForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMRegionTypDelegate {


   /**
    * Create a new com.cbrc.org.form.MRegionTypForm object and persist (i.e. insert) it.
    *
    * @param   mRegionTypForm   The object containing the data for the new com.cbrc.org.form.MRegionTypForm object
    * @exception   Exception   If the new com.cbrc.org.form.MRegionTypForm object cannot be created or persisted.
    */
   public static com.cbrc.org.form.MRegionTypForm create (com.cbrc.org.form.MRegionTypForm mRegionTypForm) throws Exception {
      com.cbrc.org.hibernate.MRegionTyp mRegionTypPersistence = new com.cbrc.org.hibernate.MRegionTyp ();
      TranslatorUtil.copyVoToPersistence(mRegionTypPersistence, mRegionTypForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: mRegionTypPersistence = (com.cbrc.org.hibernate.MRegionTyp)session.save(mRegionTypPersistence);
session.save(mRegionTypPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mRegionTypPersistence, mRegionTypForm);
      return mRegionTypForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.org.form.MRegionTypForm object.
    *
    * @param   mRegionTypForm   The com.cbrc.org.form.MRegionTypForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.org.form.MRegionTypForm object cannot be updated/persisted.
    */
   public static com.cbrc.org.form.MRegionTypForm update (com.cbrc.org.form.MRegionTypForm mRegionTypForm) throws Exception {
      com.cbrc.org.hibernate.MRegionTyp mRegionTypPersistence = new com.cbrc.org.hibernate.MRegionTyp ();
      TranslatorUtil.copyVoToPersistence(mRegionTypPersistence, mRegionTypForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(mRegionTypPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mRegionTypPersistence, mRegionTypForm);
      return mRegionTypForm;
   }

   /**
    * Retrieve an existing com.cbrc.org.form.MRegionTypForm object for editing.
    *
    * @param   mRegionTypForm   The com.cbrc.org.form.MRegionTypForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MRegionTypForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MRegionTypForm edit (com.cbrc.org.form.MRegionTypForm mRegionTypForm) throws Exception {
      com.cbrc.org.hibernate.MRegionTyp mRegionTypPersistence = new com.cbrc.org.hibernate.MRegionTyp ();
      TranslatorUtil.copyVoToPersistence(mRegionTypPersistence, mRegionTypForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
mRegionTypPersistence = (com.cbrc.org.hibernate.MRegionTyp)session.load(com.cbrc.org.hibernate.MRegionTyp.class, mRegionTypPersistence.getRegionTypId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mRegionTypPersistence, mRegionTypForm);
      return mRegionTypForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MRegionTypForm object.
    *
    * @param   mRegionTypForm   The com.cbrc.org.form.MRegionTypForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MRegionTypForm object cannot be removed.
    */  
   public static void remove (com.cbrc.org.form.MRegionTypForm mRegionTypForm) throws Exception {
      com.cbrc.org.hibernate.MRegionTyp mRegionTypPersistence = new com.cbrc.org.hibernate.MRegionTyp ();
      TranslatorUtil.copyVoToPersistence(mRegionTypPersistence, mRegionTypForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
mRegionTypPersistence = (com.cbrc.org.hibernate.MRegionTyp)session.load(com.cbrc.org.hibernate.MRegionTyp.class, mRegionTypPersistence.getRegionTypId());
session.delete(mRegionTypPersistence);
tx.commit();
session.close();
   }

   /**
    * Retrieve all existing com.cbrc.org.form.MRegionTypForm objects.
    *
    * @exception   Exception   If the com.cbrc.org.form.MRegionTypForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MRegionTyp"));
tx.commit();
session.close();
      ArrayList mRegionTyp_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MRegionTypForm mRegionTypFormTemp = new com.cbrc.org.form.MRegionTypForm();
         com.cbrc.org.hibernate.MRegionTyp mRegionTypPersistence = (com.cbrc.org.hibernate.MRegionTyp)it.next();
         TranslatorUtil.copyPersistenceToVo(mRegionTypPersistence, mRegionTypFormTemp);
         mRegionTyp_vos.add(mRegionTypFormTemp);
      }
      retVals = mRegionTyp_vos;
      return retVals;
   }

   /**
    * Retrieve a set of existing com.cbrc.org.form.MRegionTypForm objects for editing.
    *
    * @param   mRegionTypForm   The com.cbrc.org.form.MRegionTypForm object containing the data used to retrieve the objects (i.e. the criteria for the retrieval).
    * @exception   Exception   If the com.cbrc.org.form.MRegionTypForm objects cannot be retrieved.
    */
   public static List select (com.cbrc.org.form.MRegionTypForm mRegionTypForm) throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MRegionTyp as obj1 where obj1.regionTypNm='" + mRegionTypForm.getRegionTypNm() + "'"));
tx.commit();
session.close();
      ArrayList mRegionTyp_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MRegionTypForm mRegionTypFormTemp = new com.cbrc.org.form.MRegionTypForm();
         com.cbrc.org.hibernate.MRegionTyp mRegionTypPersistence = (com.cbrc.org.hibernate.MRegionTyp)it.next();
         TranslatorUtil.copyPersistenceToVo(mRegionTypPersistence, mRegionTypFormTemp);
         mRegionTyp_vos.add(mRegionTypFormTemp);
      }
      retVals = mRegionTyp_vos;
      return retVals;
   }

}