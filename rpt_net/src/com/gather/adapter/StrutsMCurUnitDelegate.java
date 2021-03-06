
package com.gather.adapter;

import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MCurUnit data (i.e. 
 * com.gather.struts.MCurUnitForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMCurUnitDelegate {


   /**
    * Create a new com.gather.struts.forms.MCurUnitForm object and persist (i.e. insert) it.
    *
    * @param   mCurUnitForm   The object containing the data for the new com.gather.struts.forms.MCurUnitForm object
    * @exception   Exception   If the new com.gather.struts.forms.MCurUnitForm object cannot be created or persisted.
    */
   public static boolean create (com.gather.struts.forms.MCurUnitForm mCurUnitForm) throws Exception {
      com.gather.hibernate.MCurUnit mCurUnitPersistence = new com.gather.hibernate.MCurUnit ();
      try{
      TranslatorUtil.copyVoToPersistence(mCurUnitPersistence, mCurUnitForm);
      DBConn conn=new DBConn();
      Session session=conn.beginTransaction();
      session.save(mCurUnitPersistence);
      conn.endTransaction(true);
      return true;
      }catch(Exception e)
     {
    	  new Log(StrutsMCurUnitDelegate.class).info(":::class:StrutsMCurUnitDelegate --  method: create �쳣��"+e.getMessage());
    	  throw e;
     }
   }
  
}
