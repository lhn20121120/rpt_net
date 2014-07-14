
package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MVirOrgRlt data (i.e. 
 * com.cbrc.org.form.MVirOrgRltForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMVirOrgRltDelegate {


   /**
    * Create a new com.cbrc.org.form.MVirOrgRltForm object and persist (i.e. insert) it.
    *
    * @param   mVirOrgRltForm   The object containing the data for the new com.cbrc.org.form.MVirOrgRltForm object
    * @exception   Exception   If the new com.cbrc.org.form.MVirOrgRltForm object cannot be created or persisted.
    */
   public static com.cbrc.org.form.MVirOrgRltForm create (com.cbrc.org.form.MVirOrgRltForm mVirOrgRltForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgRlt mVirOrgRltPersistence = new com.cbrc.org.hibernate.MVirOrgRlt ();
      TranslatorUtil.copyVoToPersistence(mVirOrgRltPersistence, mVirOrgRltForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: mVirOrgRltPersistence = (com.cbrc.org.hibernate.MVirOrgRlt)session.save(mVirOrgRltPersistence);
session.save(mVirOrgRltPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mVirOrgRltPersistence, mVirOrgRltForm);
      return mVirOrgRltForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.org.form.MVirOrgRltForm object.
    *
    * @param   mVirOrgRltForm   The com.cbrc.org.form.MVirOrgRltForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgRltForm object cannot be updated/persisted.
    */
   public static com.cbrc.org.form.MVirOrgRltForm update (com.cbrc.org.form.MVirOrgRltForm mVirOrgRltForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgRlt mVirOrgRltPersistence = new com.cbrc.org.hibernate.MVirOrgRlt ();
      TranslatorUtil.copyVoToPersistence(mVirOrgRltPersistence, mVirOrgRltForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(mVirOrgRltPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mVirOrgRltPersistence, mVirOrgRltForm);
      return mVirOrgRltForm;
   }

   /**
    * Retrieve an existing com.cbrc.org.form.MVirOrgRltForm object for editing.
    *
    * @param   mVirOrgRltForm   The com.cbrc.org.form.MVirOrgRltForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgRltForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MVirOrgRltForm edit (com.cbrc.org.form.MVirOrgRltForm mVirOrgRltForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgRlt mVirOrgRltPersistence = new com.cbrc.org.hibernate.MVirOrgRlt ();
      TranslatorUtil.copyVoToPersistence(mVirOrgRltPersistence, mVirOrgRltForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mVirOrgRltPersistence, mVirOrgRltForm);
      return mVirOrgRltForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MVirOrgRltForm object.
    *
    * @param   mVirOrgRltForm   The com.cbrc.org.form.MVirOrgRltForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgRltForm object cannot be removed.
    */  
   public static void remove (com.cbrc.org.form.MVirOrgRltForm mVirOrgRltForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgRlt mVirOrgRltPersistence = new com.cbrc.org.hibernate.MVirOrgRlt ();
      TranslatorUtil.copyVoToPersistence(mVirOrgRltPersistence, mVirOrgRltForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
session.delete(mVirOrgRltPersistence);
tx.commit();
session.close();
   }

   /**
    * Retrieve all existing com.cbrc.org.form.MVirOrgRltForm objects.
    *
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgRltForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MVirOrgRlt"));
tx.commit();
session.close();
      ArrayList mVirOrgRlt_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MVirOrgRltForm mVirOrgRltFormTemp = new com.cbrc.org.form.MVirOrgRltForm();
         com.cbrc.org.hibernate.MVirOrgRlt mVirOrgRltPersistence = (com.cbrc.org.hibernate.MVirOrgRlt)it.next();
         TranslatorUtil.copyPersistenceToVo(mVirOrgRltPersistence, mVirOrgRltFormTemp);
         mVirOrgRlt_vos.add(mVirOrgRltFormTemp);
      }
      retVals = mVirOrgRlt_vos;
      return retVals;
   }

   /**
    * Retrieve a set of existing com.cbrc.org.form.MVirOrgRltForm objects for editing.
    *
    * @param   mVirOrgRltForm   The com.cbrc.org.form.MVirOrgRltForm object containing the data used to retrieve the objects (i.e. the criteria for the retrieval).
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgRltForm objects cannot be retrieved.
    */
   public static List select (com.cbrc.org.form.MVirOrgRltForm mVirOrgRltForm) throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MVirOrgRlt as obj1 where obj1.sumProp='" + mVirOrgRltForm.getSumProp() + "'"));
retVals.addAll(session.find("from com.cbrc.org.hibernate.MVirOrgRlt as obj1 where obj1.startDate='" + mVirOrgRltForm.getStartDate() + "'"));
retVals.addAll(session.find("from com.cbrc.org.hibernate.MVirOrgRlt as obj1 where obj1.endDate='" + mVirOrgRltForm.getEndDate() + "'"));
tx.commit();
session.close();
      ArrayList mVirOrgRlt_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MVirOrgRltForm mVirOrgRltFormTemp = new com.cbrc.org.form.MVirOrgRltForm();
         com.cbrc.org.hibernate.MVirOrgRlt mVirOrgRltPersistence = (com.cbrc.org.hibernate.MVirOrgRlt)it.next();
         TranslatorUtil.copyPersistenceToVo(mVirOrgRltPersistence, mVirOrgRltFormTemp);
         mVirOrgRlt_vos.add(mVirOrgRltFormTemp);
      }
      retVals = mVirOrgRlt_vos;
      return retVals;
   }

}
