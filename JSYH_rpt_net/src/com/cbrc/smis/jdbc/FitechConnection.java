package com.cbrc.smis.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
/**
 * ������Դ������
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class FitechConnection {
	private FitechException log=new FitechException(FitechConnection.class);
	
	/**
	 * DB2������ԴJNDI
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
    * ��ȡConnection
    * 
    * @return Connection �ɹ�������������Դ���ӵ�Connection���󣻷��򣬷���null
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
    * �ر�connection
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