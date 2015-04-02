package com.cbrc.smis.proc.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.cbrc.smis.dao.DBConn;
/**
 * 与数据源连接类
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class FitechConnection {
	//private FitechException log=new FitechException(FitechConnection.class);
	
	/**
	 * DB2的数据源JNDI
	 */
	private final String DS_DB2_JNDI=""; //"java:comp/env/jdbc/jndi_smis";
	private static final String DRIVER=""; //"oracle.jdbc.driver.OracleDriver";
	private static final String URL=""; //"jdbc:oracle:thin:@192.168.0.89:1521:smis";
	private static final String USER=""; //"fitech";
	private static final String PASS=""; //aaa";
	DBConn dbconn=null;
	//0用jdbc 1用jndi  2hibernate
	public static  int CONN_TYPE=2;
   
   /**
    * Connection
    */
	private Connection conn=null;
	
   /**
    * Datasource
    */
	private DataSource ds=null;
	
   /**
    * 获取Connection
    * 
    * @return Connection 成功，返回与数据源连接的Connection对象；否则，返回null
    */
	public Connection getConn(){
		if(this.CONN_TYPE==0)return this.getConnect();
		if(this.CONN_TYPE==2)return this.getSessionConnect();
	  Context ctx=null;
	  
      try{
        ctx=new InitialContext();
        ds=(DataSource)ctx.lookup(DS_DB2_JNDI);
        this.conn=ds.getConnection();
      }catch(NamingException ne){
      	ne.printStackTrace();
      }catch(SQLException sqle){
      	sqle.printStackTrace();
      }
      
      return this.conn;
	}
	
   /**
    * 关闭connection
    * 
    * @return void 
    */	
	public void close(){
		if(this.dbconn!=null){
			dbconn.closeSession();
		}
		if(this.conn!=null)
			try{
				  this.conn.close();
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
	}
	

	
	public Connection getConnect(){
		if(this.CONN_TYPE==1)return this.getConn();
		if(this.CONN_TYPE==2)return this.getSessionConnect();
		try{
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASS);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	public Connection getSessionConnect(){
		if(this.CONN_TYPE==1)return this.getConn();
		if(this.CONN_TYPE==0)return this.getConnect();	
		try{
			dbconn=new DBConn();
			conn=dbconn.beginTransaction().connection();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
}