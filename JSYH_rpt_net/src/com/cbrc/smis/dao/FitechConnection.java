package com.cbrc.smis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 * ������Դ������
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class FitechConnection {
	//private FitechException log=new FitechException(FitechConnection.class);
	
	/**
	 * DB2������ԴJNDI
	 */
	private final String DS_DB2_JNDI="java:comp/env/jdbc/jndi_dc";
   
   /**
    * Connection
    */
	private Connection conn=null;
	
   /**
    * Datasource
    */
	private DataSource ds=null;
	
   /**
    * ��ȡConnection
    * 
    * @return Connection �ɹ�������������Դ���ӵ�Connection���󣻷��򣬷���null
    */
	public Connection getConn(){
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
    * �ر�connection
    * 
    * @return void 
    */	
	public void close(){
		if(this.conn!=null)
			try{
			  this.conn.close();	
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
	}
	
	private static final String DRIVER="com.ibm.db2.jcc.DB2Driver";
	private static final String URL="jdbc:db2://localhost:50000/CZB1104";
	private static final String USER="cj";
	private static final String PASS="aaa";

	public Connection getConnect(){
		Connection conn=null;
		
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
	
}