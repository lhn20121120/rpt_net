
package com.cbrc.smis.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.methods.GetMethod;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.LogInForm;
import com.cbrc.smis.hibernate.LogIn;
import com.cbrc.smis.util.FitechException;
import com.gather.adapter.StrutsLogDelegate;
import com.gather.hibernate.Log;
import com.cbrc.smis.hibernate.LogType;
/**
 * 编写者：姚捷
 * 功能：对日志表进行操作
 */
public class StrutsLogInDelegate {

	private static FitechException log = new FitechException(StrutsLogInDelegate.class);
	
   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：LogIn
    * 写日志
    * 
    * @author rds
    * @param   logInForm   The object containing the data for the new com.cbrc.smis.form.LogInForm object
    * @exception   Exception   If the new com.cbrc.smis.form.LogInForm object cannot be created or persisted.
    */
   public static boolean create (com.cbrc.smis.form.LogInForm logInForm) throws Exception {
      boolean result=false;
      
      if(logInForm==null) return result;
      
      DBConn conn=null;
      Session session=null;
      
      try{
    	  conn=new DBConn();
    	  session=conn.beginTransaction();
    	  LogIn logIn=new LogIn();
    	  TranslatorUtil.copyVoToPersistence(logIn,logInForm);
    	  /*// System.out.println("[StrutsLogInDelegate]logIn.getLogType().getLogTypeId:" + logIn.getLogType().getLogTypeId());*/
    	  session.save(logIn);
    	  
    	  result=true;
      }catch(HibernateException he){
    	 log.printStackTrace(he);
      }finally{
    	  if(conn!=null) conn.endTransaction(result);
      }
      
      return result;
   }

   /**
    * 已使用hibernate 姜明青2011-12-28
    * 影响对象：LogIn
    * 写日志
    * 
    * @author rds
    * @param   logInForm   The object containing the data for the new com.cbrc.smis.form.LogInForm object
    * @exception   Exception   If the new com.cbrc.smis.form.LogInForm object cannot be created or persisted.
    */
   public static boolean createLoginLog(int logType,String orgName,String operation,String memo){
	    boolean result=true;
	    DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		LogIn logIn=new LogIn();
		logIn.setLogInId(new Integer(getMaxId().intValue()+1));
		LogType myLogType=new LogType();
		myLogType.setLogTypeId(new Integer(logType));
		logIn.setLogType(myLogType);
		logIn.setLogTime(new Date());
		logIn.setUserName(orgName);
		logIn.setOperation(operation);
		logIn.setMemo(memo);
		try{
		    session.save(logIn);
		}catch(HibernateException he){
			log.printStackTrace(he);
			result = false;
		}finally{
	    	  if(conn!=null) conn.endTransaction(result);
	    }
		 return result;
	}
   
   /**
	 * @author linfeng
	 * @function 得到最大值
	 */
	
	public static Integer getMaxId(){
		DBConn conn=new DBConn();
		Session session=conn.openSession();
		String hsql="select max(obj.logInId) from LogIn as obj";
		List list=new ArrayList();
		Integer maxId=new Integer(0);
		try{
		     list.addAll(session.find(hsql));
		     if(list!=null && list.size()>0){
		    	 maxId=(Integer)list.get(0);
		     }
		}catch(Exception e){
			new com.gather.common.Log(StrutsLogDelegate.class).info(":::class:StrutsLogDelegate --  method: getMaxId 异常："+e.getMessage());
			e.printStackTrace();
		}finally{
			try{
				if(session!=null) session.close();
			}catch(Exception e){e.printStackTrace();}
		}
		maxId=maxId==null?new Integer(0):maxId;
		return maxId;
	}

   /**
    * Update (i.e. persist) an existing com.cbrc.smis.form.LogInForm object.
    *
    * @param   logInForm   The com.cbrc.smis.form.LogInForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm object cannot be updated/persisted.
    */
   public static com.cbrc.smis.form.LogInForm update (com.cbrc.smis.form.LogInForm logInForm) throws Exception {
      com.cbrc.smis.hibernate.LogIn logInPersistence = new com.cbrc.smis.hibernate.LogIn ();
      TranslatorUtil.copyVoToPersistence(logInPersistence, logInForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(logInPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(logInPersistence, logInForm);
      return logInForm;
   }

   /**
    * Retrieve an existing com.cbrc.smis.form.LogInForm object for editing.
    *
    * @param   logInForm   The com.cbrc.smis.form.LogInForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm object cannot be retrieved.
    */
   public static com.cbrc.smis.form.LogInForm edit (com.cbrc.smis.form.LogInForm logInForm) throws Exception {
      com.cbrc.smis.hibernate.LogIn logInPersistence = new com.cbrc.smis.hibernate.LogIn ();
      TranslatorUtil.copyVoToPersistence(logInPersistence, logInForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
logInPersistence = (com.cbrc.smis.hibernate.LogIn)session.load(com.cbrc.smis.hibernate.LogIn.class, logInPersistence.getLogInId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(logInPersistence, logInForm);
      return logInForm;
   }

   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：LogIn
    *删除日志
    *
    * @param   logInForm   包含要删除的日志的编号
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm object cannot be removed.
    */  
   public static boolean remove (com.cbrc.smis.form.LogInForm logInForm) throws Exception {
	   LogIn logInPersistence = null;
		boolean result=false;
		
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			Integer[] deleteLogIn = logInForm.getDeleteLogInId();
			for(int i=0;i<deleteLogIn.length;i++)
			{
				logInPersistence = (LogIn) session.load(LogIn.class, deleteLogIn[i]);
				session.delete(logInPersistence);
				session.flush();
				result = true;
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
    * Retrieve all existing com.cbrc.smis.form.LogInForm objects.
    *
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.smis.hibernate.LogIn"));
tx.commit();
session.close();
      ArrayList logIn_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.LogInForm logInFormTemp = new com.cbrc.smis.form.LogInForm();
         com.cbrc.smis.hibernate.LogIn logInPersistence = (com.cbrc.smis.hibernate.LogIn)it.next();
         TranslatorUtil.copyPersistenceToVo(logInPersistence, logInFormTemp);
         logIn_vos.add(logInFormTemp);
      }
      retVals = logIn_vos;
      return retVals;
   }

   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：LogIn
    * 206，210行将oracle语法to_date函数转化为sqlserver convert函数 待测试
    * 已增加数据库判断
    * 按条件查询日志记录，并返回List集合
    * Retrieve a set of existing com.cbrc.smis.form.LogInForm objects for editing.
    * @return List 包含查询到的所有记录	
    * @param   logInForm   包含查询的条件信息（操作员，日志内容，日志类型，日志时间范围）
    * @param  offset 偏移量
    * @param  limit 要取的记录条数
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm objects cannot be retrieved.
    */
   	public static List select (com.cbrc.smis.form.LogInForm logInForm,int offset,int limit)
   {
      List retVals = null;
		
	  DBConn conn=null;
	  Session session=null;
	  
	  try{
		  conn=new DBConn();
		  session=conn.openSession();
		  //;
		  StringBuffer hql = new StringBuffer("from LogIn l where 1=1 ");
		  StringBuffer where = new StringBuffer("");
		  if(logInForm!=null)
		  {
			  //依据传过来的参数条件，增加相应的查询条件
			  //操作员
			  if(logInForm.getUserName()!=null && !logInForm.getUserName().equals(""))
			  	where.append(" and l.userName='"+logInForm.getUserName()+"'");
			  //日志内容
			  if(logInForm.getOperation()!=null && !logInForm.getOperation().equals(""))
			  	where.append(" and l.operation like '%"+logInForm.getOperation()+"%'");
			  //日志类型
			  if(logInForm.getLogTypeId()!=null && logInForm.getLogTypeId().intValue()!=0)
				  where.append(" and l.logType.logTypeId="+logInForm.getLogTypeId());
			  //日志时间范围
			  String startDate = logInForm.getStartDate();
			  String endDate = logInForm.getEndDate();
			  if(startDate!=null && !startDate.equals(""))
			  {
				  /**此处将oracle语法to_date函数转化为sqlserver convert函数
				   * 已增加数据库判断*/
				  if(Config.DB_SERVER_TYPE.equals("oracle"))
				  {
					  where.append( " and l.logTime>=to_date('" +startDate+"','YYYY-MM-DD HH24:MI:SS')");
				  }
				  if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					  where.append( " and l.logTime>=convert(datetime,'" +startDate+"',120)");
				  if(Config.DB_SERVER_TYPE.equals("db2"))
					  where.append( " and l.logTime>='" +startDate+"'");
			  }
			  if(endDate!=null && !endDate.equals(""))
			  {
				  /**此处将oracle语法to_date函数转化为sqlserver convert函数
				   * 已增加数据库判断*/
				  if(Config.DB_SERVER_TYPE.equals("oracle"))
					  where.append( " and l.logTime<=to_date('" +endDate+"','YYYY-MM-DD HH24:MI:SS')");
				  if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					  where.append( " and l.logTime<=convert(datetime,'" +endDate+"',120)");
				  if(Config.DB_SERVER_TYPE.equals("db2"))
					  where.append( " and l.logTime<='" +endDate+"'");
			  }
		  }
		  hql.append(where.toString());
		  //根据条件取相应的记录条数
		  Query query = session.createQuery(hql.toString()+" order by l.logTime desc");
		  query.setFirstResult(offset);
		  query.setMaxResults(limit);
		  
		  retVals=query.list();
		  
		  if(retVals!=null){
			  ArrayList logIn_vos = new ArrayList();
			  //将查询到的记录存放入List中
		      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
		         LogInForm logInFormTemp = new LogInForm();
		         LogIn logInPersistence = (LogIn)it.next();
		         TranslatorUtil.copyPersistenceToVo(logInPersistence, logInFormTemp);
		         logIn_vos.add(logInFormTemp);
		      }
		      retVals = logIn_vos;
		  }	      
	  }catch(HibernateException he){
		  log.printStackTrace(he);
	  }
	  catch(Exception e){
		  log.printStackTrace(e);
	  }
	  finally{
		  if(conn!=null) conn.closeSession();		  
	  }
      return retVals;
   }
   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：LogIn
    * 行277，281将oracle语法to_date函数转化为sqlserver convert函数 待测试
    * 已增加数据库判断
    * 取得按条件查询到的记录条数
    * Retrieve a set of existing com.cbrc.smis.form.LogInForm objects for editing.
    * @return int 查询到的记录条数	
    * @param   logInForm   包含查询的条件信息（操作员，日志内容，日志类型，日志时间范围）
    */
   
   public static int getRecordCount(LogInForm logInForm)
   {
	   /*return select(logInForm,0,0).size(); */
           int recordCount = 0;
	      List retVals = null;
			
		  DBConn conn=null;
		  Session session=null;
		  
		  try{
			  conn=new DBConn();
			  session=conn.openSession();
			  StringBuffer hql = new StringBuffer("select count(*)from LogIn l where 1=1 ");
			  StringBuffer where = new StringBuffer("");
			  if(logInForm!=null)
			  {
				  //依据传过来的参数条件，增加相应的查询条件
				  //操作员
				  if(logInForm.getUserName()!=null && !logInForm.getUserName().equals(""))
				  	where.append(" and l.userName='"+logInForm.getUserName()+"'");
				  //日志内容
				  if(logInForm.getOperation()!=null && !logInForm.getOperation().equals(""))
				  	where.append(" and l.operation like '%"+logInForm.getOperation()+"%'");
				  //日志类型
				  if(logInForm.getLogTypeId()!=null && logInForm.getLogTypeId().intValue()!=0)
					  where.append(" and l.logType.logTypeId="+logInForm.getLogTypeId());
				  //日志时间范围
				  String startDate = logInForm.getStartDate();
				  String endDate = logInForm.getEndDate();
				  if(startDate!=null && !startDate.equals(""))
				  {
					  /**此处将oracle语法to_date函数转化为sqlserver convert函数
					   * 已增加数据库判断*/
					  if(Config.DB_SERVER_TYPE.equals("oracle"))
					  {
						  where.append( " and l.logTime>=to_date('" +startDate+"','YYYY-MM-DD HH24:MI:SS')");
					  }
					  if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						  where.append( " and l.logTime>=convert(datetime,'"+startDate+"',120)");
					  if(Config.DB_SERVER_TYPE.equals("db2"))
						  where.append( " and l.logTime>='" +startDate+"'");
				  }
				  if(endDate!=null && !endDate.equals(""))
				  {
					  /**此处将oracle语法to_date函数转化为sqlserver convert函数
					   * 已增加数据库判断*/
					  
					  if(Config.DB_SERVER_TYPE.equals("oracle"))
					  {
						  where.append( " and l.logTime<=to_date('" +endDate+"','YYYY-MM-DD HH24:MI:SS')");
					  }
					  if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						  where.append( " and l.logTime<=convert(datetime,'" +endDate+"',120)");
					  if(Config.DB_SERVER_TYPE.equals("db2"))
						  where.append( " and l.logTime>='" +startDate+"'");
				  }
			
			  }
			  hql.append(where.toString());
			  //根据条件取相应的记录条数

			  //Query query = session.createQuery(hql.toString()+" order by l.logTime desc");
			  Query query = session.createQuery(hql.toString());

			  retVals=query.list();
			  
			  if(retVals!=null){
                  recordCount =  Integer.parseInt(retVals.get(0).toString());
			  }	      
		  }catch(HibernateException he){
			  log.printStackTrace(he);
		  }
		  catch(Exception e){
			  log.printStackTrace(e);
		  }
		  finally{
			  if(conn!=null) conn.closeSession();		  
		  }
	      return recordCount;
   }
   /**
    * 获得该天生成仓库文件日志的记录数
    * 
    * @author 姚捷
    * @return 当天生成仓库文件的日志记录数
    */
   public static int getDb2XmlLogCount()
   {
       int recordCount = 0;
          List retVals = null;
            
          DBConn conn=null;
          Session session=null;
          
          try{
              conn=new DBConn();
              session=conn.openSession();
              StringBuffer hql = new StringBuffer("select count(*)from LogIn l where 1=1 ");
              StringBuffer where = new StringBuffer("");
              //日志类型
              where.append(" and l.logType.logTypeId="+Config.LOG_SYSTEM_CREATESTORAGEXML);
              //日志时间范围
              Date today = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
              where.append( " and l.logTime>='" +dateFormat.format(today)+" 00:00:00'");
              where.append( " and l.logTime<='" +dateFormat.format(today)+" 23:59:59'"); 
              
              hql.append(where.toString());
              //根据条件取相应的记录条数
              Query query = session.createQuery(hql.toString());
              
              retVals=query.list();
              
              if(retVals!=null){
               recordCount =  Integer.parseInt(retVals.get(0).toString());
              }       
          }catch(HibernateException he){
              log.printStackTrace(he);
          }
          catch(Exception e){
              log.printStackTrace(e);
          }
          finally{
              if(conn!=null) conn.closeSession();         
          }
          return recordCount;
       
   }
   /**
    * 取得生成仓库文件的日志
    *
    *@author 姚捷 
    * @return List 包含查询到的所有记录   
    * @param  offset 偏移量
    * @param  limit 要取的记录条数
    * @exception   Exception   If the com.cbrc.smis.form.LogInForm objects cannot be retrieved.
    */
    public static List getDb2XmlLog(int offset,int limit)
   {
      List retVals = null;
        
      DBConn conn=null;
      Session session=null;
      
      try{
          conn=new DBConn();
          session=conn.openSession();

          StringBuffer hql = new StringBuffer("from LogIn l where 1=1 ");
          StringBuffer where = new StringBuffer("");
         
          where.append(" and l.logType.logTypeId="+Config.LOG_SYSTEM_CREATESTORAGEXML);
          //日志时间范围
          Date today = new Date();
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          where.append( " and l.logTime>='" +dateFormat.format(today)+" 00:00:00'");
          where.append( " and l.logTime<='" +dateFormat.format(today)+" 23:59:59'"); 
          hql.append(where.toString());
          //根据条件取相应的记录条数
          Query query = session.createQuery(hql.toString()+" order by l.logTime desc");
          query.setFirstResult(offset);
          query.setMaxResults(limit);
          
          retVals=query.list();
          
          if(retVals!=null){
              ArrayList logIn_vos = new ArrayList();
              //将查询到的记录存放入List中
              for (Iterator it = retVals.iterator(); it.hasNext(); ) {
                 LogInForm logInFormTemp = new LogInForm();
                 LogIn logInPersistence = (LogIn)it.next();
                 TranslatorUtil.copyPersistenceToVo(logInPersistence, logInFormTemp);
                 logIn_vos.add(logInFormTemp);
              }
              retVals = logIn_vos;
          }       
      }catch(HibernateException he){
          log.printStackTrace(he);
      }
      catch(Exception e){
          log.printStackTrace(e);
      }
      finally{
          if(conn!=null) conn.closeSession();         
      }
      return retVals;
   }
   /**
    * This method will return all objects referenced by LogType
    */
   public static List getLogType(com.cbrc.smis.form.LogInForm logInForm) throws Exception {
      List retVals = new ArrayList();
      com.cbrc.smis.hibernate.LogIn logInPersistence = null;
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
logInPersistence = (com.cbrc.smis.hibernate.LogIn)session.load(com.cbrc.smis.hibernate.LogIn.class, logInPersistence.getLogInId());
tx.commit();
session.close();
retVals.add(logInPersistence.getLogType());
      ArrayList LOG_TYPE_ID_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.LogTypeForm LOG_TYPE_ID_Temp = new com.cbrc.smis.form.LogTypeForm();
         com.cbrc.smis.hibernate.LogType LOG_TYPE_ID_PO = (com.cbrc.smis.hibernate.LogType)it.next();
         TranslatorUtil.copyPersistenceToVo(LOG_TYPE_ID_PO, LOG_TYPE_ID_Temp);
         LOG_TYPE_ID_vos.add(LOG_TYPE_ID_Temp);
      }
      retVals = LOG_TYPE_ID_vos;
      return retVals;
    }
}
