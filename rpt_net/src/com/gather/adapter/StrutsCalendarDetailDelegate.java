
package com.gather.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.Session;

import com.gather.common.Config;
import com.gather.common.DateUtil;
import com.gather.common.Log;
import com.gather.dao.DBConn;
import com.gather.hibernate.CalendarDetail;
import com.gather.struts.forms.CalendarDetailForm;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for CalendarDetail data (i.e. 
 * com.gather.struts.CalendarDetailForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsCalendarDetailDelegate {
	

    /**
     * @author linfeng
     * @function 得到两个日期之间的所有数据
     * @param fromDate
     * @param toDate
     * @return CalendarDetailForm list
     */
	
	public static List findByTwoDays(Date fromDate,Date toDate){
		List list=new ArrayList();
		  if(fromDate==null || toDate==null) return null;
	  	  List listTemp=new ArrayList();
	  	  String sql="";
	  	  String strFromDate=DateUtil.date2String(fromDate).substring(0,10);
	  	  String strToDate=DateUtil.date2String(toDate).substring(0,10);
	  		  sql="from com.gather.hibernate.CalendarDetail as obj where "+
	  		       "obj.calendarType.calTypeId="+Config.WORK_TYPE_ID+" and "+
	  		       "obj.calDate>to_date('"+strFromDate+"','YYYY-MM-DD') and "+
	  		       "obj.calDate<to_date('"+strToDate+"','YYYY-MM-DD') "+
	  		       "order by obj.calDate";
	      // System.out.println("----findByTwoDays sql is: "+sql);
	      
	      DBConn conn=new DBConn();
	      Session session=null;
	      try{
	          session=conn.openSession();

	      	list.addAll(session.find(sql));
	     	 //// System.out.println("list.size() is: ======="+list.size());
			 for (int i=0;i<list.size();i++ ) {
				 CalendarDetailForm myForm = new CalendarDetailForm();
				 CalendarDetail myPersistence = (CalendarDetail)list.get(i);
			     TranslatorUtil.copyPersistenceToVo(myPersistence,myForm);
			     listTemp.add(myForm);
			   }
	      }catch(Exception e){
	      	e.printStackTrace();
	      	Log log=new Log(StrutsCalendarDetailDelegate.class);
	      	log.info("class:StrutsCalendarDetailDelegate 方法:findByTwoDays() 异常："+e.getMessage());
	      	}finally{
			 try{
			     session.close();
			}catch(Exception e){
			   e.printStackTrace();
			}
			}
	  	  return listTemp;
	}
	
	   /**
     * @author linfeng
     * @function 得到所有数据
     * @return CalendarDetailForm list
     */
	public static List findWorkDay(){
		List list=new ArrayList();
	  	  List listTemp=new ArrayList();
	  	  String sql="";
	  		  sql="from com.gather.hibernate.CalendarDetail as obj where "+
	  		       "obj.calendarType.calTypeId="+Config.WORK_TYPE_ID+" "+
	  		       "order by obj.calDate";
	      // System.out.println("----findWorkDay() sql is: "+sql);
	      
	      DBConn conn=new DBConn();
	      Session session=null;
	      try{
	          session=conn.openSession();

	      	list.addAll(session.find(sql));
	     	 // System.out.println("list.size() is: ======="+list.size());
			 for (int i=0;i<list.size();i++ ) {
				 CalendarDetailForm myForm = new CalendarDetailForm();
				 CalendarDetail myPersistence = (CalendarDetail)list.get(i);
			     TranslatorUtil.copyPersistenceToVo(myPersistence,myForm);
			     listTemp.add(myForm);
			   }
	      }catch(Exception e){
	      	e.printStackTrace();
	      	Log log=new Log(StrutsCalendarDetailDelegate.class);
	        log.info("class:StrutsCalendarDetailDelegate 方法findWorkDay()，异常："+e.getMessage());
	      	}finally{
			 try{
			     session.close();
			}catch(Exception e){
			   e.printStackTrace();
			}
			}
	  	  return listTemp;
	}


   /**
    * Create a new com.gather.struts.CalendarDetailForm object and persist (i.e. insert) it.
    *
    * @param   calendarDetailForm   The object containing the data for the new com.gather.struts.CalendarDetailForm object
    * @exception   Exception   If the new com.gather.struts.CalendarDetailForm object cannot be created or persisted.
    */
   public static boolean create (com.gather.struts.forms.CalendarDetailForm calendarDetailForm) throws Exception {
      com.gather.hibernate.CalendarDetail calendarDetailPersistence = new com.gather.hibernate.CalendarDetail ();
 try{
  TranslatorUtil.copyVoToPersistence(calendarDetailPersistence, calendarDetailForm);
  DBConn conn=new DBConn();
  Session session=conn.beginTransaction();
  session.save(calendarDetailPersistence);
  conn.endTransaction(true);
  return true;
 }catch(Exception e)
 {
	 throw e;
 }
}
}
