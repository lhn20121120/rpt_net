
package com.gather.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
import com.gather.hibernate.MDataRgType;
import com.gather.struts.forms.MDataRgTypeForm;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MDataRgType data (i.e. 
 * com.gather.struts.MDataRgTypeForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMDataRgTypeDelegate {

    /*
     * @author linfeng
     * @function 返回系统所有数据范围
     * @return List (MDataRgTypeForm)
     */
	  public static List findAll () {
	      List retVals = new ArrayList();
	      DBConn conn=new DBConn();
	      Session session=null;
	      try{
	    	session=conn.openSession();
	      String hsql="from com.gather.hibernate.MDataRgType as obj1";
	              retVals.addAll(session.find(hsql));
	              session.close();
	              ArrayList org_vos = new ArrayList();
	    	      if(retVals!=null && retVals.size()>0){
	    	    	  for (Iterator it = retVals.iterator(); it.hasNext(); ) {
	    		    	  MDataRgTypeForm MDataRgTemp = new MDataRgTypeForm();
	    		    	  MDataRgType MDataRgPersistence = (MDataRgType)it.next();
	    		         TranslatorUtil.copyPersistenceToVo(MDataRgPersistence, MDataRgTemp);
	    		         org_vos.add(MDataRgTemp);
	    		      }
	    	    	return   org_vos;
	    	      }
	      }catch(Exception e){
	    	  new Log(StrutsMDataRgTypeDelegate.class).info(":::class:StrutsMDataRgTypeDelegate --  method: findAll 异常："+e.getMessage());
	    	  e.printStackTrace();
	      }finally{
	    	  try{
	    		  session.close();
	    	  }catch(Exception e){e.printStackTrace();}
	      }
	     
	      
	      return null;
	   }
	
	
   /**
    * Create a new com.gather.struts.MDataRgTypeForm object and persist (i.e. insert) it.
    *
    * @param   mDataRgTypeForm   The object containing the data for the new com.gather.struts.MDataRgTypeForm object
    * @exception   Exception   If the new com.gather.struts.MDataRgTypeForm object cannot be created or persisted.
    */
   public static boolean  create (com.gather.struts.forms.MDataRgTypeForm mDataRgTypeForm) throws Exception {
	   com.gather.hibernate.MDataRgType mDataRgTypePersistence = new com.gather.hibernate.MDataRgType ();
	   try{
      TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence, mDataRgTypeForm);
      DBConn conn=new DBConn();
      Session session=conn.beginTransaction();
      session.save(mDataRgTypePersistence);
      conn.endTransaction(true);
      return true;
	   }catch(Exception e)
	   {
		   new Log(StrutsMDataRgTypeDelegate.class).info(":::class:StrutsMDataRgTypeDelegate --  method: create 异常："+e.getMessage()); 
		   throw e;
	   }
   }
}
