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
 * ��ϵͳ�Զ�У��ִ�н����Ϣ��¼�Ĳ�����
 * 
 * @author rds
 * @serialDate 2005-12-21 16:07
 */
public class LogInImpl {
	/**
	 * ���У����־��ʶ
	 */
	private static final String LOG_TYPE_ID_BJ_FLAG="LOG_SYSTEM_CHECKOUTINSIDEREPORTS";
	/**
	 * ����У����־��ʶ
	 */
	private static final String LOG_TYPE_ID_BN_FLAG="LOG_SYSTEM_CHECKOUTOUTERREPORTS";
	/**
	 * ϵͳ��־��ʶ
	 */
	private static final String LOG_TYPE_ID_SYS_FLAG="LOG_SYSTEM_OTHER";
	
	private static final String  LOG_OPERATION_INSTITUTION ="LOG_OPERATION_INSTITUTION" ;
	
	/**
	 * �û���
	 */
	private static String OPERATOR="SYSTEM";
	
	/**
	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
	 * ���Ը� 2011-12-26
	 * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
	 * ���������ݿ��ж�
	 * ��¼ִ�����
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��ִ�����
	 * @param memo ��ע
	 * @param lgoTypeFlag String ��־����ʶ
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
	 * ��¼ִ�����
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��ִ�����
	 * @param memo ��ע
	 * @param lgoTypeFlag String ��־����ʶ
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
	 * �����У���ִ�����д����־
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��ִ�����
	 * @return void 
	 */
	public static void writeBJLog(Connection conn,String operation) throws Exception{
		writeBJLog(conn,operation,"");
	}
	/**
	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
	 * ���Ը� 2011-12-26
	 * �����У���ִ�����д����־
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��String ִ�����
	 * @param memo String ��ע
	 * @return void 
	 */
	public static void writeBJLog(Connection conn,String operation,String memo) throws Exception{
		writeLog(conn,operation,memo,LOG_TYPE_ID_BJ_FLAG);
	}
	
	/**
	 * ������У���ִ�����д����־
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��ִ�����
	 * @return void 
	 */
	public static void writeBNLog(Connection conn,String operation) throws Exception{
		writeBNLog(conn,operation,"");
	}
	/**
	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
	 * ���Ը� 2011-12-26
	 * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
	 * ���������ݿ��ж�
	 * ������У���ִ�����д����־
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��String ִ�����
	 * @param memo String ��ע
	 * @return void 
	 */
	public static void writeBNLog(Connection conn,String operation,String memo) throws Exception{
		writeLog(conn,operation,memo,LOG_TYPE_ID_BN_FLAG);
	}
	
	/**
	 * дϵͳ��־
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��String ִ�����
	 * @param memo String ��ע
	 * @return void 
	 */
	public static void writeLog(Connection conn,String operation,String memo) throws Exception{
		writeLog(conn,operation,memo,LOG_TYPE_ID_SYS_FLAG);
	}
	
	/**
	 * дϵͳ��־
	 * 
	 * @param conn Connection�����ݿ�����
	 * @param operation��String ִ�����
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
