package com.cbrc.smis.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
/**
 * 与数据源连接类
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class FitechConnection {
	private FitechException log=new FitechException(FitechConnection.class);
	
	/**
	 * DB2的数据源JNDI
	 */
	//private final String DS_DB2_JNDI="java:comp/env/jdbc/cbrc_cj";
	//private final String DS_DB2_JNDI="jndi_test_cj";
   
   /**
    * Connection
    */
	private Connection conn=null;
	
   /**
    * Datasource
    */
	private DataSource ds=null;
	
   /**
    * DBConn dbConn
    */
	private DBConn dbConn=null;
	
   /**
    * 获取Connection
    * 
    * @return Connection 成功，返回与数据源连接的Connection对象；否则，返回null
    */
	public Connection getConn(){
	  /*Context ctx=null;
	  
      try{
        ctx=new InitialContext();
        ds=(DataSource)ctx.lookup(DS_DB2_JNDI);
        this.conn=ds.getConnection();
      }catch(NamingException ne){
      	log.printStackTrace(ne);
      }catch(SQLException sqle){
      	log.printStackTrace(sqle);
      }*/
      try{
    	  this.dbConn=new DBConn();
    	  this.conn=this.dbConn.openSession().connection();
      }catch(Exception e){
    	  log.printStackTrace(e);
      }
      
      return this.conn;
	}
	
   /**
    * 关闭connection
    * 
    * @return void 
    */	
	public void close(){
		if(this.conn!=null)
			try{
			  this.conn.close();
			  this.conn=null;
			}catch(SQLException sqle){
				log.printStackTrace(sqle);
			}
		if(this.dbConn!=null) {
			this.dbConn.closeSession();
			dbConn=null;
		}
	}
	
}