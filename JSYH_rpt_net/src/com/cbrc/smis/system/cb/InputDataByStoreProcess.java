package com.cbrc.smis.system.cb;

import java.sql.CallableStatement;
import java.sql.Connection;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;

/**
 * 该类用于调用存储过程
 * @author  
 *
 */
public class InputDataByStoreProcess {

	/**
	 * 该方法供入库成功后调用
	 * @param repInId   type:Integer 实际表单ID
	 * @throws Exception
	 */
	public static void conductDetail(Integer repInId) throws Exception {
		
		    DBConn  dBConn =  null;
		    
		    Session  session = null;
		    
		    Connection con = null;
		    
		    CallableStatement  stmt = null;
		    
		    String  sql = null;
		    
		    try{

		    	dBConn = new DBConn();
		    	
		    	session = dBConn.beginTransaction();
		    	
		    	con = session.connection();
		    	
		        sql = "{call ReportZDJS(?)}";  

			    stmt = con.prepareCall(sql);

			    stmt.setInt(1, repInId.intValue());

			    stmt.execute();

			    con.commit();

		    }catch(Exception e){
		    	
		    	con.rollback();
		    	
		        e.printStackTrace();
		    }
		    finally{
		    	
		    	if(stmt!=null)
		    		
		    		stmt.close();
		    	
		    	if(con!=null)
		    		
		    		con.close();
		    }
	}
	
	

}
