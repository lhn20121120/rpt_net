/*
 * Created on 2005-12-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;

/**
 * @author cb
 * 
 * 该类用于将一个清单式的报表数据入库
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StrutsListingTable {

    public static boolean insertTable(String sql) {

        boolean  isSuess = true;
        
        DBConn   dBConn = null;
        
        Session  session = null;
        
        Connection  con = null;
        
        try{
            
            dBConn = new DBConn();
            
            session = dBConn.beginTransaction();
            
            con = session.connection();
            
            PreparedStatement  stmt = con.prepareStatement(sql);
            
            stmt.executeUpdate();
            
            dBConn.endTransaction(true);
        }
        catch(Exception e){
            
            dBConn.endTransaction(false);
            
            isSuess = false;
        }
        
        return true;

    }
}