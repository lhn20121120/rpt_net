package com.fitech.net.common;
/**
 *<p>���⣺<p>
 *<p>������<p>
 *<p>���ڣ�<p>
 *<p>���ߣ�<p>
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.sybase.jdbc2.jdbc.SybDriver;
public class ETLDBManager
{
 private String driver;
 private String url;
 private String user;
 private String password;
 Connection conn;
 Statement stmt;
 PreparedStatement ps;
 private CallableStatement cs;
 public static DataSource dataSource = null;

 public ETLDBManager()
 {

     if(dataSource == null)
     {
         Properties prop = new Properties();
         try
         {
             prop.load(getClass().getClassLoader().getResourceAsStream("czbetl.properties"));
         }
         catch(Exception e)
         {
             throw new IllegalArgumentException("��CLASSPATH��δ�ҵ������ļ�czbetl.properties");
         }

         driver = prop.getProperty("database.driver");
         url = prop.getProperty("database.url");
         user = prop.getProperty("database.user");
         password = prop.getProperty("database.password");
         try
         {
   
             Class.forName(driver).getClass().toString();
         }
         catch(ClassNotFoundException e)
         {
        	 e.printStackTrace();
             throw new IllegalArgumentException("δ�ҵ���������");
         }
         dataSource = setupDataSource(driver, user, password, url);
     }
     connect();
 }

 public void connect()
 {
     try
     {
         BasicDataSource ds = (BasicDataSource)dataSource;
         conn = dataSource.getConnection();
         if(conn == null)
             throw new RuntimeException("�������ݿ�ʧ��");
         stmt = conn.createStatement();
     }
     catch(Exception e)
     {
         e.printStackTrace();
         throw new RuntimeException("�������ݿ�ʧ��");
     }
 }

 public DataSource setupDataSource(String sDrvName, String sUserName, String sPwd, String connectURI)
 {
     BasicDataSource ds = new BasicDataSource();
     ds.setDriverClassName(sDrvName);
     ds.setUsername(sUserName);
     ds.setPassword(sPwd);
     ds.setUrl(connectURI);
     ds.setMaxActive(50);
     ds.setMaxIdle(10);
     ds.setMaxWait(10000L);
     return ds;
 }

 public ResultSet executeQuery(String sql)
     throws SQLException
 {
     ResultSet rs = stmt.executeQuery(sql);
     return rs;
 }

 public void executeUpdate(String sql)
     throws SQLException
 {
     stmt.executeUpdate(sql);
 }

 public PreparedStatement prepareStatement(String sql)
     throws SQLException
 {
     ps = conn.prepareStatement(sql);
     return ps;
 }

 public void close()
 {
     BasicDataSource bdc = (BasicDataSource)dataSource;
     try
     {
         if(conn != null && !conn.isClosed())
             conn.close();
     }
     catch(Exception e)
     {
         throw new RuntimeException("�ر����ݿ�����ʱ����");
     }
 }

 public void setAutoCommit(boolean autoCommit)
     throws SQLException
 {
     conn.setAutoCommit(autoCommit);
 }

 public Savepoint setSavepoint()
     throws SQLException
 {
     return conn.setSavepoint();
 }

 public void rollback(Savepoint savepoint)
     throws SQLException
 {
     conn.rollback(savepoint);
 }

 public void rollback()
     throws SQLException
 {
     conn.rollback();
 }

 public void commit()
     throws SQLException
 {
     conn.commit();
 }

 public CallableStatement prepareCall(String sql)
     throws SQLException
 {
     cs = conn.prepareCall(sql);
     return cs;
 }
 /**
  *<p>���⣺��ֵ�Ӽ�������ĵ�����ֵ<p>
  *<p>���������� aaa ���� aaa <p>
  *<p>������<p>
  *<p>���أ�<p>
  *<p>���ڣ�<p>
  *<p>���ߣ�<p>
  */

 public static void main(String args[]){
	 ETLDBManager db=new ETLDBManager();
	 try {
		ResultSet rs=db.executeQuery("select cb.result, cb.expr_x from  cbrc_exc_inte cb");
		while(rs.next()){
			System.out.println(rs.getString(1));
		}
		db.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
}


