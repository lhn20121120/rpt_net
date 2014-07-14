
package com.gather.adapter;

import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for ValidateType data (i.e. 
 * com.gather.struts.ValidateTypeForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsValidateTypeDelegate {


   /**
    * Create a new com.gather.struts.ValidateTypeForm object and persist (i.e. insert) it.
    *
    * @param   validateTypeForm   The object containing the data for the new com.gather.struts.ValidateTypeForm object
    * @exception   Exception   If the new com.gather.struts.ValidateTypeForm object cannot be created or persisted.
    */
   public static boolean create (com.gather.struts.forms.ValidateTypeForm validateTypeForm) throws Exception {
      com.gather.hibernate.ValidateType validateTypePersistence = new com.gather.hibernate.ValidateType ();
      try{
      TranslatorUtil.copyVoToPersistence(validateTypePersistence, validateTypeForm);
      DBConn conn=new DBConn();
      Session session=conn.beginTransaction();
      session.save(validateTypePersistence);
      conn.endTransaction(true);
      return true;
      }catch(Exception e)
      {
    	  new Log(StrutsValidateTypeDelegate.class).info(":::class:StrutsValidateTypeDelegate --  method: create �쳣��"+e.getMessage());
    	  throw e;
      }
   }
}
