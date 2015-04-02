package com.fitech.net.common;
/**
 *<p>标题：<p>
 *<p>描述：<p>
 *<p>日期：<p>
 *<p>作者：<p>
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
             throw new IllegalArgumentException("在CLASSPATH中未找到配置文件czbetl.properties");
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
             throw new IllegalArgumentException("未找到驱动程序");
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
             throw new RuntimeException("连接数据库失败");
         stmt = conn.createStatement();
     }
     catch(Exception e)
     {
         e.printStackTrace();
         throw new RuntimeException("连接数据库失败");
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
         throw new RuntimeException("关闭数据库连接时出错");
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
  *<p>标题：数值加减等运算的到最总值<p>
  *<p>描述：例如 aaa 返回 aaa <p>
  *<p>参数：<p>
  *<p>返回：<p>
  *<p>日期：<p>
  *<p>作者：<p>
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


