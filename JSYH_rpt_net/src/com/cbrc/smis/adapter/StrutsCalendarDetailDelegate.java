
package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.CalendarDetailForm;
import com.cbrc.smis.hibernate.CalendarDetail;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.util.FitechException;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for CalendarDetail data (i.e. 
 * com.cbrc.smis.form.CalendarDetailForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsCalendarDetailDelegate {

	private static FitechException log = new FitechException(StrutsCalendarDetailDelegate.class);
   /**
    * 插入日历记录
    * @author 姚捷
    * @param   calendarDetailForm  包含要插入的日历信息
    * @exception   Exception   If the new com.cbrc.smis.form.CalendarDetailForm object cannot be created or persisted.
    */
   public static boolean create (CalendarDetailForm calendarDetailForm) throws Exception {
	   boolean result=false;
	
       Connection conn = null;
       PreparedStatement pstmt = null;
       
       
       if(calendarDetailForm!=null)
        {
            Integer calYear = calendarDetailForm.getCalYear();
            Integer calMonth = calendarDetailForm.getCalMonth();
            //Integer calId = calendarDetailForm.getCalId();
               
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,calYear.intValue());
            calendar.set(Calendar.MONTH,calMonth.intValue()-1);
            calendar.set(Calendar.DATE,1);
            /**取得该月的天数*/
            int day_of_month = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);   
            /**取得工作日的日期集合*/
            Integer[] workDays = calendarDetailForm.getWorkDay();
            /**取得数据库连接*/    
            FitechConnection connectionFactory = new FitechConnection();
            conn = connectionFactory.getConn();
            /**插入的sql语句*/
            try
            {
            String sql = "insert into CALENDAR_DETAIL values(?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
               
                /**循环插入该月的记录*/
                for (int day = 1; day <= day_of_month; day++) 
                {
                    Integer calTypeId = Integer.valueOf("0");
                    for (int i = 0; i < workDays.length; i++) 
                    {
                        if (workDays[i].intValue() == day) 
                            calTypeId = Integer.valueOf("1");
                    }
                    /**年*/
                    pstmt.setInt(1, Integer.parseInt(String.valueOf(calYear)));
                    /**月*/
                    pstmt.setInt(2, Integer.parseInt(String.valueOf(calMonth)));
                    /**日*/
                    pstmt.setInt(3, day);
                    /**日历id*/
                    pstmt.setInt(4, Integer.parseInt(String.valueOf("1")));
                    /**日历类别id*/
                    pstmt.setInt(5, Integer.parseInt(String.valueOf(calTypeId)));
                    /**日期*/
                    String calDate = calYear.intValue() + "-" + calMonth.intValue()+ "-" + day;
                    pstmt.setDate(6, Date.valueOf(calDate));
                    /*// System.out.println("insert into CALENDAR_DETAIL values(" + calYear + "," + 
                    		calMonth + "," + day +",1," + calTypeId + ",'" + calDate +"')");*/
                    pstmt.execute();
                    conn.commit();
                    
                    result = true;
                }
            }
            catch(Exception e)
            {
                conn.rollback();
                result = false;
                log.printStackTrace(e);
            }
            finally
            {
               if(conn!=null)
                   connectionFactory.close();
            }
         }
        return result;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.smis.form.CalendarDetailForm object.
    *
    * @param   calendarDetailForm   The com.cbrc.smis.form.CalendarDetailForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.smis.form.CalendarDetailForm object cannot be updated/persisted.
    */
   public static com.cbrc.smis.form.CalendarDetailForm update (com.cbrc.smis.form.CalendarDetailForm calendarDetailForm) throws Exception {
      com.cbrc.smis.hibernate.CalendarDetail calendarDetailPersistence = new com.cbrc.smis.hibernate.CalendarDetail ();
      TranslatorUtil.copyVoToPersistence(calendarDetailPersistence, calendarDetailForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(calendarDetailPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(calendarDetailPersistence, calendarDetailForm);
      return calendarDetailForm;
   }

   /**
    * Retrieve an existing com.cbrc.smis.form.CalendarDetailForm object for editing.
    *
    * @param   calendarDetailForm   The com.cbrc.smis.form.CalendarDetailForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.smis.form.CalendarDetailForm object cannot be retrieved.
    */
   public static com.cbrc.smis.form.CalendarDetailForm edit (com.cbrc.smis.form.CalendarDetailForm calendarDetailForm) throws Exception {
      com.cbrc.smis.hibernate.CalendarDetail calendarDetailPersistence = new com.cbrc.smis.hibernate.CalendarDetail ();
      TranslatorUtil.copyVoToPersistence(calendarDetailPersistence, calendarDetailForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(calendarDetailPersistence, calendarDetailForm);
      return calendarDetailForm;
   }

   /**
    * 根据条件删除工作日历（年，月）
    *
    * @param   calendarDetailForm   包含查询条件
    * @exception   Exception   If the com.cbrc.smis.form.CalendarDetailForm object cannot be removed.
    */  
   public static boolean remove (com.cbrc.smis.form.CalendarDetailForm calendarDetailForm) throws Exception {
		boolean result=false;
		
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			StringBuffer hql = new StringBuffer("from CalendarDetail cal");
			if(calendarDetailForm!=null)
			{
				if(calendarDetailForm.getCalYear()!=null && 
						calendarDetailForm.getCalMonth()!=null)
				{
					hql.append(" where cal.comp_id.calYear="+calendarDetailForm.getCalYear()+
							" and cal.comp_id.calMonth="+calendarDetailForm.getCalMonth());
					session.delete(hql.toString());
					session.flush();
					result = true;
				}
			}
		} 
		catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null) conn.endTransaction(result);
		}	
		return result;
   }

   /**
    * Retrieve all existing com.cbrc.smis.form.CalendarDetailForm objects.
    *
    * @exception   Exception   If the com.cbrc.smis.form.CalendarDetailForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.smis.hibernate.CalendarDetail"));
tx.commit();
session.close();
      ArrayList calendarDetail_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.CalendarDetailForm calendarDetailFormTemp = new com.cbrc.smis.form.CalendarDetailForm();
         com.cbrc.smis.hibernate.CalendarDetail calendarDetailPersistence = (com.cbrc.smis.hibernate.CalendarDetail)it.next();
         TranslatorUtil.copyPersistenceToVo(calendarDetailPersistence, calendarDetailFormTemp);
         calendarDetail_vos.add(calendarDetailFormTemp);
      }
      retVals = calendarDetail_vos;
      return retVals;
   }
   /**
    * 根据 年,月,工作日历id 取得该月的所有工作日历明细记录数量
    *
    * @param   calendarDetailForm   包含查询条件:年,月,工作日历id
    * @exception   Exception   If the com.cbrc.smis.form.CalendarDetailForm objects cannot be retrieved.
    */
   public static int getRecordCount(com.cbrc.smis.form.CalendarDetailForm calendarDetailForm)throws Exception
   {
	   int result = 0;
	   DBConn conn = null;
	   Session session = null;	  
	   try{
	       if(calendarDetailForm!=null)
			 {
				 Integer calYear = calendarDetailForm.getCalYear();
				 Integer calMonth = calendarDetailForm.getCalMonth();
				 //Integer calId = calendarDetailForm.getCalId();
				 if(calYear != null && calMonth!=null)
				 {
                     conn = new DBConn();
                     session = conn.openSession();
                     StringBuffer hql = new StringBuffer("select count(*) from CalendarDetail cal");
					 hql.append(" where cal.comp_id.calYear="+calYear.intValue()+
							 	" and cal.comp_id.calMonth="+calMonth.intValue());
                     List list = session.find(hql.toString());
                     if(list!=null && list.size()!=0)
                     {
                         return Integer.parseInt(list.get(0).toString());
                     }  		 
				 }
			 }
		  }
           catch(HibernateException he)
           {
			  log.printStackTrace(he);
		  }
           finally
           {
			  if(conn!=null) conn.closeSession();		  
		  }
           return result;	   
   }
   /**
    * 根据 年,月,工作日历id 取得该月的所有工作日历明细记录
    *
    * @param   calendarDetailForm   包含查询条件:年,月,工作日历id
    * @exception   Exception   If the com.cbrc.smis.form.CalendarDetailForm objects cannot be retrieved.
    */
   public static List select (com.cbrc.smis.form.CalendarDetailForm calendarDetailForm) throws Exception {
	   List result = null;
		
	   DBConn conn = null;
	   Session session = null;	  
	   try{
	          if(calendarDetailForm!=null)
			 {
				 Integer calYear = calendarDetailForm.getCalYear();
				 Integer calMonth = calendarDetailForm.getCalMonth();
				 if(calYear != null && calMonth!=null)
				 {      
                     conn = new DBConn();
                     session = conn.openSession();
                     StringBuffer hql = new StringBuffer("from CalendarDetail cal where 1=1");
					 hql.append(" and cal.comp_id.calYear="+calYear.intValue()+
							 	" and cal.comp_id.calMonth="+calMonth.intValue());
                     List list = session.find(hql.toString());
                     
                     if(list!=null && list.size()!=0){
                         result =new ArrayList();  
                          //将查询到的记录存放入List中
                          for (Iterator it = list.iterator(); it.hasNext(); ) 
                          {
                             CalendarDetail calendarDetail = (CalendarDetail)it.next();
                             CalendarDetailForm calendarDetailFormTemp = new CalendarDetailForm();
                             TranslatorUtil.copyPersistenceToVo(calendarDetail,calendarDetailFormTemp);
                             result.add(calendarDetailFormTemp);
                          }
                      }       
				 }
			 }
		  }
           catch(Exception he)
          {
              result = null;
			  log.printStackTrace(he);
		  }
          finally
          {
			  if(conn!=null) conn.closeSession();		  
		  }
	      return result;
   }

   /**
    * This method will return all objects referenced by CalendarType
    */
   public static List getCalendarType(com.cbrc.smis.form.CalendarDetailForm calendarDetailForm) throws Exception {
      List retVals = new ArrayList();
      com.cbrc.smis.hibernate.CalendarDetail calendarDetailPersistence = null;
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
retVals.add(calendarDetailPersistence.getCalendarType());
      ArrayList CAL_TYPE_ID_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.CalendarTypeForm CAL_TYPE_ID_Temp = new com.cbrc.smis.form.CalendarTypeForm();
         com.cbrc.smis.hibernate.CalendarType CAL_TYPE_ID_PO = (com.cbrc.smis.hibernate.CalendarType)it.next();
         TranslatorUtil.copyPersistenceToVo(CAL_TYPE_ID_PO, CAL_TYPE_ID_Temp);
         CAL_TYPE_ID_vos.add(CAL_TYPE_ID_Temp);
      }
      retVals = CAL_TYPE_ID_vos;
      return retVals;
    }
   /**
    * This method will return all objects referenced by Calendar
    */
   public static List getCalendar(com.cbrc.smis.form.CalendarDetailForm calendarDetailForm) throws Exception {
      List retVals = new ArrayList();
      com.cbrc.smis.hibernate.CalendarDetail calendarDetailPersistence = null;
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
retVals.add(calendarDetailPersistence.getCalendar());
      ArrayList CAL_ID_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.CalendarForm CAL_ID_Temp = new com.cbrc.smis.form.CalendarForm();
         com.cbrc.smis.hibernate.Calendar CAL_ID_PO = (com.cbrc.smis.hibernate.Calendar)it.next();
         TranslatorUtil.copyPersistenceToVo(CAL_ID_PO, CAL_ID_Temp);
         CAL_ID_vos.add(CAL_ID_Temp);
      }
      retVals = CAL_ID_vos;
      return retVals;
    }
}
