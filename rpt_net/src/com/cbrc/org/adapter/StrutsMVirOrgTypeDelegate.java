
package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MVirOrgType data (i.e. 
 * com.cbrc.org.form.MVirOrgTypeForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMVirOrgTypeDelegate {


   /**
    * Create a new com.cbrc.org.form.MVirOrgTypeForm object and persist (i.e. insert) it.
    *
    * @param   mVirOrgTypeForm   The object containing the data for the new com.cbrc.org.form.MVirOrgTypeForm object
    * @exception   Exception   If the new com.cbrc.org.form.MVirOrgTypeForm object cannot be created or persisted.
    */
   public static com.cbrc.org.form.MVirOrgTypeForm create (com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgType mVirOrgTypePersistence = new com.cbrc.org.hibernate.MVirOrgType ();
      TranslatorUtil.copyVoToPersistence(mVirOrgTypePersistence, mVirOrgTypeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: mVirOrgTypePersistence = (com.cbrc.org.hibernate.MVirOrgType)session.save(mVirOrgTypePersistence);
session.save(mVirOrgTypePersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mVirOrgTypePersistence, mVirOrgTypeForm);
      return mVirOrgTypeForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.org.form.MVirOrgTypeForm object.
    *
    * @param   mVirOrgTypeForm   The com.cbrc.org.form.MVirOrgTypeForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgTypeForm object cannot be updated/persisted.
    */
   public static com.cbrc.org.form.MVirOrgTypeForm update (com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgType mVirOrgTypePersistence = new com.cbrc.org.hibernate.MVirOrgType ();
      TranslatorUtil.copyVoToPersistence(mVirOrgTypePersistence, mVirOrgTypeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(mVirOrgTypePersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mVirOrgTypePersistence, mVirOrgTypeForm);
      return mVirOrgTypeForm;
   }

   /**
    * Retrieve an existing com.cbrc.org.form.MVirOrgTypeForm object for editing.
    *
    * @param   mVirOrgTypeForm   The com.cbrc.org.form.MVirOrgTypeForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgTypeForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MVirOrgTypeForm edit (com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgType mVirOrgTypePersistence = new com.cbrc.org.hibernate.MVirOrgType ();
      TranslatorUtil.copyVoToPersistence(mVirOrgTypePersistence, mVirOrgTypeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
mVirOrgTypePersistence = (com.cbrc.org.hibernate.MVirOrgType)session.load(com.cbrc.org.hibernate.MVirOrgType.class, mVirOrgTypePersistence.getVirtuTypeId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mVirOrgTypePersistence, mVirOrgTypeForm);
      return mVirOrgTypeForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MVirOrgTypeForm object.
    *
    * @param   mVirOrgTypeForm   The com.cbrc.org.form.MVirOrgTypeForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgTypeForm object cannot be removed.
    */  
   public static void remove (com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeForm) throws Exception {
      com.cbrc.org.hibernate.MVirOrgType mVirOrgTypePersistence = new com.cbrc.org.hibernate.MVirOrgType ();
      TranslatorUtil.copyVoToPersistence(mVirOrgTypePersistence, mVirOrgTypeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
mVirOrgTypePersistence = (com.cbrc.org.hibernate.MVirOrgType)session.load(com.cbrc.org.hibernate.MVirOrgType.class, mVirOrgTypePersistence.getVirtuTypeId());
session.delete(mVirOrgTypePersistence);
tx.commit();
session.close();
   }

   /**
    * Retrieve all existing com.cbrc.org.form.MVirOrgTypeForm objects.
    *
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgTypeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MVirOrgType"));
tx.commit();
session.close();
      ArrayList mVirOrgType_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeFormTemp = new com.cbrc.org.form.MVirOrgTypeForm();
         com.cbrc.org.hibernate.MVirOrgType mVirOrgTypePersistence = (com.cbrc.org.hibernate.MVirOrgType)it.next();
         TranslatorUtil.copyPersistenceToVo(mVirOrgTypePersistence, mVirOrgTypeFormTemp);
         mVirOrgType_vos.add(mVirOrgTypeFormTemp);
      }
      retVals = mVirOrgType_vos;
      return retVals;
   }

   /**
    * Retrieve a set of existing com.cbrc.org.form.MVirOrgTypeForm objects for editing.
    *
    * @param   mVirOrgTypeForm   The com.cbrc.org.form.MVirOrgTypeForm object containing the data used to retrieve the objects (i.e. the criteria for the retrieval).
    * @exception   Exception   If the com.cbrc.org.form.MVirOrgTypeForm objects cannot be retrieved.
    */
   public static List select (com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeForm) throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.org.hibernate.MVirOrgType as obj1 where obj1.virtuTypeNm='" + mVirOrgTypeForm.getVirtuTypeNm() + "'"));
tx.commit();
session.close();
      ArrayList mVirOrgType_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.org.form.MVirOrgTypeForm mVirOrgTypeFormTemp = new com.cbrc.org.form.MVirOrgTypeForm();
         com.cbrc.org.hibernate.MVirOrgType mVirOrgTypePersistence = (com.cbrc.org.hibernate.MVirOrgType)it.next();
         TranslatorUtil.copyPersistenceToVo(mVirOrgTypePersistence, mVirOrgTypeFormTemp);
         mVirOrgType_vos.add(mVirOrgTypeFormTemp);
      }
      retVals = mVirOrgType_vos;
      return retVals;
   }

}
