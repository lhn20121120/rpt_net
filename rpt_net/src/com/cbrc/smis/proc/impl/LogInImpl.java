package com.cbrc.smis.proc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.LogIn;
import com.cbrc.smis.hibernate.LogType;
/**
 * 将系统自动校验执行结果信息记录的操作类
 * 
 * @author rds
 * @serialDate 2005-12-21 16:07
 */
public class LogInImpl {
	/**
	 * 表间校验日志标识
	 */
	private static final String LOG_TYPE_ID_BJ_FLAG="LOG_SYSTEM_CHECKOUTINSIDEREPORTS";
	/**
	 * 表内校验日志标识
	 */
	private static final String LOG_TYPE_ID_BN_FLAG="LOG_SYSTEM_CHECKOUTOUTERREPORTS";
	/**
	 * 系统日志标识
	 */
	private static final String LOG_TYPE_ID_SYS_FLAG="LOG_SYSTEM_OTHER";
	
	private static final String  LOG_OPERATION_INSTITUTION ="LOG_OPERATION_INSTITUTION" ;
	
	/**
	 * 用户名
	 */
	private static String OPERATOR="SYSTEM";
	
	/**
	 * jdbc技术 oracle语法(nextval) 需要修改
	 * 卞以刚 2011-12-26
	 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
	 * 已增加数据库判断
	 * 记录执行情况
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　执行情况
	 * @param memo 备注
	 * @param lgoTypeFlag String 日志类别标识
	 * @return void 
	 * @exception Exception
	 */
	private static void writeLog(Connection conn,String operation,String memo,String logTypeFlag) throws Exception{
		if(logTypeFlag==null) return;
		String userName = OPERATOR;
		DBConn dbconn=null;
	    Session session=null;
	    boolean result = true;
		try{
			
			dbconn=new DBConn();
	    	session=dbconn.beginTransaction();
	    	LogIn logIn = new LogIn();
	    	logIn.setUserName(userName);
	    	logIn.setMemo(memo);
	    	logIn.setLogTime(new Date());
	    	logIn.setOperation(operation);
	    	LogType logType = null;
	    	String hql="from LogType e where e.logTypeFlag='"+logTypeFlag+"'";
	    	logType = (LogType)session.createQuery(hql).uniqueResult();
	    	logIn.setLogType(logType);
	    	session.save(logIn);
		}catch(HibernateException he){
			he.printStackTrace();
			result = false;
	     }finally{
	    	  if(dbconn!=null) dbconn.endTransaction(result);
	      }
	}
	
	/**
	 * 记录执行情况
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　执行情况
	 * @param memo 备注
	 * @param lgoTypeFlag String 日志类别标识
	 * @return void 
	 * @exception Exception
	 */
	public static void writeLog(Connection conn,String operation,String memo,String logTypeFlag,String userName) throws Exception{
		if(conn==null || logTypeFlag==null) return;
		DBConn dbconn=null;
	    Session session=null;
	    boolean result = true;
		try{
			dbconn=new DBConn();
	    	session=dbconn.beginTransaction();
	    	LogIn logIn = new LogIn();
	    	logIn.setUserName(userName);
	    	logIn.setMemo(memo);
	    	logIn.setLogTime(new Date());
	    	logIn.setOperation(operation);
	    	LogType logType = null;
	    	String hql="from LogType e where e.logTypeFlag='"+logTypeFlag+"'";
	    	logType = (LogType)session.createQuery(hql).uniqueResult();
	    	logIn.setLogType(logType);
	    	session.save(logIn);
		}catch(HibernateException he){
			he.printStackTrace();
			result = false;
	    }finally{
	    	  if(dbconn!=null) dbconn.endTransaction(result);
	    }
	}
	
	/**
	 * 将表间校验的执行情况写入日志
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　执行情况
	 * @return void 
	 */
	public static void writeBJLog(Connection conn,String operation) throws Exception{
		writeBJLog(conn,operation,"");
	}
	/**
	 * jdbc技术 oracle语法(nextval) 需要修改
	 * 卞以刚 2011-12-26
	 * 将表间校验的执行情况写入日志
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　String 执行情况
	 * @param memo String 备注
	 * @return void 
	 */
	public static void writeBJLog(Connection conn,String operation,String memo) throws Exception{
		writeLog(conn,operation,memo,LOG_TYPE_ID_BJ_FLAG);
	}
	
	/**
	 * 将表内校验的执行情况写入日志
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　执行情况
	 * @return void 
	 */
	public static void writeBNLog(Connection conn,String operation) throws Exception{
		writeBNLog(conn,operation,"");
	}
	/**
	 * jdbc技术 oracle语法(nextval) 需要修改
	 * 卞以刚 2011-12-26
	 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
	 * 已增加数据库判断
	 * 将表内校验的执行情况写入日志
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　String 执行情况
	 * @param memo String 备注
	 * @return void 
	 */
	public static void writeBNLog(Connection conn,String operation,String memo) throws Exception{
		writeLog(conn,operation,memo,LOG_TYPE_ID_BN_FLAG);
	}
	
	/**
	 * 写系统日志
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　String 执行情况
	 * @param memo String 备注
	 * @return void 
	 */
	public static void writeLog(Connection conn,String operation,String memo) throws Exception{
		writeLog(conn,operation,memo,LOG_TYPE_ID_SYS_FLAG);
	}
	
	/**
	 * 写系统日志
	 * 
	 * @param conn Connection　数据库连接
	 * @param operation　String 执行情况
	 * @return void 
	 */
	public static void writeLog(Connection conn,String operation) throws Exception{
		writeLog(conn,operation,LOG_TYPE_ID_SYS_FLAG);
	}
	
//	public static void writeInstitutionLog(Connection conn,String operation  ,String userName) throws Exception{
//		writeLog(conn,operation,null,LOG_OPERATION_INSTITUTION ,userName);
//	}
//	
	
}
