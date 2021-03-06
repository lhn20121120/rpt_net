/** 姬怀宝
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * @author <strong>Generated by Middlegen.</strong>
	 */
package com.gather.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
import com.gather.hibernate.LogType;

public class StrutsLogType {
	public static List findAll () throws Exception {
	   	List result = null;
		
		  DBConn conn = null;
		  Session session = null;	  
		  try{
			  conn = new DBConn();
			  session = conn.openSession();
			  result = session.find("from LogType");
			  if(result!=null){
				  //// System.out.println("Search result: "+retVals.size());
				  ArrayList logType_vos = new ArrayList();
				  //将查询到的记录存放入List中
			      for (Iterator it = result.iterator(); it.hasNext(); ) {
			         LogType logTypePersistence = (LogType)it.next();
			         logType_vos.add(logTypePersistence);
			      }
			      result = logType_vos;
			  }	      
		  }catch(HibernateException he){
			  new Log(StrutsLogType.class).info(":::class:StrutsLogType --  method: findAll 异常："+he.getMessage());
			  he.printStackTrace();
		  }finally{
			  if(conn!=null) conn.closeSession();		  
		  }
	      return result;
   }
}
