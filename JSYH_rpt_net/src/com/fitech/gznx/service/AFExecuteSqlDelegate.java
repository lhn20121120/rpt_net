package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.common.StringUtil;

public class AFExecuteSqlDelegate {

	public static boolean setSql(String contents) {
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	boolean result=false;
		try {
			conn = new DBConn();
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	String[] insql = contents.split(";");
	    	for(String desql :insql){
	    		if(!StringUtil.isEmpty(desql))
	    			stmt.addBatch(desql);
	    	}
	    	if(insql!=null && insql.length>0){
	    		stmt.executeBatch();
	    	}	    		
	    	result=true;
		}  catch (Throwable e1) {
			result = false;
			e1.printStackTrace();
			
		}finally{
    		try {
				if(result == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(result);
				if(stmt != null) 
					stmt.close();

				if(connection != null)
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
    	}
		return result;
	}

}
